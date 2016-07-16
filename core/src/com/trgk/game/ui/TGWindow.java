package com.trgk.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.trgk.game.utils.TGResources;


public class TGWindow extends Group {
    final Image background;
    public TGWindow(float size) {
        background = new Image(TGResources.getInstance().getAtlasSprite("roundbox"));
        background.setScale(size / 256f);
        this.addActor(background);
        this.setSize(
                size * background.getWidth() / 256f,
                size * background.getHeight() / 256f
        );
    }

    public static <T extends Actor> T showAfter(T actor, float time) {
        actor.setVisible(false);
        actor.setTouchable(Touchable.disabled);
        actor.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.delay(time),
                        Actions.visible(true),
                        Actions.fadeIn(0.5f),
                        Actions.touchable(Touchable.enabled)
                )
        );
        return actor;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.setColor(getColor());
        super.draw(batch, parentAlpha);
    }
}
