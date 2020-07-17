package prog;

import java.io.File;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

/**
 * Classe de conversion de fichier mp4 en mp3.
 * @author ronan
 *
 */
public class Converter {

	public Converter() {

	}
	
	/**
	 * Convertit un fichier source mp4 en mp3 et le met dans le dossier donné.
	 * @param input
	 * @param output
	 * @param bitRate
	 * @throws IllegalArgumentException
	 * @throws InputFormatException
	 * @throws EncoderException
	 */
	public void encode(File input, File output, int bitRate) throws IllegalArgumentException, InputFormatException, EncoderException {
		Encoder encoder = new Encoder();
		AudioAttributes audio	= new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(bitRate));
		audio.setChannels(new Integer(2));
		audio.setSamplingRate(new Integer(44100));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);
		encoder.encode(input, output, attrs);
	}
}
