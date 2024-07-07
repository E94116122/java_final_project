package myFarm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import target.Obj_Grain;
import target.Obj_Strawberry;

public class UI {
	
	GamePanel gp;
	Graphics2D g2;
	Font pixelsF, cuteF;
	BufferedImage carrotImage;
	BufferedImage grainImage;
	BufferedImage cornImage;
	BufferedImage beanImage;
	BufferedImage eggImage;
	public boolean messageOn = false;
	public String message = "";
	int messageTimer = 0;
	public boolean gameFinished = false;
	public String currentDialogue;
	public int startChoice = 0;
	String lines[] = new String[10];
	int lineIndex=0;
	
	double playTime;
	DecimalFormat dFormat = new DecimalFormat("#0.00");
	
	public UI(GamePanel gp) {
		this.gp = gp;
		
		try {
			InputStream is = getClass().getResourceAsStream("/font/Pixels Regular.ttf");
			pixelsF = Font.createFont(Font.TRUETYPE_FONT,  is);
			is = getClass().getResourceAsStream("/font/Cute Font Regular.ttf");
			cuteF = Font.createFont(Font.TRUETYPE_FONT,  is);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Obj_Strawberry sb =  new Obj_Strawberry(gp);  //carrot image
		//carrotImage = sb.image; //carrot image
		Obj_Grain g =  new Obj_Grain(gp);  //carrot image
		grainImage = g.image; //carrot image
		try {
			cornImage = ImageIO.read(getClass().getResourceAsStream("/objects/corn.png"));
			carrotImage = ImageIO.read(getClass().getResourceAsStream("/objects/carrot.png"));
			beanImage = ImageIO.read(getClass().getResourceAsStream("/objects/bean.png"));
			eggImage = ImageIO.read(getClass().getResourceAsStream("/objects/egg.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void showMessage(String text) {
		message = text;
		messageOn = true;
		
				
	}
	
	public void draw(Graphics2D g2) {
		
		if(gameFinished == true) {
			
			
//			text = "You used"+dFormat.format(playTime)+"secs!";
//			textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
//			x = gp.screenWidth/2 - textLength/2;
//			y = gp.screenHeight/2 + (gp.tileSize*4);
//			g2.drawString(text, x, y);
			

		}
		else {
			this.g2 = g2;
			g2.setFont(pixelsF);
			g2.setColor(Color.white);
//			g2.drawImage(carrotImage,  gp.tileSize/2, gp.tileSize/2, gp.tileSize , gp.tileSize, null);
//			g2.drawString("= "+ gp.player.hasStrawberry, 75, 65);
			if(gp.player.hasCarrot>0) {
				g2.drawImage(carrotImage,  gp.tileSize/2, gp.tileSize/2, gp.tileSize , gp.tileSize, null);
			}
			if(gp.player.hasGrain>=10) {
				g2.drawImage(grainImage,  gp.tileSize/2+gp.tileSize, gp.tileSize/2, gp.tileSize , gp.tileSize, null);
			}
			if(gp.player.hasCorn>0) {
				g2.drawImage(cornImage,  gp.tileSize/2+gp.tileSize*2, gp.tileSize/2, gp.tileSize , gp.tileSize, null);
			}
			if(gp.player.hasEgg>0) {
				
				g2.drawImage(eggImage,  gp.tileSize/2+gp.tileSize*3, gp.tileSize/2, gp.tileSize , gp.tileSize, null);
			}
			if(gp.player.hasBean>0) {
				//gp.ui.showMessage("You're not supposed to get the beans:(");
				g2.drawImage(beanImage,  gp.tileSize/2+gp.tileSize*4, gp.tileSize/2, gp.tileSize , gp.tileSize, null);				
			}
			//time
//			playTime += (double)1/60;
//			g2.drawString("Time"+dFormat.format(playTime), gp.tileSize*12, 65);
			
			if(messageOn==true) {
				g2.setFont(g2.getFont().deriveFont(30F));
				g2.drawString(message, gp.tileSize/2, gp.tileSize*5);
				
				messageTimer++;
				
				if(messageTimer>120) {
					messageTimer=0;
					messageOn = false;
				}
			}
			if(gp.gameState == gp.startState) {
				drawStartScreen();
			}
			if(gp.gameState == gp.playState) {
				
			}
			if(gp.gameState == gp.pauseState) {
				drawPauseScreen();
			}
			if(gp.gameState == gp.dialogueState) {
				drawDialogueScreen();
			}
			if(gp.gameState == gp.endState) {
				drawEndScreen();
			}
//			if(gp.gameState == gp.instructionState) {
//				System.out.println("ready to draw");
//				drawInstructionScreen();
//			}
		}
		
	}
	
	public void drawStartScreen() {
		
		UtilityTool uTool = new UtilityTool();
		BufferedImage farmImage;
		
		
		try {
		
			farmImage = ImageIO.read(getClass().getResourceAsStream("/background/farm.png"));
			farmImage = uTool.scaleImage(farmImage, gp.screenWidth, gp.screenHeight);
			g2.drawImage(farmImage, 0, 0, null);
			
		}catch(IOException e) {
			e.printStackTrace();
		}

		
		g2.setColor(new Color(255, 255, 255, 220));
		g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);

		g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
		String text = "JieJie's Farm";
		int x = getCenteredTextX(text);
		int y = gp.tileSize*3;
		
		//shadow
		g2.setColor(Color.gray);
		g2.drawString(text, x+5, y+5);
		
		g2.setColor(Color.black);
		g2.drawString(text, x, y);
		
		//image
		x = gp.screenWidth/2-gp.tileSize;
		y+=gp.tileSize*2;
		g2.drawImage(gp.player.down2, x, y, gp.tileSize*2, gp.tileSize*2, null);
		
		//Menu
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));
		
		text = "START";
		x = getCenteredTextX(text);
		y+=gp.tileSize*4;
		g2.drawString(text, x, y);
		if(startChoice==0) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
//		text = "INSTRUCTION";
//		x = getCenteredTextX(text);
//		y+=gp.tileSize;
//		g2.drawString(text, x, y);
//		if(startChoice==1) {
//			g2.drawString(">", x-gp.tileSize, y);
//		}
		
		
		text = "QUIT";
		x = getCenteredTextX(text);
		y+=gp.tileSize;
		g2.drawString(text, x, y);
		if(startChoice==1) {
			g2.drawString(">", x-gp.tileSize, y);
		}
	}
	
	public void drawInstructionScreen() {
		System.out.println("start drawing");
		String text;
		g2.setColor(new Color(0, 0, 0));
		g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);
		text = "WASD to move\nSPACE to collect the crops\nP to pause/play\nENTER to finish the dialogue";
		int x = getCenteredTextX(text);
		int y = gp.tileSize*3;
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));
		g2.drawString(text, x, y);
		
	}
	
	public void drawDialogueScreen() {
		int x = gp.tileSize*2;
		int y = gp.tileSize/2;
		int width = gp.screenWidth - (gp.tileSize*4);
		int height=gp.tileSize*5-gp.tileSize/2;
		
		drawWindow(x, y, width, height);
		g2.setFont(cuteF);
		g2.setFont(g2.getFont().deriveFont(42F));
		x+=gp.tileSize;
		y+=gp.tileSize+gp.tileSize/2;
		for(String line: currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y+=35;
		}
		
		
	}
	
	public void drawPauseScreen() {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
		String text = "PAUSED";
		int x = getCenteredTextX(text);
		int y = gp.screenHeight/2;
		
		g2.drawString(text, x, y);
		
	}
	
	public void drawEndScreen() {
		
		//System.out.println("draw end screen1");
		UtilityTool uTool = new UtilityTool();
		BufferedImage houseImage, npcImage;
		
		try {
			houseImage = ImageIO.read(getClass().getResourceAsStream("/background/inHouse.png"));
			houseImage = uTool.scaleImage(houseImage, gp.screenWidth, gp.screenHeight);
			g2.drawImage(houseImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
			
			g2.setColor(new Color(255, 255, 255, 180));
			g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);
			
			npcImage = ImageIO.read(getClass().getResourceAsStream("/npc/oldman_down_1.png"));
			npcImage = uTool.scaleImage(npcImage, gp.tileSize*3, gp.tileSize*3);
			g2.drawImage(npcImage, gp.screenWidth/2-gp.tileSize, gp.screenHeight/2+gp.tileSize, null);
		
		}catch(IOException e) {
			e.printStackTrace();
		}
		//System.out.println("draw end screen2");
		
		displayEndingLines();

	}
	
	public int getCenteredTextX(String text) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth/2 - length/2;
		return x;
	}
	
	public void drawWindow(int x, int y, int width, int height) {
		
		Color c = new Color(0,0,0,180);  //220 is the transparency
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
	}
	
	public void displayEndingLines() {
		lines[0] = "Welcome back, JieJie!";
		lines[1] = "You are great at playing games!";
		lines[2] = "Here's the fried rice for you...";
		lines[3] = "WITHOUT BEANS :)";
		lines[4] = "Mission completed!!!";
		lines[5] = "";
		
		currentDialogue = lines[lineIndex];

		drawDialogueScreen();



	}
}
