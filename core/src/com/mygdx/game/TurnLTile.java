package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by kpidding on 11/22/15.
 */
public class TurnLTile extends   ActionTile{
    TurnLTile(Texture texture, int x, int y, ActionCallback cb) {
        super(texture, x, y, cb);
        actionSprite.setAlpha(0.3f);
        setActive(false);
    }

    @Override
    void setActive(boolean active) {
        if(active)
        {
            actionSprite.setAlpha(1.0f);

        }
        super.setActive(active);
    }

    @Override
    void onClick() {
        if(isActive)
        {
            clickTween();
            cb.send(ACTIONS.MAPL);
        }

    }
}
