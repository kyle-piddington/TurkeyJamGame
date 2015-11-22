package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kpidding on 11/22/15.
 */
public class GUI {
    private GrabTile grabTile;
    private static final int grabTileX = -Gdx.graphics.getWidth()/2 + 60, grabTileY = -Gdx.graphics.getHeight()/2 + 30;

    private TorchTile torchTile;
    private static final int torchTileX = -Gdx.graphics.getWidth()/2 + 100, torchTileY = -Gdx.graphics.getHeight()/2 + 30;

    private FireTile fireTile;
    private static final int fireTileX = -Gdx.graphics.getWidth()/2 + 140, fireTileY = -Gdx.graphics.getHeight()/2 + 30;

    List<ActionTile> tiles;
    public GUI(ActionCallback cb)
    {
        tiles = new ArrayList<ActionTile>(5);
        grabTile = new GrabTile(new Texture("art/sprites/pickup.png"), grabTileX, grabTileY,cb);
        torchTile = new TorchTile(new Texture("art/sprites/LightTorch.png"),torchTileX,torchTileY,cb);
        fireTile = new FireTile(new Texture("art/sprites/StartFire.png"),fireTileX,fireTileY,cb);
        tiles.add(grabTile);
        tiles.add(torchTile);
        tiles.add(fireTile);
    }
    public void update()
    {
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.input.getY();
        mouseX -= Gdx.graphics.getWidth()/2;
        mouseY = (Gdx.graphics.getHeight() - mouseY) - Gdx.graphics.getHeight()/2;
        for(ActionTile t : tiles)
        {
            if(t.contains(mouseX,mouseY))
            {
                t.onHover();
            }
            else
            {
                t.onNotHover();
            }
        }

    }
    public void onClick()
    {
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.input.getY();
        mouseX -= Gdx.graphics.getWidth()/2;
        mouseY = (Gdx.graphics.getHeight() - mouseY) - Gdx.graphics.getHeight()/2;

        for(ActionTile t : tiles) {
            if (t.contains(mouseX, mouseY)) {
                t.onClick();
            }
        }

    }

    public void setGrabActive(boolean active)
    {
        grabTile.setActive(active);
    }
    public void setTorchActive(boolean active)
    {
        torchTile.setActive(active);
    }
    public void setFireActive(boolean active)
    {
        fireTile.setActive(active);
    }
    public void draw(SpriteBatch spriteBatch) {
        for(ActionTile t : tiles)
        {
            t.draw(spriteBatch);
        }
    }
}
