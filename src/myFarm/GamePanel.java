package myFarm;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import invaders.SpaceInvaders;
import mine.GoldMiner;
import mine.Stage;
import snake.SnakeGame;
import egg.EggGamePanel;
import egg.CatchEggGame; 
import target.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	//screen settings
	final int originalTileSize = 16; //16*16 tile
	final int scale = 3;  
	public final int tileSize = originalTileSize*scale;  //48*48 tile
	public final int maxScreenRow = 12;
	public final int maxScreenCol = 16;
	public final int screenWidth = tileSize* maxScreenCol;  //768 pixels
	public final int screenHeight = tileSize * maxScreenRow;  //576 pixels
	
	//world settings
	public final int maxWorldCol = 62;
	public final int maxWorldRow = 62;
	public final int worldWidth = tileSize*maxWorldCol;
	public final int worldHeight = tileSize*maxWorldRow;
	
	int FPS = 60;
	
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler(this);
	Thread gameThread;
	JFrame snakeFrame = new JFrame("Snake Game");
	JFrame mineGameFrame = new JFrame("Gold Miner");
	public CollisionDetector colliD = new CollisionDetector(this);
	public AssetSetter assetS = new AssetSetter(this);
	public Player player = new Player(this, keyH);
	public SuperObject obj[] = new SuperObject[40];
	public Entity npc[] = new Entity[3];
	public Entity chicken[] = new Entity[10];
	public UI ui = new UI(this);
	public SnakeGame snakeGame = new SnakeGame(800, 600, snakeFrame);
	//EggGamePanel eggGP = new EggGamePanel();
	CatchEggGame eggGame = new CatchEggGame();
	GoldMiner goldMiner = new GoldMiner(mineGameFrame);
    public SpaceInvaders spaceInvaders = new SpaceInvaders();
    

	
	
	//game state
	public int gameState;
	public final int playState = 1;
	public final int pauseState = 0;
	public final int dialogueState = 2;
	public final int startState = 3;
	public final int snakeGameState = 4;
	public final int eggGameState = 5;
	public final int mineGameState = 6;
	public final int beanGameState = 7;
	public final int endState = 8;
	public final int instructionState = 9;
	
	public boolean dialogueEnd = false;
	public boolean inSnakeGame = false;
	public boolean gameEnd = false;
	
	
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void setUpGame() {
		assetS.setObject();
		assetS.setNPC();
		assetS.setChicken();
		gameState = startState;
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS;  //1000000000ns=1secs
		double nextDrawTime = System.nanoTime()+drawInterval;
		
		while(gameThread!=null) {
			//.out.println("The game loop is running!");
			long currentTime = System.nanoTime();
			//System.out.println("current time:"+currentTime);
			
			//update information character position
			update();
			
			//draw the screen with the updated information
			repaint();  //call paintComponent
			try {
				double remainingTime = nextDrawTime - System.nanoTime();
				remainingTime = remainingTime/1000000;
				
				if(remainingTime<0) {
					remainingTime = 0;
				}
				
				Thread.sleep((long)remainingTime);
				nextDrawTime+=drawInterval;
			}
			catch(InterruptedException e) {
				e.printStackTrace();
				
			}
		}
		
	}
	
	public void update() {
		if(gameState ==playState) {
			
			//player
			player.update();
			
			//npc
			for(int i=0;i<npc.length; i++) {
				if(npc[i]!=null) {
					npc[i].update();
				}
			}
			
			//chicken
			for(int i=0;i<chicken.length; i++) {
				if(chicken[i]!=null) {
					chicken[i].update();
				}
			}
		}
		else if(gameState == pauseState) {
			
		}
		else if (gameState == snakeGameState) {
            //snakeGame.update();
			if(snakeGame.endSnakeGame==true) {
				gameState=playState;

				//snakeGame.endSnakeGame=false;
				endSnakeGame();
			}

        }
		else if(gameState == eggGameState) {
			//startEggGame();
			if(eggGame.gp.endEggGame) {
				gameState=playState;
				endEggGame();
			}
		}
		else if(gameState == mineGameState) {

			if(goldMiner.stage.endMineGame) {
				gameState=playState;
				endMineGame();
			}
		}
		else if(gameState==beanGameState) {
			if(spaceInvaders.board.endInvadersGame) {
				gameState=playState;
				endSpaceInvadersGame();
			}
		}
		
		
		

	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		//debug
		long drawStart = 0;
		if(keyH.checkDrawTime==true) {
			drawStart = System.nanoTime();
		}
		
		//title
		if(gameState==startState) {
			ui.draw(g2);
		}
		else if(gameState==snakeGameState) {
			
		}
		else if(gameState==endState) {
			ui.draw(g2);
		}
//		else if(gameState==instructionState) {
//			ui.draw(g2);
//		}
		else {
			//tile
			tileM.draw(g2);
			
			//object
			for(int i=0; i<obj.length; i++) {
				if(obj[i]!=null) {
					obj[i].draw(g2, this);
				}
			}
			
			//npc
			for(int i=0; i<npc.length; i++) {
				if(npc[i]!=null) {
					npc[i].draw(g2, this);
				}
			}
			
			//chicken
			for(int i=0; i<chicken.length; i++) {
				if(chicken[i]!=null) {
					chicken[i].draw(g2, this);
				}
			}
			
			//player
			player.draw(g2);
			
			//ui
			ui.draw(g2);
			
			//debug2
			if(keyH.checkDrawTime==true) {
				long drawEnd = System.nanoTime();
				long passed = drawEnd - drawStart;
				g2.setColor(Color.white);
				g2.drawString("DrawTime"+passed,10,40);
				System.out.println("Draw Time"+ passed);
			}
		}
		
		

		
		
		//g2.setColor(Color.white);
		//g2.fillRect(playerX, playerY, tileSize, tileSize);  //draw a 48*48 rectangle at (X,Y)
		g2.dispose();
	}
	

	
	
	public void startSnakeGame() {
		if(gameState==snakeGameState) {
			//gameState=snakeGameState;
			snakeGame.resetGame();
			inSnakeGame=true;
			System.out.println("in snake game");
	        int boardWidth = 800;
	        int boardHeight = 600;
			//JFrame snakeFrame = new JFrame("Snake Game");
		    snakeFrame.setSize(boardWidth, boardHeight); // 设置贪吃蛇游戏窗口的大小
		    snakeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 关闭窗口时仅关闭当前窗口
		    snakeFrame.setLocationRelativeTo(null); // 将窗口放置在屏幕中央
		    snakeFrame.setResizable(false);
		    //SnakeGame snakeGame = new SnakeGame(800, 600, snakeFrame);
		    
		    snakeFrame.add(snakeGame);
		    
		   
		    snakeFrame.setVisible(true);

		    snakeFrame.pack();
		    snakeGame.requestFocusInWindow();
		}
		
    }

	public void endSnakeGame() {
		System.out.println("end snake game");
		//inSnakeGame = false;
		player.hasCorn++;
		ui.showMessage("Got the corn!");
        //gameState = playState;
		keyH.spacePressed=false;
		keyH.downPressed=false;
		keyH.upPressed=false;
		keyH.leftPressed=false;
		keyH.rightPressed=false;

	}
	
	public void startEggGame() {

		eggGame.startGame();

    }

    // End the egg game
    public void endEggGame() {
 
        eggGame.endGame();
        player.hasEgg++;
        ui.showMessage("Got the egg!");
		keyH.spacePressed=false;
		keyH.downPressed=false;
		keyH.upPressed=false;
		keyH.leftPressed=false;
		keyH.rightPressed=false;

    }
	
	public void startMineGame() {
		
        mineGameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mineGameFrame.setSize(800, 600);

        //try {
            //GoldMiner goldMiner = new GoldMiner();
            goldMiner.setVisible(true);
            mineGameFrame.add(goldMiner);
        //} catch (IOException e) {
            //e.printStackTrace();
        //}
        //goldMiner.stage.stageState= Stage.StageState.MENU;
        mineGameFrame.pack();
        mineGameFrame.setLocationRelativeTo(null);
        mineGameFrame.setVisible(true);
    }
	
	public void endMineGame() {
		player.hasCarrot++;
		ui.showMessage("Got the carrot!");
		keyH.spacePressed=false;
		keyH.downPressed=false;
		keyH.upPressed=false;
		keyH.leftPressed=false;
		keyH.rightPressed=false;
	}
	
	public void startSpaceInvadersGame() {
		EventQueue.invokeLater(() -> {
            spaceInvaders.setVisible(true);
        });
	}
	
	public void endSpaceInvadersGame() {
		player.hasBean++;
		ui.showMessage("You are not supposed to have the beans!");
		keyH.spacePressed=false;
		keyH.downPressed=false;
		keyH.upPressed=false;
		keyH.leftPressed=false;
		keyH.rightPressed=false;
	}
	

	
}
