package reminder.util;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicLoader {

	public static File sound;
//	private float value;
	private Clip clip;

	public MusicLoader() {
//		value = 0f;
	}

	public void load() {
		sound = new File("src/main/resources/Music/Bird Alarm.wav");
	}

	public void play(File file) {
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sound));

//			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//			gainControl.setValue(value);

			clip.start();
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		clip.close();
	}

}
