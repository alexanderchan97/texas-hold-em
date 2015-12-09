package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import poker.Card;
import poker.HumanPlayer;
import poker.Player;

public class Game implements Runnable {

    private static final int PORT = 25888;
    private static Player player;
    private static String hostname;
    private static GameModelClient gmc;
    private JPanel commandPanel;
    
    @Override
    public void run() {
        
        player = new HumanPlayer(-1, 0);
        gmc = new GameModelClient();
        
        // Query server for initial game state
        try (
            Socket socket = new Socket(hostname, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ) {
            String playerRequest = player.getId() + " req id";
            out.println(playerRequest);
            String update = in.readLine();
            String[] parsed = update.split(" ");
            player.setId(Integer.parseInt(parsed[0]));
            player.setChips(Integer.parseInt(parsed[1]));
            
            String gmcRequest = player.getId() + " req gmc";
            out.println(gmcRequest);
            update = in.readLine();
            update = update.replaceAll("_", Integer.toString(player.getId()));
            String[] updateA = update.split("\\?");
            for (String s : updateA) {
                gmc.updateModel(s);
            }
        } catch (UnknownHostException e) {
            System.exit(1);
        } catch (IOException e) {
            System.exit(1);
        }
        
        final JFrame frame = new JFrame("Texas Hold-em: Heads up");
        frame.setLocation(150, 150);
        
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
        
        final JButton help = new JButton("Help");
        help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Tutorial());
            }
        });
        
        controlPanel.add(info);
        controlPanel.add(help);
        
        final JButton check = new JButton("Check");
        check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (
                    Socket socket = new Socket(hostname, PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                        ) {
                    out.println(player.getId() + " check 0");
                    System.out.println(player.getId() + " check 0");
                    if (in.readLine().equals("inv")) {
                        JOptionPane.showMessageDialog(new JFrame(),
                                "Invalid Action", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(new JFrame(),
                            "I/O Error", "Error", JOptionPane.ERROR_MESSAGE);
                } 
            }
        });
        
        final JButton call = new JButton("Call");
        call.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (
                    Socket socket = new Socket(hostname, PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                        ) {
                    out.println(player.getId() + " call " + gmc.getCurrentBet());
                    System.out.println(player.getId() + " call " + gmc.getCurrentBet());
                    if (in.readLine().equals("inv")) {
                        JOptionPane.showMessageDialog(new JFrame(),
                                "Invalid Action", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(new JFrame(),
                            "I/O Error", "Error", JOptionPane.ERROR_MESSAGE);
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
                    try (
                        Socket socket = new Socket(hostname, PORT);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                            ) {
                        out.println(player.getId() + " bet " + Integer.parseInt(s));
                        System.out.println(player.getId() + " bet " + Integer.parseInt(s));
                        if (in.readLine().equals("inv")) {
                            JOptionPane.showMessageDialog(new JFrame(),
                                    "Invalid Action", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(new JFrame(),
                                "I/O Error", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        final JButton raise = new JButton("Raise");
        raise.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = (String) JOptionPane.showInputDialog(new JFrame(),
                        "Raise to: ");
                if (!s.matches("\\d+")) {
                    JOptionPane.showMessageDialog(new JFrame(),
                            "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try (
                        Socket socket = new Socket(hostname, PORT);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                            ) {
                        out.println(player.getId() + " raise " + Integer.parseInt(s));
                        System.out.println(player.getId() + " raise " + Integer.parseInt(s));
                        if (in.readLine().equals("inv")) {
                            JOptionPane.showMessageDialog(new JFrame(),
                                    "Invalid Action", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(new JFrame(),
                                "I/O Error", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        final JButton fold = new JButton("Fold");
        fold.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (
                    Socket socket = new Socket(hostname, PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                        ) {
                    out.println(player.getId() + " fold 0");
                    System.out.println(player.getId() + " fold 0");
                    if (in.readLine().equals("inv")) {
                        JOptionPane.showMessageDialog(new JFrame(),
                                "Invalid Action", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(new JFrame(),
                            "I/O Error", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        final JLabel chips = new JLabel("Chips: 0");
        final JLabel pot = new JLabel("Pot: 0");
        final JLabel oppChips = new JLabel("Opponent: 0");
        
        final JButton ready = new JButton("Ready");
        ready.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (
                    Socket socket = new Socket(hostname, PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                        ) {
                    out.println(player.getId() + " ready");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(new JFrame(),
                            "I/O Error", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        commandPanel.add(chips);
        commandPanel.add(check);
        commandPanel.add(call);
        commandPanel.add(bet);
        commandPanel.add(raise);
        commandPanel.add(fold);
        commandPanel.add(oppChips);
        commandPanel.add(pot);
        controlPanel.add(ready);
        
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try (
                    Socket socket = new Socket(hostname, PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                        ) {
                    out.println(player.getId() + " disconnect");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                frame.dispose();
                System.exit(0);
            }
        });
        frame.setVisible(true);
        
        court.reset();
        
        // Every second, poll the server to refresh the client's game state
        Thread pollServer = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try (
                        Socket socket = new Socket(hostname, PORT);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                            ) {
                        
                        String gmcRequest = player.getId() + " req gmc";
                        out.println(gmcRequest);
                        String update = in.readLine();
                        update = update.replaceAll("_", Integer.toString(player.getId()));
                        String[] updateA = update.split("\\?");
                        for (String s : updateA) {
                            System.out.println(s);
                            gmc.updateModel(s);
                        }
                        
                        String playerRequest = player.getId() + " req player";
                        out.println(playerRequest);
                        String playerUpdate = in.readLine();
                        String[] playerParse = playerUpdate.split(" ");
                        String hole = playerParse[1];
                        if (!hole.equals("null")) {
                            court.drawCards(Card.fromString(hole), "player");
                        } else {
                            court.drawDoubleBlanks("player"); 
                        }
                        player.setChips(Integer.parseInt(playerParse[0]));

                        chips.setText("Chips: " + player.getChips());
                        pot.setText("Pot: " + gmc.getPot());
                        oppChips.setText("Opponent: " + gmc.getOppChips());
                        
                        String boardRequest = player.getId() + " req board";
                        out.println(boardRequest);
                        String board = in.readLine();
                        if (!board.equals("null")) {
                            court.drawCards(Card.fromString(board), "board");
                        } else {
                            court.drawBlankBoard();
                        }
                        
                        String turnRequest = player.getId() + " req turn";
                        out.println(turnRequest);
                        String turn = in.readLine();
                        gmc.setIsTurn((turn).equals("true"));
                        if (gmc.isTurn()) {
                            for (Component c : commandPanel.getComponents()) {
                                c.setEnabled(true);
                            }
                        } else {
                            for (Component c : commandPanel.getComponents()) {
                                c.setEnabled(false);
                            }
                        }
                        
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        pollServer.start();
    }
    
    public static String getHost() {
        String s = (String) JOptionPane.showInputDialog(new JFrame(),
                "Host to connect to (localhost for this computer): ",
                "");
        return s;
    }
    
    public static void main(String[] args) {
        hostname = getHost();
        SwingUtilities.invokeLater(new Game());
    }

}
