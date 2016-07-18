package com.trgk.game.menuscene;

import com.badlogic.gdx.graphics.Color;
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

public class GameoverScene extends TGScene {
    final GameScene gameScene;
    public GameoverScene(GameScene gameScene) {
        super(new Stage(new ScreenViewport()));
        this.gameScene = gameScene;
        gameScene.ref();

        ScreenFillingGroup g = new ScreenFillingGroup(150, 100);
        this.getStage().addActor(g);

        Image background = PrimitiveImage.rectImage(0, 0, 300, 100, new Color(.8f, .8f, .8f, 0.3f));
        background.setPosition(75, 50, Align.center);
        g.addActor(background);

        TGWindow wnd = new TGWindow(46);
        wnd.setPosition(75, 50, Align.center);
        g.addActor(wnd);
        wnd.setOrigin(Align.center);
        wnd.setScale(1.6f);

        final GameoverScene this2 = this;

        wnd.addActor(TGWindow.showAfter(
                new TGText("Game over", 7, 23, 39, new Color(0, 0, 0, 1)),
                0.5f
        ));

        String scoreText = String.format(Locale.ENGLISH, "Score : %d", gameScene.getScore());
        wnd.addActor(TGWindow.showAfter(
                new TGText(scoreText, 5, 23, 28, new Color(0, 0, 0, 1)),
                1.0f
        ));

        wnd.addActor(TGWindow.showAfter(
                new TGButton("Share", 8f, 23, 16, new Color(.40f, .67f, .93f, 1)) {
                    @Override
                    public void clicked() {
                        this2.getSceneManager().setCurrentScene(new GameScene());
                    }
                },
                1.5f
        ));

        wnd.addActor(TGWindow.showAfter(
                new TGButton("Retry", 8f, 23, 7, new Color(.40f, .67f, .93f, 1)) {
                    @Override
                    public void clicked() {
                        this2.getSceneManager().setCurrentScene(new GameScene());
                    }
                },
                2.0f
        ));


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
