package com.trgk.game.gamescene;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.trgk.game.utils.ScreenFillingGroup;

import java.util.ArrayList;

/**
 * Created by phu54 on 2016-07-16.
 */
public class FrameGroup extends ScreenFillingGroup {
    ArrayList<HitFrame> aliveFrames;
    int score = 0;

    public FrameGroup() {
        super(150, 100);
        aliveFrames = new ArrayList<HitFrame>();
    }

    /**
     * Add frame to framegroup
     * @param frame frame to add
     */
    public void addFrame(HitFrame frame) {
        addActorAt(0, frame);
        aliveFrames.add(frame);
        score += frame.size;
    }

    /**
     * Kill frame. (Make non-alive)
     * @param frame frame to kill
     */
    public void killFrame(HitFrame frame) {
        aliveFrames.remove(frame);
    }

    /**
     * Count alive frames
     * @return Number of alive frames
     */
    public int getAliveFrameCount() {
        return aliveFrames.size();
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
