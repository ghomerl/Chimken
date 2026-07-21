package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;


public final class SnakeMovementController {

    private static final float SPEED = 150f;

    private static final int SPACING = 22;

    private static final float DROP_AMOUNT = 130f;

    private enum Dir { LEFT, DOWN, RIGHT }

    private final Array<Enemy> snake;
    private final float worldWidth;
    private final float worldHeight;


    private final Array<Array<Vector2>> trails;


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


    public void update(float delta) {
        promoteLeaderIfNeeded();

        if (leaderIndex >= snake.size) { return; }

        Enemy leader = snake.get(leaderIndex);
        if (leader.isAlive()) {
            moveLeader(leader, delta);
        }

        Array<Vector2> leaderTrail = trails.get(leaderIndex);
        leaderTrail.add(new Vector2(leader.getX(), leader.getY()));
        trimTrail(leaderTrail);


        for (int i = leaderIndex + 1; i < snake.size; i++) {
            Enemy e = snake.get(i);
            if (!e.isAlive()) continue;


            int aheadIdx = findNextAliveAhead(i);
            if (aheadIdx < 0) continue;

            Array<Vector2> aheadTrail = trails.get(aheadIdx);
            int trailIdx = aheadTrail.size - 1 - SPACING;
            if (trailIdx >= 0) {
                Vector2 p = aheadTrail.get(trailIdx);
                e.setX(p.x);
                e.setY(p.y);
            }

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


    private int findNextAliveAhead(int currentIndex) {
        for (int j = currentIndex - 1; j >= leaderIndex; j--) {
            if (snake.get(j).isAlive()) return j;
        }
        return -1;
    }


    private void trimTrail(Array<Vector2> trail) {
        int maxNeeded = SPACING + 2;
        if (trail.size > maxNeeded) {
            trail.removeRange(0, trail.size - maxNeeded);
        }
    }


    private void promoteLeaderIfNeeded() {
        while (leaderIndex < snake.size
            && !snake.get(leaderIndex).isAlive()) {
            leaderIndex++;
        }
    }


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
