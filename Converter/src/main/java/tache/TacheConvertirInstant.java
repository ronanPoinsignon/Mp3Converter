package tache;

import java.util.List;

import event.tache.event.EventTacheUpdated;
import javafx.event.EventHandler;
import prog.video.Video;

public class TacheConvertirInstant extends Tache<Video> {

	private String url;
	
	public TacheConvertirInstant(String url) {
		this.url = url;
	}
	
	@Override
	protected Video call() throws Exception {
		TacheCharger tache = new TacheCharger(url);
		System.out.println("0");
		tache.addEventHandler(EventTacheUpdated.EVENT_UPDATE, new EventHandler<EventTacheUpdated>() {

			@Override
			public void handle(EventTacheUpdated event) {
				System.out.println(event.getEventType());
			}
		});
		/*tache.addEventHandler(EventTacheUpdateProgress.EVENT_UPDATE_PROGRESS, new EventHandlerTacheUpdateProgress() {
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
        });*/
		System.out.println("3");
		new Thread(tache).start();
		List<Video> liste = tache.getValue();
		if(liste != null && !liste.isEmpty())
			return liste.get(0);
		else
			return null;
	}

}
