package com.github.ghomerl.chimken.view.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    public static AssetManager manager;

    public static final String UI_SKIN = "ui/skin/main/quantum-horizon-ui.json";
    public static final String SECONDARY_UI_SKIN = "ui/skin/secondary/pixthulhu-ui.json";
    public static final String MAIN_SOUNDTRACK = "audio/Main Theme.mp3";
    public static final String RETRO_FONT = "fonts/RetroSigned-DYYY0.ttf";
    public static final String TRAJAN_FONT = "fonts/TrajanPro-Regular.ttf";
    public static final String PERPETUA_FONT = "fonts/Perpetua-Regular.otf";
    public static final String INVADERS_ITALIC_FONT = "fonts/InvadersItalic-qAa0.ttf";
    public static final String INVADERS_BOLD_FONT = "fonts/InvadersBold-D370.ttf";
    public static final String INVADERS_BI_FONT = "fonts/InvadersBoldItalic-A52m.ttf";
    public static final String INVADERS_FONT = "fonts/Invaders-ppWZ.ttf";




    public static Skin skin;
    public static Skin secondSkin;
    public static Music mainTheme;

    public static void init() {
        manager = new AssetManager();

    }

    public static void queueLoad() {
        manager.load(UI_SKIN, Skin.class);
        manager.load(SECONDARY_UI_SKIN, Skin.class);
        manager.load(MAIN_SOUNDTRACK, Music.class);
    }

    public static boolean update() {
        if (manager.update()) {
            if (skin == null) {
                skin = manager.get(UI_SKIN, Skin.class);
            }
            if (mainTheme == null) {
                mainTheme = manager.get(MAIN_SOUNDTRACK, Music.class);
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

    public static void dispose() {
        if (manager != null) {
            manager.dispose();
        }
    }

    public static float getProgress() {
        return manager.getProgress();
    }
}
