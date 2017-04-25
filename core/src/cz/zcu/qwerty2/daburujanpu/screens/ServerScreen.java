package cz.zcu.qwerty2.daburujanpu.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;

import java.util.ArrayList;

import cz.zcu.qwerty2.daburujanpu.DaburuJanpu;
import cz.zcu.qwerty2.daburujanpu.data.GamePreferences;
import cz.zcu.qwerty2.daburujanpu.net.Command;

import static cz.zcu.qwerty2.daburujanpu.util.Consts.DEF_HEIGHT;
import static cz.zcu.qwerty2.daburujanpu.util.Consts.DEF_WIDTH;

public class ServerScreen implements Screen {
    private final FillViewport viewport;
    DaburuJanpu game;
    Table mainTable;
    List list;
    ScrollPane scrollPane;
    TextButton refreshButton, joinButton, createButton, backButton, serverStatsButton,reconnectButton;
    TextField lobbyNameTextField;
    Stage stage;
    ArrayList<Lobby> lobbyList = new ArrayList<Lobby>();

    Array<String> makeLabelArray(ArrayList<Lobby> lobbyList) {
        Array<String> arr = new Array<String>();
        for (int i = 0; i < lobbyList.size(); i++) {
            arr.add(lobbyList.get(i).toString());
        }
        return arr;
    }

    ;

    class Lobby {
        public int id;
        public String name;
        public int players;
        public int capacity;

        public Lobby(int id, String name, int players, int capacity) {
            this.id = id;
            this.name = name;
            this.players = players;
            this.capacity = capacity;
        }

        public String toString() {
            return name + " | " + players + "/" + capacity;
        }
    }


    public ServerScreen(DaburuJanpu game) {
        this.game = game;
        Array<String> x = makeLabelArray(lobbyList);
        list = new List(game.skin);
        list.setItems(x);
        list.getSelection().setMultiple(false);
        list.setSelectedIndex(-1);
        scrollPane = new ScrollPane(list, game.skin);
        refreshButton = new TextButton("Refresh", game.skin);
        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                sendRefresh();
            }
        });

        joinButton = new TextButton("Join", game.skin);
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                joinLobby();
            }
        });
        createButton = new TextButton("Create", game.skin);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                createLobby();
            }
        });
        backButton = new TextButton("Back", game.skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                backToMenu();
            }
        });
        serverStatsButton = new TextButton("Server stats",game.skin);
        serverStatsButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                askForStats();
            }
        });
        reconnectButton = new TextButton("Reconnect",game.skin);
        reconnectButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                reconnect();
            }
        });

        lobbyNameTextField = new TextField("", game.skin);
        lobbyNameTextField.setTextFieldFilter(new TextField.TextFieldFilter() {

            // filtruj zakazenej charakter ';'
            public boolean acceptChar(TextField textField, char c) {
                if (c == Command.DEFAULT_DELIMITER)
                    return false;
                return true;
            }
        });

        lobbyNameTextField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (lobbyNameTextField.getText().length() > 0)
                    createButton.setTouchable(Touchable.enabled);
                else createButton.setTouchable(Touchable.disabled);
            }
        });
        //createButton.setTouchable(Touchable.disabled);
        lobbyNameTextField.setText(GamePreferences.getPrefPlayerName()+"'s Game");

        list.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (list.getSelection().size() > 0) joinButton.setTouchable(Touchable.enabled);
                else joinButton.setTouchable(Touchable.disabled);
            }
        });
        joinButton.setTouchable(Touchable.disabled);


        mainTable = new Table(game.skin);
        mainTable.setFillParent(true);
        mainTable.add(scrollPane).size(400, 300).colspan(4);
        mainTable.row();
        mainTable.add(joinButton).uniform().fill();
        mainTable.add(refreshButton).uniform().fill();
        mainTable.add(lobbyNameTextField).fill();
        mainTable.add(createButton).uniform().fill();
        mainTable.row();
        mainTable.add(backButton).uniform().fill().spaceTop(30);
        mainTable.add(serverStatsButton).fill().spaceTop(30);
        mainTable.add(reconnectButton).fill().spaceTop(30);

        OrthographicCamera camera = new OrthographicCamera(DEF_WIDTH,DEF_HEIGHT);
        viewport = new FillViewport(DEF_WIDTH,DEF_HEIGHT,camera);
        stage = new Stage(viewport) {
            @Override
            public boolean keyUp(int keyCode) {
                if (keyCode== Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
                    backToMenu();
                }
                return super.keyUp(keyCode);
            }
        };

        String name = GamePreferences.getPrefPlayerName().replaceAll(";"," ");
        if (name.length()==0) name = " ";
        game.commandQueue.add(new Command(Command.SET_PLAYER_NAME).addArg(name));
        sendRefresh();

        reconnectButton.setVisible(false);
        askForReconnect();


    }

    private void askForReconnect() {
        int id = GamePreferences.getReconnectId();
        if (id!=GamePreferences.DEFAULT_RECONNECT_ID) {
            game.commandQueue.add(new Command(Command.GAME_RECONNECT_ASK).addArg(id));
        }
    }

    private void reconnect() {
        game.commandQueue.add(new Command(Command.GAME_RECONNECT_DO).addArg(GamePreferences.getReconnectId()));
    }

    private void askForStats() {
        game.commandQueue.add(new Command(Command.SHOW_SERVER_STATISTICS));
    }

    private void createLobby() {
        Command command = new Command(Command.CREATE_LOBBY).addArg(lobbyNameTextField.getText());
        game.setScreen(new LoadingScreen(game,LoadingScreen.SITUATION_SERVER_TO_LOBBY_CREATE,command));
    }

    private void backToMenu() {
        game.commandQueue.add(new Command(Command.SW_DISCONNECT));
        game.setScreen(new MainMenuScreen(game));
    }

    private void joinLobby() {
        Command command = new Command(Command.JOIN_LOBBY).addArg(lobbyList.get(list.getSelectedIndex()).id);
        game.setScreen(new LoadingScreen(game,LoadingScreen.SITUATION_SERVER_TO_LOBBY_JOIN,command));
    }

    private void sendRefresh() {
        game.commandQueue.add(new Command(Command.REQUEST_LOBBY_LIST));
    }

    @Override
    public void show() {
        stage.clear();
        stage.addActor(mainTable);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);


    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        if (!game.resultQueue.isEmpty()) {
            Command c = game.resultQueue.remove();
            if (c.code == Command.RESULT_PLAYER_NAME) {
                game.me.name = c.args.get(0);
            }
            if (c.code == Command.LOBBY_LIST) {
                updateLobbyList(c);
            }
            if (c.code == Command.HW_DISCONNECTED) {
                game.setScreen(new LoadingScreen(game, LoadingScreen.SITUATION_DISCONNECTED, null));
            }
            if (c.code == Command.SERVER_STATISTICS) {
                System.out.println(c.args.get(0));
            }
            if (c.code == Command.GAME_RECONNECT_ASK_RESULT) {
                if (c.argInt(0)==1) reconnectButton.setVisible(true);
            }
            if (c.code == Command.GAME_RECONNECT_DO_RESULT) {
                if (c.argInt(0)==1) game.setScreen(new GameScreen(game,GameScreen.MULTI_PLAYER));
            }
        }
    }

    private void updateLobbyList(Command c) {
        int i = 0;
        int size = Integer.valueOf(c.args.get(i));
        lobbyList = new ArrayList<Lobby>();
        for (int j = 0; j < size; j++) {
            lobbyList.add(new Lobby(
                    Integer.valueOf(c.args.get(++i)),
                    c.args.get(++i),
                    Integer.valueOf(c.args.get(++i)),
                    Integer.valueOf(c.args.get(++i))
            ));
        }
        list.setItems(makeLabelArray(lobbyList));
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
