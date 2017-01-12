package cz.zcu.qwerty2.daburujanpu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import cz.zcu.qwerty2.daburujanpu.DaburuJanpu;
import cz.zcu.qwerty2.daburujanpu.data.ColorPalette;
import cz.zcu.qwerty2.daburujanpu.data.Player;
import cz.zcu.qwerty2.daburujanpu.net.Command;

public class LobbyScreen implements Screen {
    private DaburuJanpu game;
    private Table mainTable, playersTable;

    private ScrollPane scrollPane, chatScrollPane;
    private Label chat, lobbyNameLabel;
    private int lobbyId = -1;
    private int capacity = -1;
    private int playerCount = -1;
    private TextField chatField;
    private TextButton backButton, chatButton, colorButton, readyButton;
    private Stage stage;

    ArrayList<Player> players = new ArrayList<Player>();

    private void makePlayersTable(ArrayList<Player> players, Table playersTable) {
        playersTable.clear();
        for (Player player : players
                ) {
            playersTable.add(player.name).width(120).fill();
            Pixmap squareColor = new Pixmap(16, 16, Pixmap.Format.RGB888);
            squareColor.setColor(ColorPalette.colors[player.color]);
            squareColor.fill();
            Image image= new Image(new Texture(squareColor));
            playersTable.add(image).center().padLeft(10).padRight(10);
            if (player.ready) {
                playersTable.add("READY").minWidth(50).fill().center();
            } else {
                playersTable.add("").minWidth(50).fill().center();
            }
            if (playersTable.getRows() < players.size()) { // aby sme na konci neudelali radek navic
                playersTable.row();
            }

        }

    }

    public LobbyScreen(DaburuJanpu game) {
        this.game = game;

        backButton = new TextButton("Leave", game.skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                backToServer();
            }
        });

        playersTable = new Table(game.skin);
        playersTable.top(); // zarovnani tabulky
        chat = new Label("", game.skin);
        chat.setAlignment(Align.topLeft);


        chatField = new TextField("", game.skin);
        chatField.setMaxLength(Command.CHAT_MAX_LENGTH);
        chatField.addListener(new InputListener() {
            @Override

            // pri entru odesli chat
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    chatSend();
                }
                return false;
            }
        });

        chatButton = new TextButton("Send", game.skin);
        chatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                chatSend();
            }
        });
        readyButton = new TextButton("Ready", game.skin);
        readyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                readySend();
            }
        });
        colorButton = new TextButton("Color", game.skin);
        colorButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                colorSend();
            }
        });



        makePlayersTable(players, playersTable);


        scrollPane = new ScrollPane(playersTable, game.skin);
        chatScrollPane = new ScrollPane(chat, game.skin);

        lobbyNameLabel = new Label("", game.skin);
        lobbyNameLabel.setAlignment(Align.center);

        mainTable = new Table(game.skin);
        mainTable.setFillParent(true);
        mainTable.add(lobbyNameLabel).fill().colspan(4);
        mainTable.row();
        mainTable.add(scrollPane).maxHeight(200).colspan(1).fill().top();
        mainTable.add(chatScrollPane).size(300, 200).colspan(3).fill();

        mainTable.row();
        mainTable.add(chatField).colspan(3).fill();
        mainTable.add(chatButton).fill();
        mainTable.row();

        mainTable.add(backButton).fill().spaceTop(30);
        mainTable.add(colorButton).fill().spaceTop(30);
        mainTable.add(readyButton).fill().spaceTop(30);
        stage = new Stage();

        sendRefresh();


    }

    private void colorSend() {
        game.commandQueue.add(new Command(Command.CHANGE_COLOR));
    }

    private void readySend() { game.commandQueue.add(new Command(Command.SET_READY)); }

    private void chatSend() {
        if (chatField.getText().length() > 0) {
            String message = chatField.getText();
            message = message.replaceAll(";",":semicolon:");
            if (message.length()>Command.CHAT_MAX_LENGTH) message = message.substring(0,Command.CHAT_MAX_LENGTH);
            game.commandQueue.add(new Command(Command.SEND_CHAT).addArg(message));
            chatField.setText("");
        }
    }

    private void receiveChat(Command c) {
        String message = c.args.get(0);
        message = message.replaceAll(":semicolon:",";");
        if (chat.getText().length() == 0) {
            chat.setText(message);
        } else {
            chat.setText(chat.getText() + "\n" + message);
        }
        chatScrollPane.layout();
        chatScrollPane.setScrollPercentY(100);
    }


    private void backToServer() {
        game.commandQueue.add(new Command(Command.LEAVE_LOBBY));
        game.setScreen(new ServerScreen(game));
    }

    private void sendRefresh() {
        game.commandQueue.add(new Command(Command.REQUEST_LOBBY_INFO));
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
            switch (c.code) {
                case Command.LOBBY_INFO:
                    updateLobbyInfo(c);
                    break;
                case Command.RECEIVE_CHAT:
                    receiveChat(c);
                    break;
                case Command.GAME_STARTED:
                    game.setScreen(new GameScreen(game,GameScreen.MULTI_PLAYER));
                    break;
                case Command.HW_DISCONNECTED:
                    game.setScreen(new LoadingScreen(game,LoadingScreen.SITUATION_DISCONNECTED,null));
                    break;
            }
        }
    }


    private void updateLobbyInfo(Command c) {
        int i = 0;
        lobbyId = Integer.valueOf(c.args.get(i));
        String lobbyName = c.args.get(++i);
        lobbyNameLabel.setText(lobbyName);
        capacity = Integer.valueOf(c.args.get(++i));
        playerCount = Integer.valueOf(c.args.get(++i));
        players.clear();
        for (int j = 0; j < playerCount; j++) {
            players.add(new Player(
                    Integer.valueOf(c.args.get(++i)),
                    c.args.get(++i),
                    Integer.valueOf(c.args.get(++i)),
                    (Integer.valueOf(c.args.get(++i)) == 1)
            ));
        }

        makePlayersTable(players, playersTable);


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