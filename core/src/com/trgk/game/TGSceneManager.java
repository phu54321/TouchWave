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

package com.trgk.game;

import com.badlogic.gdx.Gdx;

public class TGSceneManager {
    TGScene currentScene;

    public TGSceneManager() {
        currentScene = null;
    }

    /**
     * Replace current scene with other scene
     * @param newScene Scene to replace to.
     */
    public void setCurrentScene(TGScene newScene) {
        TGScene oldScene = currentScene;

        currentScene = newScene;

        // currentScene may hold the only reference to newScene.
        // So we should re-referece newScene before unreferencing (that may destroy) currentScene.
        if(newScene != null) {
            newScene.ref();
            newScene.setSceneManager(this);
            Gdx.input.setInputProcessor(newScene.stage);
        }

        if(oldScene != null) {
            oldScene.setSceneManager(null);
            oldScene.unref();
        }
    }


    /**
     * Get current scene.
     * @return current scene. If scene stack is empty, null.
     */
    public TGScene getCurrentScene() {
        return currentScene;
    }
}

