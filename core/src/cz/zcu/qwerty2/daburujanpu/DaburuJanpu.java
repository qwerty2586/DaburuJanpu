package cz.zcu.qwerty2.daburujanpu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import cz.zcu.qwerty2.daburujanpu.data.Player;
import cz.zcu.qwerty2.daburujanpu.net.CommandQueue;
import cz.zcu.qwerty2.daburujanpu.net.NetService;
import cz.zcu.qwerty2.daburujanpu.screens.MainMenuScreen;
import cz.zcu.qwerty2.daburujanpu.screens.SplashScreen;

public class DaburuJanpu extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Skin skin;
	public CommandQueue commandQueue,resultQueue;
	public Player me = new Player();
	public NetService netService;
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
		skin = new Skin(skinFile);
		commandQueue = new CommandQueue();
		resultQueue = new CommandQueue();
		netService = new NetService(commandQueue,resultQueue);
		new Thread(netService).start();
		setScreen(new SplashScreen(this));
		//setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		//batch.dispose();
		font.dispose();
		Gdx.app.exit();
	}
}
