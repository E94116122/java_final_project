package tile;


import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import myFarm.GamePanel;
import myFarm.UtilityTool;

public class TileManager {
	GamePanel gp;
	public Tile[] tiles;
	public int mapTileNum[][];
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		tiles = new Tile[10];
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];  
	   	
		getTileImage();
		//loadMap("/maps/map_01.txt");
		loadMap("/maps/world_01.txt");
	}
	
	public void getTileImage(){
//		try {
//			tiles[0] = new Tile();
//			tiles[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
			setUp(0, "grass", false, false, false, false, false);
			
//			tiles[1] = new Tile();
//			tiles[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
//			tiles[1].collision = true;
			setUp(1, "wall", true, false, false, false, false);
			
//			tiles[2] = new Tile();
//			tiles[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
//			tiles[2].collision = true;
			setUp(2, "water", true, false, false, false, false);
			
//			tiles[3] = new Tile(); 
//			tiles[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/carrot.png"));
//			tiles[3].carrotField = true;
			setUp(3, "carrot", false, true, false, false, false);
			
//			tiles[4] = new Tile();
//			tiles[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));
			setUp(4, "sand", false, false, false, false, false);
			
//			tiles[5] = new Tile();
//			tiles[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
//			tiles[5].collision = true;
			setUp(5, "tree", true, false, false, false, false);
			
//			tiles[6] = new Tile(); 
//			tiles[6].image = ImageIO.read(getClass().getResourceAsStream("/tiles/bean.png"));
//			tiles[6].beanField = true;
			setUp(6, "bean", false, false, true, false, false);
			
//			tiles[7] = new Tile();  
//			tiles[7].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grain.png"));
//			tiles[7].grainField = true;
			setUp(7, "grain", false, false, false, true, false);
			
//			tiles[8] = new Tile(); 
//			tiles[8].image = ImageIO.read(getClass().getResourceAsStream("/tiles/corn.png"));
//			tiles[8].cornField = true;
			setUp(8, "corn", false, false, false, false, true);
			
//			tiles[9] = new Tile();  
//			tiles[9].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
//			tiles[9].collision = true;
			
//			
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public void setUp(int index,String tileName, boolean collision, boolean carrotField, boolean beanField, boolean grainField, boolean cornField) {
		UtilityTool uTool = new UtilityTool();
		
		try {
			tiles[index] = new Tile();
			tiles[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+ tileName +".png"));
			tiles[index].image = uTool.scaleImage(tiles[index].image, gp.tileSize, gp.tileSize);
			tiles[index].collision = collision;
			tiles[index].carrotField = carrotField;
			tiles[index].beanField = beanField;
			tiles[index].cornField = cornField;
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String filePath)
	{
		try {
			InputStream inputS = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputS));
			
			int col = 0;
			int row = 0;
			while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				String line = br.readLine();
				while(col < gp.maxWorldCol){
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row]=num;
					col++;
				}
				if(col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			br.close();
			
		}catch(Exception e){
			 
		}
	}
	
	public void draw(Graphics2D g2) {
//		g2.drawImage(tiles[0].image, 0, 0, gp.tileSize, gp.tileSize, null);
//		g2.drawImage(tiles[1].image, 48, 0, gp.tileSize, gp.tileSize, null);
//		g2.drawImage(tiles[2].image, 96, 0, gp.tileSize, gp.tileSize, null);
		
		int worldCol = 0;
		int worldRow = 0;
//		int x = 0;
//		int y = 0;
		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			int tileNum = mapTileNum[worldCol][worldRow];
			int worldX = worldCol*gp.tileSize;
			int worldY = worldRow*gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
			   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
		       worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
			   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
				//g2.drawImage(tiles[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
				g2.drawImage(tiles[tileNum].image, screenX, screenY, null);
			}
			
			worldCol++;
//			x+=gp.tileSize;
			
			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
//				x = 0;
				worldRow++;
//				y+=gp.tileSize;
			}
		}
	}
		
}
