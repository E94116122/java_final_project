package invaders;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class SpaceInvaders extends JFrame {
	public Board board = new Board(this);

    public SpaceInvaders() {

        initUI();
    }

    private void initUI() {
        // 在 JFrame 上加入遊戲面板
        add(board);

        
        setSize(Common.BOARD_WIDTH, Common.BOARD_HEIGHT); // 設定視窗大小

        setDefaultCloseOperation(EXIT_ON_CLOSE); // 設定關閉視窗時的預設行為
        setResizable(false); // 設定視窗大小不可調整
        setLocationRelativeTo(null); // 將視窗置中顯示
    }

//    public static void main(String[] args) {
//
//        EventQueue.invokeLater(() -> {
//
//            var ex = new SpaceInvaders();
//            ex.setVisible(true); // 顯示視窗
//        });
//    }
}
