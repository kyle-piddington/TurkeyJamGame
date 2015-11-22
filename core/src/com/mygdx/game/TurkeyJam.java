package com.mygdx.game;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
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
	Sound windAmbient;
	Sound fireAmbient;
	Music fireMusic;

	float mapHeight, mapWidth;

	long windID, fireID;

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
		player = new Player(new Texture("art/sprites/PlayerChar.png"));
		player.setSpeed(0.75f);
		testStick = new Stick(new Sprite(new Texture("art/sprites/Stick.png")));
        world.addGameObject(player);
		world.addGameObject(testStick);
		camera = new GameCamera();
		camera.setToOrtho(false, w, h);
        player.setPosition(11*64,64*64 - 9*64);
		camera.zoom = 1.f;
		camera.update();
        world.addGameObject(new Fire(11*64,64*64 - 8*64));

		fireMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/ambient_fire_music.wav"));
		fireAmbient = Gdx.audio.newSound(Gdx.files.internal("sound/fire_sound.wav"));
		windAmbient = Gdx.audio.newSound(Gdx.files.internal("sound/wind_sound.wav"));

		windID = windAmbient.loop(0.2f);

		fireID = fireAmbient.loop(0f);

		fireMusic.play();
		fireMusic.setLooping(true);
		fireMusic.setVolume(0f);

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
		healPlayer();
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
		player.freezeSlowdown();
        Vector2 vel = new Vector2();
        if(moveLeft)
        {
           vel.x = -32.f;
        }
        if(moveRight)
        {
            vel.x = 32.f;
        }
        if(moveUp)
        {
            vel.y = 32.f;
        }
        if(moveDown)
        {
            vel.y = -32.f;
        }
        vel.nor();
        world.checkCollision(player,vel);
        vel.nor();
        player.move(vel.x * player.getSpeed(),vel.y * player.getSpeed(),mapWidth-player.getSprite().getWidth(),mapHeight-player.getSprite().getHeight());

	}

	public void healPlayer()
	{
		float distance;
		float volume;

		if(world.getFire() == null) {

			distance = 0;
			//fireAmbient.stop();
		}
		else {
			float tempX = (player.getX() - world.getFire().get(0).getX());
			float tempY = (player.getY() - world.getFire().get(0).getY());

			distance = (float) Math.sqrt((tempX * tempX) + (tempY * tempY));
		}
		player.fireWarm(distance);

		volume = (distance <= 500f) ? 1f - (distance % 500f)*0.001f : 0f;

		fireMusic.setVolume(volume * 0.2f);
		windAmbient.setVolume(windID, 0.3f - volume);
		fireAmbient.setVolume(fireID, volume);
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
