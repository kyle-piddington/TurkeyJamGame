package com.mygdx.game;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.mygdx.game.TweenWrappers.SpriteAccessor;

/**
 * Created by kpidding on 11/21/15.
 */
public class FireUIElement {
    static Texture fullUITexture = new Texture("art/sprites/FireFull.png");
    static Texture emptyUITexture = new Texture("art/sprites/FireGrey.png");
    ProgressBar bar;
    boolean shown;
    float targX, targY;
    float percent;
    Sprite fullSprite,emptySprite;
    public FireUIElement()
    {
        fullSprite = new Sprite(fullUITexture);
        emptySprite = new Sprite(emptyUITexture);
        emptySprite.setAlpha(0);
        fullSprite.setAlpha(0);

    }
    public void display(float x, float y)
    {
        if(!shown)
        {
            fullSprite.setPosition(x,y);
            emptySprite.setPosition(x,y);
            Tween.to(fullSprite,SpriteAccessor.TWEEN_ALPHA,0.3f)
                    .target(0.6f)
                    .start(World.manager);
            Tween.to(emptySprite,SpriteAccessor.TWEEN_ALPHA,0.3f)
                    .target(0.8f)
                    .start(World.manager);
        }
    }
    public void hide()
    {
        if(shown)
        {
            Tween.to(fullSprite, SpriteAccessor.TWEEN_ALPHA,0.3f)
                    .target(0)
                    .start(World.manager);
            Tween.to(emptySprite,SpriteAccessor.TWEEN_ALPHA,0.3f)
                    .target(0)
                    .start(World.manager);
            shown = false;
        }
    }
    public void move(float x, float y)
    {
        if(!shown || targX != x && targY != y)
        {
            if(!shown)
            {
                display(x,y);
                shown = true;
            }
            else
            {

                Tween.to(fullSprite,SpriteAccessor.TWEEN_XY,0.5f)
                        .target(x,y)
                        .start(World.manager);
                Tween.to(emptySprite,SpriteAccessor.TWEEN_XY,0.5f)
                        .target(x,y)
                        .start(World.manager);
            }
            targX = x;
            targY = y;
        }
    }
    public void draw(SpriteBatch sb)
    {

        emptySprite.draw(sb);
        fullSprite.setScale(percent);
        fullSprite.draw(sb);

    }
    public void updatePercent(float pct)
    {
        this.percent = pct;
    }


}
