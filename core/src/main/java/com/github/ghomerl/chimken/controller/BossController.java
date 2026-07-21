package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.model.entities.enemies.bosses.Boss;
import com.github.ghomerl.chimken.model.entities.weapons.BossWeapon;


public class BossController {


    private static final float FIRE_INTERVAL = 0.3f;
    private static final float PAUSE_DURATION = 1.5f;

    private static final int PHASE_1_SHOTS = 15;
    private static final int PHASE_2_SHOTS = 18;
    private static final int PHASE_3_SHOTS = 18;


    private static final float ROTATION_STEP = (float) Math.toRadians(15.0);


    private enum State { ENTERING, ATTACKING, PAUSING }
    private enum Phase { PHASE_1, PHASE_2, PHASE_3 }

    private final Boss boss;

    private State state = State.ENTERING;
    private Phase phase = Phase.PHASE_1;


    private int shotsFired = 0;
    private float timer = 0f;
    private float currentRotation = 0f;

    public BossController(Boss boss) {
        this.boss = boss;
    }


    public void update(float delta) {
        if (boss.getWeapon() != null) {
            boss.getWeapon().update(delta);
        }

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
                    fireVolley();
                    advanceRotation();
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


    private void beginPhase(Phase newPhase) {
        phase = newPhase;
        state = State.ATTACKING;
        shotsFired = 0;
        timer = 0f;
        currentRotation = 0f;
    }

    private void fireVolley() {
        if (!(boss.getWeapon() instanceof BossWeapon)) return;
        BossWeapon weapon = (BossWeapon) boss.getWeapon();
        float cx = boss.getX() + boss.getWidth() * 0.5f;
        float cy = boss.getY() + boss.getHeight() * 0.5f;
        weapon.fireCross(cx, cy, currentRotation);
    }


    private void advanceRotation() {
        switch (phase) {
            case PHASE_2:
                currentRotation -= ROTATION_STEP;
                break;
            case PHASE_3:
                currentRotation += ROTATION_STEP;
                break;
            case PHASE_1:
            default:
                // No rotation in phase 1.
                break;
        }
    }

    private int maxShotsForCurrentPhase() {
        switch (phase) {
            case PHASE_2: return PHASE_2_SHOTS;
            case PHASE_3: return PHASE_3_SHOTS;
            case PHASE_1:
            default:     return PHASE_1_SHOTS;
        }
    }

    private Phase nextPhase(Phase p) {
        switch (p) {
            case PHASE_1: return Phase.PHASE_2;
            case PHASE_2: return Phase.PHASE_3;
            case PHASE_3:
            default:     return Phase.PHASE_1;
        }
    }



    public Phase getPhase() {
        return phase;
    }

    public State getState() {
        return state;
    }

    public int getShotsFired() {
        return shotsFired;
    }
}
