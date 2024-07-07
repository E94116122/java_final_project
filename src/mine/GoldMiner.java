package mine;


import javax.swing.*;

import mine.Stage.StageState;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
//import java.util.Timer;
/**
 * Created by lzc on 4/2/16.
 */
public class GoldMiner extends JPanel {
//public class GoldMiner extends JFrame{
    public Stage stage;
    static final double TIME_STEP = 1.0; //單位事件長
    static final double PERIOD = 20.0;
    private JFrame parentFrame;
    private Timer gameTimer; // 添加定时器
    
    //public boolean frameDispose=false;
    public GoldMiner(JFrame parentFrame){
    	this.parentFrame=parentFrame;
    //public GoldMiner() throws IOException{
    	//setTitle("Gold Miner");
        //setSize(800,600);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800,600));
    	try {
			stage = new Stage();
			//stage.setParentFrame(parentFrame);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	setLayout(new BorderLayout());
    	add(stage, BorderLayout.CENTER);
		stage.setFocusable(true);
		stage.requestFocusInWindow();
        stage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        stage.hook.launch();
                        break;
                    case KeyEvent.VK_P:
                        stage.pause();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        stage.configure();
                        break;
                }
            }
        });

        stage.addMouseListener(new MouseAdapter() {
                                   @Override
                                   public void mouseClicked(MouseEvent e) {
                                       //super.mouseClicked(e);
                                       int x=e.getX(), y=e.getY();
                                       if(stage.stageState==Stage.StageState.MENU){

                                           if(x>340&&x<490 && y>200 && y<250){
                                               try{
                                                   stage.load(0);
                                                   stage.start();
                                               }catch (IOException e1){
                                                   e1.printStackTrace();
                                               }
                                           }else if(x>340&&x<490&& y>280 && y<330){
                                               //frameDispose=true;
                                        	   parentFrame.dispose();
                                           }
                                       }else if(stage.stageState== Stage.StageState.GAME_OVER){
                                           int distance = (x-500)*(x-500)+(y-350)*(y-350);
                                           if(distance<1500){
                                               try{
                                                   stage.load(0);
                                                   stage.start();
                                                   stage.hook = new Hook(stage.width, 180);
                                               }catch (IOException e1){
                                                   e1.printStackTrace();
                                               }
                                           }

                                       }
                                   }
                               }
        );

        //add(stage);
        stage.stageState= Stage.StageState.MENU;
        //stage.start();
        
        gameTimer = new Timer((int) PERIOD, e -> {
            if (stage.endMineGame) {
            	endGame();
                gameTimer.stop();
                
 
            }
        });
        gameTimer.start();

    }

//    public static void main(String[] args) throws IOException{
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//			        GoldMiner goldMiner = new GoldMiner();
//			        goldMiner.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//    }
    public void endGame(){

        Timer timer = new Timer(3000, e -> {
            setVisible(false); 
            parentFrame.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
    


}
