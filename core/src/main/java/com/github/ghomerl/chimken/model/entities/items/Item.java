package com.github.ghomerl.chimken.model.entities.items;

import com.badlogic.gdx.math.Rectangle;

public abstract class Item {
    protected float x, y, width, height;
    protected boolean active;
    protected int points;
    protected int foodUnits;
    protected int keyCount;

    public Item(float x, float y, float width, float height, int points) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.points = points;
        this.active = true;
        this.foodUnits = 0;
        this.keyCount = 0;
    }

    public abstract void update(float delta);
    public Rectangle getHitbox() { return new Rectangle(x, y, width, height); }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public float getWidth() { return width; }
    public void setWidth(float width) { this.width = width; }

    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getFoodUnits() { return foodUnits; }
    public void setFoodUnits(int foodUnits) { this.foodUnits = foodUnits; }

    public int getKeyCount() { return keyCount; }
    public void setKeyCount(int keyCount) { this.keyCount = keyCount; }
}
