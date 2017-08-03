package engine.scenes;

import engine.drawable.StationObject;
import engine.model.Logic;
import engine.model.Station;
import jGame.model.game.GameObject;
import jGame.model.game.GameScene;
import jGame.model.graphics.Camera;
import jGame.model.input.Input;
import jGame.model.timer.SimpleTimer;
import jGame.view.Renderer;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * JM_Monitors created by jmkhilario on 02/08/2017.
 */
public class GUIScene extends GameScene {

    public static final int CENTER_X = (720/2)-50;
    public static final int CENTER_Y = (480/2);
    public static final int RADIUS = (480/3);

    public Camera camera;
    public HashMap<Integer, SimpleTimer> inputTimers;
    public ArrayList<Station> stations;

    public GUIScene(String name) {
        super(name);
        camera = new Camera(500);
        inputTimers = new HashMap<>();
        inputTimers.put(KeyEvent.VK_ENTER, new SimpleTimer(1.00f));
        setDoneLoading(true);
        init();
    }

    @Override
    public void init() {
        camera.reset();
        getActors().clear();
        // initialize station objects
            stations = new ArrayList<>();
            StationObject tempStationObject;
            for (int i = 0; i < 8; i++) {
                Double angleRad = i * (2.0 * Math.PI) / 8.0;
                long x = CENTER_X + (long)(Math.cos(angleRad) * RADIUS);
                long y = CENTER_Y + (long)(Math.sin(angleRad) * RADIUS);
                tempStationObject = new StationObject(x, y, "S" + (i+1), (i+1));
                getActors().add(tempStationObject);
                stations.add(new Station((i+1), tempStationObject));
            }
    }

    @Override
    public void input(Input input, long deltaTime) {
        if(input.getKeyboardKey(KeyEvent.VK_ENTER) && !Logic.getInstance().isSimulationRunning())
            if(inputTimers.get(KeyEvent.VK_ENTER).checkTime()){
                Logic.getInstance().setSimulationRunning(true);
                Logic.getInstance().setStations(stations);
                Logic.getInstance().start();
            }

    }

    @Override
    public void logic(long deltaTime) {

    }

    @Override
    public void render(Renderer renderer) {
        for(GameObject S: getActors())
            S.render(renderer, camera);
    }

    @Override
    public void close() {

    }
}
