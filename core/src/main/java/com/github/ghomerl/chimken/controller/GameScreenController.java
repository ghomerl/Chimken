package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.view.screens.PreGameMenuScreen;

public class GameScreenController {

    public static void openPreGameMenu() {
        ScreenManager.setScreen(new PreGameMenuScreen());
    }

}
