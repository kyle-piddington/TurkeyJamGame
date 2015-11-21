package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by kpidding on 11/21/15.
 */
public class SpriteRenderable implements Renderable {
    private Sprite sprite;
    public SpriteRenderable(Sprite sprite)
    {
        this.sprite = sprite;
    }

    public void setRotation(float rotation)
    {
        this.sprite.setRotation(rotation);
    }
    public void setPosition(float x, float y)
    {
        this.sprite.setPosition(x,y);
    }
    public Sprite getSprite()
    {
        return sprite;
    }

    @Override
    public void render(SpriteBatch sb) {
        sprite.draw(sb);
    }

}
