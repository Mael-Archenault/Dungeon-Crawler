import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class RenderEngine extends JPanel implements Engine{

    public ArrayList<Displayable> renderList;

    PhysicsEngine physicsEngine;
    GameEngine gameEngine;

    double zoom = 1;
    private double targetZoom;

    double cameraX = 300;
    double cameraY = 100;

    double windowWidth;
    double windowHeight;

    boolean displayHitboxes = false;

    public RenderEngine(int windowWidth, int windowHeight){
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setFocusable(true);

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        renderList = new ArrayList<>();
    }
    // Setters //
    public void addToRenderList(Displayable displayable){
        renderList.add(displayable);
    }

    public void toggleHitboxes(){
        this.displayHitboxes = !this.displayHitboxes;
    }
    public void setTargetZoom(double target){
        this.targetZoom = target;
    }

    public void centerCameraOnPlayer(){
        this.cameraX = -this.gameEngine.hero.getXPosition() + this.windowWidth/(2*zoom) - this.gameEngine.hero.getWidth()/2;
        this.cameraY = -this.gameEngine.hero.getYPosition() + this.windowHeight/(2*zoom)- this.gameEngine.hero.getHeight()/2;
    }

    public void setReferenceToOtherEngine(PhysicsEngine physicsEngine, GameEngine gameEngine){
        this.physicsEngine = physicsEngine;
        this.gameEngine = gameEngine;

        this.centerCameraOnPlayer(); // a bit weird to place this function here,but this function needs the gameEngine to be initialized to work
    }

    // Update for Game Loop //

    @Override
    public void update(int framerate) {

        // Virtual camera management
        double cameraXSpeed = -Math.pow(gameEngine.hero.getXPosition()-this.windowWidth/(2*zoom)+ this.gameEngine.hero.getWidth()/2 + cameraX,3)*zoom/(500);
        double cameraYSpeed = -Math.pow(gameEngine.hero.getYPosition()-this.windowHeight/(2*zoom)+ this.gameEngine.hero.getHeight()/2 + cameraY,3)*zoom/(2000);

        this.cameraX += cameraXSpeed/framerate;
        this.cameraY += cameraYSpeed/framerate;

        // Zoom management
        if (!ZoomBox.zoomBoxeList.isEmpty()){
            for (ZoomBox box : ZoomBox.zoomBoxeList){
                if (box.isHeroInside()){
                    this.targetZoom = 0.5;
                }

                else{
                    this.targetZoom = 1;
                }
                double zoomSpeed =(this.targetZoom-this.zoom)*2;
                this.zoom += zoomSpeed/framerate;
            }
        }
        // Applying changes
        this.repaint();
    }


    // Drawing Function //

    @Override
    protected void paintComponent(Graphics g) {
        this.draw(g);
    }


    public void draw(Graphics g){
        super.paintComponent(g);
        if (!renderList.isEmpty()){
            // Displaying all objects
            for (Displayable displayable : renderList){
                displayable.draw(g, cameraX, cameraY, zoom);
            }
            // Displaying hitboxes for objects which have one
            if (displayHitboxes){
                for (Displayable displayable : renderList){
                    if (displayable instanceof SolidSprite){
                        SolidSprite solidSprite = (SolidSprite) displayable;
                        solidSprite.drawHitbox(g, cameraX, cameraY, zoom);
                    }

                }
            }
        }
    }



    
}
