package brickGame.controller;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import static brickGame.Constants.SOUND_FILE_PATH;


/**
 * The {@code SoundController} class manages audio playback in the brick game, providing controls
 * for playing, stopping, pausing, resuming, and adjusting volume. It uses the JavaFX {@code MediaPlayer}
 * for audio playback.
 * <p>
 * The class follows the Singleton pattern, ensuring that only one instance of the {@code SoundController}
 * exists during the game's lifecycle. The class initializes the {@code MediaPlayer} with the sound file
 * specified in the {@code SOUND_FILE_PATH} constant.
 * </p>
 * <p>
 * It provides methods for playing, stopping, pausing, and resuming audio playback. The {@code bindVolumeSlider}
 * method allows binding a {@code Slider} to the volume property of the {@code MediaPlayer}, enabling volume control.
 * The {@code bindMuteCheckbox} method allows binding a {@code CheckBox} to mute/unmute audio by pausing/resuming.
 * </p>
 * <p>
 * The class also provides a method to check the current status of the {@code MediaPlayer}.
 * </p>
 * <p>
 * The {@code SoundController} class is designed as a singleton, and the instance is created lazily to ensure
 * efficiency and avoid unnecessary initialization when the class is not in use.
 * </p>
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/controller/SoundController.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public class SoundController {

    /**
     * The single instance of the {@code SoundController}.
     */
    private static SoundController instance;

    /**
     * The {@code MediaPlayer} responsible for audio playback.
     */
    private MediaPlayer mediaPlayer;

    /**
     * The current playback time of the media, used for pausing and resuming.
     */
    private Duration currentMediaTime;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private SoundController() {
    }

    /**
     * Retrieves the single instance of the {@code SoundController} using the Singleton pattern.
     *
     * @return The single instance of the {@code SoundController}.
     */
    public static synchronized SoundController getInstance() {
        if (instance == null) {
            instance = new SoundController();
            instance.initializeMediaPlayer();
        }
        return instance;
    }

    /**
     * Initializes the {@code MediaPlayer} with the sound file specified in {@code SOUND_FILE_PATH}.
     */
    private void initializeMediaPlayer() {
        Media sound = new Media(getClass().getResource(SOUND_FILE_PATH).toExternalForm());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
    }

    /**
     * Starts playing the audio if the {@code MediaPlayer} is not null.
     */
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    /**
     * Stops the audio if the {@code MediaPlayer} is not null.
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * Pauses the audio if the {@code MediaPlayer} is not null and currently playing.
     */
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            currentMediaTime = mediaPlayer.getCurrentTime();
            mediaPlayer.pause();
        }
    }

    /**
     * Resumes the audio if the {@code MediaPlayer} is not null and currently paused.
     */
    public void resume() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            mediaPlayer.seek(currentMediaTime);
            mediaPlayer.play();
        }
    }

    /**
     * Disposes of the resources used by the {@code MediaPlayer} if not null.
     */
    public void dispose() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
    }

    /**
     * Binds the value of the provided {@code Slider} to the volume property of the {@code MediaPlayer}.
     *
     * @param volumeSlider The Slider for adjusting the volume.
     */
    public void bindVolumeSlider(Slider volumeSlider) {
        volumeSlider.valueProperty().bindBidirectional(mediaPlayer.volumeProperty());
    }

    /**
     * Binds the provided {@code CheckBox} to mute/unmute audio by pausing/resuming.
     *
     * @param muteCheckBox The CheckBox for muting/unmuting audio.
     */
    public void bindMuteCheckbox(CheckBox muteCheckBox) {
        muteCheckBox.setOnAction(event -> {
            if (muteCheckBox.isSelected()) {
                pause();
            } else {
                resume();
            }
        });
    }

    /**
     * Retrieves the current status of the {@code MediaPlayer}.
     *
     * @return The current status of the {@code MediaPlayer}.
     */
    public MediaPlayer.Status getStatus() {
        return mediaPlayer.getStatus();
    }
}
