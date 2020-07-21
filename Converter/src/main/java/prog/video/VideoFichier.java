package prog.video;

import java.io.File;

public class VideoFichier extends Video {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public VideoFichier() {

	}
	
	public VideoFichier(String titre, String lien){
		super(titre, lien);
	}

	@Override
	public File convertToMp4(File folder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File convertToMp4GoodQuality(File folder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
