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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;

public class TouchWave extends ApplicationAdapter {
	TGSceneManager sceneManager;
	public static final String TAG = TouchWave.class.getSimpleName();

	public TouchWave() {
		sceneManager = new TGSceneManager();
	}

	////////

	@Override
	public void create () {
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

		// Draw
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        currentScene.draw();
    }
}
