package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.model.dao.UserDAO;
import com.github.ghomerl.chimken.model.User;
import com.github.ghomerl.chimken.view.screens.SettingsScreen;

public class AudioSettingController {

    private static String pendingToast = null;


    public static String getPendingToast() {
        String msg = pendingToast;
        pendingToast = null;
        return msg;
    }

    public static void openSettings() {
        ScreenManager.setScreen(new SettingsScreen());
    }


    public static void saveAndGoBack(int masterVolume, int musicVolume, int sfxVolume) {
        if (LoginMenuController.isLoggedIn()) {
            User user = LoginMenuController.getCurrentUser();
            user.setMasterVolume(masterVolume);
            user.setMusicVolume(musicVolume);
            user.setSfxVolume(sfxVolume);

            String error = new UserDAO().updateAudioSettings(user);
            pendingToast = (error == null) ? "Audio settings saved" : error;
        }
        openSettings();
    }
}
