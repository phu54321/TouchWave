package com.trgk.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.trgk.game.utils.TGResources;

public class TGButton extends Group {
    final Image background;
    boolean clicked = false;
    public TGButton(String caption, float size) {
        background = new Image(TGResources.getInstance().getAtlasSprite("roundbutton"));
        background.setScale(size / 64f);
        this.addActor(background);
        this.setSize(
                size * background.getWidth() / 64f,
                size * background.getHeight() / 64f
        );

        TGText text = new TGText(caption, size / 2f, getWidth() / 2, getHeight() / 2, new Color(0, 0, 0, 1));
        this.addActor(text);

        final TGButton this2 = this;
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                this2.clicked();
            }
        });
    }

    public TGButton(String caption, float size, float x, float y, Color color) {
        this(caption, size);
        this.setPosition(x, y, Align.center);
        this.setColor(color);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.setColor(getColor());
        super.draw(batch, parentAlpha);
    }

    public void clicked() {}
}
