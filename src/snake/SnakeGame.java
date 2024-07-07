package snake;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

import javax.swing.JPanel;

import myFarm.GamePanel;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
//	public static class GameState {
//        Tile snakeHead;
//        ArrayList<Tile> snakeBody;
//        Tile food;
//        int velocityX;
//        int velocityY;
//        boolean gameOver;
//        boolean gameWon;
//        int foodEaten;
//    }
	
	private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;  // 遊戲板寬度
    int boardHeight; // 遊戲板高度
    int tileSize = 25; // 每個方塊的大小

    // 蛇
    Tile snakeHead; // 蛇頭
    ArrayList<Tile> snakeBody; // 蛇身

    // 食物
    Tile food;
    Random random;

    // 遊戲邏輯
    int velocityX; // 水平速度
    int velocityY; // 垂直速度
    Timer gameLoop; // 遊戲循環計時器
    Timer winTimer; // 勝利計時器
    int foodEaten = 0; // 記錄吃掉的食物數量

    boolean gameOver = false; // 遊戲結束標誌
    boolean gameWon = false; // 遊戲勝利標誌
  

    JFrame parentFrame; // 父窗口
//    GamePanel gp;
    Image backgroundImage; // 背景圖片
    Image foodImage; // 食物圖片
    
    public boolean endSnakeGame=false;

    // 構造函數
    public SnakeGame(int boardWidth, int boardHeight, JFrame parentFrame) {
        //this.gp = gp;
    	this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        //this.parentGamePanel = parentGamePanel;
        this.parentFrame = parentFrame;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));

        // 加載背景圖片
        backgroundImage = new ImageIcon(getClass().getResource("/snakeGame/grass01.png")).getImage();
        
        // 加載食物圖片
        foodImage = new ImageIcon(getClass().getResource("/snakeGame/corn.png")).getImage();

        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 1;
        velocityY = 0;
        
        gameLoop = new Timer(150, this);
        gameLoop.start();
    }

    // 繪製組件
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // 繪製遊戲畫面
    public void draw(Graphics g) {
        // 繪製背景圖片，填滿每個格子
        for (int x = 0; x < boardWidth; x += tileSize) {
            for (int y = 0; y < boardHeight; y += tileSize) {
                g.drawImage(backgroundImage, x, y, tileSize, tileSize, this);
            }
        }

        // 繪製食物
        g.drawImage(foodImage, food.x * tileSize, food.y * tileSize, tileSize, tileSize, this);

        // 繪製蛇頭
        g.setColor(Color.decode("#CD853F"));
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);
        
        // 繪製蛇身
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        // 繪製分數
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.white);
        g.drawString("Score: " + snakeBody.size(), 10, 20);

        // 繪製遊戲結束或通關信息
        if (gameOver || gameWon) {
            String message = gameOver ? "Game Over" : "Congratulations! You Win!";
            g.setFont(new Font("Arial", Font.BOLD, 36));
            FontMetrics metrics = g.getFontMetrics();
            int x = (boardWidth - metrics.stringWidth(message)) / 2;
            int y = ((boardHeight - metrics.getHeight()) / 2) + metrics.getAscent();
            g.setColor(gameOver ? Color.red : Color.blue);
            g.drawString(message, x, y);

            String restartMessage = gameOver ? "Press Space to Restart" : "Close The Window After 5 Secs";
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            metrics = g.getFontMetrics();
            x = (boardWidth - metrics.stringWidth(restartMessage)) / 2;
            y = y + metrics.getHeight() + 10;
            g.setColor(Color.white);
            g.drawString(restartMessage, x, y);
        }
    }

    // 放置食物
    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    // 移動蛇
    public void move() {
        // 檢查是否吃到食物
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
            foodEaten++; // 增加計數器

            // 檢查是否獲勝
            if (snakeBody.size() >= 5) {    
                gameWon = true;
                gameLoop.stop();
                
                winTimer = new Timer(5000, new ActionListener() {
                	
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                    	parentFrame.dispose();
                    	//System.out.println("1");
                    	
                    	endSnakeGame=true;
                    	//gp.gameState=gp.playState;
                    	//System.out.println("2");
                    	//gp.endSnakeGame();
                    	//parentGamePanel.endSnakeGame();
                    }
                });
                winTimer.setRepeats(false);
                winTimer.start();
            }

            // 增加蛇的速度
            int newDelay = gameLoop.getDelay() - 10;
            if (newDelay > 0) {
                gameLoop.setDelay(newDelay);
            }
        }

        // 移動蛇身
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        // 移動蛇頭
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // 檢查蛇是否碰到自己
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        // 檢查蛇是否碰到邊界
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize >= boardWidth || 
            snakeHead.y * tileSize < 0 || snakeHead.y * tileSize >= boardHeight) {
            gameOver = true;
        }
    }

    // 檢查兩個Tile是否碰撞
    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    // 重置遊戲
    public void resetGame() {
        snakeHead = new Tile(5, 5);
        snakeBody.clear();
        placeFood();
        velocityX = 1;
        velocityY = 0;
        gameOver = false;
        gameWon = false;
        foodEaten = 0; // 重置計數器
        if (winTimer != null) {
            winTimer.stop();
        }
        gameLoop.setDelay(150); // 重置速度
        gameLoop.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !gameWon) {
            move();
            repaint();
        } else {
//        	gp.gameState=gp.playState;
//            gp.endSnakeGame();
        	gameLoop.stop();
            
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE && (gameOver || gameWon)) {
            resetGame();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
    
}
