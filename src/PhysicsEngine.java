import java.awt.*;
import java.util.ArrayList;

public class PhysicsEngine implements Engine{

    GameEngine gameEngine;
    RenderEngine renderEngine;
    
    private ArrayList<Sprite> environment;
    private ArrayList<Figure> figureList;
    private ArrayList<DynamicSprite> movingSpriteList;
    private ArrayList<ZoomBox> zoomBoxList;


    public PhysicsEngine(){
        figureList = new ArrayList<Figure>();
        movingSpriteList = new ArrayList<DynamicSprite>();
        zoomBoxList = new ArrayList<>();

    }

    public void setReferenceToOtherEngine(GameEngine gameEngine, RenderEngine renderEngine){
        this.gameEngine = gameEngine;
        this.renderEngine = renderEngine;

        ZoomBox a = new ZoomBox(300,600,1000,1000);
        renderEngine.addToRenderList(a);
        zoomBoxList.add(a);
    }

    public void addToEnvironmentList(Sprite sprite){
        environment.add(sprite);
    }

    public void addToFigureList(Figure figure){
        figureList.add(figure);
    }

    public void addToMovingSpriteList(DynamicSprite sprite){

        movingSpriteList.add(sprite);
    }

    public void setEnvironment(ArrayList<Sprite> environment) {
        this.environment = environment;
    }



    @Override
    public void update(int framerate) {

        if (!figureList.isEmpty()){
            for(Figure figure : figureList){
                if (figure instanceof AutoFigure){
                    AutoFigure fig = (AutoFigure)figure;
                    if (fig.isViewing(gameEngine.hero) && fig.getState()!="chasing"){
                        fig.setState("moving");
                        fig.setSpeed(fig.getMaxSpeed());
                        fig.setNewTarget(new Point(gameEngine.hero.getXPosition(), gameEngine.hero.getYPosition()));
                    }
                }
                figure.update(framerate, environment, figureList);

            }
        }

        if (!Bomb.bombList.isEmpty()){
            ArrayList<Bomb> toRemove = new ArrayList<Bomb>();

            for (Bomb bomb : Bomb.bombList){
                bomb.update(framerate);
                String state = bomb.getState();

                switch (state){
                    case "exploding":

                        if (!figureList.isEmpty()) {
                            for (Figure figure : figureList) {
                                if (bomb.intersect(figure) && !bomb.damagedFigures.contains(figure)) {
                                    figure.setDamage(50);
                                    bomb.addToDamagedFigures(figure);
                                }
                            }
                        }
                        break;
                    case "exploded":
                        toRemove.add(bomb);
                        break;
                }

            }
            if (!toRemove.isEmpty()){
                for (Bomb bomb: toRemove){
                    Bomb.bombList.remove(bomb);
                    renderEngine.renderList.remove(bomb);
                }
            }
        }
        if (!Fireball.fireballList.isEmpty()){
            ArrayList<Fireball> toRemove = new ArrayList<Fireball>();

            for (Fireball ball : Fireball.fireballList){
                ball.moveIfPossible(framerate, environment, figureList);
                String state = ball.getState();

                switch (state){
                    case "exploding":

                        if (!figureList.isEmpty()) {
                            for (Figure figure : figureList) {
                                if (ball.intersect(figure)&&!ball.damagedFigures.contains(figure)) {
                                    figure.setDamage(ball.getDamage());
                                    ball.addToDamagedFigures(figure);
                                }
                            }
                        }
                        break;
                    case "exploded":
                        toRemove.add(ball);
                        break;
                }

            }
            if (!toRemove.isEmpty()){
                for (Fireball ball: toRemove){
                    Fireball.fireballList.remove(ball);
                    renderEngine.renderList.remove(ball);
                }
            }
        }

        if (!ZoomBox.zoomBoxeList.isEmpty()){
            for (ZoomBox box : ZoomBox.zoomBoxeList){

                box.intersect(gameEngine.hero);

            }
        }
        
    }
    
}
