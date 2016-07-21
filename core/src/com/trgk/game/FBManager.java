package com.trgk.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.trgk.game.utils.MessageBox;

import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallback;
import de.tomgrill.gdxfacebook.core.GDXFacebookConfig;
import de.tomgrill.gdxfacebook.core.GDXFacebookError;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphRequest;
import de.tomgrill.gdxfacebook.core.GDXFacebookSystem;
import de.tomgrill.gdxfacebook.core.JsonResult;
import de.tomgrill.gdxfacebook.core.SignInMode;
import de.tomgrill.gdxfacebook.core.SignInResult;


public class FBManager {
    static FBManager inst = null;
    public static FBManager getInstance() {
        if(inst != null) inst = new FBManager();
        return inst;
    }

    ////

    final GDXFacebook fbHandle;
    private FBManager() {
        GDXFacebookConfig config = new GDXFacebookConfig();
        config.APP_ID = "1791581581070293"; // required
        config.PREF_FILENAME = ".facebookSessionData"; // optional
        fbHandle = GDXFacebookSystem.install(config);
    }

    ////
    String userID = null, username = null;

    boolean isReadLogon = false, isPublishLogon = false;

    /**
     * Login FB with read permission
     */
    public void loginRead(final Runnable onLoginSuccess, final Runnable onLoginFailure) {
        if(isReadLogon) return;

        Array<String> fbReadPermissions = new Array<String>();
        fbReadPermissions.add("public_profile");
        fbReadPermissions.add("user_friends");
        fbReadPermissions.add("email");

        final FBManager this2 = this;
        fbHandle.signIn(SignInMode.READ, fbReadPermissions, new GDXFacebookCallback<SignInResult>() {
            @Override
            public void onSuccess(SignInResult result) {
                // Login successful
                updateUserInfo();
                isReadLogon = true;
                onLoginSuccess.run();
            }

            @Override
            public void onError(GDXFacebookError error) {
                // Error handling
                MessageBox.alert("Facebook Login Error", "Error logining to fb");
                logout();
                onLoginFailure.run();
            }

            @Override
            public void onCancel() {
                // When the user cancels the login process
            }

            @Override
            public void onFail(Throwable t) {
                // When the login fails
                MessageBox.alert("Facebook Login Error", "Error logining to fb");
                this2.logout();
            }
        });
    }


    /**
     * Login FB with publish permission
     */
    public void loginPublish() {
        Array<String> fbPublishPermissions = new Array<String>();
        fbPublishPermissions.add("publish_actions");

        final FBManager this2 = this;
        fbHandle.signIn(SignInMode.PUBLISH, fbPublishPermissions, new GDXFacebookCallback<SignInResult>() {

            @Override
            public void onSuccess(SignInResult result) {
                updateUserInfo();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onFail(Throwable t) {
                MessageBox.alert("Error", "Error on login for publishing.");
                logout();
            }

            @Override
            public void onError(GDXFacebookError error) {
                MessageBox.alert("Error", "Error on login for publishing.");
                logout();
            }

        });
    }


    /**
     * Logout from FB
     */
    public void logout() {
        fbHandle.signOut();
    }



    /**
     * Update user information.
     */
    public void updateUserInfo() {
        final FBManager this2 = this;

        GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me").useCurrentAccessToken();
        fbHandle.graph(request, new GDXFacebookCallback<JsonResult>() {
            @Override
            public void onSuccess(JsonResult result) {
                // Success
                JsonValue root = result.getJsonValue();
                this2.userID = root.getString("id");
                this2.username = root.getString("name");
            }

            @Override
            public void onError(GDXFacebookError error) {
                // Error
                MessageBox.alert("Facebook Login Error", "Error reading user info");
                this2.logout();
            }

            @Override
            public void onFail(Throwable t) {
                // Fail
                Gdx.app.error(TouchWave.TAG, "Exception occured while trying to post to user wall.");
                this2.logout();
            }

            @Override
            public void onCancel() {
                // Cancel
                this2.logout();
            }

        });
    }

    /**
     * Post message to user wall
     */
    private void postToUserWall(String caption, String message, String link) {
        GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me/feed").useCurrentAccessToken();
        request.setMethod(Net.HttpMethods.POST);
        request.putField("message", message);
        request.putField("link", link);
        request.putField("caption", caption);
        fbHandle.graph(request, new GDXFacebookCallback<JsonResult>() {
            @Override
            public void onFail(Throwable t) {
                Gdx.app.error(TouchWave.TAG, "Exception occured while trying to post to user wall.");
                MessageBox.alert("Error", "Error trying to post to user wall\n" + t.getMessage());
                t.printStackTrace();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onSuccess(JsonResult result) {
                MessageBox.alert("Shared", "Result shared");
            }

            @Override
            public void onError(GDXFacebookError error) {
                MessageBox.alert("Error", "Error trying to post to user wall\n" + error.getErrorMessage());
            }

        });
    }
}
