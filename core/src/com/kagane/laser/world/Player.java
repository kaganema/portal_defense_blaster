package com.kagane.laser.world;

/**
 * The player class with properties of controls, collisions, rendering, defense point, and scores.
 * The player can fire bullets.
 * @author Emir Atik (kaganema)
 * @version 1.0
 * @since 1.0 2021-09-10 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.audio.Sound;
//import com.badlogic.gdx.InputProcessor;
//import com.badlogic.gdx.input.GestureDetector;

public class Player {
    public static final String TAG = Player.class.getName();
    private static final Color PLAYER_COLOUR = Color.WHITE; //Subject to change
    private static float Speed = 150.f;
    private static final float MAX_SPEED = 500.f;
    private static final float radius = 0.7f;
    long fireTime = 10000;
    private float tick;
    long lastTime;

    Viewport viewport;
    private Vector2 pos;
    private Circle bounds;
    SpriteBatch batch;
    private Texture img;
    Texture defense;
    Rectangle zone;
    private DelayedRemovalArray<Ammunition> ammo;
    private Sprite actor;
    //GestureDetector gest;
    public int lifeCounter;
    String ammunition;
    float offset;

    private int score;
    //Rotate any sprite (Stackoverflow.com)
    //private float count =360.0f;
    boolean fire;
    boolean ticked;

    // Explosion data
    private Sound click;
    boolean soundToggle;

    public Player(Viewport viewport) {
        this.viewport = viewport;
        lifeCounter = 1; //Subject to change with having a value change per level or difficulty
        score = 0;
        init();
    }

    public Player(SpriteBatch b, Viewport viewport) {
        this.batch = b;
        this.viewport = viewport;
        lifeCounter = 5; //Subject to change with having a value change per level or difficulty
        init();
    }

    public Player(SpriteBatch b, Viewport viewport, boolean sound) {
        this.batch = b;
        this.viewport = viewport;
        lifeCounter = 5; //Subject to change with having a value change per level or difficulty
        this.soundToggle = sound;
        init();
    }

    public Player(Viewport viewport, String type) {
        this.ammunition = type;
        this.viewport = viewport;
        score = 0;
        lifeCounter = 5; //Subject to change with having a value change per level or difficulty
        init();
    }

    public Player(Viewport viewport, int currentScore, int life) {
        this.viewport = viewport;
        this.score = currentScore;
        this.lifeCounter = life; //Subject to change with having a value change per level or difficulty
        init();
    }

    public Player(Viewport viewport, String type, int currentScore, int life) {
        ammunition = type;
        this.viewport = viewport;
        this.score = currentScore;
        this.lifeCounter = life; //Subject to change with having a value change per level or difficulty
        init();
    }

    public Player(Viewport viewport, int currentScore, int life, boolean sound) {
        this.viewport = viewport;
        this.score = currentScore;
        this.lifeCounter = life; //Subject to change with having a value change per level or difficulty
        this.soundToggle = sound;
        init();
    }

    public Player(Viewport viewport, String type, int currentScore, int life, boolean sound) {
        ammunition = type;
        this.viewport = viewport;
        this.score = currentScore;
        this.lifeCounter = life; //Subject to change with having a value change per level or difficulty
        this.soundToggle = sound;
        init();
    }

    /*
    * The features to start or restart when the game is started or resumed.
    * Anything that is more constant appear here. */
    public void init() {
        // We want this to be in the middle of the level regardless of the platform.
        img = new Texture("ufo-resized.png");
        pos = new Vector2(viewport.getWorldWidth()/2.5f, viewport.getWorldHeight()/2.5f);
        bounds = new Circle(pos.x, pos.y, radius);
        offset = (img.getWidth() / 2f) + (img.getHeight()/2f);
        // We now get a rotating spaceship
        actor = new Sprite(img);
        actor.setPosition(bounds.x, bounds.y);
        //actor.setOrigin(actor.getWidth()/2f, actor.getHeight()/2f);
        ammo = new DelayedRemovalArray<>(true, 50);
        defense = new Texture("mship.png");
        zone = new Rectangle(0, -3, viewport.getScreenWidth(), 40);
        fire = false;
        ticked = false;
        click = Gdx.audio.newSound(Gdx.files.internal("popped-soft.mp3"));
    }

    /*
    * Player controls and fire movements. Some parts are still in debate of being used. */
    public void update(float delta) {
        ticked = fire;
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            pos.x += delta * Speed;
            //pos.x += delta * Gdx.graphics.getDeltaTime();
        }
        else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            pos.x -= delta * Speed;
        }

        if (Gdx.input.isKeyPressed(Keys.UP)) {
            pos.y += delta * Speed;
        }
        else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            pos.y -= delta * Speed;
        }

        bounds.x = pos.x;
        bounds.y = pos.y;
        actor.setCenter(bounds.x, bounds.y);
        //actor.setBounds(bounds.x, bounds.y, bounds.x, bounds.y);
        //float turn = MathUtils.atan2((Gdx.graphics.getHeight() - Gdx.input.getY()) - pos.y, Gdx.input.getX() - pos.x);
        float turn = MathUtils.atan2( Gdx.input.getY() - pos.y, Gdx.input.getX() - pos.x);
        //turn *= (180/MathUtils.PI2);
        if (turn < 0) turn = 360 - (turn);
        //pos.rotate(turn + 90);
        //actor.setX(actor.getX() + delta);
        //actor.setRotation(actor.getRotation() + delta);
        actor.rotate(turn);
        tick -= delta;
        //Ammunition bl;
        if (Gdx.input.isTouched() && tick <= 0.1){
            //bullet fire
            //Ammunition bl
            //Gdx.input.getX() - pos.x, Gdx.input.getY() - pos.y
            //viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            Ammunition bl = new Ammunition(pos.x, pos.y, actor.getRotation(), ammunition, soundToggle);
            /*bl = new Ammunition(pos.x, pos.y, MathUtils.atan2(Gdx.input.getY() - actor.getY(),
                    Gdx.input.getX() - actor.getX()) * (180/MathUtils.PI2))*///;
            ammo.add(bl);
            tick+=0.2;
        }
        for (Ammunition bullet: ammo) {
            bullet.update(delta);
        }
        ammo.begin();
        for (int i=0; i<ammo.size; i++){
            //ammo.get(i).update(delta);
            if ((ammo.get(i).pos.x < -1f || ammo.get(i).pos.y < -1f) ||
                    (ammo.get(i).pos.x > viewport.getWorldWidth() || ammo.get(i).pos.y > viewport.getWorldHeight())){
                ammo.removeIndex(i);
            }
        }
        ammo.end();

        //Rapid fire controls
        //float yInput = (Gdx.graphics.getHeight() - Gdx.input.getY());
        //if (gest.touchDown(viewport.getScreenX(), viewport.getScreenY(), 0, Buttons.LEFT)) {}

        //Mobile accelerometer
        //TODO: Work on mobile
        float accelInput = -Gdx.input.getAccelerometerY() / (9.8f * 0.5f);
        pos.x += -delta * accelInput * Speed;
        /*if (Gdx.input.isTouched()) {
            Vector3 touchLoc = new Vector3();
            touchLoc.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchLoc);
            pos.add(touchLoc.x, touchLoc.y);
        }*/

        //invisible walls
        screenBounds();
    }

    /*
     * Initial control configuration for the player, where character is moving in directions.
     * */
    //public void configureA() {}

    /*
     * Alternative control configurations, with variation of rotating.
     */
    //public void configureB() {}


    /*
    * Don't move off screen */
    private void screenBounds() {
//        if (pos.x - radius < -viewport.getWorldWidth()){
//            pos.x = radius;
//        }
        if (pos.x - radius < 0){
            pos.x = radius;
        }
        if (pos.x + radius > viewport.getWorldWidth()) {
            pos.x = viewport.getWorldWidth() - radius;
        }
        /*if (pos.x + actor.getWidth() > viewport.getScreenWidth()) {
            pos.x = viewport.getScreenWidth() - img.getWidth();
        }*/
        if (pos.y - radius < 0) {
            pos.y = radius;
        }
        if (pos.y + radius > viewport.getWorldHeight()) {
            pos.y = viewport.getWorldHeight() - radius;
        }
    }

    /*
    * Uses Intersect class from math library to get the edges of the enemy for checking hits.
    * This will help:
    * https://www.gamedevelopment.blog/collision-detection-circles-rectangles-and-polygons/ */
    /**
     * Checks if the enemy edges cross over with the player's. This involves a lot more writing due to the complexity of
     * checking if a circle or polygon edge/corner are crossing over.
     * @param p shape of enemy
     * @param circle shape of the player.
     * @return whether the player was hit */
    private boolean enemyCross(Polygon p, Circle circle) {
        final float[] coords = p.getTransformedVertices();
        final int setTotal = coords.length;
        boolean collect = false;
        for (int i=0; i < coords.length; i+=2) {
            Vector2 start = new Vector2(coords[i], coords[i+1]);
            Vector2 end = new Vector2(coords[(i + 2) % setTotal], coords[(i+3) % setTotal]);
            Vector2 centre = new Vector2(circle.x, circle.y);
            float sqRad = circle.radius * circle.radius;
            collect = Intersector.intersectSegmentCircle(start, end, centre, sqRad);
        }

        return collect;
    }

    /**
     * Checks if the weapon has effectively hit an enemy.
     * Activate score and destroy enemy as threat if it does.
     * @param e current rendered set of enemies */
    public void enemyHit (Invaders e) {
        DelayedRemovalArray<Enemy> dr = e.invaders;
        dr.begin();
        ammo.begin();
        for (int a=0; a<dr.size; a++){
            Enemy t = dr.get(a);
            for (int d=0; d<ammo.size; d++){
                Ammunition b = ammo.get(d);
                if (Intersector.overlapConvexPolygons(t.base, b.bounds)){
                    if (!dr.get(a).destroyed){
                        if (soundToggle) click.play();
                        else click.play(0.0f);
                        dr.get(a).setDestroyed(true);
                        ammo.removeIndex(d);
                        score++;
                    }
                }
            }
        }
        dr.end();
        ammo.end();
    }

    /*
    * When the player gets hit (by the enemy ship itself or its debris)
    * */
    public boolean gotHit(Invaders inv) {
        boolean isHit = false;
        Polygon p = new Polygon(new float[] {0, 0, zone.width, 0, zone.width, zone.height, 0, zone.height});
        p.setPosition(zone.x, zone.y);
        for (Enemy enemy: inv.invaders){
            //If enemy bounds cross over to circle, player loses life value.
            //Uses math Polygons to check for bounds.
            if (enemyCross(enemy.base, this.bounds)) {
                isHit = true;
                lifeCounter--;
            }
        }
        /* If an enemy makes it to the portal without getting destroyed.
        * The enemies gain a point. Free space once they pass the map borders. */
        inv.invaders.begin();
        for (int a=0; a<inv.invaders.size; a++) {
            if (Intersector.overlapConvexPolygons(inv.invaders.get(a).base, p)){
                if (inv.invaders.get(a).destroyed) inv.invaders.removeIndex(a);
                else{
                inv.invaders.get(a).setDestroyed(true);
                inv.invaders.removeIndex(a);
                inv.invaded++;
                }
            }
        }
        inv.invaders.end();
        enemyHit(inv);
        return isHit;
    }

    // Public Getters
    public int getScore() {
        return score;
    }

    /*
    * Debug drawing method, using Shapes
    * Currently, the collision bounds is a tiny speckle on the screen, making it difficult to get hit. Due to the
    * current dimensions, the bounds will remain at this size for now. */
    public void render(ShapeRenderer renderer) {
        renderer.arc(pos.x, pos.y, 1f, 0, 360);
        renderer.setColor(PLAYER_COLOUR);
        renderer.set(ShapeType.Filled);
        renderer.circle(bounds.x, bounds.y, 10.f, 20);
    }

    /*
    * Release version: with images to support the collisions. */
    public void render(SpriteBatch batch) {
        actor.draw(batch);
        batch.draw(defense, zone.x, zone.y, zone.width, zone.height);
        //To rotate any sprite (Stackoverflow.com)
        //batch.draw(sprite,(Gdx.graphics.getWidth() - sprite.getRegionWidth()) / 2.0f,(Gdx.graphics.getHeight() - sprite.getRegionHeight()) / 2.0f,sprite.getRegionWidth()/2.0f,sprite.getRegionHeight()/2.0f, sprite.getRegionWidth(), sprite.getRegionHeight(), 1f, 1f,count, false);
        /*if(count < 0.0f)
            count = 360.0f;
        else
            count --;*/
        //For every active bullet
        //If bullet passes screen
        //Remove from array
        // Bullet renders
        for (Ammunition a: ammo) {
            a.render(batch);
        }
    }

    public void dispose() {
        img.dispose();
        click.dispose();
    }
}