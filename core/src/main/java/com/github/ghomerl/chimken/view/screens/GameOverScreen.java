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
 * Displays "GAME OVER" centred on screen with the player's final
 * score and a main-menu button in the top-left corner.
 */
public class GameOverScreen extends AbstractScreen {

    private final int score;

    public GameOverScreen(int score) {
        this.score = score;
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

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.buildFont(160, "Bold");
        titleStyle.fontColor = Color.RED;
        Label titleLabel = new Label("GAME OVER", titleStyle);
        centre.add(titleLabel).row();

        Label.LabelStyle scoreStyle = new Label.LabelStyle();
        scoreStyle.font = Assets.buildFont(64, "Default");
        scoreStyle.fontColor = Color.WHITE;
        Label scoreLabel = new Label("Your Score : " + score, scoreStyle);
        centre.add(scoreLabel).padTop(40f);

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
