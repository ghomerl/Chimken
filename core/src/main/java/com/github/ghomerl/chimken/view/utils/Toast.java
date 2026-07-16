package com.github.ghomerl.chimken.view.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class Toast {

    private static final float FADE_DURATION = 0.3f;
    private static final float DISPLAY_DURATION = 2.5f;
    private static final float PADDING = 20f;

    public static void show(Stage stage, Skin skin, String message) {
        if (stage == null || skin == null || message == null) {
            return;
        }

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.18f, 0.18f, 0.18f, 0.88f));
        pixmap.fill();
        Texture bgTexture = new Texture(pixmap);
        pixmap.dispose();

        Drawable bgDrawable = new TextureRegionDrawable(new TextureRegion(bgTexture));

        Label label = new Label(message, skin);
        label.setColor(Color.WHITE);

        Table inner = new Table();
        inner.setBackground(bgDrawable);
        inner.add(label).pad(10f, 18f, 10f, 18f);

        Table wrapper = new Table();
        wrapper.setFillParent(true);
        wrapper.bottom().right().pad(PADDING);
        wrapper.add(inner);

        wrapper.setColor(1, 1, 1, 0f);
        wrapper.addAction(Actions.sequence(
            Actions.fadeIn(FADE_DURATION),
            Actions.delay(DISPLAY_DURATION),
            Actions.fadeOut(FADE_DURATION),
            Actions.run(bgTexture::dispose),
            Actions.removeActor()
        ));

        stage.addActor(wrapper);
    }
}
