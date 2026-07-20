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
import com.github.ghomerl.chimken.model.KeyBindings;
import com.github.ghomerl.chimken.model.entities.enemies.ChickenEnemy;
import com.github.ghomerl.chimken.model.entities.enemies.DoubleEggChicken;
import com.github.ghomerl.chimken.model.entities.enemies.Enemy;
import com.github.ghomerl.chimken.model.entities.Player;
import com.github.ghomerl.chimken.model.entities.enemies.SniperChicken;
import com.github.ghomerl.chimken.model.entities.items.Item;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;
import com.github.ghomerl.chimken.model.entities.projectiles.SuperheatedMetalloidChunk;
import com.github.ghomerl.chimken.model.entities.weapons.BoronRailgun;
import com.github.ghomerl.chimken.model.entities.weapons.PlasmaBlaster;
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

    // ── Enemies ───────────────────────────────────────────────────
    private final Array<Enemy> enemies = new Array<>();

    private ChickenEnemy chicken;
    private DoubleEggChicken doubleEggChicken;
    private SniperChicken sniperChicken;

    private ChickenController chickenController;
    private DoubleEggChickenController doubleEggChickenController;
    private SniperChickenController sniperChickenController;

    private ChickenRenderer chickenRenderer;
    private EggProjectileRenderer eggProjectileRenderer;

    // ── Items ────────────────────────────────────────────────────
    private final Array<Item> items = new Array<>();
    private ItemRenderer itemRenderer;
    private SuperheatedMetalloidChunkRenderer chunkRenderer;

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
            // First entry: let AbstractScreen create cameras, viewports,
            // stage, skin, and the input multiplexer.
            super.show();
            initGame();
            initialized = true;
        } else {
            // Re-entry (e.g. returning from Settings while paused).
            // Do NOT call super.show() — it would create NEW viewport/
            // camera objects that are disconnected from the stage's
            // internal viewport, causing all UI to vanish.
            // Instead just update the existing viewports and rebuild
            // the input multiplexer.
            worldViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

            InputMultiplexer multiplexer = new InputMultiplexer();
            multiplexer.addProcessor(stage);
            Gdx.input.setInputProcessor(multiplexer);
        }

        // Both first entry and re-entry need the game input processors.
        reAddInputProcessors();

        // When returning from Settings while still paused, make sure the
        // overlay is visible again.
        if (pauseController != null && pauseController.isPaused()) {
            pauseOverlay.setVisible(true);
        }
    }

    /**
     * One-time setup: renderers, player, enemies, UI, pause overlay.
     * Only called on the first {@link #show()}.
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

        // ── Enemies (one of each kind) ─────────────────────────────
        float ww = worldViewport.getWorldWidth();

        chicken = new ChickenEnemy(ww * 0.2f, 900f);
        doubleEggChicken = new DoubleEggChicken(ww * 0.5f - DoubleEggChicken.WIDTH / 2f, 950f);
        sniperChicken = new SniperChicken(ww * 0.75f, 860f);

        enemies.addAll(chicken, doubleEggChicken, sniperChicken);

        chickenController = new ChickenController(chicken);
        doubleEggChickenController = new DoubleEggChickenController(doubleEggChicken);
        sniperChickenController = new SniperChickenController(sniperChicken, player);

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

    /**
     * Adds the {@link PauseController} and {@link PlayerController}
     * to the front of the current InputMultiplexer.
     * <p>
     * Order: pauseController (index 0, catches ESC first) →
     *        playerController (index 1, movement + shooting) →
     *        stage (last, handles UI clicks)
     */
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
        // ── 1-pixel textures used as tiled backgrounds ─────────────
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

        // ── Full-screen dim layer ──────────────────────────────────
        pauseOverlay = new Table();
        pauseOverlay.setFillParent(true);
        pauseOverlay.setBackground(new TextureRegionDrawable(new TextureRegion(dimBgTexture)));

        // ── Centred 640 × 360 panel ───────────────────────────────
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

        // ── Button listeners ───────────────────────────────────────
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

    /**
     * Called by {@link GameScreenController#returnToPausedGame()} to
     * restore the paused state after returning from the Settings screen.
     */
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

        // Keep the overlay visibility in sync with the controller state
        // (covers both ESC-toggle and button-toggle paths).
        if (pauseOverlay != null) {
            pauseOverlay.setVisible(pauseController.isPaused());
        }

        if (pauseController.isPaused()) {
            renderPausedFrame(clamped);
            return;
        }

        renderGameFrame(clamped);
    }

    /**
     * Draws the frozen game world (no updates) plus the pause overlay.
     */
    private void renderPausedFrame(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawWorldFilled();
        drawWorldDebug();

        // UI pass — draws the pause overlay on top
        uiViewport.apply();
        stage.act(delta);
        stage.draw();
    }

    /**
     * Full game frame: update → collisions → death check → draw.
     */
    private void renderGameFrame(float delta) {
        // ── Update ─────────────────────────────────────────────────
        playerController.update(delta);
        chickenController.update(delta);
        doubleEggChickenController.update(delta);
        sniperChickenController.update(delta);

        // ── Items ─────────────────────────────────────────────────
        ItemController.update(items, player, delta);

        // ── Collision detection ────────────────────────────────────
        if (!playerController.isPlayerDead()) {
            resolveCollisions();
        }

        // ── Death check ────────────────────────────────────────────
        if (playerController.isPlayerDead()) {
            MusicManager.stopBattleTheme();
            ScreenManager.setScreen(new GameOverScreen(player.getTotalPoints()));
            return;
        }

        // ── Draw ───────────────────────────────────────────────────
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawWorldFilled();
        drawWorldDebug();

        // UI pass
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
            eggProjectileRenderer.render(shapeRenderer, e.getWeapon().getProjectiles());
        }

        // Items
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
            eggProjectileRenderer.renderDebug(shapeRenderer, e.getWeapon().getProjectiles());
        }

        // Items (magenta debug)
        shapeRenderer.setColor(Color.MAGENTA);
        itemRenderer.renderDebug(shapeRenderer, items);

        shapeRenderer.end();
    }

    // ══════════════════════════════════════════════════════════════
    //  Collision resolution
    // ══════════════════════════════════════════════════════════════

    private void resolveCollisions() {
        // 1) Player body ↔ Enemy body (enemy dies, player loses 1 HP, rise respawn)
        for (Enemy e : enemies) {
            if (CollisionController.checkPlayerEnemyCollision(player, e)) {
                playerController.onPlayerHit(PlayerController.RESPAWN_RISE);
                e.takeDamage(e.getHp()); // instant kill
                awardKillPoints(e);
                DropController.rollDrops(e, player, items, worldViewport.getWorldWidth());
                break;
            }
        }

        // 2) Enemy egg projectile ↔ Player (egg disappears, player -1 HP, teleport respawn)
        if (CollisionController.canPlayerCollide(player)) {
            boolean playerWasHit = false;
            for (Enemy e : enemies) {
                if (playerWasHit) break;
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

        // 3) Player plasma projectile ↔ Enemy (bolt disappears, enemy -1 HP)
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

    /**
     * Renders the player's weapon projectiles using the correct
     * renderer for the currently equipped weapon.
     */
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

    /**
     * Adds the enemy's point value to the player's score and
     * increments the kill counter.
     */
    private void awardKillPoints(Enemy enemy) {
        player.setTotalPoints(player.getTotalPoints() + enemy.getPoints());
        player.setKillCount(player.getKillCount() + 1);
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
