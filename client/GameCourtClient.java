package client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import poker.Card;
import poker.Rank;
import poker.Suit;

public class GameCourtClient extends JPanel {
    
    public boolean playing = false;
    public static final int INTERVAL = 35;
    public static final int COURT_WIDTH = 650;
    public static final int COURT_HEIGHT = 650;
    public static final int BOARD_WIDTH = (5 * (Rank.WIDTH + Rank.GAP));
    public static final int BOARD_HEIGHT = Suit.HEIGHT;
    public static final int BOARD_X = (COURT_WIDTH - BOARD_WIDTH) / 2;
    public static final int BOARD_Y = (COURT_HEIGHT - BOARD_HEIGHT) / 2;
    
    public GameCourtClient() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JLabel table = new JLabel(new ImageIcon("./table.png"));
        add(table);
        
        JPanel board = new JPanel();
        board.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        board.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        board.setLocation(BOARD_X, BOARD_Y);
        Card c = new Card(Rank.DEUCE, Suit.SPADES);
        Rectangle r = new Rectangle((int) c.getSrcCoords().getX(), (int) c.getSrcCoords().getY(), Rank.WIDTH, Suit.HEIGHT);
        add(board);
        
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start();
        
        setFocusable(true);
        
    }
    
    public void reset() {
        playing = true;
    }
    
    void tick() {
        if (playing) {
            
            
            repaint();
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return (new Dimension(COURT_WIDTH, COURT_HEIGHT));
    }
    
}
