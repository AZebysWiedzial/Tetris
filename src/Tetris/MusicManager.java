package Tetris;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Paths;

public class MusicManager {
    static MediaPlayer mediaPlayer;
    public static void playMusic(String url)
    {
        Media media = new Media(Paths.get(url).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });
        mediaPlayer.play();
    }
    public static void stopMusic()
    {
        mediaPlayer.stop();
    }
}
