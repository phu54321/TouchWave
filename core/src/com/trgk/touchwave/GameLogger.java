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

package com.trgk.touchwave;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

public class GameLogger {
    public long installTime;
    public long totalPlayTime;
    public long lastAccessTime;
    public int maxScore;
    public long lastPlayTime;

    static GameLogger instance;
    public static GameLogger getInstance() {
        if(instance == null) instance = new GameLogger();
        return instance;
    }

    // -------

    static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * Load game data!
     */
    private GameLogger() {
        // Try parsing existing file
        FileHandle gamelog = Gdx.files.local("gamelog.json");
        long currentTime = getCurrentTime();
        if(gamelog.exists()) {
            JsonReader reader = new JsonReader();
            JsonValue content = reader.parse(gamelog);
            installTime = content.getLong("installTime", currentTime);
            totalPlayTime = content.getLong("totalPlayTime", 0);
            lastAccessTime = content.getLong("lastAccessTime", currentTime);
            lastPlayTime = content.getLong("lastPlayTime", -1);
            maxScore = content.getInt("maxScore", 0);
        }
        else {
            installTime = currentTime;
            totalPlayTime = 0;
            lastAccessTime = currentTime;
            lastPlayTime = -1;
            maxScore = 0;
        }

        saveGameLog();
    }

    /**
     * Save game data
     */
    public void saveGameLog() {
        long currentTime = getCurrentTime();

        FileHandle gamelog = Gdx.files.local("gamelog.json");

        // TODO : Refactor using addChild method when libgdx is updated.
        // addChild method was not present at time of this code.
        JsonValue value = new JsonValue(JsonValue.ValueType.object);

        JsonValue installTimeValue = new JsonValue(installTime);
        JsonValue totalPlayTimeValue = new JsonValue(totalPlayTime);
        JsonValue lastAccessTimeValue = new JsonValue(currentTime);
        JsonValue lastPlayTimeValue = new JsonValue(lastPlayTime);
        JsonValue maxScoreValue = new JsonValue(maxScore);

        installTimeValue.setName("installTime");
        totalPlayTimeValue.setName("totalPlayTime");
        lastAccessTimeValue.setName("lastAccessTime");
        lastPlayTimeValue.setName("lastPlayTime");
        maxScoreValue.setName("maxScore");

        value.child = installTimeValue;
        installTimeValue.setNext(totalPlayTimeValue);
        totalPlayTimeValue.setNext(lastAccessTimeValue);
        lastAccessTimeValue.setNext(lastPlayTimeValue);
        lastPlayTimeValue.setNext(maxScoreValue);

        gamelog.writeString(value.toJson(JsonWriter.OutputType.json), false);
    }


    // ------

    public void updatePlay(long newPlaytime, int score) {
        long currentTime = getCurrentTime();
        totalPlayTime += newPlaytime;
        lastPlayTime = currentTime;
        if(score > maxScore) maxScore = score;
        saveGameLog();
    }

}
