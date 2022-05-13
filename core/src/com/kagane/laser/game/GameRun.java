package com.kagane.laser.game;

import com.badlogic.gdx.Game;

public class GameRun extends Game {

	@Override
	public void create () {
		setScreen(new MainMenu(this));
	}

	/*public void startGame() {
		setScreen(new ScreenFace(this));
	}*/

	/*
	Start the game with defaults.
	*/
	public void startGame() {
		setScreen(new ScreenFace(this, 0, 5, false));
	}

	/*
	Start the game with changes from the options menu.
	*/
    public void startGame(boolean s) {
        setScreen(new ScreenFace(this, 0, 5, s));
    }

	/*public void gameConfig() {
	    setScreen(new GameOption(this));
    }*/

    public void gameConfig(boolean sound) {
        setScreen(new GameOption(this, sound));
    }

	/* Section to allow restarts after game is finished.
	 * Pass in the success or fail result depending on outcome. */

	/* Return to main menu, ideally from pause menu. */
    public void quitGame(int playerScore) {
        setScreen(new MainMenu(this, playerScore));
    }

}
