package tache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import event.tache.event.EventTacheUpdateMessage;
import event.tache.event.EventTacheUpdated;
import event.tache.handler.EventHandlerTacheUpdateMessage;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import log.Logger;
import prog.video.Video;

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
		tache.addEventHandler(EventTacheUpdated.EVENT_UPDATE, new EventHandler<EventTacheUpdated>() {

			@Override
			public void handle(EventTacheUpdated event) {
				System.out.println(event.getEventType());
			}
		});
		/*tache.addEventHandler(EventTacheUpdateProgress.EVENT_UPDATE_PROGRESS, new EventHandlerTacheUpdateProgress() {
			@Override
			public void onUpdateProgress(long workDone, long max) {
				TacheConvertirInstant.this.updateProgress(workDone, max);
			}
		});*/
		tache.addEventHandler(EventTacheUpdateMessage.EVENT_UPDATE_MESSAGE, new EventHandlerTacheUpdateMessage() {
			@Override
			public void onUpdateMessage(String message) {
				TacheConvertirInstant.this.updateMessage(message);
			}
		});
		tache.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
            	System.out.println("fini");
                listeVideos = tache.getValue();
                if(!listeVideos.isEmpty()) {
                	TacheConvertirToFile tacheConv = new TacheConvertirToFile(listeVideos, folder, bitRate, listeExtensions);
                	new Thread(tacheConv).start();
                }
        		Logger.getInstance().showErrorAlertVideosNonChargees(tache.getListeUrlsMauvaisLien(), tache.getListeUrlsErreur());
            }
        });
		new Thread(tache).start();
		return null;
	}

}
