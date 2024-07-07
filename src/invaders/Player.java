package invaders;

//import com.zetcode.Commons;

import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;

public class Player extends Sprite {

    private int width;  // 玩家寬度

    public Player() {

        initPlayer();  // 初始化玩家
    }

    private void initPlayer() {

        var playerImg = "res/invaderGame/player.png";  // 玩家圖像的路徑
        var ii = new ImageIcon(playerImg);  // 創建圖像圖標對象

        width = ii.getImage().getWidth(null);  // 設置玩家寬度
        setImage(ii.getImage());  // 設置玩家圖像

        int START_X = 270;  // 初始x坐標
        setX(START_X);

        int START_Y = 280;  // 初始y坐標
        setY(START_Y);
    }

    public void act() {

        x += dx;  // 根據水平速度更新玩家的位置

        if (x <= 2) {  // 如果玩家移動到了左邊界

            x = 2;  // 將玩家的x坐標設置為2
        }

        if (x >= Common.BOARD_WIDTH - 2 * width) {  // 如果玩家移動到了右邊界

            x = Common.BOARD_WIDTH - 2 * width;  // 將玩家的x坐標設置為BOARD_WIDTH - 2 * width
        }
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();  // 獲取按鍵事件的按鍵代碼

        if (key == KeyEvent.VK_LEFT) {  // 如果按下的是左箭頭鍵

            dx = -2;  // 將水平速度設置為-2，向左移動
        }

        if (key == KeyEvent.VK_RIGHT) {  // 如果按下的是右箭頭鍵

            dx = 2;  // 將水平速度設置為2，向右移動
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();  // 獲取按鍵事件的按鍵代碼

        if (key == KeyEvent.VK_LEFT) {  // 如果釋放的是左箭頭鍵

            dx = 0;  // 將水平速度設置為0，停止向左移動
        }

        if (key == KeyEvent.VK_RIGHT) {  // 如果釋放的是右箭頭鍵

            dx = 0;  // 將水平速度設置為0，停止向右移動
        }
    }
}
