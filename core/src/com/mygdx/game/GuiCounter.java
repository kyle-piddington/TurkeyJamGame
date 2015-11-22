package com.mygdx.game;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.TweenWrappers.SpriteAccessor;

/**
 * Created by kpidding on 11/22/15.
 */
public class GuiCounter {
    int count;
    int max;
    Sprite guiSprite;
    int baseX,baseY;
    private static BitmapFont font = new BitmapFont();
    GuiCounter(Texture tex, int x, int y, int max, int initialCount)
    {
        guiSprite = new Sprite(tex);
        guiSprite.setPosition(x,y);
        this.max = max;
        count = initialCount;
        baseX = x;
        baseY = y;

    }
    void setCount(int count)
    {
        if(count != this.count) {
            this.count = count;
            guiSprite.setY(guiSprite.getY() + 15);
            Tween.to(guiSprite, SpriteAccessor.TWEEN_XY, 0.5f)
                    .target(baseX,baseY)
                    .ease(TweenEquations.easeOutBounce)
                    .start(World.manager);
        }
    }


    public void draw(SpriteBatch spriteBatch) {
        guiSprite.draw(spriteBatch);
        Color drawColor;
        if(count == max)
            drawColor = Color.YELLOW;
        else if(count == 0)
            drawColor = Color.RED;
        else
            drawColor = Color.BLACK;
        GuiCounter.drawText(baseX + guiSprite.getWidth() + 5,baseY, count + "/" + max, drawColor, spriteBatch);
    }

    private static void drawText(float x, int y, String s, Color drawColor, Batch batch) {
        font.setColor(drawColor);
        font.draw(batch,s,x,y);
    }
}
