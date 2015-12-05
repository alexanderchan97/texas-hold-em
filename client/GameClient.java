package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.UnknownHostException;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import poker.HumanPlayer;
import poker.Player;

public class GameClient implements Runnable, Observer {

    private static final int PORT = 25888;
    private static Player player;
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static String hostname;
    private static ObjectOutputStream objOut;
    private static ObjectInputStream objIn;
    private static GameModelClient gmc;
    private JPanel commandPanel;
    
    @Override
    public void run() {
        
        // Create a new player object with an invalid value
        player = new HumanPlayer(-1, 0);
        // Send this player to the server for modification
        // Receive updated player
        try {
            objOut.writeObject(player);
            player = (Player) objIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create a new gmc with default values
        gmc = new GameModelClient();
        // Request gmc information from server
        try {
        out.print(player.getId() + " req gmc");
        while (in.ready()) {
                gmc.updateModel(in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        final JFrame frame = new JFrame("Texas Hold-em: Heads up");
        frame.setLocation(300, 300);
        
        final GameCourtClient court = new GameCourtClient();
        frame.add(court, BorderLayout.CENTER);
        
        final JPanel controlPanel = new JPanel();
        frame.add(controlPanel, BorderLayout.NORTH);
        
        commandPanel = new JPanel();
        frame.add(commandPanel, BorderLayout.SOUTH);
        
        final JButton info = new JButton("Information");
        info.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(new JFrame(),
                        "Current server: " + hostname + "\nPlayer ID: " + player.getId() +
                        "\nCurrent bet: " + gmc.getCurrentBet());
            }
        });
        controlPanel.add(info);
        
        final JButton check = new JButton("Check");
        check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    out.print(player.getId() + " check");
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
        });
        
        final JButton call = new JButton("Call");
        call.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    out.print(player.getId() + " call ");
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
        });
        
        final JButton bet = new JButton("Bet");
        bet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = (String) JOptionPane.showInputDialog(new JFrame(),
                        "Bet amount: ");
                if (!s.matches("\\d+")) {
                    JOptionPane.showMessageDialog(new JFrame(),
                            "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        out.print(player.getId() + " bet " + Integer.parseInt(s));
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }
                }
            }
        });
        
        final JButton fold = new JButton("Fold");
        fold.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    out.print(player.getId() + " fold");
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
        });
        
        
        commandPanel.add(check);
        commandPanel.add(call);
        commandPanel.add(bet);
        commandPanel.add(fold);
        
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        court.reset();
    }
    
    public static String getHost() {
        String s = (String) JOptionPane.showInputDialog(new JFrame(),
                "Host to connect to (localhost for this computer): ",
                "");
        return s;
    }
    
    public static void listenSocket(String host) {
        hostname = host;
        try {
            socket = new Socket(host, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            objOut = new ObjectOutputStream(socket.getOutputStream());
            objIn = new ObjectInputStream(socket.getInputStream());
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Unknown host: " + host, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "No input or output stream from host", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    @Override
    public void update(Observable obs, Object obj) {
        if (obs == gmc) {
            if (!gmc.isTurn()) {
                for (Component c : commandPanel.getComponents()) {
                    c.setEnabled(false);
                }
            } else {
                for (Component c : commandPanel.getComponents()) {
                    c.setEnabled(true);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        //listenSocket(getHost());
        SwingUtilities.invokeLater(new GameClient());
    }




}
