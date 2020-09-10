package tache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.YoutubeException.BadPageException;
import com.github.kiulian.downloader.model.playlist.PlaylistVideoDetails;
import com.github.kiulian.downloader.model.playlist.YoutubePlaylist;

import prog.video.Video;
import prog.video.VideoYtb;

public class TacheChargerPlaylist extends Tache<List<Video>> {

	private String id;
	private boolean hasPb = false;
	private List<String> listeVideoError = new ArrayList<>();
	
	public TacheChargerPlaylist(String id) {
		this.id = id;
	}
	
	@Override
	protected List<Video> call() throws Exception {
		this.updateMessage("Téléchargement des vidéos");
		YoutubeDownloader downloader = new YoutubeDownloader();
		System.out.println("oui avant");
		YoutubePlaylist playlist = null;
		List<Video> liste = new ArrayList<>();
		try {
			playlist = downloader.getPlaylist(id);
		}
		catch(BadPageException e) {
			e.printStackTrace();
			hasPb = true;
			this.updateProgress(1, 1);
			return liste;
		}
		System.out.println("oui apres");
		List<PlaylistVideoDetails> videos = playlist.videos();
		int i = 0, max = videos.size();
		for(PlaylistVideoDetails video : videos) {
			Video vid = null;
			try {
				vid = new VideoYtb(video.videoId());
				this.updateMessage("Conversion de : " + vid.getTitre());
				liste.add(vid);
				this.updateProgress(++i, max);
			}
			catch(YoutubeException | IOException e) {
				listeVideoError.add(video.title());
				e.printStackTrace();
			}
		}
		return liste;
	}
	
	public boolean hasPb() {
		return hasPb;
	}
	
	public List<String> getLinkError(){
		return listeVideoError;
	}

}
