package com.github.ghomerl.chimken.model.levels;

import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.enemies.ChickenEnemy;
import com.github.ghomerl.chimken.model.entities.enemies.DoubleEggChicken;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.entities.enemies.SniperChicken;
import com.github.ghomerl.chimken.model.entities.enemies.bosses.Boss1;
import com.github.ghomerl.chimken.model.entities.enemies.bosses.Boss2;
import com.github.ghomerl.chimken.model.entities.weapons.DoubleEggThrower;
import com.github.ghomerl.chimken.model.entities.weapons.EggThrower;
import com.github.ghomerl.chimken.model.entities.weapons.SniperEggThrower;


public final class LevelFactory {


    private static final float FORMATION_SPEED = 200f;


    private static final float MIN_GAP = 30f;

    private LevelFactory() {
    }


    //  Level 1


    public static Array<Wave> buildLevel1(float worldWidth, float worldHeight) {
        Array<Wave> waves = new Array<>();
        float cw = ChickenEnemy.DEFAULT_WIDTH;

        waves.add(build2x2Wave(cw, worldWidth, worldHeight));


        waves.add(build3x3Wave(cw, worldWidth, worldHeight));

        return waves;
    }


    //  Level 2  (Wave 3 in the game)


    public static Array<Wave> buildLevel2(float worldWidth, float worldHeight) {
        Array<Wave> waves = new Array<>();
        float cw = ChickenEnemy.DEFAULT_WIDTH;
        float spawnY = worldHeight * 0.7f;
        int hp = (int) Math.ceil(ChickenEnemy.getDefaultHp() * 1.1f);

        Wave wave = new Wave();
        wave.setMovementType(Wave.MovementType.SNAKE);

        for (int i = 0; i < 30; i++) {
            float spawnX = worldWidth + cw + i * cw;
            Enemy e = new ChickenEnemy(spawnX, spawnY,
                cw, cw, 0f, hp, 1, 100, new EggThrower());
            // Target equals spawn — movement is handled by SnakeMovementController
            wave.add(new SpawnEntry(e, spawnX, spawnY));
        }

        waves.add(wave);
        return waves;
    }

    //  Level 4  (Wave 4 in the game)


    public static Array<Wave> buildLevel4(float worldWidth, float worldHeight) {
        Array<Wave> waves = new Array<>();

        float cw = ChickenEnemy.DEFAULT_WIDTH;   // 64
        float sw = SniperChicken.DEFAULT_WIDTH;  // 56
        float ch = ChickenEnemy.DEFAULT_HEIGHT;  // 64
        float sh = SniperChicken.DEFAULT_HEIGHT; // 56

        // HP multiplier applied to every enemy in this wave
        int sniperHp = (int) Math.ceil(SniperChicken.getDefaultHp() * 1.2f); // 4 → 5
        int chickenHp = (int) Math.ceil(ChickenEnemy.getDefaultHp() * 1.2f); // 3 → 4

        Wave wave = new Wave();
        wave.setMovementType(Wave.MovementType.FORMATION);

        // ── Top row: 10 snipers in a horizontal line ────────────
        float sniperLineY = worldHeight * 0.75f;
        float sniperSpacing = sw + MIN_GAP;            // 86 px center-to-center
        float sniperRowWidth = 10 * sw + 9 * MIN_GAP;  // 830 px
        float sniperStartX = (worldWidth - sniperRowWidth) / 2f;

        // Target positions: indices 0..9 left-to-right
        // Spawn: first 5 from the right (off-screen right at target Y),
        // last  5 from the left  (off-screen left  at target Y).
        for (int i = 0; i < 10; i++) {
            float tx = sniperStartX + i * sniperSpacing;
            float ty = sniperLineY;
            float spawnX = (i < 5) ? worldWidth + sw : -sw;
            float spawnY = ty;
            Enemy e = new SniperChicken(spawnX, spawnY,
                sw, sh, FORMATION_SPEED, sniperHp, 1, 300,
                new SniperEggThrower());
            wave.add(new SpawnEntry(e, tx, ty));
        }

        // ── Three chicken rows beneath the sniper line ─────────
        float chickenSpacing = cw + MIN_GAP;           // 94 px center-to-center
        float chickenRowWidth = 16 * cw + 15 * MIN_GAP; // 1474 px
        float chickenStartX = (worldWidth - chickenRowWidth) / 2f;

        // Vertical step from one row to the next.
        // The first row sits MIN_GAP below the sniper line edge.
        float sniperBottom = sniperLineY; // sniper Y is its bottom edge
        float firstRowY = sniperBottom - MIN_GAP - ch;       // bottom of row 1
        float rowStep = ch + MIN_GAP;                         // 94 px

        for (int row = 0; row < 3; row++) {
            float rowY = firstRowY - row * rowStep;
            // 16 chickens: first 8 from the left, last 8 from the right
            for (int col = 0; col < 16; col++) {
                float tx = chickenStartX + col * chickenSpacing;
                float ty = rowY;
                float spawnX = (col < 8) ? -cw : worldWidth + cw;
                float spawnY = ty;
                Enemy e = new ChickenEnemy(spawnX, spawnY,
                    cw, ch, FORMATION_SPEED, chickenHp, 1, 100,
                    new EggThrower());
                wave.add(new SpawnEntry(e, tx, ty));
            }
        }

        waves.add(wave);
        return waves;
    }

    //  Level 5  (Wave 5 in the game)


    public static Array<Wave> buildLevel5(float worldWidth, float worldHeight) {
        Array<Wave> waves = new Array<>();

        float cw = ChickenEnemy.DEFAULT_WIDTH;  // 64
        float ch = ChickenEnemy.DEFAULT_HEIGHT; // 64
        int hp = ChickenEnemy.getDefaultHp();       // 3 (no multiplier this wave)

        Wave wave = new Wave();
        wave.setMovementType(Wave.MovementType.ZIGZAG);

        // 3 main columns centred at 25%, 50%, 75% of the world width
        float[] columnCentres = {
            worldWidth * 0.25f,
            worldWidth * 0.50f,
            worldWidth * 0.75f
        };

        // Sub-column offsets: 2 sub-columns straddle the column centre
        float subColOffset = (cw + MIN_GAP) / 2f;   // 47 px

        // Vertical layout: 10 rows, top row starts above the screen
        float rowSpacing = ch + MIN_GAP;            // 94 px
        float topRowY = worldHeight + ch;           // first row off-screen above
        int rows = 10;

        for (int col = 0; col < columnCentres.length; col++) {
            float centerX = columnCentres[col];
            for (int sub = 0; sub < 2; sub++) {
                float subX = centerX + (sub == 0 ? -subColOffset : subColOffset);
                for (int row = 0; row < rows; row++) {
                    float spawnX = subX;
                    float spawnY = topRowY + row * rowSpacing;
                    Enemy e = new ChickenEnemy(spawnX, spawnY,
                        cw, ch, 0f, hp, 1, 100, new EggThrower());
                    // Target equals spawn — ZigzagMovementController drives motion
                    wave.add(new SpawnEntry(e, spawnX, spawnY));
                }
            }
        }

        waves.add(wave);
        return waves;
    }

    //  Level 6  (Wave 6 in the game)


    public static Array<Wave> buildLevel6(float worldWidth, float worldHeight) {
        Array<Wave> waves = new Array<>();

        float dw = DoubleEggChicken.DEFAULT_WIDTH;   // 72
        float dh = DoubleEggChicken.DEFAULT_HEIGHT;  // 72
        int hp = DoubleEggChicken.getDefaultHp();        // 5

        Wave wave = new Wave();
        wave.setMovementType(Wave.MovementType.FORMATION);

        float cx = worldWidth / 2f;
        float cy = worldHeight * 0.75f;
        float gap = MIN_GAP;
        float gridW = dw * 3 + gap * 2;

        float startX = cx - gridW / 2f;
        float startY = cy - gridW / 2f;

        // 9 target positions (row-major, top row first)
        float[][] targets = new float[9][2];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int idx = (2 - row) * 3 + col;   // top row = indices 0,1,2
                targets[idx][0] = startX + col * (dw + gap);
                targets[idx][1] = startY + row * (dh + gap);
            }
        }

        // 3 from right (0-2), 3 from left (3-5), 3 from top (6-8)
        for (int i = 0; i < 3; i++) {
            Enemy e = new DoubleEggChicken(worldWidth + dw, targets[i][1],
                dw, dh, FORMATION_SPEED, hp, 1, 200, new DoubleEggThrower());
            wave.add(new SpawnEntry(e, targets[i][0], targets[i][1]));
        }
        for (int i = 3; i < 6; i++) {
            Enemy e = new DoubleEggChicken(-dw, targets[i][1],
                dw, dh, FORMATION_SPEED, hp, 1, 200, new DoubleEggThrower());
            wave.add(new SpawnEntry(e, targets[i][0], targets[i][1]));
        }
        for (int i = 6; i < 9; i++) {
            Enemy e = new DoubleEggChicken(targets[i][0], worldHeight + dh,
                dw, dh, FORMATION_SPEED, hp, 1, 200, new DoubleEggThrower());
            wave.add(new SpawnEntry(e, targets[i][0], targets[i][1]));
        }

        waves.add(wave);
        return waves;
    }

    // ══════════════════════════════════════════════════════════════
    //  Level 7  (Wave 7 in the game — the boss fight)
    // ══════════════════════════════════════════════════════════════


    public static Array<Wave> buildLevel7(float worldWidth, float worldHeight) {
        Array<Wave> waves = new Array<>();

        float bw = Boss1.DEFAULT_WIDTH;
        float bh = Boss1.DEFAULT_HEIGHT;

        // Spawn just above the top edge, horizontally centred.
        float spawnX = worldWidth / 2f - bw / 2f;
        float spawnY = worldHeight + bh;
        // Target: exact centre of the world.
        float targetX = spawnX;
        float targetY = worldHeight / 2f - bh / 2f;

        Wave wave = new Wave();
        wave.setMovementType(Wave.MovementType.FORMATION);

        Boss1 boss = new Boss1(spawnX, spawnY);
        wave.add(new SpawnEntry(boss, targetX, targetY));

        waves.add(wave);
        return waves;
    }

    //  Level 8  (Wave 8 — snake of double-egg chickens)


    public static Array<Wave> buildLevel8(float worldWidth, float worldHeight) {
        Array<Wave> waves = new Array<>();
        float dw = DoubleEggChicken.DEFAULT_WIDTH;   // 72
        float dh = DoubleEggChicken.DEFAULT_HEIGHT;  // 72
        float spawnY = worldHeight * 0.7f;
        int hp = (int) Math.ceil(DoubleEggChicken.getDefaultHp() * 1.5f); // 5 → 8

        Wave wave = new Wave();
        wave.setMovementType(Wave.MovementType.SNAKE);

        for (int i = 0; i < 30; i++) {
            float spawnX = worldWidth + dw + i * dw;
            Enemy e = new DoubleEggChicken(spawnX, spawnY,
                dw, dh, 0f, hp, 1, 200, new DoubleEggThrower());
            // Target equals spawn — movement is handled by SnakeMovementController
            wave.add(new SpawnEntry(e, spawnX, spawnY));
        }

        waves.add(wave);
        return waves;
    }


    //  Level 9  (Wave 9 — zigzag with snipers + double-egg chickens)


    public static Array<Wave> buildLevel9(float worldWidth, float worldHeight) {
        Array<Wave> waves = new Array<>();

        float dw = DoubleEggChicken.DEFAULT_WIDTH;   // 72
        float dh = DoubleEggChicken.DEFAULT_HEIGHT;  // 72
        int doubleHp = (int) Math.ceil(DoubleEggChicken.getDefaultHp() * 1.5f); // 5 → 8
        int sniperHp = (int) Math.ceil(SniperChicken.getDefaultHp() * 1.5f);    // 4 → 6

        Wave wave = new Wave();
        wave.setMovementType(Wave.MovementType.ZIGZAG);

        float[] columnCentres = {
            worldWidth * 0.25f,
            worldWidth * 0.50f,
            worldWidth * 0.75f
        };

        // Uniform spacing based on the larger enemy (DoubleEggChicken).
        float subColOffset = (dw + MIN_GAP) / 2f;   // 51
        float rowSpacing = dh + MIN_GAP;            // 102
        float topRowY = worldHeight + dh;           // first row off-screen above
        int rows = 10;

        for (int col = 0; col < columnCentres.length; col++) {
            float centerX = columnCentres[col];
            boolean isMiddle = (col == 1);  // middle column → snipers

            for (int sub = 0; sub < 2; sub++) {
                float subX = centerX + (sub == 0 ? -subColOffset : subColOffset);
                for (int row = 0; row < rows; row++) {
                    float spawnX = subX;
                    float spawnY = topRowY + row * rowSpacing;
                    Enemy e;
                    if (isMiddle) {
                        e = new SniperChicken(spawnX, spawnY,
                            SniperChicken.DEFAULT_WIDTH, SniperChicken.DEFAULT_HEIGHT,
                            0f, sniperHp, 1, 300, new SniperEggThrower());
                    } else {
                        e = new DoubleEggChicken(spawnX, spawnY,
                            dw, dh, 0f, doubleHp, 1, 200, new DoubleEggThrower());
                    }
                    wave.add(new SpawnEntry(e, spawnX, spawnY));
                }
            }
        }

        waves.add(wave);
        return waves;
    }


    //  Level 10  (Wave 10 — formation with double-egg chickens)


    public static Array<Wave> buildLevel10(float worldWidth, float worldHeight) {
        Array<Wave> waves = new Array<>();

        float dw = DoubleEggChicken.DEFAULT_WIDTH;   // 72
        float dh = DoubleEggChicken.DEFAULT_HEIGHT;  // 72
        float sw = SniperChicken.DEFAULT_WIDTH;      // 56
        float sh = SniperChicken.DEFAULT_HEIGHT;     // 56

        int sniperHp = (int) Math.ceil(SniperChicken.getDefaultHp() * 1.8f);    // 4 → 8
        int doubleHp = (int) Math.ceil(DoubleEggChicken.getDefaultHp() * 1.8f); // 5 → 9

        Wave wave = new Wave();
        wave.setMovementType(Wave.MovementType.FORMATION);

        // ── Top row: 10 snipers in a horizontal line ────────────
        float sniperLineY = worldHeight * 0.75f;
        float sniperSpacing = sw + MIN_GAP;            // 86
        float sniperRowWidth = 10 * sw + 9 * MIN_GAP;  // 830
        float sniperStartX = (worldWidth - sniperRowWidth) / 2f;

        for (int i = 0; i < 10; i++) {
            float tx = sniperStartX + i * sniperSpacing;
            float ty = sniperLineY;
            float spawnX = (i < 5) ? worldWidth + sw : -sw;
            float spawnY = ty;
            Enemy e = new SniperChicken(spawnX, spawnY,
                sw, sh, FORMATION_SPEED, sniperHp, 1, 300,
                new SniperEggThrower());
            wave.add(new SpawnEntry(e, tx, ty));
        }

        // ── Three double-egg-chicken rows beneath the sniper line ─
        float chickenSpacing = dw + MIN_GAP;            // 102
        float chickenRowWidth = 16 * dw + 15 * MIN_GAP; // 1602
        float chickenStartX = (worldWidth - chickenRowWidth) / 2f;

        float sniperBottom = sniperLineY;
        float firstRowY = sniperBottom - MIN_GAP - dh;  // bottom of row 1
        float rowStep = dh + MIN_GAP;                   // 102

        for (int row = 0; row < 3; row++) {
            float rowY = firstRowY - row * rowStep;
            for (int col = 0; col < 16; col++) {
                float tx = chickenStartX + col * chickenSpacing;
                float ty = rowY;
                float spawnX = (col < 8) ? -dw : worldWidth + dw;
                float spawnY = ty;
                Enemy e = new DoubleEggChicken(spawnX, spawnY,
                    dw, dh, FORMATION_SPEED, doubleHp, 1, 200,
                    new DoubleEggThrower());
                wave.add(new SpawnEntry(e, tx, ty));
            }
        }

        waves.add(wave);
        return waves;
    }


    public static Array<Wave> buildLevel11(float worldWidth, float worldHeight) {
        Array<Wave> waves = new Array<>();

        float bw = Boss2.DEFAULT_WIDTH;
        float bh = Boss2.DEFAULT_HEIGHT;

        // Spawn just above the top edge, horizontally centred.
        float spawnX = worldWidth / 2f - bw / 2f;
        float spawnY = worldHeight + bh;
        // Target: exact centre of the world.
        float targetX = spawnX;
        float targetY = worldHeight / 2f - bh / 2f;

        Wave wave = new Wave();
        wave.setMovementType(Wave.MovementType.FORMATION);

        Boss2 boss = new Boss2(spawnX, spawnY);
        wave.add(new SpawnEntry(boss, targetX, targetY));

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
