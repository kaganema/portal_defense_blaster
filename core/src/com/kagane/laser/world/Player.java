package com.kagane.laser.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.Input.*;
//import com.badlogic.gdx.input.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.audio.Sound;
import com.kagane.laser.game.ScreenFace;
//import com.badlogic.gdx.InputProcessor;
//import com.badlogic.gdx.input.GestureDetector;

public class Player {
    public static final String TAG = Player.class.getName();
    private static final Color PLAYER_COLOUR = Color.WHITE; //Subject to change
    private static final Color PLAYER_STROKE = Color.BLACK; //Subject to change
    private static float Speed = 150.f;
    private static final float MAX_SPEED = 500.f;
    private static final float radius = 0.7f;
    long fireTime = 10000;
    float tick;
    long lastTime;

    Viewport viewport;
    Vector2 pos;
    Circle bounds;
    SpriteBatch batch;
    Texture img;
    Texture defense;
    Rectangle zone;
    Ammunition bl;
    DelayedRemovalArray<Ammunition> ammo;
    Sprite actor;
    //GestureDetector gest;
    public int lifeCounter;
    String ammunition;
    float offset;

    private int score;
    //Rotate any sprite (Stackoverflow.com)
    //private float count =360.0f;
    //int lifeCounter;
    boolean fire;
    boolean ticked;
    // Set array of bullets to be fired here.

    // Explosion data
    Sound click;
    boolean soundToggle;

    //Experiment: Particle effects
    //ParticleEffect explosion;
    ParticleEffect part;
    Explosion plo;

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
        //this.ammunition = "BULLET";
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
        //ammunition = "BULLET";
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


    public Player(Viewport viewport, Vector2 vec) {
        this.viewport = viewport;
        this.pos = vec;
        //bl = new Ammunition(pos.x, pos.y, 45);
        init(vec);
    }


    public void init() {
        // We want this to be in the middle of the level regardless of the platform.
        //img = new Texture(Gdx.files.internal("ufo.png"));
        img = new Texture("ufo-resized.png");
        //pos = new Vector2(viewport.getScreenWidth() / 2.f, viewport.getScreenHeight() / 2.f);
        pos = new Vector2(viewport.getWorldWidth()/2.5f, viewport.getWorldHeight()/2.5f);
        //pos = new Vector2(viewport.getScreenX(), viewport.getScreenY());

        //pos.set(img.getWidth()/2f, img.getHeight()/2f);
        //pos = new Vector2(img.getWidth()/2f, img.getHeight()/2f);
        bounds = new Circle(pos.x, pos.y, radius);
        offset = (img.getWidth() / 2f) + (img.getHeight()/2f);
        //bounds = new Circle(img.getWidth()/2f, img.getHeight()/2f, radius);
        //bl = new Ammunition(bounds.x, bounds.y, 90);
        //bl = new Ammunition(img.getWidth() - bounds.x, img.getHeight() - bounds.y, 90);
        //ammo = new DelayedRemovalArray<>();
        actor = new Sprite(img);
        actor.setPosition(bounds.x, bounds.y);
        //actor.setOrigin(actor.getWidth()/2f, actor.getHeight()/2f);
        ammo = new DelayedRemovalArray<>(true, 50);
        defense = new Texture("mship.png");
        zone = new Rectangle(0, -3, viewport.getScreenWidth(), 40);
        //score = 0;
        fire = false;
        ticked = false;
        click = Gdx.audio.newSound(Gdx.files.internal("popped-soft.mp3"));

//        explosion = new ParticleEffect();
//        explosion.load(Gdx.files.internal("blast.p"), Gdx.files.internal(""));
        //part = new ParticleEffect();
        //part.load(Gdx.files.internal("blast.p"), Gdx.files.internal(""));
        //plo = new Explosion();
    }

    public void init(Vector2 vec) {
        // We want this to be in the middle of the level regardless of the platform.
        pos = new Vector2(vec.x, vec.y);
    }

    /*public void update(float delta, Viewport v) {
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            //pos.x += delta * Speed;
            pos.x += 0.01f;
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            //pos.x -= delta * Speed;
            pos.x -= 0.01f;
        }

        //Clamp acceleration

        //invisible walls
        //screenBounds(v.getWorldWidth(), v.getWorldHeight());
        screenBounds();
    }*/

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
        if (Gdx.input.isTouched() && tick <= 0.1){
            //bullet fire
            //Gdx.graphics.getDeltaTime();
            //Gdx.input.getX() - pos.x, Gdx.input.getY() - pos.y
            //viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            //bl = new Ammunition(pos.x, pos.y, Gdx.input.getX() + -(Gdx.input.getY()));
            //bl = new Ammunition(pos.x, pos.y, actor.getRotation());
            //bl = new Ammunition(pos.x, pos.y, actor.getRotation(), ammunition);
            bl = new Ammunition(pos.x, pos.y, actor.getRotation(), ammunition, soundToggle);
            /*bl = new Ammunition(pos.x, pos.y, MathUtils.atan2(Gdx.input.getY() - actor.getY(),
                    Gdx.input.getX() - actor.getX()) * (180/MathUtils.PI2))*/;
            //bl = new Ammunition(bounds.x, bounds.y);
            //viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            //viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            ammo.add(bl);
            tick+=0.2;

            //bl = new Ammunition(pos.x, pos.y, Gdx.input.getRotation());
            //bl = new Ammunition(new Vector2(Gdx.input.getX()-bounds.x, Gdx.input.getY()-bounds.y),
            //       new Vector2(Gdx.input.getX(), Gdx.input.getY()), Gdx.input.getAzimuth() || Gdx.input.getRotation());
            //viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
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
    * Tip: Modify this when using player rotation. */
/*    private void rotatePlayerToMouse() {

        Vector3 mousePos = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        //Vector3 playerPos = this.pos.setToRandomDirection();//this.instance.transform.getTranslation(Vector3)
        //Use this calculation
        float radians = (float) Math.atan2(mousePos.y - this.pos.y, mousePos.x - this.pos.x);
        radians -= Math.PI/2;

        this.instance.transform.setFromEulerAnglesRad(0, 0, radians).setTranslation(this.pos);
    }*/

    /**
     * Initial control configuration for the player, where character is moving in directions.
     * */
    //public void configureA() {}

    /**
     * Alternative control configurations, with variation of rotating.
     */
    //public void configureB() {}

    /*public void screenBounds(float radius, float viewportWidth, float viewportHeight) {
        if (pos.x - radius < 0) {
            pos.x = radius;
            //velocity.x = -velocity.x;
            pos.x = -pos.x;
        }
        if (pos.x + radius > viewportWidth) {
            pos.x = viewportWidth - radius;
            //velocity.x = -velocity.x;
            pos.x = -pos.x;
        }
        if (pos.y - radius < 0) {
            pos.y = radius;
            //velocity.y = -velocity.y;
            pos.y = -pos.y;
        }
        if (pos.y + radius > viewportHeight) {
            pos.y = viewportHeight - radius;
            //velocity.y = -velocity.y;
            pos.y = -pos.y;
        }
    }*/
    public void screenBounds(float radius, float viewportWidth, float viewportHeight) {
        if (pos.x - radius < -viewportWidth) {
            pos.x = radius;
            //velocity.x = -velocity.x;
            pos.x = -pos.x;
        }
        if (pos.x + radius > viewportWidth) {
            pos.x = viewportWidth - radius;
            //velocity.x = -velocity.x;
            pos.x = -pos.x;
        }
        if (pos.y - radius < -viewportHeight) {
            pos.y = radius;
            //velocity.y = -velocity.y;
            pos.y = -pos.y;
        }
        if (pos.y + radius > viewportHeight) {
            pos.y = viewportHeight - radius;
            //velocity.y = -velocity.y;
            pos.y = -pos.y;
        }
    }
    public void screenBounds(float viewportWidth, float viewportHeight) {
        if (pos.x < -viewportWidth + 1) {
            //velocity.x = -velocity.x;
            pos.x = -viewportWidth;
        }
        if (pos.x > viewportWidth - 1) {
            //velocity.x = -velocity.x;
            pos.x = viewportWidth;
        }
        if (pos.y - radius < 0) {
            pos.y = -pos.y;
        }
        if (pos.y > viewportHeight) {
            //velocity.y = -velocity.y;
            pos.y = -pos.y;
        }
    }

    public void screenBounds() {
//        if (pos.x - radius < -viewport.getWorldWidth()){
//            pos.x = radius;
//        }
        if (pos.x - radius < 0){
            pos.x = radius;
        }
        /*if (pos.x + radius > viewport.getWorldWidth()) {
            pos.x = viewport.getWorldWidth() - radius;
        }*/
        if (pos.x + actor.getWidth() > viewport.getScreenWidth()) {
            pos.x = viewport.getScreenWidth() - img.getWidth();
        }
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
    private boolean enemyCross(Polygon p, Circle circle) {
        final float[] coords = p.getTransformedVertices();
        final int setTotal = coords.length;
        boolean collect = false;
        for (int i=0; i < coords.length; i+=2) {
            Vector2 start = new Vector2(coords[i], coords[i+1]);
            Vector2 end = new Vector2(coords[(i + 2) % setTotal], coords[(i+3) % setTotal]);
            Vector2 centre = new Vector2(circle.x, circle.y);
            float sqRad = circle.radius * circle.radius;

//            return Intersector.intersectSegmentCircle(start, end, centre, sqRad);
            collect = Intersector.intersectSegmentCircle(start, end, centre, sqRad);
        }

        return collect;
    }

    public void enemyHit (Invaders e) {
        //boolean hit = false;
        DelayedRemovalArray<Enemy> dr = e.invaders;
        //Explosion exp;
        dr.begin();
        ammo.begin();
        for (int a=0; a<dr.size; a++){
            Enemy t = dr.get(a);
            for (int d=0; d<ammo.size; d++){
                Ammunition b = ammo.get(d);
                if (Intersector.overlapConvexPolygons(t.base, b.bounds)){
                    //click.play();
                    //Explosion exp = new Explosion();
                    //float up = Gdx.graphics.getDeltaTime();
                    //Explosion exp = new Explosion(new Vector2(dr.get(a).base.getX(), dr.get(a).base.getY()), up);
                    //exp.particleUpdateLocation(new Vector2(dr.get(a).base.getX(), dr.get(a).base.getY()), up);
                    //plo.particleUpdateLocation(new Vector2(dr.get(a).base.getX(), dr.get(a).base.getY()), up);
                    //dr.get(a).explosion.setPosition(dr.get(a).base.getX(), dr.get(a).base.getY());
                    //dr.get(a).destroyed = true;
                    //dr.get(a).explosion.start();
                    //if (dr.get(a).isDestroyed()) dr.get(a).explosion.draw(new SpriteBatch());
                    //dr.get(a).explode();
                    //exp.activate(new SpriteBatch(), dr.get(a).destroyed);
                    //exp.activate(new SpriteBatch(), up);
                    //ParticleEffect part = new ParticleEffect();
                    //part.load(Gdx.files.internal("blast.p"), Gdx.files.internal(""));
                    //part.setPosition(dr.get(a).base.getX() - 8,  dr.get(a).base.getY() - 8);
                    //part.setPosition(dr.get(a).pos.x, dr.get(a).pos.y);
                    //part.update(up);
                    //part.start();
                    //explosions.add(exp);
                    //part.draw(new SpriteBatch(), up);
                    //if (part.isComplete()) part.dispose();
                    //exp.end();
                    //exp.set(dr.get(a).pos, Gdx.graphics.getDeltaTime(), new SpriteBatch());
                    //dr.get(a).destroyed = true;
                    //t.setDestroyed(true);
                    //dr.get(a).setDestroyed(true);
                    //dr.get(a).explode(batch);
                    //dr.get(a).explode(batch);
                    //ParticleEffect part = new ParticleEffect();
                    //part.load(Gdx.files.internal("blast.p"), Gdx.files.internal(""));
                    //part.setPosition(dr.get(a).base.getX() - 8,  dr.get(a).base.getY() - 8);
                    //part.update(up);
                    //part.start();
                    //part.setPosition(dr.get(a).base.getX(),  dr.get(a).base.getY());
                    //dr.removeIndex(a);
                    //ammo.removeIndex(d);
                    //part.draw(new SpriteBatch(), up);
                    //if (part.isComplete()) part.dispose();
                    //explosions.removeIndex(exp)
                    //exp.end();
                    //explosion.setPosition(dr.get(a).pos.x, dr.get(a).pos.y);
                    //dr.get(a).explosion.start();
                    //explosion.draw(batch);
                    //dr.get(a).explosion.draw(batch);
                    //dr.get(a).explosion.dispose();
                    //score++;
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
    * When the player gets hit (at least by the enemy ship itself.)
    * */
    public boolean gotHit(Invaders inv) {
        boolean isHit = false;
        Polygon p = new Polygon(new float[] {0, 0, zone.width, 0, zone.width, zone.height, 0, zone.height});
        p.setPosition(zone.x, zone.y);
        for (Enemy enemy: inv.invaders){
            /*if ((enemy.pos.dst(pos) <= this.pos.y) && (enemy.pos.dst(pos) == this.pos.x)) {
                isHit = true;
                lifeCounter--;
            }*/
            /*if (enemy.base.contains(this.pos)) {
                isHit = true;
                lifeCounter--;
            }*/
            //If enemy bounds cross over to circle, player loses life value.
            //Uses math Polygons to check for bounds.
            if (enemyCross(enemy.base, this.bounds)) {
                isHit = true;
                lifeCounter--;
            }
            //enemyHit(inv, enemy.base, ammo, bl.bounds);
            /*if (Intersector.overlapConvexPolygons(bl.bounds, enemy.base)) {
                inv.invaders.removeValue(enemy, isHit);
                ammo.removeValue(bl, isHit);
            }*/
            //isHit = enemyCross(enemy.base, this.bounds) ? true : false; or isHit = enemyCros(enemy.base, this.bounds)
            //if (isHit) lifeCounter--;
            /*if (this.bounds.contains(enemy.base.getX(), enemy.base.getY())) {
                isHit = true;
                lifeCounter--;
            }*/
            /*float p[]=enemy.base.getTransformedVertices();
            for (int i=0; i<p.length; i+=2){
                if (bounds.contains(p[i], p[i+1])){
                    isHit = true;
                    lifeCounter--;
                }
            }*/
            /*if (Intersector.intersectLines(enemy.pos, this.pos, enemy.pos, this.pos, enemy.pos)){
                isHit = true;
                lifeCounter--;
            }*/
            /*if (Intersector.overlapConvexPolygons(enemy.base, p)){
                inv.invaded++;
            }*/
        }
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

    public boolean touchDown(int sx, int sy, int pointer, int button) {
        Vector2 startPos = viewport.unproject(new Vector2(sx, sy));
        Vector2 renderPos = new Vector2();
        renderPos.sub(startPos);
        return true;
    }

    public boolean touchDown(int sx, int sy, int pointer, int button, Vector2 pos) {
        Vector2 startPos = viewport.unproject(new Vector2(sx, sy));
        Vector2 renderPos = new Vector2(pos);
        renderPos.sub(startPos);
        return true;
    }

    /* A shape to draw bullets. */
    public void bulletDraw(ShapeRenderer renderer) {
        renderer.rect(0, 0, 0.5f, 0.5f);
    }

    public void bulletFire(float angle) {
        viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
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
        renderer.setColor(PLAYER_STROKE);
        //renderer.set(ShapeType.Line);
        //renderer.circle(pos.x, pos.y, 0.71f, 30);
        renderer.arc(pos.x, pos.y, 1f, 0, 360);
        renderer.setColor(PLAYER_COLOUR);
        renderer.set(ShapeType.Filled);
        //renderer.circle(pos.x, pos.y, radius, 20);
        renderer.circle(bounds.x, bounds.y, 10.f, 20);
        //bullet
        bl.render(renderer);
    }

    /*
    * Release version: with images to support the collisions. */
    public void render(SpriteBatch batch) {
        //batch.draw(img, bounds.x, bounds.y);
        actor.draw(batch);
//        batch.draw(img, bounds.x, bounds.y, img.getWidth()/2f, img.getHeight()/2f,
//                img.getWidth(), img.getHeight(), 1f, 1f, pos.angle(), 0, 0, img.getWidth(), img.getHeight(),
//                false, false);
        batch.draw(defense, zone.x, zone.y, zone.width, zone.height);
        //To rotate any sprite (Stackoverflow.com)
        /*batch.draw(sprite,(Gdx.graphics.getWidth() - sprite.getRegionWidth()) / 2.0f,(Gdx.graphics.getHeight() - sprite.getRegionHeight()) / 2.0f,sprite.getRegionWidth()/2.0f,sprite.getRegionHeight()/2.0f, sprite.getRegionWidth(), sprite.getRegionHeight(), 1f, 1f,count, false);

        if(count < 0.0f)
            count = 360.0f;
        else
            count --;*/
        //bullet
        //TODO: Draw bullets fired
        //For every active bullet
        //If bullet passes screen
        //Remove from array
        //bl.render(batch);
        for (Ammunition a: ammo) {
            a.render(batch);
        }

        //if (enemyHit())
        //Only render when enemy is destroyed
        //part.start();
        //plo.activate(batch);
        //part.draw(batch);
        //if (part.isComplete()) part.dispose();
    }

    public void dispose() {
        img.dispose();
        click.dispose();
    }
}

/*class AmmoList {
    DelayedRemovalArray<Ammunition> ammo;
    Viewport v;

    public AmmoList(Viewport view, Ammunition a) {
        v = view;
        ammo = new DelayedRemovalArray<>();
        ammo.add(a);
    }

    public void update(float delta) {
        for (Ammunition a: ammo) {
            a.update(delta);
        }
    }

    public void delete() {
        ammo.begin();
        for (int i=0; i<ammo.size; i++) {
            if ((ammo.get(i).pos.x < 0 || ammo.get(i).pos.y < 0) ||
                    (ammo.get(i).pos.x > v.getWorldWidth() || ammo.get(i).pos.y > v.getWorldHeight())){
                ammo.removeIndex(i);
            }
        }
        ammo.end();
    }
}*/
