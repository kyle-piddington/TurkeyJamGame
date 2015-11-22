package com.mygdx.game;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.TweenWrappers.SpriteAccessor;
import com.mygdx.game.test.TestGameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
    BlizzardMask blizMask;
	boolean gameOver;
	GoalMap target;
	float mapHeight, mapWidth;
    float blizzardtimer = 10.0f;
	long windID, fireID;
    Random rand = new Random();
	boolean moveLeft,moveRight,moveUp,moveDown;
    private CameraDirection camDir = CameraDirection.NORTH;
    private FireUIElement fireUI;

    private boolean blizzardCalmed = false;
    private  static final float BLIZZARD_MIN =  20.f;
    private static final float BLIZZARD_RANGE = 60.f;
    enum CameraDirection
    {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }


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
		target = new GoalMap(new Sprite(new Texture("art/sprites/map.png")));
        world.addGameObject(player);
		world.addGameObject(testStick);
		world.addGameObject(target);

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

        fireUI = new FireUIElement();
		Gdx.input.setInputProcessor(this);
        blizMask = new BlizzardMask();
	}


	@Override
	public void render (){

		camera.position.x = player.getSprite().getX() + player.getSprite().getOriginX();
		camera.position.y = player.getSprite().getY() + player.getSprite().getOriginY();
		camera.update();
        //camera.rotate(0.25f);
		world.update(camera, Gdx.graphics.getDeltaTime());


        Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Blizzard
        blizMask.update(Gdx.graphics.getDeltaTime());
        world.render(camera, spriteBatch);

        //Draw UI
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        fireUI.move(player.getX(),player.getY());
        fireUI.draw(spriteBatch);
        fireUI.updatePercent(0.0f);
        spriteBatch.end();

        spriteBatch.setProjectionMatrix(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()).combined);
        spriteBatch.begin();

        blizMask.draw(spriteBatch,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        spriteBatch.end();

        blizzardtimer -= Gdx.graphics.getDeltaTime();
        if(blizzardtimer <= 0)
        {
           if(blizzardtimer > -3)
           {
              blizMask.setIntensity(1.0f);
           }
           else if(!blizzardCalmed)
           {
               setRandCamDir();
               blizzardCalmed = true;

           }
           else
           {
               blizMask.setIntensity(0.5f);
               blizzardtimer = rand.nextFloat() * BLIZZARD_RANGE + BLIZZARD_MIN;
           }

        }
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
                break;
			case Input.Keys.NUM_1:
				world.addGameObject(new Fire(player.getX(),player.getY()));
                break;
			case Input.Keys.NUM_2:
				//tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
				break;
            case Input.Keys.NUM_3:
                setRandCamDir();
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
				player.lightTorch();
				break;
		}
		return false;
	}

    void setRandCamDir()
    {
       
        switch (rand.nextInt(4))
        {
            case 0:
                camera.setRotation(0f);
                camDir = CameraDirection.NORTH;
                break;
            case 1:
                camera.setRotation(90f);
                camDir = CameraDirection.EAST;
                break;
            case 2:
                camera.setRotation(180f);
                camDir = CameraDirection.SOUTH;
                break;
            case 4:
                camera.setRotation(270f);
                camDir = CameraDirection.WEST;
                break;
        }
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
        Vector2 directionalVel = new Vector2();

        switch (camDir)
        {
            case NORTH:
                directionalVel.x = vel.x;
                directionalVel.y = vel.y;
                break;
            case EAST:
                directionalVel.x = vel.y;
                directionalVel.y = -vel.x;
                break;
            case SOUTH:
                directionalVel.x = -vel.x;
                directionalVel.y = -vel.y;
                break;
            case WEST:
                directionalVel.x = -vel.y;
                directionalVel.y = vel.x;
                break;
        }
        directionalVel.nor();
        world.checkCollision(player,directionalVel);
        directionalVel.nor();
        player.move(directionalVel.x * player.getSpeed(),directionalVel.y * player.getSpeed(),mapWidth-player.getSprite().getWidth(),mapHeight-player.getSprite().getHeight());
        player.moveAnim(vel.x,vel.y);
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
            world.sortFire(player.getX(),player.getY());
            Fire nearestFire = world.getFire().get(0);
			float tempX = (player.getX() - nearestFire.getX());
			float tempY = (player.getY() - nearestFire.getY());

			distance = (float) Math.sqrt((tempX * tempX) + (tempY * tempY));
		    if(nearestFire.isExtinguished())
                distance = 100000;
            if(distance < 128)
            {

            }
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
