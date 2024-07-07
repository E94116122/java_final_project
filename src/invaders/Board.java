package invaders;


//import Alien;
//import com.zetcode.sprite.Player;
//import com.zetcode.sprite.Shot;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board extends JPanel {

    private Dimension d; // 遊戲視窗的尺寸
    private List<Alien> aliens; // 敵人的列表
    private Player player; // 玩家
    private Shot shot; // 子彈
    
    private int direction = -1; // 敵人移動的方向
    private int deaths = 0; // 擊敗敵人的數量

    private boolean inGame = true; // 遊戲是否進行中
    public boolean endInvadersGame = false;
    private String explImg = "res/invaderGame/explosion.png"; // 爆炸圖片的路徑
    private String message = ""; // 遊戲結束時的訊息

    private Timer timer; // 遊戲計時器
    private Timer winTimer; // 贏後延遲計時器

    private Image earthImage; // 地球圖片
    private Image oldmanImage; // 地球圖片
    
    private JFrame parentFrame; 

    public Board(JFrame frame) {
    	
    	this.parentFrame=frame;
        initBoard(); // 初始化遊戲畫面
        gameInit(); // 初始化遊戲
    }

    private void initBoard() {
        // 設定鍵盤監聽器
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Common.BOARD_WIDTH, Common.BOARD_HEIGHT);
        setBackground(Color.black);

        timer = new Timer(Common.DELAY, new GameCycle());
        timer.start();

        // 加載地球圖片
        earthImage = new ImageIcon("res/invaderGame/earth.png").getImage();

        gameInit();
    }

    private void gameInit() {
        // 初始化敵人和玩家
        aliens = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {

                var alien = new Alien(Common.ALIEN_INIT_X + 18 * j,
                        Common.ALIEN_INIT_Y + 18 * i);
                aliens.add(alien);
            }
        }

        player = new Player();
        shot = new Shot();
    }

    private void drawAliens(Graphics g) {
        // 繪製敵人
        for (Alien alien : aliens) {

            if (alien.isVisible()) {

                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {

                alien.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {
        // 繪製玩家
        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            player.die();
            inGame = false;
            message = "Game Over";
        }
    }

    private void drawShot(Graphics g) {
        // 繪製子彈
        if (shot.isVisible()) {

            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        // 進行繪製
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        // 繪製地球圖片作為背景
        for (int x = 0; x < Common.BOARD_WIDTH; x += earthImage.getWidth(this)) {
            for (int y = 0; y < Common.BOARD_HEIGHT; y += earthImage.getHeight(this)) {
                g.drawImage(earthImage, x, y, this);
            }
        }

        if (inGame) {
            g.drawLine(0, Common.GROUND,
                    Common.BOARD_WIDTH, Common.GROUND);

            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
        } else {
            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {
        // 顯示遊戲結束畫面
        var bigFont = new Font("Helvetica", Font.BOLD, 25);
        var smallFont = new Font("Helvetica", Font.BOLD, 16);
        var bigFontMetrics = this.getFontMetrics(bigFont);
        var smallFontMetrics = this.getFontMetrics(smallFont);

        g.setColor(message.equals("Game Over") ? Color.red : Color.blue);
        g.setFont(bigFont);
        g.drawString(message, (Common.BOARD_WIDTH - bigFontMetrics.stringWidth(message)) / 2,
                Common.BOARD_HEIGHT / 2);

        g.setColor(Color.black);
        g.setFont(smallFont);
        String subMessage = message.equals("Game Over") ? "Press Space to Restart" : "BUT I HATE BEANS!!!";
        g.drawString(subMessage, (Common.BOARD_WIDTH - smallFontMetrics.stringWidth(subMessage)) / 2,
                (Common.BOARD_HEIGHT / 2) + 30);


        if (message.equals("Congratulation! You win!")) {
        	
        	Image image = new ImageIcon(getClass().getResource("/npc/oldman_down_1.png")).getImage();

        	int newWidth = image.getWidth(null)*4;
        	int newHeight = image.getHeight(null)*4;
        	int imageX = (Common.BOARD_WIDTH /2 - image.getWidth(null));
        	int imageY = (Common.BOARD_HEIGHT - image.getHeight(null))/2+60;
        	g.drawImage(image, imageX, imageY, newWidth, newHeight,this);
        	
            winTimer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	endInvadersGame=true;
                	parentFrame.dispose();
                    //System.exit(0); // 關閉程序
                }
            });
            winTimer.setRepeats(false);
            winTimer.start();
        }
    }

    private void update() {
        // 更新遊戲狀態

        if (deaths == Common.NUMBER_OF_ALIENS_TO_DESTROY) {
            // 若擊敗所有敵人，則遊戲獲勝
            inGame = false;
            timer.stop();
            message = "Congratulation! You win!";
        }

        // 玩家行動
        player.act();

        // 子彈移動與擊中敵人的處理
        if (shot.isVisible()) {
            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Alien alien : aliens) {
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + Common.ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + Common.ALIEN_HEIGHT)) {

                        var ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }

            }

            int y = shot.getY();
            y -= 4;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // 敵人移動和碰撞偵測
        for (Alien alien : aliens) {
            int x = alien.getX();

            if (x >= Common.BOARD_WIDTH - Common.BORDER_RIGHT && direction != -1) {
                // 敵人到達右邊界，向下移動
                direction = -1;

                Iterator<Alien> i1 = aliens.iterator();

                while (i1.hasNext()) {
                    Alien a2 = i1.next();
                    a2.setY(a2.getY() + Common.GO_DOWN);
                }
            }

            if (x <= Common.BORDER_LEFT && direction != 1) {
                // 敵人到達左邊界，向下移動
                direction = 1;

                Iterator<Alien> i2 = aliens.iterator();

                while (i2.hasNext()) {
                    Alien a = i2.next();
                    a.setY(a.getY() + Common.GO_DOWN);
                }
            }
        }

        // 敵人移動和行動
        Iterator<Alien> it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = it.next();

            if (alien.isVisible()) {
                int y = alien.getY();

                if (y > Common.GROUND - Common.ALIEN_HEIGHT) {
                    // 敵人接近地面，遊戲結束
                    inGame = false;
                    message = "Game Over";
                }

                alien.act(direction);
            }
        }
    }

    private void doGameCycle() {
        // 遊戲循環，更新遊戲狀態並重新繪製畫面
        update();
        repaint();
    }

    private class GameCycle implements ActionListener {
        // 遊戲循環的計時器

        @Override
        public void actionPerformed(ActionEvent e) {
            // 每次觸發計時器事件時執行遊戲循環
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {
        // 鍵盤事件的監聽器

        @Override
        public void keyReleased(KeyEvent e) {
            // 當按鍵釋放時，通知玩家物件
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // 當按鍵按下時，通知玩家物件，並在按下空白鍵時發射子彈
            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                if (inGame) {
                    if (!shot.isVisible()) {
                        shot = new Shot(x, y);
                    }
                } else if (message.equals("Game Over")) {
                    // 當遊戲結束且顯示 Game Over 時按下空白鍵重新開始遊戲
                    resetGame();
                }
            }
        }
    }

    private void resetGame() {
        // 重置遊戲狀態
        player = new Player();
        shot = new Shot();
        deaths = 0;
        aliens.clear();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                var alien = new Alien(Common.ALIEN_INIT_X + 18 * j, Common.ALIEN_INIT_Y + 18 * i);
                aliens.add(alien);
            }
        }

        if (winTimer != null) {
            winTimer.stop();
        }

        inGame = true;
        timer.start();
        message = "";
    }
}
