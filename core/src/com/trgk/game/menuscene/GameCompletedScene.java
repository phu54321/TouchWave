package com.trgk.game.menuscene;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.game.TGScene;
import com.trgk.game.gamescene.GameScene;
import com.trgk.game.ui.TGButton;
import com.trgk.game.ui.TGWindow;
import com.trgk.game.utils.PrimitiveImage;
import com.trgk.game.utils.ScreenFillingGroup;
import com.trgk.game.ui.TGText;

import java.util.Locale;

public class GameCompletedScene extends TGScene {
    final GameScene gameScene;
    public GameCompletedScene(GameScene gameScene) {
        super(new Stage(new ScreenViewport()));
        this.gameScene = gameScene;
        gameScene.ref();

        ScreenFillingGroup g = new ScreenFillingGroup(150, 100);
        this.getStage().addActor(g);

        Image background = PrimitiveImage.rectImage(0, 0, 300, 100, new Color(.8f, .8f, .8f, 0.3f));
        background.setPosition(75, 50, Align.center);
        g.addActor(background);

        TGWindow wnd = new TGWindow(50);
        wnd.setPosition(75, 50, Align.center);
        g.addActor(wnd);

        final GameCompletedScene this2 = this;

        wnd.addActor(new TGText("Game over", 7, 25, 40, new Color(0, 0, 0, 1)));
        String scoreText = String.format(Locale.ENGLISH, "Score : %d", gameScene.getScore());
        wnd.addActor(new TGText(scoreText, 5, 25, 25, new Color(0, 0, 0, 1)));

        wnd.addActor(new TGButton("Retry", 4.5f, 13, 10, new Color(.8f, .8f, .8f, 1)) {
            @Override
            public void clicked() {
                this2.getSceneManager().setCurrentScene(new GameScene());
            }
        });

        wnd.addActor(new TGButton("Quit", 4.5f, 37, 10, new Color(.8f, .8f, .8f, 1)) {
            @Override
            public void clicked() {
                this2.getSceneManager().setCurrentScene(null);
            }
        });


        g.setColor(1, 1, 1, 0);
        g.addAction(Actions.sequence(
                Actions.fadeIn(.5f)
        ));
    }

    @Override
    protected void onZeroRef() {
        gameScene.unref();
        super.onZeroRef();
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        gameScene.act(dt);
    }

    @Override
    public void draw() {
        gameScene.draw();
        super.draw();
    }
}
