package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;

/**
 * Renders {@link com.github.ghomerl.chimken.model.entities.projectiles.EggProjectile}
 * instances as white ovals with a slight yellowish glow, making them
 * visually distinct from the player's cyan plasma bolts.
 * <p>
 * Accepts {@link Array}{@code <Projectile>} so it can be fed directly
 * from {@link com.github.ghomerl.chimken.model.entities.weapons.Weapon#getProjectiles()}
 * without unchecked casts.
 */
public class EggProjectileRenderer {

    /**
     * Draws every active projectile in the array as an egg.
     * <b>Must be called between</b> {@code shapeRenderer.begin(ShapeType.Filled)}
     * and {@code shapeRenderer.end()}.
     */
    public void render(ShapeRenderer renderer, Array<Projectile> projectiles) {
        for (Projectile p : projectiles) {
            if (!p.isActive()) {
                continue;
            }
            drawEgg(renderer, p);
        }
    }

    private void drawEgg(ShapeRenderer renderer, Projectile p) {
        float x = p.getX();
        float y = p.getY();
        float w = p.getWidth();
        float h = p.getHeight();

        // Outer glow
        renderer.setColor(1f, 1f, 0.7f, 0.35f);
        renderer.rect(x - 3f, y - 2f, w + 6f, h + 4f);

        // Egg body (white oval)
        renderer.setColor(Color.WHITE);
        renderer.ellipse(x, y, w, h);

        // Yolk tint centre
        renderer.setColor(1f, 0.95f, 0.4f, 0.7f);
        renderer.ellipse(x + 3f, y + 4f, w - 8f, h - 10f);
    }

    /**
     * Debug hitbox outlines for egg projectiles.
     * <b>Must be called between</b> {@code shapeRenderer.begin(ShapeType.Line)}
     * and {@code shapeRenderer.end()}.
     */
    public void renderDebug(ShapeRenderer renderer, Array<Projectile> projectiles) {
        for (Projectile p : projectiles) {
            if (p.isActive()) {
                renderer.rect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
            }
        }
    }
}
