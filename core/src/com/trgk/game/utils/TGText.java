package com.trgk.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

/**
 * Created by phu54 on 2016-07-14.
 */
public class TGText extends Actor {
    String content;
    public TGText(String content) {
        this.content = content;
        this.setSize(100, 7.5f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        BitmapFont font = TGResources.getInstance().font;
        font.getData().setScale(0.5f, 0.5f);
        font.setColor(0, 0, 0, 1);
        font.draw(batch, content, getX(), getY() + 7.5f, 100, Align.center, false);
    }
}
