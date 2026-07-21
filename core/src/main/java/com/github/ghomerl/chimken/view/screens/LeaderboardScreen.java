package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.github.ghomerl.chimken.controller.LeaderboardController;
import com.github.ghomerl.chimken.model.User;
import com.github.ghomerl.chimken.model.utils.DatabaseManager;
import com.github.ghomerl.chimken.view.assets.Assets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardScreen extends AbstractScreen {

    private static final int TOP_N = 10;

    @Override
    public void show() {
        super.show();


        Array<User> users = DatabaseManager.getUsers();
        List<User> sorted = new ArrayList<>();
        for (User u : users) {
            sorted.add(u);
        }
        Collections.sort(sorted, (a, b) -> {
            if (a.getHighScore() != b.getHighScore()) {
                return Integer.compare(b.getHighScore(), a.getHighScore());
            }
            if (a.getTotalKills() != b.getTotalKills()) {
                return Integer.compare(b.getTotalKills(), a.getTotalKills());
            }
            String an = a.getDisplayName() == null ? "" : a.getDisplayName();
            String bn = b.getDisplayName() == null ? "" : b.getDisplayName();
            return an.compareToIgnoreCase(bn);
        });

        Stack stack = new Stack();
        stack.setFillParent(true);

        Table titleWrapper = new Table();
        titleWrapper.center().top().pad(80);
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.buildFont(120, "Bold");
        titleStyle.fontColor = Color.GOLD;
        Label titleLabel = new Label("Hall of Fame", titleStyle);
        titleWrapper.add(titleLabel).row();


        Table backBtnWrapper = new Table();
        backBtnWrapper.top().left().pad(12);
        TextButton backBtn = new TextButton("Back", skin);
        backBtnWrapper.add(backBtn).width(240).height(60);


        Table centre = new Table();
        centre.center();

        if (sorted.isEmpty()) {
            Label.LabelStyle emptyStyle = new Label.LabelStyle();
            emptyStyle.font = Assets.buildFont(48, "Default");
            emptyStyle.fontColor = Color.WHITE;
            centre.add(new Label("No scores yet — be the first!", emptyStyle));
        } else {
            Label.LabelStyle headerStyle = new Label.LabelStyle();
            headerStyle.font = Assets.buildFont(40, "Bold");
            headerStyle.fontColor = Color.YELLOW;


            Label.LabelStyle rowStyle = new Label.LabelStyle();
            rowStyle.font = Assets.buildFont(40, "Default");
            rowStyle.fontColor = Color.WHITE;


            Label.LabelStyle topStyle = new Label.LabelStyle();
            topStyle.font = Assets.buildFont(40, "Bold");
            topStyle.fontColor = Color.GOLD;


            float rankW   = 120f;
            float nameW   = 700f;
            float scoreW  = 360f;
            float killsW  = 300f;
            float rowPad  = 8f;


            centre.add(new Label("#",       headerStyle)).width(rankW).padBottom(rowPad);
            centre.add(new Label("Player",  headerStyle)).width(nameW).padBottom(rowPad);
            centre.add(new Label("Score",   headerStyle)).width(scoreW).padBottom(rowPad);
            centre.add(new Label("Kills",   headerStyle)).width(killsW).padBottom(rowPad);
            centre.row();

            int count = Math.min(TOP_N, sorted.size());
            for (int i = 0; i < count; i++) {
                User u = sorted.get(i);
                Label.LabelStyle style = (i == 0) ? topStyle : rowStyle;

                String name = u.getDisplayName();
                if (name == null || name.isEmpty()) {
                    name = u.getUsername();
                }
                if (name == null || name.isEmpty()) {
                    name = "(unknown)";
                }

                centre.add(new Label(String.valueOf(i + 1),style)).width(rankW).padBottom(rowPad);
                centre.add(new Label(name,style)).width(nameW).padBottom(rowPad);
                centre.add(new Label(String.valueOf(u.getHighScore()), style)).width(scoreW).padBottom(rowPad);
                centre.add(new Label(String.valueOf(u.getTotalKills()), style)).width(killsW).padBottom(rowPad);
                centre.row();
            }
        }

        stack.add(titleWrapper);
        stack.add(backBtnWrapper);
        stack.add(centre);
        stage.addActor(stack);


        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LeaderboardController.openMainMenu();
            }
        });
    }
}
