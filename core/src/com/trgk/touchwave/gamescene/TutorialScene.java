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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.touchwave.tgengine.TGPopupScene;
import com.trgk.touchwave.tgengine.ui.TGButton;
import com.trgk.touchwave.tgengine.ui.TGText;
import com.trgk.touchwave.tgengine.ui.TGWindow;
import com.trgk.touchwave.utils.ScreenFillingGroup;


public class TutorialScene extends TGPopupScene {
    public TutorialScene(GameScene gameScene) {
        super(gameScene, new Stage(new ScreenViewport()), new Color(0.8f, 0.8f, 0.8f, 0.4f));
        setModal(true);

        ScreenFillingGroup group = new ScreenFillingGroup(150, 100);
        this.getStage().addActor(group);

        group.addActor(buildWindow());
        group.setColor(1, 1, 1, 0);
        group.addAction(Actions.sequence(
                Actions.fadeIn(.5f)
        ));
    }

    /**
     * Helper for adding link
     */
    void addText(TGWindow wnd, float x, float y, String str) {
        TGText text = new TGText(str, 2.3f, x, y, Color.BLACK);
        wnd.addActor(text);
    }

    /**
     * Generate window
     */
    public TGWindow buildWindow() {
        TGWindow wnd = new TGWindow(40);
        wnd.setPosition(75, 50, Align.center);
        wnd.setOrigin(Align.center);
        wnd.setScale(1.6f);

        wnd.addActor(new TGText("플레이 방법", 4, 20, 35, Color.BLACK));

        // Texts
        addText(wnd, 20, 25, "같은 색의 원을 동시에 터치!");
        addText(wnd, 20, 19, "원이 많으면 빠르게 차례대로 터치!");

        wnd.addActor((
                new TGButton("돌아가기", 7f, 20, 8, new Color(.40f, .67f, .93f, 1)) {
                    @Override
                    public void clicked() {
                        gotoParent();
                    }
                }
        ));

        return wnd;
    }
}
