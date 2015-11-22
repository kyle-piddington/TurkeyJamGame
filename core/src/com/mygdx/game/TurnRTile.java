package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by kpidding on 11/22/15.
 */
public class TurnRTile extends ActionTile {
    TurnRTile(Texture texture, int x, int y, ActionCallback cb) {
        super(texture, x, y, cb);
        setActive(false);
            actionSprite.setAlpha(0.3f);
    }

    @Override
    void setActive(boolean active) {
        super.setActive(active);
        actionSprite.setAlpha(1.0f);
    }


    @Override
    void onClick() {
        if(isActive) {
            clickTween();
            cb.send(ACTIONS.MAPR);
        }
    }
}
