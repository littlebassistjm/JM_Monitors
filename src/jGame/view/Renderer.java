package jGame.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import jGame.model.game.GameScreen;

public class Renderer {
	
	private GameScreen gameScreen;
	private BufferedImage bufferedImage;
	private Graphics2D rendIn;
	
	public Renderer(GameScreen gameScreen){
		setGameScreen(gameScreen);
		setBufferedImage(BufferedImage.TYPE_INT_ARGB);
		setGraphics2D();
	}

	public void clear() {
		rendIn.setColor(Color.WHITE);
		rendIn.fillRect(0, 0, gameScreen.getWidth(), gameScreen.getHeight());
	}
	
	public void render(JPanel parent){
		Graphics rendOut = parent.getGraphics();
		rendOut.drawImage(getBufferedImage(), 0, 0, null);
		rendOut.dispose();
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(int TYPE) {
		bufferedImage = null;
		bufferedImage = new BufferedImage(gameScreen.getWidth(), gameScreen.getHeight(), TYPE);
	}

	public Graphics2D getRendIn() {
		return rendIn;
	}

	public void setGraphics2D() {
		if(getBufferedImage() != null)
			rendIn = (Graphics2D) getBufferedImage().getGraphics();
		else
			System.err.println("Cannot initialize Graphics 2D!");
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}
	
}
