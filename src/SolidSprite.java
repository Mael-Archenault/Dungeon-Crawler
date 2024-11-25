import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.awt.Graphics;
import java.awt.Color;

public class SolidSprite extends Sprite {

    protected int HBwidth;
    protected int HBheight;
    protected int HBx;
    protected int HBy;

    Rectangle2D hitbox;

    boolean HBactive = true;

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
        if (this.HBactive && other.isHBactive()){
            Rectangle2D otherHitbox = other.getHitbox();
            return this.hitbox.intersects(otherHitbox);
        }
        else{
            return false;
        }

    }

    // Getters //
    @Override
    public String toString(){
        return this.x + " " + this.y + " " + this.HBwidth + " " + this.HBheight;
    }

    public boolean isHBactive(){
        return this.HBactive;
    }

    // Setters //
    public void setHitbox(double x, double y, double width, double height){
        this.hitbox.setRect(x, y, width, height);
    }
    public void setHBinactive(){
        this.HBactive = false;
    }

    @Override
    public void rescale(double scale){
        super.rescale(scale);
        this.HBheight = (int) (this.HBheight * scale);
        this.HBwidth = (int) (this.HBwidth * scale);
        this.HBx = (int) (this.HBx * scale);
        this.HBy = (int) (this.HBy * scale);
    }

    public void destroy() {
        try {
            this.image = ImageIO.read(new File("./img/ground.png"));
            this.setHBinactive();
        }
        catch (Exception ignored) {}
    }

    // Drawing Function //
    public void drawHitbox(Graphics g, double cameraX, double cameraY, double zoom) {
        // In red: the actual hitbox
        g.setColor(Color.RED);
        g.drawRect((int)((cameraX+hitbox.getX())*zoom), (int)((cameraY+hitbox.getY())*zoom), (int)(hitbox.getWidth()*zoom), (int)(hitbox.getHeight()*zoom));
        // In blue: the image borders
        g.setColor(Color.BLUE);
        g.drawRect((int)((cameraX+this.x)*zoom), (int)((cameraY+this.y)*zoom), (int)(this.width*zoom), (int)(this.height*zoom));

        
    }
}
