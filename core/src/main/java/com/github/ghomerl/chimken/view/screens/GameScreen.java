package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
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
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;
import com.github.ghomerl.chimken.model.entities.projectiles.SuperheatedMetalloidChunk;
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

        // ── Wave system ────────────────────────────────────────────
        float ww = worldViewport.getWorldWidth();
        float wh = worldViewport.getWorldHeight();
        Array<Wave> waves = LevelFactory.buildLevel1(ww, wh);
        // Level 1 has two phases (index 0 and 1), both part of wave 1.
        // lastWaveIndex = 1 → winning after phase B is cleared.
        waveManager = new WaveManager(waves, 1);
        spawnNextWave();

        // ── UI: back button ────────────────────────────────────────
        Stack stack = new Stack();
        stack.setFillParent(true);

        Table backBtnWrapper = new Table();
        backBtnWrapper.top().left().pad(12);
        TextButton backBtn = new TextButton("back", skin);
        backBtnWrapper.add(backBtn).width(240).height(60);

        stack.add(backBtnWrapper);
        stage.addActor(stack);

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreenController.openPreGameMenu();
            }
        });

        // ── UI: pause overlay ──────────────────────────────────────
        createPauseOverlay();
    }

    // ══════════════════════════════════════════════════════════════
    //  Wave management
    // ══════════════════════════════════════════════════════════════

    /**
     * Spawns all enemies of the next wave, creates their controllers,
     * and stores the spawn entries for movement.
     */
    private void spawnNextWave() {
        // Dispose old enemies' weapons
        for (Enemy e : enemies) {
            e.dispose();
        }
        enemies.clear();
        chickenControllers.clear();

        waveManager.spawnCurrentWave(enemies);
        activeSpawns = waveManager.getCurrentSpawns();

        // Create a chicken controller for each enemy
        for (Enemy e : enemies) {
            chickenControllers.add(new ChickenController(e));
        }
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

        uiViewport.apply();
        stage.act(delta);
        stage.draw();
    }

    private void renderGameFrame(float delta) {
        // ── Update ─────────────────────────────────────────────────
        playerController.update(delta);

        // Enemy movement (formation entry)
        EnemyMovementController.update(activeSpawns, delta);

        // Enemy AI (firing)
        for (ChickenController cc : chickenControllers) {
            cc.update(delta);
        }

        // ── Items ─────────────────────────────────────────────────
        ItemController.update(items, player, delta);

        // ── Collision detection ────────────────────────────────────
        if (!playerController.isPlayerDead()) {
            resolveCollisions();
        }

        // ── Death check ────────────────────────────────────────────
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

    // ══════════════════════════════════════════════════════════════
    //  Collision resolution
    // ══════════════════════════════════════════════════════════════

    private void resolveCollisions() {
        // 1) Player body ↔ Enemy body
        for (Enemy e : enemies) {
            if (CollisionController.checkPlayerEnemyCollision(player, e)) {
                playerController.onPlayerHit(PlayerController.RESPAWN_RISE);
                e.takeDamage(e.getHp());
                awardKillPoints(e);
                DropController.rollDrops(e, player, items, worldViewport.getWorldWidth());
                break;
            }
        }

        // 2) Enemy egg ↔ Player
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

        // 3) Player projectile ↔ Enemy
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
        super.dispose();
    }
}
