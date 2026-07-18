package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class PauseController extends InputAdapter {

    private boolean paused;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE) {
            paused = !paused;
            return true;
        }
        return false;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void resume() {
        this.paused = false;
    }
}
