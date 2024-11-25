import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;


public class Playground {
    private ArrayList<Sprite> environment;

    private int tileWidth;
    private int tileHeight;

    public char[][] textFile = new char[16][];

    public Playground(String levelPath){

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(levelPath));

            for (int i = 0; i < 16; i++){
                String line = bufferedReader.readLine();
                textFile[i] = line.toCharArray();
                }

            bufferedReader.close();

            } 
        catch (Exception ignored) {}

        this.setTileSize(200,200);
        this.setMap();
    }
    // Getters //
    public ArrayList<Sprite> getSpriteList(){
        return environment;
    }

    // Setters //
    public void setTileSize(int width, int height){
        this.tileWidth = width;
        this.tileHeight = height;
    }

    public void setMap(){
        environment = new ArrayList<>();
        Sprite readTile;

        for (int i = 0; i < textFile.length; i++) {
            for (int j = 0; j < textFile[i].length; j++) {
                if (textFile[i][j] == 'T') {
                    readTile = new SolidSprite("./img/tree.png", j*tileWidth, i*tileHeight, tileWidth, tileHeight, tileWidth, tileHeight, 0,0);
                }

                else if (textFile[i][j] == 'R') {
                    readTile = new SolidSprite("./img/rock.png", j*tileWidth, i*tileHeight, tileWidth, tileHeight, tileWidth, tileHeight, 0,0);
                }

                else {
                    readTile = new Sprite("./img/grass.png", j*tileWidth, i*tileHeight, tileWidth, tileHeight);
                }
                environment.add(readTile);

            }
        }

        
    }
}
