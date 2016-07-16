package com.trgk.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.setColor(getColor());
        super.draw(batch, parentAlpha);
    }
}
