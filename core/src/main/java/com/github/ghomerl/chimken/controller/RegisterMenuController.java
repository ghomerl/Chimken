package com.github.ghomerl.chimken.controller;


import com.github.ghomerl.chimken.model.User;
import com.github.ghomerl.chimken.model.dao.UserDAO;
import com.github.ghomerl.chimken.view.screens.LoginMenuScreen;
import com.github.ghomerl.chimken.view.screens.MainMenuScreen;

public class RegisterMenuController {

    private static final UserDAO userDAO = new UserDAO();

    public static void openLoginPage() {
        ScreenManager.setScreen(new LoginMenuScreen());
    }

    public static void openMainMenu() {
        ScreenManager.setScreen(new MainMenuScreen());
    }


    public static String register(String displayName, String username, String password) {
        if (displayName == null || displayName.trim().isEmpty()) {
            return "Please enter a display name";
        }
        if (username == null || username.trim().isEmpty()) {
            return "Please enter a username";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Please enter a password";
        }

        User user = new User(displayName.trim(), username.trim(), password);
        return userDAO.register(user);
    }
}
