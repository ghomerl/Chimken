package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.ghomerl.chimken.controller.PreGameMenuController;
import com.github.ghomerl.chimken.controller.ScreenManager;

public class PreGameMenuScreen extends AbstractScreen {
    @Override
    public void show() {
        super.show();

        Stack stack = new Stack();
        stack.setFillParent(true);

        Table backBtnWrapper = new Table();
        backBtnWrapper.defaults().width(120);
        backBtnWrapper.top().left().pad(10f);
        TextButton backBtn = new TextButton("back", skin);
        backBtnWrapper.add(backBtn);

        Table menuBtnsWrapper = new Table();
        menuBtnsWrapper.center().center().pad(12);
        menuBtnsWrapper.defaults().width(480).height(80);
        menuBtnsWrapper.defaults().pad(12);
        TextButton newGameBtn = new TextButton("New Game", skin);
        TextButton achievementsBtn = new TextButton("Achievements", skin);
        TextButton shopBtn = new TextButton("Shop", skin);
        menuBtnsWrapper.add(newGameBtn).row();
        menuBtnsWrapper.add(achievementsBtn).row();
        menuBtnsWrapper.add(shopBtn).row();



        stack.add(backBtnWrapper);
        stack.add(menuBtnsWrapper);


        stage.addActor(stack);

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreGameMenuController.openMainMenu();
            }
        });

        newGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PreGameMenuController.startNewGame();
            }
        });
    }
}
