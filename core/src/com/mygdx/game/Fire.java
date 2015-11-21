package com.mygdx.game;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.TweenWrappers.SpriteAccessor;

/**
 * Created by kpidding on 11/21/15.
 */
public class Fire extends GameObject {
    private static final int NUM_FRAMES = 4;
    int numFrames;
    int currFrame = 0;
    float fireLife = 60*3; //3 minutes of life by default
    float t;
    boolean out = false;
    Animation fireAnimation;

    public Fire(float x, float y) {
        super(new Sprite(new Texture("art/sprites/fire.png")));
        super.setPosition(x, y);
        super.getSprite().setScale(0.0f);

        Timeline.createSequence()
            .push(Tween.to(getSprite(), SpriteAccessor.TWEEN_SCALEXY,0.2f)
                .target(1.3f,2.8f)
            )
            .push(Tween.to(getSprite(),SpriteAccessor.TWEEN_SCALEXY,1.0f)
                .target(2.5f,2.5f)
                .ease(TweenEquations.easeOutElastic))
            .start(World.manager);


        Texture fireTexture = getSprite().getTexture();
        getSprite().setSize(fireTexture.getHeight(),fireTexture.getHeight());
        getSprite().setOrigin(fireTexture.getHeight()/2,fireTexture.getHeight()/6);
        TextureRegion tmp[][] = TextureRegion.split(fireTexture,fireTexture.getHeight(),fireTexture.getHeight());
        TextureRegion frames[] = new TextureRegion[NUM_FRAMES];
        for(int i = 0; i < NUM_FRAMES; i++) {
            frames[i] = tmp[0][i];
        }
        fireAnimation = new Animation(0.25f,frames);
        super.getSprite().setRegion(fireAnimation.getKeyFrame(0));
    }
    public void update(float dt)
    {
        t += dt;

        fireLife -= dt;
        if(fireLife > 0)
            super.getSprite().setRegion(fireAnimation.getKeyFrame(t, true));
        else if(!out)
        {

            Texture burntTex = new Texture("art/sprites/fireBurnt.png");
            super.getSprite().setTexture(burntTex);
            super.getSprite().setRegion(burntTex);
            out = true;

        }
    }
    public boolean isExtinguished()
    {
        return out;
    }

}
