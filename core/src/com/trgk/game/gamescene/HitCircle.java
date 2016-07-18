package com.trgk.game.gamescene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.trgk.game.utils.AnimatedImage;
import com.trgk.game.utils.PrimitiveImage;

public class HitCircle  extends Group {
    final HitFrame parent;
    final GameScene gameScene;
    final Image innerImage;
    boolean touched;

    public HitCircle(HitFrame frame, float x, float y, Color innerColor) {
        this.parent = frame;
        this.gameScene = frame.scene;
        this.touched = false;

        final float baseSize = 1.5f;

        // Position actor in frame
        setSize(10, 10);
        this.setOrigin(Align.center);
        setPosition(x, y, Align.center);

        // Create color core
        Image inner = PrimitiveImage.circleImage(5, innerColor);
        this.innerImage = inner;
        addActor(inner);
        inner.setPosition(5, 5, Align.center);

        // Create countdown timer anim
        Animation countdownAnimation = CountdownAnim.getInstance().countdownAnimation;
        AnimatedImage anim = new AnimatedImage(countdownAnimation);
        anim.setOrigin(Align.center);
        anim.setScale(13 / 64f);
        addActor(anim);
        anim.setPosition(5, 5, Align.center);

        this.setScale(baseSize * 0.3f);
        this.addAction(Actions.sequence(
                Actions.scaleTo(baseSize, baseSize, 0.3f, Interpolation.exp10Out),
                Actions.scaleTo(baseSize * 1.5f, baseSize * 1.5f, 6.7f),
                Actions.scaleTo(baseSize * 3f, baseSize * 3f, 0.5f)
        ));
    }


    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if(touchable && getTouchable() == Touchable.disabled) return null;

        final float cx = getWidth() / 2;
        final float cy = getHeight() / 2;
        final float r = 6f * getScaleX();
        float dx = x - cx, dy = y - cy;
        if(dx * dx + dy * dy <= r * r) return this;
        else return null;
    }
}
