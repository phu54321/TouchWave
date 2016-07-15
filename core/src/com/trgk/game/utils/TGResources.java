package com.trgk.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by 박현우 on 2015-11-29.
 */
public class TGResources {
    static TGResources inst = null;
    static public TGResources getInstance() {
        if(inst == null) inst = new TGResources();
        return inst;
    }

    // -------

    public TextureAtlas textureAtlas;
    public Skin skin;
    public BitmapFont font;

    TGResources() {
        textureAtlas = new TextureAtlas(Gdx.files.internal("img/dptextures.atlas"));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        font = skin.getFont("default-font");
    }

    /**
     * Get atlas sprite.
     * @param fname atlas fname
     * @return Atlas sprite
     * @throws TGException Unknwon srite
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
     * @throws TGException Unknwon srite
     */
    public TextureRegion getAtlastRegion(String fname) {
        TextureRegion region = textureAtlas.findRegion(fname);
        if(region != null) return region;
        else throw new RuntimeException(String.format("Unknown region \"%s\"", fname));
    }
}
