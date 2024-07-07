package entity;

import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import myFarm.GamePanel;

public class NPC extends Entity{
	
	public NPC(GamePanel gp){
		super(gp);
		direction = "down";
		speed = 1;
		getCharImage();
		setDialogue();
	}
	public void getCharImage() {
		try {
			up1 = ImageIO.read(getClass().getResourceAsStream("/npc/oldman_up_1.png")); 
			up2 = ImageIO.read(getClass().getResourceAsStream("/npc/oldman_up_2.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/npc/oldman_down_1.png")); 
			down2 = ImageIO.read(getClass().getResourceAsStream("/npc/oldman_down_2.png")); 
			left1 = ImageIO.read(getClass().getResourceAsStream("/npc/oldman_left_1.png")); 
			left2 = ImageIO.read(getClass().getResourceAsStream("/npc/oldman_left_2.png")); 
			right1 = ImageIO.read(getClass().getResourceAsStream("/npc/oldman_right_1.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream("/npc/oldman_right_2.png")) ;
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
	
	public void setDialogue() {
		dialogues[0] = "Good Morning, JieJie!";
		dialogues[1] = "Have you had breakfast yet?";
		dialogues[2] = "I bet you haven't. Let me cook you \nsomething.";
		dialogues[3] = "Why not explore the farm and find some \ningredients you like?";
		dialogues[4] = "Go ahead! When you're prepared, meet \nme in the farmhouse!";
	}
	public void speak() {
		super.speak();
	}
}
