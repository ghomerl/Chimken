package com.github.ghomerl.chimken;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.ghomerl.chimken.controller.ScreenManager;
import com.github.ghomerl.chimken.view.assets.Assets;
import com.github.ghomerl.chimken.view.screens.MainMenuScreen;

public class Main extends Game {

    private boolean assetsLoaded = false;

    @Override
    public void create() {
        ScreenManager.init(this);
        Assets.init();
        Assets.queueLoad();

        MainMenuScreen mainMenuScreen = new MainMenuScreen();
        setScreen(mainMenuScreen);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0f, 0f, 0f, 0f);
        super.render();


        if (!assetsLoaded) {
            if (Assets.update()) {
                assetsLoaded = true;
                ScreenManager.setScreen(new MainMenuScreen());
            }
        }
    }

    @Override
    public void dispose() {
        Assets.dispose();
        super.dispose();
    }
}
