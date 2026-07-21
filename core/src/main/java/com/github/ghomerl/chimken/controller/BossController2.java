package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.Player;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.entities.enemies.SniperChicken;
import com.github.ghomerl.chimken.model.entities.enemies.bosses.Boss2;

public class BossController2 {


    private static final float FIRE_INTERVAL = 0.1f;
    private static final float PAUSE_DURATION = 1.5f;


    private static final int PHASE_1_SHOTS = 15;
    private static final int PHASE_2_SHOTS = 18;
    private static final int PHASE_3_SHOTS = 18;


    private static final float SNIPER_SPEED = 600f;


    private static final float FLANK_GAP = 10f;


    private enum State { ENTERING, ATTACKING, PAUSING }
    private enum Phase { PHASE_1, PHASE_2, PHASE_3 }

    private final Boss2 boss;
    private final Array<Enemy> enemies;
    private final Array<SniperChickenController> sniperControllers;
    private final Player player;

    private State state = State.ENTERING;
    private Phase phase = Phase.PHASE_1;

    private int shotsFired = 0;
    private float timer = 0f;
    private int pairsSpawned = 0;


    private final Array<MovingSniper> movingSnipers = new Array<>();


    private static final class MovingSniper {
        final SniperChicken sniper;
        final float targetX;

        MovingSniper(SniperChicken sniper, float targetX) {
            this.sniper = sniper;
            this.targetX = targetX;
        }
    }


    public BossController2(Boss2 boss, Array<Enemy> enemies,
                           Array<SniperChickenController> sniperControllers,
                           Player player) {
        this.boss = boss;
        this.enemies = enemies;
        this.sniperControllers = sniperControllers;
        this.player = player;
    }


    public void update(float delta) {
        updateMovingSnipers(delta);

        if (!boss.isAlive()) return;

        switch (state) {
            case ENTERING:
                if (boss.getSpeed() <= 0f) {
                    beginPhase(Phase.PHASE_1);
                }
                break;

            case ATTACKING:
                timer += delta;
                if (timer >= FIRE_INTERVAL) {
                    timer -= FIRE_INTERVAL;
                    spawnSniperPair();
                    shotsFired++;
                    if (shotsFired >= maxShotsForCurrentPhase()) {
                        state = State.PAUSING;
                        timer = 0f;
                    }
                }
                break;

            case PAUSING:
                timer += delta;
                if (timer >= PAUSE_DURATION) {
                    beginPhase(nextPhase(phase));
                }
                break;
        }
    }


    private void spawnSniperPair() {
        float bossCenterX = boss.getX() + boss.getWidth() * 0.5f;
        float bossCenterY = boss.getY() + boss.getHeight() * 0.5f;
        float bossLeftEdge = boss.getX();
        float bossRightEdge = boss.getX() + boss.getWidth();

        float sw = SniperChicken.DEFAULT_WIDTH;
        float sh = SniperChicken.DEFAULT_HEIGHT;


        float yOffset = (pairsSpawned % 5) * 60f - 120f; // -120..+120
        float spawnY = bossCenterY - sh * 0.5f + yOffset;
        float spawnX = bossCenterX - sw * 0.5f;


        SniperChicken left = new SniperChicken(spawnX, spawnY);
        left.setSpeed(SNIPER_SPEED);
        enemies.add(left);
        sniperControllers.add(new SniperChickenController(left, player));
        float leftTargetX = bossLeftEdge - sw - FLANK_GAP;
        movingSnipers.add(new MovingSniper(left, leftTargetX));

        SniperChicken right = new SniperChicken(spawnX, spawnY);
        right.setSpeed(SNIPER_SPEED);
        enemies.add(right);
        sniperControllers.add(new SniperChickenController(right, player));
        float rightTargetX = bossRightEdge + FLANK_GAP;
        movingSnipers.add(new MovingSniper(right, rightTargetX));

        pairsSpawned++;
    }


    private void updateMovingSnipers(float delta) {
        float step = SNIPER_SPEED * delta;
        for (int i = movingSnipers.size - 1; i >= 0; i--) {
            MovingSniper ms = movingSnipers.get(i);
            SniperChicken s = ms.sniper;

            if (!s.isAlive()) {
                movingSnipers.removeIndex(i);
                continue;
            }

            float dx = ms.targetX - s.getX();
            if (Math.abs(dx) <= step) {
                s.setX(ms.targetX);
                s.setSpeed(0f);
                movingSnipers.removeIndex(i);
            } else {
                s.setX(s.getX() + Math.signum(dx) * step);
            }
        }
    }



    private void beginPhase(Phase newPhase) {
        phase = newPhase;
        state = State.ATTACKING;
        shotsFired = 0;
        timer = 0f;
    }

    private int maxShotsForCurrentPhase() {
        switch (phase) {
            case PHASE_2: return PHASE_2_SHOTS;
            case PHASE_3: return PHASE_3_SHOTS;
            default:     return PHASE_1_SHOTS;
        }
    }

    private Phase nextPhase(Phase p) {
        switch (p) {
            case PHASE_1: return Phase.PHASE_2;
            case PHASE_2: return Phase.PHASE_3;
            default:     return Phase.PHASE_1;
        }
    }
}
