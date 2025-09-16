import javax.sound.sampled.*;
import java.io.File;

public class Audio {
    public Audio() {
        try {
            File file = new File("C:/Users/anany/Downloads/tsubaki-soothing-calm-piano-music-144414.wav");
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file); // file inside resources
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // loop forever
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
