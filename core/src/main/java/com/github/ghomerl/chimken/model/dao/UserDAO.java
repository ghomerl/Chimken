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


    public String updateHighScore(User user, int newScore) {
        Array<User> users = DatabaseManager.getUsers();

        for (User u : users) {
            if (u.getId() == user.getId()) {
                if (newScore > u.getHighScore()) {
                    u.setHighScore(newScore);
                }
                DatabaseManager.save();
                Gdx.app.log("UserDAO", "High score updated for: " + u.getUsername()
                    + " (new best: " + u.getHighScore() + ")");
                return null;
            }
        }

        Gdx.app.error("UserDAO", "User not found for high-score update: id=" + user.getId());
        return "Failed to save high score";
    }


    public String updateKills(User user, int killDelta) {
        Array<User> users = DatabaseManager.getUsers();

        for (User u : users) {
            if (u.getId() == user.getId()) {
                u.setTotalKills(u.getTotalKills() + killDelta);
                DatabaseManager.save();
                Gdx.app.log("UserDAO", "Kills updated for: " + u.getUsername()
                    + " (total: " + u.getTotalKills() + ")");
                return null;
            }
        }

        Gdx.app.error("UserDAO", "User not found for kills update: id=" + user.getId());
        return "Failed to save kills";
    }


    public String updateWins(User user) {
        Array<User> users = DatabaseManager.getUsers();

        for (User u : users) {
            if (u.getId() == user.getId()) {
                u.setTotalWins(u.getTotalWins() + 1);
                DatabaseManager.save();
                Gdx.app.log("UserDAO", "Wins updated for: " + u.getUsername()
                    + " (total: " + u.getTotalWins() + ")");
                return null;
            }
        }

        Gdx.app.error("UserDAO", "User not found for wins update: id=" + user.getId());
        return "Failed to save wins";
    }

    public String updateKeys(User user, int keyDelta) {
        Array<User> users = DatabaseManager.getUsers();

        for (User u : users) {
            if (u.getId() == user.getId()) {
                u.setKeys(u.getKeys() + keyDelta);
                DatabaseManager.save();
                Gdx.app.log("UserDAO", "Keys updated for: " + u.getUsername()
                    + " (balance: " + u.getKeys() + ")");
                return null;
            }
        }

        Gdx.app.error("UserDAO", "User not found for keys update: id=" + user.getId());
        return "Failed to save keys";
    }

    public String updateOwnedShopItems(User user, Array<String> ownedShopItems) {
        Array<User> users = DatabaseManager.getUsers();

        for (User u : users) {
            if (u.getId() == user.getId()) {
                u.setOwnedShopItems(ownedShopItems);
                DatabaseManager.save();
                Gdx.app.log("UserDAO", "Owned shop items updated for: " + u.getUsername()
                    + " (" + ownedShopItems.size + " items)");
                return null;
            }
        }

        Gdx.app.error("UserDAO", "User not found for shop items update: id=" + user.getId());
        return "Failed to save shop items";
    }


    public String updateRunResults(User user, int score, int killDelta,
                                   int keyDelta, boolean won) {
        Array<User> users = DatabaseManager.getUsers();

        for (User u : users) {
            if (u.getId() == user.getId()) {
                if (score > u.getHighScore()) {
                    u.setHighScore(score);
                }
                u.setTotalKills(u.getTotalKills() + killDelta);
                u.setKeys(u.getKeys() + keyDelta);
                if (won) {
                    u.setTotalWins(u.getTotalWins() + 1);
                }

                user.setHighScore(u.getHighScore());
                user.setTotalKills(u.getTotalKills());
                user.setKeys(u.getKeys());
                user.setTotalWins(u.getTotalWins());

                DatabaseManager.save();
                Gdx.app.log("UserDAO", "Run results saved for: " + u.getUsername()
                    + " (score=" + score + ", kills=" + killDelta
                    + ", keys=" + keyDelta + ", won=" + won + ")");
                return null;
            }
        }

        Gdx.app.error("UserDAO", "User not found for run-results update: id=" + user.getId());
        return "Failed to save run results";
    }
}
