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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Locale;

public class FBShareResult extends com.trgk.touchwave.tgengine.TGPopupScene {
    com.trgk.touchwave.gamescene.GameScene gameScene;
    int currentState = 0;
    public FBShareResult(com.trgk.touchwave.tgengine.TGScene parent, com.trgk.touchwave.gamescene.GameScene gameScene) {
        super(parent, new Stage(new ScreenViewport()), new Color(0, 0, 0, 0.8f));
        this.gameScene = gameScene;
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        com.trgk.touchwave.facebook.FBService fb = com.trgk.touchwave.facebook.FBService.getInstance();

        // State 0: issue fb login
        if(currentState == 0) {
            if (!com.trgk.touchwave.facebook.FBService.getInstance().isLogonPublish()) {
                com.trgk.touchwave.facebook.FBService.getInstance().loginPublish();
                currentState = 1;
            } else currentState = 2;
        }

        // State 1: wait for login
        if(currentState == 1) {
            if(!fb.isBusy()) {
                com.trgk.touchwave.facebook.FBService.Result result = fb.getLastActionResult();
                if(result == com.trgk.touchwave.facebook.FBService.Result.SUCCESS) currentState = 2;
                else currentState = 4;
            }
        }

        // Stage 2: post to wall
        if(currentState == 2) {
            String username = fb.username;
            String shareString = String.format(Locale.ENGLISH,
                    "%s님이 TouchWave에서 %d점을 달성했습니다!", username, gameScene.getScore());
            fb.postPhotoToUserWall("TouchWave", shareString, null, Gdx.files.local("screenshot.png"));
            currentState = 3;
        }

        // Stage 3: Wait for wall posting
        if(currentState == 3) {
            if(!fb.isBusy()) {
                currentState = 4;
            }
        }

        // Stage 4: End
        if(currentState == 4) {
            fb.logout();
            gotoParent();
            currentState = 5;
        }
    }
}
