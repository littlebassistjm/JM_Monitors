package engine;

import jGame.JGame;
import jGame.JGameDriver;
import jGame.model.game.GameScene;
import jGame.model.game.GameSceneManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainWindow extends JGameDriver {

    protected JGame game;
    //private LeftPanel leftPanel;

    public MainWindow(String title, int width, int height, int fps){
        super(title,width,height,fps);
        //leftPanel = new LeftPanel(220, height);
        GameSceneManager tempSceneMan = new GameSceneManager();

        game = new JGame(title, width, height, fps, tempSceneMan);
    }

    public void initialScene(String sceneName){ game.initialGameScene(sceneName);}
    public void registerScene(GameScene scene){ game.registerGameScene(scene); }

    public void start(){
        JFrame window = new JFrame(game.getTitle());
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.setSize(new Dimension(game.getWidth(), game.getHeight()));
        JPanel container = new JPanel();
        FlowLayout layout = new FlowLayout();
        layout.setVgap(0);
        layout.setHgap(0);
        container.setLayout(layout);
        //container.add(leftPanel);
        container.add(game);

        window.add(container);

        window.pack();
        window.setVisible(true);

        // center
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - game.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - game.getHeight()) / 2);
        window.setLocation(x, y);

        window.add(container);
        window.pack();
        window.setVisible(true);
    }
}
