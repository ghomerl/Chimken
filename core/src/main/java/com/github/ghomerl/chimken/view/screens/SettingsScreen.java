package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.ghomerl.chimken.controller.SettingController;
import com.github.ghomerl.chimken.controller.AudioSettingController;
import com.github.ghomerl.chimken.view.utils.Toast;

public class SettingsScreen extends AbstractScreen {

    @Override
    public void show() {
        super.show();

        Stack stack = new Stack();
        stack.setFillParent(true);

        Table backBtnWrapper = new Table();
        backBtnWrapper.top().left().pad(12);
        TextButton backBtn = new TextButton("Back", skin);
        backBtnWrapper.add(backBtn).width(240).height(60);

        Table settingBtnsWrapper = new Table();
        settingBtnsWrapper.center().center().pad(12);
        settingBtnsWrapper.defaults().width(480).height(80);
        settingBtnsWrapper.defaults().pad(12);
        TextButton audioBtn = new TextButton("Audio", skin);
        TextButton controlBtn = new TextButton("Controls", skin);
        settingBtnsWrapper.add(audioBtn).row();
        settingBtnsWrapper.add(controlBtn).row();



        stack.add(backBtnWrapper);
        stack.add(settingBtnsWrapper);

        stage.addActor(stack);

        String pending = AudioSettingController.getPendingToast();
        if (pending != null) {
            Toast.show(stage, skin, pending);
        }

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SettingController.openMainMenu();
            }
        });

        audioBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SettingController.openAudioSettings();
            }
        });

    }
}
