package entity;

import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import myFarm.GamePanel;

public class Chicken extends Entity{
	public Chicken(GamePanel gp){
		super(gp);
		direction = "down";
		speed = 1;
		getCharImage();
	}
	public void getCharImage() {
		try {
			up1 = ImageIO.read(getClass().getResourceAsStream("/npc/chicken_up_1.png")); 
			up2 = ImageIO.read(getClass().getResourceAsStream("/npc/chicken_up_2.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/npc/chicken_down_1.png")); 
			down2 = ImageIO.read(getClass().getResourceAsStream("/npc/chicken_down_2.png")); 
			left1 = ImageIO.read(getClass().getResourceAsStream("/npc/chicken_left_1.png")); 
			left2 = ImageIO.read(getClass().getResourceAsStream("/npc/chicken_left_2.png")); 
			right1 = ImageIO.read(getClass().getResourceAsStream("/npc/chicken_right_1.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream("/npc/chicken_right_2.png")) ;
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setAction() {  //if subclass has same method it takes priority
		actionLookCounter++;
		
		if(actionLookCounter==120) {
			Random random = new Random();
			int i = random.nextInt(100)+1;  //1~100
			
			if(i<=25) {
				direction = "up";
			}else if(i>25 && i<=50) {
				direction = "down";
			}else if(i>50 && i<=75) {
				direction = "left";
			}else if(i>75 && i<=100){
				direction = "right";
			}
			actionLookCounter = 0;
		}
		
	}

}
