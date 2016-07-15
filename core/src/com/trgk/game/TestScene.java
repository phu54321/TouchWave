package com.trgk.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.game.gamescene.GameScene;
import com.trgk.game.utils.ScreenFillingGroup;
import com.trgk.game.utils.TGResources;

public class TestScene extends TGScene {
    BitmapFont font;

    public TestScene() {
        super(new Stage(new ScreenViewport()));

        ScreenFillingGroup mainGroup = new ScreenFillingGroup(640, 480);
        mainGroup.addActor(
                new Label("Text", TGResources.getInstance().skin)
        );
        getStage().addActor(mainGroup);
    }

    @Override
    public void act(float dt) {
        getSceneManager().setCurrentScene(new GameScene());
    }
}
