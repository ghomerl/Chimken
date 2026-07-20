package com.github.ghomerl.chimken.model.entities.enemies;

import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.weapons.EggThrower;

/**
 * Static factory that builds the wave sequence for each level.
 * <p>
 * To add a new level, add a new {@code buildLevelX()} method and
 * wire it into the game-screen setup.
 */
public final class LevelFactory {

    /** Movement speed for formation entry (pixels / second). */
    private static final float FORMATION_SPEED = 200f;

    private LevelFactory() {
    }

    // ══════════════════════════════════════════════════════════════
    //  Level 1
    // ══════════════════════════════════════════════════════════════

    /**
     * Builds Level 1 — one wave with two sequential phases.
     * <p>
     * <b>Phase A:</b> 4 chickens (2 from left, 2 from right) form a
     * 2x2 grid at the centre-top of the screen.
     * <br>
     * <b>Phase B:</b> 9 chickens (3 from left, 3 from right, 3 from
     * top) form a 3x3 grid at the centre-top of the screen.
     * <p>
     * Phase B spawns only after every enemy from Phase A is dead.
     *
     * @param worldWidth  the horizontal extent of the game world
     * @param worldHeight the vertical extent of the game world
     * @return array of waves (2 waves for level 1)
     */
    public static Array<Wave> buildLevel1(float worldWidth, float worldHeight) {
        Array<Wave> waves = new Array<>();
        float cw = ChickenEnemy.DEFAULT_WIDTH;

        // ── Phase A: 2x2 square (4 chickens) ───────────────────
        waves.add(build2x2Wave(cw, worldWidth, worldHeight));

        // ── Phase B: 3x3 square (9 chickens) ──────────────────
        waves.add(build3x3Wave(cw, worldWidth, worldHeight));

        return waves;
    }

    // ══════════════════════════════════════════════════════════════
    //  Level 2
    // ══════════════════════════════════════════════════════════════

    /**
     * Builds Level 2 — one phase of 20 normal chickens with
     * {@code 1.1x} base HP in a snake / centipede formation.
     * <p>
     * The chickens enter from the right side at the vertical
     * middle of the screen in a horizontal line.  The leader
     * hugs the screen edges (left → down → right → down → …)
     * while the rest of the line follows like a snake.
     * The wave ends when every chicken is killed or goes
     * out of bounds.
     *
     * @param worldWidth  the horizontal extent of the game world
     * @param worldHeight the vertical extent of the game world
     * @return array containing a single snake wave
     */
    public static Array<Wave> buildLevel2(float worldWidth, float worldHeight) {
        Array<Wave> waves = new Array<>();
        float cw = ChickenEnemy.DEFAULT_WIDTH;
        float spawnY = worldHeight * 0.5f;
        int hp = (int) Math.ceil(ChickenEnemy.DEFAULT_HP * 1.1f);

        Wave wave = new Wave();
        wave.setMovementType(Wave.MovementType.SNAKE);

        for (int i = 0; i < 20; i++) {
            float spawnX = worldWidth + cw + i * cw;
            Enemy e = new ChickenEnemy(spawnX, spawnY,
                cw, cw, 0f, hp, 1, 100, new EggThrower());
            // Target equals spawn — movement is handled by SnakeMovementController
            wave.add(new SpawnEntry(e, spawnX, spawnY));
        }

        waves.add(wave);
        return waves;
    }

    // ══════════════════════════════════════════════════════════════
    //  Level 1 phase builders
    // ══════════════════════════════════════════════════════════════

    private static Wave build2x2Wave(float cw, float ww, float wh) {
        Wave wave = new Wave();

        float cx = ww / 2f;
        float cy = wh * 0.75f;
        float gap = 8f;
        float gridW = cw * 2 + gap;

        float startX = cx - gridW / 2f;
        float startY = cy - gridW / 2f;

        // Target positions: TL, TR, BL, BR (top-left origin, row-major)
        float[][] targets = {
            {startX,              startY + cw + gap},     // TL
            {startX + cw + gap,   startY + cw + gap},     // TR
            {startX,              startY},                // BL
            {startX + cw + gap,   startY}                 // BR
        };

        // Spawn positions: 2 from right, 2 from left
        float[][] spawns = {
            {ww + cw, targets[0][1]},     // right → TL
            {ww + cw, targets[1][1]},     // right → TR
            {-cw,     targets[2][1]},     // left  → BL
            {-cw,     targets[3][1]}      // left  → BR
        };

        for (int i = 0; i < 4; i++) {
            Enemy e = new ChickenEnemy(spawns[i][0], spawns[i][1],
                cw, cw, FORMATION_SPEED, 3, 1, 100, new EggThrower());
            wave.add(new SpawnEntry(e, targets[i][0], targets[i][1]));
        }

        return wave;
    }

    private static Wave build3x3Wave(float cw, float ww, float wh) {
        Wave wave = new Wave();

        float cx = ww / 2f;
        float cy = wh * 0.75f;
        float gap = 8f;
        float gridW = cw * 3 + gap * 2;

        float startX = cx - gridW / 2f;
        float startY = cy - gridW / 2f;

        // 9 target positions (row-major, top row first)
        float[][] targets = new float[9][2];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int idx = (2 - row) * 3 + col;   // top row = indices 0,1,2
                targets[idx][0] = startX + col * (cw + gap);
                targets[idx][1] = startY + row * (cw + gap);
            }
        }

        // 3 from right (0-2), 3 from left (3-5), 3 from top (6-8)
        for (int i = 0; i < 3; i++) {
            Enemy e = new ChickenEnemy(ww + cw, targets[i][1],
                cw, cw, FORMATION_SPEED, 3, 1, 100, new EggThrower());
            wave.add(new SpawnEntry(e, targets[i][0], targets[i][1]));
        }
        for (int i = 3; i < 6; i++) {
            Enemy e = new ChickenEnemy(-cw, targets[i][1],
                cw, cw, FORMATION_SPEED, 3, 1, 100, new EggThrower());
            wave.add(new SpawnEntry(e, targets[i][0], targets[i][1]));
        }
        for (int i = 6; i < 9; i++) {
            Enemy e = new ChickenEnemy(targets[i][0], wh + cw,
                cw, cw, FORMATION_SPEED, 3, 1, 100, new EggThrower());
            wave.add(new SpawnEntry(e, targets[i][0], targets[i][1]));
        }

        return wave;
    }
}
