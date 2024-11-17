import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AutoFigure extends Figure {
    private ArrayList<Point> trajectory;
    private double waitingTime;
    private Point target;
    private double timeCount;
    private String state;
    private int targetIndex;
    private int directionExplored;

    private double visionWidth;
    private double visionDeepness;


    private Rectangle2D visionField = new Rectangle2D.Double(0, 0, visionWidth, visionDeepness);

    private ArrayList<Direction> directionsToLookAt;

    private boolean touchingHero;
    private Figure prey;

    public AutoFigure(String filePath, int x, int y, double width, double height, int HBwidth, int HBheight, int HBx, int HBy) {
        super(filePath, x, y, width, height, HBwidth, HBheight, HBx, HBy);
        this.trajectory = new ArrayList<Point>();
        this.trajectory.add(new Point(700,300));
        this.trajectory.add(new Point(800,600));

        this.directionsToLookAt = new ArrayList<>(Arrays.asList(Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH));

        this.targetIndex = 0;
        this.target = this.trajectory.get(this.targetIndex);
        this.setMaxSpeed(200);
        this.setSpeed(this.getMaxSpeed());
        this.state = "moving";

        this.waitingTime = 3;

        this.visionWidth = 200;
        this.visionDeepness = 600;
    }

    public void computeShortestDirection(int framerate, ArrayList<Sprite> spriteList, ArrayList<Figure> figureList){
        double dx = this.target.x - this.width/2 - this.x;
        double dy = this.target.y  -this.height/2 - this.y;

        double length = Math.sqrt(dx*dx + dy*dy);

        double directionX = dx/length;
        double directionY = dy/length;

        double angle = -Math.toDegrees(Math.atan2(directionY, directionX));
        System.out.println(angle);

        if (angle<-170 | angle>170){
            this.setDirection(Direction.WEST);
        }
        else{
            switch((int)(angle/45)){
                case 0:
                    this.setDirection(Direction.EAST);
                    break;
                case 1:
                    this.setDirection(Direction.NORTHEAST);
                    break;
                case 2:
                    this.setDirection(Direction.NORTH);
                    break;
                case 3:
                    this.setDirection(Direction.NORTHWEST);
                    break;
                case -1:
                    this.setDirection(Direction.SOUTHEAST);
                    break;
                case -2:
                    this.setDirection(Direction.SOUTH);
                    break;
                case -3:
                    this.setDirection(Direction.SOUTHWEST);
            }
        }

        //System.out.println(this.getDirection());
        int i = 1;
        boolean flag = true;
        while(!isMovingPossible(framerate, spriteList, figureList)&&flag){
            this.setDirection(this.getDirection().next());
            i++;
            if (i>7){
                flag = false;
            }
            //System.out.println(this.getDirection());

        }
        //System.out.println("Move possible\n\n");


    }

    @Override
    public void update(int framerate, ArrayList<Sprite> spriteList, ArrayList<Figure> figureList){
        this.timeCount+=1/(double)framerate;
        int xSave = this.x;
        int ySave = this.y;
        this.moveIfPossible(framerate, spriteList, figureList);
        //System.out.println("dx :"+(this.x-xSave) + " dy : "+(this.y-ySave));
        if (this.state.equals("moving") | this.state.equals("chasing")){

            if (this.state.equals("chasing")){

                double distance = this.target.distance(new Point((int)(this.x+this.width/2), (int)(this.y+this.height/2)));

                if (distance>600){
                    this.setState("moving");
                }
                else {
                    Point targetPosition = new Point((int)(this.prey.getXPosition()+this.prey.getWidth()/2), (int)(this.prey.getYPosition()+ this.prey.getHeight()/2));
                    this.target = targetPosition ;
                }

                //System.out.println(distance);

            }

            this.computeShortestDirection(framerate, spriteList, figureList);


            if (Math.abs(this.x+this.width/2-target.x)<10 && Math.abs(this.y+this.height/2 -target.y)<10) {
                this.setSpeed(0);
                this.state = "waiting";
                this.timeCount = 0;
                this.targetIndex = (this.targetIndex+1)%this.trajectory.size();
                this.target = this.trajectory.get(this.targetIndex);
                this.directionExplored = 0;
                Collections.shuffle(this.directionsToLookAt);

            }


        }

        if (this.state.equals("waiting")){

            if (this.directionExplored>3 && this.timeCount>this.waitingTime/(this.directionsToLookAt.size())){
                this.state = "moving";
                this.setSpeed(this.getMaxSpeed());
            }

            else if (this.timeCount> this.waitingTime/(this.directionsToLookAt.size())){
                this.setDirection(this.directionsToLookAt.get(this.directionExplored));
                this.timeCount = 0;
                this.directionExplored ++;
            }



        }



//            target = trajectory.get(1);}
        //System.out.println(this.x + " " + this.y);
    }

    @Override
    public void setDirection(Direction direction){
        super.setDirection(direction);

        switch(direction){
            case EAST:
                this.visionField.setRect(this.x+ this.width, this.y+(this.height-this.visionWidth)/2, this.visionDeepness, this.visionWidth);
                break;
            case NORTH:case NORTHEAST: case NORTHWEST:
                this.visionField.setRect(this.x +(this.width- this.visionWidth)/2, this.y- this.visionDeepness, this.visionWidth, this.visionDeepness);
                break;
            case SOUTH: case SOUTHEAST: case SOUTHWEST:
                this.visionField.setRect(this.x +(this.width- this.visionWidth)/2, this.y+ this.height, this.visionWidth, this.visionDeepness);
                break;
            case WEST:
                this.visionField.setRect(this.x-this.visionDeepness, this.y+(this.height-this.visionWidth)/2, this.visionDeepness, this.visionWidth);
                break;

        }

    }
    @Override
    public boolean isMovingPossible(int framerate, ArrayList<Sprite> spriteList, ArrayList<Figure> figureList){

        int xSave = this.x;
        int ySave = this.y;

        this.touchingHero = false;

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
            if (figure instanceof SolidSprite && figure!=this){
                if (this.intersect((SolidSprite)figure)){
                    this.x = xSave;
                    this.y = ySave;
                    if (figure instanceof Figure){
                        this.touchingHero = true;
                       // System.out.println("test");
                    }
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

        this.updateHitbox(this.x+this.HBx, this.y+this.HBy, this.HBwidth, this.HBheight);

        return true;


    }

    @Override
    public void drawHitbox(Graphics g, double cameraX, double cameraY, double zoom) {
        super.drawHitbox(g, cameraX, cameraY, zoom);
        g.setColor(Color.BLACK);
        g.drawRect((int)((cameraX+this.visionField.getX())*zoom), (int)((cameraY+this.visionField.getY())*zoom), (int)(this.visionField.getWidth()*zoom), (int)(this.visionField.getHeight()*zoom));

        g.setColor(Color.RED);
        g.fillRoundRect((int)((cameraX+this.target.x-15)*zoom), (int)((cameraY+this.target.y -15)*zoom), (int)(30*zoom), (int)(30*zoom), (int)(40*zoom),(int)(40*zoom));

        g.setColor(Color.BLACK);
        g.drawLine((int)((cameraX+this.x + this.width/2)*zoom), (int)((cameraY+this.y +this.height/2)*zoom), (int)((cameraX+this.target.x)*zoom), (int)((cameraY+this.target.y)*zoom));
    }
    public boolean isViewing(SolidSprite other){
        return this.visionField.intersects(other.getHitbox());
    }
    public String getState(){
        return this.state;
    }
    public void setState(String state){
        this.state = state;
    }

    public void setNewTarget(Point p){
        this.target = p;
    }

    public void setNewPrey(Figure prey){
        this.prey = prey;
    }







}
