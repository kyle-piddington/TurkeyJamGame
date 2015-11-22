package com.mygdx.game;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by kpidding on 11/22/15.
 */
public class CameraAccessor implements TweenAccessor<GameCamera> {
    public static final int ROT = 0;
    @Override
    public int getValues(GameCamera target, int tweenType, float[] returnValues) {
        switch (tweenType)
        {
            case ROT:
                returnValues[0]= target.getRotation();
                return 1;
            default:
                assert false; return 0;
        }
    }

    @Override
    public void setValues(GameCamera target, int tweenType, float[] newValues) {
        switch (tweenType)
        {
            case ROT:
                target.setRotation(newValues[0]);
                break;
            default:
                assert false;
        }
    }
}
