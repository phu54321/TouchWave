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

package com.trgk.touchwave.menuscene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.touchwave.gamescene.GameScene;
import com.trgk.touchwave.tgengine.TGResources;
import com.trgk.touchwave.tgengine.TGScene;
import com.trgk.touchwave.tgengine.TransitScene;
import com.trgk.touchwave.tgengine.ui.TGButton;
import com.trgk.touchwave.tgengine.ui.TGText;
import com.trgk.touchwave.tgengine.ui.TGWindow;
import com.trgk.touchwave.utils.ScreenFillingGroup;

public class MenuScene extends TGScene {
    final static String versionStr = "TouchWave v1.4";

    ScreenFillingGroup uiGroup;
    Image pauseButton;
    TGText versionString;

    public MenuScene() {
        super(new Stage(new ScreenViewport()));

        uiGroup = new ScreenFillingGroup(150, 100);
        this.getStage().addActor(uiGroup);

        // Window
        uiGroup.addActor(buildWindow());

        // Pause
        pauseButton = new Image(TGResources.getInstance().getAtlasSprite("infobutton"));
        pauseButton.setScale(10 / 128f);
        pauseButton.setOrigin(Align.topRight);
        final MenuScene this2 = this;
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getSceneManager().setCurrentScene(new AboutScene(this2));
                super.clicked(event, x, y);
            }
        });
        uiGroup.addActor(pauseButton);

        // Version
        versionString = new TGText(versionStr, 3, 0, 0, Color.BLACK);
        uiGroup.addActor(versionString);
        versionString.setOrigin(Align.topLeft);
    }

    /**
     * Generate window
     */
    public TGWindow buildWindow() {
        com.trgk.touchwave.tgengine.ui.TGWindow wnd = new TGWindow(50);
        wnd.setPosition(75, 50, Align.center);
        wnd.setOrigin(Align.center);
        wnd.setScale(1.6f);


        wnd.addActor(new TGText("TouchWave", 7, 25, 41, Color.BLACK));

        final MenuScene this2 = this;

        Image logo = new Image(TGResources.getInstance().getAtlasSprite("icon"));
        logo.setScale(18 / 256f);
        logo.setOrigin(Align.center);
        logo.setPosition(25, 25, Align.center);
        logo.setColor(1, 1, 1, 0.5f);
        logo.addAction(
                Actions.repeat(RepeatAction.FOREVER, Actions.sequence(
                        Actions.alpha(1, 1),
                        Actions.alpha(0.5f, 1)
                ))
        );
        wnd.addActor(logo);

        wnd.addActor((
                new TGButton("플레이", 4.5f, 25, 12, new Color(.40f, .67f, .93f, 1), true) {
                    @Override
                    public void clicked() {
                        getSceneManager().setCurrentScene(new TransitScene(this2, new GameScene(), 0.2f));
                    }
                }
        ));

        wnd.addActor((
                new TGButton("랭킹 확인", 4.5f, 15, 6, new Color(.40f, .67f, .93f, 1)) {
                    @Override
                    public void clicked() {
                        getSceneManager().setCurrentScene(new TransitScene(this2, new RankingScene(), 0.2f));
                    }
                }
        ));

        wnd.addActor((
                new TGButton("기록 보기", 4.5f, 35, 6, new Color(.40f, .67f, .93f, 1)) {
                    @Override
                    public void clicked() {
                        getSceneManager().setCurrentScene(new TransitScene(this2, new StatsScene(), 0.2f));
                    }
                }
        ));

        return wnd;
    }

    @Override
    public void draw() {
        Vector2 topRight = uiGroup.getlogicalScreenTopRight();
        pauseButton.setPosition(topRight.x - 2, topRight.y - 2, Align.topRight);

        Vector2 topLeft = uiGroup.getlogicalScreenTopLeft();
        versionString.setPosition(topLeft.x + 2, topLeft.y - 2, Align.topLeft);

        super.draw();
    }
}
