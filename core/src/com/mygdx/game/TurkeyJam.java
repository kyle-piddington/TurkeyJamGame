package com.mygdx.game;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
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
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.TweenWrappers.SpriteAccessor;

import java.util.ArrayList;
import java.util.Random;

public class TurkeyJam extends ApplicationAdapter implements InputProcessor, ActionCallback{
	//SpriteBatch batch;
	Texture img;
    World world;
	GameCamera camera;
	SpriteBatch spriteBatch;
	Player player;
	//Stick testStick;
	Sound windAmbient;
	Sound fireAmbient;
	Music fastSteps;
	Music medSteps;
	Music slowSteps;
	Music fireMusic;
    BlizzardMask blizMask;
	GoalMap target;
	float mapHeight, mapWidth;
    float blizzardtimer = 30.0f;
	long windID, fireID;
    Random rand = new Random(System.currentTimeMillis());
	boolean moveLeft,moveRight,moveUp,moveDown;
    private CameraDirection camDir = CameraDirection.NORTH;
    private FireUIElement fireUI;
	private int currSpeed;

	SpriteBatch endGameBatch;
	Sprite winGame;
	Sprite loseGame;

    private boolean blizzardCalmed = false;
    private  static final float BLIZZARD_MIN =  20.f;
    private static final float BLIZZARD_RANGE = 30.f;

    @Override
    public void send(ActionTile.ACTIONS action) {
        switch (action)
        {
            case GRAB:
                for (Stick stick : world.getStickList()) {
                    if (player.pickUpStick(stick)) {
                        world.removeGameObject(stick);
                        gameGui.updateBranchCount(player.getSticks());
                        break;
                    }
            }
            break;
            case TORCH:
                player.lightTorch();
                gameGui.updateBranchCount(player.getSticks());
                gameGui.updateTinderCount(player.getTinderboxes());
                break;
            case LIGHT:

                player.useBranches(2);
                gameGui.updateBranchCount(player.getSticks());
                if(!player.getTorch().isLit())
                {
                    player.useTinderboxes(1);
                    gameGui.updateTinderCount(player.getTinderboxes());
                }
                world.addGameObject(new Fire(player.getX(),player.getY()));
                break;
            case MAPL:
                CameraDirection nextDirection = camDir;
                float nextAngle = 0;
                switch(camDir)
                {
                    case NORTH:
                        nextDirection = CameraDirection.WEST;
                        nextAngle = 270;
                        break;
                    case WEST:
                        nextDirection = CameraDirection.SOUTH;
                        nextAngle = 180;
                        break;
                    case SOUTH:
                        nextDirection = CameraDirection.EAST;
                        nextAngle = 90;
                        break;
                    case EAST:
                        nextDirection = CameraDirection.NORTH;
                        nextAngle = 0;
                        break;


                }
                camDir = nextDirection;
                if(camera.getRotation() == 0)
                {
                    camera.setRotation(360);
                }
                Tween.to(camera,CameraAccessor.ROT,0.25f)
                        .target(nextAngle)
                        .ease(TweenEquations.easeOutCubic)
                        .start(World.manager);
                break;
            case MAPR:
                CameraDirection nextRDirection = camDir;
                float nextRAngle = 0;
                switch(camDir)
                {
                    case NORTH:
                        nextRDirection = CameraDirection.EAST;
                        nextRAngle = 90;
                        break;
                    case EAST:
                        nextRDirection = CameraDirection.SOUTH;
                        nextRAngle = 180;
                        break;
                    case SOUTH:
                        nextRDirection = CameraDirection.WEST;
                        nextRAngle = 270;
                        break;
                    case WEST:
                        nextRDirection = CameraDirection.NORTH;
                        nextRAngle = 0;
                        break;


                }
                camDir = nextRDirection;
                if(camera.getRotation() == 270)
                {
                    camera.setRotation(-90);
                }
                Tween.to(camera,CameraAccessor.ROT,0.25f)
                        .target(nextRAngle)
                        .ease(TweenEquations.easeOutCubic)
                        .start(World.manager);
                //Do nothing

        }
    }

    enum CameraDirection
    {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    GUI gameGui;
    @Override
	public void create () {
        Tween.registerAccessor(Sprite.class,new SpriteAccessor());
        Tween.registerAccessor(GameCamera.class,new CameraAccessor());

        spriteBatch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
        world = new World(new TmxMapLoader().load("maps/debug-map.tmx"));
		TiledMapTileLayer temp = (TiledMapTileLayer) world.getMap().getLayers().get(0);
		mapHeight = temp.getHeight() * temp.getTileHeight();
		mapWidth = temp.getWidth() * temp.getTileWidth();
		player = new Player(new Texture("art/sprites/PlayerChar.png"), 8*64, 11*64);
		//player.setSpeed(0.75f);
		//testStick = new Stick(new Sprite(new Texture("art/sprites/Stick.png")));
		target = new GoalMap(11*64,64*64 - 9*64);
		//target = new GoalMap(10 * 64, 8 * 64);
        world.addGameObject(player);
		//world.addGameObject(testStick);
		world.addGameObject(target);

		camera = new GameCamera();
		camera.setToOrtho(false, w, h);

        player.setPosition(8*64, 11*64);

        camera.zoom = 1.f;
		camera.update();
        world.addGameObject(new Fire(7*64,10*64));

		fireMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/ambient_fire_music.wav"));
		fireAmbient = Gdx.audio.newSound(Gdx.files.internal("sound/fire_sound.wav"));
		windAmbient = Gdx.audio.newSound(Gdx.files.internal("sound/wind_sound.wav"));

		fastSteps = Gdx.audio.newMusic(Gdx.files.internal("sound/footsteps_fast.wav"));
		medSteps = Gdx.audio.newMusic(Gdx.files.internal("sound/footsteps_med.wav"));
		slowSteps = Gdx.audio.newMusic(Gdx.files.internal("sound/footsteps_slow.wav"));

		windID = windAmbient.loop(0.2f);
		fireID = fireAmbient.loop(0f);

		fireMusic.play();
		fireMusic.setLooping(true);
		fireMusic.setVolume(0f);

		fastSteps.play();
		fastSteps.setLooping(true);
		fastSteps.setVolume(0f);

		medSteps.play();
		medSteps.setLooping(true);
		medSteps.setVolume(0f);

		slowSteps.play();
		slowSteps.setLooping(true);
		slowSteps.setVolume(0f);

        fireUI = new FireUIElement();
		Gdx.input.setInputProcessor(this);
        blizMask = new BlizzardMask();

		endGameBatch = new SpriteBatch();
		winGame = new Sprite(new Texture("art/sprites/WinScreen.png"));
		loseGame = new Sprite(new Texture("art/sprites/DeathScreen.png"));

        gameGui = new GUI(this);
	}


	@Override
	public void render () {

		if (!player.returnedHome() && player.getLifeStatus()) {
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
			fireUI.draw(spriteBatch);
			spriteBatch.end();

			spriteBatch.setProjectionMatrix(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()).combined);
			spriteBatch.begin();

			blizMask.draw(spriteBatch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			gameGui.draw(spriteBatch);
            spriteBatch.end();

			blizzardtimer -= Gdx.graphics.getDeltaTime();
			if (blizzardtimer <= 0) {
				if (blizzardtimer > -3) {
					blizMask.setIntensity(1.0f);
				} else if (!blizzardCalmed) {
					setRandCamDir();
					blizzardCalmed = true;

				} else {
					blizMask.setIntensity(0.5f);
					blizzardtimer = rand.nextFloat() * BLIZZARD_RANGE + BLIZZARD_MIN;
                    blizzardCalmed = false;
				}


			}
            updateGUI();
			movePlayer();
			healPlayer();
			player.overMap(target);

			if(player.foundMap())
            {
                gameGui.activateTurnTiles();
                world.removeGameObject(target);

            }

		}
		else if(!player.getLifeStatus())
		{
			slowSteps.dispose();
			medSteps.dispose();
			fastSteps.dispose();
			fireMusic.dispose();
			fireAmbient.dispose();

			endGameBatch.begin();
			loseGame.draw(endGameBatch);
			endGameBatch.end();
		}
		else if(player.returnedHome())
		{
			slowSteps.dispose();
			medSteps.dispose();
			fastSteps.dispose();
			fireMusic.dispose();
			fireAmbient.dispose();
			windAmbient.dispose();

			endGameBatch.begin();
			winGame.draw(endGameBatch);
			endGameBatch.end();
		}

    }

    private void updateGUI() {
        boolean grabActive = false;
        if(player.canGetStick()) {
            for (Stick stick : world.getStickList()) {
                if (player.overStick(stick)) {
                    grabActive = true;
                }
            }
        }
        gameGui.setGrabActive(grabActive);

        gameGui.setTorchActive(player.canLightTorch());

        gameGui.setFireActive(player.canLightFire());

        gameGui.updateTorchUI(player.getTorch().getLitPercent());
        gameGui.updateThermoGUI(player.getHeat());
        gameGui.update();
    }

    @Override
	public boolean keyDown(int keycode) //holding
	{
		switch (keycode)
		{
			case Input.Keys.LEFT:
				//camera.translate(-32,0);
				moveLeft = true;
				currSpeed = stepSpeed();
				break;
			case Input.Keys.RIGHT:
				//camera.translate(32,0);
				moveRight = true;
				currSpeed = stepSpeed();
				break;
			case Input.Keys.UP:
				//camera.translate(0,-32);
				moveUp = true;
				currSpeed = stepSpeed();
				break;
			case Input.Keys.DOWN:
				//camera.translate(0,32);
				moveDown = true;
				currSpeed = stepSpeed();
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
            case Input.Keys.ENTER:
                gameGui.closeJournal();
			case Input.Keys.NUM_1:
				//tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
				break;
			case Input.Keys.NUM_2:
				//player.lightTorch();
				break;
		}

		return false;
	}

    void setRandCamDir()
    {
        ArrayList<CameraDirection> directions = new ArrayList<CameraDirection>();
        directions.add( CameraDirection.NORTH);
        directions.add( CameraDirection.SOUTH);
        directions.add( CameraDirection.EAST);
        directions.add(CameraDirection.WEST);
        directions.remove(camDir);
        int rnd =  rand.nextInt(6);
        if(rnd < 2)
        {
            camDir = directions.get(0);
        }
        else if(rnd < 4)
        {
            camDir = directions.get(1);
        }
        else if(rnd < 6)
        {
            camDir = directions.get(2);
        }
        switch (camDir)
        {
            case NORTH:
                camera.setRotation(0f);
                break;
            case EAST:
                camera.setRotation(90f);
                break;
            case SOUTH:
                camera.setRotation(180f);
                break;
            case WEST:
                camera.setRotation(270f);
                break;
        }
    }

    public Vector2 camVector(Vector2 dir)
    {
        Vector2 directionalVel = new Vector2();

        switch (camDir)
        {
            case NORTH:
                directionalVel.x = dir.x;
                directionalVel.y = dir.y;
                break;
            case EAST:
                directionalVel.x = dir.y;
                directionalVel.y = -dir.x;
                break;
            case SOUTH:
                directionalVel.x = -dir.x;
                directionalVel.y = -dir.y;
                break;
            case WEST:
                directionalVel.x = -dir.y;
                directionalVel.y = dir.x;
                break;
        }
        return directionalVel;
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
		if(!moveLeft && !moveRight && !moveUp && !moveDown)
		{
			switch (currSpeed) {
				case 1: fastSteps.setVolume(0f);
					break;
				case 2: medSteps.setVolume(0f);
					break;
				case 3: slowSteps.setVolume(0f);
					break;
			}
		}
        Vector2 directionalVel = camVector(vel);
        directionalVel.nor();
        world.checkCollision(player,directionalVel);
        directionalVel.nor();
        player.move(directionalVel.x * player.getSpeed(),directionalVel.y * player.getSpeed(),mapWidth-player.getSprite().getWidth(),mapHeight-player.getSprite().getHeight());
        player.moveAnim(vel.x,vel.y);
	}

	public void healPlayer()
	{
		float distance;
		float volume, musicVolume;

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
            if(distance < Fire.FireDist)
            {

               fireUI.move(nearestFire.getX(),nearestFire.getY());
               fireUI.updatePercent(nearestFire.firePercent());
            }
            else
            {
                fireUI.hide();
            }
        }
		player.fireWarm(distance);

		volume = (distance <= 500f) ? 1f - (distance % 500) * 0.001f : 0f;
		volume = (volume > 0f) ? volume : 0f;
		musicVolume = (volume - 0.5f) > 0f ? volume - 0.5f : 0.1f;

		if(volume != 0f) {

			fireMusic.setVolume(musicVolume - 0.05f);
			windAmbient.setVolume(windID, 0.25f - Math.max(0f, Math.min(0.24f, volume / 5)));
			fireAmbient.setVolume(fireID, musicVolume);
		}
		else
			soundVolume();
	}

	private void soundVolume()
	{
		float heatLevel, volume;

		heatLevel = player.getHeat();
		volume = Math.min(1f, (100f - heatLevel) * 0.01f + 0.2f);

		fireMusic.setVolume(0f);
		fireAmbient.setVolume(fireID, 0f);
		windAmbient.setVolume(windID, volume);
	}

	private int stepSpeed()
	{
		float heatLevel;
		int currSpeed; // 1 = fast, 2 = med, 3 = slow

		heatLevel = player.getHeat();

		if(heatLevel >= 65f) {

			if(fastSteps.getVolume() == 0f)
				fastSteps.setVolume(1f);
			medSteps.setVolume(0f);
			currSpeed = 1;
		}
		else if(heatLevel >= 30f) {

			if(medSteps.getVolume() == 0f)
				medSteps.setVolume(1f);
			fastSteps.setVolume(0f);
			slowSteps.setVolume(0f);
			currSpeed = 2;
		}
		else {

			if(slowSteps.getVolume() == 0f)
				slowSteps.setVolume(1f);
			medSteps.setVolume(0f);
			currSpeed = 3;
		}

		return currSpeed;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
        gameGui.onClick();
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
