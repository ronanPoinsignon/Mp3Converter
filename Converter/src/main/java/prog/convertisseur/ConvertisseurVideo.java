package prog.convertisseur;

import java.io.File;

import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;

public class ConvertisseurVideo extends Convertisseur {
		
	public ConvertisseurVideo(String extension, int audioBitRate, boolean hasSound) {
		super(extension, audioBitRate, hasSound);
	}
	
	@Override
	public File convertir(File input, File output) throws IllegalArgumentException, InputFormatException, EncoderException {
		if(input.getPath().equals(output.getPath()))
			return input;
		return this.convertirToVideo(input, output);
	}
	
}
