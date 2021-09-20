package com.kagane.laser.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.uwsoft.editor.renderer.actor.ParticleActor;

public class Explosion {
    private float duration;
    private ParticleEffect explosion;
    private ParticleEffectPool effects;
    private ParticleEffectPool.PooledEffect part;
    private DelayedRemovalArray<ParticleEffectPool.PooledEffect> efx = new DelayedRemovalArray<ParticleEffectPool.PooledEffect>();
    private ParticleActor actor;
    private DelayedRemovalArray<ParticleEffect> particles = new DelayedRemovalArray<ParticleEffect>();
    private float statetime;

    public Explosion() {
        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("blast.p"), Gdx.files.internal(""));
        effects = new ParticleEffectPool(explosion, 1, 3);
        explosion.setEmittersCleanUpBlendFunction(false);
    }

    public Explosion(Vector2 v, float d) {
        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("blast.p"), Gdx.files.internal(""));
        explosion.setPosition(v.x, v.y);
        explosion.update(d);
        explosion.start();
    }

    /*public Explosion(Vector2 v) {
        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("blast.p"), Gdx.files.internal(""));
        effects = new ParticleEffectPool(explosion, 1, 3);
        //particleUpdateLocation(v, Gdx.graphics.getDeltaTime());
        //activate(new SpriteBatch());
    }*/

    public void particleUpdateLocation(Vector2 v, float delta) {
        part = effects.obtain();
        explosion.setPosition(v.x, v.y);
        efx.add(part);
        particles.add(explosion);
        explosion.update(delta);
        explosion.start();
        //actor.act(delta);
        //actor.remove();
    }

    public void activate(SpriteBatch batch) {
        explosion.draw(batch);
        for (int i=particles.size-1; i>=0; --i){
            ParticleEffect p = particles.get(i);
            p.draw(batch);
            if (p.isComplete()){
                p.dispose();
                particles.clear();
            }
        }
        //actor.draw(batch, 1.0f);
    }

    public void activate(SpriteBatch batch, boolean dest) {
        for (int o=particles.size-1; o>=0; --o){
            ParticleEffect e = particles.get(o);
            if (dest){
                e.draw(batch);
                explosion.draw(batch);
                if (e.isComplete()){
                    e.dispose();
                    particles.removeIndex(o);
                }
            }
        }
        //actor.draw(batch, 1.0f);
    }

    public void end() {
        if (explosion.isComplete()){
            //part.free();
            explosion.dispose();
            particles.clear();
        }
    }

    public void set(Vector2 v, float delta, SpriteBatch s) {
        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("blast.p"), Gdx.files.internal(""));
        effects = new ParticleEffectPool(explosion, 1, 3);
        explosion.setPosition(v.x * delta, v.y * delta);
        explosion.update(delta);
        explosion.start();
        explosion.draw(s);
        if (explosion.isComplete()) explosion.dispose();
    }
}
