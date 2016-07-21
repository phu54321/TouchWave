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
    final public float countdownTime = 7f;

    private CountdownAnim() {
        ArrayList<TextureRegion> countdownImgList = new ArrayList<TextureRegion>();
        TGResources res = TGResources.getInstance();

        for(int i = 360 ; i >= 0 ; i -= 3) {
            String imgPath = String.format(Locale.ENGLISH, "countdown_a%d", i);
            countdownImgList.add(res.getAtlastRegion(imgPath));
        }

        TextureRegion[] countdownImgArray = new TextureRegion[countdownImgList.size()];
        countdownImgList.toArray(countdownImgArray);
        countdownAnimation  = new Animation(
                countdownTime / 120f,
                countdownImgArray
        );
        countdownAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }
}
