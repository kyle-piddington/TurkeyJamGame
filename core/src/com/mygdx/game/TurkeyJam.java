package com.mygdx.game;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.TweenWrappers.SpriteAccessor;

public class TurkeyJam extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	TweenManager manager = new TweenManager();
    Sprite sprite = new Sprite(new Texture("badlogic.jpg"));
	@Override
	public void create () {
		batch = new SpriteBatch();

    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        sprite.draw(batch);
		batch.end();
        manager.update(1/60.0f);
	}
}
