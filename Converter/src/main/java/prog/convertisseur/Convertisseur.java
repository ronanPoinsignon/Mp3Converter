package prog.convertisseur;

import java.io.File;

import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.VideoAttributes;

/**
 * classe abstraite pour la conversion de fichiers en vidéo / audio.
 * @author ronan
 *
 */
public abstract class Convertisseur {

	protected String extension;
	protected int audioBitRate;
	protected boolean hasSound;

	protected Convertisseur(String extension, int audioBitRate, boolean hasSound) {
		this.extension = extension;
		this.audioBitRate = audioBitRate;
		this.hasSound = hasSound;
	}

	public abstract File convertir(File input, File output) throws Exception;

	/**
	 * Enlève le son de la vidéo.
	 * @param input
	 * @param output
	 * @return
	 * @throws IllegalArgumentException
	 * @throws InputFormatException
	 * @throws EncoderException
	 */
	protected File convertirToVideoSansSon(File input, File output) throws IllegalArgumentException, EncoderException {
		EncodingAttributes attrs = new EncodingAttributes();
		VideoAttributes video = new VideoAttributes();
		video.setCodec("libx264");
		video.setBitRate(80000);
		video.setFrameRate(15);
		attrs.setFormat(extension);
		attrs.setVideoAttributes(video);
		Encoder encoder = new Encoder();
		MultimediaObject multimediaObject = new MultimediaObject(input);
		encoder.encode(multimediaObject, output, attrs);
		return output;
	}

	/**
	 * Convertit le fichier donné en vidéo.
	 * @param input
	 * @param output
	 * @return
	 * @throws IllegalArgumentException
	 * @throws InputFormatException
	 * @throws EncoderException
	 */
	protected File convertirToVideo(File input, File output) throws IllegalArgumentException, EncoderException {
		EncodingAttributes attrs = new EncodingAttributes();
		if(audioBitRate > 0) {
			AudioAttributes audio = new AudioAttributes();
			audio.setCodec("libmp3lame");
			audio.setBitRate(audioBitRate);
			audio.setChannels(2);
			attrs.setAudioAttributes(audio);
		}
		VideoAttributes video = new VideoAttributes();
		video.setCodec("libx264");
		video.setBitRate(800000);
		video.setFrameRate (15);
		attrs.setFormat(extension);
		attrs.setVideoAttributes(video);
		Encoder encoder = new Encoder();
		MultimediaObject multimediaObject = new MultimediaObject(input);
		encoder.encode(multimediaObject, output, attrs);
		return output;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public boolean isHasSound() {
		return hasSound;
	}

	public void setHasSound(boolean hasSound) {
		this.hasSound = hasSound;
	}

}
