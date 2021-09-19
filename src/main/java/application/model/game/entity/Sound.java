package application.model.game.entity;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	private AudioInputStream audioIn;
	private Clip clip;

	public Sound(String name) {
		try {
			audioIn = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/application/sounds/" + name));
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			clip = null;
			e.printStackTrace();
		}
	}

	public void loop() {
		if (clip != null) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	public void start() {
		if (clip != null) {
			if (clip.getFramePosition() == clip.getFrameLength())
				clip.setFramePosition(0);
			clip.start();
		}
	}

	public void stop() {
		if (clip != null) {
			clip.stop();
		}
	}

	public void restart() {
		if (clip != null) {
			clip.stop();
			clip.setFramePosition(0);
			clip.start();
		}
	}

	public void reduceVolume() {
		if (clip != null) {
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float value = gainControl.getValue();
			value -= 15.0f;
			if (value >= gainControl.getMinimum())
				gainControl.setValue(value);
		}
	}

	public void incrementVolume() {
		if (clip != null) {
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float value = gainControl.getValue();
			value += 1.0f;
			if (value <= gainControl.getMaximum())
				gainControl.setValue(value);
		}
	}
}