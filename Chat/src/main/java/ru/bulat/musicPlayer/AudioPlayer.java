package ru.bulat.musicPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class AudioPlayer implements LineListener {
	private static final int SECONDS_IN_HOUR = 60 * 60;
	private static final int SECONDS_IN_MINUTE = 60;

	private boolean playCompleted;

	private boolean isStopped;

	private boolean isPaused;

	private Clip audioClip;

	void load(String audioFilePath)
			throws UnsupportedAudioFileException, IOException,
			LineUnavailableException {
		File audioFile = new File(audioFilePath);

		AudioInputStream audioStream = AudioSystem
				.getAudioInputStream(audioFile);

		AudioFormat format = audioStream.getFormat();

		DataLine.Info info = new DataLine.Info(Clip.class, format);

		audioClip = (Clip) AudioSystem.getLine(info);

		audioClip.addLineListener(this);

		audioClip.open(audioStream);
	}
	
	long getClipSecondLength() {
		return audioClip.getMicrosecondLength() / 1_000_000;
	}
	
	String getClipLengthString() {
		String length = "";
		long hour = 0;
		long minute;
		long seconds = audioClip.getMicrosecondLength() / 1_000_000;
		
		if (seconds >= SECONDS_IN_HOUR) {
			hour = seconds / SECONDS_IN_HOUR;
			length = String.format("%02d:", hour);
		} else {
			length += "00:";
		}
		
		minute = seconds - hour * SECONDS_IN_HOUR;
		if (minute >= SECONDS_IN_MINUTE) {
			minute = minute / SECONDS_IN_MINUTE;
			length += String.format("%02d:", minute);
			
		} else {
			minute = 0;
			length += "00:";
		}
		
		long second = seconds - hour * SECONDS_IN_HOUR - minute * SECONDS_IN_MINUTE;
		
		length += String.format("%02d", second);
		
		return length;
	}

	void play(){

		audioClip.start();

		playCompleted = false;
		isStopped = false;

		while (!playCompleted) {
			// wait for the playback completes
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				if (isStopped) {
					audioClip.stop();
					break;
				}
				if (isPaused) {
					audioClip.stop();
				} else {
					audioClip.start();
				}
			}
		}

		audioClip.close();

	}

	void stop() {
		isStopped = true;
	}

	void pause() {
		isPaused = true;
	}

	void resume() {
		isPaused = false;
	}

	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();
		if (type == LineEvent.Type.STOP) {
			if (isStopped || !isPaused) {
				playCompleted = true;
			}
		}
	}
	
	Clip getAudioClip() {
		return audioClip;
	}	
}