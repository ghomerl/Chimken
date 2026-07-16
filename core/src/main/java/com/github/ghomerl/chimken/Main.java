package com.github.ghomerl.chimken;

import com.badlogic.gdx.Game;
import com.github.ghomerl.chimken.controller.ScreenManager;
import com.github.ghomerl.chimken.model.utils.DatabaseManager;
import com.github.ghomerl.chimken.view.assets.Assets;
import com.github.ghomerl.chimken.view.screens.LoadingScreen;


public class Main extends Game {

    @Override
    public void create() {
        ScreenManager.init(this);
        Assets.init();


        ScreenManager.setScreen(new LoadingScreen());
    }

    @Override
    public void dispose() {
        Assets.dispose();
        DatabaseManager.dispose();
        super.dispose();
    }
}
