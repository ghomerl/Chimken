package com.github.ghomerl.chimken.model;


public class User {

    private int id;
    private String displayName;
    private String username;
    private String password;
    private int highScore;
    private int masterVolume;
    private int musicVolume;
    private int sfxVolume;
    private KeyBindings keyBindings;
    private String items;


    public User() {
        this.highScore = 0;
        this.masterVolume = 100;
        this.musicVolume = 100;
        this.sfxVolume = 100;
        this.keyBindings = new KeyBindings();
        this.items = "[]";
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

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}
