import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Sprite implements Displayable{
    protected double height;
    protected double width;
    protected BufferedImage image;
    protected int x;
    protected int y;

    public Sprite(String filePath, int x, int y, double width, double height){
       
        try {
            if (filePath== null){
                this.image = null;
            }
            else {
                this.image = ImageIO.read(new File(filePath));
            }
            this.height = height;
            this.width = width;
        }

        catch (Exception ignored) {}

        this.x = x;
        this.y = y;


    }

    //  Getters //
    public int getXPosition(){
        return this.x;
    }
    public int getYPosition(){
        return this.y;
    }
    public double getHeight(){
        return this.height;
    }
    public double getWidth(){
        return this.width;
    }

    //  Setters //
    public void rescale(double scale){
        this.width = width * scale;
        this.height = height * scale;
    }

    //  Drawing Function //
    @Override
    public void draw(Graphics g, double cameraX, double cameraY, double zoom) {
        if (this.image != null) {
            g.drawImage(this.image, (int)((cameraX + this.x)*zoom), (int)((cameraY+this.y)*zoom), (int) (width*zoom), (int) (height*zoom), null);
        }
        
    }

}
