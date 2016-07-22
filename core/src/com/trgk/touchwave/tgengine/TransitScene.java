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

package com.trgk.touchwave.tgengine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.touchwave.tgengine.ui.TGPrimitive;

public class TransitScene extends TGScene {
    TGScene before, after;
    TGScene backgroundScene;
    Image background;


    public TransitScene(final TGScene before, final TGScene after, float transitTime) {
        super(new Stage(new ScreenViewport()));
        this.before = before;
        this.after = after;

        before.ref();
        after.ref();

        backgroundScene = before;

        // Add background image
        background = TGPrimitive.rectImage(0, 0, 1, 1, Color.BLACK);
        background.setColor(0, 0, 0, 0);
        getStage().addActor(background);

        final TransitScene this2 = this;

        background.addAction(Actions.sequence(
                Actions.fadeIn(transitTime / 2),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        this2.backgroundScene = after;
                        this2.before = null;
                        before.unref();
                        return true;
                    }
                },
                Actions.fadeOut(transitTime),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        getSceneManager().setCurrentScene(after);
                        this2.after = null;
                        after.unref();
                        return true;
                    }
                }
        ));
    }

    @Override
    protected void onZeroRef() {
        if(before != null) before.unref();
        if(after != null) after.unref();
        before = after = null;
        super.onZeroRef();
    }

    @Override
    public void draw() {
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(backgroundScene != null) backgroundScene.draw();
        super.draw();
    }
}
