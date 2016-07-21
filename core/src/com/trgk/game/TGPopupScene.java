package com.trgk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.trgk.game.utils.PrimitiveImage;

/**
 * Created by trgk on 16. 7. 19.
 */
public class TGPopupScene extends TGScene {
    final TGScene parent;
    Image background;


    public void gotoParent() {
        background.clearActions();
        background.addAction(Actions.sequence(
                Actions.alpha(0f, 0.3f),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        TGScene parent = getParentScene();
                        parent.getStage().getRoot().setTouchable(Touchable.enabled);
                        getSceneManager().setCurrentScene(parent);
                        return true;
                    }
                }
        ));
    }

    public TGPopupScene(TGScene parent, Stage stage, Color backgroundColor) {
        super(stage);

        // Set parent
        parent.ref();
        parent.getStage().getRoot().setTouchable(Touchable.disabled);
        this.parent = parent;

        // Add background image
        background = PrimitiveImage.rectImage(0, 0, 1, 1, Color.BLACK);
        background.setColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 0);
        getStage().addActor(background);
        background.addAction(Actions.alpha(backgroundColor.a, 0.3f));
    }

    public TGScene getParentScene() {
        return parent;
    }

    // ------

    @Override
    protected void onZeroRef() {
        parent.unref();
        super.onZeroRef();
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        parent.act(dt);
    }

    @Override
    public void draw() {
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        parent.draw();
        super.draw();
    }
}
