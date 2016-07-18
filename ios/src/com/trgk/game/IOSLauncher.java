package com.trgk.game;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSPropertyList;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.pods.facebook.core.FBSDKAppEvents;
import org.robovm.pods.facebook.core.FBSDKApplicationDelegate;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.trgk.game.TouchWave;

import de.tomgrill.gdxfacebook.core.*;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new TouchWave(), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }


    // FB Related

    @Override
    public void didBecomeActive (UIApplication application) {
        super.didBecomeActive(application);
        FBSDKAppEvents.activateApp();
    }

    @Override
    public boolean openURL (UIApplication application, NSURL url, String sourceApplication, NSPropertyList annotation) {
        return FBSDKApplicationDelegate.getSharedInstance().openURL(application, url, sourceApplication, annotation);
    }

    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        // I know kinda weird. But keep it this way for now.
        boolean finished = super.didFinishLaunching(application, launchOptions);
        FBSDKApplicationDelegate.getSharedInstance().didFinishLaunching(application, launchOptions);
        return finished;
    }
}