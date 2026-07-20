package com.github.ghomerl.chimken.view.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import static com.github.ghomerl.chimken.controller.audio.MusicManager.musicList;

public class Assets {
    public static AssetManager manager;

    public static final String UI_SKIN = "ui/skin/main/quantum-horizon-ui.json";
    public static final String SECONDARY_UI_SKIN = "ui/skin/secondary/pixthulhu-ui.json";
    public static final String MAIN_SOUNDTRACK = "audio/Main Theme.mp3";
    public static final String VICTORY_SOUNDTRACK = "audio/Streetlight Manifesto - Oooo (Halfway Version).mp3";
    public static final String BATTLE_SOUNDTRACK = "audio/Battle Theme.mp3";
    public static final String RETRO_FONT = "fonts/RetroSigned-DYYY0.ttf";
    public static final String TRAJAN_FONT = "fonts/TrajanPro-Regular.ttf";
    public static final String PERPETUA_FONT = "fonts/Perpetua-Regular.otf";
    public static final String INVADERS_ITALIC_FONT = "fonts/InvadersItalic-qAa0.ttf";
    public static final String INVADERS_BOLD_FONT = "fonts/InvadersBold-D370.ttf";
    public static final String INVADERS_BI_FONT = "fonts/InvadersBoldItalic-A52m.ttf";
    public static final String INVADERS_FONT = "fonts/Invaders-ppWZ.ttf";
    public static final String HEART_ICON = "icons/heartima.jpg";
    public static final String FOOD_ICON = "icons/food.jpg";
    public static final String POWER_ICON = "icons/lightning.jpg";
    public static final String MISSILE_ICON = "icons/Missiles.png";





    public static Skin skin;
    public static Skin secondSkin;
    public static Music mainTheme;
    public static Music battleTheme;
    public static Music victoryTheme;
    public static Texture iconHeart;
    public static Texture iconFood;
    public static Texture iconPower;
    public static Texture iconMissile;

    public static void init() {
        manager = new AssetManager();

    }

    public static void queueLoad() {
        manager.load(UI_SKIN, Skin.class);
        manager.load(MAIN_SOUNDTRACK, Music.class);
        manager.load(SECONDARY_UI_SKIN, Skin.class);
    }

    public static boolean update() {
        if (manager.update()) {
            if (skin == null) {
                skin = manager.get(UI_SKIN, Skin.class);
            }
            if (mainTheme == null) {
                mainTheme = manager.get(MAIN_SOUNDTRACK, Music.class);
                musicList.add(mainTheme);
            }
            if (secondSkin == null) {
                secondSkin = manager.get(SECONDARY_UI_SKIN, Skin.class);
            }
            return true;
        }
        return false;
    }

    public static BitmapFont buildFont(int size, String fontName) {
        String fontPath = switch (fontName) {
            case "Trajan" -> TRAJAN_FONT;
            case "Perpetua" -> PERPETUA_FONT;
            case "Retro" -> RETRO_FONT;
            case "Bold" -> INVADERS_BOLD_FONT;
            case "Italic" -> INVADERS_ITALIC_FONT;
            case "Both" -> INVADERS_BI_FONT;
            default -> INVADERS_FONT;
        };

        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal(fontPath));

        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = size;
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        param.color = Color.WHITE;
        param.borderColor = new Color(0, 0, 0, 0.6f);
        param.borderWidth = Math.max(0.5f, size / 36f);

        BitmapFont font = generator.generateFont(param);
        generator.dispose();
        return font;
    }

    public static void queueGameAssets() {
        manager.load(BATTLE_SOUNDTRACK, Music.class);
        manager.load(VICTORY_SOUNDTRACK, Music.class);
        manager.load(HEART_ICON, Texture.class);
        manager.load(FOOD_ICON, Texture.class);
        manager.load(POWER_ICON, Texture.class);
        manager.load(MISSILE_ICON, Texture.class);
    }


    public static boolean updateGameAssets() {
        if (!manager.update()) {
            return false;
        }
        if (battleTheme == null && manager.isLoaded(BATTLE_SOUNDTRACK)) {
            battleTheme = manager.get(BATTLE_SOUNDTRACK, Music.class);
            musicList.add(battleTheme);
        }
        if (victoryTheme == null && manager.isLoaded(VICTORY_SOUNDTRACK)) {
            victoryTheme = manager.get(VICTORY_SOUNDTRACK, Music.class);
            musicList.add(victoryTheme);
        }
        if (iconHeart == null && manager.isLoaded(HEART_ICON)) {
            iconHeart = manager.get(HEART_ICON, Texture.class);
        }
        if (iconFood == null && manager.isLoaded(FOOD_ICON)) {
            iconFood = manager.get(FOOD_ICON, Texture.class);
        }
        if (iconPower == null && manager.isLoaded(POWER_ICON)) {
            iconPower = manager.get(POWER_ICON, Texture.class);
        }
        if (iconMissile == null && manager.isLoaded(MISSILE_ICON)) {
            iconMissile = manager.get(MISSILE_ICON, Texture.class);
        }


        return true;
    }

    public static float getGameProgress() {
        return manager.getProgress();
    }

    public static void dispose() {
        if (manager != null) {
            manager.dispose();
        }
    }

    public static float getProgress() {
        return manager.getProgress();
    }
}
