package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.controller.*;
import com.github.ghomerl.chimken.controller.audio.MusicManager;
import com.github.ghomerl.chimken.controller.audio.SfxManager;
import com.github.ghomerl.chimken.model.User;
import com.github.ghomerl.chimken.model.dao.UserDAO;
import com.github.ghomerl.chimken.model.runlog.RunLog;
import com.github.ghomerl.chimken.model.runlog.RunLogDAO;
import com.github.ghomerl.chimken.model.KeyBindings;
import com.github.ghomerl.chimken.model.entities.Player;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.levels.LevelFactory;
import com.github.ghomerl.chimken.model.entities.enemies.SniperChicken;
import com.github.ghomerl.chimken.model.levels.SpawnEntry;
import com.github.ghomerl.chimken.model.levels.Wave;
import com.github.ghomerl.chimken.controller.WaveManager;
import com.github.ghomerl.chimken.model.entities.enemies.bosses.Boss;
import com.github.ghomerl.chimken.model.entities.enemies.bosses.Boss2;
import com.github.ghomerl.chimken.model.entities.items.Item;
import com.github.ghomerl.chimken.model.entities.projectiles.MissileProjectile;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;
import com.github.ghomerl.chimken.model.entities.weapons.BoronRailgun;
import com.github.ghomerl.chimken.view.assets.Assets;
import com.github.ghomerl.chimken.view.renderers.BossRenderer;
import com.github.ghomerl.chimken.view.renderers.ChickenRenderer;
import com.github.ghomerl.chimken.view.renderers.EggProjectileRenderer;
import com.github.ghomerl.chimken.view.renderers.ExplosionAnimation;
import com.github.ghomerl.chimken.view.renderers.ItemRenderer;
import com.github.ghomerl.chimken.view.renderers.MissileRenderer;
import com.github.ghomerl.chimken.view.renderers.PlayerRenderer;
import com.github.ghomerl.chimken.view.renderers.ProjectileRenderer;
import com.github.ghomerl.chimken.view.renderers.SuperheatedMetalloidChunkRenderer;

public class GameScreen extends AbstractScreen {

    private static final boolean DEBUG_HITBOXES = false;

    private Player player;
    private PlayerController playerController;
    private PlayerRenderer playerRenderer;
    private ProjectileRenderer projectileRenderer;

    private final Array<Enemy> enemies = new Array<>();
    private final Array<ChickenController> chickenControllers = new Array<>();

    private WaveManager waveManager;
    private Array<SpawnEntry> activeSpawns;

    private final Array<Item> items = new Array<>();
    private ItemRenderer itemRenderer;
    private SuperheatedMetalloidChunkRenderer chunkRenderer;

    private ChickenRenderer chickenRenderer;
    private EggProjectileRenderer eggProjectileRenderer;
    private BossRenderer bossRenderer;
    private MissileRenderer missileRenderer;

    private PauseController pauseController;
    private Table pauseOverlay;
    private Texture dimBgTexture;
    private Texture panelBgTexture;

    private ShapeRenderer shapeRenderer;

    private boolean initialized = false;

    private static final float WAVE_ANNOUNCE_DURATION = 3f;
    private boolean waveAnnouncementActive;
    private float waveAnnouncementTimer;
    private int announcedWaveNumber;
    private BitmapFont waveFont;

    private BitmapFont scoreFont;
    private BitmapFont hudFont;

    private Texture iconHeart;
    private Texture iconMissile;
    private Texture iconLightning;
    private Texture iconFood;

    private final Array<MissileProjectile> missiles = new Array<>();

    private final Array<ExplosionAnimation> activeExplosions = new Array<>();
    private static final float EXPLOSION_DURATION = 0.6f;
    private static final float EXPLOSION_SIZE_MISSILE = 400f;
    private static final float EXPLOSION_SIZE_PLAYER = 200f;

    private SnakeMovementController snakeController;

    private ZigzagMovementController zigzagController;

    private BossController bossController;

    private BossController2 bossController2;

    private final Array<SniperChickenController> sniperControllers = new Array<>();

    private int nextLifeThreshold;


    @Override
    public void show() {
        if (!initialized) {
            super.show();
            initGame();
            initialized = true;
        } else {
            worldViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

            InputMultiplexer multiplexer = new InputMultiplexer();
            multiplexer.addProcessor(stage);
            Gdx.input.setInputProcessor(multiplexer);
        }

        reAddInputProcessors();

        if (pauseController != null && pauseController.isPaused()) {
            pauseOverlay.setVisible(true);
        }
    }


    private void initGame() {
        shapeRenderer = new ShapeRenderer();


        playerRenderer = new PlayerRenderer();
        projectileRenderer = new ProjectileRenderer();
        chickenRenderer = new ChickenRenderer();
        eggProjectileRenderer = new EggProjectileRenderer();
        bossRenderer = new BossRenderer();
        missileRenderer = new MissileRenderer();
        itemRenderer = new ItemRenderer();
        chunkRenderer = new SuperheatedMetalloidChunkRenderer();


        scoreFont = Assets.buildFont(36, "Bold");
        hudFont = Assets.buildFont(28, "Default");
        waveFont = Assets.buildFont(120, "Bold");


        iconHeart = Assets.iconHeart;
        iconMissile = Assets.iconMissile;
        iconLightning = Assets.iconPower;
        iconFood = Assets.iconFood;


        KeyBindings kb = LoginMenuController.isLoggedIn()
            ? LoginMenuController.getCurrentUser().getKeyBindings()
            : new KeyBindings();
        float startX = worldViewport.getWorldWidth() / 2f - Player.DEFAULT_WIDTH / 2f;
        player = new Player(kb, startX, 100f);

        playerController = new PlayerController(
            player,
            worldViewport.getWorldWidth(),
            worldViewport.getWorldHeight()
        );


        pauseController = new PauseController();

        float ww = worldViewport.getWorldWidth();
        float wh = worldViewport.getWorldHeight();
        Array<Wave> allWaves = new Array<>();
        allWaves.addAll(LevelFactory.buildLevel1(ww, wh));
        allWaves.addAll(LevelFactory.buildLevel2(ww, wh));
        allWaves.addAll(LevelFactory.buildLevel4(ww, wh));
        allWaves.addAll(LevelFactory.buildLevel5(ww, wh));
        allWaves.addAll(LevelFactory.buildLevel6(ww, wh));
        allWaves.addAll(LevelFactory.buildLevel7(ww, wh));
        allWaves.addAll(LevelFactory.buildLevel8(ww, wh));
        allWaves.addAll(LevelFactory.buildLevel9(ww, wh));
        allWaves.addAll(LevelFactory.buildLevel10(ww, wh));
        allWaves.addAll(LevelFactory.buildLevel11(ww, wh));
        waveManager = new WaveManager(allWaves, allWaves.size - 1);


        nextLifeThreshold = 10000;


        createPauseOverlay();


        spawnNextWave();
    }


    private void spawnNextWave() {
        // Dispose old enemies' weapons
        for (Enemy e : enemies) {
            e.dispose();
        }
        enemies.clear();
        chickenControllers.clear();
        sniperControllers.clear();
        snakeController = null;
        zigzagController = null;
        bossController = null;
        bossController2 = null;

        waveManager.spawnCurrentWave(enemies);
        activeSpawns = waveManager.getCurrentSpawns();


        boolean isBossWave = false;
        boolean isBoss2Wave = false;
        for (Enemy e : enemies) {
            if (e instanceof Boss2) {
                isBossWave = true;
                isBoss2Wave = true;
                bossController2 = new BossController2(
                    (Boss2) e, enemies, sniperControllers, player);
            } else if (e instanceof Boss) {
                isBossWave = true;
                bossController = new BossController((Boss) e);
            } else if (e instanceof SniperChicken) {
                sniperControllers.add(
                    new SniperChickenController((SniperChicken) e, player)
                );
            } else {
                chickenControllers.add(new ChickenController(e));
            }
        }

        // If the wave uses snake or zigzag movement, create its controller
        Wave currentWave = waveManager.getCurrentWave();
        if (currentWave != null) {
            switch (currentWave.getMovementType()) {
                case SNAKE:
                    snakeController = new SnakeMovementController(
                        enemies,
                        worldViewport.getWorldWidth(),
                        worldViewport.getWorldHeight()
                    );
                    break;
                case ZIGZAG:
                    zigzagController = new ZigzagMovementController(enemies);
                    break;
                default:
                    // FORMATION - handled by EnemyMovementController
                    break;
            }
        }

        // Boss waves get their own music; every other wave uses battle theme.
        if (isBoss2Wave) {
            MusicManager.stopBattleTheme();
            MusicManager.stopBossFightTheme();
            MusicManager.playBossFightTheme2();
        } else if (isBossWave) {
            MusicManager.stopBattleTheme();
            MusicManager.stopBossFightTheme2();
            MusicManager.playBossFightTheme();
        } else {
            MusicManager.stopBossFightTheme();
            MusicManager.stopBossFightTheme2();
            MusicManager.playBattleTheme();
        }

        // Start wave announcement
        waveAnnouncementActive = true;
        waveAnnouncementTimer = WAVE_ANNOUNCE_DURATION;
        announcedWaveNumber = waveManager.getCurrentWaveNumber();
    }


    private Wave getCurrentWave() {
        return waveManager.getCurrentWave();
    }


    private void onWaveCleared() {
        if (waveManager.wasLastWave()) {
            // Level complete - player wins
            MusicManager.stopBattleTheme();
            MusicManager.stopBossFightTheme();
            MusicManager.stopBossFightTheme2();
            MusicManager.playVictoryTheme();
            saveRunResults(true);
            ScreenManager.setScreen(new WinScreen(
                player.getTotalPoints(),
                player.getKillCount(),
                player.getKeysObtained()
            ));
            return;
        }

        // Advance to next wave
        if (waveManager.advanceWave()) {
            spawnNextWave();
        }
    }


    private void reAddInputProcessors() {
        InputMultiplexer multiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if (multiplexer != null) {
            multiplexer.addProcessor(0, pauseController);
            multiplexer.addProcessor(1, playerController);
        }
    }


    private void createPauseOverlay() {
        Pixmap dimPx = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        dimPx.setColor(0, 0, 0, 0.5f);
        dimPx.fill();
        dimBgTexture = new Texture(dimPx);
        dimPx.dispose();

        Pixmap panelPx = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        panelPx.setColor(0.1f, 0.1f, 0.15f, 0.85f);
        panelPx.fill();
        panelBgTexture = new Texture(panelPx);
        panelPx.dispose();

        pauseOverlay = new Table();
        pauseOverlay.setFillParent(true);
        pauseOverlay.setBackground(new TextureRegionDrawable(new TextureRegion(dimBgTexture)));

        Table panel = new Table();
        panel.setBackground(new TextureRegionDrawable(new TextureRegion(panelBgTexture)));

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.buildFont(72, "Bold");
        titleStyle.fontColor = Color.WHITE;
        Label titleLabel = new Label("PAUSED", titleStyle);

        TextButton resumeBtn = new TextButton("Resume Game", skin);
        TextButton settingsBtn = new TextButton("Settings", skin);
        TextButton quitBtn = new TextButton("Quit Game", skin);

        panel.add(titleLabel).padBottom(30f).row();
        panel.add(resumeBtn).width(400).height(60).pad(8f).row();
        panel.add(settingsBtn).width(400).height(60).pad(8f).row();
        panel.add(quitBtn).width(400).height(60).pad(8f);

        pauseOverlay.add(panel).width(640).height(360);

        pauseOverlay.setVisible(false);
        stage.addActor(pauseOverlay);

        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseController.resume();
                pauseOverlay.setVisible(false);
            }
        });

        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseOverlay.setVisible(false);
                GameScreenController.openSettingsFromPause(GameScreen.this);
            }
        });

        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseController.resume();
                pauseOverlay.setVisible(false);
                GameScreenController.openPreGameMenu();
            }
        });
    }

    public void setPaused(boolean paused) {
        if (pauseController != null) {
            pauseController.setPaused(paused);
        }
    }


    @Override
    public void render(float delta) {
        float clamped = Math.min(delta, 1 / 30f);

        if (pauseOverlay != null) {
            pauseOverlay.setVisible(pauseController.isPaused());
        }

        if (pauseController.isPaused()) {
            renderPausedFrame(clamped);
            return;
        }

        renderGameFrame(clamped);
    }

    private void renderPausedFrame(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawWorldFilled();
        drawBoss();
        drawWorldDebug();
        drawExplosion();

        drawHUD();

        uiViewport.apply();
        stage.act(delta);
        stage.draw();
    }

    private void renderGameFrame(float delta) {
        if (waveAnnouncementActive) {
            waveAnnouncementTimer -= delta;
            if (waveAnnouncementTimer <= 0f) {
                waveAnnouncementActive = false;
            }
        }

        playerController.update(delta);

        if (!waveAnnouncementActive) {
            Wave wave = getCurrentWave();
            if (wave != null) {
                switch (wave.getMovementType()) {
                    case SNAKE:
                        if (snakeController != null) snakeController.update(delta);
                        break;
                    case ZIGZAG:
                        if (zigzagController != null) zigzagController.update(delta);
                        break;
                    default:
                        EnemyMovementController.update(activeSpawns, delta);
                        break;
                }
            }

            // Enemy AI (firing) - only when chickens are active
            for (ChickenController cc : chickenControllers) {
                cc.update(delta);
            }
            for (SniperChickenController sc : sniperControllers) {
                sc.update(delta);
            }
            if (bossController != null) {
                bossController.update(delta);
            }
            if (bossController2 != null) {
                bossController2.update(delta);
            }
        }

        ItemController.update(items, player, delta);

        updateMissiles(delta);

        for (int i = activeExplosions.size - 1; i >= 0; i--) {
            ExplosionAnimation ex = activeExplosions.get(i);
            ex.update(delta);
            if (ex.isFinished()) {
                activeExplosions.removeIndex(i);
            }
        }

        if (!playerController.isPlayerDead()) {
            resolveCollisions();
        }

        checkExtraLife();

        if (playerController.isPlayerDead()) {
            MusicManager.stopBattleTheme();
            MusicManager.stopBossFightTheme();
            MusicManager.stopBossFightTheme2();

            saveRunResults(false);
            ScreenManager.setScreen(new GameOverScreen(player.getTotalPoints()));
            return;
        }


        if (waveManager.isCurrentWaveCleared()) {
            onWaveCleared();
            return;
        }


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawWorldFilled();
        drawBoss();
        drawWorldDebug();
        drawExplosion();
        drawHUD();
        drawBossHealthBar();
        drawWaveAnnouncement();

        uiViewport.apply();
        stage.act(delta);
        stage.draw();
    }



    private void drawWorldFilled() {
        worldViewport.apply();


        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();


        if (Assets.backgroundTexture != null) {
            batch.draw(Assets.backgroundTexture,
                0f, 0f,
                worldViewport.getWorldWidth(),
                worldViewport.getWorldHeight());
        }

        // 1) Player spaceship
        playerRenderer.render(batch, player);

        // 2) Player projectiles (plasma or boron depending on weapon)
        renderPlayerProjectiles();

        // 3) Non-boss enemies (chickens of all types)
        for (Enemy e : enemies) {
            if (!e.isAlive()) continue;
            if (e instanceof Boss) continue;
            chickenRenderer.render(batch, e);
        }

        // 4) Enemy egg projectiles
        for (Enemy e : enemies) {
            if (e.getWeapon() != null) {
                eggProjectileRenderer.render(batch, e.getWeapon().getProjectiles());
            }
        }

        // 5) In-flight missiles
        missileRenderer.render(batch, missiles);

        batch.end();

        // Shapes (mostly items)
        shapeRenderer.setProjectionMatrix(worldCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        itemRenderer.render(shapeRenderer, items);
        shapeRenderer.end();
    }

    private void drawWorldDebug() {
        if (!DEBUG_HITBOXES) return;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.RED);
        playerRenderer.renderDebug(shapeRenderer, player);

        shapeRenderer.setColor(Color.GREEN);
        renderPlayerProjectileDebug(shapeRenderer);

        shapeRenderer.setColor(Color.YELLOW);
        for (Enemy e : enemies) {
            if (!e.isAlive()) continue;
            if (e instanceof Boss) continue;
            chickenRenderer.renderDebug(shapeRenderer, e);
        }

        shapeRenderer.setColor(Color.YELLOW);
        for (Enemy e : enemies) {
            if (e instanceof Boss) {
                bossRenderer.renderDebug(shapeRenderer, e);
            }
        }

        shapeRenderer.setColor(Color.ORANGE);
        for (Enemy e : enemies) {
            if (e.getWeapon() != null) {
                eggProjectileRenderer.renderDebug(shapeRenderer, e.getWeapon().getProjectiles());
            }
        }

        shapeRenderer.setColor(Color.MAGENTA);
        itemRenderer.renderDebug(shapeRenderer, items);

        shapeRenderer.setColor(Color.ORANGE);
        missileRenderer.renderDebug(shapeRenderer, missiles);

        shapeRenderer.end();
    }


    private void drawBoss() {
        Boss boss = null;
        for (Enemy e : enemies) {
            if (e instanceof Boss) {
                boss = (Boss) e;
                break;
            }
        }
        if (boss == null) return;

        worldViewport.apply();
        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        bossRenderer.render(batch, boss);
        batch.end();
    }


    private void spawnExplosion(float x, float y, float size) {
        activeExplosions.add(new ExplosionAnimation(
            x, y, EXPLOSION_DURATION, size));
    }


    private void drawExplosion() {
        if (activeExplosions.size == 0) return;

        worldViewport.apply();
        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        for (ExplosionAnimation ex : activeExplosions) {
            ex.render(batch);
        }
        batch.end();
    }



    private void drawHUD() {
        uiViewport.apply();


        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Panel background
        shapeRenderer.setColor(0, 0, 0, 0.45f);
        shapeRenderer.rect(16, 16, 220, 184);

        shapeRenderer.end();

        // Icons, Text labels
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();


        float row0y = 176;  // Hearts
        float row1y = 134;  // Missiles
        float row2y = 92;   // Lightning / weapon level
        float row3y = 50;   // Food

        float iconSize = 28f;
        float iconX = 28f;

        // 1) Heart icon - health
        batch.draw(iconHeart, iconX, row0y - iconSize / 2f, iconSize, iconSize);
        // 2) Missile icon - missile count
        batch.draw(iconMissile, iconX, row1y - iconSize / 2f, iconSize, iconSize);
        // 3) Lightning icon - weapon level
        batch.draw(iconLightning, iconX, row2y - iconSize / 2f, iconSize, iconSize);
        // 4) Food icon - food amount
        batch.draw(iconFood, iconX, row3y - iconSize / 2f, iconSize, iconSize);

        hudFont.setColor(Color.WHITE);
        hudFont.draw(batch, String.valueOf(player.getHp()), 64, row0y + 8);
        hudFont.draw(batch, String.valueOf(player.getMissileCount()), 64, row1y + 8);
        hudFont.draw(batch, String.valueOf(player.getWeaponLevel()), 64, row2y + 8);
        hudFont.draw(batch, String.valueOf(player.getFoodObtained()), 64, row3y + 8);

        // Score - top-left
        scoreFont.setColor(Color.WHITE);
        scoreFont.draw(batch, "SCORE  " + player.getTotalPoints(), 20, 1060);

        batch.end();
    }



    private void drawBossHealthBar() {
        // Find the live boss, if any.
        Boss boss = null;
        for (Enemy e : enemies) {
            if (e instanceof Boss && e.isAlive()) {
                boss = (Boss) e;
                break;
            }
        }
        if (boss == null) return;

        uiViewport.apply();
        Gdx.gl.glEnable(GL20.GL_BLEND);

        float barWidth = 1200f;
        float barHeight = 28f;
        float barX = (uiViewport.getWorldWidth() - barWidth) / 2f;
        float barY = 1014f;   // top of the screen, below the score row

        int hp = boss.getHp();
        int maxHp = boss.getMaxHp();
        if (maxHp <= 0) maxHp = 1;
        float fillRatio = Math.max(0f, Math.min(1f, (float) hp / (float) maxHp));
        float fillWidth = barWidth * fillRatio;


        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);


        shapeRenderer.setColor(0f, 0f, 0f, 0.7f);
        shapeRenderer.rect(barX - 6f, barY - 6f, barWidth + 12f, barHeight + 12f);


        shapeRenderer.setColor(0.3f, 0.05f, 0.05f, 1f);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);


        float r = 1f;
        float g = 0.2f * fillRatio;
        float b = 0.2f * fillRatio;
        shapeRenderer.setColor(r, g, b, 1f);
        shapeRenderer.rect(barX, barY, fillWidth, barHeight);

        shapeRenderer.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1f, 1f, 1f, 0.85f);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.end();

        // Label (SpriteBatch)
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        hudFont.setColor(Color.WHITE);
        String label = "BOSS  " + hp + " / " + maxHp;
        com.badlogic.gdx.graphics.g2d.GlyphLayout layout =
            new com.badlogic.gdx.graphics.g2d.GlyphLayout(hudFont, label);
        hudFont.draw(batch, label,
            (uiViewport.getWorldWidth() - layout.width) / 2f,
            barY - 12f);
        batch.end();
    }


    private void drawWaveAnnouncement() {
        if (!waveAnnouncementActive) return;

        uiViewport.apply();
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        // Fade: full opacity for first 2.2s, then fade out over 0.8s
        float alpha = 1f;
        if (waveAnnouncementTimer < 0.8f) {
            alpha = waveAnnouncementTimer / 0.8f;
        }
        waveFont.getColor().a = alpha;
        String text = "Wave " + announcedWaveNumber;
        com.badlogic.gdx.graphics.g2d.GlyphLayout layout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(waveFont, text);
        waveFont.draw(batch, layout,
            (uiViewport.getWorldWidth() - layout.width) / 2f,
            (uiViewport.getWorldHeight() + layout.height) / 2f);

        batch.end();
        waveFont.getColor().a = 1f; // reset
    }


    private void updateMissiles(float delta) {
        // Check if the player requested a missile this frame
        if (playerController.consumeMissileRequest() && player.getMissileCount() > 0) {
            player.setMissileCount(player.getMissileCount() - 1);
            float cx = player.getX() + player.getWidth() / 2f;
            float cy = player.getY() + player.getHeight() / 2f;
            missiles.add(new MissileProjectile(
                cx, cy,
                worldViewport.getWorldWidth() / 2f,
                worldViewport.getWorldHeight() / 2f
            ));
        }

        // Update missiles
        for (int i = missiles.size - 1; i >= 0; i--) {
            MissileProjectile m = missiles.get(i);
            m.update(delta);

            if (m.hasExploded()) {
                // Deal 2500 damage to ALL alive enemies
                for (Enemy e : enemies) {
                    if (e.isAlive()) {
                        e.takeDamage(2500);
                        if (!e.isAlive()) {
                            // Per-enemy death SFX + points + drops
                            if (e instanceof Boss) {
                                SfxManager.playBossDeath();
                            } else {
                                SfxManager.playChickenDeath();
                            }
                            awardKillPoints(e);
                            DropController.rollDrops(e, player, items,
                                worldViewport.getWorldWidth());
                        }
                    }
                }
                // Spawn explosion animation + SFX at the detonation point
                spawnExplosion(m.getX(), m.getY(), EXPLOSION_SIZE_MISSILE);
                SfxManager.playMissileExplosion();
                missiles.removeIndex(i);
            } else if (!m.isActive()) {
                missiles.removeIndex(i);
            }
        }
    }


    private void checkExtraLife() {
        if (player.getTotalPoints() >= nextLifeThreshold) {
            player.setHp(player.getHp() + 1);
            nextLifeThreshold += 10_000;
        }
    }


    private void resolveCollisions() {
        // 1) Player body , Enemy body
        for (Enemy e : enemies) {
            if (CollisionController.checkPlayerEnemyCollision(player, e)) {
                playerController.onPlayerHit(PlayerController.RESPAWN_RISE);
                // Explosion + SFX on the player for every body-collision hit.
                spawnExplosion(
                    player.getX() + player.getWidth() * 0.5f,
                    player.getY() + player.getHeight() * 0.5f,
                    EXPLOSION_SIZE_PLAYER);
                SfxManager.playPlayerDeath();
                if (!(e instanceof Boss)) {
                    e.takeDamage(e.getHp());
                    // Enemy died from body collision - play death SFX
                    if (!e.isAlive()) {
                        SfxManager.playChickenDeath();
                    }
                    awardKillPoints(e);
                    DropController.rollDrops(e, player, items, worldViewport.getWorldWidth());
                }
                break;
            }
        }

        // 2) Enemy egg , Player
        if (CollisionController.canPlayerCollide(player)) {
            boolean playerWasHit = false;
            for (Enemy e : enemies) {
                if (playerWasHit) break;
                if (e.getWeapon() == null) continue;
                Array<Projectile> eggs = e.getWeapon().getProjectiles();
                for (int i = eggs.size - 1; i >= 0; i--) {
                    Projectile p = eggs.get(i);
                    if (CollisionController.checkProjectilePlayerCollision(p, player)) {
                        p.setActive(false);
                        playerController.onPlayerHit(PlayerController.RESPAWN_TELEPORT);
                        // Explosion + SFX on the player for every egg hit.
                        spawnExplosion(
                            player.getX() + player.getWidth() * 0.5f,
                            player.getY() + player.getHeight() * 0.5f,
                            EXPLOSION_SIZE_PLAYER);
                        SfxManager.playPlayerDeath();
                        playerWasHit = true;
                        break;
                    }
                }
            }
        }

        // 3) Player projectile , Enemy
        Array<Projectile> plasmas = player.getWeapon().getProjectiles();
        for (int i = plasmas.size - 1; i >= 0; i--) {
            Projectile p = plasmas.get(i);
            if (!p.isActive()) continue;
            for (Enemy e : enemies) {
                if (CollisionController.checkProjectileEnemyCollision(p, e)) {
                    p.setActive(false);
                    e.takeDamage(p.getDamage());
                    if (!e.isAlive()) {
                        // Enemy died from player projectile - play death SFX
                        if (e instanceof Boss) {
                            SfxManager.playBossDeath();
                        } else {
                            SfxManager.playChickenDeath();
                        }
                        awardKillPoints(e);
                        DropController.rollDrops(e, player, items, worldViewport.getWorldWidth());
                    }
                    break;
                }
            }
        }
    }

    private void renderPlayerProjectiles() {
        Array<Projectile> projs = player.getWeapon().getProjectiles();
        if (player.getWeapon() instanceof BoronRailgun) {
            chunkRenderer.render(batch, projs);
        } else {
            projectileRenderer.render(batch, projs);
        }
    }

    private void renderPlayerProjectileDebug(ShapeRenderer r) {
        Array<Projectile> projs = player.getWeapon().getProjectiles();
        if (player.getWeapon() instanceof BoronRailgun) {
            chunkRenderer.renderDebug(r, projs);
        } else {
            projectileRenderer.renderDebug(r, projs);
        }
    }

    private void awardKillPoints(Enemy enemy) {
        player.setTotalPoints(player.getTotalPoints() + enemy.getPoints());
        player.setKillCount(player.getKillCount() + 1);
    }


    private void saveRunResults(boolean won) {
        int score = player.getTotalPoints();
        int kills = player.getKillCount();
        int keys  = player.getKeysObtained();
        int lastLevel = waveManager.getCurrentWaveNumber();

        int userId = RunLog.NO_USER;


        if (LoginMenuController.isLoggedIn()) {
            User user = LoginMenuController.getCurrentUser();
            userId = user.getId();
            new UserDAO().updateRunResults(user, score, kills, keys, won);
        }

        // Always log the run to the JSON log file, recording who was playing
        RunLogDAO.logRun(userId, score, lastLevel);
    }


    @Override
    public void resize(int width, int height) {
        worldViewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        for (Enemy e : enemies) {
            e.dispose();
        }
        if (player != null) {
            player.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (dimBgTexture != null) {
            dimBgTexture.dispose();
        }
        if (panelBgTexture != null) {
            panelBgTexture.dispose();
        }
        if (scoreFont != null)  scoreFont.dispose();
        if (hudFont != null)    hudFont.dispose();
        if (waveFont != null)   waveFont.dispose();
        if (iconHeart != null)     iconHeart.dispose();
        if (iconMissile != null)   iconMissile.dispose();
        if (iconLightning != null) iconLightning.dispose();
        if (iconFood != null)      iconFood.dispose();
        super.dispose();
    }
}
