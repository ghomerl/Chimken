package com.github.ghomerl.chimken.controller.audio;

import com.badlogic.gdx.audio.Sound;
import com.github.ghomerl.chimken.view.assets.Assets;

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

    private static float effectiveVolume() {
        return AudioManager.masterVolume * sfxVolume / 10000f;
    }

    public static void play(Sound sound) {
        if (sound == null) return;
        sound.play(effectiveVolume());
    }

    public static void playPlayerDeath() {
        play(Assets.playerDeathSfx);
    }

    public static void playMissileExplosion() {
        play(Assets.missileExplosionSfx);
    }

    public static void playChickenDeath() {
        play(Assets.chickenDeathSfx);
    }

    public static void playBossDeath() {
        play(Assets.bossDeathSfx);
    }

    public static void playPlasmaShot() {
        play(Assets.plasmaShotSfx);
    }

    public static void playBoronShot() {
        play(Assets.boronShotSfx);
    }

    public static void playPowerup() {
        play(Assets.powerupSfx);
    }

    public static void playGift() {
        play(Assets.giftSfx);
    }
}
