package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.ghomerl.chimken.controller.ScreenManager;
import com.github.ghomerl.chimken.controller.audio.MusicManager;
import com.github.ghomerl.chimken.view.assets.Assets;


public class GameLoadingScreen extends AbstractScreen {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;

    public GameLoadingScreen() {
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        Assets.queueGameAssets();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        float progress = Assets.getGameProgress();

        float barWidth = Gdx.graphics.getWidth() / 2f;
        float barHeight = 30f;
        float startX = (Gdx.graphics.getWidth() - barWidth) / 2f;
        float startY = Gdx.graphics.getHeight() / 2f;


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(startX, startY, barWidth, barHeight);

        shapeRenderer.setColor(Color.SLATE);
        shapeRenderer.rect(startX, startY, barWidth * progress, barHeight);
        shapeRenderer.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(startX, startY, barWidth, barHeight);
        shapeRenderer.end();


        batch.begin();
        int percentage = (int) (progress * 100);
        String text = "Preparing battle... " + percentage + "%";

        font.draw(batch, text, startX, startY - 20f);
        batch.end();


        if (Assets.updateGameAssets()) {
            MusicManager.stopMainTheme();
            MusicManager.playBattleTheme();
            ScreenManager.setScreen(new GameScreen());
        }
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
        if (font != null) {
            font.dispose();
        }
        super.dispose();
    }
}
