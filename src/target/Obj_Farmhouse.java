package target;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Obj_Farmhouse extends SuperObject{
	public Obj_Farmhouse() {
		name = "Farmhouse";
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objects/farmhouse.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		collision = true;
	}

}
