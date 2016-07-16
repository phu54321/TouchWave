package com.trgk.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.trgk.game.TouchWave;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(getApplication());

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new TouchWave(), config);
	}
}
