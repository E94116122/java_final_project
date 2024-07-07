package invaders;

import java.awt.Image;

public class Sprite {

    private boolean visible;  // 可見性標誌，表示精靈是否可見
    private Image image;  // 圖像對象，用於表示精靈的圖像
    private boolean dying;  // 死亡標誌，表示精靈是否正在死亡

    int x;  // x坐標，表示精靈的水平位置
    int y;  // y坐標，表示精靈的垂直位置
    int dx;  // 水平速度，表示精靈的水平移動速度

    public Sprite() {
        visible = true;  // 初始化時，精靈可見
    }

    public void die() {
        visible = false;  // 精靈死亡時，設置為不可見
    }

    public boolean isVisible() {
        return visible;  // 返回精靈的可見性
    }

    protected void setVisible(boolean visible) {
        this.visible = visible;  // 設置精靈的可見性
    }

    public void setImage(Image image) {
        this.image = image;  // 設置精靈的圖像
    }

    public Image getImage() {

        return image;  // 返回精靈的圖像
    }

    public void setX(int x) {
        this.x = x;  // 設置精靈的x坐標
    }

    public void setY(int y) {
        this.y = y;  // 設置精靈的y坐標
    }

    public int getY() {
        return y;  // 返回精靈的y坐標
    }

    public int getX() {
        return x;  // 返回精靈的x坐標
    }

    public void setDying(boolean dying) {
        this.dying = dying;  // 設置精靈的死亡狀態
    }

    public boolean isDying() {
        return this.dying;  // 返回精靈的死亡狀態
    }
}
