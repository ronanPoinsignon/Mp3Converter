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
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import com.github.kiulian.downloader.model.videos.formats.VideoWithAudioFormat;
import com.github.kiulian.downloader.model.videos.quality.VideoQuality;

import exception.NoVideoFoundException;

/**
 * Classe permettant le téléchargement de vidéos Youtube.
 * @author ronan
 *
 */
public class Downloader {

	/**
	 * Prépare le téléchargement.
	 * @param folder
	 * @param url
	 * @return
	 * @throws YoutubeException
	 * @throws IOException
	 * @throws NoVideoFoundException
	 */
	public File download(File folder, String url, boolean goodVideo) throws IOException, NoVideoFoundException {
		String id = Utils.getvideoId(url);
		return downloadFromId(folder, id, null, goodVideo);
	}

	public File download(File folder, String url, String title, boolean goodVideo) throws IOException, NoVideoFoundException {
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
	 * @throws NoVideoFoundException
	 */
	public File downloadFromId(File folder, String videoId, String title, boolean goodVideo) throws IOException, NoVideoFoundException {
		YoutubeDownloader downloader = new YoutubeDownloader();

		RequestVideoInfo request = new RequestVideoInfo(videoId);
		Response<VideoInfo> response = downloader.getVideoInfo(request);
		VideoInfo video = response.data();

		List<VideoWithAudioFormat> videoWithAudioFormats = video.videoWithAudioFormats();
		VideoFormat format = null;
		if(goodVideo) {
			format = getMaxVideoQuality(videoWithAudioFormats); //return the better video quality -> throw NoVideoFoundException if no video found
		}
		else {
			format = getMinVideoQuality(videoWithAudioFormats); //return the worst video quality -> throw NoVideoFoundException if no video found
		}
		URL website = new URL(format.url());
		String titre = title;
		if(titre == null) {
			titre = video.details().title();
		}
		if(!folder.exists()) {
			folder.mkdirs();
		}
		titre = Utils.removeIllegalChars(titre);
		File fichierVideo = new File(folder.getPath() + File.separator + titre + ".mp4");
		try (InputStream in = website.openStream()) {
			Files.copy(in, fichierVideo.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		return fichierVideo;
	}

	public VideoFormat getMaxVideoQuality(List<VideoWithAudioFormat> videoWithAudioFormats) throws NoVideoFoundException {
		List<VideoFormat> listeFormat = new ArrayList<>();
		VideoQuality[] tabQuality = VideoQuality.values();
		for(VideoQuality quality : tabQuality) {
			if(quality != VideoQuality.unknown && quality != VideoQuality.noVideo) {
				videoWithAudioFormats.stream().filter(vid -> vid.videoQuality() == quality).forEach(listeFormat::add);
				if(!listeFormat.isEmpty()) {
					return listeFormat.get(0);
				}
			}
		}
		videoWithAudioFormats.stream().filter(vid -> vid.videoQuality() == VideoQuality.unknown).forEach(listeFormat::add);
		if(!listeFormat.isEmpty()) {
			return listeFormat.get(0);
		}
		videoWithAudioFormats.stream().filter(vid -> vid.videoQuality() == VideoQuality.noVideo).forEach(listeFormat::add);
		if(!listeFormat.isEmpty()) {
			return listeFormat.get(0);
		}
		throw new NoVideoFoundException();
	}

	public VideoFormat getMinVideoQuality(List<VideoWithAudioFormat> videoWithAudioFormats) throws NoVideoFoundException {
		List<VideoFormat> listeFormat = new ArrayList<>();
		VideoQuality[] tabQuality = inverser(VideoQuality.values());
		for(VideoQuality quality : tabQuality) {
			if(quality != VideoQuality.unknown && quality != VideoQuality.noVideo) {
				videoWithAudioFormats.stream().filter(vid -> vid.videoQuality() == quality).forEach(listeFormat::add);
				if(!listeFormat.isEmpty()) {
					return listeFormat.get(0);
				}
			}
		}
		videoWithAudioFormats.stream().filter(vid -> vid.videoQuality() == VideoQuality.unknown).forEach(listeFormat::add);
		if(!listeFormat.isEmpty()) {
			return listeFormat.get(0);
		}
		videoWithAudioFormats.stream().filter(vid -> vid.videoQuality() == VideoQuality.noVideo).forEach(listeFormat::add);
		if(!listeFormat.isEmpty()) {
			return listeFormat.get(0);
		}
		throw new NoVideoFoundException();
	}

	public VideoQuality[] inverser(VideoQuality[] tabQuality) {
		VideoQuality[] tab = new VideoQuality[tabQuality.length];
		for(int i = tabQuality.length - 1; i >= 0; i--) {
			tab[tabQuality.length - i - 1] = tabQuality[i];
		}
		return tab;
	}
}