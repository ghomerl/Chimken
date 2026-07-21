package com.github.ghomerl.chimken.view.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    public static final String BOSS_FIGHT_SOUNDTRACK = "audio/Angry_Birds_Bird-7.mp3";
    public static final String BOSS_FIGHT_SOUNDTRACK_2 = "audio/Angry_Birds_Bird-7.mp3";
    public static final String BOSS1_TEXTURE = "images/boss1.png";
    public static final String BOSS2_TEXTURE = "images/boss2.png";


    public static final String SPACESHIP_TEXTURE = "images/spaceship.png";
    public static final String CHICKEN_TEXTURE = "images/chicken.png";
    public static final String DOUBLE_CHICKEN_TEXTURE = "images/double_chicken.png";
    public static final String SNIPER_CHICKEN_TEXTURE = "images/sniper_chicken.png";
    public static final String EGG_TEXTURE = "images/egg.png";
    public static final String PLASMA_TEXTURE = "images/plasma.png";
    public static final String BORON_TEXTURE = "images/boron.png";
    public static final String MISSILE_TEXTURE = "images/missile.png";
    public static final String EXPLOSION_TEXTURE = "images/explosion.png";
    public static final String BACKGROUND_TEXTURE = "images/background.png";

    public static final String PLAYER_DEATH_SFX = "audio/sfx/player_death.ogg";
    public static final String MISSILE_EXPLOSION_SFX = "audio/sfx/missile_explosion.ogg";
    public static final String CHICKEN_DEATH_SFX = "audio/sfx/chicken_death.ogg";
    public static final String BOSS_DEATH_SFX = "audio/sfx/boss_death.ogg";
    public static final String PLASMA_SHOT_SFX = "audio/sfx/plasma_shot.ogg";
    public static final String BORON_SHOT_SFX = "audio/sfx/boron_shot.ogg";
    public static final String POWERUP_SFX = "audio/sfx/powerup.ogg";
    public static final String GIFT_SFX = "audio/sfx/powerup.ogg";

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
    public static Music bossFightTheme;
    public static Music bossFightTheme2;
    public static Texture iconHeart;
    public static Texture iconFood;
    public static Texture iconPower;
    public static Texture iconMissile;
    public static Texture boss1Texture;
    public static Texture boss2Texture;



    public static Texture spaceshipTexture;
    public static Texture chickenTexture;
    public static Texture doubleChickenTexture;
    public static Texture sniperChickenTexture;
    public static Texture eggTexture;
    public static Texture plasmaTexture;
    public static Texture boronTexture;
    public static Texture missileTexture;
    public static Texture explosionTexture;
    public static Texture backgroundTexture;

    public static Sound playerDeathSfx;
    public static Sound missileExplosionSfx;
    public static Sound chickenDeathSfx;
    public static Sound bossDeathSfx;
    public static Sound plasmaShotSfx;
    public static Sound boronShotSfx;
    public static Sound powerupSfx;
    public static Sound giftSfx;

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
        manager.load(BOSS_FIGHT_SOUNDTRACK, Music.class);
        manager.load(BOSS_FIGHT_SOUNDTRACK_2, Music.class);
        manager.load(HEART_ICON, Texture.class);
        manager.load(FOOD_ICON, Texture.class);
        manager.load(POWER_ICON, Texture.class);
        manager.load(MISSILE_ICON, Texture.class);
        manager.load(BOSS1_TEXTURE, Texture.class);
        manager.load(BOSS2_TEXTURE, Texture.class);

        // Entity / projectile textures
        manager.load(BACKGROUND_TEXTURE, Texture.class);
        manager.load(SPACESHIP_TEXTURE, Texture.class);
        manager.load(CHICKEN_TEXTURE, Texture.class);
        manager.load(DOUBLE_CHICKEN_TEXTURE, Texture.class);
        manager.load(SNIPER_CHICKEN_TEXTURE, Texture.class);
        manager.load(EGG_TEXTURE, Texture.class);
        manager.load(PLASMA_TEXTURE, Texture.class);
        manager.load(BORON_TEXTURE, Texture.class);
        manager.load(MISSILE_TEXTURE, Texture.class);
        manager.load(EXPLOSION_TEXTURE, Texture.class);

        // Sound effects
        manager.load(PLAYER_DEATH_SFX, Sound.class);
        manager.load(MISSILE_EXPLOSION_SFX, Sound.class);
        manager.load(CHICKEN_DEATH_SFX, Sound.class);
        manager.load(BOSS_DEATH_SFX, Sound.class);
        manager.load(PLASMA_SHOT_SFX, Sound.class);
        manager.load(BORON_SHOT_SFX, Sound.class);
        manager.load(POWERUP_SFX, Sound.class);
        manager.load(GIFT_SFX, Sound.class);
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
        if (bossFightTheme == null && manager.isLoaded(BOSS_FIGHT_SOUNDTRACK)) {
            bossFightTheme = manager.get(BOSS_FIGHT_SOUNDTRACK, Music.class);
            musicList.add(bossFightTheme);
        }
        if (bossFightTheme2 == null && manager.isLoaded(BOSS_FIGHT_SOUNDTRACK_2)) {
            bossFightTheme2 = manager.get(BOSS_FIGHT_SOUNDTRACK_2, Music.class);
            musicList.add(bossFightTheme2);
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
        if (boss1Texture == null && manager.isLoaded(BOSS1_TEXTURE)) {
            boss1Texture = manager.get(BOSS1_TEXTURE, Texture.class);
        }
        if (boss2Texture == null && manager.isLoaded(BOSS2_TEXTURE)) {
            boss2Texture = manager.get(BOSS2_TEXTURE, Texture.class);
        }
        if (spaceshipTexture == null && manager.isLoaded(SPACESHIP_TEXTURE)) {
            spaceshipTexture = manager.get(SPACESHIP_TEXTURE, Texture.class);
        }
        if (chickenTexture == null && manager.isLoaded(CHICKEN_TEXTURE)) {
            chickenTexture = manager.get(CHICKEN_TEXTURE, Texture.class);
        }
        if (doubleChickenTexture == null && manager.isLoaded(DOUBLE_CHICKEN_TEXTURE)) {
            doubleChickenTexture = manager.get(DOUBLE_CHICKEN_TEXTURE, Texture.class);
        }
        if (sniperChickenTexture == null && manager.isLoaded(SNIPER_CHICKEN_TEXTURE)) {
            sniperChickenTexture = manager.get(SNIPER_CHICKEN_TEXTURE, Texture.class);
        }
        if (eggTexture == null && manager.isLoaded(EGG_TEXTURE)) {
            eggTexture = manager.get(EGG_TEXTURE, Texture.class);
        }
        if (plasmaTexture == null && manager.isLoaded(PLASMA_TEXTURE)) {
            plasmaTexture = manager.get(PLASMA_TEXTURE, Texture.class);
        }
        if (boronTexture == null && manager.isLoaded(BORON_TEXTURE)) {
            boronTexture = manager.get(BORON_TEXTURE, Texture.class);
        }
        if (missileTexture == null && manager.isLoaded(MISSILE_TEXTURE)) {
            missileTexture = manager.get(MISSILE_TEXTURE, Texture.class);
        }
        if (explosionTexture == null && manager.isLoaded(EXPLOSION_TEXTURE)) {
            explosionTexture = manager.get(EXPLOSION_TEXTURE, Texture.class);
        }
        if (backgroundTexture == null && manager.isLoaded(BACKGROUND_TEXTURE)) {
            backgroundTexture = manager.get(BACKGROUND_TEXTURE, Texture.class);
        }
        if (playerDeathSfx == null && manager.isLoaded(PLAYER_DEATH_SFX)) {
            playerDeathSfx = manager.get(PLAYER_DEATH_SFX, Sound.class);
        }
        if (missileExplosionSfx == null && manager.isLoaded(MISSILE_EXPLOSION_SFX)) {
            missileExplosionSfx = manager.get(MISSILE_EXPLOSION_SFX, Sound.class);
        }
        if (chickenDeathSfx == null && manager.isLoaded(CHICKEN_DEATH_SFX)) {
            chickenDeathSfx = manager.get(CHICKEN_DEATH_SFX, Sound.class);
        }
        if (bossDeathSfx == null && manager.isLoaded(BOSS_DEATH_SFX)) {
            bossDeathSfx = manager.get(BOSS_DEATH_SFX, Sound.class);
        }
        if (plasmaShotSfx == null && manager.isLoaded(PLASMA_SHOT_SFX)) {
            plasmaShotSfx = manager.get(PLASMA_SHOT_SFX, Sound.class);
        }
        if (boronShotSfx == null && manager.isLoaded(BORON_SHOT_SFX)) {
            boronShotSfx = manager.get(BORON_SHOT_SFX, Sound.class);
        }
        if (powerupSfx == null && manager.isLoaded(POWERUP_SFX)) {
            powerupSfx = manager.get(POWERUP_SFX, Sound.class);
        }
        if (giftSfx == null && manager.isLoaded(GIFT_SFX)) {
            giftSfx = manager.get(GIFT_SFX, Sound.class);
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
