package com.mygdx.game;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.TweenWrappers.SpriteAccessor;

/**
 * Created by kpidding on 11/22/15.
 */
public abstract class ActionTile {
    protected Sprite actionSprite;
    TextureRegion active;
    TextureRegion inactive;
    public boolean isHovered;
    protected boolean isActive;
    ActionCallback cb;
    public enum ACTIONS
    {
        GRAB,
        TORCH,
        LIGHT,
        MAPL,
        MAPR
    }
    ActionTile(Texture texture, int x, int y, ActionCallback cb)
    {
        TextureRegion[][] tmp = TextureRegion.split(texture,texture.getHeight(),texture.getHeight());
        active = tmp[0][0];
        inactive = tmp[0][1];
        actionSprite = new Sprite(inactive);
        //actionSprite.setOrigin();
        actionSprite.setPosition(x,y);
        this.cb = cb;



    }
    void draw(SpriteBatch sb)
    {
        actionSprite.draw(sb);
    }
    void setActive(boolean active)
    {
        isActive = active;
        actionSprite.setRegion((isActive ? this.active : inactive));
    }
    void onHover()
    {
        if(!isHovered) {
            isHovered = true;
            Tween.to(actionSprite, SpriteAccessor.TWEEN_SCALEXY, 0.1f)
                    .target(1.5f, 1.5f)
                    .start(World.manager);
        }
    }
    void onNotHover()
    {
        if(isHovered)
        {
            Tween.to(actionSprite, SpriteAccessor.TWEEN_SCALEXY,0.3f)
                    .target(1.0f, 1.0f)
                    .start(World.manager);
            isHovered = false;
        }

    }

    abstract void onClick();

    public boolean contains(int x, int y)
    {

        return actionSprite.getBoundingRectangle().contains(x,y);
    }

    protected void clickTween()
    {
        Timeline.createSequence()
                .push(Tween.to(actionSprite,SpriteAccessor.TWEEN_SCALEXY,0.1f)
                      .target(1.4f,1.4f))
                .push(Tween.to(actionSprite,SpriteAccessor.TWEEN_SCALEXY,0.1f)
                      .target(1.5f,1.5f))
                .start(World.manager);

    }


}
