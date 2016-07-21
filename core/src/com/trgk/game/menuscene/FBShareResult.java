package com.trgk.game.menuscene;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.game.TGPopupScene;
import com.trgk.game.TGScene;
import com.trgk.game.facebook.FBService;
import com.trgk.game.gamescene.GameScene;
import com.trgk.game.utils.PrimitiveImage;

public class FBShareResult extends TGPopupScene {
    GameScene gameScene;
    int currentState = 0;
    public FBShareResult(TGScene parent, GameScene gameScene) {
        super(parent, new Stage(new ScreenViewport()), new Color(0, 0, 0, 0.8f));
        this.gameScene = gameScene;
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        FBService fb = FBService.getInstance();

        // State 0: issue fb login
        if(currentState == 0) {
            if (!FBService.getInstance().isLogonPublish()) {
                FBService.getInstance().loginPublish();
                currentState = 1;
            } else currentState = 2;
        }

        // State 1: wait for login
        if(currentState == 1) {
            if(!fb.isBusy()) {
                FBService.Result result = fb.getLastActionResult();
                if(result == FBService.Result.SUCCESS) currentState = 2;
                else currentState = 4;
            }
        }

        // Stage 2: post to wall
        if(currentState == 2) {
            fb.postToUserWall("Test", "Test", "http://www.naver.com", Gdx.files.local("screenshot.png"));
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
        }
    }
}
