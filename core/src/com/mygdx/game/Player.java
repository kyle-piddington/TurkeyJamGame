package com.mygdx.game;


import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by William on 11/21/2015.
 */
public class Player extends GameObject{

    private float speed, heat, sticks;
    private float[] position = new float[2];
    public final float MAX_SPEED =  4f;
    public final float MIN_SPEED = 0.75f;
    public final float MAX_HEAT =  100;
    public final float MAX_DISTANCE_WARM = 256;
    public final float MAX_DISTANCE_HOLD = 384;

    public Player(Sprite sprite)
    {
        super(sprite);
        speed = MAX_SPEED;
        heat = MAX_HEAT;
        //setPosition(position[0], position[1]);

    }

    public float getSpeed()
    {
        return this.speed;
    }

    public void setSpeed(float speed)
    {
        this.speed = speed;
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

        if(position[1] > 0)
            position[1] += y;
        else
            position[1] = 0;

        if(position[1] < yBound)
            position[1] += y;
        else
            position[1] = yBound;
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
    @Override
    public void setPosition(float x, float y)
    {
        position[0] = x;
        position[1] = y;
        super.setPosition(x,y);
    }

    public void fireWarm(float distance)
    {
        System.out.println(distance);
        if(heat < MAX_HEAT && distance < MAX_DISTANCE_WARM) {
            System.out.println("hi");
            heat += (12.0/60.0);
            System.out.println("warm: " + heat);
        }
        if(heat > 0 && distance > MAX_DISTANCE_HOLD)
        {
            heat -= (5.0/60.0);
        }
        System.out.println("heat: " + heat);
    }

    public void freezeSlowdown()
    {
        if(heat > 65 && speed < MAX_SPEED)
        {
        }
    }
}
