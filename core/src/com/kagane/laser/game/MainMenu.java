package com.kagane.laser.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/*
* Main menu class has the following:
* Buttons to play the game and set sound.
* A scoreboard displaying scores of the last game and best score of session. (if save feature is implemented,
* then also a best score of all time for user.)
* Author: Emir Atik (kaganema) */


public class MainMenu extends InputAdapter implements Screen{
    public static final String TAG = MainMenu.class.getName();
    GameRun game;

    ShapeRenderer renderer;
    SpriteBatch batch;
    FitViewport viewport;

    private static final float STAR_DENSITY = 0.03f;

    Background bg = new Background();

    private BitmapFont menuFont;
    private static final float buttonSize = 75.f;
    static final float titleFont = 90.f;
    static final Vector2 stb = new Vector2(480/3f, 480/2f);
    static final Vector2 optb = new Vector2(480/3f, 480/3f);
    static final float SET_SCREEN_SIZE = 480f;
    int currentScore;
    int highScore;
    Vector2 soundb;
    boolean stog;

    MainMenu(GameRun game) {
        this.game = game;
    }

    /* Back to main menu */
    MainMenu(GameRun game, int sc) {
        this.game = game;
        this.currentScore = sc;
    }

    MainMenu(GameRun game, boolean sound) {
        this.game = game;
        this.stog = sound;
    }

    MainMenu(GameRun game, int sc, boolean sound) {
        this.game = game;
        this.currentScore = sc;
        this.stog = sound;
    }

    public void show() {
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();

        bg.bgStart(STAR_DENSITY);
        viewport = new FitViewport(SET_SCREEN_SIZE, SET_SCREEN_SIZE);
        Gdx.input.setInputProcessor(this);

        menuFont = new BitmapFont();
        menuFont.getData().setScale(1.5f);
        menuFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    @Override
    public void render(float v) {
        viewport.apply();
        // OPTIONAL: Gdx clear colour background
        // Clear the buffer for each frame (stops the flickering).
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        bg.render(renderer);

        // Draw the buttons.
        renderer.begin(ShapeType.Filled);
        // TODO: Button designs
        // Play
        renderer.setColor(100, 100, 100, 0.4f);
        renderer.rect(stb.x, stb.y, buttonSize * 2f, buttonSize / 2f);
        // Options
        renderer.rect(optb.x, optb.y, buttonSize * 2f, buttonSize / 2f);
        renderer.end();

        // Write out the text.
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        menuFont.setColor(Color.GOLDENROD);
        // TODO: Game data and options
        final GlyphLayout title = new GlyphLayout(menuFont, "SuperBlaster");
        menuFont.draw(batch, "SuperBlaster!\nTechnically not tower-defense!", 10,
                viewport.getWorldHeight()-title.height,
                0, Align.topLeft, false);
        // Score
        //final GlyphLayout scorestring = new GlyphLayout(menuFont, "Score");
        menuFont.draw(batch, "Score: " + currentScore,
                viewport.getWorldWidth()/2, viewport.getWorldHeight()-100, 0,
                Align.center, false);
        // TODO: Highest score
        // Start button
        final GlyphLayout opt1 = new GlyphLayout(menuFont, "Start");
        menuFont.draw(batch, "Start", stb.x + (opt1.width/2), stb.y + (opt1.height+2));
        // Options
        menuFont.draw(batch, "Options", optb.x + (opt1.width/2), optb.y + (opt1.height));
        batch.end();
    }

    public void resize(int width, int height) {
        bg.bgStart(STAR_DENSITY);
        //menuFont.getData().setScale(Math.min(width, height) / SET_SCREEN_SIZE);
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        batch.dispose();
        menuFont.dispose();
        renderer.dispose();
    }

    @Override
    public void dispose() {
        //batch.dispose();
        //menuFont.dispose();
        //renderer.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 scrTch = viewport.unproject(new Vector2(screenX, screenY));
        //if (scrTch.dst(stb) < buttonSize) game.startGame();
        if (scrTch.dst(stb) < buttonSize) game.startGame(stog);
        //if (scrTch.dst(optb) < buttonSize) game.gameConfig();
        if (scrTch.dst(optb) < buttonSize) game.gameConfig(stog);
        //return super.touchDown(screenX, screenY, pointer, button);
        return true;
    }
}
