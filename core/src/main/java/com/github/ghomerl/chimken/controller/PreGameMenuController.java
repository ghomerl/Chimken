package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.view.screens.GameScreen;
import com.github.ghomerl.chimken.view.screens.MainMenuScreen;
import com.github.ghomerl.chimken.view.screens.PreGameMenuScreen;

public class PreGameMenuController {

    public static void openMainMenu() {
        ScreenManager.setScreen(new MainMenuScreen());
    }

    public static void startNewGame() {
        ScreenManager.setScreen(new GameScreen());
    }

}
