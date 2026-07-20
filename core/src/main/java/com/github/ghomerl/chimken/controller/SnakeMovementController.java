package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;

/**
 * Drives a centipede / snake movement pattern for a line of
 * enemies.  The leader (index 0) hugs the screen boundaries:
 * <ol>
 *   <li>moves LEFT until hitting the left edge</li>
 *   <li>moves DOWN a fixed distance</li>
 *   <li>moves RIGHT until hitting the right edge</li>
 *   <li>moves DOWN a fixed distance</li>
 *   <li>repeats</li>
 * </ol>
 * Every other chicken in the line follows the leader's
 * position history with an increasing frame delay, producing
 * a smooth snake trail.
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

    /** Full position history of the leader (one entry per frame). */
    private final Array<Vector2> history = new Array<>();

    private Dir dir = Dir.LEFT;
    private Dir nextHorizontal = Dir.RIGHT;
    private float downTargetY;

    public SnakeMovementController(Array<Enemy> snake,
                                    float worldWidth, float worldHeight) {
        this.snake = snake;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    // ══════════════════════════════════════════════════════════════
    //  Per-frame update
    // ══════════════════════════════════════════════════════════════

    public void update(float delta) {
        Enemy leader = snake.get(0);
        if (leader.isAlive()) {
            moveLeader(leader, delta);
            history.add(new Vector2(leader.getX(), leader.getY()));
        }

        // Position each follower from the leader's history
        for (int i = 1; i < snake.size; i++) {
            Enemy e = snake.get(i);
            if (!e.isAlive()) continue;
            int idx = history.size - 1 - i * SPACING;
            if (idx >= 0) {
                Vector2 p = history.get(idx);
                e.setX(p.x);
                e.setY(p.y);
            }
            // else: follower hasn't entered the screen yet — stays at spawn
        }

        // Mark chickens that fell off the bottom as dead
        for (Enemy e : snake) {
            if (!e.isAlive()) continue;
            if (e.getY() < -e.getHeight() * 3f) {
                e.setAlive(false);
            }
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