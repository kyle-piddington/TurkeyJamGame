package com.mygdx.game;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.game.TweenWrappers.SpriteAccessor;
import com.mygdx.game.test.TestGameObject;

import java.util.ArrayList;

public class TurkeyJam extends ApplicationAdapter implements InputProcessor{
	//SpriteBatch batch;
	Texture img;
    World world;
	GameCamera camera;
	SpriteBatch spriteBatch;
	Player player;
	Stick testStick;

	float mapHeight, mapWidth;

	boolean moveLeft,moveRight,moveUp,moveDown;



    @Override
	public void create () {
        Tween.registerAccessor(Sprite.class,new SpriteAccessor());
		spriteBatch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
        world = new World(new TmxMapLoader().load("maps/debug-map.tmx"));
		TiledMapTileLayer temp = (TiledMapTileLayer) world.getMap().getLayers().get(0);
		mapHeight = temp.getHeight() * temp.getTileHeight();
		mapWidth = temp.getWidth() * temp.getTileWidth();
		player = new Player(new Sprite(new Texture("art/sprites/PlayerPlaceholder.png")));
		testStick = new Stick(new Sprite(new Texture("art/sprites/Stick.png")));
        world.addGameObject(player);
		world.addGameObject(testStick);
		camera = new GameCamera();
		camera.setToOrtho(false, w, h);
        player.setPosition(11*64,64*64 - 9*64);
		camera.zoom = 1.f;
		camera.update();
        world.addGameObject(new Fire(11*64,64*64 - 8*64));

		Gdx.input.setInputProcessor(this);
	}


	@Override
	public void render (){
		camera.position.x = player.getSprite().getX() + player.getSprite().getOriginX();
		camera.position.y = player.getSprite().getY() + player.getSprite().getOriginY();
		camera.update();
		world.update(camera, Gdx.graphics.getDeltaTime());


        Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.render(camera,spriteBatch);
		movePlayer();
    }

	@Override
	public boolean keyDown(int keycode) //holding
	{
		switch (keycode)
		{
			case Input.Keys.LEFT:
				//camera.translate(-32,0);
				moveLeft = true;
				break;
			case Input.Keys.RIGHT:
				//camera.translate(32,0);
				moveRight = true;
				break;
			case Input.Keys.UP:
				//camera.translate(0,-32);
				moveUp = true;
				break;
			case Input.Keys.DOWN:
				//camera.translate(0,32);
				moveDown = true;
				break;
			case Input.Keys.SPACE:

				for (Stick stick : world.getStickList()) {
					if (player.pickUpStick(stick))
						world.removeGameObject(stick);
				}
			case Input.Keys.NUM_1:
				//tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
				break;
			case Input.Keys.NUM_2:
				//tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
				break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) //releasing
 	{
		switch (keycode)
		{
			case Input.Keys.LEFT:
				//camera.translate(-32,0);
				moveLeft = false;
				break;
			case Input.Keys.RIGHT:
				//camera.translate(32,0);
				moveRight = false;
				break;
			case Input.Keys.UP:
				//camera.translate(0,-32);
				moveUp = false;
				break;
			case Input.Keys.DOWN:
				//camera.translate(0,32);
				moveDown = false;
				break;
			case Input.Keys.SPACE:
                break;
			case Input.Keys.NUM_1:
				//tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
				break;
			case Input.Keys.NUM_2:
				//tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
				break;
		}
		return false;
	}

	public void movePlayer()
	{
		if(moveLeft)
			player.move(-32,0,mapHeight - player.getSprite().getHeight(),mapWidth - player.getSprite().getWidth());
		if(moveRight)
			player.move(32,0,mapHeight - player.getSprite().getHeight(),mapWidth - player.getSprite().getWidth());
		if(moveUp)
			player.move(0,32,mapHeight - player.getSprite().getHeight(),mapWidth - player.getSprite().getWidth());
		if(moveDown)
			player.move(0,-32,mapHeight - player.getSprite().getHeight(),mapWidth - player.getSprite().getWidth());

	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int point)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}
}
