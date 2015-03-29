package fi.oma.peli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import fi.oma.framework.Game;
import fi.oma.framework.Graphics;
import fi.oma.framework.Image;
import fi.oma.framework.Screen;
import fi.oma.framework.Input.TouchEvent;
import fi.oma.peli.OptionsActivity;


public class GameScreen extends Screen {
	enum GameState {
		Ready, Running, Paused, GameOver, Options
	}

	GameState state = GameState.Ready;

	// Variable Setup

	private static Background bg1, bg2;
	private static Player Player;
	public static BasicEnemy hb, hb2;

	private Image currentSprite, character, character2, character3, enemy,
			enemy2, enemy3, enemy4, enemy5;
	private Animation anim, hanim;

	private ArrayList<Tile> tilearray = new ArrayList<Tile>();

	int livesLeft = 1;
	Paint paint, paint2;

	public GameScreen(Game game) {
		super(game);

		// Initialize game objects here

		bg1 = new Background(0, 0);
		bg2 = new Background(2160, 0);
		Player = new Player();
		hb = new BasicEnemy(340, 360);
		hb2 = new BasicEnemy(700, 360);

		character = Assets.character;
		character2 = Assets.character2;
		character3 = Assets.character3;

		enemy = Assets.Enemy;
		enemy2 = Assets.Enemy2;
		enemy3 = Assets.Enemy3;
		enemy4 = Assets.Enemy4;
		enemy5 = Assets.Enemy5;

		anim = new Animation();
		anim.addFrame(character, 1250);
		anim.addFrame(character2, 50);
		anim.addFrame(character3, 50);
		anim.addFrame(character2, 50);

		hanim = new Animation();
		hanim.addFrame(enemy, 100);
		hanim.addFrame(enemy2, 100);
		hanim.addFrame(enemy3, 100);
		hanim.addFrame(enemy4, 100);
		hanim.addFrame(enemy5, 100);
		hanim.addFrame(enemy4, 100);
		hanim.addFrame(enemy3, 100);
		hanim.addFrame(enemy2, 100);

		currentSprite = anim.getImage();

		loadMap();

		// Defining a Different fonts and their colors for menus
		paint = new Paint();
		paint.setTextSize(40);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.YELLOW);

		paint2 = new Paint();
		paint2.setTextSize(60);
		paint2.setTextAlign(Paint.Align.CENTER);
		paint2.setAntiAlias(true);
		paint2.setColor(Color.YELLOW);

	}

	private void loadMap() {
		ArrayList lines = new ArrayList();
		int width = 0;
		int height = 0;

		Scanner scanner = new Scanner(GameActivity.map);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			// no more lines to read
			if (line == null) {
				break;
			}

			if (!line.startsWith("!")) {
				lines.add(line);
				width = Math.max(width, line.length());

			}
		}
		height = lines.size();

		for (int j = 0; j < 12; j++) {
			String line = (String) lines.get(j);
			for (int i = 0; i < width; i++) {

				if (i < line.length()) {
					char ch = line.charAt(i);
					Tile t = new Tile(i, j, Character.getNumericValue(ch));
					tilearray.add(t);
				}

			}
		}

	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		//FIVE UPDATE METHODS
		// DIFFERENT STATES OF THE GAME CALL DIFFERENT METHODS (OPTIONS NOT USED)
		
		if (state == GameState.Ready)
			updateReady(touchEvents);
		if (state == GameState.Running)
			updateRunning(touchEvents, deltaTime);
		if (state == GameState.Paused)
			updatePaused(touchEvents);
		if (state == GameState.GameOver)
			updateGameOver(touchEvents);
		if (state == GameState.Options)
			updateGameOver(touchEvents);
	}

	private void updateReady(List<TouchEvent> touchEvents) {

		//STARTS FROM READY SCREEN AND WHEN USER TOUCHES THE SCREEN GAME BEGINS
		// STATE CHANGES TO GAMESTATE.RUNNING AND UPDATE METHOD WILL BE CALLED
		

		if (touchEvents.size() > 0)
			state = GameState.Running;
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {

		

		// ALL(MAYBE) TOUCH IMPUTS ARE HANDLED HERE
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {

				if (inBounds(event, 0, 285, 65, 65)) {
					Player.jump();
					currentSprite = anim.getImage();
					Player.setDucked(false);
				}

				else if (inBounds(event, 0, 350, 65, 65)) {

					if (Player.isDucked() == false && Player.isJumped() == false
							&& Player.isReadyToFire()) {
						Player.shoot();
					}
				}

				else if (inBounds(event, 0, 415, 65, 65)
						&& Player.isJumped() == false) {
					currentSprite = Assets.characterDown;
					Player.setDucked(true);
					Player.setSpeedX(0);

				}

				if (event.x > 400) {
					// MOVING RIGHT
					Player.moveRight();
					Player.setMovingRight(true);

				}

			}

			if (event.type == TouchEvent.TOUCH_UP) {

				if (inBounds(event, 0, 415, 65, 65)) {
					currentSprite = anim.getImage();
					Player.setDucked(false);

				}

				if (inBounds(event, 0, 0, 35, 35)) {
					pause();

				}

				if (event.x > 400) {
					// Move right.
					Player.stopRight();
				}
			}

		}

		// CHECK IF PLAYER IS DEAD

		if (livesLeft == 0) {
			state = GameState.GameOver;
		}

		// ALL GAME UPDATES HAPPEN HERE
		Player.update();
		if (Player.isJumped()) {
			currentSprite = Assets.characterJump;
		} else if (Player.isJumped() == false && Player.isDucked() == false) {
			currentSprite = anim.getImage();
		}

		ArrayList projectiles = Player.getProjectiles();
		for (int i = 0; i < projectiles.size(); i++) {
			Projectile p = (Projectile) projectiles.get(i);
			if (p.isVisible() == true) {
				p.update();
			} else {
				projectiles.remove(i);
			}
		}

		updateTiles();
		hb.update();
		hb2.update();
		bg1.update();
		bg2.update();
		animate();

		if (Player.getCenterY() > 500) {
			state = GameState.GameOver;
		}
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	private void updatePaused(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 0, 0, 800, 240)) {

					if (!inBounds(event, 0, 0, 35, 35)) {
						resume();
					}
				}

				if (inBounds(event, 0, 240, 800, 240)) {
					nullify();
					goToMenu();
				}
			}
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(event, 0, 0, 800, 480)) {
					nullify();
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}

	}

	private void updateTiles() {

		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			t.update();
		}

	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();

		g.drawImage(Assets.background, bg1.getBgX(), bg1.getBgY());
		g.drawImage(Assets.background, bg2.getBgX(), bg2.getBgY());
		paintTiles(g);

		ArrayList projectiles = Player.getProjectiles();
		for (int i = 0; i < projectiles.size(); i++) {
			Projectile p = (Projectile) projectiles.get(i);
			g.drawRect(p.getX(), p.getY(), 10, 5, Color.YELLOW);
		}
		// DRAWING GAME ELEMENTS FIRST

		g.drawImage(currentSprite, Player.getCenterX() - 61,
				Player.getCenterY() - 63);
		g.drawImage(hanim.getImage(), hb.getCenterX() - 48,
				hb.getCenterY() - 48);
		g.drawImage(hanim.getImage(), hb2.getCenterX() - 48,
				hb2.getCenterY() - 48);

		
		// g.drawImage(Assets.background, 0, 0);
		// g.drawImage(Assets.character, characterX, characterY);

		// SECONDLY DRAWING UI STUFF
		if (state == GameState.Ready)
			drawReadyUI();
		if (state == GameState.Running)
			drawRunningUI();
		if (state == GameState.Paused)
			drawPausedUI();
		if (state == GameState.GameOver)
			drawGameOverUI();

	}

	private void paintTiles(Graphics g) {
		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			if (t.type != 0) {
				g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY());
			}
		}
	}

	public void animate() {
		anim.update(10);
		hanim.update(50);
	}

	private void nullify() {

		// SETTING ALL VARIABLES TO NULL. WILL BE RECREATED IN THE CONSTRUCTOR
		
		paint = null;
		bg1 = null;
		bg2 = null;
		Player = null;
		hb = null;
		hb2 = null;
		currentSprite = null;
		character = null;
		character2 = null;
		character3 = null;
		enemy = null;
		enemy2 = null;
		enemy3 = null;
		enemy4 = null;
		enemy5 = null;
		anim = null;
		hanim = null;

		// CALLING CARBAGE COLLECTOR TO CLEAN THE MEMORY
		System.gc();

	}

	private void drawReadyUI() {
		Graphics g = game.getGraphics();

		g.drawARGB(155, 0, 0, 0);
		g.drawString("Tap to Start.", 400, 240, paint);

	}

	private void drawRunningUI() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.button, 0, 285, 0, 0, 65, 65);
		g.drawImage(Assets.button, 0, 350, 0, 65, 65, 65);
		g.drawImage(Assets.button, 0, 415, 0, 130, 65, 65);
		g.drawImage(Assets.button, 0, 0, 0, 195, 35, 35);

	}
        // FONTS ARE SET IN THE BEGINNING!
	private void drawPausedUI() {
		Graphics g = game.getGraphics();
		// MAKING BACKGROUND BLACK WHILE GAME IS PAUSED
		g.drawARGB(155, 0, 0, 0);
		g.drawString("Resume", 400, 165, paint2);
		g.drawString("Main menu", 400, 360, paint2);

	}

	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		g.drawRect(0, 0, 1281, 801, Color.BLACK);
		g.drawString("YOU DIED.", 400, 240, paint2);
		g.drawString("Tap to return.", 400, 290, paint);
		

	}

	@Override
	public void pause() {
		if (state == GameState.Running)
			state = GameState.Paused;

	}

	@Override
	public void resume() {
		if (state == GameState.Paused)
			state = GameState.Running;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {
		pause();
	}

	private void goToMenu() {
		
		game.setScreen(new MainMenuScreen(game));

	}

	public static Background getBg1() {
		
		return bg1;
	}

	public static Background getBg2() {
		
		return bg2;
	}

	public static Player getPelaaja() {
		
		return Player;
	}

	@Override
	public void options() {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public void options() {
		 Intent intent = new Intent(GameScreen.this,NewActivity.class);
         startActivity(intent);
		
	}

	private void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		
	}*/

}