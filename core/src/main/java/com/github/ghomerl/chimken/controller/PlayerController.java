package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.InputAdapter;
import com.github.ghomerl.chimken.controller.audio.SfxManager;
import com.github.ghomerl.chimken.model.KeyBindings;
import com.github.ghomerl.chimken.model.entities.Player;
import com.github.ghomerl.chimken.model.entities.weapons.BoronRailgun;
import com.github.ghomerl.chimken.model.entities.weapons.PlasmaBlaster;


public class PlayerController extends InputAdapter {


    public static final int RESPAWN_RISE = 0;
    public static final int RESPAWN_TELEPORT = 1;


    private enum State { NORMAL, HIT_WAIT, RESPAWNING, INVINCIBLE }


    private static final float HIT_WAIT_DURATION   = 0.5f;
    private static final float RESPAWN_RISE_SPEED  = 600f;
    private static final float INVINCIBLE_DURATION = 2f;
    private static final float SPAWN_Y             = 100f;


    private final Player player;
    private final float worldWidth;
    private final float worldHeight;


    private State state = State.NORMAL;
    private float stateTimer;
    private boolean playerDead;
    private int respawnType;


    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean attackPressed;
    private boolean missilePressed;


    private boolean missileRequested;


    public PlayerController(Player player, float worldWidth, float worldHeight) {
        this.player = player;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }



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


    public void onPlayerHit(int respawnType) {
        player.setHp(player.getHp() - 1);
        player.setVisible(false);
        player.setInvincible(false);
        this.respawnType = respawnType;
        state = State.HIT_WAIT;
        stateTimer = HIT_WAIT_DURATION;
    }


    public boolean isPlayerDead() {
        return playerDead;
    }


    public boolean consumeMissileRequest() {
        boolean r = missileRequested;
        missileRequested = false;
        return r;
    }



    public void update(float delta) {
        missileRequested = false;

        // Weapon projectiles always tick (even while hit)
        player.getWeapon().update(delta);

        switch (state) {
            case NORMAL:
                applyMovement(delta);
                applyShooting();
                checkMissileInput();
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
                checkMissileInput();
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
        if (keycode == kb.getUp())          upPressed    = pressed;
        else if (keycode == kb.getDown())        downPressed  = pressed;
        else if (keycode == kb.getLeft())        leftPressed  = pressed;
        else if (keycode == kb.getRight())       rightPressed = pressed;
        else if (keycode == kb.getAttack())      attackPressed = pressed;
        else if (keycode == kb.getMissile())     missilePressed = pressed;
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
            int before = player.getWeapon().getProjectiles().size;
            player.getWeapon().fireLeveled(
                player.getX() + player.getWidth() * 0.5f,
                player.getY() + player.getHeight(),
                1f,
                player.getWeaponLevel()
            );
            int after = player.getWeapon().getProjectiles().size;
            if (after > before) {
                if (player.getWeapon() instanceof BoronRailgun) {
                    SfxManager.playBoronShot();
                } else if (player.getWeapon() instanceof PlasmaBlaster) {
                    SfxManager.playPlasmaShot();
                }
            }
        }
    }

    private void checkMissileInput() {
        if (missilePressed && player.getMissileCount() > 0) {
            missileRequested = true;
        }
    }
}
