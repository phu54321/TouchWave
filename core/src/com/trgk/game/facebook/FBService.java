/*
 * Copyright 2015 Hyun Woo Park
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module. An independent module is a module which is not derived from or
 * based on this library. If you modify this library, you may extend this
 * exception to your version of the library, but you are not obliged to
 * do so. If you do not wish to do so, delete this exception statement
 * from your version.
 */

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
    }

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

    public String username = null;
    public Long userID = null;

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
        fbReadPermissions.add("email");

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
                userID = root.getLong("id");
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
        if(message != null) request.putField("message", message);
        if(link != null) request.putField("link", link);
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
