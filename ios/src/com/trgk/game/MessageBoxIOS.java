package com.trgk.game;

import com.trgk.game.utils.MessageBox;
import com.trgk.game.utils.MessageBoxToken;

import org.robovm.apple.uikit.UIAlertView;

public class MessageBoxIOS extends MessageBox{
    @Override
    public MessageBoxToken alertImpl(final String title, final String message) {
        UIAlertView newAlertView = new UIAlertView(title, message, null, "OK");
        newAlertView.show();
        newAlertView.release();
        newAlertView.dispose();

        MessageBoxToken token = new MessageBoxToken();
        token.completed = true;
        return token;
    }
}
