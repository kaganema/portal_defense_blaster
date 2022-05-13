package com.kagane.laser.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Class to represent a single instance of ammunition. The instance is configurable via the pause menu.
 * Between lead bullet, rockets, or lasers.
 * @author Emir Atik (kaganema)
 * @version 1.0
 * @since 1.0 2021-09-10 */

public class Ammunition {
    // Position and origin point of bullet.
    Vector2 pos;
    // Updated movement of position.
    private Vector2 vel;
    Vector2 angle;
    float deg;
    private String text;
    private FireType ammo;

    // Boundary shape surrounding the point.
    Polygon bounds;
    // Possible move to offset around the circle.
    //static final float offset = 2.f;
    //private float spawnDifference = 3f;
   // static final Vector2 ACCELERATION = new Vector2(Gdx.input.getX() * ammo.fireRate, Gdx.input.getY() * ammo.fireRate);


    public Ammunition(float x, float y, float r) {
        ammo = FireType.BULLET;
        //Set position on offset.
        pos = new Vector2(x, y);
        bounds = new Polygon(new float[] {0, 0, 0, ammo.img.getHeight(), ammo.img.getWidth(), 0});
        vel = new Vector2();
        //Position coordinates
        //pos.setAngle(r);
        r = MathUtils.degreesToRadians * r;
        vel.x = MathUtils.cos(r) * ammo.fireRate;
        //MathUtils.roundPositive()
        vel.y = MathUtils.sin(r) * ammo.fireRate;

        //this.bounds.setOrigin(x, y);
        //if (r < 0) r = 360 - -(r);
        // Rotate to the orientation of the user's input.
        //pos.setAngle(vel.angle());
        //this.bounds.setRotation(pos.angle());
        this.bounds.setRotation(vel.angle() - 90f);
        // Set the position to the player's location.
        //this.bounds.setPosition(x, y);
        this.bounds.setPosition(pos.x, pos.y);
        //v.unproject(new Vector2(v.getWorldWidth() - Gdx.input.getX(), v.getWorldHeight() - Gdx.input.getY()));
    }

    /**
     * Starts a new instance of a weapon to be fired.
     * @param x position
     * @param y position
     * @param r start orientation of fire.
     * @param type Argument to determine type of weapon, if not specified it's BULLET by default.
     * @param sound toggle */
    public Ammunition(float x, float y, float r, String type, boolean sound) {
        this.text = type;
        //Set position on offset.
        //Position to fire from coordinates
        pos = new Vector2(x, y);
        vel = new Vector2();

        // Find and get ammo type based on selection
        if (type != null) ammo = FireType.valueOf(type);
        else ammo = FireType.BULLET;

        // Set collision bounds based on image shape.
        bounds = new Polygon(new float[] {0, 0, 0, ammo.img.getHeight(), ammo.img.getWidth(), 0});
        // Set angle to fire from.
        r = MathUtils.degreesToRadians * r;
        vel.x = MathUtils.cos(r) * ammo.fireRate;
        //MathUtils.roundPositive()
        vel.y = MathUtils.sin(r) * ammo.fireRate;
        this.bounds.setRotation(vel.angle() - 90f);
        // Set the position to the player's location.
        this.bounds.setPosition(pos.x, pos.y);
        ammo.trigger.setLooping(0, false);
        if (sound)
        ammo.trigger.play();
        else ammo.trigger.play(0.0f);
    }

    public void update(float delta) {
        //Method 1
        vel.mulAdd(new Vector2(0, ammo.fireRate), delta);
        pos.mulAdd(vel, delta);
        pos.clamp(0, 15);

        //Method 2
        //float dx = (delta * vel.x) * speed;
        //float dy = (delta * vel.y) * speed;
        //float xdir = pos.x + dx;
        //float ydir = pos.y + dy;
        //pos.set(xdir, ydir);
        //distMoved += Vector2.dst(pos.x, pos.y, dx2, dy2); //use if delay removal doesn't work
        //pos.x += (vel.x * delta) * speed;
        //pos.y += (vel.y * delta) * speed;
//        pos.x += dx * delta;
//        pos.y += dy * delta;
//        vel.x += dx;
//        vel.y += dy;
        //viewport.unproject(new Vector2(Gdx.input.getDeltaX(), Gdx.input.getDeltaY()));
        //vel.sub(ACCELERATION).nor();

        // Vel in negative direction causes the bullets go upwards before getting deleted.
        bounds.translate(pos.x * vel.x, pos.y * vel.y);
    }

    /* For test purposes only:
     * Get the origins points of the bounds for debug print. */
    public float getX() {
        return bounds.getOriginX();
    }

    public float getY() {
        return bounds.getOriginY();
    }

    // For debug and visualisation
    /*public void render(ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.polygon(bounds.getTransformedVertices());
    }*/

    // Draw the images
    public void render(SpriteBatch batch) {
        batch.draw(ammo.img, bounds.getX(), bounds.getY(), bounds.getOriginX(), bounds.getOriginY(), ammo.img.getWidth(),
                ammo.img.getHeight(), 0.4f, 0.4f, bounds.getRotation(), 0, 0, ammo.img.getWidth(), ammo.img.getHeight(),
                false, false);
    }
}

/*
* The enum will require the following things: the name, the texture, and the rate of fire, and the sound source. */

enum FireType {
    BULLET (new Texture("bullet.png"), 5f, Gdx.audio.newSound(Gdx.files.internal("game-bullet.mp3"))),
    ROCKET(new Texture("erpg-resize.png"), 2f, Gdx.audio.newSound(Gdx.files.internal("reverbswoosh.mp3"))),
    LASER (new Texture("lasr.png"), 10f, Gdx.audio.newSound(Gdx.files.internal("lasers.mp3")));
    float fireRate;
    Texture img;
    Sound trigger;
    FireType(Texture src, float speed, Sound blast) {
        this.img = src;
        this.fireRate = speed;
        this.trigger = blast;
    }
}
