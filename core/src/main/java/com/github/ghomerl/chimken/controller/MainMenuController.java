package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.Gdx;
import com.github.ghomerl.chimken.view.screens.LoginMenuScreen;
import com.github.ghomerl.chimken.view.screens.MainMenuScreen;
import com.github.ghomerl.chimken.view.screens.PreGameMenuScreen;
import com.github.ghomerl.chimken.view.screens.SettingsScreen;

public class MainMenuController {

    public static void exitGame() {
        Gdx.app.exit();
    }


    public static String openPreGameMenu() {
        if (!LoginMenuController.isLoggedIn()) {
            return "You must log in first to play";
        }
        ScreenManager.setScreen(new PreGameMenuScreen());
        return null;
    }


    public static String openSettings() {
        if (!LoginMenuController.isLoggedIn()) {
            return "You must log in first to change settings";
        }
        ScreenManager.setScreen(new SettingsScreen());
        return null;
    }

    public static void openLoginPage() {
        ScreenManager.setScreen(new LoginMenuScreen());
    }


    public static void logout() {
        LoginMenuController.logout();
        ScreenManager.setScreen(new MainMenuScreen());
    }


}
