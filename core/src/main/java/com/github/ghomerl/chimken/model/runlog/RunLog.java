package com.github.ghomerl.chimken.model.runlog;


public class RunLog {


    public static final int NO_USER = -1;

    private int id;
    private String timestamp;
    private int score;
    private int lastLevelReached;


    private int userId = NO_USER;


    public RunLog() {
    }


    @Deprecated
    public RunLog(int id, String timestamp, int score, int lastLevelReached) {
        this(id, timestamp, score, lastLevelReached, NO_USER);
    }


    public RunLog(int id, String timestamp, int score, int lastLevelReached, int userId) {
        this.id = id;
        this.timestamp = timestamp;
        this.score = score;
        this.lastLevelReached = lastLevelReached;
        this.userId = userId;
    }


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


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public boolean hasUser() {
        return userId != NO_USER;
    }
}
