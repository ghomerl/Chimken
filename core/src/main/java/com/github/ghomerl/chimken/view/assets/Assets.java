package com.github.ghomerl.chimken.view.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    private static AssetManager manager;

    public static final String UI_SKIN = "ui/uiskin.json";

    public static Skin skin;

    public static void init() {
        manager = new AssetManager();
    }

    public static void queueLoad() {
        manager.load(UI_SKIN, Skin.class);
    }

    public static boolean update() {
        if (manager.update()) {
            // Once fully loaded, assign the static reference for easy access
            if (skin == null) {
                skin = manager.get(UI_SKIN, Skin.class);
            }
            return true;
        }
        return false;
    }

    public static void dispose() {
        if (manager != null) {
            manager.dispose();
        }
    }
}
