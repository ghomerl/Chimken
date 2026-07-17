package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.controller.audio.MusicManager;
import com.github.ghomerl.chimken.view.screens.PreGameMenuScreen;

public class GameScreenController {

    public static void openPreGameMenu() {
        MusicManager.stopBattleTheme();
        MusicManager.playMainTheme();
        ScreenManager.setScreen(new PreGameMenuScreen());
    }

}
