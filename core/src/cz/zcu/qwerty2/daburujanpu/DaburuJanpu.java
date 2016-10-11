package cz.zcu.qwerty2.daburujanpu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import cz.zcu.qwerty2.daburujanpu.screens.MainMenuScreen;

public class DaburuJanpu extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
		/*
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		System.out.println("width: "+Gdx.graphics.getWidth()+" height: "+Gdx.graphics.getHeight());
		batch.draw(img, 0, 0,640,480);
		bitmapFont.draw(batch,""+Gdx.graphics.getFramesPerSecond(),20,20);
		batch.end();
		*/

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
