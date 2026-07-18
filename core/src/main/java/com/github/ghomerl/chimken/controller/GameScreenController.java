package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.controller.audio.MusicManager;
import com.github.ghomerl.chimken.view.screens.GameScreen;
import com.github.ghomerl.chimken.view.screens.PreGameMenuScreen;
import com.github.ghomerl.chimken.view.screens.SettingsScreen;

public class GameScreenController {

    // ── Pause → Settings navigation state ──────────────────────────
    private static GameScreen pausedGameScreen;
    private static boolean navigatingFromPause = false;

    /**
     * Normal quit: stops battle music, returns to pre-game menu.
     */
    public static void openPreGameMenu() {
        clearPauseState();
        MusicManager.stopBattleTheme();
        MusicManager.playMainTheme();
        ScreenManager.setScreen(new PreGameMenuScreen());
    }

    /**
     * Opens the Settings screen from the pause menu.
     * The GameScreen instance is kept alive so the player can
     * return to it in the exact same paused state.
     */
    public static void openSettingsFromPause(GameScreen gameScreen) {
        pausedGameScreen = gameScreen;
        navigatingFromPause = true;
        ScreenManager.setScreen(new SettingsScreen());
    }

    /**
     * @return {@code true} when the Settings screen was opened from
     *         the in-game pause menu and its Back button should
     *         return to the paused GameScreen instead of the main menu.
     */
    public static boolean isNavigatingFromPause() {
        return navigatingFromPause;
    }

    /**
     * Restores the paused GameScreen. Called by SettingsScreen's
     * Back button when {@link #isNavigatingFromPause()} is true.
     */
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
