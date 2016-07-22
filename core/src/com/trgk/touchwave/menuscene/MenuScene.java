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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.touchwave.gamescene.GameScene;
import com.trgk.touchwave.tgengine.TGScene;
import com.trgk.touchwave.tgengine.TransitScene;
import com.trgk.touchwave.tgengine.ui.TGButton;
import com.trgk.touchwave.tgengine.ui.TGText;

public class MenuScene extends TGScene {
    public MenuScene() {
        super(new Stage(new ScreenViewport()));

        com.trgk.touchwave.utils.ScreenFillingGroup group = new com.trgk.touchwave.utils.ScreenFillingGroup(150, 100);
        this.getStage().addActor(group);

        group.addActor(buildWindow());
    }

    /**
     * Generate window
     */
    public com.trgk.touchwave.tgengine.ui.TGWindow buildWindow() {
        com.trgk.touchwave.tgengine.ui.TGWindow wnd = new com.trgk.touchwave.tgengine.ui.TGWindow(50);
        wnd.setPosition(75, 50, Align.center);
        wnd.setOrigin(Align.center);
        wnd.setScale(1.6f);


        wnd.addActor(new TGText("TouchWave", 7, 25, 41, Color.BLACK));

        final MenuScene this2 = this;

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
}
