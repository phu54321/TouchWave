package com.trgk.game.gamescene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.trgk.game.utils.PrimitiveImage;
import com.trgk.game.utils.TGResources;

import java.util.ArrayList;


class HitCircleLines extends Actor {
    final HitFrame parent;
    final Color lineColor;

    final static Color[] colorTable = {
            new Color(0f, 0f, 1f, 1f),
            new Color(0f, 1f, 0f, 1f),
            new Color(1f, 0f, 0f, 1f),
            new Color(1f, 0f, 1f, 1f),
            new Color(1f, 1f, 0f, 1f),
    };

    HitCircleLines(HitFrame parent) {
        this.parent = parent;
        this.lineColor = colorTable[parent.size - 1];
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
                    if (p1.touched && p2.touched) {
                        color.a = 0.8f * parentAlpha;
                    } else {
                        color.a = 0.3f * parentAlpha;
                    }

                    PrimitiveImage.drawLine(batch,
                            p1.getX() + p1.getWidth() / 2,
                            p1.getY() + p1.getHeight() / 2,
                            p2.getX() + p2.getWidth() / 2,
                            p2.getY() + p2.getHeight() / 2,
                            1f,
                            color
                    );
                }
            }
        }
    }
}
