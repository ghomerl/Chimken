package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.entities.enemies.bosses.Boss2;
import com.github.ghomerl.chimken.view.assets.Assets;


public class BossRenderer {


    public void render(Batch batch, Enemy enemy) {
        if (!enemy.isAlive()) return;

        Texture tex;
        if (enemy instanceof Boss2) {
            tex = Assets.boss2Texture;
        } else {
            tex = Assets.boss1Texture;
        }
        if (tex == null) return;

        batch.draw(tex,
            enemy.getX(), enemy.getY(),
            enemy.getWidth(), enemy.getHeight());
    }


    public void renderDebug(ShapeRenderer renderer, Enemy enemy) {
        if (enemy.isAlive()) {
            renderer.rect(enemy.getX(), enemy.getY(),
                enemy.getWidth(), enemy.getHeight());
        }
    }
}
