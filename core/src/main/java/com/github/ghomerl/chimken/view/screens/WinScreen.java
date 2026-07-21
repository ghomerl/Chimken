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

public class WinScreen extends AbstractScreen {

    private final int score;
    private final int kills;
    private final int keys;

    public WinScreen(int score, int kills, int keys) {
        this.score = score;
        this.kills = kills;
        this.keys = keys;
    }

    @Override
    public void show() {
        super.show();

        Stack stack = new Stack();
        stack.setFillParent(true);


        Table menuBtnWrapper = new Table();
        menuBtnWrapper.top().left().pad(12);
        TextButton menuBtn = new TextButton("Main Menu", skin);
        menuBtnWrapper.add(menuBtn).width(240).height(60);

        Table center = new Table();
        center.center();


        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.buildFont(160, "Bold");
        titleStyle.fontColor = Color.GOLD;
        Label titleLabel = new Label("YOU WIN", titleStyle);
        center.add(titleLabel).row();

        Label.LabelStyle statStyle = new Label.LabelStyle();
        statStyle.font = Assets.buildFont(48, "Default");
        statStyle.fontColor = Color.WHITE;

        center.add(new Label("Score : " + score, statStyle)).padTop(30f).row();
        center.add(new Label("Kills : " + kills, statStyle)).padTop(10f).row();
        center.add(new Label("Keys : " + keys, statStyle)).padTop(10f);

        stack.add(menuBtnWrapper);
        stack.add(center);
        stage.addActor(stack);

        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameOverController.openMainMenu();
            }
        });
    }
}
