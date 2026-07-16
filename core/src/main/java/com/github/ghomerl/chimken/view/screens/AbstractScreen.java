package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.ghomerl.chimken.view.assets.Assets;


public abstract class AbstractScreen implements Screen {

    protected static Skin skin;

    protected final SpriteBatch batch;
    protected Stage stage;
    protected OrthographicCamera worldCamera;
    protected OrthographicCamera uiCamera;
    protected Viewport worldViewport;
    protected Viewport uiViewport;


    protected AbstractScreen() {
        batch = new SpriteBatch();
    }


    @Override
    public void show() {
        if (skin == null) {
            skin = Assets.skin;
        }
        worldCamera = new OrthographicCamera();
        worldViewport = new FitViewport(1920, 1080, worldCamera);
        worldCamera.position.set(worldViewport.getWorldWidth() / 2, worldViewport.getWorldHeight() / 2, 0);

        uiCamera = new OrthographicCamera();
        uiViewport = new ExtendViewport(1920, 1080, uiCamera);
        uiCamera.position.set(uiViewport.getWorldWidth() / 2, uiViewport.getWorldHeight() / 2, 0);

        if (stage == null) {
            stage = new Stage(uiViewport, batch);
        }
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        InputMultiplexer multiplexer = buildInputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);

        Gdx.app.log(getClass().getSimpleName(), "show()");
    }

    @Override
    public void render(float delta) {

        float clamped = Math.min(delta, 1 / 30f);
        update(clamped);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(clamped);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void pause() {
        Gdx.app.log(getClass().getSimpleName(), "pause()");
    }

    @Override
    public void resume() {
        Gdx.app.log(getClass().getSimpleName(), "resume()");
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        Gdx.app.log(getClass().getSimpleName(), "hide()");
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
        Gdx.app.log(getClass().getSimpleName(), "dispose()");
    }

    // ──────────────────────── template methods ───────────────────────────


    protected void update(float delta) {

    }


    protected InputMultiplexer buildInputMultiplexer() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        return multiplexer;
    }


}
