package com.mygdx.game;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.*;

/**
 * Created by kpidding on 11/20/15.
 */
public class World {
    public static TweenManager manager = new TweenManager();
    private class BillboardComarator implements Comparator<SpriteRenderable>
    {
        private Matrix4 v;
        public BillboardComarator(Matrix4 v)
        {
            setViewMatrix(v);
        }
        void setViewMatrix(Matrix4 v)
        {
            this.v = v;
        }
        @Override
        public int compare(SpriteRenderable spriteRenderable, SpriteRenderable spriteRenderable2) {
            Sprite s1 = spriteRenderable.getSprite();
            Sprite s2 = spriteRenderable2.getSprite();
            Matrix4 m1 = new Matrix4(); m1.setTranslation(s1.getX(), s1.getY(),0);
            Matrix4 m2 = new Matrix4(); m2.setTranslation(s2.getX(), s2.getY(),0);
            Matrix4 view1 = new Matrix4(); view1.mul(v);
            Matrix4 view2 = new Matrix4(); view2.mul(v);
            Vector3 v1 = (view1.mul(m1)).getTranslation(new Vector3());
            Vector3 v2 = (view2.mul(m2)).getTranslation(new Vector3());
            if(v1.y < v2.y)
            {
                return 1;
            }
            else return -1;
        }
    }

    private class FireComarator implements Comparator<Fire>
    {

        private float x;
        private float y;
        FireComarator(float playerX, float playerY)
        {
            this.x = playerX;
            this.y = playerY;
        }
        @Override
        public int compare(Fire fire, Fire fire2) {
            //if A is out, and B is out, fires are equal
            if(fire.isExtinguished() && fire2.isExtinguished())
            {
                return 0;
            }

            else if(fire.isExtinguished())
            {
                return 1;
            }
            //if B is out, A is greater
            else if(fire2.isExtinguished())
            {
                return -1;
            }
            else
            {
                float d1 = (x - fire.getSprite().getX()) * (x - fire.getSprite().getX()) +
                            (y - fire.getSprite().getY()) * (y - fire.getSprite().getY());
                float d2 = (x - fire2.getSprite().getX()) * (x - fire2.getSprite().getX()) +
                        (y - fire2.getSprite().getY()) * (y - fire2.getSprite().getY());
                if(d1 < d2)
                {
                    return -1;
                }
                else if(d1 == d2)
                {
                    return 0;
                }
                else
                {
                    return 1;
                }
            }
        }
    }
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private List<Updatable> updatables;
    private List<Renderable> renderables;
    private List<SpriteRenderable> billboardedRenderables;
    private List<Stick> stickList;
    private List<Fire> fires;
    private List<GameObject> removeObjects;
    TiledMapTileLayer trees, rocks;
    float tileSize;
    public World(TiledMap map)
    {
        World.manager = new TweenManager();
        this.map = map;
        renderer = new OrthogonalTiledMapRenderer(map);
        updatables = new LinkedList<Updatable>();
        renderables = new LinkedList<Renderable>();
        billboardedRenderables = new ArrayList<SpriteRenderable>();
        stickList = new LinkedList<Stick>();
        fires = new LinkedList<Fire>();
        removeObjects = new LinkedList<GameObject>();

        //Set up trees
        trees = (TiledMapTileLayer)map.getLayers().get("Trees");
        rocks = (TiledMapTileLayer)map.getLayers().get("Rocks");
        rocks.setVisible(false);
        trees.setVisible(false);
        Texture treeTexture = new Texture("art/sprites/summer_pine_tree_tiles.png");
        Texture rockTexture = new Texture("art/sprites/PNG/16px/boulder.png");
        tileSize = trees.getTileWidth();
        for(int tx = 0; tx < trees.getWidth(); tx++)
        {
            for(int ty = 0; ty < trees.getHeight(); ty++)
            {
                if(trees.getCell(tx,ty) != null)
                {
                    SpriteRenderable treeRenderable = new SpriteRenderable(new Sprite(treeTexture));

                    treeRenderable.setPosition(tx * trees.getTileWidth(),ty * trees.getTileHeight());
                    treeRenderable.getSprite().setScale(3);
                    treeRenderable.getSprite().setOrigin(31/2,8);

                    billboardedRenderables.add(treeRenderable);
                }
            }
        }
        for(int tx = 0; tx < rocks.getWidth(); tx++)
        {
            for(int ty = 0; ty < rocks.getHeight(); ty++)
            {
                if(rocks.getCell(tx,ty) != null)
                {
                    SpriteRenderable rockRenderable = new SpriteRenderable(new Sprite(rockTexture));

                    rockRenderable.setPosition(tx * trees.getTileWidth(),
                            ty * trees.getTileHeight());
                    rockRenderable.getSprite().setScale(1.0f);
                    rockRenderable.getSprite().setOrigin(rockTexture.getWidth()/2,rockTexture.getHeight()/2);

                    billboardedRenderables.add(rockRenderable);
                }
            }
        }
        MapLayer sticks = map.getLayers().get("Sticks");
        MapObjects obs = sticks.getObjects();

        Texture stickTexture = new Texture("art/sprites/Stick.png");
        for(int i = 0; i < obs.getCount(); i++)
        {
            Sprite stickSprite = new Sprite(stickTexture);
            stickSprite.setPosition((Float) obs.get(i).getProperties().get("x"), (Float) obs.get(i).getProperties().get("y"));

            Stick s = new Stick(stickSprite);
            addGameObject(s);
        }




    }

    TiledMap getMap()
    {
        return map;
    }

    List<Fire> getFire()
    {
        return fires;
    }


    void render(GameCamera camera, SpriteBatch batch)
    {

        //Render Bottom Layer of TILED
        //Render outside bounds for rotation
        float width = camera.viewportWidth * camera.zoom * 2;
        float height = camera.viewportHeight * camera.zoom * 2;

        renderer.setView(
                camera.combined,
                camera.position.x - width / 2, camera.position.y - height / 2
                ,width,height);

        renderer.render();


        batch.setProjectionMatrix(camera.projection.mul(camera.view));
        batch.begin();


        //Sort billboarded sprites

        for(SpriteRenderable r : billboardedRenderables)
        {
            r.setRotation(-camera.getRotation());
            r.render(batch);
        }
        batch.end();



        //Render top layer of TILED

        //Render snow particles/Snowstorm

    }
    void update(GameCamera camera, float dt)
    {
        for(Updatable u : updatables)
        {
            u.update(dt);
        }
        BillboardComarator cmp = new BillboardComarator(camera.view);
        Collections.sort(billboardedRenderables,cmp);

        World.manager.update(dt);
        removeGameObjects(); //Clear any removal lists
    }
    void addGameObject(GameObject object)
    {
        billboardedRenderables.add(object);
        updatables.add(object);
        if(object instanceof Stick)
        {
            stickList.add((Stick) object);
        }
        if(object instanceof Fire)
        {
            fires.add((Fire) object);
        }

    }

    void removeGameObject(GameObject object)
    {
        removeObjects.add(object);
    }

    void removeGameObjects()
    {
        for(GameObject object : removeObjects) {
            billboardedRenderables.remove(object);
            updatables.remove(object);
            if (object instanceof Stick) {
                stickList.remove(object);
            }
        }
        removeObjects.clear();
    }

    List<Stick> getStickList()
    {
        return stickList;
    }

    void addRenderable(Renderable r)
    {
        renderables.add(r);
    }
    void addUpdateable(Updatable u)
    {
        updatables.add(u);
    }

    int getSign(float t)
    {
        if(t < 0)
            return -1;
        else if(t == 0)
            return  0;
        else
            return 1;
    }
    //
    void checkCollision(Player player, Vector2  direction)
    {
        //Check X direction if it enters new cell


        float w = player.getSprite().getWidth()  *  getSign(direction.x);
        float h = player.getSprite().getHeight() *  getSign(direction.y);

        int cellInitialX = (int)(Math.round(player.getSprite().getX()/tileSize));
        int cellInitialY = (int)(Math.round(player.getSprite().getY() / tileSize));

        int cellFinalX = (int)Math.round((player.getSprite().getX() + direction.x + w/2)/tileSize);
        int cellFinalY = (int)Math.round((player.getSprite().getY() + direction.y + h / 2) / tileSize);

        if(trees.getCell(cellFinalX,cellInitialY)!=null || rocks.getCell(cellFinalX,cellInitialY) != null)
        {
            direction.x = 0;
        }
        if(trees.getCell(cellInitialX,cellFinalY) !=null || rocks.getCell(cellInitialX,cellFinalY) != null)
        {
            direction.y = 0;
        }




        //Move offset based off of sign of movePosition
    }

    void sortFire(float playerX, float playerY)
    {
        Collections.sort(fires,new FireComarator(playerX,playerY));
    }


}
