import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;


public class Playground {
    private ArrayList<Sprite> environment;
    private int tileWidth;
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
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<SolidSprite> getSolidSpriteList(){
        ArrayList<SolidSprite> res = new ArrayList<SolidSprite>();
        for (Sprite sprite : environment){
            if (sprite instanceof SolidSprite){
                res.add((SolidSprite) sprite);
            }
        }
        return res;
    }

    public ArrayList<Sprite> getSpriteList(){
        return environment;
    }

    public void setTileWidth(int width){
        this.tileWidth = width;
        this.setMap();
    }

    public void setMap(){
        System.out.println("setting the map");
        environment = new ArrayList<Sprite>();
        Sprite readTile;


        for (int i = 0; i < textFile.length; i++) {
            for (int j = 0; j < textFile[i].length; j++) {
                //System.out.println(i + " " + j + " " + textFile.length + " " + textFile[i].length);
                if (textFile[i][j] == 'T') {
                    readTile = new SolidSprite("./img/tree.png", j*tileWidth, i*tileWidth, tileWidth, tileWidth, tileWidth, tileWidth, 0,0);
                }

                else if (textFile[i][j] == 'R') {
                    readTile = new SolidSprite("./img/rock.png", j*tileWidth, i*tileWidth, tileWidth, tileWidth, tileWidth, tileWidth, 0,0);
                }

                else {
                    readTile = new Sprite("./img/grass.png", j*tileWidth, i*tileWidth, tileWidth, tileWidth);
                }

                environment.add(readTile); //
                readTile.height = tileWidth;
                readTile.width = tileWidth;
                //System.out.println("Element placé : "+ textFile[i][j]+" " + readTile.x + " " + readTile.y);

            }
        }

        
    }
}
