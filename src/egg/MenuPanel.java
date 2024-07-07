package egg;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;

public class MenuPanel extends JPanel {

    JButton play = new JButton("");
    JButton exit = new JButton("");

    Image menubkg = new ImageIcon(getClass().getResource("/eggGame/menubkg.png")).getImage();  //menu background


  

    public MenuPanel() {
   
        /* adding the buttons in the panel */
    	 setLayout(new FlowLayout(FlowLayout.CENTER, 0, 100)); // 使用 FlowLayout 並將對齊方式設置為居中，並調整 Y 軸位置
    	 
        play.setFont(new Font("Comic Sans MS", Font.BOLD, 30));//設置大小
        exit.setFont(new Font("Comic Sans MS", Font.BOLD, 30));//
        
        
        
        play.setForeground(Color.WHITE);  // 設定前景色（文字顏色）
        play.setBackground(new Color(128, 128, 128, 128)); // 半透明的灰色背景
       
        exit.setForeground(Color.WHITE);  // 設定前景色（文字顏色）
        exit.setBackground(new Color(128, 128, 128, 128));    // 設定背景色
        
        play.setText("Play"); // 設置play按鈕的文本
        exit.setText("Exit"); // 設置exit按鈕的文本
        
        play.setBounds(300, 600, 400, 100); // 設置play按鈕的位置和大小
        exit.setBounds(300, 800, 400, 100); // 設置exit按鈕的位置和大小
        
       add(play);
       add(exit);
        
        /* adding mouseListeners on buttons */
        play.addMouseListener(new Click());
        exit.addMouseListener(new Click());
    }//end constructor

    class Click extends MouseAdapter { //internal friendly class
        public void mouseClicked(MouseEvent me) {
            if (me.getSource() == play) {
                CatchEggGame.cl.show(CatchEggGame.cards, "GamePanel"); //show gamePanel when play is clicked
                CatchEggGame.gp.requestFocusInWindow(); // Ensure game panel has focus for key events
            }
            if (me.getSource() == exit) {
                System.exit(0);  //exit application when exit is clicked
            }
        }//end mouseClick
    }//end mouseAdapter

    public void paintComponent(Graphics g) {
        super.paintComponent(g); //calling the super class functions
        Graphics2D g2d = (Graphics2D) g; //casting to graphics2D
        setFocusable(true);         //setting the focus on the panel
        g2d.drawImage(menubkg, 0, 0, null); //draw menu image
    }//end paintComponent
}//end MenuPanel




