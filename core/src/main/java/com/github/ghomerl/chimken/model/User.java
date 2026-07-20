package com.github.ghomerl.chimken.model;

import com.badlogic.gdx.utils.Array;


public class User {

    private int id;
    private String displayName;
    private String username;
    private String password;

    private int highScore;
    private int totalKills;
    private int totalWins;

    private int keys;

    private Array<String> ownedShopItems;

    private int masterVolume;
    private int musicVolume;
    private int sfxVolume;

    private KeyBindings keyBindings;



    public User() {
        this.highScore = 0;
        this.totalKills = 0;
        this.totalWins = 0;
        this.keys = 0;
        this.ownedShopItems = new Array<>();
        this.masterVolume = 100;
        this.musicVolume = 100;
        this.sfxVolume = 100;
        this.keyBindings = new KeyBindings();
    }


    public User(String displayName, String username, String password) {
        this();
        this.displayName = displayName;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getKeys() {
        return keys;
    }

    public void setKeys(int keys) {
        this.keys = keys;
    }

    public Array<String> getOwnedShopItems() {
        return ownedShopItems;
    }

    public void setOwnedShopItems(Array<String> ownedShopItems) {
        this.ownedShopItems = ownedShopItems;
    }

    public int getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(int masterVolume) {
        this.masterVolume = masterVolume;
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
    }

    public int getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(int sfxVolume) {
        this.sfxVolume = sfxVolume;
    }

    public KeyBindings getKeyBindings() {
        return keyBindings;
    }

    public void setKeyBindings(KeyBindings keyBindings) {
        this.keyBindings = keyBindings;
    }
}
