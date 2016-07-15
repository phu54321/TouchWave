package com.trgk.game.gamescene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.trgk.game.utils.PrimitiveImage;
import com.trgk.game.utils.TGResources;

import java.util.ArrayList;

/**
 * Created by phu54 on 2016-07-14.
 */
public class HitFrame extends Group {
    ArrayList<HitCircle> circles;
    int touchedNum = 0;
    boolean destroyed;

    final static Color[] colorTable = {
            new Color(1f, 1f, 1f, 1f),
            new Color(0f, 0f, 1f, 1f),
            new Color(0f, 1f, 0f, 1f),
            new Color(1f, 0f, 0f, 1f),
            new Color(1f, 0f, 1f, 1f),
            new Color(1f, 1f, 0f, 1f),
            new Color(0f, 1f, 1f, 1f),
            new Color(0f, 0f, 0f, 1f),
    };

    public static Color hsvToRgb(float hue, float saturation, float value) {
        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0: return new Color(value, t, p, 1.0f);
            case 1: return new Color(q, value, p, 1.0f);
            case 2: return new Color(p, value, t, 1.0f);
            case 3: return new Color(p, q, value, 1.0f);
            case 4: return new Color(t, p, value, 1.0f);
            case 5: return new Color(value, p, q, 1.0f);
            default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
    }

    final GameScene scene;

    public HitFrame(final GameScene scene, int count) {
        this.scene = scene;
        final Color circleColor = hsvToRgb((float)Math.random(), 1f, 1f);
        final Color lineColor = colorTable[count];
        this.circles = new ArrayList<HitCircle>();
        this.touchedNum = 0;
        this.destroyed = false;

        // Add background
        Image bg = PrimitiveImage.rectImage(-75f, 0, 300f, 100f, new Color(1, 1, 1, 0.4f));
        bg.setTouchable(Touchable.disabled);
        this.addActor(bg);

        this.addActor(new HitCircleLines(this, lineColor));

        // Add circles
        final float minDist = 30f;

        for(int i = 0 ; i < count ; i++) {
            float cx, cy;
            do {
                cx = (float) Math.random() * 130 + 10;
                cy = (float) Math.random() * 80 + 10;

                boolean tooClose = false;
                for (int j = 0; j < i; j++) {
                    HitCircle priorCircle = circles.get(j);
                    float pcx = priorCircle.getX() + priorCircle.getOriginX();
                    float pcy = priorCircle.getY() + priorCircle.getOriginY();
                    float dx = pcx - cx, dy = pcy - cy;
                    if (dx * dx + dy * dy <= minDist * minDist) {
                        tooClose = true;
                        break;
                    }
                }
                if (!tooClose) break;
            } while(true);

            HitCircle hitCircle = new HitCircle(this, cx, cy, circleColor);
            circles.add(hitCircle);
            this.addActor(hitCircle);
        }

        // Simple alpha animations
        this.setTouchable(Touchable.disabled);
        this.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.fadeIn(0.3f),
                        Actions.touchable(Touchable.enabled),
                        Actions.delay(14.7f),
                        new Action() {
                            @Override
                            public boolean act(float delta) {
                                scene.gameCompleted = true;
                                return true;
                            }
                        }
                )
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(destroyed) return;

        if(allCirclesTouched()) {
            this.scene.frameGroup.killFrame(this);
            
            this.clearActions();
            this.addAction(Actions.sequence(
                    Actions.touchable(Touchable.disabled),
                    Actions.alpha(1),
                    Actions.fadeOut(0.3f),
                    Actions.removeActor()
            ));
            for(HitCircle circle: circles) {
                circle.destroyed = true;
            }
            this.destroyed = true;
        }
    }

    public boolean allCirclesTouched() {
        for(HitCircle circle: circles) {
            if(!circle.touched) return false;
        }
        return true;
    }
}
