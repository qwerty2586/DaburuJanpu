package cz.zcu.qwerty2.daburujanpu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import cz.zcu.qwerty2.daburujanpu.DaburuJanpu;
import cz.zcu.qwerty2.daburujanpu.data.GamePreferences;


public class SettingsScreen implements Screen {


    DaburuJanpu game;
    OrthographicCamera camera;
    Table table;
    TextButton saveButton;
    TextButton backButton;
    TextField nameText;
    TextField adressText;
    TextField portText;
    Stage stage;

    public SettingsScreen(final DaburuJanpu game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);


        nameText = new TextField(GamePreferences.getPrefPlayerName(),game.skin);
        nameText.setAlignment(Align.center);
        adressText = new TextField(GamePreferences.getPrefServerAddress(),game.skin);
        adressText.setAlignment(Align.center);
        portText = new TextField(""+GamePreferences.getPrefPort(),game.skin);
        portText.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        portText.setAlignment(Align.center);


        saveButton = new TextButton("Save", game.skin);
        saveButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GamePreferences.setPrefPlayerName(nameText.getText());
                GamePreferences.setPrefServerAddress(adressText.getText());
                int port = GamePreferences.stringToPort(portText.getText());
                GamePreferences.setPrefPort(port);
                portText.setText(""+port);
            }
        });
        backButton = new TextButton("Back", game.skin);
        backButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new MainMenuScreen(game));
            }
        });



        table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        table.setSkin(game.skin);
        table.add("Name").uniform().fill();
        table.add(nameText).uniform().fill();
        table.row();
        table.add("Server Adress").fill();
        table.add(adressText).fill();
        table.row();
        table.add("Port").fill().spaceBottom(30);
        table.add(portText).fill().spaceBottom(30);
        table.row();
        table.add(backButton).uniform().fill();
        table.add(saveButton).uniform().fill();
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

