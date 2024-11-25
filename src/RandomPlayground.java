import java.io.BufferedReader;
import java.util.*;
import java.io.FileReader;


public class RandomPlayground {
    private ArrayList<Sprite> environment;

    private int tileWidth;
    private int tileHeight;



    private ArrayList<ArrayList<Integer>> map;
    private ArrayList<ArrayList<ArrayList<Character>>> groups;
    private ArrayList<Map<String, ArrayList<String>>> possibilities;

    private int numberOfGroups;
    private int tilesPerGroup;

    String filepath;
    public ArrayList<ArrayList<Character>> textFile;


    public RandomPlayground(String levelPath, int size){

        try {

            filepath = levelPath;
            this.textFile = new ArrayList<>();
            this.possibilities = new ArrayList<>();
            this.groups = new ArrayList<>();

            } 
        catch (Exception ignored) {
        }

        this.loadPossibilityArray();
        this.loadTileGroups();
        this.generateMap(size,size);
        this.setTileSize(200,200);
        this.setMap();


    }

    // Getters //

    public ArrayList<Sprite> getSpriteList(){
        return environment;
    }

    // Setters

    public void setTileSize(int width, int height){
        this.tileWidth = width;
        this.tileHeight = height;
    }


    // Loading functions //
    public void skipLines(BufferedReader buffer,int n){
        try{
            for (int i = 0; i < n; i++) {
                buffer.readLine();
            }
        }
        catch (Exception ignored) {}
    }

    public void loadPossibilityArray(){
        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.filepath));

            String line = bufferedReader.readLine();

            this.numberOfGroups = Integer.parseInt(line.split(":")[1]);
            line = bufferedReader.readLine();
            this.tilesPerGroup = Integer.parseInt(line.split(":")[1]);
            skipLines(bufferedReader, 2);
            for (int i = 0; i < this.numberOfGroups; i++) {
                bufferedReader.readLine();
                line = bufferedReader.readLine();

                List<String> rPossibilities = Arrays.asList(line.split(":")[1].split(","));
                line =bufferedReader.readLine();
                List<String> dPossibilities = Arrays.asList(line.split(":")[1].split(","));

                HashMap<String, ArrayList<String>> possibilitiesMap = new HashMap<>();
                possibilitiesMap.put("right", new ArrayList<>(rPossibilities));
                possibilitiesMap.put("down", new ArrayList<>(dPossibilities));

                this.possibilities.add(possibilitiesMap);

            }
            bufferedReader.close();
        }
        catch (Exception ignored) {}
    }

    public void loadTileGroups(){
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.filepath));
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
                        arr.add(' ');
                    }
                    groupTextFile.add(arr);
                }
                groups.add(groupTextFile);
                skipLines(bufferedReader, 1);
            }
            bufferedReader.close();

        }
        catch (Exception ignored) {}
    }

    // Generating the map //

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
                res = possibilities.get(leftGroup).get("right");
            } else if (j == 0) {
                int upGroup = map.get(i - 1).get(j);
                res =  possibilities.get(upGroup).get("down");
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

        return res;
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

    public void setMap(){
        environment = new ArrayList<>();
        Sprite readTile;

        for (int i = 0; i < this.map.size(); i++) {
            for (int j = 0; j < this.map.get(i).size(); j++) {
                ArrayList<ArrayList<Character>> group;
                group = this.groups.get(map.get(i).get(j));

                for (int k = 0; k < Math.sqrt(tilesPerGroup); k++) {
                    for(int l = 0; l < Math.sqrt(tilesPerGroup); l++) {
                        if (group.get(k).get(l) == 'T') {
                            readTile = new SolidSprite("./img/tree.png", (int)(j*tileWidth*Math.sqrt(tilesPerGroup)+l*tileWidth), (int)(i*tileWidth*Math.sqrt(tilesPerGroup)+k*tileHeight), tileWidth, tileHeight, tileWidth, tileHeight, -0,0);
                        }

                        else if (group.get(k).get(l) == 'R') {
                            readTile = new SolidSprite("./img/rock.png",  (int)(j*tileWidth*Math.sqrt(tilesPerGroup)+l*tileWidth), (int)(i*tileWidth*Math.sqrt(tilesPerGroup)+k*tileHeight), tileWidth, tileHeight,tileWidth, tileHeight,-0,0);
                        }

                        else {
                            readTile = new Sprite("./img/grass.png",  (int)(j*tileWidth*Math.sqrt(tilesPerGroup)+l*tileWidth), (int)(i*tileHeight*Math.sqrt(tilesPerGroup)+k*tileWidth), tileWidth, tileHeight);
                        }
                        environment.add(readTile);
                    }
                }
            }
        }
    }



}
