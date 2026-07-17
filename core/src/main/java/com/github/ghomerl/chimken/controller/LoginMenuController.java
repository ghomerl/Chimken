package com.github.ghomerl.chimken.controller;

import com.github.ghomerl.chimken.controller.audio.AudioManager;
import com.github.ghomerl.chimken.controller.audio.MusicManager;
import com.github.ghomerl.chimken.controller.audio.SfxManager;
import com.github.ghomerl.chimken.model.dao.UserDAO;
import com.github.ghomerl.chimken.model.User;
import com.github.ghomerl.chimken.model.utils.RememberMeManager;
import com.github.ghomerl.chimken.view.screens.MainMenuScreen;
import com.github.ghomerl.chimken.view.screens.RegisterMenuScreen;

public class LoginMenuController {

    private static final UserDAO userDAO = new UserDAO();
    private static User currentUser = null;

    public static void openRegisterPage() {
        ScreenManager.setScreen(new RegisterMenuScreen());
    }

    public static void openMainMenu() {
        ScreenManager.setScreen(new MainMenuScreen());
    }


    public static String login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return "Please enter your username";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Please enter your password";
        }

        User user = userDAO.login(username.trim(), password);

        if (user == null) {
            return "Invalid username or password";
        }

        currentUser = user;
        MusicManager.setMusicVolume(currentUser.getMusicVolume());
        SfxManager.setSfxVolume(currentUser.getSfxVolume());
        AudioManager.setMasterVolume(currentUser.getMasterVolume());
        AudioManager.updateMasterVolume();

        return null;
    }


    public static boolean isLoggedIn() {
        return currentUser != null;
    }


    public static User getCurrentUser() {
        return currentUser;
    }


    public static void logout() {
        currentUser = null;
        RememberMeManager.clearCredentials();
    }


    public static boolean autoLogin() {
        String username = RememberMeManager.getRememberedUsername();
        String password = RememberMeManager.getRememberedPassword();
        if (username == null || password == null) {
            return false;
        }
        String error = login(username, password);
        if (error == null) {
            return true;
        }

        RememberMeManager.clearCredentials();
        return false;
    }
}
