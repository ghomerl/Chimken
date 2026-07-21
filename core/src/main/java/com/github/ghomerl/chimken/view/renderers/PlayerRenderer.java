package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.ghomerl.chimken.model.entities.Player;
import com.github.ghomerl.chimken.view.assets.Assets;


public class PlayerRenderer {


    public void render(Batch batch, Player player) {
        if (!player.isVisible()) {
            return;
        }
        Texture tex = Assets.spaceshipTexture;
        if (tex == null) return;
        batch.draw(tex,
            player.getX(), player.getY(),
            player.getWidth(), player.getHeight());
    }


    public void renderDebug(ShapeRenderer renderer, Player player) {
        if (!player.isVisible()) {
            return;
        }
        renderer.rect(player.getX(), player.getY(),
            player.getWidth(), player.getHeight());
    }
}
