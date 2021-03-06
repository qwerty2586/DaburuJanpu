package cz.zcu.qwerty2.daburujanpu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;

import cz.zcu.qwerty2.daburujanpu.DaburuJanpu;
import cz.zcu.qwerty2.daburujanpu.net.Command;


public class LoadingScreen implements Screen {
    public static final int SITUATION_MAIN_TO_SERVER = 0;
    public static final int SITUATION_SERVER_TO_MAIN = 1; // tohle se nepouzije
    public static final int SITUATION_SERVER_TO_LOBBY_JOIN = 2;
    public static final int SITUATION_SERVER_TO_LOBBY_CREATE = 3;
    public static final int SITUATION_LOBBY_TO_SERVER = 4;
    public static final int SITUATION_LOBBY_TO_GAME = 5;
    public static final int SITUATION_GAME_TO_SERVER = 6;
    public static final int SITUATION_DISCONNECTED = 7;

    private static final String[] DEFAULT_TEXT = {
            "Connecting to server...",
            "Disconnecting from server...",
            "Joining game...",
            "Creating game...",
            "Leaving game...",
            "Starting...",
            "Exiting...",
            "Disconnect..."
    };

    private static final String[] FAIL_TEXT = {
            "Cant connect to server",
            "",
            "Unable to join lobby.",
            "There is already a lobby with that name!",
            "",
            "",
            "",
            "Disconnect..."
    };



    private static final int[] EXIT_RESULT_COMMAND = {
            Command.RESULT_CONNECT,
            -100,
            Command.RESULT_JOIN_LOBBY,
            Command.RESULT_CREATE_LOBBY


    };

    private final FillViewport viewport;
    private Stage stage;
    final private DaburuJanpu game;
    private Label label;
    private int situation;
    private TextButton closeButton;
    private Table table;

    public LoadingScreen(final DaburuJanpu game, int situation, Command sendCommand) {
        this.game = game;
        this.situation = situation;

        OrthographicCamera camera = new OrthographicCamera(640,480);
        viewport = new FillViewport(640,480,camera);


        label = new Label(DEFAULT_TEXT[situation], game.skin);
        table = new Table(game.skin);
        table.setFillParent(true);
        table.add(label).fill();
        closeButton = new TextButton("Close", game.skin);
        closeButton.setVisible(false);
        if (situation==SITUATION_DISCONNECTED) {
            fallback(DEFAULT_TEXT[situation],new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    game.setScreen(new MainMenuScreen(game));
                }
            }) ;
        }
        table.row();
        table.add(closeButton).center();

        stage = new Stage(viewport) {
            @Override
            public boolean keyUp(int keyCode) {
                if (keyCode== Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
                    InputEvent event = new InputEvent();
                    event.setType(InputEvent.Type.touchDown);
                    closeButton.fire(event);
                    event.setType(InputEvent.Type.touchUp);
                    closeButton.fire(event);
                }
                return super.keyUp(keyCode);
            }
        };

        if (sendCommand!=null) game.commandQueue.add(sendCommand);
    }

    @Override
    public void show() {
        stage.clear();
        label.setPosition(Gdx.graphics.getWidth() / 2f - label.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - label.getHeight() / 2f);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        // deme  checknout jesli nam neco neprislo

        if (!game.resultQueue.isEmpty()) {
            Command n = game.resultQueue.remove();
            if (n.code == EXIT_RESULT_COMMAND[situation]) {
                if (Integer.valueOf(n.args.get(0)) == Command.SUCCESS) {
                    switch (situation) {
                        case SITUATION_MAIN_TO_SERVER:
                            game.me.id = game.netService.myId;
                            game.setScreen(new ServerScreen(game));
                            break;
                        case SITUATION_SERVER_TO_LOBBY_JOIN:
                        case SITUATION_SERVER_TO_LOBBY_CREATE:
                            game.setScreen(new LobbyScreen(game));
                            break;
                    }

                }
                if (Integer.valueOf(n.args.get(0)) == Command.FAIL) {
                    switch (situation) {
                        case SITUATION_MAIN_TO_SERVER:
                            fallback(FAIL_TEXT[situation], new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    super.clicked(event, x, y);
                                    game.setScreen(new MainMenuScreen(game));
                                }
                            });
                            break;
                        case SITUATION_SERVER_TO_LOBBY_JOIN:
                            fallback(FAIL_TEXT[situation]+" Reason: " + n.args.get(1), new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    super.clicked(event, x, y);
                                    game.setScreen(new ServerScreen(game));
                                }
                            });
                            break;

                        case SITUATION_SERVER_TO_LOBBY_CREATE:
                            fallback(FAIL_TEXT[situation], new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    super.clicked(event, x, y);
                                    game.setScreen(new ServerScreen(game));
                                }
                            });
                            break;
                        case Command.HW_DISCONNECTED:
                            game.setScreen(new LoadingScreen(game,LoadingScreen.SITUATION_DISCONNECTED,null));
                            break;

                    }
                }
            }
        }
    }

    private void fallback(String message, ClickListener clickListener) {
        label.setText(message);
        closeButton.addListener(clickListener);
        closeButton.setVisible(true);
    }

    @Override
    public void resize(int width, int height) {
        //label.setPosition(Gdx.graphics.getWidth() / 2f - label.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - label.getHeight() / 2f);
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
