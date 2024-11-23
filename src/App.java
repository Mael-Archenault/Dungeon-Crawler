import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import java.util.TimerTask;
import javax.swing.JPanel;

public class App{
    

    int framerate= 30;
    int windowWidth = 500;
    int windowHeight = windowWidth/6*9;

    JFrame frame;


    public App(){
        this.frame = new JFrame();
        this.frame.setTitle("TD4");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(this.windowWidth, this.windowHeight);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.startMenu();

        







        

    }

    public void startGame() {
        Bomb.bombList = new ArrayList<Bomb>();
        Fireball.fireballList = new ArrayList<Fireball>();
        ZoomBox.zoomBoxeList = new ArrayList<ZoomBox>();

        Figure hero =new Figure("./img/heroTileSheetWithSword.png", 500 ,300, 100,100,70,100,15, 0);
        hero.rescale(0.75);
        Figure villain = new AutoFigure("./img/heroTileSheetWithSword.png", 800 ,800, 100,100,70,100,15, 0);
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
        ArrayList<Sprite> levelSpriteList = level1.getSpriteList();

        physicsEngine.setEnvironment(levelSpriteList);

        for(Displayable displayable : levelSpriteList){
            renderEngine.addToRenderList(displayable);
        }

        renderEngine.addToRenderList(hero);
        renderEngine.addToRenderList(villain);
        physicsEngine.addToFigureList(hero);
        physicsEngine.addToFigureList(villain);
        Timer gameLoop = new Timer(1000 / framerate, (time)->{
                gameEngine.update(framerate);
                physicsEngine.update(framerate);
                renderEngine.update(framerate);

                if (gameEngine.hero.isDeathFinished()){
                    this.frame.remove(renderEngine);
                    this.startMenu();
                    ((Timer)time.getSource()).stop();
                }

        });
        renderEngine.setVisible(true);
        this.frame.add(renderEngine);
        this.frame.revalidate();
        this.frame.repaint();
        renderEngine.requestFocusInWindow();
        gameLoop.start();
    }

    public void startMenu(){
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(windowWidth, windowHeight));
        menu.setFocusable(true);

        this.frame.add(menu);

        JButton startButton = new JButton("Start");
        JTextArea textArea = new JTextArea("Le jeu de l'année");
        menu.add(textArea);
        menu.add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(menu);
                startGame();
            }
        });
        menu.setVisible(true);
        frame.add(menu);
        frame.revalidate();
        frame.repaint();
    }



}
