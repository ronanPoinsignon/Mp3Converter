package prog.video;

import java.io.File;

public interface Convertissable {

	public abstract File convertToMp4(File folder) throws Exception;
	public abstract File convertToMp4GoodQuality(File folder) throws Exception;
}
