package com.boredxgames.tictactoeclient.domain.managers.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class AudioManager {

    private static MediaPlayer bgmPlayer;
    private static double musicVolume = 0.5;
    private static double sfxVolume = 1.0;
    private static boolean sfxEnabled = true;

    // ===== Background Music =====
    public static void playMusic(String path, double volume) {
        stopMusic();
        try {
            URL resource = AudioManager.class.getResource(path);
            if (resource == null) {
                System.out.println("Failed to play music: Resource not found: " + path);
                return;
            }
            Media media = new Media(resource.toExternalForm());
            bgmPlayer = new MediaPlayer(media);
            bgmPlayer.setVolume(volume);
            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // تكرار مستمر
            bgmPlayer.play();
        } catch (Exception e) {
            System.out.println("Failed to play music: " + e.getMessage());
        }
    }

    public static void stopMusic() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer = null;
        }
    }

    public static void setMusicVolume(double volume) {
        musicVolume = volume;
        if (bgmPlayer != null) {
            bgmPlayer.setVolume(volume);
        }
    }

    // ===== Sound Effects =====
    public static void playSfx(String path, double volume) {
        if (!sfxEnabled) return;
        try {
            URL resource = AudioManager.class.getResource(path);
            if (resource == null) {
                System.out.println("Failed to play SFX: Resource not found: " + path);
                return;
            }
            AudioClip clip = new AudioClip(resource.toExternalForm());
            clip.setVolume(volume * sfxVolume);
            clip.play();
        } catch (Exception e) {
            System.out.println("Failed to play SFX: " + e.getMessage());
        }
    }

    public static void setSfxEnabled(boolean enabled) {
        sfxEnabled = enabled;
    }

    public static void setSfxVolume(double volume) {
        sfxVolume = volume;
    }
}
