import java.awt.image.BufferedImage;
import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Bomb extends SolidSprite{
    public static ArrayList<Bomb> bombList;
    private double timeCount;
    private final double lifetime;
    private final double animationFramerate;
    private final double explosionDuration;
    private final double explosionMaxRange = 200;
    private int animationIndex;

    private final ArrayList<Integer> explosionHitboxSizes;

    public ArrayList<Figure> damagedFigures;


    private BufferedImage animationTileSheet;

    private String state;
    

    public Bomb(int x, int y){
        super("./img/bomb.png", x, y,50,50, 30,30,10,15);
        Bomb.bombList.add(this);
        this.lifetime = 3;
        this.timeCount = 0;
        this.explosionDuration = 0.5;
        this.animationFramerate = 13/this.explosionDuration;
        this.animationIndex = 0;

        this.explosionHitboxSizes = new ArrayList<>(Arrays.asList(20,30,50,50,90,100,100,100,100,90,85,80,60)); // these values are adapted for explosionMaxRange = 130
        this.explosionHitboxSizes.replaceAll(n -> (int)(n *(explosionMaxRange/130)));

        this.damagedFigures = new ArrayList<>();
        this.state = "idle";

        try {

            String explosionFilePath = "./img/explosion.png";
            this.animationTileSheet = ImageIO.read(new File(explosionFilePath));

        }

        catch (Exception ignored) {}

    }

    // Getters //

    public boolean isExploding(){
        return this.state.equals("exploding");
    }
    public String getState(){
        return this.state;
    }

    // Setters //

    public void addToDamagedFigures(Figure figure){
        this.damagedFigures.add(figure);
    }


    public void trigger(){ // A function to early trigger the bomb
        this.timeCount = 0;
        this.x = (int)(this.x + this.width/2-this.explosionMaxRange/2);
        this.y = (int)(this.y + this.height/2 - this.explosionMaxRange/2);

        this.width = this.explosionMaxRange;
        this.height = this.explosionMaxRange;

        this.image = this.animationTileSheet.getSubimage(64*this.animationIndex,0, 64,64);
        double newX = this.x + (this.explosionMaxRange- this.explosionHitboxSizes.get(this.animationIndex))/2;
        double newY = this.y + (this.explosionMaxRange- this.explosionHitboxSizes.get(this.animationIndex))/2;
        this.setHitbox(newX, newY, explosionHitboxSizes.get(this.animationIndex), explosionHitboxSizes.get(this.animationIndex));
    }

    // Updating in Game Loop //

    public void update(int framerate){
        
        this.timeCount += (double)1/framerate;

        if (this.timeCount >= this.lifetime && !this.isExploding()){
            this.trigger();
            this.state = "exploding";
        }

        else if (this.isExploding()){
            this.animationUpdate();
            if (!Bomb.bombList.isEmpty()){
                for (Bomb bomb: Bomb.bombList){
                    if (bomb!=this && !bomb.isExploding() && this.intersect(bomb)){
                        bomb.trigger();
                        bomb.state = "exploding";
                    }
                }
            }
            if (this.timeCount >= this.explosionDuration && !this.state.equals("exploded")){
                this.state = "exploded";
            }
        }
    }

    // Update animation //

    public void animationUpdate(){
        int currentAnimationIndex = Math.min(12,(int)(this.timeCount*this.animationFramerate));
        if (currentAnimationIndex!=this.animationIndex){

            this.animationIndex = currentAnimationIndex;
            this.image = this.animationTileSheet.getSubimage(64*this.animationIndex,0, 64,64);
            double newX = this.x + (this.explosionMaxRange- this.explosionHitboxSizes.get(this.animationIndex))/2;
            double newY = this.y + (this.explosionMaxRange- this.explosionHitboxSizes.get(this.animationIndex))/2;
            this.setHitbox(newX, newY, explosionHitboxSizes.get(this.animationIndex), explosionHitboxSizes.get(this.animationIndex));
        }
    }
}
