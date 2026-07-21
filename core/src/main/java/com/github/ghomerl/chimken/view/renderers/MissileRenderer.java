package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.projectiles.MissileProjectile;
import com.github.ghomerl.chimken.view.assets.Assets;


public class MissileRenderer {


    public void render(Batch batch, Array<MissileProjectile> missiles) {
        Texture tex = Assets.missileTexture;
        if (tex == null) return;
        for (MissileProjectile m : missiles) {
            if (!m.isActive() || m.hasExploded()) continue;
            float w = 24f;
            float h = 48f;
            batch.draw(tex,
                m.getX() - w * 0.5f, m.getY() - h * 0.5f,
                w, h);
        }
    }


    public void renderDebug(ShapeRenderer renderer, Array<MissileProjectile> missiles) {
        for (MissileProjectile m : missiles) {
            if (m.isActive() && !m.hasExploded()) {
                renderer.rect(m.getX() - 12f, m.getY() - 24f, 24f, 48f);
            }
        }
    }
}
