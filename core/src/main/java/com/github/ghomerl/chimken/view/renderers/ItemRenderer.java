package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.github.ghomerl.chimken.model.entities.items.*;


public class ItemRenderer {

    public void render(ShapeRenderer r, Iterable<Item> items) {
        for (Item item : items) {
            if (!item.isActive()) continue;
            float x = item.getX(), y = item.getY();
            float w = item.getWidth(), h = item.getHeight();

            if (item instanceof Drumstick)       drawDrumstick(r, x, y, w, h);
            else if (item instanceof Burger)     drawBurger(r, x, y, w, h);
            else if (item instanceof Roast)      drawRoast(r, x, y, w, h);
            else if (item instanceof KeyItem)    drawKey(r, x, y, w, h);
            else if (item instanceof PowerUpItem) drawPowerUp(r, x, y, w, h);
            else if (item instanceof GiftItem)   drawGift(r, x, y, w, h);
        }
    }

    // ── Food ─────────────────────────────────────────────────────

    private void drawDrumstick(ShapeRenderer r, float x, float y, float w, float h) {
        // Bone
        r.setColor(Color.WHITE);
        r.rect(x + w * 0.42f, y, w * 0.16f, h * 0.45f);
        // Meat
        r.setColor(0.65f, 0.35f, 0.15f, 1f);
        r.rect(x + w * 0.15f, y + h * 0.35f, w * 0.7f, h * 0.65f);
    }

    private void drawBurger(ShapeRenderer r, float x, float y, float w, float h) {
        float s = h * 0.2f;
        // Bottom bun
        r.setColor(0.85f, 0.6f, 0.25f, 1f);
        r.rect(x, y, w, s);
        // Patty
        r.setColor(0.4f, 0.25f, 0.12f, 1f);
        r.rect(x, y + s, w, s * 1.2f);
        // Lettuce
        r.setColor(0.3f, 0.7f, 0.2f, 1f);
        r.rect(x, y + s + s * 1.2f, w, s * 0.6f);
        // Top bun
        r.setColor(0.9f, 0.65f, 0.3f, 1f);
        r.rect(x, y + h - s * 1.2f, w, s * 1.2f);
    }

    private void drawRoast(ShapeRenderer r, float x, float y, float w, float h) {
        // Outer
        r.setColor(0.55f, 0.28f, 0.1f, 1f);
        r.ellipse(x, y, w, h);
        // Inner highlight
        r.setColor(0.7f, 0.4f, 0.18f, 1f);
        r.ellipse(x + 4f, y + 3f, w - 8f, h - 6f);
    }

    // ── Key ─────────────────────────────────────────────────────

    private void drawKey(ShapeRenderer r, float x, float y, float w, float h) {
        // Ring (circle approximation)
        float cx = x + w / 2f;
        float cy = y + h * 0.7f;
        float ringR = Math.min(w, h) * 0.3f;
        r.setColor(Color.GOLD);
        r.circle(cx, cy, ringR);
        r.setColor(0f, 0f, 0f, 1f);
        r.circle(cx, cy, ringR * 0.45f);
        // Shaft
        r.setColor(Color.GOLD);
        r.rect(cx - w * 0.1f, y, w * 0.2f, h * 0.55f);
        // Teeth
        r.rect(cx, y, w * 0.25f, h * 0.18f);
        r.rect(cx, y + h * 0.22f, w * 0.18f, h * 0.15f);
    }

    // ── Power-up & Gift ─────────────────────────────────────────

    private void drawPowerUp(ShapeRenderer r, float x, float y, float w, float h) {
        // Green glowing arrow
        r.setColor(0.15f, 0.85f, 0.3f, 0.4f);
        r.rect(x - 3f, y - 3f, w + 6f, h + 6f);
        r.setColor(0.2f, 1f, 0.35f, 1f);
        // Arrow pointing up
        float cx = x + w / 2f;
        r.triangle(cx, y + h, x, y + h * 0.4f, x + w, y + h * 0.4f);
        r.rect(cx - w * 0.2f, y, w * 0.4f, h * 0.45f);
    }

    private void drawGift(ShapeRenderer r, float x, float y, float w, float h) {
        // Box body
        r.setColor(0.55f, 0.15f, 0.65f, 1f);
        r.rect(x, y, w, h);
        // Ribbon horizontal
        r.setColor(1f, 0.85f, 0.1f, 1f);
        r.rect(x, y + h * 0.4f, w, h * 0.2f);
        // Ribbon vertical
        r.rect(x + w * 0.4f, y, w * 0.2f, h);
        // Lid
        r.setColor(0.7f, 0.2f, 0.8f, 1f);
        r.rect(x - 2f, y + h * 0.75f, w + 4f, h * 0.25f);
    }

    // ── Debug ───────────────────────────────────────────────────

    public void renderDebug(ShapeRenderer r, Iterable<Item> items) {
        for (Item item : items) {
            if (item.isActive()) {
                r.rect(item.getX(), item.getY(), item.getWidth(), item.getHeight());
            }
        }
    }
}
