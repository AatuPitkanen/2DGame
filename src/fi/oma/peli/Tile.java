package fi.oma.peli;

import android.graphics.Rect;
import fi.oma.framework.Image;

public class Tile {

	private int tileX, tileY, speedX;
	public int type;
	public Image tileImage;

	private Player Pelaaja = GameScreen.getPelaaja();
	private Background bg = GameScreen.getBg1();

	private Rect r;

	public Tile(int x, int y, int typeInt) {
		tileX = x * 40;
		tileY = y * 40;

		type = typeInt;

		r = new Rect();

		if (type == 5) {
			tileImage = Assets.tiledirt;
		} else if (type == 8) {
			tileImage = Assets.tilegrassTop;
		} else if (type == 4) {
			tileImage = Assets.tilegrassLeft;

		} else if (type == 6) {
			tileImage = Assets.tilegrassRight;

		} else if (type == 2) {
			tileImage = Assets.tilegrassBot;
		} else {
			type = 0;
		}

	}

		public void update() {
			speedX = bg.getSpeedX() * 5;
			tileX += speedX;
			r.set(tileX, tileY, tileX+40, tileY+40);
	
			
			
			if (Rect.intersects(r, Player.yellowRed) && type != 0) {
				checkVerticalCollision(Player.rect, Player.rect2);
				checkSideCollision(Player.rect3, Player.rect4, Player.footleft, Player.footright);
			}
	
		}

	public int getTileX() {
		return tileX;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

	public Image getTileImage() {
		return tileImage;
	}

	public void setTileImage(Image tileImage) {
		this.tileImage = tileImage;
	}

	public void checkVerticalCollision(Rect rtop, Rect rbot) {
		if (Rect.intersects(rtop, r)) {
			
		}

		if (Rect.intersects(rbot, r) && type == 8) {
			Pelaaja.setJumped(false);
			Pelaaja.setSpeedY(0);
			Pelaaja.setCenterY(tileY - 63);
		}
	}

	public void checkSideCollision(Rect rleft, Rect rright, Rect leftfoot, Rect rightfoot) {
		if (type != 5 && type != 2 && type != 0){
			if (Rect.intersects(rleft, r)) {
				Pelaaja.setCenterX(tileX + 102);
	
				Pelaaja.setSpeedX(0);
	
			}else if (Rect.intersects(leftfoot, r)) {
				
				Pelaaja.setCenterX(tileX + 85);
				Pelaaja.setSpeedX(0);
			}
			
			if (Rect.intersects(rright, r)) {
				Pelaaja.setCenterX(tileX - 62);
	
				Pelaaja.setSpeedX(0);
			}
			
			else if (Rect.intersects(rightfoot, r)) {
				Pelaaja.setCenterX(tileX - 45);
				Pelaaja.setSpeedX(0);
			}
		}
	}

}