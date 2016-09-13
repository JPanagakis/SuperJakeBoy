import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by justin on 5/14/16.
 */
public class MapScene extends Scene{

    private final double SCALE_X = OptionsManager.get().getScaleRatioX();
    private final double SCALE_Y = OptionsManager.get().getScaleRatioY();

    protected Sprite playerSprite;
    protected ArrayList<Sprite> floorTileMap;
    protected ArrayList<Sprite> midTileMap;
    protected ArrayList<Sprite> highTileMap;
    protected MapGenerator mapGenerator;
    protected int tileWidth = 0;

    public MapScene(){
        super();

        currentSprites = new ArrayList<>();
        currentMessages = new ArrayList<>();
        midTileMap = new ArrayList<>();
        highTileMap = new ArrayList<>();
        mapGenerator = new MapGenerator();
        
        // Visual Sprites
        setupLayers();
        things = new ArrayList<>();
        mergedVisualSprites = new ArrayList<>();

        playerSprite = new Sprite(true){
            @Override
            public void updateAnimation(long timePassed){
                super.updateAnimation(timePassed);

                updateMovement(timePassed);
            }

            @Override
            public void updateCollisionBounds(){
                cUp = (int)Math.floor(y);
                cDown = (int)Math.floor(y + getHeight());
                cLeft = (int)Math.floor(x);
                cRight = (int)Math.floor(x + getWidth());
            }
        };
        playerSprite.loadImage("Tile_Sprites", "ninjaboy");
        things.add(playerSprite);

        actors = new ArrayList<>();
    }

    public void updateDebugMode(){

        try {
            debug1.setString("Player X: " + playerSprite.getX());
            debug2.setString("Player Y: " + playerSprite.getY());
            debug3.setString("Player DX: " + playerSprite.getVelocityX());
            debug4.setString("Player DY: " + playerSprite.getVelocityY());
            debug5.setString("Player cDown: " + playerSprite.getcDown());
            debug6.setString("Down Tile cUp: " + playerSprite.getDownTile().getcUp());
            debug7.setString("");
            debug8.setString("");
            debug10.setString("");
            debug11.setString("");
            debug10.setString("");
            debug11.setString("");
            debug12.setString("");
            debug13.setString("");
            debug12.setString("");
            debug13.setString("");
            debug14.setString("");
            debug15.setString("");
            debug14.setString("");
            debug15.setString("");
            debug16.setString("");
            debug17.setString("");
            debug16.setString("");
            debug17.setString("");
        } catch (Exception e){

        }

    }

    public void setCurrentMap(Map map){

        layers = map.getLayers();

        tileWidth = map.getTileWidth();
    }

    //////////////////////////   Collision   \\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void updatePlayerSprite(){
        playerSprite.updateLocation();
        playerSprite.updateCollisionBounds();
    }
    
    ///////////////////        Logic Layer          \\\\\\\\\\\\\\\\\\\

    public void updateAllSprites(){
        for (int i = 0; i < layers.length; i++){
            for (int j = 0; j < layers[i].size(); j++){
                layers[i].get(j).updateLocation();
                layers[i].get(j).updateCollisionBounds();
                layers[i].get(j).setArrayLocation(j);
                updateTileCollision(i, j, layers[i].get(j));
            }
        }
        for (int i = 0; i < things.size(); i++){
            things.get(i).updateLocation();
            things.get(i).updateCollisionBounds();
            updateThingCollision(things.get(i).getLayerNum(), things.get(i));
        }
    }

    public void updateTileCollision(int layer, int arrayLoc, Sprite sprite){

        if (arrayLoc - 1 >= 0) {
            sprite.setLeftTile((Tile) layers[layer].get(arrayLoc - 1));
        } else {sprite.setLeftTile(null);}
        if (arrayLoc + 1 < layers[layer].size()) {
            sprite.setRightTile((Tile) layers[layer].get(arrayLoc + 1));
        } else {sprite.setRightTile(null);}
        if (arrayLoc - tileWidth >= 0) {
            sprite.setUpTile((Tile) layers[layer].get(arrayLoc - tileWidth));
        } else {sprite.setUpTile(null);}
        if (arrayLoc + tileWidth < layers[layer].size()) {
            sprite.setDownTile((Tile) layers[layer].get(arrayLoc + tileWidth));
        } else {sprite.setDownTile(null);}
    }

    public void updateThingCollision(int layer, Sprite sprite){
        int xloc = sprite.getLocX();
        int yloc = sprite.getLocY();
        for (int i = 0; i < layers[layer].size(); i++){
            if (xloc > layers[layer].get(i).getcLeft() && xloc <= layers[layer].get(i).getcRight() &&
                    yloc > layers[layer].get(i).getcUp() && yloc <= layers[layer].get(i).getcDown()){
                sprite.setArrayLocation(i);
            }
        }
        sprite.setCurrentTile((Tile)layers[layer].get(sprite.getArrayLoc()));
        updateTileCollision(layer, sprite.getArrayLoc(), sprite);
    }
    
    
    ///////////////////       Visual Layer          \\\\\\\\\\\\\\\\\\\
    
    protected ArrayList<Sprite> mergedVisualSprites;

    protected ArrayList<Sprite>[] layers = new ArrayList[10];

    protected ArrayList<Sprite> things;

    public void setupLayers(){
        for (int i = 0; i < layers.length; i++){
            layers[i] = new ArrayList<>();
        }
    }

    public void mergeVisualLayer(){
        mergedVisualSprites.clear();
        
        for (int i = 0; i < layers.length; i++){
            ArrayList<Sprite> tempMerge = new ArrayList<>();
            for (int h = 0; h < things.size(); h++){
                if (things.get(h).getLayerNum() == i){
                    tempMerge.add(things.get(h));
                }
            }
            for (int j = 0; j < layers[i].size(); j++){
                tempMerge.add(layers[i].get(j));
            }
            sortSpriteArrayList(tempMerge);
            for (int k = 0; k < tempMerge.size(); k++){
                mergedVisualSprites.add(tempMerge.get(k));
            }
        }
    }

    //sort sprites by Y location
    public void sortSpriteArrayList(ArrayList<Sprite> sprites){
        for (int i = 0; i < sprites.size() - 1; i++){
            int smallest = i;
            for (int j = i + 1; j < sprites.size(); j++){
                if (sprites.get(j).getLocY() < sprites.get(smallest).getLocY()){
                    smallest = j;
                }
            }
            Collections.swap(sprites, i, smallest);
        }
    }
    
    

    @Override
    public synchronized void updateScene(){

        if (debugMode) {
            updateDebugMode();
            currentMessages = debugMessages;
        }

        updatePlayerSprite();
        updatePlayerActions();

        updateAllSprites();
        mergeVisualLayer();
        currentSprites = mergedVisualSprites;

        updateMapChange();

        updateCamera();
    }


    //////////////////////////    Map Change   \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    String mapChangeCode;

    public void updateMapChange(){

        switch(loadCycle) {

            case 1:
                if (playerSprite.getCurrentTile().getTileType().equals("Changeable")) {
                    Thread fadeInThread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            MilliTimer fTimer = new MilliTimer();
                            int runTime = 300;
                            fTimer.start();
                            while (fTimer.getElapsedTime() < runTime){
                                setLoadOpacity((fTimer.getElapsedTime() / (double)runTime));
                            }
                            setFadedIn(true);
                        }
                    });

                    loading = true;
                    try {
                        fadeInThread.start();
                    } catch (IllegalThreadStateException e){
                        e.printStackTrace();
                        fadeInThread.run();
                    }
                    
                    loadCycle = 2;
                }
                break;
            case 2:
                if (fadedIn) {

                    doMapChange(mapChangeCode);

                    loadCycle = 3;
                }
                break;
            case 3:
                Thread fadeoutThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        MilliTimer fTimer = new MilliTimer();
                        int runTime = 300;
                        fTimer.start();
                        while (fTimer.getElapsedTime() < runTime){
                            setLoadOpacity(1 - (fTimer.getElapsedTime() / (double)runTime));
                        }
                        setFadedIn(false);
                        loading = false;
                    }
                });

                try {
                    fadeoutThread.start();
                } catch (IllegalThreadStateException e){
                    e.printStackTrace();
                    fadeoutThread.run();
                }

                loadCycle = 1;
                break;
        }
    }

    //Implement in Every MapScene
    public void doMapChange(String s){

    }

    public void loadVendor(){

    }

    //////////////////////////      Camera      \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    double camX, camY;
    int viewportSizeX, viewportSizeY, worldSizeX, worldSizeY, offsetMinX, offsetMinY, offsetMaxX, offsetMaxY;

    public void initCamera() {
        viewportSizeX = (int) (OptionsManager.get().getViewportX() * SCALE_X);
        viewportSizeY = (int) (OptionsManager.get().getViewportY() * SCALE_Y);
        worldSizeX = (int) (tileWidth * 50 * SCALE_X);
        //worldSizeY = (int) ((floorTileMap.size() / tileWidth) * 50 * SCALE_Y);
        worldSizeY = (int) ((layers[0].size() / tileWidth) * 50 * SCALE_Y);
        offsetMinX = 0;
        offsetMinY = 0;
        offsetMaxX = worldSizeX - viewportSizeX;
        offsetMaxY = worldSizeY - viewportSizeY;
    }

    public void updateCamera(){

        if (viewportSizeX < worldSizeX) {
            camX = playerSprite.getLocX() - viewportSizeX / 2;
            if (camX > offsetMaxX){
                camX = offsetMaxX;
            } else if (camX < offsetMinX){
                camX = offsetMinX;
            }
        } else {
            camX = - (viewportSizeX - worldSizeX) / 2;
        }
        if (viewportSizeY < worldSizeY) {
            camY = playerSprite.getLocY() - viewportSizeY / 2;
            if (camY > offsetMaxY){
                camY = offsetMaxY;
            } else if (camY < offsetMinY){
                camY = offsetMinY;
            }
        } else {
            camY = - (viewportSizeY - worldSizeY) / 2;
        }
    }

    public double getCamX(){
        return camX;
    }

    public double getCamY(){
        return camY;
    }


    //////////////////////////     Controls     \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\


    private boolean fastMode = false;
    private boolean forwardPressed = false;
    private boolean backPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    @Override
    public void forward(){
        forwardPressed = true;

        if (!isCutSceneMode()){
            if (playerSprite.getDownTile().getTileType().equals("Block") && cd){
                doJump();
            }
            if (!playerSprite.getDownTile().getTileType().equals("Block") && cd &&
                    ((cl && playerSprite.getDownTile().getLeftTile().getTileType().equals("Block")) ||
                            (cr && playerSprite.getDownTile().getRightTile().getTileType().equals("Block")))){
                doJump();
            }
        }
    }

    @Override
    public void back(){
        backPressed = true;

        if (!isCutSceneMode()) {
            
        }
    }

    @Override
    public void triangle(){

    }

    @Override
    public void square(){

    }

    @Override
    public void up(){
        upPressed = true;

        if (!isCutSceneMode()) {
            
        }
    }

    @Override
    public void down(){
        downPressed = true;

        if (!isCutSceneMode()) {
            
        }
    }

    @Override
    public void left(){
        leftPressed = true;

        if (!isCutSceneMode()) {

        }
    }

    @Override
    public void right(){
        rightPressed = true;

        if (!isCutSceneMode()) {

        }
    }

    @Override
    public void forwardReleased(){
        forwardPressed = false;

        if (!isCutSceneMode()){
            
        }
    }

    @Override
    public void backReleased(){
        backPressed = false;

        if (!isCutSceneMode()) {
            
        }
    }

    @Override
    public void triangleReleased(){
        
    }

    @Override
    public void squareReleased(){
        
    }

    @Override
    public void upReleased(){
        upPressed = false;

        if (!isCutSceneMode()) {
            
        }
    }

    @Override
    public void downReleased(){
        downPressed = false;

        if (!isCutSceneMode()) {
            
        }
    }

    @Override
    public void leftReleased(){
        leftPressed = false;

        if (!isCutSceneMode()) {
            
        }
    }

    @Override
    public void rightReleased(){
        rightPressed = false;

        if (!isCutSceneMode()) {
            
        }
    }

    ///////////////////  Collision Type Checks  \\\\\\\\\\\\\\\\\\\

    protected boolean cl = false, cr = false, cu = false, cd = false;

    public void updateCollision(){
        try {
            if (playerSprite.getcLeft() < playerSprite.getLeftTile().getcRight()){
                cl = true;
            } else {
                cl = false;
            }
            if (playerSprite.getcRight() > playerSprite.getRightTile().getcLeft()){
                cr = true;
            } else {
                cr = false;
            }
            if (playerSprite.getcUp() <= playerSprite.getUpTile().getcDown()){
                cu = true;
            } else {
                cu = false;
            }
            if (playerSprite.getcDown() >= playerSprite.getDownTile().getcUp()){
                cd = true;
            } else {
                cd = false;
            }
        } catch (Exception e){

        }

        if (playerSprite.getLeftTile() != null) {
            if (playerSprite.getVelocityX() <= 0 && cl &&
                    !playerSprite.getLeftTile().getTileType().equals("Space")){
                playerSprite.setVelocityX(0);
                playerSprite.setX(playerSprite.getLeftTile().getcRight());
            }
        }
        if (playerSprite.getRightTile() != null) {
            if (playerSprite.getVelocityX() >= 0 && cr &&
                    !playerSprite.getRightTile().getTileType().equals("Space")){
                playerSprite.setVelocityX(0);
                playerSprite.setX(playerSprite.getCurrentTile().getX());
            }
        }
        if (playerSprite.getUpTile() != null) {
            if (playerSprite.getVelocityY() <= 0 && cu &&
                    !playerSprite.getUpTile().getTileType().equals("Space")){
                playerSprite.setVelocityY(0);
            }
            if (cu && playerSprite.getVelocityY() <= 0 &&
                    ((cl && !playerSprite.getUpTile().getLeftTile().getTileType().equals("Space")) ||
                            (cr && !playerSprite.getUpTile().getRightTile().getTileType().equals("Space")))){
                playerSprite.setVelocityY(0);
            }
        }
        if (playerSprite.getDownTile() != null){
            if (cd &&
                    ((cl && !playerSprite.getDownTile().getLeftTile().getTileType().equals("Space")) ||
                            (cr && !playerSprite.getDownTile().getRightTile().getTileType().equals("Space")))){
                playerSprite.setVelocityY(0);
            }
        }


        if (playerSprite.getVelocityX() > 0.5){playerSprite.setVelocityX((float)0.5);}
        if (playerSprite.getVelocityX() < -0.5){playerSprite.setVelocityX((float)-0.5);}
        if (playerSprite.getVelocityY() > 0.5){playerSprite.setVelocityY((float)0.5);}
    }


    ///////////////////     Player Actions      \\\\\\\\\\\\\\\\\\\

    public void updatePlayerActions(){
        updateCollision();
        updateMove();
    }

    public void doJump(){
        playerSprite.setVelocityY((float)-1.2);
    }

    public void updateMove(){
        if (rightPressed){
            if (playerSprite.getRightTile() != null) {
                if (playerSprite.getRightTile().getTileType().equals("Space")) {
                    if (playerSprite.getVelocityX() < 0.5) {
                        if (playerSprite.getVelocityX() < 0){
                            playerSprite.setVelocityX((float) (playerSprite.getVelocityX() + 0.03));
                        } else {
                            playerSprite.setVelocityX((float) (playerSprite.getVelocityX() + 0.01));
                        }
                    }
                } else if (playerSprite.getVelocityX() >= 0 &&
                        playerSprite.getcRight() >= playerSprite.getRightTile().getcLeft()) {
                    playerSprite.setVelocityX(0);
                } else if (playerSprite.getVelocityX() >= 0){
                    if (playerSprite.getVelocityX() < 0.5){
                        if (playerSprite.getVelocityX() < 0){
                            playerSprite.setVelocityX((float) (playerSprite.getVelocityX() + 0.03));
                        } else {
                            playerSprite.setVelocityX((float) (playerSprite.getVelocityX() + 0.01));
                        }
                    }
                }
            }
        }

        if (leftPressed){
            if (playerSprite.getLeftTile() != null) {
                if (playerSprite.getLeftTile().getTileType().equals("Space")) {
                    if (playerSprite.getVelocityX() > -0.5){
                        if (playerSprite.getVelocityX() > 0){
                            playerSprite.setVelocityX((float) (playerSprite.getVelocityX() - 0.03));
                        } else {
                            playerSprite.setVelocityX((float) (playerSprite.getVelocityX() - 0.01));
                        }
                    }
                } else if (playerSprite.getVelocityX() <= 0 &&
                        playerSprite.getcLeft() <= playerSprite.getLeftTile().getcRight()){
                    playerSprite.setVelocityX(0);
                } else if (playerSprite.getVelocityX() <= 0){
                    if (playerSprite.getVelocityX() > -0.5){
                        if (playerSprite.getVelocityX() > 0){
                            playerSprite.setVelocityX((float) (playerSprite.getVelocityX() - 0.03));
                        } else {
                            playerSprite.setVelocityX((float) (playerSprite.getVelocityX() - 0.01));
                        }
                    }
                }
            }
        }

        if (!leftPressed && !rightPressed){
            if (playerSprite.getVelocityX() > -.05 && playerSprite.getVelocityX() < .05){
                playerSprite.setVelocityX(0);
            } else if (playerSprite.getVelocityX() > 0){
                playerSprite.setVelocityX((float)(playerSprite.getVelocityX() - 0.05));
            } else if (playerSprite.getVelocityX() < 0){
                playerSprite.setVelocityX((float)(playerSprite.getVelocityX() + 0.05));
            }
        }

        if (playerSprite.getDownTile() != null){
            if (playerSprite.getDownTile().getTileType().equals("Space")){
                if (cd &&
                        ((cl && !playerSprite.getDownTile().getLeftTile().getTileType().equals("Space")) ||
                                (cr && !playerSprite.getDownTile().getRightTile().getTileType().equals("Space")))){
                    playerSprite.setVelocityY(0);
                    playerSprite.setY(playerSprite.getCurrentTile().getY());
                } else if (playerSprite.getVelocityY() < 0.5) {
                    playerSprite.setVelocityY((float) (playerSprite.getVelocityY() + 0.05));
                }
            } else if (playerSprite.getcDown() >= playerSprite.getDownTile().getcUp()){
                playerSprite.setVelocityY(0);
                playerSprite.setY(playerSprite.getCurrentTile().getY());
            } else {
                playerSprite.setVelocityY((float) (playerSprite.getVelocityY() + 0.05));
            }
        }
    }


    ///////////////////// General Cut Scenes \\\\\\\\\\\\\\\\\\\\\\

    protected boolean cutSceneMode = false;
    protected ArrayList<Sprite> actors;
    
    public boolean isCutSceneMode(){
        return cutSceneMode;
    }

    public void swapTiles(Sprite s1, Sprite s2){

        Collections.swap(layers[1], s1.getArrayLoc(), s2.getArrayLoc());
    }
}
