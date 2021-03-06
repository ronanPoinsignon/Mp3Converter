package tache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import event.tache.event.EventTacheUpdateMessage;
import event.tache.handler.EventHandlerTacheUpdateMessage;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import log.Logger;
import prog.video.Video;

/**
 * {@link Tache} permettant une conversion instantannée en vidéo depuis un lien donné sans passer par les actions 
 * normales d'ajout dans la liste d'une vidéo puis de conversion.
 * @author ronan
 *
 */
public class TacheConvertirInstant extends Tache<Video> {

	private String url;
	private File folder;
	private int bitRate;
	private List<String> listeExtensions;
	private List<Video> listeVideos = new ArrayList<>();
	
	public TacheConvertirInstant(String url, File folder, int bitRate, List<String> listeExtensions) {
		this.url = url;
		this.folder = folder;
		this.bitRate = bitRate;
		this.listeExtensions = listeExtensions;
	}
	
	@Override
	protected Video call() throws Exception {
		TacheCharger tache = new TacheCharger(url);
    	TacheConvertirToFile tacheConv = new TacheConvertirToFile(listeVideos, folder, bitRate, listeExtensions);
    	
		tache.addEventHandler(EventTacheUpdateMessage.EVENT_UPDATE_MESSAGE, new EventHandlerTacheUpdateMessage() {
			@Override
			public void onUpdateMessage(String message) {
				TacheConvertirInstant.this.updateMessage(message);
			}
		});
		tache.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
            	List<Video> liste = tache.getValue();
                listeVideos.addAll(liste);
                if(!listeVideos.isEmpty()) {
                	new Thread(tacheConv).start();
                }
        		Logger.getInstance().showErrorAlertVideosNonChargees(tache.getListeUrlsMauvaisLien(), tache.getListeUrlsErreur());
            }
        });
		

    	tacheConv.addEventHandler(EventTacheUpdateMessage.EVENT_UPDATE_MESSAGE, new EventHandlerTacheUpdateMessage() {
			@Override
			public void onUpdateMessage(String message) {
				TacheConvertirInstant.this.updateMessage(message);
			}
		});
    	tacheConv.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
            	TacheConvertirInstant.this.updateMessage("Vidéo convertie");
            	TacheConvertirInstant.this.updateProgress(1, 1);
            }
    	});
		
		new Thread(tache).start();
		return null;
	}

}
