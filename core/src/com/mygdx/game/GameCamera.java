package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by kpidding on 11/21/15.
 */
public class GameCamera extends OrthographicCamera {
    float rotation;

    public void rotate(float angle)
    {
        rotation += angle;
        super.rotate(angle);
        super.update();
    }
    public void setRotation(float angle)
    {

        super.rotate(-rotation);
        rotation = angle;
        super.rotate(angle);
        super.update();

    }
    public float getRotation()
    {
        return rotation;
    }

}
