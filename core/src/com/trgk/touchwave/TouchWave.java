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

package com.trgk.touchwave;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.trgk.touchwave.menuscene.MenuScene;
import com.trgk.touchwave.tgengine.TGResources;
import com.trgk.touchwave.tgengine.TGScene;
import com.trgk.touchwave.tgengine.TGSceneManager;
import com.trgk.touchwave.tgengine.TransitScene;
import com.trgk.touchwave.utils.MessageBox;

public class TouchWave extends ApplicationAdapter {
	com.trgk.touchwave.tgengine.TGSceneManager sceneManager;

	public TouchWave() {
		sceneManager = new TGSceneManager();
	}

	////////

	@Override
	public void create () {
		TGResources.getInstance().init(new AssetManager());
		GameLogger.getInstance(); // Init game log
		sceneManager.setCurrentScene(new TransitScene(null, new MenuScene(), 0.2f));
	}

	@Override
	public void resize(int w, int h) {
		TGScene scene = sceneManager.getCurrentScene();
		scene.resize(w, h);
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void pause() {
		GameLogger.getInstance().saveGameLog();
		super.pause();
	}

	@Override
	public void dispose() {
		super.dispose();
		TGResources.getInstance().dispose();
	}

	@Override
	public void render () {
		if(!sceneManager.render()) Gdx.app.exit();
	}
}
