package com.trgk.game;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.trgk.game.TouchWave;
import com.trgk.game.utils.MessageBox;

import java.lang.reflect.Field;
import java.util.Map;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MessageBox.setImpl(new MessageBoxAndroid(this));
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new TouchWave(), config);
	}
}
