package com.trgk.game.utils;


/**
 * Token returned by MessageBox.alert. completed field will be set to true when messagebox has
 * finished execution.
 */
public class MessageBoxToken {
    public volatile boolean completed = false;
}
