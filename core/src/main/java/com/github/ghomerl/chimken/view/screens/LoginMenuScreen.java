package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.ghomerl.chimken.controller.LoginMenuController;
import com.github.ghomerl.chimken.view.assets.Assets;
import com.github.ghomerl.chimken.view.utils.Toast;

public class LoginMenuScreen extends AbstractScreen {
    @Override
    public void show() {
        super.show();

        Stack stack = new Stack();
        stack.setFillParent(true);

        Table titleWrapper = new Table();
        titleWrapper.center().top().pad(80);
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.buildFont(120, "Bold");
        titleStyle.fontColor = Color.GOLD;
        Label titleLabel = new Label("Login", titleStyle);
        titleWrapper.add(titleLabel).row();

        Table formTable = new Table();
        formTable.defaults().space(12f);
        formTable.defaults().width(240f).height(45f);


        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("username");
        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        TextButton loginBtn = new TextButton("login", skin);
        TextButton backBtn = new TextButton("back", skin);
        loginBtn.setWidth(60f);
        backBtn.setWidth(60f);



        formTable.add(usernameField).width(480).colspan(2).padBottom(10).row();
        formTable.add(passwordField).width(480).colspan(2).padBottom(20).row();
        formTable.add(backBtn).width(200).padRight(10);
        formTable.add(loginBtn).width(200);

        Table registerBtnWrapper = new Table();
        registerBtnWrapper.top().right().pad(10);
        TextButton registerBtn = new TextButton("Register", skin);
        registerBtnWrapper.add(registerBtn).width(240).height(60);

        stack.add(formTable);
        stack.add(registerBtnWrapper);
        stack.add(titleWrapper);


        stage.addActor(stack);

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LoginMenuController.openMainMenu();
            }
        });

        registerBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LoginMenuController.openRegisterPage();
            }
        });

        loginBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String error = LoginMenuController.login(usernameField.getText(), passwordField.getText());
                if (error == null) {
                    LoginMenuController.openMainMenu();
                } else {
                    Toast.show(stage, skin, error);
                }
            }
        });
    }
}
