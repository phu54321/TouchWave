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
        float minCircleNum = (float)Math.sqrt(currentFrameIndex + 100f) / 5f - 1;
        if(minCircleNum > 2.6f) minCircleNum = 2.6f;
        float maxCirclePerNum = (float)Math.sqrt(currentFrameIndex + 20.25f) / 4.5f + 1;
        if(maxCirclePerNum > 4.6f) maxCirclePerNum = 4.6f;
        return (int)Math.floor(Math.random() * (maxCirclePerNum - minCircleNum) + minCircleNum);
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        FrameGroup frameGroup = parent.frameGroup;

        remainingTime -= dt;
        // To few frames -> Add more.
        float maxRemainingTIme = getRandomCircleNum() * 0.1f;
        if (frameGroup.getAliveFrameCount()<= 3 && remainingTime >= maxRemainingTIme)
            remainingTime = maxRemainingTIme;

        while (remainingTime <= 0) {
            currentFrameIndex++;

            int circleNum = getRandomCircleNum();
            HitFrame frame = new HitFrame(parent, circleNum);
            frameGroup.addFrame(frame);
            frame.act(-remainingTime);

            remainingTime += nextRemainingTime();
        }
    }
}
