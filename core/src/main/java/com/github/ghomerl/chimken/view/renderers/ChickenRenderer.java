package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.ghomerl.chimken.model.entities.enemies.DoubleEggChicken;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.entities.enemies.SniperChicken;

/**
 * Renders all chicken-type enemies with a simple geometric style:
 * a body rectangle, a beak triangle, a comb, and an eye.
 * <p>
 * Each subtype gets a slightly different accent colour so the player
 * can tell them apart at a glance.
 */
public class ChickenRenderer {

    /**
     * Draws any chicken enemy.
     * <b>Must be called between</b> {@code shapeRenderer.begin(ShapeType.Filled)}
     * and {@code shapeRenderer.end()}.
     */
    public void render(ShapeRenderer renderer, Enemy enemy) {
        if (!enemy.isAlive()) {
            return;
        }

        float x = enemy.getX();
        float y = enemy.getY();
        float w = enemy.getWidth();
        float h = enemy.getHeight();

        Color accent = accentFor(enemy);

        // Body
        renderer.setColor(0.95f, 0.85f, 0.55f, 1f);  // warm tan
        renderer.rect(x, y, w, h);

        // Inner body highlight
        renderer.setColor(1f, 0.93f, 0.7f, 1f);
        renderer.rect(x + 6, y + 6, w - 12, h - 12);

        // Comb (red, top centre)
        renderer.setColor(Color.RED);
        float combW = w * 0.25f;
        float combH = 10f;
        renderer.rect(x + w * 0.5f - combW / 2f, y + h - 2f, combW, combH);

        // Beak (orange triangle, bottom-centre)
        renderer.setColor(accent);
        renderer.triangle(
            x + w * 0.5f, y - 6f,           // beak tip
            x + w * 0.38f, y + 6f,           // left base
            x + w * 0.62f, y + 6f            // right base
        );

        // Eye
        renderer.setColor(Color.BLACK);
        renderer.circle(x + w * 0.35f, y + h * 0.7f, 4f);
        renderer.setColor(Color.WHITE);
        renderer.circle(x + w * 0.35f, y + h * 0.7f, 2f);
    }

    /**
     * Debug hitbox outline.
     * <b>Must be called between</b> {@code shapeRenderer.begin(ShapeType.Line)}
     * and {@code shapeRenderer.end()}.
     */
    public void renderDebug(ShapeRenderer renderer, Enemy enemy) {
        if (enemy.isAlive()) {
            renderer.rect(enemy.getX(), enemy.getY(),
                enemy.getWidth(), enemy.getHeight());
        }
    }

    private Color accentFor(Enemy enemy) {
        if (enemy instanceof SniperChicken) {
            return Color.MAGENTA;   // purple-ish for sniper
        }
        if (enemy instanceof DoubleEggChicken) {
            return Color.ORANGE;    // orange for double
        }
        return Color.YELLOW;       // yellow for basic
    }
}
