package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import poker.Card;
import poker.Rank;
import poker.Suit;

public class GameCourtClient extends JPanel {
    
    private static final long serialVersionUID = 1L;
    public boolean playing = false;
    public static final int INTERVAL = 35;
    public static final int COURT_WIDTH = 650;
    public static final int COURT_HEIGHT = 650;
    public static final int BOARD_WIDTH = (5 * (Rank.WIDTH + Rank.GAP));
    public static final int BOARD_HEIGHT = Suit.HEIGHT;
    public static final int BOARD_X = (COURT_WIDTH - BOARD_WIDTH) / 2;
    public static final int BOARD_Y = (COURT_HEIGHT - BOARD_HEIGHT) / 2;
    public static final int HOLE_WIDTH = (2 * (Rank.WIDTH + Rank.GAP));
    public static final int HOLE_HEIGHT = Suit.HEIGHT;
    Image boardImg;
    Image oppImg;
    Image playerImg;
    
    public GameCourtClient() {
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
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
    
    public void drawCards(Collection<Card> cards, String val) {
        String s = "";
        for (Card c : cards) {
            s += c.toString();
            s += ";";
        }
        Image i = null;
        String[] sA = s.split(";");
        for (int j = 0; j < sA.length; ++j) {
            String cardImg = "./res/" + sA[j] + ".png";
            try {
                File cF = new File(cardImg);
                i = append(i, ImageIO.read(cF));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (val.equals("board")) {
            boardImg = i;
        } else if (val.equals("player")) {
            playerImg = i;
        } else if (val.equals("opp")) {
            oppImg = i;
        }
    }
    
    public void drawBlankBoard() {
        Image i = null;
        for (int j = 0; j < 5; ++j) {
            String cardImg = "./res/blank.png";
            try {
                File cF = new File(cardImg);
                i = append(i, ImageIO.read(cF));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boardImg = i;
    }
    
    public void drawDoubleBlanks(String val) {
        String sImg = "./res/blank.png";
        Image i = null;
        try {
            File cF = new File(sImg);
            i = append(ImageIO.read(cF), ImageIO.read(cF));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (val.equals("player")) {
            playerImg = i;
        } else if (val.equals("opp")) {
            oppImg = i;
        }
    }
    
    private Image append(Image original, Image append) {
        if (original == null) {
            return append;
        } else if (append == null) {
            return original;
        }
        int w = original.getWidth(null) + append.getWidth(null) + Rank.GAP;
        int h = Math.max(original.getHeight(null), append.getHeight(null));
        BufferedImage ret = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = ret.createGraphics();
        g.drawImage(original, 0, 0, original.getWidth(null), original.getHeight(null), null);
        g.drawImage(append, original.getWidth(null) + Rank.GAP, 0, null);
        g.dispose();
        return ret;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Image blank = ImageIO.read(new File("./res/blank.png"));
            Image blanks = append(blank, blank);
            g.drawImage(ImageIO.read(new File("./res/table.png")), 25, 25, null);
            g.drawImage(blanks, (COURT_WIDTH - HOLE_WIDTH) / 2, 85, null);
            g.drawImage(blanks, (COURT_WIDTH - HOLE_WIDTH) / 2, 500, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (boardImg != null) {
            g.drawImage(boardImg, BOARD_X, BOARD_Y, null);
        }
        if (oppImg != null) {
            g.drawImage(oppImg, (COURT_WIDTH - HOLE_WIDTH) / 2, 85, null);
        }
        if (playerImg != null) {
            g.drawImage(playerImg,
                    (COURT_WIDTH - HOLE_WIDTH) / 2, 500, null);
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return (new Dimension(COURT_WIDTH, COURT_HEIGHT));
    }
    
}
