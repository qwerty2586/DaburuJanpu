package cz.zcu.qwerty2.daburujanpu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import cz.zcu.qwerty2.daburujanpu.screens.SplashScreen;

public class DaburuJanpu extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Skin skin;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
		skin = new Skin(skinFile);
		setScreen(new SplashScreen(this));
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
