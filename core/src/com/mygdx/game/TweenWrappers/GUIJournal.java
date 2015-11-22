package com.mygdx.game.TweenWrappers;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GuiCounter;
import com.mygdx.game.World;

/**
 * Created by kpidding on 11/22/15.
 */
public class GUIJournal {
    Sprite jSprite;
    static BitmapFont font = new BitmapFont();
    boolean active = true;

    static final String text1 =
            "2/3/19xx\n\n "+
            "Things aren't looking good. My \n map has blown away and\n"+
            " I have found myself\n stuck in this blasted forest.\n My only hope" +
            " of survival is to track \n it down and return\n to base camp\n\n\n"+
            "It's damn cold out here,\n I've got to keep warm\n\n\n\nPress enter to close";
    int text1X = -80;
    int text1Y = 54;
    static final String torchText =
                    "Torches allow you to\n"+
                    "start fires without\n a tinderbox\n" +
                    "Sticks: 1\n"+
                    "Light with a tinderbox\n or a fire";
    int torchX = 250;
    int torchY = 50;
    static final String fireText =
            "Fires provide warmth,\n"+
                    "light torches without\n a tinderbox\n" +
                    "Sticks : 2\n"+
                    "Light with a tinderbox\nor a torch";
    int fireX = 250;
    int fireY = -65;
    static final String grabText =
            "Use  Grab \nto pick up sticks";
    int grabX = 250;
    int grabY = -190;

    static final String stickText = "Sticks: use in \nfires and torches";
    int stickX = 250;
    int stickY = -290;
    static final String tinderboxText =
            "Tinderboxes:\nlight fires and torches \n" +
                    "No replacements";
    int tinderX = 250;
    int tinderY = -335;





    public GUIJournal(int x, int y)
    {
        Texture jTex = new Texture("art/sprites/Journal.png");
        jSprite = new Sprite(jTex);
        jSprite.setOrigin(jTex.getWidth()/2,jTex.getHeight()/2);
        jSprite.setScale(1.75f);
        jSprite.setCenter(x, y);

    }
    public void draw(SpriteBatch sb)
    {
        jSprite.draw(sb);
        float upLX = jSprite.getX();
        float upLY = jSprite.getY() + jSprite.getHeight();
        drawText(upLX + text1X,upLY + text1Y, text1, sb);
        drawText(upLX + torchX,upLY + torchY, torchText, sb);
        drawText(upLX + fireX,upLY + fireY, fireText, sb);
        drawText(upLX + grabX,upLY + grabY, grabText, sb);
        drawText(upLX + stickX,upLY + stickY, stickText, sb);
        drawText(upLX + tinderX,upLY + tinderY, tinderboxText, sb);


    }
    private static void drawText(float x, float y, String s, Batch batch) {
        font.setColor(Color.BLACK);

        font.draw(batch,s,x,y);
    }
    public void close()
    {
        Tween.to(jSprite,SpriteAccessor.TWEEN_XY,1.5f)
                .target(jSprite.getX(),Gdx.graphics.getHeight()*1.5f)
                .ease(TweenEquations.easeInBack)
                .start(World.manager);
    }
}
