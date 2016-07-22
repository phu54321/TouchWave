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
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.touchwave.GameLogger;
import com.trgk.touchwave.tgengine.TransitScene;
import com.trgk.touchwave.tgengine.ui.TGButton;
import com.trgk.touchwave.tgengine.ui.TGText;
import com.trgk.touchwave.tgengine.ui.TGWindow;
import com.trgk.touchwave.utils.ScreenFillingGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StatsScene extends com.trgk.touchwave.tgengine.TGScene {
    public StatsScene() {
        super(new Stage(new ScreenViewport()));

        ScreenFillingGroup group = new ScreenFillingGroup(150, 100);
        this.getStage().addActor(group);

        group.addActor(buildWindow());
    }


    public String encodePlaytime(float playTime) {
        StringBuilder builder = new StringBuilder();

        // 일
        if(playTime >= 86400) {
            int days = (int)Math.floor(playTime / 86400f);
            builder.append(String.format(Locale.KOREAN, "%d일 ", days));
            playTime -= days * 86400;
        }

        // 시간
        if(playTime >= 3600) {
            int hours = (int)Math.floor(playTime / 3600);
            builder.append(String.format(Locale.KOREAN, "%d시간 ", hours));
            playTime -= hours * 3600;
        }

        // 분
        if(playTime >= 60) {
            int minutes = (int)Math.floor(playTime / 60);
            builder.append(String.format(Locale.KOREAN, "%d분 ", minutes));
            playTime -= minutes * 60;
        }

        builder.append(String.format(Locale.KOREAN, "%d초", (int)Math.floor(playTime)));
        return builder.toString();
    }

    /**
     * Generate window
     */
    public TGWindow buildWindow() {
        com.trgk.touchwave.tgengine.ui.TGWindow wnd = new TGWindow(46);
        TGText text;
        GameLogger inst = GameLogger.getInstance();

        wnd.setPosition(75, 50, Align.center);
        wnd.setOrigin(Align.center);
        wnd.setScale(1.6f);


        wnd.addActor(new TGText("게임 기록", 4, 23, 39, Color.BLACK));

        // Texts
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.KOREAN);

        text = new TGText(
                String.format(Locale.KOREAN, "최초 실행 시각 : %s", dayTime.format(new Date(inst.installTime))),
                2.2f, 23, 28, Color.BLACK);
        text.setPosition(4, 31, Align.left);
        text.setOrigin(Align.left);
        wnd.addActor(text);

        text = new TGText(
                String.format(Locale.KOREAN, "총 플레이 시간 : %s", encodePlaytime(inst.totalPlayTime / 1000f)),
                2.2f, 23, 28, Color.BLACK);
        text.setPosition(4, 26f, Align.left);
        text.setOrigin(Align.left);
        wnd.addActor(text);

        text = new TGText(
                String.format(Locale.KOREAN, "최고 획득 점수 : %d", inst.maxScore),
                2.2f, 23, 28, Color.BLACK);
        text.setPosition(4, 21, Align.left);
        text.setOrigin(Align.left);
        wnd.addActor(text);

        text = new TGText(
                String.format("마지막 플레이 시각 : %s",
                        (inst.lastPlayTime == -1) ? "플레이 기록 없음"
                        : dayTime.format(new Date(inst.lastPlayTime))),
                2.2f, 23, 28, Color.BLACK);
        text.setPosition(4, 16, Align.left);
        text.setOrigin(Align.left);
        wnd.addActor(text);


        final StatsScene this2 = this;
        wnd.addActor((
                new TGButton("돌아가기", 5f, 23, 7, new Color(.40f, .67f, .93f, 1)) {
                    @Override
                    public void clicked() {
                        getSceneManager().setCurrentScene(new TransitScene(this2, new MenuScene(), 0.2f));
                    }
                }
        ));

        return wnd;
    }
}
