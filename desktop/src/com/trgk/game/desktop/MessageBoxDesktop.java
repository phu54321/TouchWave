package com.trgk.game.desktop;

import com.trgk.game.utils.MessageBox;
import com.trgk.game.utils.MessageBoxToken;

import javax.swing.JOptionPane;

public class MessageBoxDesktop extends MessageBox {
    class MessageBoxRunner extends Thread {
        String title;
        String message;
        MessageBoxToken token;
        MessageBoxRunner(String title, String message, MessageBoxToken token) {
            this.title = title;
            this.message = message;
            this.token = token;
        }
        public void run() {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            token.completed = true;
        }
    }

    @Override
    public MessageBoxToken alertImpl(String title, String message) {
        MessageBoxToken token = new MessageBoxToken();
        new MessageBoxRunner(title, message, token).start();
        return token;
    }
}
