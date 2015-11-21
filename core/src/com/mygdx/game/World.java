package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by kpidding on 11/20/15.
 */
public class World {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private List<Updatable> updatables;
    private List<Renderable> renderables;

    public World(TiledMap map)
    {
        this.map = map;
        renderer = new OrthogonalTiledMapRenderer(map);
        updatables = new LinkedList<Updatable>();
        renderables = new LinkedList<Renderable>();
    }


    void render(OrthographicCamera camera, SpriteBatch batch)
    {
        //Render Bottom Layer of TILED
        renderer.setView(camera);
        renderer.render();
        batch.setProjectionMatrix(camera.projection.mul(camera.view));

        batch.begin();
        for(Renderable r : renderables)
        {
            r.render(batch);
        }
        batch.end();
        //Render top layer of TILED

        //Render snow particles/Snowstorm

    }
    void update(float dt)
    {
        for(Updatable u : updatables)
        {
            u.update(dt);
        }
    }
    void addGameObject(GameObject object)
    {
        renderables.add(object);
        updatables.add(object);
    }
    void addRenderable(Renderable r)
    {
        renderables.add(r);
    }
    void addUpdateable(Updatable u)
    {
        updatables.add(u);
    }



}
