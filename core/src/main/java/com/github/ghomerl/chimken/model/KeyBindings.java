package com.github.ghomerl.chimken.model;

import com.badlogic.gdx.Input;

/**
 * Represents the key bindings for player controls.
 * Default bindings: WASD for movement, Space for attack, E for secondary attack, M for missile.
 */
public class KeyBindings {

    public static final int DEFAULT_UP = Input.Keys.W;
    public static final int DEFAULT_DOWN = Input.Keys.S;
    public static final int DEFAULT_LEFT = Input.Keys.A;
    public static final int DEFAULT_RIGHT = Input.Keys.D;
    public static final int DEFAULT_ATTACK = Input.Keys.SPACE;
    public static final int DEFAULT_SECONDARY_ATTACK = Input.Keys.E;
    public static final int DEFAULT_MISSILE = Input.Keys.M;

    private int up = DEFAULT_UP;
    private int down = DEFAULT_DOWN;
    private int left = DEFAULT_LEFT;
    private int right = DEFAULT_RIGHT;
    private int attack = DEFAULT_ATTACK;
    private int secondaryAttack = DEFAULT_SECONDARY_ATTACK;
    private int missile = DEFAULT_MISSILE;

    public KeyBindings() {
    }

    public KeyBindings(int up, int down, int left, int right,
                       int attack, int secondaryAttack, int missile) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.attack = attack;
        this.secondaryAttack = secondaryAttack;
        this.missile = missile;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getSecondaryAttack() {
        return secondaryAttack;
    }

    public void setSecondaryAttack(int secondaryAttack) {
        this.secondaryAttack = secondaryAttack;
    }

    public int getMissile() {
        return missile;
    }

    public void setMissile(int missile) {
        this.missile = missile;
    }
}
