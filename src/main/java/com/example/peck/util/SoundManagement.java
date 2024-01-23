package com.example.peck.util;

import javafx.scene.media.MediaPlayer;

import static com.example.peck.config.Variables.soundsMuted;
import static com.example.peck.config.Variables.soundsVolume;

public class SoundManagement {
    public static void applySoundSettings(MediaPlayer player) {
        if (soundsMuted) {
            player.setVolume(0.0);
        } else {
            player.setVolume(soundsVolume);
        }
    }
}
