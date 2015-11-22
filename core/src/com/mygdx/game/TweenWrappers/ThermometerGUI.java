package com.mygdx.game.TweenWrappers;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.World;

import java.sql.Time;

/**
 * Created by kpidding on 11/22/15.
 */
public class ThermometerGUI {
    Sprite thermoSprite;
    Animation thermoAnim;
    public static int NUM_FRAMES = 6;
    int currFrame = 7;
    public ThermometerGUI(int x, int y)
    {
        Texture texture = new Texture("art/sprites/ThermometerGUI.png");
        TextureRegion tmp [][] = TextureRegion.split(texture, texture.getWidth() / NUM_FRAMES, texture.getHeight());
        thermoAnim = new Animation(100/7.0f,tmp[0]);
        thermoSprite = new Sprite(thermoAnim.getKeyFrame(0.0f));
        thermoSprite.setScale(2.f);
        thermoSprite.setPosition(x,y);
        thermoSprite.setAlpha(0.75f);
    }
    public void draw(SpriteBatch sb)
    {
        thermoSprite.draw(sb);
    }
    public void updateHealth(float newHealth)
    {
        thermoSprite.setRegion(thermoAnim.getKeyFrame(100-newHealth));
        if(Math.ceil(newHealth / (100 / 7.0f)) != currFrame)
        {
            Timeline.createSequence()
                    .push(Tween.to(thermoSprite,SpriteAccessor.TWEEN_ROT,0.125f)
                            .ease(TweenEquations.easeInCubic)
                            .target(-5f))
                    .push(Tween.to(thermoSprite,SpriteAccessor.TWEEN_ROT,0.125f)
                            .ease(TweenEquations.easeNone)
                            .target(5))
                    .push(Tween.to(thermoSprite, SpriteAccessor.TWEEN_ROT,0.125f)
                            .ease(TweenEquations.easeOutCubic)
                            .target(0))

                    .start(World.manager);
            currFrame = (int)Math.ceil(newHealth / (100 / 7.0f));
        }
    }


}
