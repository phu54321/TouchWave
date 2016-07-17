package com.trgk.game.gamescene;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.trgk.game.utils.TGResources;

import java.util.ArrayList;
import java.util.Locale;


public class CountdownAnim {
    static CountdownAnim inst = null;
    static public CountdownAnim getInstance() {
        if(inst == null) inst = new CountdownAnim();
        return inst;
    }

    // -------

    public Animation countdownAnimation;
    final public float countdownTIme = 7f;

    private CountdownAnim() {
        ArrayList<TextureRegion> countdowns = new ArrayList<TextureRegion>();
        TGResources res = TGResources.getInstance();

        for(int i = 360 ; i >= 0 ; i -= 3) {
            String imgPath = String.format(Locale.ENGLISH, "countdown_a%d", i);
            countdowns.add(res.getAtlastRegion(imgPath));
        }

        countdownAnimation  = new Animation(
                countdownTIme / 120f,
                countdowns.toArray(new TextureRegion[]{})
        );
        countdownAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }
}
