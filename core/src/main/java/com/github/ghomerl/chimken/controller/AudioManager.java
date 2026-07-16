package com.github.ghomerl.chimken.controller;

import com.badlogic.gdx.audio.Music;
import com.github.ghomerl.chimken.view.assets.Assets;

public class AudioManager {

    public static void playMainTheme() {
        Music theme = Assets.mainTheme;
        if (theme != null) {
            if (!theme.isPlaying()) {
                theme.setLooping(true);
                theme.setVolume(0.5f); // 50% volume
                theme.play();
            }
        }
    }

    public static void pauseMainTheme() {
        Music theme = Assets.mainTheme;
        if (theme != null && theme.isPlaying()) {
            theme.pause();
        }
    }

    public static void stopMainTheme() {
        Music theme = Assets.mainTheme;
        if (theme != null) {
            theme.stop();
        }
    }


}
