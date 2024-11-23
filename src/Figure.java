import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Figure extends DynamicSprite{
    private double timeBetweenFrames;
    protected double time;
    private int spriteSheetNumberOfColumn;
    private BufferedImage animationTileSheet;
    private double healthBarWidth = 70;
    private double healthBarHeight = 10;
    private double healthBarX = 0;
    private double healthBarY = -20;

    private double lifepoint = 100;
    private double maxLifepoints = 100;

    private double deathDelay = 2;
    protected boolean dead = false;

    private double lastAnimationFrameTime = 0;

    protected double waitStartTime = 0;
    private double lastBombDropTime = 0;
    private double lastFireballLaunchTime = 0;
    private double lastSlashTime = 0;

    private boolean slashing = false;
    private double slashingDuration = 0.2;

    private double swordRangeWidth = this.width;
    private double swordRangeDeepness = 20;
    private Rectangle2D swordRange = new Rectangle2D.Double(0, 0, swordRangeWidth, swordRangeDeepness);

    private ArrayList<Figure> slashedFigures = new ArrayList<>();

    public Figure(String filePath, int x, int y, double width, double height, int HBwidth, int HBheight, int HBx, int HBy) {
        super(filePath, x, y, width, height, HBwidth, HBheight, HBx, HBy);
        this.animationTileSheet = this.image;
        this.image = this.animationTileSheet.getSubimage(0,0, 96,93);
        this.timeBetweenFrames = 0.1;
    }

    public void updateAnimation(int framerate){
        if ((this.time - this.lastAnimationFrameTime) > this.timeBetweenFrames){

            this.lastAnimationFrameTime = this.time;

            this.spriteSheetNumberOfColumn = (this.spriteSheetNumberOfColumn + 1) % 10;
            this.image = this.animationTileSheet.getSubimage(96*this.spriteSheetNumberOfColumn, this.direction.getValue()*93, 96,93);
        }

        if (this.speed == 0){
            this.image = this.animationTileSheet.getSubimage(0, this.direction.getValue()*93, 96,93);
        }
    }

    @Override
    public void update(int framerate, ArrayList<Sprite> spriteList, ArrayList<Figure> figureList){
        this.time += 1/(double)framerate;
        if (!this.dead){

            if (!slashing){
                this.moveIfPossible(framerate, spriteList, figureList);
                updateAnimation(framerate);
            }

        }

        if (slashing && (this.time - this.lastSlashTime) >this.slashingDuration){
            this.slashing = false;

        }


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
        if (!this.dead){
            this.lifepoint = Math.max(0, this.lifepoint-lifepoints);
            if (this.lifepoint==0){
                this.dead = true;
                this.waitStartTime = this.time;
            }

        }


    }


    @Override
    public void draw(Graphics g, double cameraX, double cameraY, double zoom){
        if (!this.dead | this.dead){
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

    public boolean isDeathFinished(){
        return (this.dead && (this.time-this.waitStartTime)>this.deathDelay);
    }

    public boolean isBombDropPossible(){
        return ((this.time-this.lastBombDropTime)>5);
    }
    public void resetLastBombDropTime(){
        this.lastBombDropTime = this.time;
    }

    public boolean isFireballLaunchPossible(){
        return ((this.time - this.lastFireballLaunchTime)>0.5);
    }
    public void resetLastFireballLaunchTime(){
        this.lastFireballLaunchTime = this.time;
    }
    public boolean isSlashingPossible(){
        return ((this.time - this.lastSlashTime)>this.slashingDuration+0.2);
    }

    public void slash(){
        this.slashing = true;
        this.slashedFigures = new ArrayList<>();
        this.image = this.animationTileSheet.getSubimage(96*10, this.direction.getValue()*93, 96,93);
        this.lastSlashTime = this.time;
        System.out.println("begin of slashing");
    }

    @Override
    public void setDirection(Direction direction){
        super.setDirection(direction);

        switch(direction){
            case EAST:
                this.swordRange.setRect(this.x+ this.width, this.y+(this.height-this.swordRangeWidth)/2, this.swordRangeDeepness, this.swordRangeWidth);
                System.out.println(this.x + " " +this.swordRange.getX());
                break;
            case NORTH:case NORTHEAST: case NORTHWEST:
                this.swordRange.setRect(this.x +(this.width- this.swordRangeWidth)/2, this.y- this.swordRangeDeepness, this.swordRangeWidth, this.swordRangeDeepness);
                break;
            case SOUTH: case SOUTHEAST: case SOUTHWEST:
                this.swordRange.setRect(this.x +(this.width- this.swordRangeWidth)/2, this.y+ this.height, this.swordRangeWidth, this.swordRangeDeepness);
                break;
            case WEST:
                this.swordRange.setRect(this.x-this.swordRangeDeepness, this.y+(this.height-this.swordRangeWidth)/2, this.swordRangeDeepness, this.swordRangeWidth);
                break;

        }
    }

    @Override
    public void drawHitbox(Graphics g, double cameraX, double cameraY, double zoom){
        super.drawHitbox(g, cameraX, cameraY, zoom);
        g.setColor(Color.RED);
        g.drawRect((int)((cameraX+this.swordRange.getX())*zoom), (int)((cameraY+this.swordRange.getY())*zoom), (int)(this.swordRange.getWidth()*zoom), (int)(this.swordRange.getHeight()*zoom));
    }

    public boolean isInRange(SolidSprite other){
        boolean res = this.swordRange.intersects(other.getHitbox());
        if (res && !this.slashedFigures.contains(other)){
            this.slashedFigures.add((Figure)other);
            return true;
        }
        return false;
    }
    public boolean isSlashing(){
        return this.slashing;
    }

}
