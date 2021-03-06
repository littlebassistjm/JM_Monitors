package jGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jGame.model.game.GameScene;
import jGame.model.game.GameSceneManager;

public abstract class JGameDriver {

	protected JGame game;
	
	public JGameDriver(String title, int width, int height, int fps){
		GameSceneManager tempSceneMan = new GameSceneManager();
		
		game = new JGame(title, width, height, fps, tempSceneMan);
	}
	
	public JGameDriver(String title, int width, int height, int fps, ArrayList<GameScene> scenes){
		GameSceneManager tempSceneMan = new GameSceneManager();
		// add scenes
		for(GameScene s: scenes)
		tempSceneMan.addScene(s);
		
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
        container.add(game);
        
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
