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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class RankingScene extends com.trgk.touchwave.tgengine.TGScene {

    public RankingScene() {
        super(new Stage(new ScreenViewport()));
        com.trgk.touchwave.utils.ScreenFillingGroup group = new com.trgk.touchwave.utils.ScreenFillingGroup(150, 100);
        this.getStage().addActor(group);
        group.addActor(buildWindow());
    }

    com.trgk.touchwave.tgengine.ui.TGWindow rankingWindow;

    /**
     * Build basic ui
     * @return UI
     */
    public com.trgk.touchwave.tgengine.ui.TGWindow buildWindow() {
        final RankingScene this2 = this;

        com.trgk.touchwave.tgengine.ui.TGWindow wnd = new com.trgk.touchwave.tgengine.ui.TGWindow(90);
        wnd.setPosition(75, 50, Align.center);

        wnd.addActor(new com.trgk.touchwave.tgengine.ui.TGText("Rankings", 7, 45, 81, Color.BLACK));

        rankingWindow = new com.trgk.touchwave.tgengine.ui.TGWindow(62);
        rankingWindow.setPosition(45, 45, Align.center);
        wnd.addActor(rankingWindow);

        wnd.addActor((
                new com.trgk.touchwave.tgengine.ui.TGButton("돌아가기", 10f, 45, 7, new Color(.40f, .67f, .93f, 1)) {
                    @Override
                    public void clicked() {
                        getSceneManager().setCurrentScene(new com.trgk.touchwave.tgengine.TransitScene(this2, new MenuScene(), 0.2f));
                    }
                }
        ));

        return wnd;
    }


    // ---

    public static class RankingEntry extends Group {
        public RankingEntry(int rank, String fbNickname, int maxScore, Color color, int pos) {
            this.addActor(new com.trgk.touchwave.tgengine.ui.TGText(Integer.toString(rank), 2.8f, 3, 1.5f, color));
            this.addActor(new com.trgk.touchwave.tgengine.ui.TGText(fbNickname, 2.8f, 23, 1.5f, color));
            this.addActor(new com.trgk.touchwave.tgengine.ui.TGText(Integer.toString(maxScore), 2.8f, 50, 1.5f, color));
            this.setSize(60, 3);
            this.setOrigin(Align.topLeft);
            this.setPosition(1, 61 - 3 * pos, Align.topLeft);
        }
    }


    boolean inited  = false;

    @Override
    public void act(float dt) {
        super.act(dt);

        if(!inited) {
            getSceneManager().setCurrentScene(new RankingLoadScene(this));
            inited = true;
        }
    }
}
