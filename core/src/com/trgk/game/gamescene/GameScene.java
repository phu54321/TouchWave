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
    Group mainGroup, frameGroup;
    public GameScene() {
        super(new Stage(new ScreenViewport()));
        mainGroup = new ScreenFillingGroup(150, 100);
        frameGroup = new Group();

        getStage().addActor(mainGroup);
        mainGroup.addActor(frameGroup);
        aliveFrames = new ArrayList<HitFrame>();
    }


    float remainingTime = 0;
    int currentFrameIndex = 300;
    ArrayList<HitFrame> aliveFrames;
    boolean gameCompleted = false;

    /**
     * Next remaining time
     * @return times
     */
    float nextRemainingTime() {
        float circlePerSec = (float)Math.sqrt(currentFrameIndex + 16) / 4f;
        float decFactor = currentFrameIndex / (currentFrameIndex + 200f);
        return 1.0f + 0.6f / circlePerSec - 0.9f * decFactor;
    }

    int getRandomCircleNum() {
        float circlePerSec = (float)Math.sqrt(currentFrameIndex + 20.25f) / 4.5f;
        if(circlePerSec > 4.6f) circlePerSec = 4.6f;
        return (int)Math.floor(Math.random() * circlePerSec + 1);
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        for(int i = 0 ; i < aliveFrames.size() ; i++) {
            if(i == 11) break;
            HitFrame frame = aliveFrames.get(i);
            frame.setVisible(true);
            frame.setTouchable(Touchable.enabled);
        }
        for(int i = 11 ; i < aliveFrames.size() ; i++) {
            HitFrame frame = aliveFrames.get(i);
            frame.setVisible(false);
            frame.setTouchable(Touchable.disabled);
        }


        if(gameCompleted && this.getSceneManager() != null) {
        // Add new circles
            this.getSceneManager().setCurrentScene(new GameCompletedScene(this));
        }
        else {
            remainingTime -= dt;
            if (aliveFrames.size() <= 3 && remainingTime >= 0.5f) remainingTime = 0.3f;

            while (remainingTime <= 0) {
                currentFrameIndex++;
                Gdx.app.log("CurrentFrame", Integer.toString(currentFrameIndex));

                int circleNum = getRandomCircleNum();
                HitFrame frame = new HitFrame(this, circleNum);
                frameGroup.addActorAt(0, frame);
                aliveFrames.add(frame);
                frame.act(-remainingTime);

                remainingTime += nextRemainingTime();
            }
        }
    }


}
