package com.boredxgames.tictactoeclient.domain.managers.audio;

import java.util.prefs.Preferences;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {

    private static MediaPlayer musicPlayer;

    private static final Preferences prefs = Preferences.userNodeForPackage(AudioManager.class);

    private static final String FIRST_LAUNCH_KEY = "first_launch";
    private static final String MUSIC_ENABLED_KEY = "music_enabled";
    private static final String SFX_ENABLED_KEY = "sfx_enabled";
    private static final String MUSIC_VOLUME_KEY = "music_volume";
    private static final String SFX_VOLUME_KEY = "sfx_volume";

    private static boolean musicEnabled;
    private static boolean sfxEnabled;
    private static double musicVolume;
    private static double sfxVolume;

    static {
        boolean firstLaunch = prefs.getBoolean(FIRST_LAUNCH_KEY, true);

        if (firstLaunch) {
            musicEnabled = true;
            sfxEnabled = true;
            musicVolume = 0.5;
            sfxVolume = 0.5;

            prefs.putBoolean(MUSIC_ENABLED_KEY, true);
            prefs.putBoolean(SFX_ENABLED_KEY, true);
            prefs.putDouble(MUSIC_VOLUME_KEY, musicVolume);
            prefs.putDouble(SFX_VOLUME_KEY, sfxVolume);
            prefs.putBoolean(FIRST_LAUNCH_KEY, false);
        } else {
            musicEnabled = prefs.getBoolean(MUSIC_ENABLED_KEY, true);
            sfxEnabled = prefs.getBoolean(SFX_ENABLED_KEY, true);
            musicVolume = prefs.getDouble(MUSIC_VOLUME_KEY, 0.5);
            sfxVolume = prefs.getDouble(SFX_VOLUME_KEY, 0.5);
        }

        if (musicEnabled) playDefaultMusic();
    }

    // ===== Music =====
    public static void playMusic(String path, double volume) {
        stopMusic();
        Media media = new Media(AudioManager.class.getResource(path).toExternalForm());
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(volume);
        musicPlayer.play();
    }

    public static void playDefaultMusic() {
        if (musicEnabled) {
            playMusic("/assets/sounds/bgm.mp3", musicVolume);
        }
    }

    public static void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }
    }

    public static void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
        prefs.putBoolean(MUSIC_ENABLED_KEY, enabled);

        if (!enabled) stopMusic();
        else playDefaultMusic();
    }

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    public static void setMusicVolume(double volume) {
        musicVolume = volume;
        prefs.putDouble(MUSIC_VOLUME_KEY, volume);

        if (musicEnabled) {
            if (musicPlayer != null) musicPlayer.setVolume(volume);
            else playDefaultMusic();
        }
    }

    public static double getMusicVolume() {
        return musicVolume;
    }

    // ===== SFX =====
    public static void playSfx(String path) {
        playSfx(path, sfxVolume);
    }

    public static void playSfx(String path, double volume) {
        if (!sfxEnabled) return;
        MediaPlayer player = new MediaPlayer(new Media(AudioManager.class.getResource(path).toExternalForm()));
        player.setVolume(volume);
        player.play();
        player.setOnEndOfMedia(player::dispose);
    }

    public static void setSfxEnabled(boolean enabled) {
        sfxEnabled = enabled;
        prefs.putBoolean(SFX_ENABLED_KEY, enabled);
    }

    public static boolean isSfxEnabled() {
        return sfxEnabled;
    }

    public static void setSfxVolume(double volume) {
        sfxVolume = volume;
        prefs.putDouble(SFX_VOLUME_KEY, volume);
    }

    public static double getSfxVolume() {
        return sfxVolume;
    }

    // ===== Click Preview =====
    public static void playClickPreview() {
        if (!sfxEnabled) return;
        MediaPlayer player = new MediaPlayer(new Media(AudioManager.class.getResource("/assets/sounds/click.mp3").toExternalForm()));
        player.setVolume(sfxVolume);
        player.play();
        player.setOnEndOfMedia(player::dispose);
    }

    // ===== Attach click sound to entire Scene =====
    public static void attachGlobalClickSoundToScene(Scene scene) {
        if (scene == null) return;

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
           
            playClickPreview();
        });
    }
}
