package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.Gdx;
import com.github.ghomerl.chimken.view.screens.PreGameMenuScreen;
import com.github.ghomerl.chimken.view.screens.SettingsScreen;

public class MainMenuController {

    public static void exitGame() {
        Gdx.app.exit();
    }

    public static void openPreGameMenu() {
        ScreenManager.setScreen(new PreGameMenuScreen());
    }

    public static void openSettings() {
        ScreenManager.setScreen(new SettingsScreen());
    }


}
