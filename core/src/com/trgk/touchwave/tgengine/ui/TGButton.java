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

package com.trgk.touchwave.tgengine.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.trgk.touchwave.tgengine.TGResources;

public class TGButton extends Group {
    final Image background;
    boolean clicked = false;
    public TGButton(String caption, float size) {
        background = new Image(TGResources.getInstance().getAtlasSprite("roundbutton"));
        background.setScale(size / 64f);
        this.addActor(background);
        this.setSize(
                size * background.getWidth() / 64f,
                size * background.getHeight() / 64f
        );

        TGText text = new TGText(caption, size / 2.5f, getWidth() / 2, getHeight() / 2, new Color(0, 0, 0, 1));
        this.addActor(text);

        final TGButton this2 = this;
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                this2.clicked();
            }
        });
    }

    public TGButton(String caption, float size, float x, float y, Color color) {
        this(caption, size);
        this.setPosition(x, y, Align.center);
        this.setColor(color);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.setColor(getColor());
        super.draw(batch, parentAlpha);
    }

    public void clicked() {}
}
