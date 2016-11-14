package cz.zcu.qwerty2.daburujanpu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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

import java.util.ArrayList;

import cz.zcu.qwerty2.daburujanpu.DaburuJanpu;
import cz.zcu.qwerty2.daburujanpu.data.GamePreferences;
import cz.zcu.qwerty2.daburujanpu.net.Command;

public class ServerScreen implements Screen {
    DaburuJanpu game;
    Table mainTable;
    List list;
    ScrollPane scrollPane;
    TextButton refreshButton, joinButton, createButton, backButton;
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
        stage = new Stage();

        game.commandQueue.add(new Command(Command.SET_PLAYER_NAME).addArg(GamePreferences.getPrefPlayerName()));
        sendRefresh();


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
