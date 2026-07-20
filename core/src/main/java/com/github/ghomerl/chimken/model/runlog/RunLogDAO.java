package com.github.ghomerl.chimken.model.runlog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Data-access object for {@link RunLog} entries.
 * <p>
 * All run logs are persisted in a single JSON file
 * ({@code chimken_run_logs.json}) stored in the LibGDX local
 * filesystem.  Each completed run appends one entry.
 * <p>
 * Follows the same lazy-init / synchronized pattern as
 * {@link com.github.ghomerl.chimken.model.utils.DatabaseManager}.
 */
public class RunLogDAO {

    private static final String LOG_FILE = "chimken_run_logs.json";

    private static Array<RunLog> logs;
    private static int nextId = 1;
    private static boolean initialized = false;

    private RunLogDAO() {
    }

    // ══════════════════════════════════════════════════════════════
    //  Public API
    // ══════════════════════════════════════════════════════════════

    /**
     * Returns all stored run logs, initialising from disk on first call.
     */
    public static synchronized Array<RunLog> getLogs() {
        if (!initialized) {
            init();
        }
        return logs;
    }

    /**
     * Returns the next auto-incrementing run-log id.
     */
    public static synchronized int getNextId() {
        return nextId++;
    }

    /**
     * Persists the current in-memory log array to the JSON file.
     */
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

    /**
     * Creates and persists a new run-log entry.
     *
     * @param score            the player's final score for the run
     * @param lastLevelReached the last wave/level the player reached (1-based)
     */
    public static synchronized void logRun(int score, int lastLevelReached) {
        if (!initialized) {
            init();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String timestamp = sdf.format(new Date());

        RunLog entry = new RunLog(getNextId(), timestamp, score, lastLevelReached);
        logs.add(entry);
        save();

        Gdx.app.log("RunLogDAO", "Run logged: id=" + entry.getId()
                + ", score=" + score
                + ", lastLevelReached=" + lastLevelReached);
    }

    /**
     * Saves any pending logs and releases in-memory state.
     * Should be called during application shutdown.
     */
    public static synchronized void dispose() {
        if (initialized) {
            save();
            Gdx.app.log("RunLogDAO", "Run log database saved and closed.");
        }
        logs = null;
        initialized = false;
        nextId = 1;
    }

    // ══════════════════════════════════════════════════════════════
    //  Internal
    // ══════════════════════════════════════════════════════════════

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