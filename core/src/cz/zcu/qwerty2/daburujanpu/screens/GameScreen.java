package cz.zcu.qwerty2.daburujanpu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import cz.zcu.qwerty2.daburujanpu.DaburuJanpu;
import cz.zcu.qwerty2.daburujanpu.data.GamePreferences;
import cz.zcu.qwerty2.daburujanpu.data.Level;
import cz.zcu.qwerty2.daburujanpu.data.Player;
import cz.zcu.qwerty2.daburujanpu.net.Command;
import cz.zcu.qwerty2.daburujanpu.net.Result;

public class GameScreen implements Screen {
    public static final int SINGLE_PLAYER = 0;
    public static final int MULTI_PLAYER = 1;
    private static final int CAMERA_START_SHIFT = -200;

    final DaburuJanpu game;

    Texture dropImage;
    Texture bucketImage;

    OrthographicCamera camera;
    Rectangle bucket;
    Array<Rectangle> raindrops;
    long lastDropTime;
    int dropsGathered;
    private int startType;
    private Level level = null;
    private TextureAtlas stepAtlas;
    private Array<TextureAtlas.AtlasRegion> stepAtlasRegions;
    private TextureAtlas gearAtlas;
    private Array<TextureAtlas.AtlasRegion> gearAtlasRegions;
    ArrayList<Player> players;
    private int myindex = -1;
    Stage stage; // jenom pro vyblokovani vstupu v ostatnich screenach
    private boolean escaping = false;
    int camerapos = 0, cameramulti=0;
    int round = -1;
    Result[] results = null;


    private boolean disabled = false;

    public GameScreen(final DaburuJanpu game, int startType) {
        this.game = game;
        this.startType = startType;

        if (this.startType == SINGLE_PLAYER) {
            level = new Level(Level.generateRandomSeed());
            players = new ArrayList<Player>();
            players.add(new Player(0, GamePreferences.getPrefPlayerName(), 0, true));
            myindex = 0;
        } else { //MULTI
            players = new ArrayList<Player>();
            level = new Level(Level.generateRandomSeed());
            disabled = true;
            GamePreferences.setReconnectId(game.netService.myId);
        }

        stepAtlas = new TextureAtlas(Gdx.files.internal("steps/atlas_steps32.atlas"));
        stepAtlasRegions = stepAtlas.getRegions();
        gearAtlas = new TextureAtlas(Gdx.files.internal("gears/atlas_gears32.atlas"));
        gearAtlasRegions = gearAtlas.getRegions();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 480);
        camera.translate(0, CAMERA_START_SHIFT);

        stage = new Stage();

        game.commandQueue.add(new Command(Command.READY_FOR_GAME_INFO));

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (startType == SINGLE_PLAYER && camerapos < players.get(myindex).y) //hejbu si sam kamerou
        {
            int shift = ((int) players.get(myindex).y - camerapos) / 2;
            camera.translate(0, shift);
            camerapos += shift;
        }

        if (startType == MULTI_PLAYER && camerapos < cameramulti)
        {
            int shift = (cameramulti - camerapos) /4;
            camera.translate(0, shift);
            camerapos += shift;
        }




        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        //kresleni mapy
        for (int i = ((camerapos + CAMERA_START_SHIFT) / Level.STEP_DISTANCE); i < ((camerapos + CAMERA_START_SHIFT + 100 + Level.SCREEN_HEIGHT) / Level.STEP_DISTANCE); i++) {
            int step = level.getStep(i);
            int color = level.getStepColor(i);
            for (int j = 0; j < Level.STEPS_WIDTH; j++) {
                int type = Level.STEPS[step][j];
                if (type > 0) {
                    game.batch.draw(stepAtlasRegions.get(color * 3 + type - 1), j * 32, i * Level.STEP_DISTANCE - 32);
                }
            }
        }


        //kresleni hracu


        if (players.size()>0) {
            for (int i = 0; i < players.size(); i++) {
                game.batch.draw(gearAtlasRegions.get(players.get(i).color), players.get(i).x, players.get(i).y,
                        Player.SPRITE_SIZE / 2, Player.SPRITE_SIZE / 2, Player.SPRITE_SIZE, Player.SPRITE_SIZE, 1, 1, -players.get(i).x);
                game.font.draw(game.batch, "" + players.get(i).maxstep, 0, camerapos - CAMERA_START_SHIFT + i * game.font.getLineHeight());
                game.font.draw(game.batch, players.get(i).name, 20, camerapos - CAMERA_START_SHIFT + i * game.font.getLineHeight());
            }
        }

        //kreseni vysledkove listiny
        if (results!=null) {
            game.font.setColor(Color.WHITE);
            game.font.draw(game.batch, "SCORE", 100, camerapos - CAMERA_START_SHIFT + game.font.getLineHeight());
            game.font.draw(game.batch, "MAX STEP", 200, camerapos - CAMERA_START_SHIFT + game.font.getLineHeight());
            game.font.draw(game.batch, "NAME", 300, camerapos - CAMERA_START_SHIFT + game.font.getLineHeight());
            for (int i = 0; i < results.length; i++) {
                game.font.draw(game.batch, ""+results[i].score, 100, camerapos - CAMERA_START_SHIFT - i*game.font.getLineHeight());
                game.font.draw(game.batch, ""+results[i].maxStep, 200, camerapos - CAMERA_START_SHIFT - i*game.font.getLineHeight());
                if (results[i].left) game.font.setColor(Color.RED);
                game.font.draw(game.batch, ""+results[i].name, 300, camerapos - CAMERA_START_SHIFT - i*game.font.getLineHeight());
                game.font.setColor(Color.WHITE);

            }
            game.font.draw(game.batch, "GAME OVER - USE ESCAPE KEY TO LEAVE", 100, camerapos - (CAMERA_START_SHIFT - results.length+1) *game.font.getLineHeight());

        }

        Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());

        game.batch.end();

        //NETWORKING

        if (startType == MULTI_PLAYER) {
            while (!game.resultQueue.isEmpty()) {
                Command c = game.resultQueue.remove();
                switch (c.code) {
                    case Command.GAME_INFO:
                        int i = 0;
                        int count = c.argInt(i++);
                        if(players.size() != count) {
                            players.clear();
                            for (int j = 0; j < count; j++) players.add(new Player());
                            myindex=-1; // prepocteme pole
                        }
                        for (int j = 0; j < count; j++) {
                            if (j == myindex) {
                                if (players.get(myindex).dead) {
                                    i = players.get(j).fromArgs(c, i);  // pokud jsem mrvej muze mi server prepsat data
                                } else {
                                    Player myStats = new Player();
                                    int backup_i = i;
                                    i = myStats.fromArgs(c, i);
                                    if (myStats.dead) {                 // jinak jenom jistuju jesli nahodou nejsem mrtvej
                                        players.get(myindex).fromArgs(c, backup_i);
                                        game.commandQueue.add(new Command(Command.MY_UPDATE).addArg(players.get(myindex).serialize()));
                                        disabled = true;

                                    }
                                }
                            } else {
                                i = players.get(j).fromArgs(c, i);
                            }
                        }
                        cameramulti = c.argInt(i++);
                        if (myindex < 0) {
                            for (int j = 0; j < count; j++) {
                                if (players.get(j).id == game.netService.myId) {
                                    myindex = j;
                                }
                            }
                        }

                        break;
                    case Command.GAME_INFO_SEED:
                        int[] seed = new int[Level.SEED_LENGTH];
                        for (int j = 0; j < Level.SEED_LENGTH; j++) {
                            seed[j] = c.argInt(j);
                        }
                        level = new Level(seed);  // prijat seed od serveru
                        game.commandQueue.add(new Command(Command.ROUND_LOADED)); //dame vedet ze sme nacetli

                        break;
                    case Command.ROUND_START :
                        disabled = false;
                        round++;
                        break;
                    case Command.GAME_NEW_ROUND :
                        players.get(myindex).dead =false;
                        camera.translate(0, -camerapos);
                        camerapos = 0;
                        game.commandQueue.add(new Command(Command.ROUND_LOADED));
                        break;

                    case Command.HW_DISCONNECTED:
                        game.setScreen(new LoadingScreen(game,LoadingScreen.SITUATION_DISCONNECTED,null));
                        break;
                    case Command.GAME_RESULT:
                        int u = 0;
                        results = new Result[c.argInt(u++)];
                        for (int j = 0; j < results.length; j++) {
                            results[j] = new Result();
                            results[j].id = c.argInt(u++);
                            results[j].name = c.args.get(u++);
                            results[j].score = c.argInt(u++);
                            results[j].maxStep = c.argInt(u++);
                            results[j].left = (c.argInt(u++) == 1);
                        }
                        break;

                }
            }
        }


        //INPUTY

        if (!disabled && myindex >= 0) {
            Player me = players.get(myindex);
            // uzivatelske vstupy
            me.inputl = Gdx.input.isKeyPressed(Input.Keys.LEFT);
            me.inputp = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
            me.inputup = Gdx.input.isKeyPressed(Input.Keys.UP) ||
                    Gdx.input.isKeyPressed(Input.Keys.SPACE);
            me.nextFrame(level);
            game.commandQueue.add(new Command(Command.MY_UPDATE).addArg(players.get(myindex).serialize()));

        }



        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            escaping = true;
        } else {
            if (escaping) {
                if (startType == SINGLE_PLAYER) {
                    game.setScreen(new MainMenuScreen(game)); // alternativa ke keyup, ktera je asi 10x kratsi
                } else {
                    GamePreferences.setReconnectId(GamePreferences.DEFAULT_RECONNECT_ID);
                    game.commandQueue.add(new Command(Command.LEAVE_GAME));
                    game.setScreen(new ServerScreen(game));
                }
            }
        }




    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
