package com.github.ghomerl.chimken.view.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.ghomerl.chimken.controller.AudioManager;
import com.github.ghomerl.chimken.controller.ScreenManager;
import com.github.ghomerl.chimken.view.assets.Assets;


public class LoadingScreen extends AbstractScreen {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;

    public LoadingScreen() {
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        Assets.queueLoad();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        super.render(delta);


        float progress = Assets.getProgress();


        float barWidth = Gdx.graphics.getWidth() / 2f; // Takes up half the screen width
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
        String text = "Loading... " + percentage + "%";

        font.draw(batch, text, startX, startY - 20f);
        batch.end();


        if (Assets.update()) {
            AudioManager.playMainTheme();
            ScreenManager.setScreen(new MainMenuScreen());
        }
    }
}
