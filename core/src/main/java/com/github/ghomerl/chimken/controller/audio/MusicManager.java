package com.github.ghomerl.chimken.controller.audio;

import com.badlogic.gdx.audio.Music;
import com.github.ghomerl.chimken.view.assets.Assets;

public class MusicManager {

    private static float musicVolume = 100;

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float musicVolume) {
        MusicManager.musicVolume = musicVolume;
    }

    public static void updateMusicVolume() {
        Assets.mainTheme.setVolume(AudioManager.masterVolume * musicVolume / 10000f);
    }

    public static void playMainTheme() {
        Music theme = Assets.mainTheme;
        if (theme != null) {
            if (!theme.isPlaying()) {
                theme.setLooping(true);
                theme.setVolume(AudioManager.masterVolume * musicVolume / 10000f);
                theme.play();
            }
        }
    }

    public static void pauseMainTheme() {
        Music theme = Assets.manager.get(Assets.MAIN_SOUNDTRACK, Music.class);
        if (theme != null && theme.isPlaying()) {
            theme.pause();
        }
    }

    public static void stopMainTheme() {
        Music theme = Assets.manager.get(Assets.MAIN_SOUNDTRACK, Music.class);
        if (theme != null) {
            theme.stop();
        }
    }

    public static void playBattleTheme() {
        Music theme = Assets.battleTheme;
        if (theme != null) {
            if (!theme.isPlaying()) {
                theme.setLooping(true);
                theme.setVolume(AudioManager.masterVolume * musicVolume / 10000f);
                theme.play();
            }
        }
    }

    public static void stopBattleTheme() {
        Music theme = Assets.battleTheme;
        if (theme != null && theme.isPlaying()) {
            theme.stop();
        }
    }

}
