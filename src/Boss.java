import java.awt.*;

public class Boss extends AutoFigure{
    public Boss(String filePath, int x, int y, double width, double height, int HBwidth, int HBheight, int HBx, int HBy){
        super(filePath, x, y, width, height, HBwidth, HBheight, HBx, HBy);
        this.rescale(1.7);
        this.lifepoint = 500;
        this.maxLifepoints = 500;

        this.slashStrength = 30;

        this.trajectory.add(new Point(11*200, 7*200 + 100));
        this.trajectory.add(new Point(11*200 + 100, 12*200 + 100));
        this.trajectory.add(new Point(7*200, 12*200));
        this.target = this.trajectory.get(this.targetIndex);

    }
}
