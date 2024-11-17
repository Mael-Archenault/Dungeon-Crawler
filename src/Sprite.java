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
    protected int angle = 0;



    public Sprite(String filePath, int x, int y, double width, double height){
       
        try {
            
            this.image = ImageIO.read(new File(filePath));
            this.height = height;
            this.width = width;

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.x = x;
        this.y = y;


    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getXPosition(){
        return this.x;
    }
    public int getYPosition(){
        return this.y;
    }

    public void rescale(double scale){
        this.width = width * scale;
        this.height = height * scale;
    }

    @Override
    public void draw(Graphics g, double cameraX, double cameraY, double zoom) {
        if (this.image != null) {
            g.drawImage(this.image, (int)((cameraX + this.x)*zoom), (int)((cameraY+this.y)*zoom), (int) (width*zoom), (int) (height*zoom), null);
        }
        
    }
    
    
}
