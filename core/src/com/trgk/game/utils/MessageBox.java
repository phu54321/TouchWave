package com.trgk.game.utils;


public abstract class MessageBox {
    static MessageBox impl = null;

    public static void alert(String title, String message) {
        if(impl != null) impl.alertImpl(title, message);
    }

    public static void setImpl(MessageBox impl) {
        MessageBox.impl = impl;
    }


    // Interfaces
    public abstract MessageBoxToken alertImpl(String title, String message);
}
