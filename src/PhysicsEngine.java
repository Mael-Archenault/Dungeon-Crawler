import java.util.ArrayList;

public class PhysicsEngine implements Engine{

    GameEngine gameEngine;
    RenderEngine renderEngine;
    
    private ArrayList<Sprite> environment;
    private final ArrayList<Figure> figureList;


    public PhysicsEngine(){
        figureList = new ArrayList<>();

    }

    // Getters //

    public ArrayList<Figure> getFigureList(){
        return this.figureList;
    }

    // Setters //

    public void setReferenceToOtherEngine(GameEngine gameEngine, RenderEngine renderEngine){
        this.gameEngine = gameEngine;
        this.renderEngine = renderEngine;
    }

    public void setEnvironment(ArrayList<Sprite> environment) {
        this.environment = environment;
    }

    public void addToFigureList(Figure figure){
        figureList.add(figure);
    }

    // Update for Game Loop //

    @Override
    public void update(int framerate) {
        // Updating each figure
        if (!figureList.isEmpty()){
            for(Figure figure : figureList){
                if (figure instanceof AutoFigure){
                    AutoFigure fig = (AutoFigure)figure;
                    if (fig.isViewing(gameEngine.hero) && !fig.getState().equals("chasing") && !gameEngine.hero.isDead()){
                        fig.setState("chasing");
                        fig.setSpeed(fig.getMaxSpeed());
                        fig.setNewPrey(gameEngine.hero);
                    }
                }
                figure.update(framerate, environment, figureList);

            }
        }
        // Updating each Bomb
        if (!Bomb.bombList.isEmpty()){
            ArrayList<Bomb> toRemove = new ArrayList<>();

            for (Bomb bomb : Bomb.bombList){
                bomb.update(framerate);
                String state = bomb.getState();

                switch (state){
                    case "exploding":

                        if (!figureList.isEmpty()) {
                            for (Figure figure : figureList) {
                                if (bomb.intersect(figure) && !bomb.damagedFigures.contains(figure)) {
                                    figure.setDamage(100);
                                    bomb.addToDamagedFigures(figure);
                                }
                            }
                            for (Sprite sprite : environment) {
                                if (sprite instanceof SolidSprite){
                                    SolidSprite solid = (SolidSprite)sprite;
                                    if (bomb.intersect(solid)) {
                                        solid.destroy();
                                    }
                                }

                            }
                        }
                        break;
                    case "exploded":
                        toRemove.add(bomb);
                        break;
                }

            }
            // Removing inactive Bombs
            if (!toRemove.isEmpty()){
                for (Bomb bomb: toRemove){
                    Bomb.bombList.remove(bomb);
                    renderEngine.renderList.remove(bomb);
                }
            }
        }
        // Updating each Fireball
        if (!Fireball.fireballList.isEmpty()){
            ArrayList<Fireball> toRemove = new ArrayList<>();

            for (Fireball ball : Fireball.fireballList){
                ball.moveIfPossible(framerate, environment, figureList);
                String state = ball.getState();

                switch (state){
                    case "exploding":

                        if (!figureList.isEmpty()) {
                            for (Figure figure : figureList) {
                                if (ball.intersect(figure)&&!ball.damagedFigures.contains(figure)) {
                                    figure.setDamage(ball.getInflictedDamage());
                                    ball.addToDamagedFigures(figure);
                                    if (figure instanceof AutoFigure){
                                        AutoFigure auto = (AutoFigure)figure;
                                        auto.setState("chasing");
                                        auto.setNewPrey(gameEngine.hero);
                                    }
                                }
                            }
                        }
                        break;
                    case "exploded":
                        toRemove.add(ball);
                        break;
                }

            }
            // Removing inactive Fireballs
            if (!toRemove.isEmpty()){
                for (Fireball ball: toRemove){
                    Fireball.fireballList.remove(ball);
                    renderEngine.renderList.remove(ball);
                }
            }
        }

        // Updating each ZoombBox
        if (!ZoomBox.zoomBoxeList.isEmpty()){
            for (ZoomBox box : ZoomBox.zoomBoxeList){
                box.intersect(gameEngine.hero);
            }
        }
        
    }

}
