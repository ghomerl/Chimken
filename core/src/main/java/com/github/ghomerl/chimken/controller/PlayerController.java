package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.InputAdapter;
import com.github.ghomerl.chimken.model.KeyBindings;
import com.github.ghomerl.chimken.model.entities.Player;


public class PlayerController extends InputAdapter {

    private final Player player;
    private final float worldWidth;
    private final float worldHeight;

    // Tracked key states
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean attackPressed;


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


    public void update(float delta) {
        applyMovement(delta);
        applyShooting();
        player.getWeapon().update(delta);
    }


    private void updateKeyState(int keycode, boolean pressed) {
        KeyBindings kb = player.getKeyBindings();
        if (keycode == kb.getUp())          upPressed = pressed;
        else if (keycode == kb.getDown())   downPressed = pressed;
        else if (keycode == kb.getLeft())   leftPressed = pressed;
        else if (keycode == kb.getRight())  rightPressed = pressed;
        else if (keycode == kb.getAttack()) attackPressed = pressed;
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


        player.setX(Math.max(0f, Math.min(player.getX(), worldWidth - player.getWidth())));
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
