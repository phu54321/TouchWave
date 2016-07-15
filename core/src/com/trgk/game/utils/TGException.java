package com.trgk.game.utils;

/**
 * Created by 박현우 on 2015-11-24.
 */
public class TGException extends Exception {
    public TGException(String message) {
        super(message);
    }

    public TGException(String message, Throwable e) {
        super(message, e);
    }
}
