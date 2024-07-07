package target;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Obj_Milk extends SuperObject{
	public Obj_Milk() {
		name = "Milk";
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objects/milk.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
