package com.trgk.game.menuscene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.game.TGPopupScene;
import com.trgk.game.gamescene.GameScene;
import com.trgk.game.ui.TGButton;
import com.trgk.game.ui.TGWindow;
import com.trgk.game.utils.ScreenFillingGroup;
import com.trgk.game.ui.TGText;

import java.nio.ByteBuffer;
import java.util.Locale;

public class GameoverScene extends TGPopupScene {
    public GameoverScene(final GameScene gameScene) {
        super(gameScene, new Stage(new ScreenViewport()), new Color(0.8f, 0.8f, 0.8f, 0.4f));

        takeScreenshot();

        ScreenFillingGroup group = new ScreenFillingGroup(150, 100);
        this.getStage().addActor(group);

        group.addActor(buildWindow());
        group.setColor(1, 1, 1, 0);
        group.addAction(Actions.sequence(
                Actions.fadeIn(.5f)
        ));
    }


    public void takeScreenshot() {
        final int w = Gdx.graphics.getWidth();
        final int h = Gdx.graphics.getHeight();
        Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, w, h);

        // Flip the pixmap upside down
        ByteBuffer pixels = pixmap.getPixels();
        int numBytes = w * h * 4;
        byte[] lines = new byte[numBytes];
        int numBytesPerLine = w * 4;
        for (int i = 0; i < h; i++) {
            pixels.position((h - i - 1) * numBytesPerLine);
            pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
        }
        pixels.clear();

        // Force set  alpha channel to 255
        for(int i = 3 ; i < numBytes ; i += 4) {
            lines[i] = (byte)255;
        }

        pixels.put(lines);
        PixmapIO.writePNG(Gdx.files.local("screenshot.png"), pixmap);
    }

    /**
     * Generate window
     */
    public TGWindow buildWindow() {
        final GameScene gameScene = (GameScene)getParentScene();

        TGWindow wnd = new TGWindow(46);
        wnd.setPosition(75, 50, Align.center);
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
                        this2.getSceneManager().setCurrentScene(new FBShareResult(this2, gameScene));
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

        return wnd;
    }
}
