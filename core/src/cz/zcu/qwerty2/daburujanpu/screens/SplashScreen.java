package cz.zcu.qwerty2.daburujanpu.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by qwerty on 9. 10. 2016.
 */

public class SplashScreen implements Screen {


    int w = 0;
    int h = 0;
    int tw = 0;
    int th = 0;
    OrthographicCamera camera = null;
    Texture fav = null;
    SpriteBatch batch = null;
    Stage stage;
    Game game;

    public SplashScreen(Game game) {
        this.game = game;

    }

    @Override
    public void show() {
        fav = new Texture("fav.png");
        stage = new Stage();

        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(w, h);
        camera.position.set(w / 2, h / 2, 0);
        camera.update();
        tw = fav.getWidth();
        th = fav.getHeight();

        batch = new SpriteBatch();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(fav, camera.position.x - (tw / 2), camera.position.y - (th / 2));
        batch.end();


    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(fav, camera.position.x - (tw / 2), camera.position.y - (th / 2));
        batch.end();

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
