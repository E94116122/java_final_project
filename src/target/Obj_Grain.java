package target;

import java.io.IOException;

import javax.imageio.ImageIO;

import myFarm.GamePanel;

public class Obj_Grain extends SuperObject{
	
	GamePanel gp;
	public Obj_Grain(GamePanel gp) {
		this.gp = gp;
		name = "Grain";
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objects/grain.png"));
			uTool.scaleImage(image, gp.tileSize, gp.tileSize);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
