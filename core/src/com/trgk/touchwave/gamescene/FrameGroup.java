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

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;


public class FrameGroup extends Group {
    ArrayList<HitFrame> aliveFrames;
    int score = 0;

    public FrameGroup() {
        aliveFrames = new ArrayList<HitFrame>();
    }

    /**
     * Add frame to framegroup
     * @param frame frame to add
     */
    public void addFrame(HitFrame frame) {
        addActorAt(0, frame);
        frame.setPosition(75, 50, Align.center);
        aliveFrames.add(frame);
    }

    /**
     * Kill frame. (Make non-alive)
     * @param frame frame to kill
     */
    public void killFrame(HitFrame frame) {
        aliveFrames.remove(frame);
        score += frame.size * frame.size;
    }

    /**
     * Count alive frames
     * @return Number of alive frames
     */
    public int getAliveFrameCount() {
        return aliveFrames.size();
    }

    public int getScore() {
        return score;
    }

    /**
     * Make only 10 frames visible.
     */
    public void setFrameVisibility() {
        for (int i = 0; i < aliveFrames.size(); i++) {
            if (i == 11) break;
            HitFrame frame = aliveFrames.get(i);
            frame.setVisible(true);
            frame.setTouchable(Touchable.enabled);
        }
        for (int i = 11; i < aliveFrames.size(); i++) {
            HitFrame frame = aliveFrames.get(i);
            frame.setVisible(false);
            frame.setTouchable(Touchable.disabled);
        }
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        setFrameVisibility();
    }
}
