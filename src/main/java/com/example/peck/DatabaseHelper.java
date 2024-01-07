package com.example.peck;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

import java.security.SecureRandom;
import java.sql.*;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:pacman.db";

    // Initialize the database
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

    public static boolean registerUser(String username, String password) {
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

    public static boolean checkLogin(String username, String password) {
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
}

