package server;

import java.io.IOException;
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
        running = false;
        if (serverSocket != null && serverSocket.isClosed()) {
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
        
        Map<Integer, List<String>> serverResponses = broadcast.getResponses();
    }
    
}
