package com.trgk.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;

import com.trgk.game.utils.MessageBox;
import com.trgk.game.utils.MessageBoxToken;


public class MessageBoxAndroid extends MessageBox {
    final Activity mainActivity;
    public MessageBoxAndroid(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public MessageBoxToken alertImpl(final String title, final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        final MessageBoxToken token = new MessageBoxToken();
        handler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);

                alert.setTitle(title);
                alert.setMessage(message);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.completed = true;
                    }
                });
                alert.show();
            }
        });
        return token;
    }
}
