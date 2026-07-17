package com.github.ghomerl.chimken.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;


public class Player {

    private static final float WIDTH = 64;
    private static final float HEIGHT = 80;
    private static final float SPEED = 400;

    private float x;
    private float y;
    private final KeyBindings keyBindings;
    private Texture texture;

    public Player(KeyBindings keyBindings, float startX, float startY) {
        this.keyBindings = keyBindings != null ? keyBindings : new KeyBindings();
        this.x = startX;
        this.y = startY;
        this.texture = createSpaceshipTexture();
    }

    private Texture createSpaceshipTexture() {
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);

        pixmap.setColor(Color.CYAN);
        pixmap.fillTriangle(32, 60, 8, 8, 56, 8);

        pixmap.setColor(0.2f, 0.6f, 1f, 1f);
        pixmap.fillTriangle(32, 60, 18, 20, 46, 20);

        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(28, 4, 8, 16);

        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(keyBindings.getUp())) y += SPEED * delta;
        if (Gdx.input.isKeyPressed(keyBindings.getDown())) y -= SPEED * delta;
        if (Gdx.input.isKeyPressed(keyBindings.getLeft())) x -= SPEED * delta;
        if (Gdx.input.isKeyPressed(keyBindings.getRight())) x += SPEED * delta;

        x = Math.max(0, Math.min(x, Gdx.graphics.getWidth() - WIDTH));
        y = Math.max(0, Math.min(y, Gdx.graphics.getHeight() - HEIGHT));
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, WIDTH, HEIGHT);
    }

    public void renderDebug(ShapeRenderer renderer) {
        renderer.rect(x, y, WIDTH, HEIGHT);
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
    }
}
