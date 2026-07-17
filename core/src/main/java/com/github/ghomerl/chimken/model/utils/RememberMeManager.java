package com.github.ghomerl.chimken.model.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class RememberMeManager {

    private static final String PREFS_NAME = "chimken_remember_me";
    private static final String KEY_USERNAME = "remembered_username";
    private static final String KEY_PASSWORD = "remembered_password";
    private static final String KEY_ENABLED = "remember_me_enabled";

    private static Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public static void saveCredentials(String username, String password) {
        Preferences prefs = getPrefs();
        prefs.putBoolean(KEY_ENABLED, true);
        prefs.putString(KEY_USERNAME, username);
        prefs.putString(KEY_PASSWORD, password);
        prefs.flush();
    }

    public static void clearCredentials() {
        Preferences prefs = getPrefs();
        prefs.putBoolean(KEY_ENABLED, false);
        prefs.remove(KEY_USERNAME);
        prefs.remove(KEY_PASSWORD);
        prefs.flush();
    }

    public static boolean hasCredentials() {
        return getPrefs().getBoolean(KEY_ENABLED, false);
    }

    public static String getRememberedUsername() {
        if (!hasCredentials()) return null;
        return getPrefs().getString(KEY_USERNAME, null);
    }

    public static String getRememberedPassword() {
        if (!hasCredentials()) return null;
        return getPrefs().getString(KEY_PASSWORD, null);
    }
}
