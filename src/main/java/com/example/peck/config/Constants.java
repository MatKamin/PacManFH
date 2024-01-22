package com.example.peck.config;

/**
 * Constants for application configuration.
 */
public class Constants {

    // Disables the splash screen.
    public static final boolean SPLASH_DISABLED = false;

    // Grid dimension constants.
    public static final int TILE_SIZE = 25; // 20 - 25 should be fine

    public static final int GRID_WIDTH = 30;
    public static final int GRID_HEIGHT = 33;

    public static final int WINDOW_WIDTH = GRID_WIDTH * TILE_SIZE;
    public static final int WINDOW_HEIGHT = GRID_HEIGHT * TILE_SIZE;
    // Characters representing different wall types.
    public static final char[] WALLS = {'H', 'R', 'L', 'U', 'D', 'V'};

}

