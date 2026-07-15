package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.ghomerl.chimken.controller.ScreenManager;

public class LoginMenuScreen extends AbstractScreen {
    @Override
    public void show() {
        super.show();

        Table rootTable = new Table();
        rootTable.setFillParent(true);

        Table formTable = new Table();
        formTable.defaults().space(10f);

        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("username");
        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("password");

        TextButton loginBtn = new TextButton("login", skin);
        TextButton backBtn = new TextButton("back", skin);


        formTable.add(usernameField).growX().colspan(2).row();
        formTable.add(passwordField).growX().colspan(2).row();
        formTable.add(backBtn);
        formTable.add(loginBtn);


        rootTable.add(formTable);

        stage.addActor(rootTable);

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.setScreen(new MainMenuScreen());
            }
        });
    }
}
