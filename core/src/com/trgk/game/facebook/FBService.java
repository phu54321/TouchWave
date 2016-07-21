package com.trgk.game.facebook;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

import java.io.IOException;
import java.io.InputStream;

import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallback;
import de.tomgrill.gdxfacebook.core.GDXFacebookConfig;
import de.tomgrill.gdxfacebook.core.GDXFacebookError;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphRequest;
import de.tomgrill.gdxfacebook.core.GDXFacebookMultiPartRequest;
import de.tomgrill.gdxfacebook.core.GDXFacebookSystem;
import de.tomgrill.gdxfacebook.core.JsonResult;
import de.tomgrill.gdxfacebook.core.SignInMode;
import de.tomgrill.gdxfacebook.core.SignInResult;


public class FBService {
    // Singleton pattern
    static FBService inst = null;

    public static FBService getInstance() {
        if(inst == null) inst = new FBService();
        return inst;
    }


    ///// Constructor

    final GDXFacebook fbHandle;
    private FBService() {
        GDXFacebookConfig config = new GDXFacebookConfig();
        config.APP_ID = "1791581581070293";
        config.PREF_FILENAME = ".facebookSessionData";
        fbHandle = GDXFacebookSystem.install(config);
    }


    ///////

    // Basic interfaces
    public enum Result {
        WORKING,
        SUCCESS,
        FAILED,
        CANCELED
    };

    Result lastActionResult = Result.SUCCESS;
    boolean isLogonReadPermission = false, isLogonPublishPermission = false;

    /**
     * Whether last action has been successful or not
     * @return One of WORKING, SUCCESS, CANCELED, FAILED
     */
    public Result getLastActionResult() {
        return lastActionResult;
    }


    /**
     * Check if service is busy running actions
     */
    public boolean isBusy() {
        return lastActionResult == Result.WORKING;
    }

    public boolean isLogonRead() {
        return isLogonReadPermission;
    }

    public boolean isLogonPublish() {
        return isLogonPublishPermission;
    }


    ////////

    public String username = null, userID = null;

    /**
     * Lock service for working.
     */
    void startWorking() {
        if (isBusy()) throw new RuntimeException("Call to loginRead while running");
        lastActionResult = Result.WORKING;
    }

    /**
     * Login FB with read permission
     */
    public void loginRead() {
        startWorking();

        Array<String> fbReadPermissions = new Array<String>();
        fbReadPermissions.add("public_profile");
        fbReadPermissions.add("user_friends");
        fbReadPermissions.add("email");;

        fbHandle.signIn(SignInMode.READ, fbReadPermissions, new GDXFacebookCallback<SignInResult>() {
            @Override
            public void onSuccess(SignInResult result) {
                // Login successful
                lastActionResult = Result.SUCCESS;
                updateUserInfo();
            }

            @Override
            public void onError(GDXFacebookError error) {
                // Error handling
                lastActionResult = Result.FAILED;
            }

            @Override
            public void onCancel() {
                lastActionResult = Result.CANCELED;
            }

            @Override
            public void onFail(Throwable t) {
                lastActionResult = Result.FAILED;
            }
        });
    }


    /**
     * Login FB with publish permission
     */
    public void loginPublish() {
        startWorking();

        Array<String> fbPublishPermissions = new Array<String>();
        fbPublishPermissions.add("publish_actions");

        fbHandle.signIn(SignInMode.PUBLISH, fbPublishPermissions, new GDXFacebookCallback<SignInResult>() {
            @Override
            public void onSuccess(SignInResult result) {
                lastActionResult = Result.SUCCESS;
            }

            @Override
            public void onCancel() {
                lastActionResult = Result.CANCELED;
            }

            @Override
            public void onFail(Throwable t) {
                lastActionResult = Result.FAILED;
            }

            @Override
            public void onError(GDXFacebookError error) {
                lastActionResult = Result.FAILED;
            }
        });
    }


    /**
     * Logout from FB
     */
    public void logout() {
        fbHandle.signOut();
        isLogonReadPermission = true;
        isLogonPublishPermission = false;
    }



    /**
     * Update user information.
     */
    void updateUserInfo() {
        startWorking();

        GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me").useCurrentAccessToken();
        fbHandle.graph(request, new GDXFacebookCallback<JsonResult>() {
            @Override
            public void onSuccess(JsonResult result) {
                // Success
                JsonValue root = result.getJsonValue();
                userID = root.getString("id");
                username = root.getString("name");
                lastActionResult = Result.SUCCESS;
            }

            @Override
            public void onError(GDXFacebookError error) {
                // Error
                lastActionResult = Result.FAILED;
                logout();
            }

            @Override
            public void onFail(Throwable t) {
                // Fail
                lastActionResult = Result.FAILED;
                logout();
            }

            @Override
            public void onCancel() {
                // Cancel
                lastActionResult = Result.CANCELED;
                logout();
            }
        });
    }


    /**
     * Post photo to user wall
     */
    public void postPhotoToUserWall(String caption, String message, String link, FileHandle imgFile) {
        startWorking();

        GDXFacebookMultiPartRequest request = new GDXFacebookMultiPartRequest().setNode("me/photos").useCurrentAccessToken();
        request.setMethod(Net.HttpMethods.POST);
        request.putField("message", message);
        request.putField("link", link);
        request.putField("caption", caption);
        request.setFileHandle(imgFile, "multipart/form-data");

        fbHandle.graph(request, new GDXFacebookCallback<JsonResult>() {
            @Override
            public void onFail(Throwable t) {
                lastActionResult = Result.FAILED;
            }

            @Override
            public void onCancel() {
                lastActionResult = Result.CANCELED;
            }

            @Override
            public void onSuccess(JsonResult result) {
                lastActionResult = Result.SUCCESS;
            }

            @Override
            public void onError(GDXFacebookError error) {
                lastActionResult = Result.FAILED;
            }
        });
    }
}
