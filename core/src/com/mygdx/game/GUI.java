package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.TweenWrappers.ThermometerGUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kpidding on 11/22/15.
 */
public class GUI {
    private GrabTile grabTile;
    private static final int grabTileX = -Gdx.graphics.getWidth()/2 + 110, grabTileY = -Gdx.graphics.getHeight()/2 + 30;

    private TorchTile torchTile;
    private static final int torchTileX = -Gdx.graphics.getWidth()/2 + 150, torchTileY = -Gdx.graphics.getHeight()/2 + 30;

    private FireTile fireTile;
    private static final int fireTileX = -Gdx.graphics.getWidth()/2 + 190, fireTileY = -Gdx.graphics.getHeight()/2 + 30;

    private GuiCounter branchCounter;
    private static final int branchTileX = -Gdx.graphics.getWidth()/2 + 30, branchTileY = -Gdx.graphics.getHeight()/2 + 20;

    private GuiCounter tinderCounter;
    private static final int tinderTileX = -Gdx.graphics.getWidth()/2 + 80, tinderTileY = -Gdx.graphics.getHeight()/2 + 20;


    private FireUIElement torchUIElement;
    private static final int torchUIX = -Gdx.graphics.getWidth()/2 + 30, torchUIY = -Gdx.graphics.getHeight() /2 + 40;

    private ThermometerGUI thermometerGUI;
    private static final int thermoUIX = -Gdx.graphics.getWidth()/2 + 10, thermoUIY = -Gdx.graphics.getHeight() /2 + 40;

    List<ActionTile> tiles;
    List<GuiCounter> counters;
    public GUI(ActionCallback cb)
    {
        tiles = new ArrayList<ActionTile>(5);
        counters = new ArrayList<GuiCounter>(2);
        grabTile = new GrabTile(new Texture("art/sprites/pickup.png"), grabTileX, grabTileY,cb);
        torchTile = new TorchTile(new Texture("art/sprites/LightTorch.png"),torchTileX,torchTileY,cb);
        fireTile = new FireTile(new Texture("art/sprites/StartFire.png"),fireTileX,fireTileY,cb);
        branchCounter = new GuiCounter(new Texture("art/sprites/LogHUD.png"),branchTileX,branchTileY,3,0);
        tinderCounter = new GuiCounter(new Texture("art/sprites/TinderboxHud.png"),tinderTileX,tinderTileY,3,3);
        torchUIElement = new FireUIElement(new Texture("art/sprites/TorchUIFull.png"),new Texture("art/sprites/TorchUIEmpty.png"));
        torchUIElement.move(torchUIX,torchUIY);
        thermometerGUI = new ThermometerGUI(thermoUIX,thermoUIY);
        tiles.add(grabTile);
        tiles.add(torchTile);
        tiles.add(fireTile);
        counters.add(branchCounter);
        counters.add(tinderCounter);
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
        for(GuiCounter g : counters)
        {
            g.draw(spriteBatch);
        }
        torchUIElement.draw(spriteBatch);
        thermometerGUI.draw(spriteBatch);
    }
    public void updateBranchCount(int newAmnt)
    {
        branchCounter.setCount(newAmnt);
    }
    public void updateTinderCount(int newAmnt)
    {
        tinderCounter.setCount(newAmnt);
    }
    public void updateTorchUI(float pct)
    {
        torchUIElement.updatePercent(pct);
    }
    public void updateThermoGUI(float hp)
    {
        thermometerGUI.updateHealth(hp);
    }
}
