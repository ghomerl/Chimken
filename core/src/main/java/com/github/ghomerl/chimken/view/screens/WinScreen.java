package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.ghomerl.chimken.controller.GameOverController;
import com.github.ghomerl.chimken.view.assets.Assets;

/**
 * Displays "YOU WIN" centred on screen with the player's
 * run statistics and a main-menu button in the top-left corner.
 * <p>
 * The victory theme is already playing when this screen is
 * created.  Pressing the Main Menu button switches back to
 * the main theme.
 */
public class WinScreen extends AbstractScreen {

    private final int score;
    private final int kills;
    private final int deaths;
    private final int keys;

    public WinScreen(int score, int kills, int deaths, int keys) {
        this.score = score;
        this.kills = kills;
        this.deaths = deaths;
        this.keys = keys;
    }

    @Override
    public void show() {
        super.show();

        Stack stack = new Stack();
        stack.setFillParent(true);

        // ── Main Menu button (top-left) ───────────────────────────
        Table menuBtnWrapper = new Table();
        menuBtnWrapper.top().left().pad(12);
        TextButton menuBtn = new TextButton("Main Menu", skin);
        menuBtnWrapper.add(menuBtn).width(240).height(60);

        // ── Centre content ────────────────────────────────────────
        Table centre = new Table();
        centre.center();

        // Title
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.buildFont(160, "Bold");
        titleStyle.fontColor = Color.GOLD;
        Label titleLabel = new Label("YOU WIN", titleStyle);
        centre.add(titleLabel).row();

        // Stats
        Label.LabelStyle statStyle = new Label.LabelStyle();
        statStyle.font = Assets.buildFont(48, "Default");
        statStyle.fontColor = Color.WHITE;

        centre.add(new Label("Score : " + score, statStyle)).padTop(30f).row();
        centre.add(new Label("Kills : " + kills, statStyle)).padTop(10f).row();
        centre.add(new Label("Deaths : " + deaths, statStyle)).padTop(10f).row();
        centre.add(new Label("Keys : " + keys, statStyle)).padTop(10f);

        stack.add(menuBtnWrapper);
        stack.add(centre);
        stage.addActor(stack);

        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameOverController.openMainMenu();
            }
        });
    }
}
