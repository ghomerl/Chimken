package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;

/**
 * Renders projectile entities as glowing bolts.
 * Each projectile is drawn with a three-layer glow effect:
 * an outer semi-transparent halo, a coloured core, and a bright centre line.
 */
public class ProjectileRenderer {

    /**
     * Draws every active projectile in the supplied array.
     * <b>Must be called between</b> {@code shapeRenderer.begin(ShapeType.Filled)}
     * and {@code shapeRenderer.end()}.
     *
     * @param renderer   the active ShapeRenderer (Filled mode)
     * @param projectiles the projectiles to draw
     */
    public void render(ShapeRenderer renderer, Array<Projectile> projectiles) {
        for (Projectile p : projectiles) {
            if (!p.isActive()) {
                continue;
            }
            drawPlasmaBolt(renderer, p);
        }
    }

    /**
     * Draws a single plasma-style bolt.
     */
    private void drawPlasmaBolt(ShapeRenderer renderer, Projectile p) {
        float x = p.getX();
        float y = p.getY();
        float w = p.getWidth();
        float h = p.getHeight();

        // Outer glow (wider, semi-transparent)
        renderer.setColor(0.2f, 0.8f, 1f, 0.4f);
        renderer.rect(x - 2f, y, w + 4f, h);

        // Core
        renderer.setColor(Color.CYAN);
        renderer.rect(x, y + 2f, w, h - 4f);

        // Bright centre
        renderer.setColor(Color.WHITE);
        renderer.rect(x + 2f, y + 4f, w - 4f, h - 8f);
    }

    /**
     * Draws debug hitbox outlines for every active projectile.
     * <b>Must be called between</b> {@code shapeRenderer.begin(ShapeType.Line)}
     * and {@code shapeRenderer.end()}.
     *
     * @param renderer    the active ShapeRenderer (Line mode)
     * @param projectiles the projectiles whose hitboxes to outline
     */
    public void renderDebug(ShapeRenderer renderer, Array<Projectile> projectiles) {
        for (Projectile p : projectiles) {
            if (p.isActive()) {
                renderer.rect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
            }
        }
    }
}
