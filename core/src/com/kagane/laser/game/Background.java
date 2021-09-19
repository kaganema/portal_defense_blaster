package com.kagane.laser.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/*
* The star field of the game's background.
* Taken from the example on Udacity's tutorial as I thought to put it to more game use. */

public class Background {

    private static final float STAR_DENSITY = 0.03f;
    private Array<Vector2> stars;

    ShapeRenderer renderer;

    public void bgStart(float density) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        int starCount = (int)(screenHeight * screenWidth * density);
        // A new array of Vector2's to hold the star positions
        stars = new Array<Vector2>(starCount);
        // Fill the star field with randomised positions.
        Random random = new Random();
        for (int i = 0; i < starCount; i++) {
            int x = random.nextInt(screenWidth);
            int y = random.nextInt(screenHeight);
            stars.add(new Vector2(x, y));
        }
    }

    public void resize(int width, int height) {
        bgStart(STAR_DENSITY);
        //renderer = new ShapeRenderer();
    }

    public void resize() {
        bgStart(STAR_DENSITY);
        //renderer = new ShapeRenderer();
    }

    public void render() {
        //
        renderer.begin(ShapeType.Point);
        // Loop through the star positions and use shapeRenderer to draw points
        for (Vector2 star : stars){
            renderer.point(star.x, star.y, 0);
        }
        // Must close this renderer at the end of this background.
        renderer.end();
    }

    public void render(ShapeRenderer renderer) {
        //
        renderer.begin(ShapeType.Point);
        // Loop through the star positions and use shapeRenderer to draw points
        for (Vector2 star : stars){
            renderer.point(star.x, star.y, 0);
        }
        // Must close this renderer at the end of this background.
        renderer.end();
    }

    public void hide() {
        // Dispose of our ShapeRenderer
        renderer.dispose();
        //super.dispose();
    }

    /*public void dispose(ShapeRenderer renderer) {
        // Dispose of our ShapeRenderer
        renderer.dispose();
        //super.dispose();
    }*/

    public void dispose() {
        // Dispose of our ShapeRenderer
        renderer.dispose();
        //super.dispose();
    }

}
