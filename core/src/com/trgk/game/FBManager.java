package com.trgk.game;

import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookConfig;
import de.tomgrill.gdxfacebook.core.GDXFacebookSystem;


public class FBManager {
    static FBManager inst = null;
    public static GDXFacebook getFB() {
        if(inst != null) inst = new FBManager();
        return inst.facebook;
    }

    ////

    final GDXFacebook facebook;
    private FBManager() {
        GDXFacebookConfig config = new GDXFacebookConfig();
        config.APP_ID = "1791581581070293"; // required
        config.PREF_FILENAME = ".facebookSessionData"; // optional
        facebook = GDXFacebookSystem.install(config);
    }
}
