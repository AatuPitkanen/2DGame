package fi.oma.framework;

public abstract class Screen {
   protected final Game game;
  

    public Screen(Game game) {
        this.game = game;
    }
/*public Options(Game game) {
	this.game = game;
}*/

    public abstract void options();
    
    public abstract void update(float deltaTime);

    public abstract void paint(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
    
	public abstract void backButton();
}
