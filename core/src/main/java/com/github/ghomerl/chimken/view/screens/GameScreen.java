package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.ghomerl.chimken.controller.GameScreenController;
import com.github.ghomerl.chimken.controller.LoginMenuController;
import com.github.ghomerl.chimken.model.KeyBindings;
import com.github.ghomerl.chimken.model.Player;

public class GameScreen extends AbstractScreen {

    private Player player;
    private ShapeRenderer shapeRenderer;

    @Override
    public void show() {
        super.show();

        shapeRenderer = new ShapeRenderer();

        KeyBindings kb = LoginMenuController.isLoggedIn()
            ? LoginMenuController.getCurrentUser().getKeyBindings()
            : new KeyBindings();
        player = new Player(kb, 928f, 100f);

        Stack stack = new Stack();
        stack.setFillParent(true);

        Table backBtnWrapper = new Table();
        backBtnWrapper.top().left().pad(12);
        TextButton backBtn = new TextButton("back", skin);
        backBtnWrapper.add(backBtn).width(240).height(60);

        stack.add(backBtnWrapper);

        stage.addActor(stack);

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreenController.openPreGameMenu();
            }
        });
    }

    @Override
    public void render(float delta) {
        float clamped = Math.min(delta, 1 / 30f);
        player.update(clamped);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldViewport.apply();
        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        player.render(batch);
        batch.end();

        shapeRenderer.setProjectionMatrix(worldCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        player.renderDebug(shapeRenderer);
        shapeRenderer.end();

        uiViewport.apply();
        stage.act(clamped);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        worldViewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (player != null) {
            player.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        super.dispose();
    }
}
