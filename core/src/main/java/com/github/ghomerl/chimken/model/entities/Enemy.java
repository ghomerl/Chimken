package com.github.ghomerl.chimken.model.entities;

import com.badlogic.gdx.math.Rectangle;
import com.github.ghomerl.chimken.model.entities.weapons.Weapon;


public class Enemy {

    private float x;
    private float y;
    private float width;
    private float height;
    private float speed;
    private int hp;
    private int maxHp;
    private int damage;
    private boolean alive;
    private int points;
    private Weapon weapon;

    public Enemy(float x, float y, float width, float height,
                 float speed, int hp, int damage, int points) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.hp = hp;
        this.maxHp = hp;
        this.damage = damage;
        this.points = points;
        this.alive = true;
    }



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



    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }



    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        if (this.weapon != null) {
            this.weapon.dispose();
        }
        this.weapon = weapon;
    }


    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }


    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = 0;
            alive = false;
        }
    }


    public void dispose() {
        if (weapon != null) {
            weapon.dispose();
        }
    }
}
