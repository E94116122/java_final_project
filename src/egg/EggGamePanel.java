package egg;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class EggGamePanel extends JPanel {
    JButton retry = new JButton("");

    Image gamebkg = new ImageIcon(getClass().getResource("/eggGame/gamebkg.png")).getImage();
    Image basket = new ImageIcon(getClass().getResource("/eggGame/basket.png")).getImage();
    Image egg = new ImageIcon(getClass().getResource("/eggGame/egg.png")).getImage();
    Image gameOverbkg = new ImageIcon(getClass().getResource("/eggGame/gameOverbkg.png")).getImage();
    Image tempbkg; //temporary background

    ImageIcon retryIcon = new ImageIcon(getClass().getResource("/eggGame/replay.png")); 
    int x_basket, y_basket; //basket x and y coordinates
    int x_egg, y_egg; // x and y coordinates of egg
    int y_speed;
    Random rand = new Random(); // for randomizing x-coord of eggs

    JLabel time;
    JLabel points;

    int pointsCount = 0;
    int timeleft = 29;
    int counter = 0;

    public boolean gameOver = false;
    public boolean endEggGame = false;

    public Timer gameTimer;

    public EggGamePanel() {
        setLayout(null);
        setFocusable(true);
        tempbkg = gamebkg;

        x_basket = 450;
        y_basket = 600;
        x_egg = rand.nextInt(800);
        y_egg = 0;
        y_speed = rand.nextInt(3,7);

        time = new JLabel("Time: 59");
        time.setBounds(20, 10, 100, 20); //setting the time label on screen
        points = new JLabel("Points: 0");
        points.setBounds(100, 10, 100, 20);

        add(time);
        add(points);

        retry.setIcon(retryIcon);
        retry.setBounds(550, 450, 64, 64); // 設置重新嘗試按鈕的位置和大小
        retry.setVisible(false); // 初始化時設置為不可見
        retry.addActionListener(e -> resetGame()); // 點擊按鈕重置遊戲
        add(retry);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_LEFT && x_basket > 10) {
                    x_basket -= 10;
                    repaint(); // redraw at new position
                }
                if (ke.getKeyCode() == KeyEvent.VK_RIGHT && x_basket < 1000) {
                    x_basket += 10; // redraw at new position
                    repaint();
                }
            }
        });

        
    }
    
    public void startGame() {
    	resetGame();
    	// Initialize and start the game timer
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (!gameOver) {
                    updateTime();
                    fallEgg();
                    detectCollision();
                    checkGameOver();
                    repaint();
                }
            }
        }, 0, 10); // Update every 10 milliseconds
    }
    
    public void endGame() {
    	if(gameTimer!=null) {
    		gameTimer.cancel();
            gameTimer.purge();
    	}
    }

    public void fallEgg() {
        if (y_egg >= 650) {
            y_egg = 0;
            x_egg = rand.nextInt(800);
            y_speed = rand.nextInt(3, 7);
        } else {
            y_egg += y_speed;
        }
    }

    public void updateTime() {
        counter++;
        if (counter == 100) {
            timeleft--;
            counter = 0;
        }
        time.setText("Time: " + timeleft);
    }

    public void detectCollision() {
        Rectangle basketRect = new Rectangle(x_basket, y_basket, 100, 65);
        Rectangle eggRect = new Rectangle(x_egg, y_egg, 45, 67);

        if (eggRect.intersects(basketRect)) {
            pointsCount += 5;
            points.setText("Points: " + pointsCount);
            y_egg = 0;
            x_egg = rand.nextInt(800);
            y_speed = rand.nextInt(3, 7);
        }
    }

    public void checkGameOver() {
        if (timeleft <= 0) {
            tempbkg = gameOverbkg;
            JLabel yourScore;
            if (pointsCount >= 40) {
            	endEggGame = true;
                yourScore = new JLabel("You win !");
                retry.setVisible(false);
            } else {
                yourScore = new JLabel("Game over");
                retry.setVisible(true);
            }
            yourScore.setBounds(420, 280, 400, 200);
            Font font = new Font("Comic Sans MS", Font.BOLD, 60);
            yourScore.setFont(font);
            yourScore.setForeground(Color.white);
            add(yourScore);
            gameOver = true;
            revalidate();
            repaint();
        }
    }

    public void resetGame() {
        pointsCount = 0;
        timeleft = 29;
        counter = 0;
        gameOver = false;
        x_basket = 450;
        y_basket = 600;
        x_egg = rand.nextInt(800);
        y_egg = 0;
        y_speed = rand.nextInt(5,7);
        tempbkg = gamebkg;
        retry.setVisible(false);

        // Remove all components and re-add them
        removeAll();
        add(time);
        add(points);
        add(retry);
        revalidate();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(tempbkg, 0, 0, null);

        if (!gameOver) {
            g2d.drawImage(egg, x_egg, y_egg, 80, 80, null);
            g2d.drawImage(basket, x_basket, y_basket, null);
        }
    }
}