package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.ghomerl.chimken.model.entities.Player;

/**
 * Renders the player as a dark rectangular body containing a
 * cyan triangular spaceship shape with a white engine glow.
 * <p>
 * All drawing is done through {@link ShapeRenderer} so no
 * texture management is required.
 */
public class PlayerRenderer {

    /**
     * Draws the player entity. Skips rendering when the player is not visible.
     * <b>Must be called between</b> {@code shapeRenderer.begin(ShapeType.Filled)}
     * and {@code shapeRenderer.end()}.
     *
     * @param renderer the active ShapeRenderer (Filled mode)
     * @param player   the player to draw
     */
    public void render(ShapeRenderer renderer, Player player) {
        if (!player.isVisible()) {
            return;
        }

        float x = player.getX();
        float y = player.getY();
        float w = player.getWidth();
        float h = player.getHeight();

        // Dark navy body rectangle
        renderer.setColor(0.1f, 0.15f, 0.3f, 1f);
        renderer.rect(x, y, w, h);

        // Outer cyan triangle (ship silhouette)
        renderer.setColor(Color.CYAN);
        renderer.triangle(
            x + w * 0.5f, y + h - 4f,
            x + 8f,        y + 8f,
            x + w - 8f,    y + 8f
        );

        // Inner brighter triangle (cockpit highlight)
        renderer.setColor(0.2f, 0.6f, 1f, 1f);
        renderer.triangle(
            x + w * 0.5f, y + h - 4f,
            x + 18f,       y + 20f,
            x + w - 18f,   y + 20f
        );

        // White engine glow at the bottom centre
        renderer.setColor(Color.WHITE);
        renderer.rect(x + w * 0.5f - 4f, y + 4f, 8f, 16f);
    }

    /**
     * Draws a debug hitbox outline around the player.
     * <b>Must be called between</b> {@code shapeRenderer.begin(ShapeType.Line)}
     * and {@code shapeRenderer.end()}.
     *
     * @param renderer the active ShapeRenderer (Line mode)
     * @param player   the player whose hitbox to outline
     */
    public void renderDebug(ShapeRenderer renderer, Player player) {
        if (!player.isVisible()) {
            return;
        }
        renderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
    }
}
