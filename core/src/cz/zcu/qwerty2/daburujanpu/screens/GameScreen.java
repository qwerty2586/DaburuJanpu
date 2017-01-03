package cz.zcu.qwerty2.daburujanpu.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import cz.zcu.qwerty2.daburujanpu.DaburuJanpu;
import cz.zcu.qwerty2.daburujanpu.data.GamePreferences;
import cz.zcu.qwerty2.daburujanpu.data.Level;
import cz.zcu.qwerty2.daburujanpu.data.Player;

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
    Level level = null;
    TextureAtlas stepAtlas;
    Array<TextureAtlas.AtlasRegion> stepAtlasRegions;
    TextureAtlas gearAtlas;
    Array<TextureAtlas.AtlasRegion> gearAtlasRegions;
    ArrayList<Player> players;
    int myindex;
    Stage stage; // jenom pro vyblokovani vstupu v ostatnich screenach
    private boolean escaping =false;
    int camerapos = 0;

    public GameScreen(final DaburuJanpu game,int startType) {
        this.game = game;
        this.startType = startType;

        if (this.startType==SINGLE_PLAYER) {
            level = new Level(Level.generateRandomSeed());
            players = new ArrayList<Player>();
            players.add(new Player(0, GamePreferences.getPrefPlayerName(),0,true));
            myindex = 0;
        }

        stepAtlas = new TextureAtlas(Gdx.files.internal("steps/atlas_steps32.atlas"));
        stepAtlasRegions = stepAtlas.getRegions();
        gearAtlas = new TextureAtlas(Gdx.files.internal("gears/atlas_gears32.atlas"));
        gearAtlasRegions = gearAtlas.getRegions();

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 480);
        camera.translate(0,CAMERA_START_SHIFT);

        stage = new Stage();

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (camerapos < players.get(myindex).y)
        {
            int shift = ((int)players.get(myindex).y - camerapos) / 2 ;
            camera.translate(0,shift);
            camerapos += shift;
        }


        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        for (int i=((camerapos+CAMERA_START_SHIFT)/Level.STEP_DISTANCE);i<((camerapos+CAMERA_START_SHIFT+100+Level.SCREEN_HEIGHT)/Level.STEP_DISTANCE);i++) {
            int step = level.getStep(i);
            int color = level.getStepColor(i);
            color = 0;
            for (int j = 0; j < Level.STEPS_WIDTH; j++) {
                int type = Level.STEPS[step][j];
                if (type>0) {
                    game.batch.draw(stepAtlasRegions.get(color*3 + type - 1),j*32,i*Level.STEP_DISTANCE - 32);
                }
            }
        }
        Player me = players.get(myindex);
        game.font.draw(game.batch, "   " + me.maxstep, 0, camerapos-CAMERA_START_SHIFT);
        game.batch.draw(gearAtlasRegions.get(me.color), me.x, me.y, Player.SPRITE_SIZE/2, Player.SPRITE_SIZE/2,Player.SPRITE_SIZE,Player.SPRITE_SIZE,1,1,-me.x);

        Gdx.graphics.setTitle(""+Gdx.graphics.getFramesPerSecond());

        game.batch.end();





        // uzivatelske vstupy


        players.get(myindex).inputl = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        players.get(myindex).inputp = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        players.get(myindex).inputup = Gdx.input.isKeyPressed(Input.Keys.UP)||
                                      Gdx.input.isKeyPressed(Input.Keys.SPACE);

        players.get(myindex).nextFrame(level);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            escaping =true;
        } else  {
            if (escaping)
            game.setScreen(new MainMenuScreen(game)); // alternativa ke keyup, ktera je asi 10x kratsi
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
