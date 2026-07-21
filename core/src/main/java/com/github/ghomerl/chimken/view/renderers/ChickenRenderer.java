package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.ghomerl.chimken.model.entities.enemies.ChickenEnemy;
import com.github.ghomerl.chimken.model.entities.enemies.DoubleEggChicken;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.entities.enemies.SniperChicken;
import com.github.ghomerl.chimken.view.assets.Assets;


public class ChickenRenderer {


    public void render(Batch batch, Enemy enemy) {
        if (!enemy.isAlive()) {
            return;
        }
        Texture tex = textureFor(enemy);
        if (tex == null) return;
        batch.draw(tex,
            enemy.getX(), enemy.getY(),
            enemy.getWidth(), enemy.getHeight());
    }

    private static Texture textureFor(Enemy enemy) {
        if (enemy instanceof SniperChicken) {
            return Assets.sniperChickenTexture;
        }
        if (enemy instanceof DoubleEggChicken) {
            return Assets.doubleChickenTexture;
        }
        // Default - covers ChickenEnemy and any future basic chicken.
        return Assets.chickenTexture;
    }


    public void renderDebug(ShapeRenderer renderer, Enemy enemy) {
        if (enemy.isAlive()) {
            renderer.rect(enemy.getX(), enemy.getY(),
                enemy.getWidth(), enemy.getHeight());
        }
    }
}
