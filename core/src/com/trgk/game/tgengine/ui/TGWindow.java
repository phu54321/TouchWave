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

package com.trgk.game.tgengine.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.trgk.game.tgengine.TGResources;


public class TGWindow extends Group {
    final Image background;
    public TGWindow(float size) {
        background = new Image(TGResources.getInstance().getAtlasSprite("roundbox"));
        background.setScale(size / 256f);
        this.addActor(background);
        this.setSize(
                size * background.getWidth() / 256f,
                size * background.getHeight() / 256f
        );
    }

    public static <T extends Actor> T showAfter(T actor, float time) {
        actor.setVisible(false);
        actor.setTouchable(Touchable.disabled);
        actor.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.delay(time),
                        Actions.visible(true),
                        Actions.fadeIn(0.5f),
                        Actions.touchable(Touchable.enabled)
                )
        );
        return actor;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.setColor(getColor());
        super.draw(batch, parentAlpha);
    }
}
