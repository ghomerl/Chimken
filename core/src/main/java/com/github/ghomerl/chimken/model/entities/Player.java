package com.github.ghomerl.chimken.model.entities;

import com.badlogic.gdx.math.Rectangle;
import com.github.ghomerl.chimken.model.KeyBindings;
import com.github.ghomerl.chimken.model.entities.weapons.PlasmaBlaster;
import com.github.ghomerl.chimken.model.entities.weapons.Weapon;


public class Player {

    public static final float DEFAULT_WIDTH = 64;
    public static final float DEFAULT_HEIGHT = 80;
    public static final float DEFAULT_SPEED = 400;

    private float x;
    private float y;
    private float width;
    private float height;
    private float speed;

    private int hp;
    private int missileCount;
    private Weapon weapon;
    private int keysObtained;
    private int foodObtained;
    private int killCount;
    private int deathCount;
    private int totalPoints;
    private int weaponLevel = 1;

    private boolean alive = true;
    private boolean visible = true;
    private boolean invincible = false;

    private final KeyBindings keyBindings;

    public Player(KeyBindings keyBindings, float startX, float startY) {
        this.keyBindings = keyBindings != null ? keyBindings : new KeyBindings();
        this.x = startX;
        this.y = startY;
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.speed = DEFAULT_SPEED;

        this.hp = 5;
        this.missileCount = 0;
        this.weapon = new PlasmaBlaster();
        this.keysObtained = 0;
        this.foodObtained = 0;
        this.killCount = 0;
        this.deathCount = 0;
        this.totalPoints = 0;
    }

    // ── Position & Dimensions ──────────────────────────────────────

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }

    // ── Stats ──────────────────────────────────────────────────────

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMissileCount() {
        return missileCount;
    }

    public void setMissileCount(int missileCount) {
        this.missileCount = missileCount;
    }

    public int getKeysObtained() {
        return keysObtained;
    }

    public void setKeysObtained(int keysObtained) {
        this.keysObtained = keysObtained;
    }

    public int getFoodObtained() {
        return foodObtained;
    }

    public void setFoodObtained(int foodObtained) {
        this.foodObtained = foodObtained;
    }

    public int getKillCount() {
        return killCount;
    }

    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    public int getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    // ── Weapon Level ──────────────────────────────────────────

    public int getWeaponLevel() {
        return weaponLevel;
    }

    public void setWeaponLevel(int weaponLevel) {
        this.weaponLevel = Math.max(1, weaponLevel);
    }

    // ── State ───────────────────────────────────────────────────

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    // ── Weapon ─────────────────────────────────────────────────────

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        if (this.weapon != null) {
            this.weapon.dispose();
        }
        this.weapon = weapon;
    }

    // ── Key Bindings ───────────────────────────────────────────────

    public KeyBindings getKeyBindings() {
        return keyBindings;
    }

    // ── Lifecycle ──────────────────────────────────────────────────

    public void dispose() {
        if (weapon != null) {
            weapon.dispose();
        }
    }
}
