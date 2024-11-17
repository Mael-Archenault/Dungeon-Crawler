import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class App {
    
    JFrame displayZoneFrame;


    public static void main(String[] args) throws Exception {

        
        int framerate = 60;

        int windowWidth = 500;
        int windowHeight = windowWidth/6*9;

        Bomb.bombList = new ArrayList<Bomb>();
        Fireball.fireballList = new ArrayList<Fireball>();
        ZoomBox.zoomBoxeList = new ArrayList<ZoomBox>();

        JFrame displayZoneFrame = new JFrame("TD4");
        displayZoneFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayZoneFrame.setSize(windowWidth, windowHeight);
        displayZoneFrame.setLocationRelativeTo(null);


        Figure hero =new Figure("./img/heroTileSheet.png", 200 ,200, 100,100,70,100,15, 0);
        hero.rescale(0.75);
        Figure villain = new AutoFigure("./img/heroTileSheet.png", 300 ,200, 100,100,70,100,15, 0);
        villain.rescale(0.75);
        RenderEngine renderEngine = new RenderEngine(windowWidth, windowHeight);
        PhysicsEngine physicsEngine = new PhysicsEngine();
        GameEngine gameEngine = new GameEngine(hero);


        renderEngine.setReferenceToOtherEngine(physicsEngine, gameEngine);
        physicsEngine.setReferenceToOtherEngine(gameEngine, renderEngine);
        gameEngine.setReferenceToOtherEngine(physicsEngine, renderEngine);

        renderEngine.addKeyListener(gameEngine);

        Playground level1 = new Playground("./data/level1.txt");
        level1.setTileWidth(200);
        physicsEngine.setEnvironment(level1.getSpriteList());
        for(Displayable displayable : level1.getSpriteList()){
            renderEngine.addToRenderList(displayable);
        }
        
        renderEngine.addToRenderList(hero);
        renderEngine.addToRenderList(villain);
        physicsEngine.addToFigureList(hero);
        physicsEngine.addToFigureList(villain);



        TimerTask task = new TimerTask(){

            @Override
            public void run() {
                gameEngine.update(framerate);
                physicsEngine.update(framerate);
                renderEngine.update(framerate);
                
            }
            
        };

        Timer gameLoop = new Timer();
        gameLoop.scheduleAtFixedRate(task, 0, 1000/framerate);

        
        displayZoneFrame.add(renderEngine);
        displayZoneFrame.pack();
        displayZoneFrame.setVisible(true);

        

    }
}
