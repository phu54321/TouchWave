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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.touchwave.tgengine.TGPopupScene;
import com.trgk.touchwave.gamescene.GameScene;
import com.trgk.touchwave.tgengine.TransitScene;
import com.trgk.touchwave.tgengine.ui.TGButton;
import com.trgk.touchwave.tgengine.ui.TGText;
import com.trgk.touchwave.tgengine.ui.TGWindow;
import com.trgk.touchwave.utils.ScreenFillingGroup;

import java.nio.ByteBuffer;
import java.util.Locale;

public class GameoverScene extends TGPopupScene {
    public GameoverScene(final GameScene gameScene) {
        super(gameScene, new Stage(new ScreenViewport()), new Color(0.8f, 0.8f, 0.8f, 0.4f));

        com.trgk.touchwave.utils.ScreenFillingGroup group = new ScreenFillingGroup(150, 100);
        this.getStage().addActor(group);

        group.addActor(buildWindow());
        group.setColor(1, 1, 1, 0);
        group.addAction(Actions.sequence(
                Actions.fadeIn(.5f)
        ));
    }

    /**
     * Generate window
     */
    public TGWindow buildWindow() {
        final GameScene gameScene = (GameScene)getParentScene();

        com.trgk.touchwave.tgengine.ui.TGWindow wnd = new TGWindow(46);
        wnd.setPosition(75, 50, Align.center);
        wnd.setOrigin(Align.center);
        wnd.setScale(1.6f);

        final GameoverScene this2 = this;

        wnd.addActor(TGWindow.showAfter(
                new TGText("게임 오버", 7, 23, 39, new Color(0, 0, 0, 1)),
                0.3f
        ));

        String scoreText = String.format(Locale.KOREAN, "점수 : %d", gameScene.getScore());
        wnd.addActor(TGWindow.showAfter(
                new TGText(scoreText, 4, 23, 26f, new Color(0, 0, 0, 1)),
                0.6f
        ));

        wnd.addActor(TGWindow.showAfter(
                new TGButton("재도전", 8f, 23, 16, new Color(.40f, .67f, .93f, 1)) {
                    @Override
                    public void clicked() {
                        this2.getSceneManager().setCurrentScene(new TransitScene(this2, new GameScene(), 0.2f));
                    }
                },
                0.9f
        ));

        wnd.addActor(TGWindow.showAfter(
                new TGButton("돌아가기", 8f, 23, 7, new Color(.40f, .67f, .93f, 1)) {
                    @Override
                    public void clicked() {
                        this2.getSceneManager().setCurrentScene(new TransitScene(this2, new MenuScene(), 0.2f));
                    }
                },
                1.2f
        ));

        return wnd;
    }
}
