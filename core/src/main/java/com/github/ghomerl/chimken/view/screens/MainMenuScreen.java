package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.ghomerl.chimken.controller.ScreenManager;
import com.github.ghomerl.chimken.controller.MainMenuController;
import com.github.ghomerl.chimken.controller.LoginMenuController;
import com.github.ghomerl.chimken.view.assets.Assets;
import com.github.ghomerl.chimken.view.utils.Toast;

public class MainMenuScreen extends AbstractScreen {


    @Override
    public void show() {
        super.show();

        Stack stack = new Stack();
        stack.setFillParent(true);


        Table exitBtnWrapper = new Table();
        exitBtnWrapper.bottom().left().pad(12);
        TextButton exitBtn = new TextButton("Quit", skin);
        exitBtnWrapper.add(exitBtn).width(240).height(60);

        Table loginBtnWrapper = new Table();
        loginBtnWrapper.top().right().pad(10);
        boolean loggedIn = LoginMenuController.isLoggedIn();
        TextButton authBtn = new TextButton(loggedIn ? "Logout" : "Login", skin);
        loginBtnWrapper.add(authBtn).width(240).height(60);

        Table playBtnsWrapper = new Table();
        playBtnsWrapper.center().bottom().pad(12);
        playBtnsWrapper.defaults().width(480).height(80);
        playBtnsWrapper.defaults().pad(12);
        TextButton playBtn = new TextButton("Save the world", skin);
        TextButton leaderboardBtn = new TextButton("Hall of Fame", skin);
        TextButton guideBtn = new TextButton("How to play?", skin);
        TextButton settingsBtn = new TextButton("Options...", skin);
        playBtnsWrapper.add(playBtn).row();
        playBtnsWrapper.add(leaderboardBtn).row();
        playBtnsWrapper.add(guideBtn).row();
        playBtnsWrapper.add(settingsBtn).row();

        Table titleWrapper = new Table();
        titleWrapper.center().top().pad(120);
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.buildFont(132, "Default");
        titleStyle.fontColor = Color.YELLOW;
        Label titleLabel = new Label("Chimken Invaders", titleStyle);
        titleWrapper.add(titleLabel).row();

        Label.LabelStyle subtitleStyle = new Label.LabelStyle();
        subtitleStyle.font = Assets.buildFont(48, "Default");
        subtitleStyle.fontColor = Color.WHITE;
        String welcomeText = LoginMenuController.isLoggedIn()
            ? "Welcome " + LoginMenuController.getCurrentUser().getDisplayName()
            : "You're currently in the guest mode";
        Label welcomeLabel = new Label(welcomeText, subtitleStyle);
        titleWrapper.add(welcomeLabel).padTop(20).row();


        stack.add(titleWrapper);
        stack.add(exitBtnWrapper);
        stack.add(loginBtnWrapper);
        stack.add(playBtnsWrapper);


        stage.addActor(stack);

        if (loggedIn) {
            authBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    MainMenuController.logout();
                }
            });
        } else {
            authBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    MainMenuController.openLoginPage();
                }
            });
        }

        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String error = MainMenuController.openPreGameMenu();
                if (error != null) {
                    Toast.show(stage, skin, error);
                }
            }
        });

        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String error = MainMenuController.openSettings();
                if (error != null) {
                    Toast.show(stage, skin, error);
                }
            }
        });

        leaderboardBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainMenuController.openLeaderboard();
            }
        });

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainMenuController.exitGame();
            }
        });
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();

        skin.dispose();
    }
}
