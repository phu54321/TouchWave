package com.trgk.game.gamescene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
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

import java.util.HashSet;

public class HitCircle  extends Group {
    private final Stage mainStage;
    private float timeSinceLastTouch;
    private final Image innerImage;
    private final Color baseColor;
    private final Color touchedColor;
    HashSet<Integer> touchedSet;

    public HitCircle(HitFrame frame, float x, float y, Color innerColor) {
        this.mainStage = frame.scene.getStage();
        this.timeSinceLastTouch = 1000;
        this.touchedSet = new HashSet<Integer>();

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

        // Set color
        this.baseColor = innerColor;
        Color touchedColor = new Color();
        touchedColor.r = (innerColor.r + 1) / 2;
        touchedColor.g = (innerColor.g + 1) / 2;
        touchedColor.b = (innerColor.b + 1) / 2;
        touchedColor.a = 1;
        this.touchedColor = touchedColor;

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

        final HitCircle this2 = this;
        final HashSet<Integer> touchedSet = this.touchedSet;
        this.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchedSet.add(pointer);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    /////

    public boolean isTouched() {
        return timeSinceLastTouch < 0.2f;
    }

    public void updateTouchStatus() {
        Vector2 touchCoord = new Vector2();
        for(int pointer = 0 ; pointer < 20 ; pointer++) {
            if (touchedSet.contains(pointer)) {
                boolean currentlyTouching = false;
                if(Gdx.input.isTouched(pointer)) {
                    float touchX = Gdx.input.getX(pointer);
                    float touchY = Gdx.input.getY(pointer);
                    mainStage.screenToStageCoordinates(touchCoord.set(touchX, touchY));
                    this.stageToLocalCoordinates(touchCoord);
                    touchX = touchCoord.x;
                    touchY = touchCoord.y;
                    if(this.hit(touchX, touchY, true) == this) currentlyTouching = true;
                }
                if(!currentlyTouching) {
                    touchedSet.remove(pointer);
                }
            }
        }
    }

    /////

    @Override
    public void act(float delta) {
        super.act(delta);
        updateTouchStatus();

        if(this.touchedSet.isEmpty()) {
            timeSinceLastTouch += delta;
        }
        else {
            timeSinceLastTouch = 0;
            innerImage.clearActions();

            innerImage.addAction(Actions.sequence(
                    Actions.color(this.touchedColor),
                    Actions.color(baseColor, 0.3f)
            ));
        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if(touchable && getTouchable() == Touchable.disabled) return null;

        final float cx = getWidth() / 2;
        final float cy = getHeight() / 2;
        final float r = 5f * getScaleX();
        float dx = x - cx, dy = y - cy;
        if(dx * dx + dy * dy <= r * r) return this;
        else return null;
    }
}
