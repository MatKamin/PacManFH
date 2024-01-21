package com.example.peck;

/**
 * Represents a user's high score with a username and score value.
 */
public class HighScore {
    private String username;
    private long score;


    /**
     * Creates a new HighScore instance with the given username and score.
     *
     * @param username The username associated with the high score.
     * @param score    The score achieved by the user.
     */
    public HighScore(String username, long score) {
        this.username = username;
        this.score = score;
    }

    /**
     * Retrieves the username associated with the high score.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the score achieved by the user.
     *
     * @return The score.
     */
    public long getScore() {
        return score;
    }

    /**
     * Returns a string representation of the high score in the format "username: score".
     *
     * @return The string representation of the high score.
     */
    @Override
    public String toString() {
        return username + ": " + score;
    }
}
