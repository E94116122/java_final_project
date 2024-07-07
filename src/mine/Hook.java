package mine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;


/**
 * Created by lzc on 4/2/16.
 * Finalized by czj
 */
public class Hook {
    private double sourceX; //懸掛點
    private double sourceY; 
    private double theta=0.0; //角度
    private double d=0.0; //繩長
    final double r = 30.0; //鉤子大小
    private double weight=800.0; //鉤子本身的重量

    private Mineral mineral;//鉤到的物體
    

    HookState state; //鉤子行進狀態
    enum HookState{WAIT, FORWARD, BACKWARD}
    
    int hookWaitDirection = 1; //控制鉤子晃動的方向

    public Hook(double width, double height){
        sourceX = width/2-10;
        sourceY = height-30; //需要根據背景調節到適合的高度

        state = HookState.WAIT;
    }

    double getX(){
        return sourceX + d * Math.cos(theta);
    }

    double getY(){
        return sourceY + d * Math.sin(theta);
    }

    double getWeight(){
        return mineral == null ? weight : weight 
        		+ mineral.density * mineral.r * mineral.r;
    }

    /*分別計算拉的速度（放的速度偏慢，不能用鉤子的重量来算）*/
    /*TODO 計算参数可以调整*/
    double getPullVelocity(){
    	return 40000.0 / getWeight();
    }    
    double getPushVelocity(){
    	return 20.0;
    }
    
    boolean hasMineral() {
    	return mineral != null;
    }

    /*逐一判斷鉤子是否鉤到物體*/
    /*TODO 鉤子觸發的範圍可調整*/
    boolean hookMineral(Mineral m){
        if(distance(getX(),getY(),m.x,m.y) < ( m.r)){
            mineral = m;
            state = HookState.BACKWARD;
            return true;
        } else {
        	return false;
        }
    }
    

    
    /*每次時間更新循環時更新鉤角度, 速度與鉤子的重量有關; 判断是否抓到礦物 */
    void refresh(Stage stage){
        switch (state){
            case WAIT:
            	theta += hookWaitDirection * Math.PI / GoldMiner.PERIOD;
            	
            	/*控制鉤子的方向，到邊界時轉向*/
            	/*TODO 鉤子晃動的範圍可以调整*/
            	if (theta >= Math.PI * 19 / 20) {
            		hookWaitDirection = -1;
            	}
            	else if (theta <= Math.PI / 20) {
            		hookWaitDirection = 1;
            	}
                break;
                
            case FORWARD:
            	d += getPushVelocity();
            	
            	/*判断是否超出邊界*/
            	if (getX() < 50 || getX() > 750 || getY() > 550) {
            		state = HookState.BACKWARD;
            		break;
            	}
            	
            	/*判断是否钩到物体*/
                for(int i=0; i<stage.mineralList.size(); i++){
                    Mineral testMineral = stage.mineralList.get(i);
                	if(hookMineral(testMineral)){
                    	testMineral.hooked(stage,i);
                    	break;
                    }
                }
                
                /*判断是否碰到炸彈*/
//                for(int i=0, len = stage.bombList.size(); i<len; ++i) {
//                	Bomb testBomb = stage.bombList.get(i);
//                	if (explodeBomb(testBomb)) {
//                		stage.bombList.remove(i);
//                		testBomb.explode();
//                		len = stage.bombList.size();
//                		--i;
//                	}
//                }
                break;
                
            case BACKWARD:
            	d -= getPullVelocity();
            	
            	/*需要判断是超出边界造成的拉回还是鉤到東西造成的拉回*/
            	if (mineral != null){
            		/*给钩到的东西加了个位移*/
            		/*TODO 加位移的多少可以调整*/
            		mineral.refresh(getX() + r * Math.cos(theta), 
            				getY() + r * Math.sin(theta));
            	}
            	
            	/*到達後加分並回到等待狀態*/
            	/*TODO 判断高分物體和低分物體的界限可以调整*/
            	if (d <= 0){
            		if (mineral != null) {
            			stage.score += mineral.value;
            			
            			/*播放声音*/
            			String soundName; //指定播放聲音的文件名
            			if (mineral.value < 50) {
            				soundName = "res/mineGame/low-value.wav";
            			} else if (mineral.value >= 300) {
            				soundName = "res/mineGame/high-value.wav";
            			} else {
            				soundName = "res/mineGame/normal-value.wav";
            			}
            			
            			/*在新thread中播放聲音*/
            			Thread playSound = new Thread(new SoundPlayer(soundName)); 
        				playSound.start();
            			mineral = null;
            		}
            		d = 0;
            		state = HookState.WAIT;
            	}
            	break;
        }
    }

    /*畫線, 鉤子, 鉤到的物體*/
    public void paint(Graphics g) throws IOException{
    	switch (state) {
    	case BACKWARD:
    		if (mineral != null){
    			mineral.paint(g);	// 先畫鉤到的物體，再进入default画鉤子和線
    		}    		
    	default:
    		/*畫線*/
    		Graphics2D g2= (Graphics2D)g;
    		g2.setStroke(new BasicStroke(2.0f)); // 設置線條粗細
        	g2.drawLine((int)sourceX, (int)(sourceY), (int)getX(), (int)getY());
    		/*畫鉤子*/
    		BufferedImage hookImage = ImageIO.read(new File("res/mineGame/hook2.png"));
        	BufferedImage rotatedImage = rotateImage(hookImage, theta);
        	g.drawImage(rotatedImage,
        			(int)(getX() - r), (int)(getY() - r), 2*(int)r, 2*(int)r, null);
    	}    	
    }

    public void launch(){
        if(state==HookState.WAIT)
            state = HookState.FORWARD;
    }

    private static double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
    
    /*旋轉圖片*/
    public static BufferedImage rotateImage(final BufferedImage bufferedimage,
            final double theta) {
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(w, h, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(theta, w / 2, h / 2);
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        return img;
    }    
}

/*播放声音，在獨立thread中執行*/
class SoundPlayer implements Runnable {
	public boolean canplay=true;
	private String soundName;
	SoundPlayer(String soundName) {
		this.soundName = soundName;
	}
	
	public void run() {
		final File file = new File(soundName);

        try {
            final AudioInputStream in = 
            		AudioSystem.getAudioInputStream(file);
            
            final int ch = in.getFormat().getChannels();  
            final float rate = in.getFormat().getSampleRate();  
            final AudioFormat outFormat = new AudioFormat(
            		AudioFormat.Encoding.PCM_SIGNED, rate,
            		16, ch, ch * 2, rate, false);
            
            final DataLine.Info info = 
            		new DataLine.Info(SourceDataLine.class, outFormat);
            final SourceDataLine line = 
            		(SourceDataLine) AudioSystem.getLine(info);

            if (line != null) {
                line.open(outFormat);
                line.start();
                canplay=true;
                final byte[] buffer = new byte[65536];  
                for (int n = 0; n != -1&&canplay;
                		n = AudioSystem
                				.getAudioInputStream(outFormat, in)
                				.read(buffer, 0, buffer.length)) {  
                    line.write(buffer, 0, n);
                }
                line.drain();
                line.stop();
            }
            line.close();
            in.close();
            
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
	}
}
