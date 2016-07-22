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

package com.trgk.touchwave.gamescene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.trgk.touchwave.tgengine.ui.TGPrimitive;

import java.util.ArrayList;


public class HitFrame extends Group {
    ArrayList<com.trgk.touchwave.gamescene.HitCircle> circles;
    final int size;
    boolean destroyed = false;


    final GameScene scene;

    public HitFrame(final GameScene scene, int count, Color circleColor) {
        this.scene = scene;
        this.size = count;

        // Add while translucent background
        Image bg = TGPrimitive.rectImage(-75f, 0, 300f, 100f, new Color(1, 1, 1, 0.4f));
        bg.setTouchable(Touchable.disabled);
        this.addActor(bg);

        // Add lines
        this.addActor(new com.trgk.touchwave.gamescene.HitCircleLines(this));

        // Add circles
        final float desiredMinDist = 30f;  // Minimum distances between circle pairs
        this.circles = new ArrayList<com.trgk.touchwave.gamescene.HitCircle>();

        for(int i = 0 ; i < count ; i++) {
            float cx, cy;
            float minDist = desiredMinDist;
            do {
                cx = (float) Math.random() * 110 + 20;
                cy = (float) Math.random() * 60 + 20;

                boolean tooClose = false;
                for (int j = 0; j < i; j++) {
                    com.trgk.touchwave.gamescene.HitCircle priorCircle = circles.get(j);
                    float pcx = priorCircle.getX() + priorCircle.getOriginX();
                    float pcy = priorCircle.getY() + priorCircle.getOriginY();
                    float dx = pcx - cx, dy = pcy - cy;
                    if (dx * dx + dy * dy <= minDist * minDist) {
                        tooClose = true;
                        break;
                    }
                }
                if (!tooClose) break;
                minDist -= 0.3f;
            } while(true);

            com.trgk.touchwave.gamescene.HitCircle hitCircle = new com.trgk.touchwave.gamescene.HitCircle(this, cx, cy, circleColor);
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
                        Actions.delay(0.4f),
                        Actions.touchable(Touchable.enabled),
                        Actions.scaleTo(1, 1, 6.5f),
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
        for(com.trgk.touchwave.gamescene.HitCircle circle: circles) {
            if(!circle.isTouched()) return false;
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
            this.addAction(Actions.parallel(
                    Actions.sequence(
                            Actions.delay(0.1f),
                            Actions.touchable(Touchable.disabled)
                    ),
                    Actions.sequence(
                            Actions.alpha(1),
                            Actions.fadeOut(0.3f, Interpolation.exp10Out),
                            Actions.removeActor()
                    )
            ));

            for(com.trgk.touchwave.gamescene.HitCircle circle: circles) {
                circle.clearActions();
                circle.addAction(
                        Actions.scaleTo(3f, 3f, 0.3f, Interpolation.circleIn)
                );
            }
        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if(touchable && getTouchable() != Touchable.enabled) return null;

        Vector2 point = new Vector2();
        for(com.trgk.touchwave.gamescene.HitCircle circle : circles) {
            circle.parentToLocalCoordinates(point.set(x, y));
            Actor hit = circle.hit(point.x, point.y, touchable);
            if(hit != null) return hit;
        }
        return null;
    }
}
