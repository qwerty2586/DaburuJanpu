package cz.zcu.qwerty2.daburujanpu.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import cz.zcu.qwerty2.daburujanpu.DaburuJanpu;
import sun.rmi.runtime.Log;


import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;



public class SplashScreen implements Screen {

    OrthographicCamera camera = null;
    Texture fav = null;
    Texture daburu = null;
    Stage stage;
    DaburuJanpu game;
    Image favImage;
    Image daburuImage;
    Viewport viewport;


    public SplashScreen(DaburuJanpu game) {
        this.game = game;

        camera = new OrthographicCamera(640,480);
        viewport = new FillViewport(640,480,camera);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("width",""+Gdx.graphics.getWidth());
        Gdx.app.debug("height",""+Gdx.graphics.getHeight());

        fav = new Texture(Gdx.files.internal("fav.png"));
        daburu = new Texture(Gdx.files.internal("daburu.png"));
        stage = new Stage(viewport);
        favImage = new Image(fav);
        favImage.setPosition(stage.getWidth()/2- favImage.getWidth()/2,stage.getHeight()/2- favImage.getHeight()/2);
        daburuImage = new Image(daburu);
        daburuImage.setPosition(stage.getWidth()/2- daburuImage.getWidth()/2,stage.getHeight()/2- daburuImage.getHeight()/2);

    }

    @Override
    public void show() {
        stage.clear();
        favImage.addAction( sequence( fadeOut(0f),fadeIn( 0.75f ), delay( 1.00f ), fadeOut( 0.75f )));

        daburuImage.addAction( sequence( fadeOut(0f), delay( 2.00f ), fadeIn( 0.75f ),delay( 1.00f ), fadeOut( 0.75f ),
                new Action() {
                    @Override
                    public boolean act(
                            float delta )
                    {

                        game.setScreen( new MainMenuScreen( game ) );
                        return true;
                    }
                } ) );
        stage.addActor(favImage);
        stage.addActor(daburuImage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
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
