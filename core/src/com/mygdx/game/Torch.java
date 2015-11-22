package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

/**
 * Created by kpidding on 11/21/15.
 */
public class Torch {
    static Random rand = new Random();
    public Sprite torchSprite = new Sprite(new Texture("art/sprites/TorchGlow.png"));
    float t = 45; //Minute and a half per torch
    public Torch(float t)
    {
        this.t = t;
        torchSprite.setOrigin(torchSprite.getWidth()/2.f,torchSprite.getHeight()/2.f);
    }
    public Torch()
    {
        this.t = 45;
        torchSprite.setOrigin(torchSprite.getWidth()/2.f,torchSprite.getHeight()/2.f);
    }
    public boolean isLit()
    {
        return t > 0;
    }
    public float getLitPercent()
    {
        return Math.max(0,t) / 45.f;
    }
    public void update(float dt)
    {
        t-= dt;
        torchSprite.setScale(2 * (getLitPercent() + 0.25f) + rand.nextFloat()*0.25f);

    }
    public void render(SpriteBatch sb)
    {
        torchSprite.draw(sb);
    }
}
