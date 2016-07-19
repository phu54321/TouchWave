package com.trgk.game.menuscene;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.trgk.game.TGPopupScene;

public class FBShareResult extends TGPopupScene {
    public FBShareResult(GameoverScene parent) {
        super(parent, new Stage());

        GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me/feed").useCurrentAccessToken();
        request.setMethod(HttpMethods.POST);
        request.putField("message", "Hey use this libGDX extensions");
        request.putField("link", "https://github.com/TomGrill/gdx-facebook");
        request.putField("caption", "gdx-facebook");

        gdxFacebook.newGraphRequest(request, new GDXFacebookCallback<JsonResult>() {

            @Override
            public void onSuccess(JsonResult result) {
                // Success
            }

            @Override
            public void onError(GDXFacebookError error) {
                // Error
            }

            @Override
            public void onFail(Throwable t) {
                // Fail
            }

            @Override
            public void onCancel() {
                // Cancel
            }

        });
    }
}
