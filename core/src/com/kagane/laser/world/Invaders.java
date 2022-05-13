package com.kagane.laser.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
* A set of enemies to be spawned per round / level.
* Author: Emir Atik (kaganema)
* @version 1.0*/

public class Invaders {
    public static final String TAG = Invaders.class.getName();
    DelayedRemovalArray<Enemy> invaders;
    Viewport viewport;

    boolean soundSet;

    float spawnSec = 1.f;
    public int invaded;

    public Invaders(Viewport v) {
        this.viewport = v;
        invaded = 0;
        init();
    }

    /**
    * For resuming a paused game.
    * @param v for target viewport to display.
     * @param score saved from the last running session. */
    public Invaders(Viewport v, int score) {
        this.viewport = v;
        this.invaded = score;
        init();
    }

    public Invaders(Viewport v, boolean sound) {
        this.viewport = v;
        this.invaded = 0;
        this.soundSet = sound;
        init();
    }

    public Invaders(Viewport v, int score, boolean sound) {
        this.viewport = v;
        this.invaded = score;
        this.soundSet = sound;
        init();
    }

    public void init() {
        invaders = new DelayedRemovalArray<>(false, 10);
    }

    public void update(float delta) {
        //float HEIGHT = MathUtils.random() <= viewport.getWorldHeight()/2 ? 0 : viewport.getWorldHeight();
        //Vector2 updatePosition = new Vector2(MathUtils.random() * viewport.getWorldWidth(), HEIGHT);
        
        /*Tip: If y valus is >= height, place shape randomly on x-axis up to width, do the same for the inverse, make sure the
        * shapes are pointing towards the player.*/
        if (MathUtils.random() < delta * spawnSec) {
            Vector2 updatePosition = new Vector2(MathUtils.random() * viewport.getScreenWidth(), viewport.getScreenHeight()); //Start at top
            //Vector2 updatePosition = new Vector2(viewport.getWorldWidth(), MathUtils.random() * viewport.getWorldHeight()); //Start on right
            Enemy e = new Enemy(updatePosition, soundSet);
            invaders.add(e);
        }

        for (Enemy enemy: invaders) {
            enemy.update(delta);
        }

        invaders.begin();
        for (int i=0; i<invaders.size; i++){
            if ((invaders.get(i).pos.y < -1)){
                //invaded++;
                invaders.removeIndex(i);
            }
            /*if (((invaders.get(i).pos.x < -1) || (invaders.get(i).pos.x > viewport.getWorldWidth()+1)) ||
                    ((invaders.get(i).pos.y) > viewport.getWorldHeight() + 1)){
                invaders.removeIndex(i);
            }*/
        }
        invaders.end();
    }

    public void render(SpriteBatch batch) {
        for (Enemy e: invaders) {
            e.render(batch);
            e.afterEffect(batch);
        }
    }
}
