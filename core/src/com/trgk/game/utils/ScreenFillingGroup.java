package com.trgk.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;


/**
 * Created by 박현우 on 2015-12-30.
 */
public class ScreenFillingGroup extends Group {
    float worldWidth, worldHeight;
    float screenWidth, screenHeight;
    float groupScale;
    float convertedScreenWidth, convertedScreenHeight;

    public ScreenFillingGroup(float worldWidth, float worldHeight) {
        screenWidth = worldWidth;
        screenHeight = worldHeight;
        groupScale = 1;
        setWorldDimension(worldWidth, worldHeight);
    }

    public void setWorldDimension(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.convertedScreenWidth = screenWidth / groupScale;
        this.convertedScreenHeight = screenHeight / groupScale;
    }

    public float getConvertedScreenWidth() {
        return convertedScreenWidth;
    }

    public float getConvertedScreenHeight() {
        return convertedScreenHeight;
    }

    void updateScreenSize(int screenWidth, int screenHeight) {
        float scaleNeededX = screenWidth / worldWidth;
        float scaleNeededY = screenHeight / worldHeight;
        float groupScale = Math.min(scaleNeededX, scaleNeededY);

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.groupScale = groupScale;
        this.convertedScreenWidth = screenWidth / groupScale;
        this.convertedScreenHeight = screenHeight / groupScale;

        this.setSize(worldWidth, worldHeight);
        this.setScale(groupScale);
        this.setPosition((screenWidth - worldWidth * groupScale) / 2, (screenHeight - worldHeight * groupScale) / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        final int screenWidth = Gdx.graphics.getWidth();
        final int screenHeight = Gdx.graphics.getHeight();
        updateScreenSize(screenWidth, screenHeight);

        super.draw(batch, parentAlpha);
    }
}
