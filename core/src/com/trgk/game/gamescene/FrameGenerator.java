package com.trgk.game.gamescene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.trgk.game.utils.HSVRGB;

class FrameGenerator extends Actor {
    GameScene parent;
    public FrameGenerator(GameScene parent) {
        this.parent = parent;
        this.currentHue = (float)Math.random();
    }

    float remainingTime = 0;
    int currentFrameIndex = 0;


    float nextRemainingTime() {
        float circlePerSec = (float)Math.sqrt(currentFrameIndex + 25) / 5f;
        float decFactor = currentFrameIndex / (currentFrameIndex + 200f);
        return 1.0f + 0.7f / circlePerSec - 0.9f * decFactor;
    }



    ///////

    float getMinCircleNum() {
        float minCircleNum = (float)Math.sqrt(currentFrameIndex + 100f) / 5f - 1;
        if(minCircleNum > 2.3f) minCircleNum = 2.3f;
        return minCircleNum;
    }

    float getMaxCircleNum() {
        float maxCirclePerNum = (float)Math.sqrt(currentFrameIndex + 20.25f) / 4.5f + 1;
        if(maxCirclePerNum > 5.6f) maxCirclePerNum = 5.6f;
        return maxCirclePerNum;
    }

    int getRandomCircleNum() {
        float minCircleNum = getMinCircleNum();
        float maxCircleNum = getMaxCircleNum();
        return (int)Math.floor(Math.random() * (maxCircleNum - minCircleNum) + minCircleNum);
    }

    ///////

    float currentHue;

    public Color getNextCircleColor() {
        final Color circleColor = HSVRGB.hsvToRgb(currentHue, 0.5f, 0.95f);
        currentHue += 0.618033988749895f;
        if(currentHue >= 1) currentHue -= 1;
        return circleColor;
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        FrameGroup frameGroup = parent.frameGroup;

        remainingTime -= dt;
        // To few frames -> Add more.
        float maxRemainingTIme = getMaxCircleNum() * 0.03f + 0.07f;
        if (frameGroup.getAliveFrameCount()<= 3 && remainingTime >= maxRemainingTIme)
            remainingTime = maxRemainingTIme;

        while (remainingTime <= 0) {
            currentFrameIndex++;

            int circleNum = getRandomCircleNum();

            HitFrame frame = new HitFrame(parent, circleNum, getNextCircleColor());
            frameGroup.addFrame(frame);
            frame.act(-remainingTime);

            remainingTime += nextRemainingTime();
        }
    }
}
