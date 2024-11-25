import java.util.ArrayList;

public class DynamicSprite extends SolidSprite{
    protected double speed;
    protected Direction direction = Direction.SOUTH;
    private double maxSpeed;

    public DynamicSprite(String filePath, int x, int y, double width, double height, int HBwidth, int HBheight, int HBx, int HBy){
        super(filePath, x, y,width, height, HBwidth, HBheight, HBx, HBy);

        this.maxSpeed = 600;
        this.speed = 0;

    }
    // Getters //

    public Direction getDirection(){
        return this.direction;
    }

    public double getMaxSpeed(){
        return this.maxSpeed;
    }

    // Setters //

    public void setMaxSpeed(double maxSpeed){
        this.maxSpeed = maxSpeed;
    }
    public void setDirection(Direction direction){
        this.direction = direction;

    }
    public void setSpeed(double speed){
        this.speed = speed;
    }



    // update in Game Loop //

    public void update(int framerate, ArrayList<Sprite> spriteList, ArrayList<Figure> figureList){
        this.moveIfPossible(framerate,spriteList,figureList);
    }

    // Movement Management //

    public void moveIfPossible(int framerate, ArrayList<Sprite> spriteList, ArrayList<Figure> figureList) {
        boolean flag = this.isMovingPossible(framerate,spriteList,figureList);
        if (flag) {
            this.move(framerate);
        }
    }

    public boolean isMovingPossible(int framerate, ArrayList<Sprite> spriteList, ArrayList<Figure> figureList){

        int xSave = this.x;
        int ySave = this.y;

        this.move(framerate);
        for (Sprite sprite : spriteList){
            if (sprite instanceof SolidSprite){
                if (this.intersect((SolidSprite)sprite)){
                    this.x = xSave;
                    this.y = ySave;
                    return false;
                }
            }
        }

        for (Figure figure : figureList){
            if (figure!=this){
                if (this.intersect(figure)){
                    this.x = xSave;
                    this.y = ySave;
                    return false;
                }
            }
        }

        if (!Bomb.bombList.isEmpty()) {
            for (Bomb bomb : Bomb.bombList) {
                if (this.intersect(bomb) && bomb.getState().equals("idle")) {
                    this.x = xSave;
                    this.y = ySave;
                    return false;
                }
            }
        }

        this.x = xSave;
        this.y = ySave;

        this.setHitbox(this.x+this.HBx, this.y+this.HBy, this.HBwidth, this.HBheight);

        return true;


    }

    public void move(int framerate){
        switch (this.direction){
            case NORTH:
                this.y -= (int)this.speed/framerate;
                break;
            case SOUTH:
                this.y += (int)this.speed/framerate;
                break;
            case EAST:
                this.x += (int)this.speed/framerate;
                break;
            case WEST:
                this.x -= (int)this.speed/framerate;
                break;
            case NORTHEAST:
                this.y -= (int)this.speed/framerate;
                this.x += (int)this.speed/framerate;
                break;
            case SOUTHEAST:
                this.y += (int)this.speed/framerate;
                this.x += (int)this.speed/framerate;
                break;
            case SOUTHWEST:
                this.y += (int)this.speed/framerate;
                this.x -= (int)this.speed/framerate;
                break;
            case NORTHWEST:
                this.y -= (int)this.speed/framerate;
                this.x -= (int)this.speed/framerate;
        }
        this.setHitbox(this.x+this.HBx, this.y+this.HBy, this.HBwidth, this.HBheight);
    }

}
