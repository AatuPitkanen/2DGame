package fi.oma.peli;

import fi.oma.framework.Game;
import fi.oma.framework.Graphics;
import fi.oma.framework.Screen;
import fi.oma.framework.Graphics.ImageFormat;

public class SplashLoadingScreen extends Screen {
	public SplashLoadingScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		Assets.splash= g.newImage("Splash.png", ImageFormat.RGB565);

		
		game.setScreen(new LoadingScreen(game));

	}

	@Override
	public void paint(float deltaTime) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {

	}

	@Override
	public void options() {
		// TODO Auto-generated method stub
		
	}
}