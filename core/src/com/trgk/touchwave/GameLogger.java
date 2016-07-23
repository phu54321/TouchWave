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
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.trgk.touchwave.utils.MessageBox;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GameLogger {
    public long installTime = getCurrentTime();
    public long totalPlayTime = 0;
    public long lastAccessTime = getCurrentTime();
    public int maxScore = 0;
    public long lastPlayTime = -1;

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

        if(gamelog.exists()) {
            JsonReader reader = new JsonReader();
            JsonValue content = reader.parse(gamelog);
            try {
                for (Field field : GameLogger.class.getFields()) {
                    String fieldName = field.getName();
                    if (content.has(fieldName)) {
                        try {
                            if (field.getType() == boolean.class)
                                field.setBoolean(this, content.getBoolean(fieldName));
                            else if (field.getType() == int.class)
                                field.setInt(this, content.getInt(fieldName));
                            else if (field.getType() == long.class)
                                field.setLong(this, content.getLong(fieldName));
                        } catch (IllegalStateException e) {
                            MessageBox.alert("GameLogger", "IllegalStateException : " + e.getMessage());
                        } catch (IllegalArgumentException e) {
                            MessageBox.alert("GameLogger", "IllegalArgumentException : " + e.getMessage());
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                MessageBox.alert("Error", "IllegalAccessException : " + e.getMessage());
                throw new RuntimeException("Unexpected exception", e);
            }
        }

        saveGameLog();
    }

    /**
     * Save game data
     */
    public void saveGameLog() {
        FileHandle gamelog = Gdx.files.local("gamelog.json");
        JsonValue value = new JsonValue(JsonValue.ValueType.object);
        ArrayList<JsonValue> fields = new ArrayList<JsonValue>();

        // Add values to fields
        for (Field field : GameLogger.class.getFields()) {
            try {
                String fieldName = field.getName();
                JsonValue v;
                if (field.getType() == boolean.class)
                    v = new JsonValue(field.getBoolean(this));
                else if (field.getType() == int.class)
                    v = new JsonValue(field.getInt(this));
                else if (field.getType() == long.class)
                    v = new JsonValue(field.getLong(this));
                else {
                    MessageBox.alert("GameLogger", "Reflection error for type " + fieldName);
                    return;
                }
                v.setName(fieldName);
                fields.add(v);
            }
            catch(IllegalAccessException e) {
                MessageBox.alert("GameLogger", "IllegalAccessException : " + e.getMessage());
                return;  // Save failed
            }
        }

        // Link
        value.child = fields.get(0);
        for(int i = 0 ; i < fields.size() - 1 ; i++) {
            fields.get(i).setNext(fields.get(i + 1));
        }

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
