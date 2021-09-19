package com.kagane.laser.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.TimeUtils;

public class Ammunition {
    // Position and origin point of bullet.
    Vector2 pos;
    // Updated movement of position.
    Vector2 vel;
    Vector2 angle;
    float deg;
    String text;
    FireType ammo;

    // Boundary shape surrounding the point.
    Polygon bounds;
    //Texture img;
    Texture img = new Texture("bullet.png");
    // Possible move to offset around the circle.
    static final float offset = 2.f;

    private Sound trigger;
    Viewport viewport;

    /* Speed is one of the variables that is going to need the type of ammo that will travel. Using a "getType" from
    * the enum is suited for this. */
    static float speed = 5f;
    float spawnRate = 3f;
    static final Vector2 ACCELERATION = new Vector2(0, speed);
    //Vector2 direction;
   // static final Vector2 ACCELERATION = new Vector2(Gdx.input.getX() * speed, Gdx.input.getY() * speed);

    private float[] points = {0, 1, 0, img.getHeight()/2f, img.getWidth()/2f, img.getHeight(),
            img.getWidth(), img.getHeight()/2f, img.getWidth(), 1};

    public Ammunition(Viewport v) {
        this.viewport = v;
        //this.img = new Texture("bullet.png");
        this.bounds = new Polygon(points);
        this.bounds.setOrigin(img.getWidth()/2f, img.getHeight()/2f);
        init();
    }

    public Ammunition(float x, float y, float r) {
        //Set position on offset.
        pos = new Vector2(x, y);
        bounds = new Polygon(points);
        vel = new Vector2();
        //Position coordinates
        //pos.setAngle(r);
        r = MathUtils.degreesToRadians * r;
        //pos.x = MathUtils.cos(r) * speed;
        //pos.y = MathUtils.sin(r) * speed;
        //pos.x = MathUtils.cos(r) * speed;
        //pos.y = MathUtils.sin(r) * speed;
        vel.x = MathUtils.cos(r) * speed;
        //MathUtils.roundPositive()
        vel.y = MathUtils.sin(r) * speed;

        //this.bounds.setOrigin(x, y);
        //if (r < 0) r = 360 - -(r);
        // Rotate to the orientation of the user's input.
        //pos.setAngle(vel.angle());
        //this.bounds.setRotation(pos.angle());
        this.bounds.setRotation(vel.angle() - 90f);
        // Set the position to the player's location.
        //this.bounds.setPosition(x, y);
        //this.bounds.setPosition(x+offset, y+offset);
        this.bounds.setPosition(pos.x, pos.y);
        //this.bounds.setPosition(pos.x+offset, pos.y+offset);
        //v.unproject(new Vector2(v.getWorldWidth() - Gdx.input.getX(), v.getWorldHeight() - Gdx.input.getY()));
    }

    public Ammunition(float x, float y, float r, String type) {
        //Set position on offset.
        this.text = type;
        pos = new Vector2(x, y);
        vel = new Vector2();

        // Find and get ammo type
        if (type != null) ammo = FireType.valueOf(type);
        else ammo = FireType.BULLET;

        bounds = new Polygon(new float[] {0, 0, 0, ammo.img.getHeight(), ammo.img.getWidth(), 0});
        //Position coordinates
        //pos.setAngle(r);
        r = MathUtils.degreesToRadians * r;
        vel.x = MathUtils.cos(r) * ammo.fireRate;
        //MathUtils.roundPositive()
        vel.y = MathUtils.sin(r) * ammo.fireRate;

        //this.bounds.setRotation(pos.angle());
        this.bounds.setRotation(vel.angle() - 90f);
        // Set the position to the player's location.
        this.bounds.setPosition(pos.x, pos.y);
        ammo.trigger.setLooping(0, false);
        ammo.trigger.play();
    }

    public Ammunition(float x, float y, float r, String type, boolean sound) {
        //Set position on offset.
        this.text = type;
        pos = new Vector2(x, y);
        vel = new Vector2();

        // Find and get ammo type
        if (type != null) ammo = FireType.valueOf(type);
        else ammo = FireType.BULLET;

        bounds = new Polygon(new float[] {0, 0, 0, ammo.img.getHeight(), ammo.img.getWidth(), 0});
        //Position to fire from coordinates
        //pos.setAngle(r);
        r = MathUtils.degreesToRadians * r;
        vel.x = MathUtils.cos(r) * ammo.fireRate;
        //MathUtils.roundPositive()
        vel.y = MathUtils.sin(r) * ammo.fireRate;

        //this.bounds.setRotation(pos.angle());
        this.bounds.setRotation(vel.angle() - 90f);
        // Set the position to the player's location.
        this.bounds.setPosition(pos.x, pos.y);
        ammo.trigger.setLooping(0, false);
        if (sound)
        ammo.trigger.play();
        else ammo.trigger.play(0.0f);
    }

    public Ammunition(Vector2 start, Vector2 pos) {
        this.pos = start;
        //init(dir, rotation);
        viewport.unproject(start);
    }

    public Ammunition(Vector2 start, Vector2 dir, int rotation) {
        this.bounds.setRotation(rotation);
        //this.pos = start;
        this.pos = start;
        moveToOffset(this.pos);
    }

    public Ammunition(float x, float y) {
        pos = new Vector2(x, y);
        bounds = new Polygon(points);
        //this.bounds.setOrigin(x, y);
        this.bounds.setPosition(x, y);
        //this.bounds.setRotation(r);
    }

    //Suggestion A
    public Ammunition(float rot) {
        pos = new Vector2();
        angle.rotate(rot);
    }

    public Ammunition(float x, float y, float angle, Vector2 dest) {
        this.angle.rotate(angle);
        this.pos = new Vector2(x, y);
        this.angle.rotateAround(pos, angle);
        init(this.pos, dest);
    }

    /*Get start point from ship location. */
    public Ammunition(Vector2 vec) {
        pos = vec;
    }

    void moveToOffset(Vector2 org) {
        //Option 1
        org.x += offset;
        org.y += offset;
        //Option 2
        //org.set(org.x, org.y);
        org.set(org.x * org.x, org.y * org.y);
        //Option 3
        org.x *= org.x;
        org.y *= org.y;
    }

    public void init() {
        //ACCELERATION.rotate(90f);
        //bounds.dirty();
        bounds.setRotation(90f);
    }

    /*
    * Fire from position of the player.
    * @param org position to spawn bullet.
     * @param dir of where the player clicked in space (but can pass that)*/
    public void init(Vector2 org, Vector2 dir) {
        org.angleRad(dir);
        bounds.getRotation();
        //Use setRotation instead of rotate
        // Dir: getScreenX/Y
        float posx = org.x - dir.x;
        float posy = org.y - dir.y;
        pos.set(posx, posy);
        //bounds.setPosition(pos.x, pos.y);
        bounds.setPosition(posx, posy);
    }

    public void init(float r) {
        this.bounds.setRotation(r);
    }

    public void update(float delta) {
        //Method 1
//        pos.x *= speed * delta;
//        pos.y *= speed * delta;
        //pos.x += (delta * vel.x) * speed;
        //pos.y += (delta * vel.y) * speed;
        //vel.mulAdd(ACCELERATION, delta);
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
        //v.unproject(new Vector2(v.getScreenWidth() - Gdx.input.getX(), v.getScreenHeight() - Gdx.input.getY()));

        // Vel in negative direction causes the bullets go upwards before getting deleted.
        bounds.translate(pos.x * vel.x, pos.y * vel.y);
        //bounds.translate(dx, dy);
    }

    public void fireBullet(float x, float y) {
        float mouseAngle = 0.0f;
        float offset = 2f;
        Vector2 start = new Vector2(), dest = new Vector2();
        //Gdx.input.getRotation();
        float angleRadian = MathUtils.degreesToRadians * mouseAngle;
        pos.x = MathUtils.cos(angleRadian);
        pos.y = MathUtils.sin(angleRadian);
        pos.x = start.x + (dest.x * offset);
        pos.y = start.y + (dest.y * offset);
        angle.angleRad(new Vector2(x, y));
        bounds.getRotation();
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
    public void render(ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.circle(bounds.getOriginX(), bounds.getOriginY(), 1f);
        renderer.polygon(bounds.getTransformedVertices());
    }

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
