package com.kagane.laser.game;

import com.badlogic.gdx.Game;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameRun extends Game {
	//SpriteBatch batch;

	@Override
	public void create () {
		//batch = new SpriteBatch();
		//setScreen(new ScreenFace());
		setScreen(new MainMenu(this));
	}

	/*public void startGame() {
		setScreen(new ScreenFace(this));
	}
*/
	public void startGame() {
		setScreen(new ScreenFace(this, 0, 5, false));
	}

    public void startGame(boolean s) {
        setScreen(new ScreenFace(this, 0, 5, s));
    }

	public void gameConfig() {
	    setScreen(new GameOption(this));
    }

    public void gameConfig(boolean sound) {
        setScreen(new GameOption(this, sound));
    }

	/*Option menu accessed via main menu or game pause.*/
	public void pauseGame() {
	    setScreen(new GameOption(this));
    }

    /* Resume the game from the options menu */
    public void resumeGame(ScreenFace sc) {
        sc.show();
    }

    public void resume(GameOption sc) {
        sc.resume();
    }

	/* Section to allow restarts after game is finished.
	 * Pass in the success or fail result depending on outcome. */
	public void endGame() {}

	/* Return to main menu, ideally from pause menu. */
    public void quitGame(int playerScore) {
        setScreen(new MainMenu(this, playerScore));
    }

	/*@Override
	public void render () {
		batch.begin();
		batch.end();
	}*/
	
	/*@Override
	public void dispose () {
		batch.dispose();
	}*/
}
