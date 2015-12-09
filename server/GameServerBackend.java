package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

import poker.Card;
import poker.GameModel;
import poker.HumanPlayer;
import poker.Player;

public final class GameServerBackend implements Runnable {

    public final static int PORT = 25888;    
    private final GameModel gm;    
    private int ready = 0;    
    private volatile ServerSocket serverSocket;
    
    private volatile boolean running;
    
    public GameServerBackend(GameModel gm) {
        if (gm == null) {
            throw new NullPointerException();
        }
        this.gm = gm;
        serverSocket = null;
        running = false;
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
        
        while(true) {
            ClientWorker w;
            try {
                // Accept a new connection
                w = new ClientWorker(serverSocket.accept());
                // Create a thread to deal with this client
                Thread t = new Thread(w);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    public void stop() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    /**
     * A ClientWorker is created for every connection to the server. Each
     * ClientWorker handles the communication between that particular client
     * and the server.
     */
    class ClientWorker implements Runnable {
        private Socket client;
        
        ClientWorker(Socket client) {
            this.client = client;
        }
        
        @Override
        public void run() {
            String line;
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    if ((line = in.readLine()) != null) {
                        //System.out.println("Request: " + line);
                        int id = Integer.parseInt(line.substring(0, line.indexOf(' ')));
                        if (line.matches("-1 req id")) {
                            int numPlayers = gm.getPlayers().size();
                            int startStack = gm.getStartStack();
                            out.println(numPlayers + " " + startStack);
                            gm.addPlayer(new HumanPlayer(numPlayers, startStack));
                        } else if (line.matches("\\d req gmc")) {
                            String update = gm.toString();
                            out.println(update);
                        } else if (line.matches("\\d disconnect")) {
                            gm.removePlayer(id);
                            break;
                        } else if (line.matches("\\d ready")) {
                            ready++;
                            if (ready == 2) {
                                ready = 0;
                                Thread round = new Thread(new Runnable() {
                                    public void run() {
                                        gm.playRound();
                                    }
                                });
                                round.start();
                            }
                        } else if (line.matches("\\d req player")) {
                            Player p = gm.getPlayer(id);
                            Collection<Card> cards = p.getHoleCards();
                            if (cards.size() == 0) {
                                out.println(String.valueOf(p.getChips()) + " null");
                            } else {
                                String cardString = Card.toString(cards);
                                String chips = String.valueOf(p.getChips());
                                out.println(chips + " " + cardString);
                            }
                        } else if (line.matches("\\d req board")) {
                            Collection<Card> cards = gm.getBoard();
                            if (cards.size() == 0) {
                                out.println("null");
                            } else {
                                out.println(Card.toString(cards));
                            }
                        } else if (line.matches("\\d check 0")) {
                            if (gm.isValidAction("check 0")) {
                                Player p = gm.getPlayer(id);
                                p.setAction("check 0");
                                out.println("valid");
                            } else {
                                out.println("inv");
                            }
                        } else if (line.matches("\\d fold 0")) {
                            if (gm.isValidAction("fold 0")) {
                                Player p = gm.getPlayer(id);
                                p.setAction("fold 0");
                                out.println("valid");
                            } else {
                                out.println("inv");
                            }
                        } else if (line.matches("\\d call \\d*")) {
                            String[] parse = line.split(" ");
                            int val = Integer.valueOf(parse[2]);
                            if (gm.isValidAction("call " + val)) {
                                Player p = gm.getPlayer(id);
                                p.setAction("call " + val);
                                out.println("valid");
                            } else {
                                out.println("inv");
                            }
                        } else if (line.matches("\\d bet \\d*")) {
                            String[] parse = line.split(" ");
                            int val = Integer.valueOf(parse[2]);
                            if (gm.isValidAction("bet " + val)) {
                                Player p = gm.getPlayer(id);
                                p.setAction("bet " + val);
                                out.println("valid");
                            } else {
                                out.println("inv");
                            }
                        } else if (line.matches("\\d raise \\d*")) {
                            String[] parse = line.split(" ");
                            int val = Integer.valueOf(parse[2]);
                            if (gm.isValidAction("raise " + val)) {
                                Player p = gm.getPlayer(id);
                                p.setAction("raise " + val);
                                out.println("valid");
                            } else {
                                out.println("inv");
                            }
                        } else if (line.matches("\\d req turn")) {
                            if (id == gm.getCurrPlayer()) {
                                out.println("true");
                            } else {
                                out.println("false");
                            }
                        }
                    }
                } catch (IOException e) {
                }
            }
        }
    }
    
    protected void finalize() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
