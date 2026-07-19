package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;
import com.github.ghomerl.chimken.model.entities.projectiles.SuperheatedMetalloidChunk;


public class SuperheatedMetalloidChunkRenderer {


    public void render(ShapeRenderer renderer, Array<Projectile> projectiles) {
        for (Projectile p : projectiles) {
            if (!p.isActive() || !(p instanceof SuperheatedMetalloidChunk)) {
                continue;
            }
            drawChunk(renderer, p);
        }
    }


    private void drawChunk(ShapeRenderer renderer, Projectile p) {
        float x = p.getX();
        float y = p.getY();
        float w = p.getWidth();
        float h = p.getHeight();

        renderer.setColor(1f, 0.4f, 0.05f, 0.35f);
        renderer.rect(x - 3f, y - 1f, w + 6f, h + 2f);


        renderer.setColor(1f, 0.6f, 0.1f, 1f);
        renderer.rect(x, y + 2f, w, h - 4f);


        renderer.setColor(1f, 0.95f, 0.7f, 1f);
        renderer.rect(x + 3f, y + 5f, w - 6f, h - 10f);


        float tipY = p.getDirectionY() > 0 ? y + h - 6f : y + 2f;
        renderer.setColor(Color.WHITE);
        renderer.rect(x + 2f, tipY, w - 4f, 4f);
    }


    public void renderDebug(ShapeRenderer renderer, Array<Projectile> projectiles) {
        for (Projectile p : projectiles) {
            if (p.isActive() && p instanceof SuperheatedMetalloidChunk) {
                renderer.rect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
            }
        }
    }
}
