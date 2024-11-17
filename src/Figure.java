import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Figure extends DynamicSprite{
    private double timeBetweenFrames;
    private double time;
    private int spriteSheetNumberOfColumn;
    private BufferedImage animationTileSheet;
    private double healthBarWidth = 70;
    private double healthBarHeight = 10;
    private double healthBarX = 0;
    private double healthBarY = -20;

    private double lifepoint = 100;
    private double maxLifepoints = 100;


    public Figure(String filePath, int x, int y, double width, double height, int HBwidth, int HBheight, int HBx, int HBy) {
        super(filePath, x, y, width, height, HBwidth, HBheight, HBx, HBy);
        this.animationTileSheet = this.image;
        this.image = this.animationTileSheet.getSubimage(0,0, 96,93);
        this.timeBetweenFrames = 0.1;
    }

    public void updateAnimation(int framerate){
        if (this.time > this.timeBetweenFrames){
            this.time = 0;
            this.spriteSheetNumberOfColumn = (this.spriteSheetNumberOfColumn + 1) % 10;
            this.image = this.animationTileSheet.getSubimage(96*this.spriteSheetNumberOfColumn, this.direction.getValue()*93, 96,93);
        }

        if (this.speed != 0){
            this.time += 1.0/framerate;
        }
        else {
            this.image = this.animationTileSheet.getSubimage(0, this.direction.getValue()*93, 96,93);
        }
    }
    @Override
    public void moveIfPossible(int framerate, ArrayList<Sprite> spriteList, ArrayList<Figure> figureList) {

        boolean flag = this.isMovingPossible(framerate,spriteList, figureList);
        if (flag) {
            this.move(framerate);
        }
        updateAnimation(framerate);
//        if (this instanceof AutoFigure){
//            System.out.println(flag);
//        }



    }

    @Override
    public void rescale(double scale){
        super.rescale(scale);
        this.healthBarWidth *= scale;
        this.healthBarHeight *= scale;
        this.healthBarX *= scale;
        this.healthBarY *= scale;
    }

    public void setDamage(int lifepoints){
        this.lifepoint = Math.max(0, this.lifepoint-lifepoints);

    }
    @Override
    public void draw(Graphics g, double cameraX, double cameraY, double zoom){
        super.draw(g, cameraX, cameraY, zoom);

        double percentage = this.lifepoint/this.maxLifepoints;
        if (percentage > 0.5){
            g.setColor(Color.GREEN);

        }
        else if (percentage > 0.3){
            g.setColor(Color.YELLOW);
        }
        else {
            g.setColor(Color.RED);
        }


        g.fillRoundRect((int)((cameraX + x+this.healthBarX)*zoom), (int)((cameraY +y+ this.healthBarY)*zoom),(int)(this.healthBarWidth*this.lifepoint/100),(int)this.healthBarHeight,10,10);
        g.setColor(Color.BLACK);
        g.drawRoundRect((int)((cameraX + x+this.healthBarX)*zoom), (int)((cameraY +y+ this.healthBarY)*zoom), (int)this.healthBarWidth,(int)this.healthBarHeight, 10, 10);
    }


}
