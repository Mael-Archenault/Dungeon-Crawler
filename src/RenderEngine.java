import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class RenderEngine extends JPanel implements Engine{

    PhysicsEngine physicsEngine;
    GameEngine gameEngine;
    boolean displayHitboxes = false;
    private double timeCount =0;

    private double zoomSpeed;
    private double targetZoom;

    double cameraX = 0;
    double cameraY = 0;

    double windowWidth;
    double windowHeight;

    double zoom = 1;

    public ArrayList<Displayable> renderList;


    public RenderEngine(int windowWidth, int windowHeight){
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setFocusable(true);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        renderList = new ArrayList<Displayable>();
    }

    public void setReferenceToOtherEngine(PhysicsEngine physicsEngine, GameEngine gameEngine){
        this.physicsEngine = physicsEngine;
        this.gameEngine = gameEngine;
    }

    public void addToRenderList(Displayable displayable){
        renderList.add(displayable);
    }

    public void draw(Graphics g){
        super.paintComponent(g);
        if (!renderList.isEmpty()){
            for (Displayable displayable : renderList){
                displayable.draw(g, cameraX, cameraY, zoom);
            }

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

    @Override
    public void update(int framerate) {



        double cameraXSpeed = -Math.pow(gameEngine.hero.getXPosition()-this.windowWidth/(2*zoom) + cameraX,3)*zoom/(500);
        double cameraYSpeed = -Math.pow(gameEngine.hero.getYPosition()-this.windowHeight/(2*zoom) + cameraY,3)*zoom/(2000);
        this.cameraX += cameraXSpeed/framerate;
        this.cameraY += cameraYSpeed/framerate;



        if (!ZoomBox.zoomBoxeList.isEmpty()){
            for (ZoomBox box : ZoomBox.zoomBoxeList){
                if (box.isHeroInside()){
                    this.targetZoom = 0.5;
                }

                else{
                    this.targetZoom = 1;
                }

                this.zoomSpeed =(this.targetZoom-this.zoom)*2;
                this.zoom += this.zoomSpeed/framerate;

            }
        }
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        this.draw(g);
    }

    public void toggleHitboxes(){this.displayHitboxes = !this.displayHitboxes;}
    
}
