package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by William on 11/21/2015.
 */
public class Stick extends GameObject{
    public Stick(Sprite sprite)
    {
        super(sprite);
        updateShadow();
    }
    float t;
    @Override
    public void update(float dt) {
        t += dt;
        super.getSprite().setY(super.getSprite().getY() + 0.1f*(float)Math.cos(t));
    }
}
