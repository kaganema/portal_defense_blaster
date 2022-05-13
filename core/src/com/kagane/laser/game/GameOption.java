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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/* Settings class to the game. Switch sound, change ammo, or quit.
Accessible from the main menu and pause menu. 
Author: Emir Atik */
public class GameOption extends InputAdapter implements Screen {
    public final static String TAG = GameOption.class.getName();
    GameRun gameRun;

    private static final float STAR_DENSITY = 0.03f;
    Background bg = new Background();

    ShapeRenderer shape;
    SpriteBatch batch;
    private FitViewport viewport;

    private final static Vector2 snb = new Vector2(480/2.6f, 480/1.44f);
    private final static Vector2 sb = new Vector2(480/3f, 480/2.5f);
    private final static Vector2 sr = new Vector2(480/2.2f, 480/2.5f);
    private final static Vector2 sl = new Vector2(480/1.6f, 480/2.5f);
    private final static Vector2 play = new Vector2(480/2.5f, 480/4.5f);
    private final static Vector2 quit = new Vector2(480/2.5f, 480/7f);
    private final static float width = 10f, height = 10f;

    private BitmapFont optFont;
    Texture lead, rocket, laser;
    private boolean soundswitch;
    int score;
    int life;
    String ammoType;
    int enScore;

    String soundTog;

    static final float SET_SCREEN_SIZE = 480f;

    public GameOption(GameRun game) {
        this.gameRun = game;
    }

    public GameOption(GameRun game, boolean sound) {
        this.gameRun = game;
        this.soundswitch = sound;
    }

    /*public GameOption(GameRun game, int score, int life) {
        this.gameRun = game;
        this.score = score;
        this.life = life;
    }*/

    /*public GameOption(GameRun game, String value, int score, int life, int attackers) {
        this.gameRun = game;
        this.ammoType = value;
        this.score = score;
        this.life = life;
        this.enScore = attackers;
    }*/

    /* Pass player score and current life as saved data from the game,
     * so that user can return to current gameplay unless they quit. */
    public GameOption(GameRun game, String value, int score, int life, int attackers, boolean sound) {
        this.gameRun = game;
        this.ammoType = value;
        this.score = score;
        this.life = life;
        this.enScore = attackers;
        this.soundswitch = sound;
    }

    public void setSoundswitch(boolean toggle) {
        this.soundswitch = toggle;
    }

    boolean isOn() {
        return soundswitch;
    }


    String switchOn(boolean t) {
        return t ? "On" : "Off";
    }

    String switchOn(boolean t, String s) {
        if (t) s = "On";
        else s = "Off";
        return s;
    }

    @Override
    public void show() {
        shape = new ShapeRenderer();
        batch = new SpriteBatch();

        viewport = new FitViewport(SET_SCREEN_SIZE, SET_SCREEN_SIZE);
        Gdx.input.setInputProcessor(this);

        bg.bgStart(STAR_DENSITY);

        lead = new Texture("bullet.png");
        rocket = new Texture("erpg-resize.png");
        laser = new Texture("lasr.png");

        optFont = new BitmapFont();
        optFont.getData().setScale(1.4f);
        optFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    @Override
    public void render(float v) {
        viewport.apply();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shape.setProjectionMatrix(viewport.getCamera().combined);
        bg.render(shape);
        shape.begin(ShapeType.Filled);
        shape.rect(snb.x, snb.y, width * 6f, height * 3f);
        shape.setColor(250, 250, 250, 0.6f);
        shape.rect(play.x, play.y, width * 10f, height * 3f);
        shape.rect(quit.x, quit.y, width * 10f, height * 3f);
        shape.end();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        optFont.setColor(Color.GOLDENROD);
        // The glyph assists the fonts to be aligned consistently across all screen sizes.
        final GlyphLayout glyph = new GlyphLayout(optFont, "options");
        optFont.draw(batch, "Options", viewport.getWorldWidth()/2f, viewport.getWorldHeight()- glyph.height - 2f, 0,
                Align.center, false);
        optFont.draw(batch, "Sound: "+switchOn(soundswitch, soundTog), viewport.getWorldWidth()/2f, viewport.getWorldHeight()/1.3f - glyph.height,
        0, Align.center, false);
        batch.draw(lead, sb.x, sb.y);
        batch.draw(rocket, sr.x, sr.y);
        batch.draw(laser, sl.x, sl.y);
        optFont.draw(batch, "Finish", viewport.getWorldWidth()/2f, (viewport.getWorldHeight() - glyph.height) /3.7f,
                0, Align.center, false);
        optFont.draw(batch, "Close", viewport.getWorldWidth()/2f, (viewport.getWorldHeight() - glyph.height) /5.4f,
                0, Align.center, false);
        batch.end();
    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h, true);
        bg.resize();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        //session.resume();
    }

    @Override
    public void hide() {
        batch.dispose();
        optFont.dispose();
        shape.dispose();
    }

    @Override
    public void dispose() {
//        batch.dispose();
//        optFont.dispose();
//        shape.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return super.keyDown(keycode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 touch = viewport.unproject(new Vector2(screenX, screenY));
        if (touch.dst(sb) < width * 2f) gameRun.setScreen(new ScreenFace(gameRun, "BULLET", score, life, enScore, isOn()));
        else if (touch.dst(sr) < width * 4.3f) gameRun.setScreen(new ScreenFace(gameRun, "ROCKET", score, life, enScore, isOn()));
        else if (touch.dst(sl) < width * 2.2f) gameRun.setScreen(new ScreenFace(gameRun, "LASER", score, life, enScore, isOn()));

        if (touch.dst(play) < width) gameRun.setScreen(new MainMenu(gameRun, score, isOn())); //gameRun.resumeGame(session);
        if (touch.dst(quit) < width * 10f) gameRun.setScreen(new MainMenu(gameRun, isOn()));

        // Enable sound
        if (touch.dst(snb) < width * 6.82f) {
            if (soundswitch) setSoundswitch(false);
            else setSoundswitch(true);
        }
        return true;
    }
}
