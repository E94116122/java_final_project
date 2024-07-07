package myFarm;

import entity.Chicken;
import entity.NPC;
import target.Obj_Farmhouse;
import target.Obj_Grain;
import target.Obj_Milk;
import target.Obj_Strawberry;

public class AssetSetter {
	
	GamePanel gp;
	public AssetSetter(GamePanel gp) {
		this.gp = gp; 
	}
	public void setObject(){
//		gp.obj[0] = new Obj_Strawberry(gp);
//		gp.obj[0].worldX = 14*gp.tileSize;
//		gp.obj[0].worldY = 15*gp.tileSize;
//		
//		gp.obj[1] = new Obj_Milk();
//		gp.obj[1].worldX = 36*gp.tileSize;
//		gp.obj[1].worldY = 9*gp.tileSize;
		
		gp.obj[2] = new Obj_Farmhouse();
		gp.obj[2].worldX = 52*gp.tileSize;
		gp.obj[2].worldY = 50*gp.tileSize;
		
		setGrain(3, 44, 35);
		setGrain(4, 46, 35);
		setGrain(5, 48, 35);
		setGrain(6, 50, 35);
		setGrain(7, 45, 36);
		setGrain(8, 47, 36);
		setGrain(9, 49, 36);
		setGrain(10, 51, 36);
		setGrain(11, 44, 37);
		setGrain(12, 46, 37);
		setGrain(13, 45, 38);
		setGrain(14, 47, 38);
		setGrain(15, 44, 39);
		setGrain(16, 46, 39);
		setGrain(17, 45, 40);
		setGrain(18, 47, 40);
		setGrain(19, 44, 41);
		setGrain(20, 46, 41);
		setGrain(21, 45, 42);
		setGrain(22, 47, 42);
		setGrain(23, 44, 43);
		setGrain(24, 46, 43);
		setGrain(25, 45, 44);
		setGrain(26, 47, 44);
		setGrain(27, 44, 45);
		setGrain(28, 46, 45);
		setGrain(29, 45, 46);
		setGrain(30, 47, 46);
		setGrain(31, 49, 46);
		setGrain(32, 51, 46);
		setGrain(33, 44, 47);
		setGrain(34, 46, 47);
		setGrain(1, 48, 47);
		setGrain(0, 50, 47);
		
	}
	public void setNPC() {
		gp.npc[0] = new NPC(gp);
		gp.npc[0].worldX = 35*gp.tileSize;
		gp.npc[0].worldY = 35*gp.tileSize;
		
	}
	public void setChicken() {
		gp.chicken[0] = new Chicken(gp);
		gp.chicken[0].worldX = 38*gp.tileSize;
		gp.chicken[0].worldY = 12*gp.tileSize;
		
		gp.chicken[1] = new Chicken(gp);
		gp.chicken[1].worldX = 40*gp.tileSize;
		gp.chicken[1].worldY = 17*gp.tileSize;
		
		gp.chicken[2] = new Chicken(gp);
		gp.chicken[2].worldX = 38*gp.tileSize;
		gp.chicken[2].worldY = 24*gp.tileSize;
		
		gp.chicken[3] = new Chicken(gp);
		gp.chicken[3].worldX = 40*gp.tileSize;
		gp.chicken[3].worldY = 21*gp.tileSize;
		
		gp.chicken[4] = new Chicken(gp);
		gp.chicken[4].worldX = 38*gp.tileSize;
		gp.chicken[4].worldY = 9*gp.tileSize;
		
		
		
	}
	public void setGrain(int index, int x, int y) {
		gp.obj[index] = new Obj_Grain(gp);
		gp.obj[index].worldX = x*gp.tileSize;
		gp.obj[index].worldY = y*gp.tileSize;
	}
}
