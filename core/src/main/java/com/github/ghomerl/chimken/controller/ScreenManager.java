package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.Screen;
import com.github.ghomerl.chimken.Main;

public class ScreenManager {
    private static Main main;

    public static void init(Main main) {
        ScreenManager.main = main;
    }

    public static void setScreen(Screen screen) {
        main.setScreen(screen);
    }
}
