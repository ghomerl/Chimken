package com.github.ghomerl.chimken.model.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.ghomerl.chimken.model.User;


public class DatabaseManager {

    private static final String DB_FILE = "chimken_users.json";
    private static Array<User> users;
    private static int nextId = 1;
    private static boolean initialized = false;

    private DatabaseManager() {
    }


    public static synchronized Array<User> getUsers() {
        if (!initialized) {
            init();
        }
        return users;
    }


    public static synchronized int getNextId() {
        return nextId++;
    }


    public static synchronized void save() {
        if (!initialized) return;
        try {
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            json.setIgnoreUnknownFields(true);
            String data = json.prettyPrint(users);
            Gdx.files.local(DB_FILE).writeString(data, false);
        } catch (Exception e) {
            Gdx.app.error("DatabaseManager", "Failed to save data.", e);
        }
    }

    private static void init() {
        try {
            if (Gdx.files.local(DB_FILE).exists()) {
                String data = Gdx.files.local(DB_FILE).readString();
                if (data.trim().isEmpty()) {
                    users = new Array<>();
                } else {
                    Json json = new Json();
                    json.setIgnoreUnknownFields(true);
                    users = json.fromJson(Array.class, User.class, data);
                    for (User u : users) {
                        if (u.getId() >= nextId) {
                            nextId = u.getId() + 1;
                        }
                    }
                }
            } else {
                users = new Array<>();
            }
            initialized = true;
            Gdx.app.log("DatabaseManager", "JSON database initialized.");
        } catch (Exception e) {
            Gdx.app.error("DatabaseManager", "Failed to initialize database.", e);
            users = new Array<>();
            initialized = true;
        }
    }


    public static synchronized void dispose() {
        if (initialized) {
            save();
            Gdx.app.log("DatabaseManager", "Database saved and closed.");
        }
        users = null;
        initialized = false;
        nextId = 1;
    }
}
