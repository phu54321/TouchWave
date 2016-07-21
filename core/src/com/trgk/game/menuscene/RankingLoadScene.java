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

package com.trgk.game.menuscene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpRequestHeader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.trgk.game.GameLogger;
import com.trgk.game.facebook.FBService;
import com.trgk.game.tgengine.TGPopupScene;
import com.trgk.game.tgengine.ui.TGText;
import com.trgk.game.tgengine.ui.TGWindow;

import java.util.Locale;


public class RankingLoadScene extends TGPopupScene {
    final RankingScene parent;
    int currentState = 0;

    public RankingLoadScene(RankingScene parent) {
        super(parent, new Stage(new ScreenViewport()), new Color(0, 0, 0, 0.8f));
        this.parent = parent;
    }

    public void postErrorMsg(String string, int line) {
        TGText fbErrorMsg = new TGText(string, 2.8f, 0, 0, Color.RED);
        fbErrorMsg.setOrigin(Align.topLeft);
        fbErrorMsg.setPosition(1, 61 - 3 * line, Align.topLeft);
        parent.rankingWindow.addActor(fbErrorMsg);
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        final TGWindow rankingWindow = parent.rankingWindow;
        final FBService fb = FBService.getInstance();
        final GameLogger logger = GameLogger.getInstance();

        // Get user ID
        if(currentState == 0) {
            if(logger.maxScore == -1) currentState = 6;
            else {
                if (fb.isLogonRead()) currentState = 2;
                else {
                    fb.loginRead();
                    currentState = 1;
                }
            }
        }

        // Wait for fb read login
        if(currentState == 1) {
            if(!fb.isBusy()) {
                if(fb.getLastActionResult() != FBService.Result.SUCCESS) {
                    postErrorMsg("페이스북 로그인을 하지 못했습니다.", 0);
                    currentState = 6;  // Skip user status
                }
                else currentState = 2;
            }
        }

        // Post user data
        if(currentState == 2) {
            String reqURL = "http://phu54321.pythonanywhere.com/ranking/";
            HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

            JsonValue requestBodyJson = new JsonValue(JsonValue.ValueType.object);

            JsonValue fbIDValue = new JsonValue(fb.userID);
            JsonValue fbNicknameValue = new JsonValue(fb.username);
            JsonValue maxScoreValue = new JsonValue(GameLogger.getInstance().maxScore);

            fbIDValue.setName("fbID");
            fbNicknameValue.setName("fbNickname");
            maxScoreValue.setName("maxScore");

            requestBodyJson.child = fbIDValue;
            fbIDValue.setNext(fbNicknameValue);
            fbNicknameValue.setNext(maxScoreValue);

            String requestBody = requestBodyJson.toJson(JsonWriter.OutputType.json);

            final Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.PUT).url(reqURL)
                    .header(HttpRequestHeader.ContentType, "application/json").content(requestBody).build();
            currentState = 3;  // Wait for http request to complete

            Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    currentState = 4;
                }

                @Override
                public void failed(Throwable t) {
                    postErrorMsg("랭킹 등록에 실패했습니다.", 0);
                    currentState = 6;
                }

                @Override
                public void cancelled() {
                    postErrorMsg("랭킹 등록에 실패했습니다.", 0);
                    currentState = 6;
                }
            });
        }

        // Get user ranking
        if(currentState == 4) {
            String reqURL = String.format(Locale.ENGLISH, "http://phu54321.pythonanywhere.com/ranking/%d", fb.userID);
            HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
            final Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(reqURL).build();
            currentState = 5;
            Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    JsonReader reader = new JsonReader();
                    JsonValue result = reader.parse(httpResponse.getResultAsString());

                    if(result.isObject() && result.get("myRank") != null) {
                        int myRank = result.getInt("myRank");
                        rankingWindow.addActor(
                                new RankingScene.RankingEntry(myRank, fb.username, logger.maxScore, Color.BLUE, 0)
                        );
                        currentState = 6;
                    }
                    else {
                        postErrorMsg("순위를 읽는데 실패했습니다.", 0);
                        currentState = 6;
                    }
                }

                @Override
                public void failed(Throwable t) {
                    postErrorMsg("순위를 읽는데 실패했습니다.", 0);
                    currentState = 6;
                }

                @Override
                public void cancelled() {
                    postErrorMsg("순위를 읽는데 실패했습니다.", 0);
                    currentState = 6;
                }
            });
        }

        // Get total ranking
        if(currentState == 6) {
            String reqURL = "http://phu54321.pythonanywhere.com/ranking/";
            HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
            final Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(reqURL).build();
            currentState = 7;

            Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    JsonReader reader = new JsonReader();
                    JsonValue result = reader.parse(httpResponse.getResultAsString());

                    if(result.isObject() && result.get("rankings") != null) {
                        JsonValue rankingArray = result.get("rankings");
                        JsonValue rankingEntry = rankingArray.child;
                        int entryID = 1;
                        while(rankingEntry != null) {
                            rankingWindow.addActor(
                                    new RankingScene.RankingEntry(
                                            entryID,
                                            rankingEntry.getString("fbNickname"),
                                            rankingEntry.getInt("maxScore"),
                                            Color.BLACK, entryID)
                            );
                            rankingEntry = rankingEntry.next;
                            entryID++;

                            if(entryID == 20) break;
                        }
                        currentState = 8;
                    }
                    else {
                        postErrorMsg("랭킹을 읽지 못했습니다.", 1);
                        currentState = 8;
                    }
                }

                @Override
                public void failed(Throwable t) {
                    postErrorMsg("랭킹을 읽지 못했습니다.", 1);
                    currentState = 8;
                }

                @Override
                public void cancelled() {
                    postErrorMsg("랭킹을 읽지 못했습니다.", 1);
                    currentState = 8;
                }
            });
        }

        if(currentState == 8) {
            fb.logout();
            gotoParent();
            currentState = 9;
        }


    }
}
