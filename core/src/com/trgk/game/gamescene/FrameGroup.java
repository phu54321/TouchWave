package com.trgk.game.gamescene;

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
        score += frame.size;
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
