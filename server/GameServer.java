package server;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.Timer;

import poker.GameModel;

/**
 * Instantiates a GUI for a server.
 */
public final class GameServer {

    private GameServer() {}
    
    public static void main(String[] args) {
        
        final JFrame frame = new JFrame("Texas Hold-em Server");
        frame.setPreferredSize(new Dimension(325, 125));
        JTextArea log = new JTextArea();
        log.setBounds(25, 25, 300, 100);
        log.setEditable(false);
        log.setLineWrap(true);
        
        final GameModel gm = new GameModel();
        final GameServerBackend server = new GameServerBackend(gm);
        final Timer timer = new Timer(100, null);
        
        log.append("Server listening on PORT " + GameServerBackend.PORT);
        
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!frame.isDisplayable() || !server.isRunning()) {
                    frame.dispose();
                    server.stop();
                    timer.stop();
                }
            }
        });
        timer.start();
        new Thread(server, "Connection acceptor").start();
        frame.add(log);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

}
