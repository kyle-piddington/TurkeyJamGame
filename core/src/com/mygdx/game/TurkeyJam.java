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
	OrthographicCamera camera;
	SpriteBatch spriteBatch;
    @Override
	public void create () {
		spriteBatch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
        world = new World(new TmxMapLoader().load("maps/rpg_test_map.tmx"));
        world.addGameObject(new TestGameObject(new Sprite(new Texture("badlogic.jpg"))));
		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.update();


		Gdx.input.setInputProcessor(this);
	}


	@Override
	public void render () {
        camera.update();
		world.update(1/30.0f);
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
				camera.translate(-32,0);
				break;
			case Input.Keys.RIGHT:
				camera.translate(32,0);
				break;
			case Input.Keys.UP:
				camera.translate(0,-32);
				break;
			case Input.Keys.DOWN:
				camera.translate(0,32);
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
