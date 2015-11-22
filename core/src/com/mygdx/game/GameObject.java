package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Renderable;
import com.mygdx.game.Updatable;

/**
 * Created by kpidding on 11/20/15.
 */
abstract public class GameObject extends SpriteRenderable implements Renderable, Updatable {

    Sprite dropShadow;
    boolean renderShadow = true;
    public GameObject(Sprite sprite)
    {
        super(sprite);
        dropShadow = new Sprite(new Texture("art/sprites/dropShadow.png"));
        dropShadow.setOrigin(dropShadow.getWidth()/2.f,dropShadow.getHeight()/2.f);
    }
    protected void disableShadow() {
        renderShadow = false;
    }
    public void render(SpriteBatch sb)
    {

        if(renderShadow)
            dropShadow.draw(sb);
        super.render(sb);

        //Render a drop shadow

    }
    @Override
    public void update(float dt)
    {
        //Default Gameobjects contain no update
    }

    public void setRotation(float r)
    {
        super.setRotation(r);
        dropShadow.setRotation(r);
    }

    protected void updateShadow()
    {

        dropShadow.setPosition(sprite.getX(),sprite.getY() - sprite.getHeight()/2);

    }

    protected void updateShadow(float xOff, float yOff)
    {

        dropShadow.setPosition(sprite.getX() + xOff,sprite.getY() + yOff);

    }

    protected void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
