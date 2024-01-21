package com.example.peck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LevelLoader {
    /**
     * Reads the level layout from a file and converts it to a character array.
     * @return A char array representing the level layout.
     */
    public static char[] readLevel(String level) throws IOException {
        if (level.isEmpty()) {
            throw new IllegalArgumentException("Level file path is empty.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = LevelLoader.class.getResourceAsStream(level);

        if (inputStream == null) {
            throw new IllegalArgumentException("Invalid level file: " + level + " does not exist");
        }

        if (inputStream.available() == 0) {
            throw new IllegalArgumentException("Empty level file: " + level);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        reader.lines().forEach(stringBuilder::append);

        return stringBuilder.toString().toCharArray();
    }
}
