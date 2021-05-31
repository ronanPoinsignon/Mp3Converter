package prog.convertisseur;

import java.io.File;

import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

/**
 * Convertisseur pour la création de fichiers mp3.
 * @author ronan
 *
 */
public class ConvertisseurMusique extends Convertisseur {

	public ConvertisseurMusique(int audioBitRate) {
		super("mp3", audioBitRate, true);
	}

	@Override
	public File convertir(File input, File output) throws IllegalArgumentException, EncoderException {
		if(input.getPath().equals(output.getPath())) {
			return input;
		}
		return convertirToMusique(input, output);
	}

	protected File convertirToMusique(File input, File output) throws IllegalArgumentException, EncoderException {
		EncodingAttributes attrs = new EncodingAttributes();
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(audioBitRate);
		audio.setChannels(2);
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		MultimediaObject multimediaObject = new MultimediaObject(input);
		encoder.encode(multimediaObject, output, attrs);
		return output;
	}

}
