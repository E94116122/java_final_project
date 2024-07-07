package invaders;

import javax.swing.ImageIcon;

public class Alien extends Sprite {

    public Alien(int x, int y) {
        initAlien(x, y);
    }

    private void initAlien(int x, int y) {
        this.x = x;
        this.y = y;

        var alienImg = "res/invaderGame/bean.png";
        var ii = new ImageIcon(alienImg);

        setImage(ii.getImage());
    }

    public void act(int direction) {
        this.x += direction;
    }
}
