package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.view.screens.MainMenuScreen;

public class LeaderboardController {

    public static void openMainMenu() {
        ScreenManager.setScreen(new MainMenuScreen());
    }

}
