package com.mygdx.game.test;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GameObject;

/**
 * Created by kpidding on 11/20/15.
 */
public class TestGameObject extends GameObject {
    Sprite sprite;
    public TestGameObject(Sprite sprite)
    {
        this.sprite = sprite;
    }
    public void update(float dt)
    {

    }
    public void render(SpriteBatch sb)
    {
        sprite.draw(sb);
    }

}
