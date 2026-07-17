package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.InputAdapter;
import com.github.ghomerl.chimken.model.KeyBindings;
import com.github.ghomerl.chimken.model.entities.Player;

/**
 * Handles all player-related input, game-logic updates, and
 * hit-respawn state management.
 * <p>
 * Implements {@link InputAdapter} so it can be registered as an
 * {@link com.badlogic.gdx.InputProcessor} in the game screen's
 * {@link com.badlogic.gdx.InputMultiplexer}.
 * <p>
 * The controller drives a four-state machine:
 * <ol>
 *   <li>{@link State#NORMAL}     — full control, vulnerable</li>
 *   <li>{@link State#HIT_WAIT}   — invisible, waiting 0.5 s</li>
 *   <li>{@link State#RESPAWNING} — rising from below the screen</li>
 *   <li>{@link State#INVINCIBLE} — blinking, can move/shoot, invulnerable</li>
 * </ol>
 */
public class PlayerController extends InputAdapter {

    // ── Respawn type ────────────────────────────────────────────────
    /** Rises from y = −height up to y = 0. */
    public static final int RESPAWN_RISE = 0;
    /** Teleports to the starting square. */
    public static final int RESPAWN_TELEPORT = 1;

    // ── Internal states ─────────────────────────────────────────────
    private enum State { NORMAL, HIT_WAIT, RESPAWNING, INVINCIBLE }

    // ── Timing constants ────────────────────────────────────────────
    private static final float HIT_WAIT_DURATION   = 0.5f;
    private static final float RESPAWN_RISE_SPEED  = 600f;
    private static final float INVINCIBLE_DURATION = 2f;
    private static final float SPAWN_Y             = 100f;

    // ── References ──────────────────────────────────────────────────
    private final Player player;
    private final float worldWidth;
    private final float worldHeight;

    // ── State machine ───────────────────────────────────────────────
    private State state = State.NORMAL;
    private float stateTimer;
    private boolean playerDead;
    private int respawnType;

    // ── Tracked key states ──────────────────────────────────────────
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean attackPressed;

    /**
     * @param player      the player model to control
     * @param worldWidth  the horizontal extent of the game world (viewport)
     * @param worldHeight the vertical extent of the game world (viewport)
     */
    public PlayerController(Player player, float worldWidth, float worldHeight) {
        this.player = player;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    // ── InputProcessor ─────────────────────────────────────────────

    @Override
    public boolean keyDown(int keycode) {
        updateKeyState(keycode, true);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        updateKeyState(keycode, false);
        return false;
    }

    // ── Hit / Death API ────────────────────────────────────────────

    /**
     * Called when the player takes damage.
     *
     * @param respawnType {@link #RESPAWN_RISE} (body collision) or
     *                   {@link #RESPAWN_TELEPORT} (egg hit)
     */
    public void onPlayerHit(int respawnType) {
        player.setHp(player.getHp() - 1);
        player.setVisible(false);
        player.setInvincible(false);
        this.respawnType = respawnType;
        state = State.HIT_WAIT;
        stateTimer = HIT_WAIT_DURATION;
    }

    /**
     * @return {@code true} once the 0.5 s hit-wait expires and HP ≤ 0.
     *         The game screen should switch to {@code GameOverScreen}
     *         and <b>not</b> call {@link #update(float)} again.
     */
    public boolean isPlayerDead() {
        return playerDead;
    }

    // ── Per-frame update ───────────────────────────────────────────

    public void update(float delta) {
        // Weapon projectiles always tick (even while hit)
        player.getWeapon().update(delta);

        switch (state) {
            case NORMAL:
                applyMovement(delta);
                applyShooting();
                break;

            case HIT_WAIT:
                stateTimer -= delta;
                if (stateTimer <= 0f) {
                    if (player.getHp() > 0) {
                        beginRespawn();
                    } else {
                        playerDead = true;
                    }
                }
                break;

            case RESPAWNING:
                player.setY(player.getY() + RESPAWN_RISE_SPEED * delta);
                if (player.getY() >= 0f) {
                    player.setY(0f);
                    beginInvincibility();
                }
                break;

            case INVINCIBLE:
                applyMovement(delta);
                applyShooting();
                stateTimer -= delta;
                // Blink at ~5 Hz
                player.setVisible(Math.floor(stateTimer * 10f) % 2 == 0);
                if (stateTimer <= 0f) {
                    state = State.NORMAL;
                    player.setVisible(true);
                    player.setInvincible(false);
                }
                break;
        }
    }

    // ── Private helpers ────────────────────────────────────────────

    private void beginRespawn() {
        if (respawnType == RESPAWN_TELEPORT) {
            // Teleport to the starting square
            player.setX(worldWidth / 2f - player.getWidth() / 2f);
            player.setY(SPAWN_Y);
            player.setVisible(true);
            beginInvincibility();
        } else {
            // Rise from below the screen
            player.setY(-player.getHeight());
            player.setVisible(true);
            state = State.RESPAWNING;
        }
    }

    private void beginInvincibility() {
        state = State.INVINCIBLE;
        stateTimer = INVINCIBLE_DURATION;
        player.setInvincible(true);
    }

    private void updateKeyState(int keycode, boolean pressed) {
        KeyBindings kb = player.getKeyBindings();
        if      (keycode == kb.getUp())          upPressed    = pressed;
        else if (keycode == kb.getDown())        downPressed  = pressed;
        else if (keycode == kb.getLeft())        leftPressed  = pressed;
        else if (keycode == kb.getRight())       rightPressed = pressed;
        else if (keycode == kb.getAttack())      attackPressed = pressed;
    }

    private void applyMovement(float delta) {
        float speed = player.getSpeed();
        float dx = 0f;
        float dy = 0f;

        if (upPressed)    dy += speed * delta;
        if (downPressed)  dy -= speed * delta;
        if (leftPressed)  dx -= speed * delta;
        if (rightPressed) dx += speed * delta;

        player.setX(player.getX() + dx);
        player.setY(player.getY() + dy);

        player.setX(Math.max(0f, Math.min(player.getX(), worldWidth  - player.getWidth())));
        player.setY(Math.max(0f, Math.min(player.getY(), worldHeight - player.getHeight())));
    }

    private void applyShooting() {
        if (attackPressed) {
            player.getWeapon().fire(
                player.getX() + player.getWidth() * 0.5f,
                player.getY() + player.getHeight(),
                1f
            );
        }
    }
}
