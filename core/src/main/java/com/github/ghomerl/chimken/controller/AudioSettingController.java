package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.view.screens.SettingsScreen;

public class AudioSettingController {

    public static void openSettings() {
        ScreenManager.setScreen(new SettingsScreen());
    }
}
