package com.trgk.game;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by trgk on 16. 7. 19.
 */
public class TGPopupScene extends TGScene {
    final TGScene parent;

    public TGPopupScene(TGScene parent, Stage stage) {
        super(stage);
        parent.ref();
        this.parent = parent;
    }

    public TGScene getParentScene() {
        return parent;
    }

    // ------

    @Override
    protected void onZeroRef() {
        parent.unref();
        super.onZeroRef();
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        parent.act(dt);
    }

    @Override
    public void draw() {
        parent.draw();
        super.draw();
    }
}
