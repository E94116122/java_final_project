package mine;


import java.awt.*;

import javax.swing.ImageIcon;


/**
 * Created by lzc on 4/2/16.
 * 杨安：为了构造不同类别的矿物对象简单定义了父类的构造方法以及子类
 * Mouse added by czj
 */
public abstract class Mineral {
    double x;
    double y;
    double r;
    int value;
    int density;
    Mineral(double x,double y,double r,int value,int density){
    	this.x=x;
    	this.y=y;
    	this.r=r;
    	this.value=value;
    	this.density=density;
    }
    abstract void paint(Graphics g);
    
    /*加入了更新矿物位置的函数，在被勾住之后hook通过该函数使其位置保持同步*/
    void refresh(double newX, double newY) {
    	x = newX;
    	y = newY;
    }

    /*被钩到时的事件, 默认情况为从矿物列表中清除该矿物; 可以重写增加音效,动画,以及炸药桶爆炸的特殊事件*/
    void hooked(Stage stage, int i){
    	stage.mineralList.remove(i);
    }
}

/*class Stone extends Mineral{
	Stone(double x, double y, double r, int value) {
		super(x, y, r, value, 5);
	}
	void paint(Graphics g) {
		Image icon = new ImageIcon("res/images/mine_rock_b.png").getImage();
		g.drawImage(icon, (int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r), null);
	}
}*/

class Carrot extends Mineral{
	Carrot(double x, double y, double r, int value) {
		super(x, y, r, value, 5);
		// TODO 自动生成的构造函数存根
	}
	void paint(Graphics g) {
		Image icon = new ImageIcon("res/mineGame/carrot_b.png").getImage();
		g.drawImage(icon, (int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r), null);
	}
}

class MovingCarrot extends Mineral {
	int movingDirection;
	double movingSpeed;
	int paintCount; //用来選胡蘿蔔貼圖，逐幀更换来制造動畫效果
	boolean isHooked;
	boolean withDiamond; //用来區分带鑽石的老鼠和不待鑽石的老鼠
	
	MovingCarrot(double x, double y, double r, int value,
			int movingDirection, double movingSpeed, boolean withDiamond) {
		super(x, y, r, value, 1);
		this.movingDirection = movingDirection;
		this.movingSpeed = movingSpeed;
		this.paintCount = 0;
		this.isHooked = false;
	}
	
	/*更新老鼠的位置*/
	void runCarrot() {
		x += movingDirection * movingSpeed;
		if (x <= 0 || x >= 800) {
			movingDirection  = -movingDirection;
		}
	}
	
	void hooked(Stage stage, int i){
    	stage.mineralList.remove(i);
    	isHooked = true;
    }
	
	void paint(Graphics g) {
		String suffix;
		if (movingDirection>0) {
			suffix = new String("_carrot_right.png");
		} else {
			suffix = new String("_carrot_left.png");
		}
		
		Image icon = new ImageIcon("res/mineGame/"+(paintCount+1)+ suffix).getImage();
		//System.out.println(paintCount);
		/*只有没有被勾到的时候胡蘿蔔才會動*/
		if (!isHooked){
			paintCount += movingSpeed / 7 + 1; // 當胡蘿蔔走的速度比较快的時候腿也要動得快一些
			paintCount = paintCount %4;
		}
		g.drawImage(icon, (int)(x-2*r), (int)(y-r), (int)(4*r), (int)(4*r), null);
	}
}
