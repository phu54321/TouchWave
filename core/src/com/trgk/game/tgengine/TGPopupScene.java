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

package com.trgk.game.tgengine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.trgk.game.tgengine.ui.TGPrimitive;

public class TGPopupScene extends TGScene {
    final TGScene parent;
    Image background;


    public void gotoParent() {
        background.clearActions();
        background.addAction(Actions.sequence(
                Actions.alpha(0f, 0.3f),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        TGScene parent = getParentScene();
                        parent.getStage().getRoot().setTouchable(Touchable.enabled);
                        getSceneManager().setCurrentScene(parent);
                        return true;
                    }
                }
        ));
    }

    public TGPopupScene(TGScene parent, Stage stage, Color backgroundColor) {
        super(stage);

        // Set parent
        parent.ref();
        parent.getStage().getRoot().setTouchable(Touchable.disabled);
        this.parent = parent;

        // Add background image
        background = TGPrimitive.rectImage(0, 0, 1, 1, Color.BLACK);
        background.setColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 0);
        getStage().addActor(background);
        background.addAction(Actions.alpha(backgroundColor.a, 0.3f));
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
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        parent.draw();
        super.draw();
    }
}
