package prog.convertisseur;

import java.io.File;

import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;

public class ConvertisseurMusique extends Convertisseur {
	
	public ConvertisseurMusique(int audioBitRate) {
		super("mp3", audioBitRate, true);
	}
	
	public File convertir(File input, File output) throws IllegalArgumentException, InputFormatException, EncoderException {
		if(input.getPath().equals(output.getPath()))
			return input;
		return this.convertirToMusique(input, output);
	}

}
