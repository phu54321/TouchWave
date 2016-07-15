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

