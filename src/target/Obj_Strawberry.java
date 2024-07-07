package target;

import java.io.IOException;

import javax.imageio.ImageIO;

import myFarm.GamePanel;

public class Obj_Strawberry extends SuperObject{
	GamePanel gp;
	public Obj_Strawberry(GamePanel gp) {
		this.gp = gp;
		name = "Strawberry";
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objects/strawberry.png"));
			uTool.scaleImage(image, gp.tileSize, gp.tileSize);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
