package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.controller.audio.MusicManager;
import com.github.ghomerl.chimken.view.screens.GameScreen;
import com.github.ghomerl.chimken.view.screens.PreGameMenuScreen;
import com.github.ghomerl.chimken.view.screens.SettingsScreen;

public class GameScreenController {


    private static GameScreen pausedGameScreen;
    private static boolean navigatingFromPause = false;


    public static void openPreGameMenu() {
        clearPauseState();
        MusicManager.stopBattleTheme();
        MusicManager.stopBossFightTheme();
        MusicManager.stopBossFightTheme2();
        MusicManager.playMainTheme();
        ScreenManager.setScreen(new PreGameMenuScreen());
    }


    public static void openSettingsFromPause(GameScreen gameScreen) {
        pausedGameScreen = gameScreen;
        navigatingFromPause = true;
        ScreenManager.setScreen(new SettingsScreen());
    }


    public static boolean isNavigatingFromPause() {
        return navigatingFromPause;
    }


    public static void returnToPausedGame() {
        navigatingFromPause = false;
        if (pausedGameScreen != null) {
            pausedGameScreen.setPaused(true);
            ScreenManager.setScreen(pausedGameScreen);
            pausedGameScreen = null;
        }
    }

    private static void clearPauseState() {
        pausedGameScreen = null;
        navigatingFromPause = false;
    }
}
