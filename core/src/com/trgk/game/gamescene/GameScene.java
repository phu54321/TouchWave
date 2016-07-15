package com.trgk.game.gamescene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.game.TGScene;
import com.trgk.game.utils.ScreenFillingGroup;

import java.util.ArrayList;


public class GameScene extends TGScene {
    FrameGroup frameGroup;
    Group logicGroup;
    public GameScene() {
        super(new Stage(new ScreenViewport()));
        frameGroup = new FrameGroup();
        logicGroup = new Group();

        Stage stage = getStage();
        stage.addActor(logicGroup);
        stage.addActor(frameGroup);

        logicGroup.addActor(new FrameGenerator(this));
    }


    ArrayList<HitFrame> aliveFrames;
    boolean gameCompleted = false;

    @Override
    public void act(float dt) {
        super.act(dt);

        if (gameCompleted && this.getSceneManager() != null) {
            // Add new circles
            this.getSceneManager().setCurrentScene(new GameCompletedScene(this));
            this.logicGroup.remove();
        }
    }
}
