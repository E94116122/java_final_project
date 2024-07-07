package egg;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CatchEggGame extends JFrame {

    static MenuPanel mp = new MenuPanel();
    public static EggGamePanel gp = new EggGamePanel();
    public static CardLayout cl = new CardLayout();
    public static JPanel cards = new JPanel(); // to contain the panels as cards

    public CatchEggGame() {
        cards.setLayout(cl);
        cards.add(mp, "MenuPanel");
        cards.add(gp, "GamePanel");
        cl.show(cards, "MenuPanel");
        add(cards);

        setTitle("Catch The Eggs Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 700); //frame size
        //setVisible(true);   //frame visibility
    }
    
    public void startGame() {
    	setVisible(true);
    	gp.startGame();
    }
    public void endGame(){
    	gp.endGame();

    	
 
        Timer timer = new Timer(3000, e -> {
            setVisible(false); 
            dispose(); 
        });
        timer.setRepeats(false); // 設置為執行一次
        timer.start();
    }
//    public static void main(String args[]) {
//        new CatchEggGame();//making an object
//    }
}
