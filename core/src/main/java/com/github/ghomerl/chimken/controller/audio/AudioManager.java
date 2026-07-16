package com.github.ghomerl.chimken.controller.audio;


public class AudioManager {
    protected static float masterVolume = 100f;

    public static float getMasterVolume() {
        return masterVolume;
    }

    public static void setMasterVolume(float masterVolume) {
        AudioManager.masterVolume = masterVolume;
    }

    public static void updateMasterVolume() {
        MusicManager.updateMusicVolume();
        SfxManager.updateSfxVolume();
    }
}
