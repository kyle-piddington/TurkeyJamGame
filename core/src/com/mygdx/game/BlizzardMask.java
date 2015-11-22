package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by kpidding on 11/21/15.
 */
public class BlizzardMask {
    float intensity = 0.5f;
    float targIntensity = 0.5f;
    Vector2 tx1 = new Vector2();
    Vector2 tx2 = new Vector2();
    Vector2 v1 = new Vector2(1,0);
    Vector2 v2 = new Vector2(0,1);
    Texture blizzardTexture;
    Random rand = new Random();
    BlizzardMask()
    {
        blizzardTexture = new Texture("art/sprites/BlizzardMask.png");
        blizzardTexture.setWrap(Texture.TextureWrap.MirroredRepeat,Texture.TextureWrap.MirroredRepeat);
    }
    public void update(float dt)
    {
        intensity += (targIntensity - intensity) * 0.05f;
        tx1.add(v1);
        if(tx1.x < 0)
        {
            tx1.x = 0;
        }
        if(tx1.y < 0)
        {
            tx1.y = 0;
        }
        v1.nor().scl(intensity*3);
        v1.rotate(rand.nextFloat()*0.5f);
    }
    public void setIntensity(float i)
    {
        this.targIntensity = i;
    }
    public void draw(SpriteBatch sb, int w, int h)
    {
        sb.setColor(new Color(1,1,1,intensity));
        sb.draw(blizzardTexture,-w/2,-h/2,(int)tx1.x,(int)tx1.y,(int)(tx1.x+w),(int)(tx1.y+h));

    }
}
