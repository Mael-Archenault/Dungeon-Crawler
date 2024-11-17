import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.awt.Graphics;
import java.awt.Color;

public class SolidSprite extends Sprite {

    public int HBwidth;
    int HBheight;
    int HBx;
    int HBy;

    Rectangle2D hitbox;

    int HBPoint1x;
    int HBPoint1y;
    int HBPoint2x;
    int HBPoint2y;
    int HBPoint3x;
    int HBPoint3y;
    int HBPoint4x;
    int HBPoint4y;

    public SolidSprite(String filePath, int x, int y,double width, double height, int HBwidth, int HBheight, int HBx, int HBy) {
        super(filePath, x, y, width, height);

        this.HBwidth = HBwidth;
        this.HBheight = HBheight;

        this.HBx = HBx;
        this.HBy = HBy;

        hitbox = new Rectangle2D.Double(x +HBx, y + HBy, HBwidth, HBheight);


    }

    public Rectangle2D getHitbox(){
        return hitbox;
    }

    public boolean intersect(SolidSprite other){
        Rectangle2D otherHitbox = other.getHitbox();
        return this.hitbox.intersects(otherHitbox);
    }
    public void updateHitbox(double x, double y, double width, double height){
        this.hitbox.setRect(x, y, width, height);
    }

    @Override
    public void rescale(double scale){
        super.rescale(scale);
        this.HBheight *= scale;
        this.HBwidth *= scale;
        this.HBy *= scale;
        this.HBx *= scale;
    }

    public void drawHitbox(Graphics g, double cameraX, double cameraY, double zoom) {
        g.setColor(Color.RED);
        g.drawRect((int)((cameraX+hitbox.getX())*zoom), (int)((cameraY+hitbox.getY())*zoom), (int)(hitbox.getWidth()*zoom), (int)(hitbox.getHeight()*zoom));

        g.setColor(Color.BLUE);
        g.drawRect((int)((cameraX+this.x)*zoom), (int)((cameraY+this.y)*zoom), (int)(this.width*zoom), (int)(this.height*zoom));

        
    }

    @Override
    public String toString(){
        return this.x + " " + this.y + " " + this.HBwidth + " " + this.HBheight;
    }


}
