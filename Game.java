import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Game implements Runnable {

    @Override
    public void run() {
        
        final JFrame frame = new JFrame("Texas Hold-em");
        frame.setLocation(300, 300);
        
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }


}
