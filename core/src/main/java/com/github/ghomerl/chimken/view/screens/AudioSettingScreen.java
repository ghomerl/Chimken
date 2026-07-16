package com.github.ghomerl.chimken.view.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.ghomerl.chimken.controller.AudioSettingController;
import com.github.ghomerl.chimken.controller.audio.AudioManager;
import com.github.ghomerl.chimken.controller.audio.MusicManager;
import com.github.ghomerl.chimken.controller.audio.SfxManager;




public class AudioSettingScreen extends AbstractScreen {

    @Override
    public void show() {
        super.show();

        Stack stack = new Stack();
        stack.setFillParent(true);


        Table backBtnWrapper = new Table();
        backBtnWrapper.defaults().width(240).height(60);
        backBtnWrapper.top().left().pad(12f);
        TextButton backBtn = new TextButton("back", skin);
        backBtnWrapper.add(backBtn);

        Table sliderWrapper = new Table();
        sliderWrapper.defaults().width(360).height(60);
        sliderWrapper.defaults().space(10f);
        sliderWrapper.center().center().pad(12f);
        Label masterLabel = new Label("Master Volume", skin);
        Label sfxLabel = new Label("Sound Effect Volume", skin);
        Label musicLabel = new Label("Music Volume", skin);
        Slider masterSlider = new Slider(0, 100, 1, false, skin);
        Slider musicSlider = new Slider(0, 100, 1, false, skin);
        Slider sfxSlider = new Slider(0, 100, 1, false, skin);
        masterSlider.setValue(AudioManager.getMasterVolume());
        musicSlider.setValue(MusicManager.getMusicVolume());
        sfxSlider.setValue(SfxManager.getSfxVolume());
        sliderWrapper.add(masterLabel).padBottom(10).row();
        sliderWrapper.add(masterSlider).width(300).padBottom(40).row();
        sliderWrapper.add(musicLabel).padBottom(10).row();
        sliderWrapper.add(musicSlider).width(300).padBottom(40).row();
        sliderWrapper.add(sfxLabel).padBottom(10).row();
        sliderWrapper.add(sfxSlider).width(300).padBottom(40).row();


        stack.add(backBtnWrapper);
        stack.add(sliderWrapper);

        stage.addActor(stack);

        backBtn.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioSettingController.saveAndGoBack(
                    (int) masterSlider.getValue(),
                    (int) musicSlider.getValue(),
                    (int) sfxSlider.getValue()
                );
            }
        });

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MusicManager.setMusicVolume(musicSlider.getValue());
                MusicManager.updateMusicVolume();
            }
        });

        masterSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.setMasterVolume(masterSlider.getValue());
                AudioManager.updateMasterVolume();
            }
        });

        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SfxManager.setSfxVolume(sfxSlider.getValue());
                SfxManager.updateSfxVolume();
            }
        });
    }

}
