package com.trgk.game.gamescene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.trgk.game.utils.HSVRGB;
import com.trgk.game.utils.PrimitiveImage;

import java.util.ArrayList;


public class HitFrame extends Group {
    ArrayList<HitCircle> circles;
    final int size;
    boolean destroyed = false;


    final GameScene scene;

    public HitFrame(final GameScene scene, int count, Color circleColor) {
        this.scene = scene;
        this.size = count;

        // Add while translucent background
        Image bg = PrimitiveImage.rectImage(-75f, 0, 300f, 100f, new Color(1, 1, 1, 0.4f));
        bg.setTouchable(Touchable.disabled);
        this.addActor(bg);

        // Add lines
        this.addActor(new HitCircleLines(this));

        // Add circles
        final float minDist = 30f;  // Minimum distances between circle pairs
        this.circles = new ArrayList<HitCircle>();

        for(int i = 0 ; i < count ; i++) {
            float cx, cy;
            do {
                cx = (float) Math.random() * 110 + 20;
                cy = (float) Math.random() * 60 + 20;

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
        this.setSize(150, 100);
        this.setTouchable(Touchable.disabled);
        this.setColor(1, 1, 1, 0);
        this.setOrigin(Align.center);
        this.setScale(0.95f);

        this.addAction(
                Actions.sequence(
                        Actions.fadeIn(0.1f),
                        Actions.delay(0.2f),
                        Actions.touchable(Touchable.enabled),
                        Actions.scaleTo(1, 1, 6.7f),
                        new Action() {
                            @Override
                            public boolean act(float delta) {
                                scene.issueGameComplete();
                                return true;
                            }
                        }
                )
        );
    }


    /**
     * Check if all circles are touched.
     */
    public boolean allCirclesTouched() {
        for(HitCircle circle: circles) {
            if(!circle.touched) return false;
        }
        return true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(destroyed) return;

        // All touched -> remove
        if(allCirclesTouched()) {
            this.scene.frameGroup.killFrame(this);
            this.destroyed = true;

            // Destroy animations
            this.clearActions();
            this.addAction(Actions.sequence(
                    Actions.touchable(Touchable.disabled),
                    Actions.alpha(1),
                    Actions.fadeOut(0.3f, Interpolation.exp10Out),
                    Actions.removeActor()
            ));

            for(HitCircle circle: circles) {
                circle.clearActions();
                circle.addAction(
                        Actions.scaleTo(3f, 3f, 0.3f, Interpolation.circleIn)
                );
            }
        }
    }
}
