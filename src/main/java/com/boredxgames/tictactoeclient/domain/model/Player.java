package com.boredxgames.tictactoeclient.domain.model;

/**
 * @author Tasneem
 */
public class Player {
    private String id;
    private String username;
    private int score;

    public Player(String id, String username, int score) {
        this.id = id;
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
