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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.game.test.TestGameObject;

public class TurkeyJam extends ApplicationAdapter implements InputProcessor{
	//SpriteBatch batch;
	Texture img;
    World world;
	GameCamera camera;
	SpriteBatch spriteBatch;
	Player player;

    @Override
	public void create () {
		spriteBatch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
        world = new World(new TmxMapLoader().load("maps/debug-map.tmx"));
		player = new Player(new Sprite(new Texture("PlayerPlaceholder.png")));
        world.addGameObject(player);
		camera = new GameCamera();
		camera.setToOrtho(false,w,h);
        //camera.translate(11*64,64*64 - 10*64);
		camera.zoom = 1.f;
		camera.update();


		Gdx.input.setInputProcessor(this);
	}


	@Override
	public void render (){
		camera.position.x = player.getSprite().getX() + player.getSprite().getOriginX();
		camera.position.y = player.getSprite().getY() + player.getSprite().getOriginY();
		camera.update();
		world.update(camera, 1/30.0f);
        Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.render(camera,spriteBatch);
    }

	@Override
	public boolean keyDown(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		switch (keycode)
		{
			case Input.Keys.LEFT:
				//camera.translate(-32,0);
				player.move(-32,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
				break;
			case Input.Keys.RIGHT:
				//camera.translate(32,0);
				player.move(32,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
				break;
			case Input.Keys.UP:
				//camera.translate(0,-32);
				player.move(0,-32,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
				break;
			case Input.Keys.DOWN:
				//camera.translate(0,32);
				player.move(0,32,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
				break;
			case Input.Keys.SPACE:
				for(Stick stick : world.getStickList()) {
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
