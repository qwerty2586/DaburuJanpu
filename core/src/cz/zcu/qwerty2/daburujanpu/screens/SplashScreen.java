package cz.zcu.qwerty2.daburujanpu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import cz.zcu.qwerty2.daburujanpu.DaburuJanpu;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;



public class SplashScreen implements Screen {

    OrthographicCamera camera = null;
    Texture fav = null;
    Stage stage;
    DaburuJanpu game;
    Image logoImage;

    public SplashScreen(DaburuJanpu game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        fav = new Texture(Gdx.files.internal("fav.png"));
        stage = new Stage();
        logoImage = new Image(fav);
        logoImage.setPosition(stage.getWidth()/2-logoImage.getWidth()/2,stage.getHeight()/2-logoImage.getHeight()/2);



    }

    @Override
    public void show() {
        stage.clear();
        logoImage.addAction( sequence( fadeOut(0f),fadeIn( 0.75f ), delay( 1.00f ), fadeOut( 0.75f ),
                new Action() {
                    @Override
                    public boolean act(
                            float delta )
                    {
                        game.setScreen( new MainMenuScreen( game ) );
                        return true;
                    }
                } ) );
        stage.addActor(logoImage);
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
