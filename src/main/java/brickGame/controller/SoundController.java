package brickGame.controller;

import javafx.beans.binding.Bindings;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import static brickGame.Constants.SOUND_FILE_PATH;

public class SoundController {

    private static SoundController instance;
    private MediaPlayer mediaPlayer;
    private Duration currentMediaTime;

    private SoundController() {
        // Private constructor to prevent direct instantiation
    }

    public static synchronized SoundController getInstance() {
        if (instance == null) {
            instance = new SoundController();
            instance.initializeMediaPlayer();
        }
        return instance;
    }

    private void initializeMediaPlayer() {
        Media sound = new Media(getClass().getResource(SOUND_FILE_PATH).toExternalForm());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            currentMediaTime = mediaPlayer.getCurrentTime();
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            mediaPlayer.seek(currentMediaTime);
            mediaPlayer.play();
        }
    }

    public void dispose() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
    }

    public void bindVolumeSlider(Slider volumeSlider) {
        // Bind the value of the Slider to the volume property of the MediaPlayer
        volumeSlider.valueProperty().bindBidirectional(mediaPlayer.volumeProperty());
    }

    public void bindMuteCheckbox(CheckBox muteCheckBox) {
        muteCheckBox.setOnAction(event -> {
            if (muteCheckBox.isSelected()) {
                pause();
            } else {
                resume();
            }
        });
    }


    public MediaPlayer.Status getStatus(){
        return mediaPlayer.getStatus();
    }
}
