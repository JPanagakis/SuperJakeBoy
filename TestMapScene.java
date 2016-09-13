/**
 * Created by justin on 9/10/16.
 */
public class TestMapScene extends MapScene {

    public TestMapScene(){
        super();

        setCurrentMap(new TestMap1());

        startDebugMode();

        initCamera();
    }
}
