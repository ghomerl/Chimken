package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.ghomerl.chimken.controller.RegisterMenuController;
import com.github.ghomerl.chimken.view.utils.Toast;

public class RegisterMenuScreen extends AbstractScreen {
    @Override
    public void show() {
        super.show();

        Stack stack = new Stack();
        stack.setFillParent(true);

        Table formTable = new Table();
        formTable.defaults().space(12f);
        formTable.defaults().width(240f).height(45f);


        TextField displayNameField = new TextField("", skin);
        displayNameField.setMessageText("Display name");
        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("username");
        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        TextButton registerBtn = new TextButton("register", skin);
        TextButton backBtn = new TextButton("back", skin);
        registerBtn.setWidth(60f);
        backBtn.setWidth(60f);


        formTable.add(displayNameField).width(480).colspan(2).padBottom(10).row();
        formTable.add(usernameField).width(480).colspan(2).padBottom(10).row();
        formTable.add(passwordField).width(480).colspan(2).padBottom(20).row();
        formTable.add(backBtn).width(200).padRight(10);
        formTable.add(registerBtn).width(200);


        Table loginBtnWrapper = new Table();
        loginBtnWrapper.top().right().pad(10);
        TextButton loginBtn = new TextButton("Login", skin);
        loginBtnWrapper.add(loginBtn).width(240).height(60);


        stack.add(formTable);
        stack.add(loginBtnWrapper);

        stage.addActor(stack);

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RegisterMenuController.openMainMenu();
            }
        });

        loginBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RegisterMenuController.openLoginPage();
            }
        });

        registerBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String error = RegisterMenuController.register(
                    displayNameField.getText(), usernameField.getText(), passwordField.getText());
                if (error == null) {
                    RegisterMenuController.openLoginPage();
                } else {
                    Toast.show(stage, skin, error);
                }
            }
        });
    }
}
