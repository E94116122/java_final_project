package myFarm;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	
	GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed;
	public boolean checkDrawTime = false; //debug
	public boolean spacePressed = false;
	public boolean enterPressed = false;
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		//titlestate
		if (gp.gameState == gp.startState) {
			if(code == KeyEvent.VK_W) {
				if(gp.ui.startChoice>0) {
					gp.ui.startChoice--;
				}
				else {
					gp.ui.startChoice=1;
				}
			}
			if(code == KeyEvent.VK_S) {
				if(gp.ui.startChoice<1) {
					gp.ui.startChoice++;
				}
				else {
					gp.ui.startChoice=0;
				}
			}
			
			if(code == KeyEvent.VK_ENTER) {
				if(gp.ui.startChoice==0) {
					gp.gameState = gp.playState;
					//gp.playMusic(0);
				}
//				else if(gp.ui.startChoice==1) {
//					gp.gameState = gp.instructionState;
//					System.out.println("in instruc.");
//					//gp.ui.draw(g2);
//				}
				else if(gp.ui.startChoice==1) {
					System.exit(0);
				}

			}
		}
		
//		if(gp.gameState==gp.instructionState) {
//			if(code == KeyEvent.VK_ENTER) {
//				gp.gameState = gp.startState;
//			}
//		}
		
		//playstate
		if (gp.gameState == gp.playState) {
			if(code == KeyEvent.VK_W) {
				upPressed = true;
			}
			if(code == KeyEvent.VK_S) {
				downPressed = true;
			}
			if(code == KeyEvent.VK_A) {
				leftPressed = true;
			}
			if(code == KeyEvent.VK_D) {
				rightPressed = true;
			}
			if(code == KeyEvent.VK_SPACE) {
				spacePressed = true;
			}
			if(code == KeyEvent.VK_P) {
				//if(gp.gameState== gp.playState) {
					gp.gameState = gp.pauseState;
				//}
				//else if(gp.gameState == gp.pauseState) {
					//gp.gameState = gp.playState;
				//}
			}
			if(code == KeyEvent.VK_T) {
				if(checkDrawTime==false) {
					checkDrawTime = true;
				}else if(checkDrawTime==true) {
					checkDrawTime = false;
				}
			}
		}
		else if(gp.gameState==gp.dialogueState) {
			if(code == KeyEvent.VK_ENTER) {
				//enterPressed = true;
				if (gp.dialogueEnd) {
		            gp.gameState = gp.playState;
		            gp.dialogueEnd = false;
		        } else {
		            gp.npc[0].speak();
		        }
					//gp.gameState=gp.playState;
				
			}
		}
		else if(gp.gameState==gp.pauseState) {
			if(code == KeyEvent.VK_P) {
				gp.gameState = gp.playState;
				
			}
		}
		else if(gp.gameState==gp.endState) {
			if(code == KeyEvent.VK_ENTER) {
				if (gp.gameEnd) {
		            System.exit(0);
		        }
				else {
		            //gp.ui.displayEndingLines();
		        	gp.ui.lineIndex++;
		    		if(gp.ui.lineIndex==4) {
		    			gp.gameEnd=true;
		    			System.out.println("game finish");
		    		}
		        }
				
			}
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_W) {
			upPressed = false;
		}
		if(code == KeyEvent.VK_S) {
			downPressed = false;
		}
		if(code == KeyEvent.VK_A) {
			leftPressed = false;
		}
		if(code == KeyEvent.VK_D) {
			rightPressed = false;
		}
		if(code == KeyEvent.VK_SPACE) {
			spacePressed = false;
		}
		if(code == KeyEvent.VK_ENTER) {
			enterPressed = false;
		}
		
	}

}
