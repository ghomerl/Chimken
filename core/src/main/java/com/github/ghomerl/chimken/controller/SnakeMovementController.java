package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;

/**
 * Drives a centipede / snake movement pattern for a line of
 * enemies.  The leader (tracked via {@link #leaderIndex}) hugs
 * the screen boundaries:
 * <ol>
 *   <li>moves LEFT until hitting the left edge</li>
 *   <li>moves DOWN a fixed distance</li>
 *   <li>moves RIGHT until hitting the right edge</li>
 *   <li>moves DOWN a fixed distance</li>
 *   <li>repeats</li>
 * </ol>
 * <p>
 * Every chicken maintains its own position trail.  Each follower
 * reads from the trail of the <b>next alive chicken directly ahead
 * of it</b>, delayed by {@link #SPACING} frames.  This means:
 * <ul>
 *   <li>Killing the front chicken promotes the next alive one;
 *       the rest of the chain follows the new leader seamlessly.</li>
 *   <li>Killing a chicken in the middle causes the chicken behind
 *       it to follow the next alive chicken ahead, so the chain
 *       never breaks.</li>
 * </ul>
 * <p>
 * Enemies that fall below the screen are automatically marked
 * as not-alive so the wave-cleared check works naturally.
 */
public final class SnakeMovementController {

    private static final float SPEED = 150f;
    /** Frames of history delay between consecutive chickens. */
    private static final int SPACING = 22;
    /** How far the leader descends before turning horizontally. */
    private static final float DROP_AMOUNT = 130f;

    private enum Dir { LEFT, DOWN, RIGHT }

    private final Array<Enemy> snake;
    private final float worldWidth;
    private final float worldHeight;

    /** Per-chicken position trail (one Array per snake segment). */
    private final Array<Array<Vector2>> trails;

    /** Index of the current leader in the {@code snake} array. */
    private int leaderIndex = 0;

    private Dir dir = Dir.LEFT;
    private Dir nextHorizontal = Dir.RIGHT;
    private float downTargetY;

    public SnakeMovementController(Array<Enemy> snake,
                                   float worldWidth, float worldHeight) {
        this.snake = snake;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        // Initialise an empty trail for every chicken
        trails = new Array<>(true, snake.size, Array.class);
        for (int i = 0; i < snake.size; i++) {
            trails.add(new Array<Vector2>());
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  Per-frame update
    // ══════════════════════════════════════════════════════════════

    public void update(float delta) {
        // Advance leaderIndex past any dead chickens at the front
        promoteLeaderIfNeeded();

        if (leaderIndex >= snake.size) {
            // All chickens are dead — nothing to move
            return;
        }

        // ── Move the leader ────────────────────────────────────
        Enemy leader = snake.get(leaderIndex);
        if (leader.isAlive()) {
            moveLeader(leader, delta);
        }

        // Record the leader's current position in its own trail
        Array<Vector2> leaderTrail = trails.get(leaderIndex);
        leaderTrail.add(new Vector2(leader.getX(), leader.getY()));
        trimTrail(leaderTrail);

        // ── Move each follower ─────────────────────────────────
        for (int i = leaderIndex + 1; i < snake.size; i++) {
            Enemy e = snake.get(i);
            if (!e.isAlive()) continue;

            // Find the next alive chicken ahead of this one
            int aheadIdx = findNextAliveAhead(i);
            if (aheadIdx < 0) continue; // should not happen

            Array<Vector2> aheadTrail = trails.get(aheadIdx);
            int trailIdx = aheadTrail.size - 1 - SPACING;
            if (trailIdx >= 0) {
                Vector2 p = aheadTrail.get(trailIdx);
                e.setX(p.x);
                e.setY(p.y);
            }
            // else: trail not long enough yet — chicken stays at spawn

            // Record this chicken's position in its own trail
            Array<Vector2> myTrail = trails.get(i);
            myTrail.add(new Vector2(e.getX(), e.getY()));
            trimTrail(myTrail);
        }

        // Mark chickens that fell off the bottom as dead
        for (Enemy e : snake) {
            if (!e.isAlive()) continue;
            if (e.getY() < -e.getHeight() * 3f) {
                e.setAlive(false);
            }
        }
    }

    /**
     * Returns the index of the nearest alive chicken whose index
     * is strictly less than {@code currentIndex}, or {@code -1}
     * if none exists (should not happen when there is a leader).
     */
    private int findNextAliveAhead(int currentIndex) {
        for (int j = currentIndex - 1; j >= leaderIndex; j--) {
            if (snake.get(j).isAlive()) return j;
        }
        return -1;
    }

    /**
     * Keeps a trail from growing unbounded.  Only the most
     * recent {@link #SPACING} + 2 entries are needed.
     */
    private void trimTrail(Array<Vector2> trail) {
        int maxNeeded = SPACING + 2;
        if (trail.size > maxNeeded) {
            trail.removeRange(0, trail.size - maxNeeded);
        }
    }

    /**
     * If the current leader is dead, skip past it (and any
     * subsequent dead chickens) so the next alive one leads.
     */
    private void promoteLeaderIfNeeded() {
        while (leaderIndex < snake.size
            && !snake.get(leaderIndex).isAlive()) {
            leaderIndex++;
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  Internal
    // ══════════════════════════════════════════════════════════════

    private void moveLeader(Enemy leader, float delta) {
        float step = SPEED * delta;

        switch (dir) {
            case LEFT:
                leader.setX(leader.getX() - step);
                if (leader.getX() <= 0f) {
                    leader.setX(0f);
                    beginDown(leader);
                    nextHorizontal = Dir.RIGHT;
                }
                break;

            case DOWN:
                leader.setY(leader.getY() - step);
                if (leader.getY() <= downTargetY) {
                    leader.setY(downTargetY);
                    dir = nextHorizontal;
                }
                break;

            case RIGHT:
                leader.setX(leader.getX() + step);
                if (leader.getX() >= worldWidth - leader.getWidth()) {
                    leader.setX(worldWidth - leader.getWidth());
                    beginDown(leader);
                    nextHorizontal = Dir.LEFT;
                }
                break;
        }
    }

    private void beginDown(Enemy leader) {
        dir = Dir.DOWN;
        downTargetY = leader.getY() - DROP_AMOUNT;
    }
}
