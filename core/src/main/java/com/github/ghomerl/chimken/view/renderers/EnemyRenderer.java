package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.ghomerl.chimken.model.entities.Enemy;

/**
 * Renders enemy entities.
 * The base renderer draws a simple coloured rectangle; subclasses or
 * alternative renderers can add more elaborate visuals later.
 */
public class EnemyRenderer {

    /**
     * Draws the enemy as a red rectangle.
     * <b>Must be called between</b> {@code shapeRenderer.begin(ShapeType.Filled)}
     * and {@code shapeRenderer.end()}.
     *
     * @param renderer the active ShapeRenderer (Filled mode)
     * @param enemy    the enemy to draw
     */
    public void render(ShapeRenderer renderer, Enemy enemy) {
        if (!enemy.isAlive()) {
            return;
        }

        renderer.setColor(Color.RED);
        renderer.rect(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());

        // Small inner detail — darker core
        float pad = 6f;
        renderer.setColor(0.6f, 0.05f, 0.05f, 1f);
        renderer.rect(enemy.getX() + pad, enemy.getY() + pad,
            enemy.getWidth() - pad * 2, enemy.getHeight() - pad * 2);
    }

    /**
     * Draws a debug hitbox outline around the enemy.
     * <b>Must be called between</b> {@code shapeRenderer.begin(ShapeType.Line)}
     * and {@code shapeRenderer.end()}.
     *
     * @param renderer the active ShapeRenderer (Line mode)
     * @param enemy    the enemy whose hitbox to outline
     */
    public void renderDebug(ShapeRenderer renderer, Enemy enemy) {
        if (enemy.isAlive()) {
            renderer.rect(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
        }
    }
}
