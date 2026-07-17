package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.controller.*;
import com.github.ghomerl.chimken.controller.audio.MusicManager;
import com.github.ghomerl.chimken.model.KeyBindings;
import com.github.ghomerl.chimken.model.entities.ChickenEnemy;
import com.github.ghomerl.chimken.model.entities.DoubleEggChicken;
import com.github.ghomerl.chimken.model.entities.Enemy;
import com.github.ghomerl.chimken.model.entities.Player;
import com.github.ghomerl.chimken.model.entities.SniperChicken;
import com.github.ghomerl.chimken.model.entities.projectiles.Projectile;
import com.github.ghomerl.chimken.view.renderers.ChickenRenderer;
import com.github.ghomerl.chimken.view.renderers.EggProjectileRenderer;
import com.github.ghomerl.chimken.view.renderers.PlayerRenderer;
import com.github.ghomerl.chimken.view.renderers.ProjectileRenderer;

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

    // ── Shared ────────────────────────────────────────────────────
    private ShapeRenderer shapeRenderer;

    @Override
    public void show() {
        super.show();

        shapeRenderer = new ShapeRenderer();

        // ── Renderers ──────────────────────────────────────────────
        playerRenderer = new PlayerRenderer();
        projectileRenderer = new ProjectileRenderer();
        chickenRenderer = new ChickenRenderer();
        eggProjectileRenderer = new EggProjectileRenderer();

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

        // Insert the game input processor at the front of the multiplexer
        InputMultiplexer multiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if (multiplexer != null) {
            multiplexer.addProcessor(0, playerController);
        }

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
    }

    @Override
    public void render(float delta) {
        float clamped = Math.min(delta, 1 / 30f);

        // ── Update ─────────────────────────────────────────────────
        playerController.update(clamped);
        chickenController.update(clamped);
        doubleEggChickenController.update(clamped);
        sniperChickenController.update(clamped);

        // ── Collision detection ────────────────────────────────────
        if (!playerController.isPlayerDead()) {
            resolveCollisions();
        }

        // ── Death check ────────────────────────────────────────────
        if (playerController.isPlayerDead()) {
            MusicManager.stopBattleTheme();
            ScreenManager.setScreen(new GameOverScreen(player.getTotalPoints()));
            return; // next frame the new screen takes over
        }

        // ── Clear ──────────────────────────────────────────────────
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // ── World pass: filled shapes ──────────────────────────────
        worldViewport.apply();
        shapeRenderer.setProjectionMatrix(worldCamera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Player + plasma projectiles
        playerRenderer.render(shapeRenderer, player);
        projectileRenderer.render(shapeRenderer, player.getWeapon().getProjectiles());

        // Enemies
        for (Enemy e : enemies) {
            chickenRenderer.render(shapeRenderer, e);
        }

        // Enemy egg projectiles
        for (Enemy e : enemies) {
            eggProjectileRenderer.render(shapeRenderer, e.getWeapon().getProjectiles());
        }

        shapeRenderer.end();

        // ── World pass: debug hitboxes ─────────────────────────────
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Player
        shapeRenderer.setColor(Color.RED);
        playerRenderer.renderDebug(shapeRenderer, player);
        shapeRenderer.setColor(Color.GREEN);
        projectileRenderer.renderDebug(shapeRenderer, player.getWeapon().getProjectiles());

        // Enemies
        shapeRenderer.setColor(Color.YELLOW);
        for (Enemy e : enemies) {
            chickenRenderer.renderDebug(shapeRenderer, e);
        }

        // Enemy projectiles
        shapeRenderer.setColor(Color.ORANGE);
        for (Enemy e : enemies) {
            eggProjectileRenderer.renderDebug(shapeRenderer, e.getWeapon().getProjectiles());
        }

        shapeRenderer.end();

        // ── UI pass ────────────────────────────────────────────────
        uiViewport.apply();
        stage.act(clamped);
        stage.draw();
    }

    // ── Collision resolution ───────────────────────────────────────

    /**
     * Runs all three collision types each frame:
     * <ol>
     *   <li>Player body ↔ Enemy body</li>
     *   <li>Enemy egg projectile ↔ Player body</li>
     *   <li>Player plasma projectile ↔ Enemy body</li>
     * </ol>
     */
    private void resolveCollisions() {
        // 1) Player body ↔ Enemy body (enemy dies, player loses 1 HP, rise respawn)
        for (Enemy e : enemies) {
            if (CollisionController.checkPlayerEnemyCollision(player, e)) {
                playerController.onPlayerHit(PlayerController.RESPAWN_RISE);
                e.takeDamage(e.getHp()); // instant kill
                break; // one body collision per frame
            }
        }

        // 2) Enemy egg projectile ↔ Player (egg disappears, player loses 1 HP, teleport respawn)
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
                    break; // bolt hit one enemy
                }
            }
        }
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
        super.dispose();
    }
}
