package com.trgk.game.menuscene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.game.TGScene;
import com.trgk.game.gamescene.GameScene;
import com.trgk.game.utils.ScreenFillingGroup;
import com.trgk.game.ui.TGText;

/**
 * Created by phu54 on 2016-07-15.
 */
public class GameCompletedScene extends TGScene {
    final GameScene gameScene;
    public GameCompletedScene(GameScene gameScene) {
        super(new Stage(new ScreenViewport()));
        this.gameScene = gameScene;
        gameScene.ref();

        ScreenFillingGroup g = new ScreenFillingGroup(150, 100);
        this.getStage().addActor(g);

        TGText newText = new TGText("Test string");
        newText.setPosition(75, 50, Align.center);
        newText.setScale(10f);
        newText.setColor(1, 0, 0, 1);
        g.addActor(newText);
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
