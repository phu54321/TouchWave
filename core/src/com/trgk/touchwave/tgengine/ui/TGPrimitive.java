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

package com.trgk.touchwave.tgengine.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.trgk.touchwave.tgengine.TGResources;


public class TGPrimitive {
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
        rectTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
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

    public static void drawDottedLine(
            Batch batch,
            float x0, float y0, float x1, float y1,
            float lineWidth, float spaceRatio,
            Color color
    ) {
        float dx = x1 - x0, dy = y1 - y0;
        float lineLength = (float)Math.sqrt(dx * dx + dy * dy);
        float baseLineDotLength = 10 * lineWidth;

        // bldl * (snum + (snum - 1) * sr) = length
        // (length + bldl * sr) / (1 + sr) / bldl = snum

        int segNum = (int)Math.floor(
                (lineLength + baseLineDotLength * spaceRatio) /
                        (1 + spaceRatio) / baseLineDotLength);
        if(segNum == 0) segNum = 1;

        // pdx =
        float segDx = dx / (segNum + (segNum - 1) * spaceRatio);
        float segDy = dy / (segNum + (segNum - 1) * spaceRatio);
        float patternDx = segDx * (1 + spaceRatio);
        float patternDy = segDy * (1 + spaceRatio);

        for(int i = 0 ; i < segNum ; i++) {
            drawLine(batch, x0, y0, x0 + segDx, y0 + segDy, lineWidth, color);
            x0 += patternDx;
            y0 += patternDy;
        }
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
