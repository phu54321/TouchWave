package com.trgk.game.gamescene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;

class FrameGenerator extends Actor {
    GameScene parent;
    public FrameGenerator(GameScene parent) {
        this.parent = parent;
    }

    float remainingTime = 0;
    int currentFrameIndex = 0;

    float nextRemainingTime() {
        float circlePerSec = (float)Math.sqrt(currentFrameIndex + 25) / 5f;
        float decFactor = currentFrameIndex / (currentFrameIndex + 300f);
        return 1.0f + 0.7f / circlePerSec - 0.9f * decFactor;
    }

    int getRandomCircleNum() {
        float circlePerSec = (float)Math.sqrt(currentFrameIndex + 20.25f) / 4.5f;
        if(circlePerSec > 4.6f) circlePerSec = 4.6f;
        return (int)Math.floor(Math.random() * circlePerSec + 1);
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        FrameGroup frameGroup = parent.frameGroup;

        remainingTime -= dt;
        // To few frames -> Add more.
        if (frameGroup.getAliveFrameCount()<= 3 && remainingTime >= 0.5f) remainingTime = 0.3f;

        while (remainingTime <= 0) {
            currentFrameIndex++;
            Gdx.app.log("CurrentFrame", Integer.toString(currentFrameIndex));

            int circleNum = getRandomCircleNum();
            HitFrame frame = new HitFrame(parent, circleNum);
            frameGroup.addFrame(frame);
            frame.act(-remainingTime);

            remainingTime += nextRemainingTime();
        }
    }
}
