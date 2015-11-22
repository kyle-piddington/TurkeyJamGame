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
    }
    protected void disableShadow() {
        renderShadow = false;
    }
    public void render(SpriteBatch sb)
    {


        super.render(sb);
        if(renderShadow)
            dropShadow.draw(sb);
        //Render a drop shadow

    }
    @Override
    public void update(float dt)
    {
        //Default Gameobjects contain no update
    }

    protected void updateShadow()
    {
        dropShadow.setPosition(this.getX(),this.getY() - sprite.getHeight()/3);

    }

    protected void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
