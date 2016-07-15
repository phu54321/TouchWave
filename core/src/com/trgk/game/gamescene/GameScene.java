package com.trgk.game.gamescene;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.game.menuscene.GameCompletedScene;
import com.trgk.game.TGScene;


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


    boolean gameCompleted = false;

    public int getScore() {
        return frameGroup.score;
    }
    public void issueGameComplete() {
        if(gameCompleted) return;
        gameCompleted = true;
        this.getSceneManager().setCurrentScene(new GameCompletedScene(this));
        this.logicGroup.remove();
    }

    @Override
    public void act(float dt) {
        super.act(dt);
    }
}
