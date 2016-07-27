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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TGScene {
    Stage stage;
    TGSceneManager sceneManager;
    int ref;

    /**
     * Default constructor
     */
    public TGScene() {
        sceneManager = null;
        ref = 0;
    }


    /**
     * Constructor with stage input
     * @param stage Input stage
     */
    public TGScene(Stage stage) {
        this();
        setStage(stage);
    }


    ////
    // Basic getter & Setter

    public Stage getStage() {
        return stage;
    }

    protected void setStage(Stage stage) {
        if(this.stage != null) {
            throw new RuntimeException("stage cannot be changed");
        }
        this.stage = stage;
    }

    public TGSceneManager getSceneManager() {
        return sceneManager;
    }

    /**
     * Set associated scene manager. Should only be called by TGSceneManager::setCurrentScene
     * @param sceneManager TGSceneManager instance
     */
    void setSceneManager(TGSceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }


    ////
    // Reference counting is needed since stage should be disposed manually

    /**
     * Add a reference to TGScene so that dispose mechanism won't work.
     *
     * Since some of the libgdx resources should be manually managed rather than being garbage
     * collected, we need a mechanism to handle their lifetime. In DodgePuzzle, we use reference
     * counting as it. Program should call this function if
     *  - TGScene are not directly used by TGSceneManager, but anyway it shouldn't be disposed.
     */
    public void ref() { ref++; }

    /**
     * Remove referece from TGScene.
     */
    public void unref() {
        ref--;
        if(ref == 0) {
            onZeroRef();
        }
    }

    /**
     * Disposes everything
     *
     * This function is called when TGScene has zero references. Here you should dispose any
     * resources that should be manually disposed.
     */
    protected void onZeroRef() {
        stage.dispose();
    }


    // ------- On window drawing

    /**
     * Called at window resizing. libgdx related
     * @param w Window width
     * @param h Window height
     */
    public void resize(int w, int h) {
        stage.getViewport().update(w, h);
    }

    /**
     * Step scene by dt seconds.
     * @param dt time
     */
    public void act(float dt) {
        ref();
        stage.act(dt);
        unref();
    }

    /**
     * Draw scenes.
     */
    public void draw() {
        stage.draw();
    }
}
