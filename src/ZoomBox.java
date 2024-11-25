import java.util.ArrayList;

public class ZoomBox extends SolidSprite{

    private boolean heroInside = false;

    public static ArrayList<ZoomBox> zoomBoxeList;

    public ZoomBox(double x, double y, double HBwidth, double HBheight){
        super(null, (int)x, (int)y, 0,0, (int)HBwidth, (int)HBheight, 0,0);
        ZoomBox.zoomBoxeList.add(this);
    }

    @Override
    public boolean intersect(SolidSprite other){
        this.heroInside = super.intersect(other);
        return this.heroInside;
    }

    public boolean isHeroInside(){
        return heroInside;
    }

}
