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

package com.trgk.game.gamescene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.trgk.game.utils.HSVRGB;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.trgk.game.utils.PrimitiveImage;
import com.trgk.game.utils.TGResources;

import java.util.ArrayList;


class HitCircleLines extends Actor {
    final HitFrame parent;
    final Color lineColor;

    HitCircleLines(HitFrame parent) {
        this.parent = parent;
        float hue = 0.618033988749895f * (2 * parent.size - parent.size / 3);
        hue -= Math.floor(hue);
        this.lineColor = HSVRGB.hsvToRgb(hue, 0.9f, 0.95f);
        this.setTouchable(Touchable.disabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        ArrayList<HitCircle> circles = parent.circles;

        if(circles.size() > 1) {
            for(int i = 0 ; i < circles.size() ; i++) {
                for(int j = i + 1 ; j < circles.size() ; j++) {
                    HitCircle p1 = circles.get(i);
                    HitCircle p2 = circles.get(j);

                    Color color = new Color(lineColor);
                    if (p1.isTouched() && p2.isTouched()) {
                        color.a = 0.8f * parentAlpha;
                    } else {
                        color.a = 0.4f * parentAlpha;
                    }

                    PrimitiveImage.drawDottedLine(batch,
                            p1.getX() + p1.getWidth() / 2,
                            p1.getY() + p1.getHeight() / 2,
                            p2.getX() + p2.getWidth() / 2,
                            p2.getY() + p2.getHeight() / 2,
                            1f,
                            0.2f,
                            color
                    );
                }
            }
        }
    }
}
