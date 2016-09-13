/**
 * Created by justin on 9/10/16.
 */
public class TestMap1 extends Map {

    private int[][] visualMap = {
            {
                    2,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
                    2,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
                    2,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
                    2,0,0,0,0,0,0,2,2,2,0,0,0,2,2,
                    2,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
                    2,0,0,0,0,0,0,0,0,0,0,2,2,0,2,
                    2,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
                    2,0,0,0,0,0,0,0,2,2,2,0,0,0,2,
                    2,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
                    2,2,2,2,2,2,2,2,2,2,2,2,2,2,2
            }
    };

    private int[][] logicMap = {
            {
                    B,S,S,S,S,S,S,S,S,S,S,S,S,S,B,
                    B,S,S,S,S,S,S,S,S,S,S,S,S,S,B,
                    B,S,S,S,S,S,S,S,S,S,S,S,S,S,B,
                    B,S,S,S,S,S,S,B,B,B,S,S,S,B,B,
                    B,S,S,S,S,S,S,S,S,S,S,S,S,S,B,
                    B,S,S,S,S,S,S,S,S,S,S,B,B,S,B,
                    B,S,S,S,S,S,S,S,S,S,S,S,S,S,B,
                    B,S,S,S,S,S,S,S,B,B,B,S,S,S,B,
                    B,S,S,S,S,S,S,S,S,S,S,S,S,S,B,
                    B,B,B,B,B,B,B,B,B,B,B,B,B,B,B
            }
    };

    public TestMap1(){
        super();

        tileWidth = 15;
        tileSet = "Tile";

        setupJackStart(3, 9, 0);

        //setupMapChangeCodes();
        //setup(floorMap, midMap, typeMap, mapChangeCodes);
        setupMaps(visualMap, logicMap);
    }
}
