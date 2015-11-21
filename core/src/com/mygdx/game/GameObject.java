package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Renderable;
import com.mygdx.game.Updatable;

/**
 * Created by kpidding on 11/20/15.
 */
abstract public class GameObject implements Renderable, Updatable {
    private Sprite sprite;
    @Override
    public void render(SpriteBatch sb) {
        //Default Gameobjects contain no render informaton
    }

    @Override
    public void update(float dt)
    {
        //Default Gameobjects contain no update
    }
}
