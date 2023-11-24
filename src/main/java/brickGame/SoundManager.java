package brickGame;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;

public class SoundManager {
    public enum MusicType {
        BRICK_BREAK,
        LOSE,
        WIN,
        BG_MUSIC,
    }
    private MediaPlayer mediaPlayer;

    private Duration lastStop;

    private boolean isMute;


    public SoundManager(URL musicFilePath, MusicType musicType) {
        Media sound = new Media(musicFilePath.toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.5);

        if(musicType == MusicType.BG_MUSIC){
            // Set up the event handler for looping
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(javafx.util.Duration.ZERO));
        }


    }

    public void play() {
        mediaPlayer.play();
    }

    public void resume() {
        mediaPlayer.seek(lastStop);
        mediaPlayer.play();
    }
    public void stop() {
        mediaPlayer.stop();
        lastStop =  mediaPlayer.getCurrentTime();
    }

    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    public void mute() {
        mediaPlayer.setVolume(0);
        isMute = true;
    }

    public void unMute() {
        mediaPlayer.setVolume(0.5);
        isMute = false;
    }

    public boolean getIsMute(){
        return isMute;
    }



}