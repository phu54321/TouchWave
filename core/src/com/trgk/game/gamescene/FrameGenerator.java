/*
 * Copyright 2015 Hyun Woo Park
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module. An independent module is a module which is not derived from or
 * based on this library. If you modify this library, you may extend this
 * exception to your version of the library, but you are not obliged to
 * do so. If you do not wish to do so, delete this exception statement
 * from your version.
 */

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
