import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.Random;
import java.util.TimerTask;
import javax.swing.JPanel;

import static java.lang.Integer.parseInt;

public class App{
    

    int framerate;
    int windowWidth = 1080;
    int windowHeight = 720;

    JFrame frame;


    public App(){
        this.frame = new JFrame();
        this.frame.setTitle("TD4");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(this.windowWidth, this.windowHeight);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
        this.frame.setResizable(false);
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.startMenu();
    }

    public void startRandomMapGame() {
        Bomb.bombList = new ArrayList<Bomb>();
        Fireball.fireballList = new ArrayList<Fireball>();
        ZoomBox.zoomBoxeList = new ArrayList<ZoomBox>();

        Figure hero =new Figure("./img/heroTileSheetWithSword.png", 5*3*200  +200+ 75 ,5*3*200 +200 + 75, 100,100,70,100,15, 0);
        hero.rescale(0.75);
        RenderEngine renderEngine = new RenderEngine(windowWidth, windowHeight);
        PhysicsEngine physicsEngine = new PhysicsEngine();
        GameEngine gameEngine = new GameEngine(hero);

        renderEngine.setReferenceToOtherEngine(physicsEngine, gameEngine);
        physicsEngine.setReferenceToOtherEngine(gameEngine, renderEngine);
        gameEngine.setReferenceToOtherEngine(physicsEngine, renderEngine);

        renderEngine.addKeyListener(gameEngine);

        int mapSize = 10;
        int enemyNumber = 10;
        RandomPlayground level1 = new RandomPlayground("./data/tileGroupRepertory2.txt", mapSize);
        level1.setTileWidth(200);
        ArrayList<Sprite> levelSpriteList = level1.getSpriteList();
        physicsEngine.setEnvironment(levelSpriteList);

        for(Displayable displayable : levelSpriteList){
            renderEngine.addToRenderList(displayable);
        }

        Random random = new Random();
        ArrayList<Point> pointList = new ArrayList<>();
        pointList.add(new Point(5,5));
        for (int i = 0; i < enemyNumber; i++) {
            int x, y;
            do {
                x = random.nextInt(mapSize-1);
                y = random.nextInt(mapSize-1);
            } while(pointList.contains(new Point(x, y)));




            System.out.println(x + " " + y);
            AutoFigure enemy = new AutoFigure("./img/villainTileSheetWithSword.png", x*3*200  +200+ 75 ,y*3*200 +200 + 75, 100,100,70,100,15, 0);
            enemy.rescale(0.75);
            renderEngine.addToRenderList(enemy);
            physicsEngine.addToFigureList(enemy);

        }
        renderEngine.addToRenderList(hero);
        physicsEngine.addToFigureList(hero);

        Timer gameLoop = new Timer(1000 / framerate, (time)->{
                gameEngine.update(framerate);
                physicsEngine.update(framerate);
                renderEngine.update(framerate);

                if (gameEngine.hero.isDeathFinished()|gameEngine.earlyExit){
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

    public void startBossFight() {
        Bomb.bombList = new ArrayList<Bomb>();
        Fireball.fireballList = new ArrayList<Fireball>();
        ZoomBox.zoomBoxeList = new ArrayList<ZoomBox>();

        Figure hero =new Figure("./img/heroTileSheetWithSword.png", 4*200 + 75,9*200+75, 100,100,70,100,15, 0);
        hero.rescale(0.75);
        Figure boss = new Boss("./img/villainTileSheetWithSword.png", 7*200 ,7*200, 100,100,70,100,15, 0);
        RenderEngine renderEngine = new RenderEngine(windowWidth, windowHeight);
        PhysicsEngine physicsEngine = new PhysicsEngine();
        GameEngine gameEngine = new GameEngine(hero);

        renderEngine.setReferenceToOtherEngine(physicsEngine, gameEngine);
        physicsEngine.setReferenceToOtherEngine(gameEngine, renderEngine);
        gameEngine.setReferenceToOtherEngine(physicsEngine, renderEngine);

        renderEngine.addKeyListener(gameEngine);

        Playground map = new Playground("./data/level1.txt");
        map.setTileWidth(200);
        ArrayList<Sprite> levelSpriteList = map.getSpriteList();

        physicsEngine.setEnvironment(levelSpriteList);

        for(Displayable displayable : levelSpriteList){
            renderEngine.addToRenderList(displayable);
        }


        renderEngine.addToRenderList(boss);
        renderEngine.addToRenderList(hero);


        physicsEngine.addToFigureList(boss);
        physicsEngine.addToFigureList(hero);

        ZoomBox dezooomBox = new ZoomBox(6*200,6*200,13*200,13*200);
        renderEngine.addToRenderList(dezooomBox);

        Timer gameLoop = new Timer(1000 / framerate, (time)->{
            gameEngine.update(framerate);
            physicsEngine.update(framerate);
            renderEngine.update(framerate);

            if (gameEngine.hero.isDeathFinished()|gameEngine.earlyExit){
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
        try {

            BufferedImage backgroundImage = ImageIO.read(new File("./img/menuBackground.png"));
            JPanel menu = new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(backgroundImage, 0, 0, null);

                }
            };
            menu.setPreferredSize(new Dimension(windowWidth, windowHeight));
            menu.setFocusable(true);
            menu.setLayout(null);


            JLabel title = new JLabel();
            JTextArea textArea = new JTextArea("Enter framerate");
            JTextField framerateBox = new JTextField("60", 5);
            JButton bossButton = new JButton();
            JButton randomMapButton = new JButton();








            ImageIcon image = new ImageIcon("./img/bossButton.png");
            Image scaledImage = image.getImage().getScaledInstance(880/3, 260/3, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            bossButton.setIcon(scaledIcon);
            bossButton.setBorderPainted(false);
            bossButton.setFocusPainted(false);
            bossButton.setContentAreaFilled(false);
            bossButton.setBounds(164, 500, 880/3, 260/3);


            image = new ImageIcon("./img/randomButton.png");
            scaledImage = image.getImage().getScaledInstance(880/3, 260/3, Image.SCALE_SMOOTH);
            scaledIcon = new ImageIcon(scaledImage);
            randomMapButton.setIcon(scaledIcon);
            randomMapButton.setBorderPainted(false);
            randomMapButton.setFocusPainted(false);
            randomMapButton.setContentAreaFilled(false);
            randomMapButton.setBounds(622, 500, 880/3, 260/3);

            image = new ImageIcon("./img/title.png");
            title.setIcon(image);
            title.setBounds(100,200,920,241);

            textArea.setBounds(600,100,170,30);
            textArea.setEditable(false);
            textArea.setFont(new Font("Arial", Font.PLAIN, 24));

            framerateBox.setBounds(780,100,70,30);
            framerateBox.setFont(new Font("Arial", Font.PLAIN, 24));



            menu.add(title);
            menu.add(textArea);
            menu.add(randomMapButton);
            menu.add(bossButton);
            menu.add(framerateBox);

            this.frame.add(menu);

            randomMapButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.remove(menu);
                    framerate = parseInt(framerateBox.getText());
                    System.out.println("framerate = "+ framerate);
                    startRandomMapGame();
                }
            });

            bossButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.remove(menu);
                    framerate = parseInt(framerateBox.getText());
                    System.out.println("framerate = "+ framerate);
                    startBossFight();
                }
            });
            menu.setVisible(true);
            frame.add(menu);
            frame.revalidate();
            frame.repaint();
        }
        catch (Exception e){}

    }



}
