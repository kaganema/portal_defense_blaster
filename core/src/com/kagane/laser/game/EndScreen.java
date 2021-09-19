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

import com.kagane.laser.world.Player;

/* Results screen when the game terminates, regardless of winner. Options to retry or return to main. */

public class EndScreen extends InputAdapter implements Screen {
    GameRun go;

    private static final float STAR_DENSITY = 0.03f;
    Background bg = new Background();

    private ShapeRenderer renderer;
    private SpriteBatch batch;
    private FitViewport viewport;
    private BitmapFont scoreFont;

    static final float SET_SCREEN_SIZE = 480f;
    static final float buttonSize = 75.f;

    private static final Vector2 retry = new Vector2(480/4f, 480/3f);
    private static final Vector2 end = new Vector2(480/2f, 480/3f);

    int score;
    private String result;
    //The highest score achieved in a session. (Future update?)
    float higest;

    /* Get results data from the game. */
    // Pass player and get the score from the game, or pass score as argument in game screen.
    public EndScreen(GameRun g, String flag, Player p) {
        this.go = g;
        this.result = flag;
        this.score = p.getScore();
        //this.higest = Math.max(score, higest);
        if (score > higest) higest = score;
    }

    @Override
    public void show() {
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();

        //viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(SET_SCREEN_SIZE, SET_SCREEN_SIZE);
        bg.bgStart(STAR_DENSITY);

        Gdx.input.setInputProcessor(this);

        scoreFont = new BitmapFont();
        scoreFont.getData().setScale(1.5f);
        scoreFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    @Override
    public void render(float v) {
        viewport.apply();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setProjectionMatrix(viewport.getCamera().combined);
        bg.render(renderer);
        renderer.begin(ShapeType.Filled);
        renderer.setColor(250, 250, 250, 0.4f);
        renderer.rect(retry.x, retry.y, buttonSize, buttonSize / 2f);
        renderer.rect(end.x, end.y, buttonSize, buttonSize / 2f);
        renderer.end();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        scoreFont.setColor(Color.GOLDENROD);
        final GlyphLayout glyp = new GlyphLayout(scoreFont, "End");
        scoreFont.draw(batch, result + "\nYour Score: " + score, viewport.getWorldWidth()/2,
                viewport.getWorldHeight() - glyp.height - 10, 0, Align.center, false);
        //scoreFont.draw(batch, result + "\nYour Score: " + score + "\nYour Highest: " + higest, viewport.getWorldWidth()/2,
        //        viewport.getWorldHeight() - glyp.height - 10, 0, Align.center, false);
        scoreFont.draw(batch, "Retry", retry.x + (glyp.width/2), retry.y + (glyp.height));
        scoreFont.draw(batch, "Quit", end.x + (glyp.width/2), end.y + (glyp.height));
        batch.end();
    }

    @Override
    public void resize(int i, int i1) {
        viewport.update(i, i1, true);
        bg.bgStart(STAR_DENSITY);
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
        scoreFont.dispose();
        renderer.dispose();
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 touch = viewport.unproject(new Vector2(screenX, screenY));
        if (touch.dst(retry) < buttonSize) go.startGame();
        //if (touch.dst(end) > buttonSize) go.setScreen(new MainMenu(go, score));
        if (touch.dst(end) < buttonSize) go.quitGame(score);
        return true;
    }
}
