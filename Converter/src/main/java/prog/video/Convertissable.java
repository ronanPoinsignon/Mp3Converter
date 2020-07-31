package prog.video;

import java.io.File;

/**
 * interface permettant la conversion de {@link Video}.
 * @author ronan
 *
 */
public interface Convertissable {

	public abstract File convertToMp4(File folder) throws Exception;
	public abstract File convertToMp4GoodQuality(File folder) throws Exception;
}
