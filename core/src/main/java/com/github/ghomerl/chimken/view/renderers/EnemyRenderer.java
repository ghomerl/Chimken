package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;


public class EnemyRenderer {


    public void render(ShapeRenderer renderer, Enemy enemy) {
        if (!enemy.isAlive()) {
            return;
        }

        renderer.setColor(Color.RED);
        renderer.rect(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());


        float pad = 6f;
        renderer.setColor(0.6f, 0.05f, 0.05f, 1f);
        renderer.rect(enemy.getX() + pad, enemy.getY() + pad,
            enemy.getWidth() - pad * 2, enemy.getHeight() - pad * 2);
    }


    public void renderDebug(ShapeRenderer renderer, Enemy enemy) {
        if (enemy.isAlive()) {
            renderer.rect(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
        }
    }
}
