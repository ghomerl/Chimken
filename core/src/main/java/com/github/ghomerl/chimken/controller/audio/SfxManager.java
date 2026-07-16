package com.github.ghomerl.chimken.controller.audio;

public class SfxManager {

    private static float sfxVolume = 100f;

    public static float getSfxVolume() {
        return sfxVolume;
    }

    public static void setSfxVolume(float sfxVolume) {
        SfxManager.sfxVolume = sfxVolume;
    }

    public static void updateSfxVolume() {

    }
}
