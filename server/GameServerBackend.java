package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import poker.Action;
import poker.GameModel;

public final class GameServerBackend implements Runnable {

    private final static int PORT = 25888;
    
    private final Model m;
    
    private final BlockingQueue<Task> taskQueue;
    
    private volatile ServerSocket serverSocket;
    private final Map<Integer, Socket> openSockets;
    
    private volatile boolean running;
    private volatile Thread modelThread;
    
    public GameServerBackend(GameModel gm, GameServerModel gsm) {
        if (gm == null || gsm == null) {
            throw new NullPointerException();
        }
        m = new Model();
        m.setGameModel(gm);
        m.setGameServerModel(gsm);
        taskQueue = new LinkedBlockingQueue<>();
        serverSocket = null;
        openSockets = Collections.synchronizedMap(new HashMap<Integer, Socket>());
        running = false;
        modelThread = null;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    @Override
    public void run() {
        running = true;
        
        // Open serverSocket to listen on PORT
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            running = false;
            serverSocket = null;
        }
        
        modelThread = new Thread(new Runnable() {
            public void run() {
                while (running || !taskQueue.isEmpty()) {
                    Task task;
                    try {
                        task = taskQueue.take();
                    } catch (InterruptedException e) {
                        continue;
                    }
                    try {
                        dispatchBroadcast(task.getBroadcast());
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
                
                try {
                    if (serverSocket != null && !serverSocket.isClosed()) {
                        serverSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "Model thread");
        modelThread.start();
        
        ExecutorService workerPool = Executors.newCachedThreadPool();
        try {
            int nextId = 0;
            while (running && !serverSocket.isClosed()) {
                int clientId = nextId++;
                Socket clientSocket = serverSocket.accept();
                openSockets.put(clientId, clientSocket);
                taskQueue.add(new Registration(clientId));
                workerPool.execute(new ConnectionWorker(clientId, clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            running = false;
            workerPool.shutdown();
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                serverSocket = null;
            }
        }
        
        synchronized (openSockets) {
            Iterator<Socket> sit = openSockets.values().iterator();
            while (sit.hasNext()) {
                Socket clientSocket = sit.next();
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    sit.remove();
                }
            }
        }
    }
    
    public void stop() {
        System.out.println("stopping");
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (modelThread != null) {
            modelThread.interrupt();
        }
    }

    private void dispatchBroadcast(Broadcast broadcast) {
        if (broadcast == null) {
            return;
        }
        
        Map<Integer, List<String>> responses = broadcast.getResponses();
        for (int id : responses.keySet()) {
            try {
                Socket clientSocket = openSockets.get(id);
                PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
                for (String response : responses.get(id)) {
                    pw.println(response);
                    pw.flush();
                    System.out.printf("Response sent to user %d: \"%s\"\n",
                            id, response);
                }
                pw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private final class ConnectionWorker implements Runnable {

        private final int id;
        private final Socket clientSocket;
        BufferedReader reader = null;
        PrintWriter writer = null;
        
        public ConnectionWorker(int id, Socket socket) {
            this.id = id;
            this.clientSocket = socket;
        }
        
        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            try {
                while (running && !clientSocket.isClosed()) {
                    String line = reader.readLine();
                    if (line != null) {
                        if (line.matches("\\d req gmc")) {
                            writer.print(m.getGameModel().toString());
                            System.out.println(m.getGameModel().toString());
                        }
                        System.out.printf("Request received from user %d: " +
                                "\"%s\"\n", id, line);
                        String payload;
                        if (line.startsWith(":")) {
                            int index = line.indexOf(' ');
                            payload = line.substring(index + 1);
                        } else {
                            payload = line;
                        }
                        Request request = new Request(id, payload);
                        taskQueue.add(request);
                    } else {
                        clientSocket.close();
                        taskQueue.add(new Disconnection(id));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        
    }
    
    private interface Task {
        Broadcast getBroadcast();
    }

    /**
     * Represents a client's connection to the server.
     */
    private final class Registration implements Task {
        private final int userId;

        public Registration(int userId) {
            this.userId = userId;
        }

        @Override
        public Broadcast getBroadcast() {
            return m.getGameServerModel().registerUser(userId);
        }
    }

    /**
     * Represents a client's disconnection from the server.
     */
    private final class Disconnection implements Task {
        private final int userId;

        public Disconnection(int userId) {
            this.userId = userId;
        }

        @Override
        public Broadcast getBroadcast() {
            return m.getGameServerModel().deregisterUser(userId);
        }
    }

    /**
     * Represents an incoming command from a connected client.
     */
    private final class Request implements Task {
        private final int userId;
        private final String payload;

        public Request(int userId, String payload) {
            this.userId = userId;
            this.payload = payload;
        }

        @Override
        public Broadcast getBroadcast() {
            Action action = Action.stringToAction(userId + " " + payload);
            return null;
        }
    }
    
}
