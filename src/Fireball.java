import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;

public class Fireball extends DynamicSprite{
    public static ArrayList<Fireball> fireballList;
    Figure originFigure;

    double timeCount = 0;
    double animationFramerate = 60;
    BufferedImage animationTileSheet;
    BufferedImage explosionTileSheet;
    private int animationIndex;
    private double explosionTime = 1;
    private double explosionFramerate = 71/explosionTime;

    public ArrayList<Figure> damagedFigures;

    private String state;

    private int damage = 20;



    public Fireball(Figure originFigure, int HBwidth, int HBheight, double speed) {
        super("./img/fireball.png", (int)(originFigure.x+originFigure.width/2-50), (int)(originFigure.y+originFigure.height/2-50), 100,100, HBwidth, HBheight,(100-HBwidth)/2, (100-HBheight)/2);


        Fireball.fireballList.add(this);
        this.damagedFigures = new ArrayList<>();
        this.originFigure = originFigure;

        this.speed = speed;

        this.direction = originFigure.getDirection();
        this.state = "moving";


        this.animationTileSheet = this.image;
        this.image = this.animationTileSheet.getSubimage(0,0, 100,100);

        try {

            this.explosionTileSheet = ImageIO.read(new File("./img/fireballExplosion.png"));

        }

        catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean isMovingPossible(int framerate, ArrayList<Sprite> spriteList, ArrayList<Figure> figureList){

        timeCount+=1/(double)framerate;
        this.animationUpdate();

        boolean flag = super.isMovingPossible(framerate, spriteList, figureList);
        if (!flag){
            if (this.intersect(this.originFigure)&&timeCount<1&&this.state.equals("moving")){
                return true;
            }
            if (!this.state.equals("exploding")){
                this.state = "exploding";
                this.setSpeed(0);
                this.timeCount = 0;
            }


        }

        if(!this.state.equals("exploded")&&this.timeCount>=this.explosionTime){
            this.state = "exploded";

        }
        System.out.println(this.timeCount);


        return flag;
    }

    public void animationUpdate(){
        int currentAnimationIndex = 0;
        switch (this.state){
            case "moving":
                currentAnimationIndex = (int)(this.timeCount*this.animationFramerate)%60;
                break;
            case "exploding":
                currentAnimationIndex = Math.min(70,(int)(this.timeCount*this.explosionFramerate));
        }


        if (currentAnimationIndex!=this.animationIndex){
            this.animationIndex = currentAnimationIndex;
            int line = 0, column = 0;
            BufferedImage tileSheet = this.animationTileSheet;
            switch (this.state){
                case "moving":
                    line = (this.animationIndex/8)%8;
                    column = this.animationIndex%8;
                    break;
                case "exploding":
                    line = (this.animationIndex/10)%10;
                    column = this.animationIndex%10;
                    tileSheet = this.explosionTileSheet;
                    break;
            }
            this.image = tileSheet.getSubimage(100*column,100*line, 100,100);
        }


    }

    public String getState(){
        return this.state;
    }

    public void addToDamagedFigures(Figure figure){
        this.damagedFigures.add(figure);
    }

    public int getDamage(){
        return this.damage;
    }

}
