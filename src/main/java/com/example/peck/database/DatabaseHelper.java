package com.example.peck.database;

import com.example.peck.HighScore;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.peck.util.PasswordValidator.*;

/**
 * Handles database operations for the application.
 */
public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:pacman.db";

    /**
     * Initializes the database with required tables.
     */
    public static void initDB() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");

                Statement stmt = conn.createStatement();
                // SQL statement for creating a new table
                String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                        + " id integer PRIMARY KEY AUTOINCREMENT,\n"
                        + " username text NOT NULL UNIQUE,\n"
                        + " password text NOT NULL,\n"
                        + " highscore bigint NOT NULL DEFAULT 0\n"
                        + ");";
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Registers a new user in the database.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return true if registration is successful, false otherwise.
     */
    public static boolean registerUser(String username, String password) {
        username = username.toUpperCase();

        if (!isPasswordValid(password)) {
            System.out.println("Invalid password format.");
            return false;
        }

        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // Hash the password using PBKDF2 with SHA256 and Bouncy Castle
        Digest digest = new SHA256Digest();
        PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator(digest);
        generator.init(PKCS5S2ParametersGenerator.PKCS5PasswordToUTF8Bytes(password.toCharArray()), salt, 10000);
        byte[] hash = ((KeyParameter) generator.generateDerivedParameters(256)).getKey();

        // Store the hash and the salt
        String saltHex = Hex.toHexString(salt);
        String hashHex = Hex.toHexString(hash);
        String storedValue = saltHex + ":" + hashHex;

        // SQL statement to insert a new user with a hashed password and salt
        String sql = "INSERT INTO users(username, password) VALUES(?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, storedValue);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the provided login credentials are valid.
     *
     * @param username The username to check.
     * @param password The password to check.
     * @return true if the credentials are valid, false otherwise.
     */
    public static boolean checkLogin(String username, String password) {
        username = username.toUpperCase();
        // SQL statement to get the hashed password and salt for the user
        String sql = "SELECT password FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedValue = rs.getString("password");
                String[] parts = storedValue.split(":");
                byte[] salt = Hex.decode(parts[0]);
                byte[] hash = Hex.decode(parts[1]);

                // Hash the password with the retrieved salt
                Digest digest = new SHA256Digest();
                PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator(digest);
                generator.init(PKCS5S2ParametersGenerator.PKCS5PasswordToUTF8Bytes(password.toCharArray()), salt, 10000);
                byte[] computedHash = ((KeyParameter) generator.generateDerivedParameters(256)).getKey();

                // Compare the stored hash with the computed hash
                return java.util.Arrays.equals(hash, computedHash);
            }
            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    /**
     * Retrieves a list of high scores from the database.
     *
     * @return List of high scores.
     */
    public static List<HighScore> getHighScores() {
        List<HighScore> highScores = new ArrayList<>();
        String sql = "SELECT username, highscore FROM users ORDER BY highscore DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String username = rs.getString("username");
                long score = rs.getLong("highscore");
                highScores.add(new HighScore(username, score));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return highScores;
    }


    /**
     * Updates the high score for a specific user.
     *
     * @param username The username of the user.
     * @param newScore The new high score to be updated.
     * @return true if the update is successful, false otherwise.
     */
    public static boolean updateHighScore(String username, long newScore) {
        String query = "SELECT highscore FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmtQuery = conn.prepareStatement(query)) {
            pstmtQuery.setString(1, username);
            ResultSet rs = pstmtQuery.executeQuery();
            if (rs.next()) {
                long currentHighScore = rs.getLong("highscore");
                if (newScore > currentHighScore) {
                    // Update the score since the new score is higher
                    String update = "UPDATE users SET highscore = ? WHERE username = ?";
                    try (PreparedStatement pstmtUpdate = conn.prepareStatement(update)) {
                        pstmtUpdate.setLong(1, newScore);
                        pstmtUpdate.setString(2, username);
                        int affectedRows = pstmtUpdate.executeUpdate();
                        return affectedRows > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}

