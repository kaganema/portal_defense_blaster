package com.kagane.laser.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kagane.laser.world.*;

/* The gameplay screen that loads all the content from world.
*  */

public class ScreenFace extends InputAdapter implements Screen{
    public static final String TAG = ScreenFace.class.getName();

    private static final float STAR_DENSITY = 0.03f;

    GameRun g;
    ExtendViewport viewport;
    //ScreenViewport viewport;
    ShapeRenderer renderer;

    // test this
    Background bg = new Background();

    private Player player; //OrthographicCamera camera;

    String aType;

    //Add enemy slots to the game
    private Invaders attackers;

    //Screen viewport for HUD.
    ScreenViewport vis;

    SpriteBatch sBatch;

    BitmapFont font;
    private Sound over;

    private Vector2 pb = new Vector2(5, 480f - 10f);


    //Sound switch to pass to game elements
    boolean sTog;

    public ScreenFace(GameRun gb) {
        this.g = gb;
    }


    public ScreenFace(GameRun gb, int score, int life, boolean sound) {
        this.g = gb;
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sTog = sound;
        player = new Player(viewport, score, life, sound);
        attackers = new Invaders(viewport, sound);
    }

    public ScreenFace(GameRun gb, String ammo, int score, int life, int enScore, boolean sound) {
        this.g = gb;
        this.aType = ammo;
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sTog = sound;
        player = new Player(viewport, ammo, score, life, sound);
        attackers = new Invaders(viewport, enScore, sound);
    }

    @Override
    public void show() {
        //viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //viewport = new ScreenViewport();
        //camera = new OrthographicCamera();

        Gdx.input.setInputProcessor(this);

        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);

        vis = new ScreenViewport();

        sBatch = new SpriteBatch();

        //bg = new Background();
        bg.bgStart(STAR_DENSITY);

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        over = Gdx.audio.newSound(Gdx.files.internal("game-over-rev.mp3"));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.04f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bg.render(renderer);
        //camera.update();
        attackers.update(delta);
        player.update(delta);
        if (player.gotHit(attackers)) {
            // If player dies, it's game over.
            if (player.lifeCounter < 0) {
                g.setScreen(new EndScreen(g, "Game Over!", player));
            }
            // TODO: If player scores higher after time, player wins the game.
            // TODO: After time limit, if invaders score higher, invaders win.
        }
        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        //renderer.begin(ShapeType.Filled);
        //renderer.rect(2, viewport.getWorldHeight() - 25f, pb.x * 4.5f, pb.y * 5f);
        //renderer.end();

        vis.apply();

        sBatch.setProjectionMatrix(vis.getCamera().combined);
        sBatch.begin();
        player.render(sBatch);
        attackers.render(sBatch);
        font.setColor(Color.GOLD);
        font.draw(sBatch, "P", 5, vis.getWorldHeight() - 10f);
        font.draw(sBatch, "Lives: " + player.lifeCounter, vis.getWorldWidth() - 10.f, vis.getScreenHeight() - 5.f,
                0, Align.right, false);
        font.draw(sBatch, "Player: " + player.getScore() + " | Invaders: " + attackers.invaded, vis.getWorldWidth() / 2f, vis.getScreenHeight() - 8f,
                0, Align.center, false);
        sBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        //camera.setToOrtho(false, width, height);
        viewport.update(width, height, true);
        vis.update(width, height, true);
        //font.getData().setScale(Math.min(width, height) / 500.f);
        font.getData().setScale(Math.min(width, height) / 410.f);

        bg.bgStart(STAR_DENSITY);
        // scale the textures
        //sBatch.setProjectionMatrix(viewport.getCamera().combined);

        // set player back to middle
        player.init();
        // resize enemy ships
        attackers.init();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        //Go to pause menu
        g.setScreen(new GameOption(g));
        //g.setScreen(new GameOption(g, aType, player.getScore(), player.lifeCounter, attackers.invaded, sTog));
    }

    @Override
    public void hide() {
        renderer.dispose();
        //sBatch.dispose();
        //font.dispose();
        over.dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        sBatch.dispose();
        font.dispose();
        //img.dispose();
        over.dispose();
    }

    /*
     * Load pause menu when this button is hit. */
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 touch = viewport.unproject(new Vector2(screenX, screenY));
        // Touch on pause button.
        if (touch.dst(pb) < pb.x * 4.5) g.setScreen(new GameOption(g, aType, player.getScore(), player.lifeCounter, attackers.invaded, sTog));
        return true;
    }
}
