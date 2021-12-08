package prog.video;

import java.io.File;
import java.io.IOException;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;

import affichage.demande.TableVideo;
import exception.NoVideoFoundException;
import prog.Downloader;
import prog.Utils;

/**
 * Classe utilisée par {@link TableVideo} pour l'affichage et la modification
 * de la liste des vidéos données par l'utilisateur.
 * @author ronan
 *
 */
public class VideoYtb extends Video {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	public VideoYtb(String url) throws YoutubeException, IOException {
		YoutubeDownloader downloader = new YoutubeDownloader();
		//downloader.addCipherFunctionPattern(2, "\\b([a-zA-Z0-9$]{2})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)");
		//downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
		//downloader.setParserRetryOnFailure(1);
		lien = url;
		id = Utils.getvideoId(url);
		RequestVideoInfo request = new RequestVideoInfo(id);
		Response<VideoInfo> response = downloader.getVideoInfo(request);
		VideoInfo video = response.data();
		titre = video.details().title();
	}

	@Override
	public File convertToMp4(File folder) throws YoutubeException, IOException, NoVideoFoundException {
		Downloader downloader = new Downloader();
		return downloader.download(folder, id, titre, false);
	}

	@Override
	public File convertToMp4GoodQuality(File folder) throws YoutubeException, IOException, NoVideoFoundException {
		Downloader downloader = new Downloader();
		return downloader.download(folder, id, titre, true);
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String getTitre() {
		return titre;
	}

	/**
	 *
	 * @return
	 */
	public String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (titre == null ? 0 : titre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		VideoYtb other = (VideoYtb) obj;
		if(id.equals(other.getId()) && titre.equals(other.getTitre())) {
			return true;
		} else {
			return false;
		}
	}

}
