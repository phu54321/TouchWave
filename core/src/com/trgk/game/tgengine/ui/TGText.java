/*
 * Copyright 2015 Hyun Woo Park
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module. An independent module is a module which is not derived from or
 * based on this library. If you modify this library, you may extend this
 * exception to your version of the library, but you are not obliged to
 * do so. If you do not wish to do so, delete this exception statement
 * from your version.
 */

package com.trgk.game.tgengine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
    final static float baseFontSize = 45;
    static BitmapFont font = null;
    String text;


    static void initFont() {
        if(font == null) {
            Texture fontTexture = new Texture(Gdx.files.internal("font/basefont.png"));
            fontTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            font = new BitmapFont(
                    Gdx.files.internal("font/basefont.fnt"),
                    new TextureRegion(fontTexture),
                    false
            );
            font.setOwnsTexture(true);
        }
    }

    public TGText(String content, float size) {
        initFont();
        glyphLayout = new GlyphLayout();
        drawCache = new BitmapFontCache(font);
        setText(content);
        setScale(size);
        this.setOrigin(Align.center);
    }

    public TGText(String content, float size, float x, float y, Color color) {
        this(content, size);
        this.setPosition(x, y, Align.center);
        this.setColor(color);
    }


    public void setText(String text) {
        glyphLayout.setText(font, text);
        drawCache.setText(glyphLayout, 0, glyphLayout.height);
        this.setSize(glyphLayout.width / baseFontSize, glyphLayout.height / baseFontSize);
        this.text = text;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Matrix4 oldTransform = new Matrix4(batch.getTransformMatrix());
        Affine2 localTransform = new Affine2();
        localTransform.setToTrnScl(
                getX() + getOriginX(),
                getY() + getOriginY(),
                getScaleX(),
                getScaleY()
        );
        localTransform.translate(-getOriginX(), -getOriginY());
        localTransform.scale(1f / baseFontSize, 1f / baseFontSize);

        Matrix4 localTransformMatrix = new Matrix4();
        localTransformMatrix.set(localTransform);

        Matrix4 newTransform = new Matrix4(oldTransform);
        newTransform.mul(localTransformMatrix);
        batch.setTransformMatrix(newTransform);

        Color alphaMultipliedColor = new Color(getColor());
        alphaMultipliedColor.a *= parentAlpha;
        drawCache.tint(alphaMultipliedColor);
        drawCache.draw(batch);

        batch.setTransformMatrix(oldTransform);
    }
}
