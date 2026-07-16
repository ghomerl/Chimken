package com.github.ghomerl.chimken.model.dao;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.model.User;
import com.github.ghomerl.chimken.model.utils.DatabaseManager;


public class UserDAO {

    public String register(User user) {
        Array<User> users = DatabaseManager.getUsers();

        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(user.getUsername())) {
                return "Username already exists";
            }
        }

        user.setId(DatabaseManager.getNextId());
        users.add(user);
        DatabaseManager.save();

        Gdx.app.log("UserDAO", "User registered: " + user.getUsername());
        return null;
    }

    public User login(String username, String password) {
        Array<User> users = DatabaseManager.getUsers();

        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                Gdx.app.log("UserDAO", "User logged in: " + u.getUsername());
                return u;
            }
        }

        return null;
    }


    public String updateAudioSettings(User user) {
        Array<User> users = DatabaseManager.getUsers();

        for (User u : users) {
            if (u.getId() == user.getId()) {
                u.setMasterVolume(user.getMasterVolume());
                u.setMusicVolume(user.getMusicVolume());
                u.setSfxVolume(user.getSfxVolume());
                DatabaseManager.save();
                Gdx.app.log("UserDAO", "Audio settings updated for: " + u.getUsername());
                return null;
            }
        }

        Gdx.app.error("UserDAO", "User not found for audio update: id=" + user.getId());
        return "Failed to save audio settings";
    }
}
