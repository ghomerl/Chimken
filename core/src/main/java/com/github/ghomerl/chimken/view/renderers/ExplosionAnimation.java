package com.github.ghomerl.chimken.view.renderers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.github.ghomerl.chimken.view.assets.Assets;


public class ExplosionAnimation {


    public static final int FRAME_COLS = 6;
    public static final int FRAME_ROWS = 3;
    public static final int FRAME_COUNT = FRAME_COLS * FRAME_ROWS;

    private final float x;
    private final float y;
    private final float duration;
    private final float size;

    private float elapsed = 0f;
    private boolean finished = false;

    private final TextureRegion[] frames;


    public ExplosionAnimation(float x, float y, float duration, float size) {
        this.x = x;
        this.y = y;
        this.duration = Math.max(0.01f, duration);
        this.size = size;
        this.frames = splitSheet();
    }

    private static TextureRegion[] splitSheet() {
        TextureRegion[] out = new TextureRegion[FRAME_COUNT];
        if (Assets.explosionTexture == null) {
            return out;
        }
        TextureRegion[][] grid = TextureRegion.split(
            Assets.explosionTexture,
            Assets.explosionTexture.getWidth() / FRAME_COLS,
            Assets.explosionTexture.getHeight() / FRAME_ROWS);
        int idx = 0;
        for (int r = 0; r < FRAME_ROWS; r++) {
            for (int c = 0; c < FRAME_COLS; c++) {
                out[idx++] = grid[r][c];
            }
        }
        return out;
    }


    public void update(float delta) {
        if (finished) return;
        elapsed += delta;
        if (elapsed >= duration) {
            elapsed = duration;
            finished = true;
        }
    }


    public void render(Batch batch) {
        if (finished) return;
        if (Assets.explosionTexture == null) return;

        int frameIdx = currentFrameIndex();
        TextureRegion frame = frames[frameIdx];
        if (frame == null) return;

        batch.draw(frame,
            x - size * 0.5f, y - size * 0.5f,
            size, size);
    }


    private int currentFrameIndex() {
        float progress = elapsed / duration;            // 0..1
        int idx = (int) Math.floor(progress * FRAME_COUNT);
        return MathUtils.clamp(idx, 0, FRAME_COUNT - 1);
    }


    public boolean isFinished() {
        return finished;
    }
}
