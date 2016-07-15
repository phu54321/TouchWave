package com.trgk.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class TGText extends Actor {
    GlyphLayout glyphLayout;
    BitmapFontCache drawCache;
    final static float fontSize = 60;
    static BitmapFont font = null;

    static void initFont() {
        if(font == null) {
            Texture fontTexture = new Texture(Gdx.files.internal("font/basefnt.png"));
            fontTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            font = new BitmapFont(
                    Gdx.files.internal("font/basefnt.fnt"),
                    new TextureRegion(fontTexture),
                    false
            );
        }
    }

    public TGText(String content) {
        initFont();
        glyphLayout = new GlyphLayout();
        drawCache = new BitmapFontCache(font);
        setText(content);
    }


    public void setText(String text) {
        glyphLayout.setText(font, text);
        drawCache.setText(glyphLayout, 0, glyphLayout.height);
        this.setSize(glyphLayout.width / fontSize, glyphLayout.height / fontSize);
        this.setOrigin(Align.center);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Matrix4 oldTransform = batch.getTransformMatrix();
        Affine2 localTransform = new Affine2();
        localTransform.setToTrnScl(
                getX() + getOriginX(),
                getY() + getOriginY(),
                getScaleX(),
                getScaleY()
        );
        localTransform.translate(-getOriginX(), -getOriginY());
        localTransform.scale(1f / fontSize, 1f / fontSize);

        Matrix4 localTransformMatrix = new Matrix4();
        localTransformMatrix.set(localTransform);

        Matrix4 newTransform = new Matrix4(oldTransform);
        newTransform.mul(localTransformMatrix);
        batch.setTransformMatrix(newTransform);

        drawCache.tint(getColor());
        drawCache.draw(batch);

        batch.setTransformMatrix(oldTransform);

        super.draw(batch, parentAlpha);
    }
}
