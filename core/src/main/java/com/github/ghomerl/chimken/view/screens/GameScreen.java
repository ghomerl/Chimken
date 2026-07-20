package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
import com.github.ghomerl.chimken.model.User;
import com.github.ghomerl.chimken.model.dao.UserDAO;
import com.github.ghomerl.chimken.model.runlog.RunLogDAO;
import com.github.ghomerl.chimken.model.KeyBindings;
import com.github.ghomerl.chimken.model.entities.Player;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.entities.enemies.LevelFactory;
import com.github.ghomerl.chimken.model.entities.enemies.SpawnEntry;
import com.github.ghomerl.chimken.model.entities.enemies.Wave;
import com.github.ghomerl.chimken.model.entities.enemies.WaveManager;
import com.github.ghomerl.chimken.model.entities.items.Item;
import com.github.ghomerl.chimken.model.entities.projectiles.MissileProjectile;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;
import com.github.ghomerl.chimken.model.entities.weapons.BoronRailgun;
import com.github.ghomerl.chimken.view.assets.Assets;
import com.github.ghomerl.chimken.view.renderers.ChickenRenderer;
import com.github.ghomerl.chimken.view.renderers.EggProjectileRenderer;
import com.github.ghomerl.chimken.view.renderers.ItemRenderer;
import com.github.ghomerl.chimken.view.renderers.PlayerRenderer;
import com.github.ghomerl.chimken.view.renderers.ProjectileRenderer;
import com.github.ghomerl.chimken.view.renderers.SuperheatedMetalloidChunkRenderer;

public class GameScreen extends AbstractScreen {

    // ── Player ────────────────────────────────────────────────────
    private Player player;
    private PlayerController playerController;
    private PlayerRenderer playerRenderer;
    private ProjectileRenderer projectileRenderer;

    // ── Enemies (dynamic per wave) ────────────────────────────────
    private final Array<Enemy> enemies = new Array<>();
    private final Array<ChickenController> chickenControllers = new Array<>();

    // ── Wave system ───────────────────────────────────────────────
    private WaveManager waveManager;
    private Array<SpawnEntry> activeSpawns;

    // ── Items ────────────────────────────────────────────────────
    private final Array<Item> items = new Array<>();
    private ItemRenderer itemRenderer;
    private SuperheatedMetalloidChunkRenderer chunkRenderer;

    // ── Renderers ─────────────────────────────────────────────────
    private ChickenRenderer chickenRenderer;
    private EggProjectileRenderer eggProjectileRenderer;

    // ── Pause ─────────────────────────────────────────────────────
    private PauseController pauseController;
    private Table pauseOverlay;
    private Texture dimBgTexture;
    private Texture panelBgTexture;

    // ── Shared ────────────────────────────────────────────────────
    private ShapeRenderer shapeRenderer;

    // ── Re-entry guard ────────────────────────────────────────────
    private boolean initialized = false;

    // ── Wave announcement ─────────────────────────────────────────
    private static final float WAVE_ANNOUNCE_DURATION = 3f;
    private boolean waveAnnouncementActive;
    private float waveAnnouncementTimer;
    private int announcedWaveNumber;
    private BitmapFont waveFont;

    // ── HUD ───────────────────────────────────────────────────────
    private BitmapFont scoreFont;
    private BitmapFont hudFont;

    // ── HUD icon textures ─────────────────────────────────────────
    private Texture iconHeart;
    private Texture iconMissile;
    private Texture iconLightning;
    private Texture iconFood;

    // ── Missiles ──────────────────────────────────────────────────
    private final Array<MissileProjectile> missiles = new Array<>();
    private static final float EXPLOSION_DURATION = 0.4f;
    private static final float EXPLOSION_MAX_RADIUS = 350f;
    private float explosionTimer;
    private float explosionX, explosionY;

    // ── Snake movement ────────────────────────────────────────────
    private SnakeMovementController snakeController;

    // ── Extra lives ───────────────────────────────────────────────
    private int nextLifeThreshold;

    // ══════════════════════════════════════════════════════════════
    //  Lifecycle
    // ══════════════════════════════════════════════════════════════

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

    /**
     * One-time setup: renderers, player, UI, pause overlay,
     * and spawns the first wave.
     */
    private void initGame() {
        shapeRenderer = new ShapeRenderer();

        // ── Renderers ──────────────────────────────────────────────
        playerRenderer = new PlayerRenderer();
        projectileRenderer = new ProjectileRenderer();
        chickenRenderer = new ChickenRenderer();
        eggProjectileRenderer = new EggProjectileRenderer();
        itemRenderer = new ItemRenderer();
        chunkRenderer = new SuperheatedMetalloidChunkRenderer();

        // ── Fonts ──────────────────────────────────────────────────
        scoreFont = Assets.buildFont(36, "Bold");
        hudFont = Assets.buildFont(28, "Default");
        waveFont = Assets.buildFont(120, "Bold");

        // ── HUD icon textures ──────────────────────────────────────
        iconHeart = Assets.iconHeart;
        iconMissile = Assets.iconMissile;
        iconLightning = Assets.iconPower;
        iconFood = Assets.iconFood;

        // ── Player ────────────────────────────────────────────────
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

        // ── Pause controller ──────────────────────────────────────
        pauseController = new PauseController();

        // ── Wave system (Level 1 + Level 2) ──────────────────────
        float ww = worldViewport.getWorldWidth();
        float wh = worldViewport.getWorldHeight();
        Array<Wave> allWaves = new Array<>();
        allWaves.addAll(LevelFactory.buildLevel1(ww, wh));
        allWaves.addAll(LevelFactory.buildLevel2(ww, wh));
        waveManager = new WaveManager(allWaves, allWaves.size - 1);

        // ── Extra-life tracker ────────────────────────────────────
        nextLifeThreshold = 10_000;

        // ── UI: pause overlay only (no back button) ──────────────
        createPauseOverlay();

        // ── Spawn first wave (triggers announcement) ─────────────
        spawnNextWave();
    }

    // ══════════════════════════════════════════════════════════════
    //  Wave management
    // ══════════════════════════════════════════════════════════════

    /**
     * Spawns all enemies of the next wave, creates their controllers,
     * and stores the spawn entries for movement.  Also starts the
     * wave-announcement timer.
     */
    private void spawnNextWave() {
        // Dispose old enemies' weapons
        for (Enemy e : enemies) {
            e.dispose();
        }
        enemies.clear();
        chickenControllers.clear();
        snakeController = null;

        waveManager.spawnCurrentWave(enemies);
        activeSpawns = waveManager.getCurrentSpawns();

        // Create a chicken controller for each enemy
        for (Enemy e : enemies) {
            chickenControllers.add(new ChickenController(e));
        }

        // If the wave uses snake movement, create the controller
        Wave currentWave = waveManager.getCurrentWave();
        if (currentWave != null
            && currentWave.getMovementType() == Wave.MovementType.SNAKE) {
            snakeController = new SnakeMovementController(
                enemies,
                worldViewport.getWorldWidth(),
                worldViewport.getWorldHeight()
            );
        }

        // Start wave announcement
        waveAnnouncementActive = true;
        waveAnnouncementTimer = WAVE_ANNOUNCE_DURATION;
        announcedWaveNumber = waveManager.getCurrentWaveNumber();
    }

    /**
     * Returns the current Wave object (convenience).
     */
    private Wave getCurrentWave() {
        return waveManager.getCurrentWave();
    }

    /**
     * Called when all enemies of the current wave are dead.
     * Either advances to the next wave or triggers the win screen.
     */
    private void onWaveCleared() {
        if (waveManager.wasLastWave()) {
            // Level complete — player wins
            MusicManager.stopBattleTheme();
            MusicManager.playVictoryTheme();
            saveRunResults(true);
            ScreenManager.setScreen(new WinScreen(
                player.getTotalPoints(),
                player.getKillCount(),
                player.getDeathCount(),
                player.getKeysObtained()
            ));
            return;
        }

        // Advance to next wave
        if (waveManager.advanceWave()) {
            spawnNextWave();
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  Input
    // ══════════════════════════════════════════════════════════════

    private void reAddInputProcessors() {
        InputMultiplexer multiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if (multiplexer != null) {
            multiplexer.addProcessor(0, pauseController);
            multiplexer.addProcessor(1, playerController);
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  Pause overlay
    // ══════════════════════════════════════════════════════════════

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

    // ══════════════════════════════════════════════════════════════
    //  Render
    // ══════════════════════════════════════════════════════════════

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
        drawWorldDebug();

        drawHUD();

        uiViewport.apply();
        stage.act(delta);
        stage.draw();
    }

    private void renderGameFrame(float delta) {
        // ── Tick wave-announcement timer (non-blocking) ────────────
        if (waveAnnouncementActive) {
            waveAnnouncementTimer -= delta;
            if (waveAnnouncementTimer <= 0f) {
                waveAnnouncementActive = false;
            }
        }

        // ── Update player (always, even during announcement) ───────
        playerController.update(delta);

        // ── Enemy movement — ONLY when announcement is over ────────
        if (!waveAnnouncementActive) {
            Wave wave = getCurrentWave();
            if (wave != null && wave.getMovementType() == Wave.MovementType.SNAKE) {
                if (snakeController != null) snakeController.update(delta);
            } else {
                EnemyMovementController.update(activeSpawns, delta);
            }

            // Enemy AI (firing) — only when chickens are active
            for (ChickenController cc : chickenControllers) {
                cc.update(delta);
            }
        }

        // ── Items (always active) ─────────────────────────────────
        ItemController.update(items, player, delta);

        // ── Missiles (always active) ──────────────────────────────
        updateMissiles(delta);

        // ── Explosion visual timer ────────────────────────────────
        if (explosionTimer > 0f) {
            explosionTimer -= delta;
        }

        // ── Collision detection (always active) ────────────────────
        if (!playerController.isPlayerDead()) {
            resolveCollisions();
        }

        // ── Extra-life check (always active) ───────────────────────
        checkExtraLife();

        // ── Death check (always active) ────────────────────────────
        if (playerController.isPlayerDead()) {
            MusicManager.stopBattleTheme();
            saveRunResults(false);
            ScreenManager.setScreen(new GameOverScreen(player.getTotalPoints()));
            return;
        }

        // ── Wave cleared check ─────────────────────────────────────
        if (waveManager.isCurrentWaveCleared()) {
            onWaveCleared();
            return;
        }

        // ── Draw ───────────────────────────────────────────────────
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawWorldFilled();
        drawWorldDebug();
        drawExplosion();
        drawHUD();
        drawWaveAnnouncement();

        uiViewport.apply();
        stage.act(delta);
        stage.draw();
    }

    // ── World drawing helpers ──────────────────────────────────────

    private void drawWorldFilled() {
        worldViewport.apply();
        shapeRenderer.setProjectionMatrix(worldCamera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        playerRenderer.render(shapeRenderer, player);
        renderPlayerProjectiles(shapeRenderer);

        for (Enemy e : enemies) {
            if (e.isAlive()) chickenRenderer.render(shapeRenderer, e);
        }

        for (Enemy e : enemies) {
            if (e.getWeapon() != null) {
                eggProjectileRenderer.render(shapeRenderer, e.getWeapon().getProjectiles());
            }
        }

        itemRenderer.render(shapeRenderer, items);

        // Missiles
        for (MissileProjectile m : missiles) {
            if (m.hasExploded()) continue;
            shapeRenderer.setColor(Color.ORANGE);
            shapeRenderer.rect(m.getX() - 6, m.getY() - 14, 12, 28);
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.rect(m.getX() - 3, m.getY() - 10, 6, 20);
        }

        shapeRenderer.end();
    }

    private void drawWorldDebug() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.RED);
        playerRenderer.renderDebug(shapeRenderer, player);

        shapeRenderer.setColor(Color.GREEN);
        renderPlayerProjectileDebug(shapeRenderer);

        shapeRenderer.setColor(Color.YELLOW);
        for (Enemy e : enemies) {
            if (e.isAlive()) chickenRenderer.renderDebug(shapeRenderer, e);
        }

        shapeRenderer.setColor(Color.ORANGE);
        for (Enemy e : enemies) {
            if (e.getWeapon() != null) {
                eggProjectileRenderer.renderDebug(shapeRenderer, e.getWeapon().getProjectiles());
            }
        }

        shapeRenderer.setColor(Color.MAGENTA);
        itemRenderer.renderDebug(shapeRenderer, items);

        shapeRenderer.end();
    }

    // ── Explosion effect ──────────────────────────────────────────

    private void drawExplosion() {
        if (explosionTimer <= 0f) return;

        worldViewport.apply();
        shapeRenderer.setProjectionMatrix(worldCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float progress = 1f - explosionTimer / EXPLOSION_DURATION;
        float radius = progress * EXPLOSION_MAX_RADIUS;
        float alpha = 1f - progress;

        // Outer ring
        shapeRenderer.setColor(1f, 0.6f, 0f, alpha * 0.4f);
        shapeRenderer.circle(explosionX, explosionY, radius);

        // Inner core
        shapeRenderer.setColor(1f, 1f, 0.6f, alpha * 0.6f);
        shapeRenderer.circle(explosionX, explosionY, radius * 0.5f);

        shapeRenderer.end();
    }

    // ── HUD ───────────────────────────────────────────────────────

    private void drawHUD() {
        uiViewport.apply();

        // ── Semi-transparent panel (ShapeRenderer) ─────────────────
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Panel background
        shapeRenderer.setColor(0, 0, 0, 0.45f);
        shapeRenderer.rect(16, 16, 220, 184);

        shapeRenderer.end();

        // ── Icons + text labels (SpriteBatch) ─────────────────────
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        // Row positions (bottom-up, 42px apart)
        float row0y = 176;  // Hearts
        float row1y = 134;  // Missiles
        float row2y = 92;   // Lightning / weapon level
        float row3y = 50;   // Food

        float iconSize = 28f;
        float iconX = 28f;

        // 1) Heart icon — health
        batch.draw(iconHeart, iconX, row0y - iconSize / 2f, iconSize, iconSize);
        // 2) Missile icon — missile count
        batch.draw(iconMissile, iconX, row1y - iconSize / 2f, iconSize, iconSize);
        // 3) Lightning icon — weapon level
        batch.draw(iconLightning, iconX, row2y - iconSize / 2f, iconSize, iconSize);
        // 4) Food icon — food amount
        batch.draw(iconFood, iconX, row3y - iconSize / 2f, iconSize, iconSize);

        hudFont.setColor(Color.WHITE);
        hudFont.draw(batch, String.valueOf(player.getHp()), 64, row0y + 8);
        hudFont.draw(batch, String.valueOf(player.getMissileCount()), 64, row1y + 8);
        hudFont.draw(batch, String.valueOf(player.getWeaponLevel()), 64, row2y + 8);
        hudFont.draw(batch, String.valueOf(player.getFoodObtained()), 64, row3y + 8);

        // Score — top-left
        scoreFont.setColor(Color.WHITE);
        scoreFont.draw(batch, "SCORE  " + player.getTotalPoints(), 20, 1060);

        batch.end();
    }

    // ── Wave announcement ─────────────────────────────────────────

    private void drawWaveAnnouncement() {
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

    // ══════════════════════════════════════════════════════════════
    //  Missile logic
    // ══════════════════════════════════════════════════════════════

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
                            awardKillPoints(e);
                            DropController.rollDrops(e, player, items,
                                worldViewport.getWorldWidth());
                        }
                    }
                }
                // Start explosion visual
                explosionTimer = EXPLOSION_DURATION;
                explosionX = m.getX();
                explosionY = m.getY();
                missiles.removeIndex(i);
            } else if (!m.isActive()) {
                missiles.removeIndex(i);
            }
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  Extra-life logic
    // ══════════════════════════════════════════════════════════════

    private void checkExtraLife() {
        if (player.getTotalPoints() >= nextLifeThreshold) {
            player.setHp(player.getHp() + 1);
            nextLifeThreshold += 10_000;
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  Collision resolution
    // ══════════════════════════════════════════════════════════════

    private void resolveCollisions() {
        // 1) Player body <-> Enemy body
        for (Enemy e : enemies) {
            if (CollisionController.checkPlayerEnemyCollision(player, e)) {
                playerController.onPlayerHit(PlayerController.RESPAWN_RISE);
                e.takeDamage(e.getHp());
                awardKillPoints(e);
                DropController.rollDrops(e, player, items, worldViewport.getWorldWidth());
                break;
            }
        }

        // 2) Enemy egg <-> Player
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
                        playerWasHit = true;
                        break;
                    }
                }
            }
        }

        // 3) Player projectile <-> Enemy
        Array<Projectile> plasmas = player.getWeapon().getProjectiles();
        for (int i = plasmas.size - 1; i >= 0; i--) {
            Projectile p = plasmas.get(i);
            if (!p.isActive()) continue;
            for (Enemy e : enemies) {
                if (CollisionController.checkProjectileEnemyCollision(p, e)) {
                    p.setActive(false);
                    e.takeDamage(p.getDamage());
                    if (!e.isAlive()) {
                        awardKillPoints(e);
                        DropController.rollDrops(e, player, items, worldViewport.getWorldWidth());
                    }
                    break;
                }
            }
        }
    }

    private void renderPlayerProjectiles(ShapeRenderer r) {
        Array<Projectile> projs = player.getWeapon().getProjectiles();
        if (player.getWeapon() instanceof BoronRailgun) {
            chunkRenderer.render(r, projs);
        } else {
            projectileRenderer.render(r, projs);
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

    /**
     * Persists the results of a completed run:
     * <ul>
     *   <li>Saves high-score, kills, keys, and win flag via {@link UserDAO}</li>
     *   <li>Appends a run-log entry to {@code chimken_run_logs.json}</li>
     * </ul>
     * Skips the user-stats update if no user is logged in, but
     * always logs the run.
     *
     * @param won {@code true} if the player cleared the level
     */
    private void saveRunResults(boolean won) {
        int score = player.getTotalPoints();
        int kills = player.getKillCount();
        int keys  = player.getKeysObtained();
        int lastLevel = waveManager.getCurrentWaveNumber();

        // Persist user statistics (high-score, kills, keys, wins)
        if (LoginMenuController.isLoggedIn()) {
            User user = LoginMenuController.getCurrentUser();
            new UserDAO().updateRunResults(user, score, kills, keys, won);
        }

        // Always log the run to the JSON log file
        RunLogDAO.logRun(score, lastLevel);
    }

    // ══════════════════════════════════════════════════════════════
    //  Screen lifecycle
    // ══════════════════════════════════════════════════════════════

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
