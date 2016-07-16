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
    final Image innerImage;
    boolean touched;
    boolean destroyed;

    public HitCircle(HitFrame frame, float x, float y, Color innerColor) {
        this.parent = frame;
        this.touched = false;
        this.destroyed = false;

        // Position actor in frame
        setSize(10, 10);
        this.setOrigin(Align.center);
        setPosition(x, y, Align.center);

        // Create color core
        Image inner = PrimitiveImage.circleImage(4.5f, innerColor);
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

        this.setScale(0.3f);
        this.addAction(Actions.sequence(
                Actions.scaleTo(1, 1, 0.3f, Interpolation.exp10Out),
                Actions.scaleTo(1.5f, 1.5f, 14.7f),
                Actions.scaleTo(3f, 3f, 0.5f)
        ));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.touched = false;
        if(!destroyed && isTouchable()) {
            Stage stage = this.getStage();
            if (stage != null) {
                for (int i = 0; i < 20; i++) {
                    if (Gdx.input.isTouched(i)) {
                        float touchX = Gdx.input.getX(i);
                        float touchY = Gdx.input.getY(i);
                        Vector2 touchCoord = new Vector2(touchX, touchY);
                        stage.screenToStageCoordinates(touchCoord);
                        this.stageToLocalCoordinates(touchCoord);
                        touchX = touchCoord.x;
                        touchY = touchCoord.y;

                        if (hit(touchX, touchY, true) == this) this.touched = true;
                    }
                }
            }
        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if(touchable && getTouchable() == Touchable.disabled) return null;

        final float cx = getWidth() / 2;
        final float cy = getHeight() / 2;
        final float r = 10f;
        float dx = x - cx, dy = y - cy;
        if(dx * dx + dy * dy <= r * r) return this;
        else return null;
    }
}
