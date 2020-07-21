package tache;

import java.util.List;

import event.tache.event.EventTacheUpdateMessage;
import event.tache.event.EventTacheUpdateProgress;
import event.tache.handler.EventHandlerTacheUpdateMessage;
import event.tache.handler.EventHandlerTacheUpdateProgress;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import log.Logger;
import prog.video.Video;

public class TacheConvertirUrlToVideo extends Tache<Video> {

	private String url;
	
	public TacheConvertirUrlToVideo(String url) {
		this.url = url;
	}
	
	@Override
	protected Video call() throws Exception {
		TacheCharger tache = new TacheCharger(url);
		System.out.println("0");
		tache.addEventHandler(EventTacheUpdateProgress.EVENT_UPDATE_PROGRESS, new EventHandlerTacheUpdateProgress() {
			@Override
			public void onUpdateProgress(long workDone, long max) {
				TacheConvertirUrlToVideo.this.updateProgress(workDone, max);
			}
		});
		System.out.println("1");
		tache.addEventHandler(EventTacheUpdateMessage.EVENT_UPDATE_MESSAGE, new EventHandlerTacheUpdateMessage() {
			@Override
			public void onUpdateMessage(String message) {
				TacheConvertirUrlToVideo.this.updateMessage(message);
			}
		});
		System.out.println("2");
		tache.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
            	System.out.println("fini");
                List<Video> listeVideos = tache.getValue();
                if(!listeVideos.isEmpty()) {
	        		
                }
        		Logger.getInstance().showErrorAlertVideosNonChargees(tache.getListeUrlsMauvaisLien(), tache.getListeUrlsErreur());
            }
        });
		System.out.println("3");
		new Thread(tache).start();
		List<Video> liste = tache.getValue();
		if(liste != null && !liste.isEmpty())
			return liste.get(0);
		else
			return null;
	}

}
