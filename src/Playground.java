import javax.swing.plaf.metal.MetalLookAndFeel;
import java.io.BufferedReader;
import java.util.*;
import java.io.FileReader;


public class Playground {
    private ArrayList<Sprite> environment;
    private int tileWidth;
    public ArrayList<ArrayList<Character>> textFile;

    private ArrayList<ArrayList<Integer>> map;
    private ArrayList<ArrayList<ArrayList<Character>>> groups;
    private ArrayList<Map<String, ArrayList<String>>> possibilities;

    private int numberOfGroups;
    private int tilesPerGroup;


    
    
    public Playground(String levelPath){

        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(levelPath));
            this.textFile = new ArrayList<>();
            this.possibilities = new ArrayList<>();
            this.groups = new ArrayList<>();
            for (int i = 0; i < 17; i++){
                String line = bufferedReader.readLine();
                ArrayList<Character> arr = new ArrayList<>();

                for (char c: line.toCharArray()){
                    arr.add(c);
                }

                textFile.add(arr);
                
            }
            bufferedReader.close();

            } 
        catch (Exception e) {
            e.printStackTrace();

        }

        this.loadPossibilityArray();
        this.loadTileGroups();
        this.generateMap(3,3);
        //System.out.println(this.map);


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
        this.setMap2();
    }

    public void setMap(){
        System.out.println("setting the map");
        environment = new ArrayList<Sprite>();
        Sprite readTile;


        for (int i = 0; i < textFile.size(); i++) {
            for (int j = 0; j < textFile.get(i).size(); j++) {
                if (textFile.get(i).get(j) == 'T') {
                    readTile = new SolidSprite("./img/tree.png", j*tileWidth, i*tileWidth, tileWidth, tileWidth, tileWidth, tileWidth, -0,0);
                }

                else if (textFile.get(i).get(j) == 'R') {
                    readTile = new SolidSprite("./img/rock.png", j*tileWidth, i*tileWidth, tileWidth, tileWidth,tileWidth, tileWidth,-0,0);
                }

                else {
                    readTile = new Sprite("./img/grass.png", j*tileWidth, i*tileWidth, tileWidth, tileWidth);
                }

                environment.add(readTile); //
                readTile.height = tileWidth;
                readTile.width = tileWidth;
                //System.out.println("Element placé : "+ textFile.get(i).get(j)+" " + readTile.x + " " + readTile.y);

            }
        }



        
    }

    public void setMap2(){
        System.out.println("setting the map");
        environment = new ArrayList<Sprite>();
        Sprite readTile;


        for (int i = 0; i < this.map.size(); i++) {
            for (int j = 0; j < this.map.get(i).size(); j++) {
                ArrayList<ArrayList<Character>> group = new ArrayList<>();
                group = this.groups.get(map.get(i).get(j));

                for (int k = 0; k < Math.sqrt(tilesPerGroup); k++) {
                    for(int l = 0; l < Math.sqrt(tilesPerGroup); l++) {
                        if (group.get(k).get(l) == 'T') {
                            readTile = new SolidSprite("./img/tree.png", (int)(j*tileWidth*Math.sqrt(tilesPerGroup)+l*tileWidth), (int)(i*tileWidth*Math.sqrt(tilesPerGroup)+k*tileWidth), tileWidth, tileWidth, tileWidth, tileWidth, -0,0);
                        }

                        else if (group.get(k).get(l) == 'R') {
                            readTile = new SolidSprite("./img/rock.png",  (int)(j*tileWidth*Math.sqrt(tilesPerGroup)+l*tileWidth), (int)(i*tileWidth*Math.sqrt(tilesPerGroup)+k*tileWidth), tileWidth, tileWidth,tileWidth, tileWidth,-0,0);
                        }

                        else {
                            readTile = new Sprite("./img/grass.png",  (int)(j*tileWidth*Math.sqrt(tilesPerGroup)+l*tileWidth), (int)(i*tileWidth*Math.sqrt(tilesPerGroup)+k*tileWidth), tileWidth, tileWidth);
                        }
                        environment.add(readTile); //
                        readTile.height = tileWidth;
                        readTile.width = tileWidth;
                    }
                }
            }



                //System.out.println("Element placé : "+ textFile.get(i).get(j)+" " + readTile.x + " " + readTile.y);

            }
        }
    public void generateMap(int width, int height){
        this.map = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            this.map.add(new ArrayList<>(width));
        }
        for (int i = 0; i < height; i++){
            ArrayList<Integer> line = new ArrayList<>(width);
            for (int j = 0; j < width; j++){
                if (i==0 && j == 0){
                    line.add(1);
                }
                else{
                    Random rand = new Random();
                    ArrayList<String> possibilities = findPossibilities(i,j);
                    line.add(Integer.parseInt(possibilities.get(rand.nextInt(possibilities.size()))));
                }
                this.map.set(i, line);
            }

        }

    }
    public void loadPossibilityArray(){
        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader("./data/tileGroupRepertory.txt"));


            String line = bufferedReader.readLine();

            this.numberOfGroups = Integer.parseInt(line.split(":")[1]);
            line = bufferedReader.readLine();
            this.tilesPerGroup = Integer.parseInt(line.split(":")[1]);
            skipLines(bufferedReader, 2);
            for (int i = 0; i < this.numberOfGroups; i++) {
                bufferedReader.readLine();
                line = bufferedReader.readLine();
                //System.out.println(line);
                List<String> rPossibilities = Arrays.asList(line.split(":")[1].split(","));
                line =bufferedReader.readLine();
                List<String> dPossibilities = Arrays.asList(line.split(":")[1].split(","));
                //System.out.println(line);

                HashMap<String, ArrayList<String>> possibilitiesMap = new HashMap<>();
                possibilitiesMap.put("right", new ArrayList<>(rPossibilities));
                possibilitiesMap.put("down", new ArrayList<String>(dPossibilities));

                this.possibilities.add(possibilitiesMap);

            }
            bufferedReader.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTileGroups(){
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new FileReader("./data/tileGroupRepertory.txt"));
            skipLines(bufferedReader, 4+this.numberOfGroups*3+1);
            for (int i = 0; i < this.numberOfGroups; i++) {
                ArrayList<ArrayList<Character>> groupTextFile = new ArrayList<>();
                skipLines(bufferedReader, 3);
                for (int j = 0; j < Math.sqrt(this.tilesPerGroup); j++) {
                    ArrayList<Character> arr = new ArrayList<>();
                    line = bufferedReader.readLine();
                    for (char c: line.toCharArray()){
                        arr.add(c);
                    }

                    while(arr.size()<Math.sqrt(this.tilesPerGroup)){
                        arr.add(new Character(' '));
                    }
                    groupTextFile.add(arr);
                }
                groups.add(groupTextFile);
                skipLines(bufferedReader, 1);
            }
            bufferedReader.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void skipLines(BufferedReader buffer,int n){
        try{
            for (int i = 0; i < n; i++) {
                buffer.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> findPossibilities(int i, int j){
        ArrayList<String> res = new ArrayList<>();
        if (i==0 && j==0){
            for (int k=0; k<this.numberOfGroups; k++){
                res.add(String.valueOf(k));
            }
        }


        else {
            if (i == 0) {
                int leftGroup = map.get(i).get(j - 1);
                ArrayList<String> leftPossibilities = possibilities.get(leftGroup).get("right");
                res = leftPossibilities;
            } else if (j == 0) {
                int upGroup = map.get(i - 1).get(j);
                //System.out.println(upGroup);
                ArrayList<String> upPossibilities = possibilities.get(upGroup).get("down");
                res = upPossibilities;
            } else {
                int upGroup = map.get(i - 1).get(j);
                int leftGroup = map.get(i).get(j - 1);

                ArrayList<String> upPossibilities = possibilities.get(upGroup).get("down");
                ArrayList<String> leftPossibilities = possibilities.get(leftGroup).get("right");

                for (String p : upPossibilities) {
                    if (leftPossibilities.contains(p)) {
                        res.add(p);
                    }
                }
            }
        }
        if (res.isEmpty()){
            res.add("0");
        }
        //System.out.println(res);
        return res;
    }
}
