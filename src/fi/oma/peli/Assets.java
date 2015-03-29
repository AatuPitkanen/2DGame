package fi.oma.peli;

import fi.oma.framework.Image;
import fi.oma.framework.Music;
import fi.oma.framework.Sound;

public class Assets {
	
	public static Image menu, splash, background, character, character2, character3, Enemy, Enemy2, Enemy3, Enemy4, Enemy5;
	public static Image tiledirt, tilegrassTop, tilegrassBot, tilegrassLeft, tilegrassRight, characterJump, characterDown;
	public static Image button;
	public static Sound click;
	public static Music theme;
	
	public static void load(GameActivity sampleGame) {
		// TODO Auto-generated method stub
		theme = sampleGame.getAudio().createMusic("menutheme.mp3");
		theme.setLooping(true);
		theme.setVolume(0.85f);
		theme.play();
	}
	
}
