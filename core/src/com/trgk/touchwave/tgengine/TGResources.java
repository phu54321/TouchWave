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

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.trgk.touchwave.utils.MessageBox;

public class TGResources {
    static TGSingleton<TGResources> inst = new TGSingleton<TGResources>();
    public static TGResources getInstance() {
        return inst.getInstance(TGResources.class);
    }

    public AssetManager manager;

    // -------
    public TextureAtlas textureAtlas;

    void initTextureAtlas() {
        manager.load("img/dptextures.atlas", TextureAtlas.class);
        waitResourceLoad();
        textureAtlas = manager.get("img/dptextures.atlas", TextureAtlas.class);
    }

    // -------
    final static public float baseFontSize = 60;
    public BitmapFont font = null;

    void initFont() {
        BitmapFontLoader.BitmapFontParameter parameter = new BitmapFontLoader.BitmapFontParameter();
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        manager.load("font/basefont.fnt", BitmapFont.class, parameter);
        waitResourceLoad();
        font = manager.get("font/basefont.fnt", BitmapFont.class);

    }

    public Texture rectTexture = null;
    public TextureRegion rectTextureRegion = null;

    void initRectTexture() {
        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        map.setColor(1, 1, 1, 1);
        map.fill();
        PixmapTextureData textureData = new PixmapTextureData(map, null, false, false, true);
        rectTexture = new Texture(textureData);
        rectTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        rectTextureRegion = new TextureRegion(rectTexture);
    }

    public void init(AssetManager manager) {

        this.manager = manager;
        initTextureAtlas();
        initFont();
        initRectTexture();
        Texture.setAssetManager(manager);
    }

    public void waitResourceLoad() {
        manager.finishLoading();
    }

    public void dispose() {
        manager.dispose();
    }

    /**
     * Get atlas sprite.
     * @param fname atlas fname
     * @return Atlas sprite
     */
    public Sprite getAtlasSprite(String fname) {
        Sprite sprite = textureAtlas.createSprite(fname);
        if(sprite != null) return sprite;
        else throw new RuntimeException(String.format("Unknown sprite \"%s\"", fname));
    }

    /**
     * Get atlas sprite.
     * @param fname atlas fname
     * @return Atlas sprite
     */
    public TextureRegion getAtlastRegion(String fname) {
        TextureRegion region = textureAtlas.findRegion(fname);
        if(region != null) return region;
        else throw new RuntimeException(String.format("Unknown region \"%s\"", fname));
    }
}
