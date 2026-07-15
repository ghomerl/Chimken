package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.ghomerl.chimken.controller.ScreenManager;
import com.github.ghomerl.chimken.controller.MainMenuController;

public class MainMenuScreen extends AbstractScreen {


    @Override
    public void show() {
        super.show();

        Stack stack = new Stack();
        stack.setFillParent(true);


        Table exitBtnWrapper = new Table();
        exitBtnWrapper.bottom().left().pad(10);
        TextButton exitBtn = new TextButton("Quit", skin);
        exitBtnWrapper.add(exitBtn).width(150);

        Table loginBtnWrapper = new Table();
        loginBtnWrapper.top().right().pad(10);
        TextButton loginBtn = new TextButton("Login", skin);
        loginBtnWrapper.add(loginBtn).width(150);

        Table playBtnsWrapper = new Table();
        playBtnsWrapper.center().bottom().pad(10);
        playBtnsWrapper.defaults().width(150);
        playBtnsWrapper.defaults().height(35);
        playBtnsWrapper.defaults().pad(10);
        TextButton playBtn = new TextButton("Save the world", skin);
        TextButton leaderboardBtn = new TextButton("Hall of Fame", skin);
        TextButton settingsBtn = new TextButton("Options...", skin);
        playBtnsWrapper.add(playBtn).row();
        playBtnsWrapper.add(leaderboardBtn).row();
        playBtnsWrapper.add(settingsBtn).row();




        stack.add(exitBtnWrapper);
        stack.add(loginBtnWrapper);
        stack.add(playBtnsWrapper);


        stage.addActor(stack);

        loginBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.setScreen(new LoginMenuScreen());
            }
        });

        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainMenuController.openPreGameMenu();
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

    }
}
