package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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




        stack.add(backBtnWrapper);


        stage.addActor(stack);

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.setScreen(new MainMenuScreen());
            }
        });
    }
}
