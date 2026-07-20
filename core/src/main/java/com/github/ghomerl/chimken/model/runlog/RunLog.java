package com.github.ghomerl.chimken.model.runlog;

/**
 * A single persisted run record.
 * <p>
 * Each completed run (win or loss) produces one {@code RunLog} entry
 * that is appended to {@code chimken_run_logs.json} via
 * {@link com.github.ghomerl.chimken.model.runlog.RunLogDAO}.
 * <p>
 * Fields are JSON-serialisable by LibGDX's {@code Json} utility.
 */
public class RunLog {

    private int id;
    private String timestamp;
    private int score;
    private int lastLevelReached;

    // ══════════════════════════════════════════════════════════════
    //  Constructors
    // ══════════════════════════════════════════════════════════════

    /**
     * No-arg constructor required for LibGDX JSON deserialization.
     */
    public RunLog() {
    }

    public RunLog(int id, String timestamp, int score, int lastLevelReached) {
        this.id = id;
        this.timestamp = timestamp;
        this.score = score;
        this.lastLevelReached = lastLevelReached;
    }

    // ══════════════════════════════════════════════════════════════
    //  Accessors
    // ══════════════════════════════════════════════════════════════

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLastLevelReached() {
        return lastLevelReached;
    }

    public void setLastLevelReached(int lastLevelReached) {
        this.lastLevelReached = lastLevelReached;
    }
}