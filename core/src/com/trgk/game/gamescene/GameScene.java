package com.trgk.game.gamescene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.game.TGScene;
import com.trgk.game.menuscene.GameoverScene;
import com.trgk.game.ui.TGText;
import com.trgk.game.utils.ScreenFillingGroup;

import java.util.Locale;


public class GameScene extends TGScene {
    ScreenFillingGroup uiGroup;
    Group logicGroup;

    FrameGroup frameGroup;
    TGText scoreText;
    float elapsedTime = 0;
    public GameScene() {
        super(new Stage(new ScreenViewport()));

        // Add ui & logic groups
        Stage stage = getStage();
        uiGroup = new ScreenFillingGroup(150, 100);
        stage.addActor(uiGroup);
        logicGroup = new Group();
        stage.addActor(logicGroup);

        // UI group
        frameGroup = new FrameGroup();
        uiGroup.addActor(frameGroup);

        scoreText = new TGText("Score : 0        Time : 0.00s", 5, 0, 0, Color.BLACK);
        uiGroup.addActor(scoreText);
        scoreText.setOrigin(Align.topLeft);

        // Logic group
        logicGroup.addActor(new FrameGenerator(this));
    }


    boolean gameCompleted = false;

    public int getScore() {
        return frameGroup.getScore();
    }

    public void issueGameComplete() {
        if(gameCompleted) return;
        gameCompleted = true;
        this.getSceneManager().setCurrentScene(new GameoverScene(this));
        this.logicGroup.remove();
        this.frameGroup.setTouchable(Touchable.disabled);
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        if(gameCompleted) return;
        elapsedTime += dt;
        String scoreString = String.format(Locale.ENGLISH, "Score : %d    Time : %.2f", getScore(), elapsedTime);
        scoreText.setText(scoreString);
    }

    @Override
    public void draw() {
        Vector2 topLeft = uiGroup.getlogicalScreenTopLeft();
        scoreText.setPosition(topLeft.x + 2, topLeft.y - 2, Align.topLeft);
        super.draw();
    }
}
