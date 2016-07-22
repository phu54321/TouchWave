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

package com.trgk.touchwave.gamescene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.touchwave.GameLogger;
import com.trgk.touchwave.menuscene.GameoverScene;
import com.trgk.touchwave.tgengine.ui.TGText;
import com.trgk.touchwave.utils.ScreenFillingGroup;

import java.util.Locale;


public class GameScene extends com.trgk.touchwave.tgengine.TGScene {
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

        scoreText = new TGText("점수 : 0        시간 : 0.00s", 5, 0, 0, Color.BLACK);
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

        GameLogger.getInstance().updatePlay((long)(elapsedTime * 1000), getScore());
        this.getSceneManager().setCurrentScene(new GameoverScene(this));
        this.logicGroup.remove();
        this.frameGroup.setTouchable(Touchable.disabled);
    }

    ///////

    boolean a = false;
    @Override
    public void act(float dt) {
        // Open tutorial on first play
        if(!a || GameLogger.getInstance().lastPlayTime == -1) {
            a = true;
            getSceneManager().setCurrentScene(new TutorialScene(this));
        }

        super.act(dt);
        if(gameCompleted) return;

        elapsedTime += dt;
        String scoreString = String.format(Locale.KOREAN, "점수 : %d    시간 : %.2f", getScore(), elapsedTime);
        scoreText.setText(scoreString);
    }


    @Override
    public void draw() {
        Vector2 topLeft = uiGroup.getlogicalScreenTopLeft();
        scoreText.setPosition(topLeft.x + 2, topLeft.y - 2, Align.topLeft);
        super.draw();
    }
}
