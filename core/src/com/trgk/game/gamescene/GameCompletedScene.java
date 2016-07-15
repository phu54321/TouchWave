package com.trgk.game.gamescene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.game.TGScene;

/**
 * Created by phu54 on 2016-07-15.
 */
public class GameCompletedScene extends TGScene {
    final GameScene gameScene;
    public GameCompletedScene(GameScene gameScene) {
        super(new Stage(new ScreenViewport()));
        this.gameScene = gameScene;
        gameScene.ref();
    }

    @Override
    protected void onZeroRef() {
        gameScene.unref();
        super.onZeroRef();
    }

    @Override
    public void act(float dt) {
         super.act(dt);
        gameScene.act(dt);
    }

    @Override
    public void draw() {
        gameScene.draw();
        super.draw();
    }
}
