package com.github.ghomerl.chimken.model.runlog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RunLogDAO {

    private static final String LOG_FILE = "chimken_run_logs.json";

    private static Array<RunLog> logs;
    private static int nextId = 1;
    private static boolean initialized = false;

    private RunLogDAO() {
    }



    public static synchronized Array<RunLog> getLogs() {
        if (!initialized) {
            init();
        }
        return logs;
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
            String data = json.prettyPrint(logs);
            Gdx.files.local(LOG_FILE).writeString(data, false);
        } catch (Exception e) {
            Gdx.app.error("RunLogDAO", "Failed to save run logs.", e);
        }
    }


    public static synchronized void logRun(int userId, int score, int lastLevelReached) {
        if (!initialized) {
            init();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String timestamp = sdf.format(new Date());

        RunLog entry = new RunLog(getNextId(), timestamp, score, lastLevelReached, userId);
        logs.add(entry);
        save();

        Gdx.app.log("RunLogDAO", "Run logged: id=" + entry.getId()
            + ", userId=" + (entry.hasUser() ? entry.getUserId() : "guest")
            + ", score=" + score
            + ", lastLevelReached=" + lastLevelReached);
    }


    @Deprecated
    public static synchronized void logRun(int score, int lastLevelReached) {
        logRun(RunLog.NO_USER, score, lastLevelReached);
    }


    public static synchronized void dispose() {
        if (initialized) {
            save();
            Gdx.app.log("RunLogDAO", "Run log database saved and closed.");
        }
        logs = null;
        initialized = false;
        nextId = 1;
    }



    private static void init() {
        try {
            if (Gdx.files.local(LOG_FILE).exists()) {
                String data = Gdx.files.local(LOG_FILE).readString();
                if (data.trim().isEmpty()) {
                    logs = new Array<>();
                } else {
                    Json json = new Json();
                    json.setIgnoreUnknownFields(true);
                    logs = json.fromJson(Array.class, RunLog.class, data);
                    for (RunLog log : logs) {
                        if (log.getId() >= nextId) {
                            nextId = log.getId() + 1;
                        }
                    }
                }
            } else {
                logs = new Array<>();
            }
            initialized = true;
            Gdx.app.log("RunLogDAO", "Run log database initialized.");
        } catch (Exception e) {
            Gdx.app.error("RunLogDAO", "Failed to initialize run log database.", e);
            logs = new Array<>();
            initialized = true;
        }
    }
}
