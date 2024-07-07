package mine;

import javax.swing.*;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lzc on 4/2/16.
 */
public class Stage extends JPanel {
    double width = 800;
    double height = 600;

    int order;//關數
    final int totalOrder = 3;

    /*關卡生成的加載變量*/
    int lifetime;
    int totaltime;
    int requireScore;
    List<Mineral> mineralList = new ArrayList<Mineral>();
//    List<Bomb> bombList = new ArrayList<Bomb>();

    enum StageState {MENU, PLAYING, CONFIGURE, PAUSE, GAME_OVER,WIN}

    StageState stageState;

    int score;
    
//    List<ExplodeEffect> explodeEffectList = new ArrayList<ExplodeEffect>();

    Hook hook = new Hook(width, 180);
    Timer timer;
    
    public boolean endMineGame=false;
    
//    JFrame parentFrame;
//
//    public void setParentFrame(JFrame parentFrame) {
//        this.parentFrame = parentFrame;
//    }
    
    //播放倒數計時的target
    SoundPlayer playLastSoundThreadTarget=new SoundPlayer("res/mineGame/last-10s-sound.wav"); 

    /*Updated by Yangan*/
    void load(int order) throws IOException {
    	int minercount=0;
        this.order = order;
        mineralList.clear();
        //File filepath=new File("dat/stage"+order+".dat");
        //File filepath=new File("res/mineGame/stage"+order+".dat");
        
        String resourcePath = "/minegame/stage" + order + ".dat";
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        
        //InputStreamReader isr=new InputStreamReader(new FileInputStream(filepath));
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br=new BufferedReader(isr); //讀數據文件
        String linedata=br.readLine(); //讀關卡時間和分數
        StringTokenizer st=new StringTokenizer(linedata,",");
        lifetime=Integer.parseInt(st.nextToken());
        totaltime=lifetime;
        requireScore=Integer.parseInt(st.nextToken());
        minercount=Integer.parseInt(st.nextToken());
        for(int i=0;i<minercount;i++){  //讀礦物列表
        	linedata=br.readLine();
        	st=new StringTokenizer(linedata,",");  //格式为：礦物類型,x,y,r,value,density
        	String mineralType=st.nextToken();
        	switch(mineralType){  //根据不同的礦物類型建立不同對象
//        		
        		case "G":
        		{
                	double x=Double.parseDouble(st.nextToken());
                	double y=Double.parseDouble(st.nextToken());
                	double r=Double.parseDouble(st.nextToken());
                	int value=Integer.parseInt(st.nextToken());
        			mineralList.add(new Carrot(x,y,r,value));
        			break;
        		}
//        		
        		case "M":
        		{
        			double x=Double.parseDouble(st.nextToken());
                	double y=Double.parseDouble(st.nextToken());
                	double r=Double.parseDouble(st.nextToken());
                	int value=Integer.parseInt(st.nextToken());
                	int movingDirection=Integer.parseInt(st.nextToken());
                	double movingSpeed=Double.parseDouble(st.nextToken());
                	boolean withDiamond=st.nextToken().equals("D");
        			mineralList.add(new MovingCarrot(x,y,r,value,
        					movingDirection,movingSpeed, withDiamond));
        			break;
        		}
        	}
        }
        br.close();
        isr.close();
    }


    /* 鍵盤 */
    public Stage() throws IOException {
        /*测试时直接從第一關開始*/
        //load(0);
        this.stageState=StageState.MENU;
        this.requestFocus();
        setDoubleBuffered(true);
    }

    void pause() {
        if (stageState == StageState.PLAYING)
            stageState = StageState.PAUSE;
        else if (stageState == StageState.PAUSE) {
            stageState = StageState.PLAYING;
        }
    }

    void configure() {
        if (stageState == StageState.CONFIGURE)
            stageState = StageState.PLAYING;
        else if (stageState == StageState.PLAYING)
            stageState = StageState.CONFIGURE;
    }

    void gameOver() {
        score = 0;
        stageState = StageState.GAME_OVER;
    }
    
    void win() {
    	stageState = StageState.WIN;
    	endMineGame = true;
    
        
    }

    /*下一關*/
    void next() throws IOException{ 
        if(lifetime>0&&lifetime<=100){
        	playLastSoundThreadTarget.canplay=false;
        } //如果在倒數計時10秒内gameover或者過關應該停掉音樂
        if (score < requireScore) {
            timer.cancel();
//            bombList.clear();
            gameOver();
        }
        else {
            order++;
            if (order < totalOrder) {
//            	
            	load(order);
            } else {
            	order = 0;
            	score = 0;
//            
            	load(order);
            }
        }
    }

	void refresh() throws IOException {

        if (stageState != StageState.PLAYING) return;
        
        /*如果清空了或時間截止就直接判断能否進入下一關*/
        if(score>3000) {
        	win();
        }
        if ((mineralList.isEmpty() && ! hook.hasMineral())||lifetime <= 0) {
        	next();
        }
        lifetime--;
        /*for(int i=0; i<mineralList.size(); i++){
            mineralList.get(i).refresh();
        }*/
        hook.refresh(this);
        for (Mineral i : mineralList) {
        	/*對老鼠来说需要更新其位置*/
        	if (i instanceof MovingCarrot) {
        		((MovingCarrot)i).runCarrot();
        	}
        }
        
        if (lifetime == 100) {
        	Thread playLastSound = new Thread(playLastSoundThreadTarget);
        	playLastSound.start();
        }
        
        repaint();
    }

    void start() {
        stageState = StageState.PLAYING;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
					refresh();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }, 0, 100);
    }
//    Image scoreboard=Toolkit.getDefaultToolkit().createImage("res/mineGame/scoreboard.png");
//    Image gameoverPic=Toolkit.getDefaultToolkit().createImage("res/mineGame/gameover.png");
//    Image gamebgPic=Toolkit.getDefaultToolkit().createImage("res/mineGame/map_bg_0.png");
//    Image timeLineBg=Toolkit.getDefaultToolkit().createImage("res/mineGame/timebg.png");
//    Image timeLineGreen=Toolkit.getDefaultToolkit().createImage("res/mineGame/timegood.png");
//    Image timeLineRed=Toolkit.getDefaultToolkit().createImage("res/mineGame/timepoor.png");
//    Image buttonBg=Toolkit.getDefaultToolkit().createImage("res/mineGame/text-background.png");
//    Image timeLineCenter=Toolkit.getDefaultToolkit().createImage("res/mineGame/timecenter.png");
//    Image retryBtn=Toolkit.getDefaultToolkit().createImage("res/mineGame/replay.png");

    Image scoreboard = new ImageIcon(getClass().getResource("/mineGame/scoreboard.png")).getImage();
    Image gameoverPic = new ImageIcon(getClass().getResource("/mineGame/gameover.png")).getImage();
    Image gamebgPic = new ImageIcon(getClass().getResource("/mineGame/map_bg_0.png")).getImage();
    Image timeLineBg = new ImageIcon(getClass().getResource("/mineGame/timebg.png")).getImage();
    Image timeLineGreen = new ImageIcon(getClass().getResource("/mineGame/timegood.png")).getImage();
    Image timeLineRed = new ImageIcon(getClass().getResource("/mineGame/timepoor.png")).getImage();
    Image buttonBg = new ImageIcon(getClass().getResource("/mineGame/text-background.png")).getImage();
    Image timeLineCenter = new ImageIcon(getClass().getResource("/mineGame/timecenter.png")).getImage();
    Image retryBtn = new ImageIcon(getClass().getResource("/mineGame/replay.png")).getImage();

    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    	g.clearRect(0, 0, (int)width, (int)height);
        double leftPercent=1.0;
		switch (stageState) {
            case PLAYING:
            case PAUSE:
            	g.drawImage(gamebgPic,0,0,(int)width,(int)height,this);
            	g.drawImage(scoreboard,30,20,145,80,this);
            	g.setFont(new Font("Tahoma", Font.BOLD, 28));
            	g.setColor(Color.white);
            	g.drawString(""+requireScore,75,50);
            	g.drawString(""+score,75,90);
            	g.drawImage(timeLineBg,15,115,165,15,this);
            	//前兩秒鐘不改變進度比例以避免進度條異常運動
            	if(totaltime-lifetime>5)
            		leftPercent=(1.0*lifetime)/(1.0*totaltime);
        		g.drawImage(timeLineRed,(int)(20+165*(1.0-leftPercent)),115,180,130,
								(int)((1.0-leftPercent)*timeLineGreen.getWidth(this)),0,(int)timeLineGreen.getWidth(this),(int)timeLineGreen.getHeight(this),this);
            	if((1.0*lifetime)/(1.0*totaltime)>=0.3)
            		g.drawImage(timeLineGreen,(int)(20+165*(1.0-leftPercent)),115,180,130,
            						(int)((1.0-leftPercent)*timeLineGreen.getWidth(this)),0,(int)timeLineGreen.getWidth(this),(int)timeLineGreen.getHeight(this),this);
            	g.setColor(Color.black);
            	try {
                	hook.paint(g);
                } catch (IOException error) {}
                for (Mineral m : mineralList) {
                    m.paint(g);
                }
//                
                g.setColor(Color.red);
                break;
            case MENU:
                g.drawImage(gamebgPic,0,0,(int)width, (int)height, this);
                g.drawImage(buttonBg,310,210,180, 50,this);
                g.drawImage(buttonBg,330,290,150, 50,this);
                g.setFont(new Font("Comic Sans MS",Font.BOLD , 28));
                g.setColor(Color.white);
                g.drawString("Game start",325,242);
                g.drawString("Exit",380,322);
               

                break;
            case CONFIGURE:
                break;
            case GAME_OVER:
            	//畫結束畫面
            	g.drawImage(gameoverPic,0,0,(int)width,(int)height,this);
            	 g.setFont(new Font("Comic Sans MS",  Font.BOLD , 60));
                 g.setColor(Color.white);
                 g.drawString("Game over",350,280);
                g.drawImage(retryBtn, 480,330, 64, 64, this);

                break;
            case WIN:
            	//待改
            	g.drawImage(gameoverPic,0,0,(int)width,(int)height,this);
           	 	g.setFont(new Font("Comic Sans MS",  Font.BOLD , 60));
           	 	g.setColor(Color.white);
           	 	g.drawString("You win!",350,280);
            	
            	
        }
    }
    public void endGame() {
    	
    }

}

