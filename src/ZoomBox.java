import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class ZoomBox extends SolidSprite{
    private double x;
    private double y;
    private double HBwidth;
    private double HBheight;
    private Rectangle2D hitbox;

    private boolean heroInside = false;

    public static ArrayList<ZoomBox> zoomBoxeList;

    public ZoomBox(double x, double y, double HBwidth, double HBheight){
        super("./img/bomb.png", (int)x, (int)y, 0,0, (int)HBwidth, (int)HBheight, 0,0);
        this.x = x;
        this.y = y;
        this.HBwidth = HBwidth;
        this.HBheight = HBheight;
        this.hitbox = new Rectangle2D.Double(this.x , this.y, this.HBwidth, this.HBheight);
        ZoomBox.zoomBoxeList.add(this);
    }

    @Override
    public boolean intersect(SolidSprite other){
        boolean flag = super.intersect(other);
        this.heroInside = flag;
        return flag;
    }

    public boolean isHeroInside(){
        return heroInside;
    }

}
