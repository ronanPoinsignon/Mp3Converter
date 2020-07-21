package prog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;
import com.github.kiulian.downloader.model.formats.Format;
import com.github.kiulian.downloader.model.quality.VideoQuality;

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
	public File download(File folder, String url, boolean goodVideo) throws YoutubeException, IOException {
		String id = Utils.getvideoId(url);
		return downloadFromId(folder, id, null, goodVideo);
	}
	
	public File download(File folder, String url, String title, boolean goodVideo) throws YoutubeException, IOException {
		String id = Utils.getvideoId(url);
		return downloadFromId(folder, id, title, goodVideo);
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
	private File downloadFromId(File folder, String videoId, String title, boolean goodVideo) throws YoutubeException, IOException {
		YoutubeDownloader downloader = new YoutubeDownloader();
		downloader.addCipherFunctionPattern(2, "\\b([a-zA-Z0-9$]{2})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)");
		downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
		downloader.setParserRetryOnFailure(1);

		YoutubeVideo video = downloader.getVideo(videoId);

		List<AudioVideoFormat> videoWithAudioFormats = video.videoWithAudioFormats();
		videoWithAudioFormats.forEach(vid -> System.out.println("qualité : " + vid.videoQuality()));
		
		Format format = null;
		if(goodVideo)
			format = getMaxVideoQuality(videoWithAudioFormats);
		else
			format = getMinVideoQuality(videoWithAudioFormats);
		URL website = new URL(format.url());
		String titre = title;
		if(titre == null || titre.isEmpty())
			titre = video.details().title();
		if(!folder.exists())
			folder.mkdirs();
		File fichierVideo = new File(folder.getPath() + "\\" + titre + ".mp4");
		try (InputStream in = website.openStream()) {
		    Files.copy(in, fichierVideo.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		return fichierVideo;
	}
	
	public Format getMaxVideoQuality(List<AudioVideoFormat> videoWithAudioFormats) {
		List<Format> listeFormat = new ArrayList<>();
		VideoQuality[] tabQuality = VideoQuality.values();
		for(VideoQuality quality : tabQuality) {
			if(quality != VideoQuality.unknown && quality != VideoQuality.noVideo) {
				videoWithAudioFormats.stream().filter(vid -> vid.videoQuality() == quality).forEach(listeFormat::add);
				if(!listeFormat.isEmpty())
					return listeFormat.get(0);
			}
		}
		videoWithAudioFormats.stream().filter(vid -> vid.videoQuality() == VideoQuality.unknown).forEach(listeFormat::add);
		if(!listeFormat.isEmpty())
			return listeFormat.get(0);
		videoWithAudioFormats.stream().filter(vid -> vid.videoQuality() == VideoQuality.noVideo).forEach(listeFormat::add);
		if(!listeFormat.isEmpty())
			return listeFormat.get(0);
		return null;
	}
	
	public Format getMinVideoQuality(List<AudioVideoFormat> videoWithAudioFormats) {
		List<Format> listeFormat = new ArrayList<>();
		VideoQuality[] tabQuality = inverser(VideoQuality.values());
		for(VideoQuality quality : tabQuality) {
			if(quality != VideoQuality.unknown && quality != VideoQuality.noVideo) {
				videoWithAudioFormats.stream().filter(vid -> vid.videoQuality() == quality).forEach(listeFormat::add);
				if(!listeFormat.isEmpty())
					return listeFormat.get(0);
			}
		}
		videoWithAudioFormats.stream().filter(vid -> vid.videoQuality() == VideoQuality.unknown).forEach(listeFormat::add);
		if(!listeFormat.isEmpty())
			return listeFormat.get(0);
		videoWithAudioFormats.stream().filter(vid -> vid.videoQuality() == VideoQuality.noVideo).forEach(listeFormat::add);
		if(!listeFormat.isEmpty())
			return listeFormat.get(0);
		return null;
	}
	
	public VideoQuality[] inverser(VideoQuality[] tabQuality) {
		VideoQuality[] tab = new VideoQuality[tabQuality.length];
		for(int i = tabQuality.length - 1; i >= 0; i--) {
			tab[tabQuality.length - i - 1] = tabQuality[i];
		}
		return tab;
	}
}