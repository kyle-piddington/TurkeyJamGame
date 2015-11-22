package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by William on 11/21/2015.
 */
public class GoalMap extends GameObject {

    public GoalMap(float x, float y)
    {

        super(new Sprite(new Texture("art/sprites/map.png")));
        updateShadow(0,0);
        super.setPosition(x,y);
    }


}
