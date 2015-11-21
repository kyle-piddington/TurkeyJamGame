package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Renderable;
import com.mygdx.game.Updatable;

/**
 * Created by kpidding on 11/20/15.
 */
abstract public class GameObject extends SpriteRenderable implements Renderable, Updatable {

    public GameObject(Sprite sprite)
    {
        super(sprite);
    }


    @Override
    public void update(float dt)
    {
        //Default Gameobjects contain no update
    }

}
