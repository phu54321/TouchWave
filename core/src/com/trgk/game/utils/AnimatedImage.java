package com.trgk.game.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by phu54 on 2016-07-14.
 */
public class AnimatedImage extends Image
{
    protected Animation animation = null;
    private float stateTime = 0;

    public AnimatedImage(Animation animation) {
        super(animation.getKeyFrame(0));
        this.animation = animation;
    }

    @Override
    public void act(float delta)
    {
        stateTime += delta;
        TextureRegion currentRegion = animation.getKeyFrame(stateTime, false);
        ((TextureRegionDrawable)getDrawable()).setRegion(currentRegion);
        super.act(delta);
    }
}