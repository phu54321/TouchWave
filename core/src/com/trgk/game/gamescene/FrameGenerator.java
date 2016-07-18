package com.trgk.game.gamescene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.trgk.game.utils.HSVRGB;

import java.util.Locale;

class FrameGenerator extends Actor {
    GameScene parent;
    public FrameGenerator(GameScene parent) {
        this.parent = parent;
        this.currentHue = (float)Math.random();
    }

    float remainingTime = 0;
    int currentFrameIndex = 0;


    float nextRemainingTime() {
        float circlePerSec = (float)Math.sqrt(currentFrameIndex + 36) / 6f;
        float decFactor = currentFrameIndex / (currentFrameIndex + 200f);
        return 1.0f + 0.7f / circlePerSec - 0.9f * decFactor;
    }



    ///////

    float getMinCircleNum() {
        return 1.5f;
    }

    float getMaxCircleNum() {
        float maxCirclePerNum = 1.8f + currentFrameIndex / 70f;
        if(maxCirclePerNum > 8.6f) maxCirclePerNum = 8.6f;
        return maxCirclePerNum;
    }

    int getRandomCircleNum() {
        float minCircleNum = getMinCircleNum();
        float maxCircleNum = getMaxCircleNum();
        return (int)Math.floor(Math.random() * (maxCircleNum - minCircleNum) + minCircleNum);
    }

    public static void main(String[] args) {
        FrameGenerator generator = new FrameGenerator(null);
        int score = 0;
        for(int i = 0 ; i < 300 ; i++) {
            generator.currentFrameIndex = i;
            int cnum = generator.getRandomCircleNum();
            score += cnum * cnum;
            System.out.println(String.format(Locale.ENGLISH,
                    "%4d %5d %.2f %.2f", i, score, generator.getMaxCircleNum(), generator.getMinCircleNum()
            ));
        }
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
