package com.trgk.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;

public class TouchWave extends ApplicationAdapter {
	TGSceneManager sceneManager;
	FPSLogger logger;

	public TouchWave() {
		sceneManager = new TGSceneManager();
	}

	////////

	@Override
	public void create () {
		logger = new FPSLogger();

		sceneManager.setCurrentScene(new TestScene());
	}

	@Override
	public void resize(int w, int h) {
		TGScene scene = sceneManager.getCurrentScene();
		scene.resize(w, h);
	}


	@Override
	public void render () {
		// Note : unlike exit() in C, Gdx.app.exit is not guaranteed to immediately quit the
		// target application. So we should check if scene stack is empty every frame and
		// issue Gdx.app.exit() every time.
		TGScene currentScene = sceneManager.getCurrentScene();
		if(currentScene == null) {
			Gdx.app.exit();
			return;
		}

		// -------

		final float speedRatio = 1f;

		// Clear background
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Divide timestep by step time.
		final float baseStepTIme = 1.0f / 60.0f;
		float dt = Gdx.graphics.getDeltaTime();
		int frameN = (int)(dt / baseStepTIme + 1);
		float stepTime = dt / frameN;
		while(frameN > 0) {
			currentScene = sceneManager.getCurrentScene();
			if(currentScene == null) {
				Gdx.app.exit();
				return;
			}

			currentScene.act(stepTime * speedRatio);

			frameN--;
		}

		currentScene = sceneManager.getCurrentScene();
		if(currentScene == null) {
			Gdx.app.exit();
			return;
		}
		currentScene.draw();

		logger.log();
	}
}
