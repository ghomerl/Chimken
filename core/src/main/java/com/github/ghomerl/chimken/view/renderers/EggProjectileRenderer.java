package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;
import com.github.ghomerl.chimken.view.assets.Assets;


public class EggProjectileRenderer {


    public void render(Batch batch, Array<Projectile> projectiles) {
        Texture tex = Assets.eggTexture;
        if (tex == null) return;
        for (Projectile p : projectiles) {
            if (!p.isActive()) {
                continue;
            }
            batch.draw(tex,
                p.getX(), p.getY(),
                p.getWidth(), p.getHeight());
        }
    }


    public void renderDebug(ShapeRenderer renderer, Array<Projectile> projectiles) {
        for (Projectile p : projectiles) {
            if (p.isActive()) {
                renderer.rect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
            }
        }
    }
}
