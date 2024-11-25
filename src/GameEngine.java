import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameEngine implements Engine, KeyListener{

    PhysicsEngine physicsEngine;
    RenderEngine renderEngine;

    public Figure hero;
    boolean zPressed;
    boolean sPressed;
    boolean qPressed;
    boolean dPressed;

    public boolean earlyExit = false;


    public GameEngine(Figure hero){
        this.hero = hero;
    }
    // Setters //

    public void setReferenceToOtherEngine(PhysicsEngine physicsEngine, RenderEngine renderEngine){
        this.physicsEngine = physicsEngine;
        this.renderEngine = renderEngine;
    }

    // Key Readings
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // getting all pressed keys (to allow combination of keys)
        if (e.getKeyCode() == KeyEvent.VK_Z){
            zPressed = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_S){
            sPressed = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_Q){
            qPressed = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_D){
            dPressed = true;
        }
        // Placing a bomb (in the direction of the player)
        if (e.getKeyCode() == KeyEvent.VK_K){
            int x=0, y=0;
            switch(this.hero.getDirection().getValue()){
                case 0:
                    x = (int)(this.hero.x + this.hero.width/2-25);
                    y = (int)(this.hero.y + this.hero.height);
                    break;
                case 1:
                    x = this.hero.x - 50;
                    y = (int)(this.hero.y +this.hero.height/2- 25);
                    break;
                case 2:
                    x = (int)(this.hero.x + this.hero.width/2-25);
                    y = this.hero.y - 50;
                    break;
                case 3:
                    x = (int)(this.hero.x + this.hero.width);
                    y = (int)(this.hero.y +this.hero.height/2- 25);
                    break;
            }

            if (hero.isBombDropPossible()){
                Bomb bomb = new Bomb(x, y);
                renderEngine.addToRenderList(bomb);
                hero.resetLastBombDropTime();
            }

        }
        // Launching a Fireball
        if (e.getKeyCode() == KeyEvent.VK_J){
            if (hero.isFireballLaunchPossible()){
                Fireball fireball = new Fireball(this.hero, 40,40, 800);
                renderEngine.addToRenderList(fireball);
                hero.resetLastFireballLaunchTime();
            }


        }
        // Attacking with a sword
        if (e.getKeyCode()== KeyEvent.VK_SPACE){
            if (hero.isSlashingPossible()){
                hero.slash(physicsEngine.getFigureList());
            }

        }
        // Early exiting the game
        if (e.getKeyCode()== KeyEvent.VK_ESCAPE){
            this.earlyExit = true;
        }
        // Display hitboxes
        if (e.getKeyCode()== KeyEvent.VK_H){
            renderEngine.toggleHitboxes();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Z){
            zPressed = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_S){
            sPressed = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_Q){
            qPressed = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_D){
            dPressed = false;
        }

        this.hero.setSpeed(0);
    }

    // Update for Game Loop //

    @Override
    public void update(int framerate) {

        if (zPressed && dPressed) {
            this.hero.setSpeed(this.hero.getMaxSpeed()/1.1);
            this.hero.setDirection(Direction.NORTHEAST);
        }

        else if (zPressed && qPressed) {
            this.hero.setSpeed(this.hero.getMaxSpeed()/1.1);
            this.hero.setDirection(Direction.NORTHWEST);
        }

        else if (sPressed && dPressed) {
            this.hero.setSpeed(this.hero.getMaxSpeed()/1.1);
            this.hero.setDirection(Direction.SOUTHEAST);
        }

        else if (sPressed && qPressed) {
            this.hero.setSpeed(this.hero.getMaxSpeed()/1.1);
            this.hero.setDirection(Direction.SOUTHWEST);
        }

        else if(zPressed){
            this.hero.setSpeed(this.hero.getMaxSpeed());
            this.hero.setDirection(Direction.NORTH);
        }

        else if(sPressed){
            this.hero.setSpeed(this.hero.getMaxSpeed());
            this.hero.setDirection(Direction.SOUTH);
        }

        else if(qPressed){
            this.hero.setSpeed(this.hero.getMaxSpeed());
            this.hero.setDirection(Direction.WEST);
        }

        else if(dPressed){
            this.hero.setSpeed(this.hero.getMaxSpeed());
            this.hero.setDirection(Direction.EAST);
        }

    }

}
