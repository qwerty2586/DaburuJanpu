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
import com.badlogic.gdx.utils.viewport.FillViewport;

import cz.zcu.qwerty2.daburujanpu.DaburuJanpu;
import cz.zcu.qwerty2.daburujanpu.net.Command;

public class MainMenuScreen implements Screen {


    private DaburuJanpu game;
    private Table table;
    private final FillViewport viewport;
    private Stage stage;

    public MainMenuScreen(final DaburuJanpu game) {
        this.game = game;

        OrthographicCamera camera = new OrthographicCamera(640,480);
        viewport = new FillViewport(640,480,camera);
        TextButton singlePlayerButton = new TextButton("Single player", game.skin);
        TextButton multiPlayerButton = new TextButton("Multiplayer player", game.skin);
        TextButton settingsButton = new TextButton("Settings", game.skin);

        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new GameScreen(game,GameScreen.SINGLE_PLAYER));
            }
        });

        multiPlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new LoadingScreen(
                        game,
                        LoadingScreen.SITUATION_MAIN_TO_SERVER,
                        new Command(Command.CONNECT_TO_SERVER)

                ));

            }
        });

        settingsButton.addListener(new ClickListener() {
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


        stage = new Stage(viewport) {
            @Override
            public boolean keyUp(int keyCode) {
                if (keyCode== Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                }
                return super.keyUp(keyCode);
            }
        };


    }


    @Override
    public void show() {
        stage.clear();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
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
