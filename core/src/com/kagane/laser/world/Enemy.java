package com.kagane.laser.world;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.emitters.Emitter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.Gdx;

public class Enemy {
    public static final String TAG = Enemy.class.getName();

    private static final float ACCEL_VALUE = 0.5f;
    private static final Vector2 ACCELERATION = new Vector2(0.f, -ACCEL_VALUE); //Y Scroll
    //static final Vector2 ACCELERATION = new Vector2(-ACCEL_VALUE, 0.f); //X Scroll

    Texture img;
    private Sound zoom;

    Vector2 pos;
    Vector2 vel;

    // Collision body of the ship.
    Polygon base;

    // Check if ship got hit by bullet. Use to add Player score.
    boolean destroyed;

    ParticleEffect explosion;
    ParticleEffectPool effects;
    Explosion ex;

    public Enemy(Vector2 pos) {
        this.pos = pos;
        this.vel = new Vector2();
        //The coordinates will be the same as the ones to the shape drawing. {x1,y1,x2,y2,x3,y3}
        //this.base.setVertices(new float [] {pos.x + 5, pos.y + 3, (pos.x - 5 / 3) + 5, pos.y + 5, (pos.x + 5 / 3) + 5, pos.y + 5});

        //this.img = new Texture("enemy-rotated.png");
        this.img = new Texture("enemy-resized.png");
        this.zoom = Gdx.audio.newSound(Gdx.files.internal("enemy-ship.mp3"));

        this.base = new Polygon(new float[] {0, img.getHeight(), img.getWidth(), img.getHeight(), img.getWidth()/2, 0});
        //this.pos.rotate(MathUtils.random(50.f) * 100);
        this.base.setPosition(pos.x, pos.y);
        this.destroyed = false;

        zoom.setLooping(0, false);
        zoom.play();

        ex = new Explosion();
        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("blast.p"), Gdx.files.internal(""));
        //explosion.setEmittersCleanUpBlendFunction(false);
        //effects = new ParticleEffectPool(explosion, 1, 3);
    }

    public Enemy(Vector2 pos, boolean sound) {
        this.pos = pos;
        this.vel = new Vector2();

        this.img = new Texture("enemy-resized.png");
        this.zoom = Gdx.audio.newSound(Gdx.files.internal("enemy-ship.mp3"));
        this.base = new Polygon(new float[] {0, img.getHeight(), img.getWidth(), img.getHeight(), img.getWidth()/2, 0});
        this.base.setPosition(pos.x, pos.y);
        this.destroyed = false;

        zoom.setLooping(0, false);
        if (sound)
        zoom.play();
        else zoom.play(0.0f);

        ex = new Explosion();
        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("blast.p"), Gdx.files.internal(""));
    }

    public void update(float delta) {
        //TODO: Adjust parameters of acceleration toward player.
        vel.mulAdd(ACCELERATION, delta);

        pos.mulAdd(vel, delta);
        base.translate(vel.x, vel.y);
        explosion.setPosition(base.getX() + img.getWidth()/2,  base.getY());
        explosion.update(delta);
    }

    /*
    * Position placers to find starting point. */
    public void setX(float x) {
        this.pos.x = x;
    }

    public void setX() {
        if (this.pos.x == 0) {
            MathUtils.cos(90);
        }
    }

    public void setY(float y) {
        this.pos.y = y;
    }

    /*public void setRot(Vector2 v) {
        if (v.x <= 0){
            v.rotate(90);
        }else if (v.x >= 100){
            v.rotate90(1);
        }
        if (v.y <= 0){
            v.rotate(180);
        }else {
            v.rotate(0);
        }
    }*/

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }


    public void render(ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //renderer.triangle(pos.x + 5, pos.y + 3, (pos.x - 5 / 3) + 5, pos.y + 5, (pos.x + 5 / 3) + 5, pos.y + 5);
        //renderer.line(pos.x + 1, pos.y + 1, (pos.x - 5/2) + 1, (pos.y + 5) - 1);
        //renderer.polygon(coords);
        //renderer.polygon(new float[] {pos.x + 5.f, pos.y + 3.f, (pos.x - 5.f / 3.f) + 5.f, pos.y + 5.f, (pos.x + 5.f / 3.f) + 5, pos.y + 5.f});
    }

    public void render(SpriteBatch spriteBatch) {
        /* When the enemy ship gets destroyed, draw up the particles as the after effect of the explosion.
        * They may still damage the player (fire is dangerous!), but it should not give more points to the invaders. */
        if (destroyed) {
            explosion.start();
            explosion.draw(spriteBatch);
            if (explosion.isComplete()) explosion.dispose();
        } else
        spriteBatch.draw(img, base.getX(), base.getY());
    }

    public void afterEffect(SpriteBatch s) {
        //if (isDestroyed())
        //explosion.draw(s);
        //explosion.isComplete();
    }

    public void dispose() {
        img.dispose();
    }
}