package engine;

import engine.scenes.GUIScene;
import jGame.JGameDriver;

/**
 * JM_Monitors created by jmkhilario on 02/08/2017.
 */
public class CalTrain2 extends MainWindow {
    public CalTrain2(String title, int width, int height, int fps) {
        super(title, width, height, fps);
        registerScene(new GUIScene("CalTrain2"));
        initialScene("CalTrain2");
    }
}
