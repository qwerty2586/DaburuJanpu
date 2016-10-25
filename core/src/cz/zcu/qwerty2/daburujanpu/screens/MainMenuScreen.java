package cz.zcu.qwerty2.daburujanpu.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import cz.zcu.qwerty2.daburujanpu.DaburuJanpu;
import cz.zcu.qwerty2.daburujanpu.net.NetCommand;

public class MainMenuScreen implements Screen {


    DaburuJanpu game;
    OrthographicCamera camera;
    Table table;
    TextButton singlePlayerButton;
    TextButton multiPlayerButton;
    TextButton settingsButton;
    Stage stage;

    public MainMenuScreen(final DaburuJanpu game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        singlePlayerButton = new TextButton("Single player", game.skin);
        multiPlayerButton = new TextButton("Multiplayer player", game.skin);
        settingsButton = new TextButton("Settings", game.skin);

        singlePlayerButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new GameScreen(game));
            }
        });

        multiPlayerButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.commandQueue.add(new NetCommand(NetCommand.CONNECT_TO_SERVER,null));

            }
        });

        settingsButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new SettingsScreen(game));
            }
        });



        table = new Table();
        table.setFillParent(true);
        table.add(singlePlayerButton).size( 300, 60 ).uniform().fill();
        table.row();
        table.add(multiPlayerButton).uniform().fill();
        table.row();
        table.add(settingsButton).uniform().fill();

        stage = new Stage();


    }


    @Override
    public void show() {
        stage.clear();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
