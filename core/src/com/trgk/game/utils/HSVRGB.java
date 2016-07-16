package com.trgk.game.utils;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by phu54 on 2016-07-16.
 */
public class HSVRGB {
    public static Color hsvToRgb(float hue, float saturation, float value) {
        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0: return new Color(value, t, p, 1.0f);
            case 1: return new Color(q, value, p, 1.0f);
            case 2: return new Color(p, value, t, 1.0f);
            case 3: return new Color(p, q, value, 1.0f);
            case 4: return new Color(t, p, value, 1.0f);
            case 5: return new Color(value, p, q, 1.0f);
            default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
    }
}
