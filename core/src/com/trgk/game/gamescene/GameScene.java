package com.trgk.game.gamescene;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.game.menuscene.GameCompletedScene;
import com.trgk.game.TGScene;
import com.trgk.game.ui.TGText;

import java.util.Locale;


public class GameScene extends TGScene {
    FrameGroup frameGroup;
    Group logicGroup;
    TGText scoreText;
    float elapsedTime = 0;
    public GameScene() {
        super(new Stage(new ScreenViewport()));
        frameGroup = new FrameGroup();
        logicGroup = new Group();
        scoreText = new TGText("Score : 0        Time : 0.0s", 10);

        Stage stage = getStage();
        stage.addActor(logicGroup);
        stage.addActor(frameGroup);

        logicGroup.addActor(new FrameGenerator(this));
    }


    boolean gameCompleted = false;

    public int getScore() {
        return frameGroup.getScore();
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

        elapsedTime += dt;
        String scoreString = String.format(Locale.ENGLISH, "Score : %d    Time : %.1f", getScore(), elapsedTime);
        scoreText.setText(scoreString);
    }
}
