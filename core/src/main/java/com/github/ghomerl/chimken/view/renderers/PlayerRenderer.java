package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.ghomerl.chimken.model.entities.Player;


public class PlayerRenderer {

    public void render(ShapeRenderer renderer, Player player) {
        float x = player.getX();
        float y = player.getY();
        float w = player.getWidth();
        float h = player.getHeight();


        renderer.setColor(0.1f, 0.15f, 0.3f, 1f);
        renderer.rect(x, y, w, h);


        renderer.setColor(Color.CYAN);
        renderer.triangle(
            x + w * 0.5f, y + h - 4f,
            x + 8f,        y + 8f,
            x + w - 8f,    y + 8f
        );


        renderer.setColor(0.2f, 0.6f, 1f, 1f);
        renderer.triangle(
            x + w * 0.5f, y + h - 4f,
            x + 18f,       y + 20f,
            x + w - 18f,   y + 20f
        );


        renderer.setColor(Color.WHITE);
        renderer.rect(x + w * 0.5f - 4f, y + 4f, 8f, 16f);
    }


    public void renderDebug(ShapeRenderer renderer, Player player) {
        renderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
    }
}
