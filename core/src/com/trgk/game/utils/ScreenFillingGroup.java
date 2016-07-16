package com.trgk.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;


/**
 * Screen filling group. Centered & Fills screen with aspect ratio preserved.
 */
public class ScreenFillingGroup extends Group {
    float logicalWorldWidth, logicalWorldHeight;
    float screenWidth, screenHeight;
    float groupScale;
    float logicalScreenWidth, logicalScreenHeight;

    /**
     * Fill screen with aspect ratio preserved..
     * @param logicalWorldWidth Logical width of the group.
     * @param logicalWorldHeight Logical height of the group.
     */
    public ScreenFillingGroup(float logicalWorldWidth, float logicalWorldHeight) {
        screenWidth = logicalWorldWidth;
        screenHeight = logicalWorldHeight;
        groupScale = 1;
        setWorldDimension(logicalWorldWidth, logicalWorldHeight);
        updateScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * Reset logical size of the group.
     * @param logicalWorldWidth New logical width of the group.
     * @param logicalWorldHeight New logical height of the group.
     */
    public void setWorldDimension(float logicalWorldWidth, float logicalWorldHeight) {
        this.logicalWorldWidth = logicalWorldWidth;
        this.logicalWorldHeight = logicalWorldHeight;
        this.logicalScreenWidth = screenWidth / groupScale;
        this.logicalScreenHeight = screenHeight / groupScale;
    }

    /**
     * Update scaling based on real screen size.
     * @param screenWidth Real screen width.
     * @param screenHeight Real screen height.
     */
    void updateScreenSize(int screenWidth, int screenHeight) {
        float scaleNeededX = screenWidth / logicalWorldWidth;
        float scaleNeededY = screenHeight / logicalWorldHeight;
        float groupScale = Math.min(scaleNeededX, scaleNeededY);

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.groupScale = groupScale;
        this.logicalScreenWidth = screenWidth / groupScale;
        this.logicalScreenHeight = screenHeight / groupScale;

        this.setSize(logicalWorldWidth, logicalWorldHeight);
        this.setScale(groupScale);
        this.setPosition((screenWidth - logicalWorldWidth * groupScale) / 2, (screenHeight - logicalWorldHeight * groupScale) / 2);
    }

    //////////////////////////////

    public float getLogicalScreenWidth() {
        return logicalScreenWidth;
    }

    public float getLogicalScreenHeight() {
        return logicalScreenHeight;
    }

    public Vector2 getlogicalScreenTopLeft() {
        return new Vector2(-logicalScreenWidth / 2, logicalScreenHeight / 2);
    }

    public Vector2 getlogicalScreenTopRight() {
        return new Vector2(logicalScreenWidth / 2, logicalScreenHeight / 2);
    }

    public Vector2 getlogicalScreenBottomLeft() {
        return new Vector2(-logicalScreenWidth / 2, -logicalScreenHeight / 2);
    }

    public Vector2 getlogicalScreenBottomRight() {
        return new Vector2(logicalScreenWidth / 2, -logicalScreenHeight / 2);
    }

    //////////////////////////////

    @Override
    public void draw(Batch batch, float parentAlpha) {
        final int screenWidth = Gdx.graphics.getWidth();
        final int screenHeight = Gdx.graphics.getHeight();
        updateScreenSize(screenWidth, screenHeight);

        super.draw(batch, parentAlpha);
    }
}
