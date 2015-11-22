package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by William on 11/21/2015.
 */
public class GoalMap extends GameObject {

    private float[] position = new float[2];
    public GoalMap(Sprite sprite)
    {
        super(sprite);
        updateShadow(0,0);
        position[0] = 11*64;
        position[1] = 64*64 - 8*64;
    }


}
