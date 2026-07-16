package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.view.screens.AudioSettingScreen;
import com.github.ghomerl.chimken.view.screens.MainMenuScreen;

public class SettingController {

    public static void openMainMenu() {
        ScreenManager.setScreen(new MainMenuScreen());
    }

    public static void openAudioSettings() {
        ScreenManager.setScreen(new AudioSettingScreen());
    }
}
