package com.trgk.game;

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
    void resize(int w, int h) {
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
