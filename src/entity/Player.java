package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import myFarm.GamePanel;
import myFarm.KeyHandler;
import myFarm.UtilityTool;
import tile.Tile;
public class Player extends Entity{
	
		KeyHandler keyH;
		
		public final int screenX;
		public final int screenY;
		public int hasStrawberry = 0;
		public int hasCarrot = 0;
		public int hasGrain = 0;
		public int hasBean = 0;
		public int hasCorn = 0;
		public int hasEgg = 0;
		
//		public static class PlayerState {
//	        int x, y;
//	        int score;
//	        // 其他需要保存的狀態
//	    }
		

		
		public Player(GamePanel gp, KeyHandler keyH) { 
			super(gp); //calling the constructor of the super class of this class
			this.keyH = keyH;
			
			screenX = gp.screenWidth/2 - (gp.tileSize/2);   
			screenY = gp.screenHeight/2 - (gp.tileSize/2);
			
			solidArea = new Rectangle();
			solidArea.x = 8;
			solidArea.y = 16;
			solidAreaDefaultX = solidArea.x;
			solidAreaDefaultY = solidArea.y;
			solidArea.width = 32;
			solidArea.height = 32;
			
			setDefaultValues();
			getPlayerImage();
		}
		
		public void setDefaultValues() {
			worldX = gp.tileSize*22; //starting position
			worldY = gp.tileSize*33; 
			speed = 4;
			direction = "down";
		} 
		public void getPlayerImage() {

			
			up1 = setUp("boy_up_1"); 
			up2 = setUp("boy_up_2");
			down1 = setUp("boy_down_1");
			down2 = setUp("boy_down_2");
			left1 = setUp("boy_left_1");
			left2 = setUp("boy_left_2");
			right1 = setUp("boy_right_1");
			right2 = setUp("boy_right_2");
		}
		
		public BufferedImage setUp(String imageName) {
			UtilityTool uTool = new UtilityTool();
			BufferedImage image = null;
			
			try {
			
				image = ImageIO.read(getClass().getResourceAsStream("/player/"+ imageName +".png"));
				image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
				
			}catch(IOException e) {
				e.printStackTrace();
			}
			return image;
		}
		
		public void update() {    //called 60 times per second
			
			if (keyH.upPressed == true || keyH.downPressed == true ||
				keyH.leftPressed == true || keyH.rightPressed == true) {
				
				if (keyH.upPressed == true) { 
					direction = "up";
				}
				else if(keyH.downPressed == true) { 
					direction = "down";	
				}
				else if(keyH.leftPressed == true) {
					direction = "left";
				}
				else if (keyH.rightPressed == true) {
					direction = "right";	
				} 
				
				//check tile collision
				collisionOn = false;
			    inCarrot = false;
			    inBean = false;
			    inCorn = false;
				gp.colliD.checkTile(this);  //player is subclass of entity
				
				//check object collision
				int objIndex = gp.colliD.checkObject(this, true);
				collectObject(objIndex);
				
				//check npc collision
				int npcIndex = gp.colliD.checkEntity(this, gp.npc);
				interactNPC(npcIndex);
				
				//check chicken collision
				int chickenIndex = gp.colliD.checkEntity(this, gp.chicken);
				interactChicken(chickenIndex);
				
				if(collisionOn == false) {  //if collision is false, player can move
					switch(direction) {
					case "up":
						worldY -= speed;
						break;
					case"down":
						worldY += speed;
						break;
					case "left":
						worldX -= speed;
						break;
					case "right":
						worldX += speed;
						break;
					}
		
				}
				
				
				spriteCounter++;
				if(spriteCounter>12) {    //changes every 10 frames
					if(spriteNum==1) {
						spriteNum=2;
					}
					else if(spriteNum==2) {
						spriteNum=1;
					}
					spriteCounter=0;
				}
			}
			if(inCarrot && keyH.spacePressed) {
				System.out.println("in carrot game");
				gp.gameState=gp.mineGameState;
				gp.startMineGame();
			}
			else if(inBean && keyH.spacePressed) {
				System.out.println("in bean game");
				gp.gameState=gp.beanGameState;
				gp.startSpaceInvadersGame();
			}
			else if(inCorn && keyH.spacePressed) {
				gp.gameState=gp.snakeGameState;
				gp.startSnakeGame();
				//System.out.println("in corn game");
			}

		}
		
		public void collectObject(int index) {
			if(index!=999) {
				//gp.obj[index] = null;
				String objName = gp.obj[index].name;
				
				switch(objName) {
				case "Strawberry":
					gp.obj[index] = null;
					hasStrawberry++;
					System.out.println("Strawberry:"+hasStrawberry);
					gp.ui.showMessage("got a strawberry");
	
					
					break;
				case "Grain":
					if(keyH.spacePressed) {
						gp.obj[index] = null;
						hasGrain++;
						System.out.println("Grain:"+hasGrain);
						if(hasGrain<=10) {
							gp.ui.showMessage("Get more grain!");
						}else {
							gp.ui.showMessage("Enough grain!");
						}
					}
					
					break;
				case "Milk":
					break;
				case "Farmhouse":
					if(hasCorn!=0 && hasBean!=0 && hasCarrot!=0 && hasGrain>=10 && hasEgg!=0) {
						System.out.println("In the farmhouse");
						//gp.ui.gameFinished = true;
						gp.obj[index] = null;
							
						gp.gameState = gp.endState;
					}

					
					break;
				}
				
			}
		}
		
		public void interactNPC(int i) {
			if(i!=999) {
				System.out.println("You hit a npc!!!");
				gp.gameState = gp.dialogueState;
				gp.npc[i].speak();
			}
		}
		
		public void interactChicken(int i) {
			if(i!=999) {
				System.out.println("You hit a chicken!!!");
				//gp.startEggGame();
				gp.gameState = gp.eggGameState;
				gp.startEggGame();
				//gp.npc[i].speak();
			}
		}
		
		public void draw(Graphics2D g2) {
//			g2.setColor(Color.white) ; 
//			g2.fillRect(x, y ,gp.tileSize, gp.tileSize);
			
			BufferedImage image = null;
			switch (direction) {
			case "up": 
				if(spriteNum==1) {
					image=up1;
				}
				if(spriteNum==2) {
					image=up2;
				}
				break;
			case "down": 
				if(spriteNum==1) {
					image=down1;
				}
				if(spriteNum==2) {
					image=down2;
				}
				break;
			case "left":
				if(spriteNum==1) {
					image=left1;
				}
				if(spriteNum==2) {
					image=left2;
				}
				break;
			case "right": 
				if(spriteNum==1) {
					image=right1;
				}
				if(spriteNum==2) {
					image=right2;
				}
				break;
			}
			//g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			g2.drawImage(image, screenX, screenY, null);
		}
//		
//		public void setState(PlayerState state) {
//		    this.x = state.x;
//		    this.y = state.y;		        
//		    this.score = state.score;
//		    // 恢復其他狀態
//		}
	}


