package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.controller.audio.MusicManager;
import com.github.ghomerl.chimken.view.screens.MainMenuScreen;

/**
 * Navigation helper for the Game Over screen.
 */
public class GameOverController {

    public static void openMainMenu() {
        MusicManager.playMainTheme();
        ScreenManager.setScreen(new MainMenuScreen());
    }
}
