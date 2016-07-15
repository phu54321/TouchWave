package com.trgk.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

/**
 * Created by 박현우 on 2016-01-05.
 */
public class PrimitiveImage {
    public static Image circleImage(float radius, Color color) {
        Image image = new Image(TGResources.getInstance().getAtlasSprite("circle_general"));
        image.setOrigin(Align.center);
        image.setScale(radius / 64.0f);
        image.setColor(color);
        return image;
    }


    static Texture rectTexture = null;
    static TextureRegion rectTextureRegion = null;
    static void createRectTexture() {
        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        map.setColor(1, 1, 1, 1);
        map.fill();
        rectTexture = new Texture(map);
        rectTextureRegion = new TextureRegion(rectTexture);
    }

    public static Image rectImage(float x0, float y0, float w, float h, Color color) {
        if(rectTexture == null) createRectTexture();
        Sprite sprite = new Sprite(rectTexture);
        Image image = new Image(sprite);
        image.setPosition(x0, y0);
        image.setSize(w, h);
        image.setColor(color);
        return image;
    }

    public static void drawLine(Batch batch, float x0, float y0, float x1, float y1, float lineWidth, Color color) {
        if(rectTexture == null) createRectTexture();

        batch.setColor(color);

        float dv_perp_x = -(y1 - y0);
        float dv_perp_y = x1 - x0;
        float dv_perp_length = (float)Math.sqrt(dv_perp_x * dv_perp_x + dv_perp_y * dv_perp_y);
        dv_perp_x /= dv_perp_length;
        dv_perp_y /= dv_perp_length;

        Affine2 transform = new Affine2();
        transform.m00 = x1 - x0;
        transform.m10 = y1 - y0;
        transform.m01 = dv_perp_x * lineWidth * 2;
        transform.m11 = dv_perp_y * lineWidth * 2;
        transform.m02 = x0 - dv_perp_x * lineWidth;
        transform.m12 = y0 - dv_perp_y * lineWidth;
        batch.draw(rectTextureRegion, 1, 1, transform);
    }
}
