package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by kpidding on 11/22/15.
 */
public class FireTile extends ActionTile {
    FireTile(Texture texture, int x, int y, ActionCallback cb) {
        super(texture, x, y, cb);
    }

    @Override
    void onClick() {
        if(isActive)
        {
            cb.send(ACTIONS.LIGHT);
            clickTween();

        }
    }




}
