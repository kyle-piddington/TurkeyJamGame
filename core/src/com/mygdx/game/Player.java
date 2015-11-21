package com.mygdx.game;


import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by William on 11/21/2015.
 */
public class Player extends GameObject{

    private int health, speed, heat, sticks;
    private float[] position = new float[2];
    public final int MAX_HEALTH =  100;
    public final int MAX_SPEED =  100;
    public final int MAX_HEAT =  100;

    public Player(Sprite sprite)
    {
        super(sprite);
        health = MAX_HEALTH;
        speed = MAX_SPEED;
        heat = MAX_HEAT;
        //setPosition(position[0], position[1]);

    }

    public void update(float dt)
    {
        setPosition(position[0],position[1]);
    }

    public void move(float x, float y, float xBound, float yBound)
    {
        if(position[0] > 0)
            position[0] += x;
        else
            position[0] = 0;

        if(position[0] < xBound)
            position[0] += x;
        else
            position[0] = xBound;

        if(position[1] < yBound)
            position[1] += y;
        else
            position[1] = yBound;
        //setPosition(position[0], position[1]);
    }

    public boolean overStick(Stick stick)
    {
        return stick.getSprite().getBoundingRectangle().overlaps(this.getSprite().getBoundingRectangle());
    }

    public boolean pickUpStick(Stick stick)
    {
        if(overStick(stick))
        {
            sticks++;
            return true;
        }
        return false;
    }
}
