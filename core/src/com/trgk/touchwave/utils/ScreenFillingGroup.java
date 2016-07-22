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

package com.trgk.touchwave.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;


/**
 * Screen filling group. Centered & Fills screen with aspect ratio preserved.
 */
public class ScreenFillingGroup extends Group {
    float worldWidth, worldHeight;
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
     * @param worldWidth New logical width of the group.
     * @param worldHeight New logical height of the group.
     */
    public void setWorldDimension(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.logicalScreenWidth = screenWidth / groupScale;
        this.logicalScreenHeight = screenHeight / groupScale;
    }

    /**
     * Update scaling based on real screen size.
     * @param screenWidth Real screen width.
     * @param screenHeight Real screen height.
     */
    void updateScreenSize(int screenWidth, int screenHeight) {
        float scaleNeededX = screenWidth / worldWidth;
        float scaleNeededY = screenHeight / worldHeight;
        float groupScale = Math.min(scaleNeededX, scaleNeededY);

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.groupScale = groupScale;
        this.logicalScreenWidth = screenWidth / groupScale;
        this.logicalScreenHeight = screenHeight / groupScale;

        this.setSize(worldWidth, worldHeight);
        this.setOrigin(Align.center);
        this.setPosition(screenWidth / 2, screenHeight / 2, Align.center);
        this.setScale(groupScale);
    }

    //////////////////////////////

    public float getLogicalScreenWidth() {
        return logicalScreenWidth;
    }

    public float getLogicalScreenHeight() {
        return logicalScreenHeight;
    }

    public Vector2 getlogicalScreenTopLeft() {
        return new Vector2((worldWidth - logicalScreenWidth) / 2, (worldHeight + logicalScreenHeight) / 2);
    }

    public Vector2 getlogicalScreenTopRight() {
        return new Vector2((worldWidth + logicalScreenWidth) / 2, (worldHeight + logicalScreenHeight) / 2);
    }

    public Vector2 getlogicalScreenBottomLeft() {
        return new Vector2((worldWidth - logicalScreenWidth) / 2, (worldHeight - logicalScreenHeight) / 2);
    }

    public Vector2 getlogicalScreenBottomRight() {
        return new Vector2((worldWidth + logicalScreenWidth) / 2, (worldHeight - logicalScreenHeight) / 2);
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
