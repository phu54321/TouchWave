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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.touchwave.GameLogger;
import com.trgk.touchwave.tgengine.TGPopupScene;
import com.trgk.touchwave.tgengine.TransitScene;
import com.trgk.touchwave.tgengine.ui.TGButton;
import com.trgk.touchwave.tgengine.ui.TGText;
import com.trgk.touchwave.tgengine.ui.TGWindow;
import com.trgk.touchwave.utils.ScreenFillingGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AboutScene extends TGPopupScene {
    AboutScene(MenuScene parent) {
        super(parent, new Stage(new ScreenViewport()), new Color(0, 0, 0, 0.8f));

        ScreenFillingGroup group = new ScreenFillingGroup(150, 100);
        this.getStage().addActor(group);

        group.addActor(buildWindow());
    }


    /**
     * Helper for adding link
     */
    void addLink(TGWindow wnd, float x, float y, String str, final String url) {
        TGText text = new TGText(str, 2.2f, x, y, Color.BLUE);
        text.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.net.openURI(url);
            }
        });
        wnd.addActor(text);
    }

    /**
     * Helper for adding link
     */
    void addText(TGWindow wnd, float x, float y, String str) {
        TGText text = new TGText(str, 2.2f, x, y, Color.BLACK);
        wnd.addActor(text);
    }

    /**
     * Generate window
     */
    public TGWindow buildWindow() {
        TGWindow wnd = new TGWindow(46);
        wnd.setPosition(75, 50, Align.center);
        wnd.setOrigin(Align.center);
        wnd.setScale(1.6f);


        wnd.addActor(new TGText("TouchWave 라이센스", 4, 23, 39, Color.BLACK));

        // Texts
        addText(wnd, 23, 33, "이 프로그램은 GNU GPL 3.0을 따릅니다.");
        addLink(wnd, 23, 30, "GNU General Public License 3.0", "https://www.gnu.org/licenses/gpl-3.0.html");
        addLink(wnd, 23, 27, "[프로그램 소스 보기]", "https://github.com/phu54321/TouchWave/");


        addText(wnd, 23, 22, "이 프로그램은 다음 프로그램을 사용했습니다.");
        addLink(wnd, 23, 19, "Libgdx (아파치 2.0 라이센스)", "https://github.com/libgdx/libgdx");
        addLink(wnd, 23, 16, "gdx-facebook (아파치 2.0 라이센스)", "https://github.com/TomGrill/gdx-facebook");
        addLink(wnd, 23, 13, "네이버 나눔바른고딕 폰트", "https://help.naver.com/support/contents/contents.nhn?serviceNo=1074&categoryNo=3497");


        final AboutScene this2 = this;
        wnd.addActor((
                new TGButton("돌아가기", 7f, 23, 7, new Color(.40f, .67f, .93f, 1)) {
                    @Override
                    public void clicked() {
                        gotoParent();
                    }
                }
        ));

        return wnd;
    }
}
