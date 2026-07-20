package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.controller.audio.MusicManager;
import com.github.ghomerl.chimken.view.screens.MainMenuScreen;


public class GameOverController {

    public static void openMainMenu() {
        MusicManager.stopAll();
        MusicManager.playMainTheme();
        ScreenManager.setScreen(new MainMenuScreen());
    }
}
