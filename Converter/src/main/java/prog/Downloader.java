package prog;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;
import com.github.kiulian.downloader.model.formats.Format;

/**
 * Classe permettant le téléchargement de vidéos Youtube.
 * @author ronan
 *
 */
public class Downloader {

	public Downloader() {
		
	}
	
	/**
	 * Prépare le téléchargement.
	 * @param folder
	 * @param url
	 * @return
	 * @throws YoutubeException
	 * @throws IOException
	 */
	public File download(File folder, String url) throws YoutubeException, IOException {
		String id = Utils.getvideoId(url);
		return downloadFromId(folder, id);
	}
	
	/**
	 * Télécharge la vidéo Youtube en donnant le dossier de sauvegarde de la vidéo 
	 * ainsi que l'id de la vidéo à aller télécharger sur Youtube.
	 * @param folder
	 * @param videoId
	 * @return
	 * @throws YoutubeException
	 * @throws IOException
	 */
	private File downloadFromId(File folder, String videoId) throws YoutubeException, IOException {
		YoutubeDownloader downloader = new YoutubeDownloader();
		downloader.addCipherFunctionPattern(2, "\\b([a-zA-Z0-9$]{2})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)");
		downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
		downloader.setParserRetryOnFailure(1);

		YoutubeVideo video = downloader.getVideo(videoId);

		List<AudioVideoFormat> videoWithAudioFormats = video.videoWithAudioFormats();

		Format format = videoWithAudioFormats.get(0);

		return video.download(format, folder);
	}
	
	
}