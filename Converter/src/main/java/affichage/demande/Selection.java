package affichage.demande;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.kiulian.downloader.YoutubeException;

import commande.CommandeAjout;
import commande.CommandeReset;
import commande.CommandeSuppression;
import commande.Gestionnaire;
import event.action.ActionEventAnnuler;
import event.action.ActionEventLoadAppend;
import event.action.ActionEventLoadReset;
import event.action.ActionEventQuitter;
import event.action.ActionEventRedo;
import event.action.ActionEventSave;
import event.mouse.MouseEventAjout;
import event.mouse.MouseEventConversion;
import event.mouse.MouseEventConversionInstantannee;
import event.mouse.MouseEventSuppression;
import event.window.WindowEventQuitter;
import fichier.DirectoryChooserManager;
import fichier.FileManager;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import log.Logger;
import prog.Downloader;
import prog.video.Video;
import tache.TacheCharger;
import tache.TacheConvertirToFile;
import tache.TacheConvertirUrlToVideo;

/**
 * Classe gérant les éléments affichés.
 * @author ronan
 *
 */
public class Selection extends BorderPane {

	private static final double TAILLE_BOUTON = 100;
	
	private final ProgressIndicator indicateur = new ProgressIndicator(0);
	private final Label labelIndicateur = new Label("");
	
	private Logger logger = Logger.getInstance();
	private FileManager fileManager = FileManager.getInstance();
	private Gestionnaire gestionnaire = Gestionnaire.getInstance();
	
	private TableVideo table = new TableVideo();
	private Button boutonAjout = new Button("Ajouter"),
			boutonSuppression = new Button("Supprimer"),
			boutonConvertir = new Button("Convertir"),
			boutonConvertirUne = new Button("Convertir Une");
	private CheckBox checkBoxMp3 = new CheckBox("Mp3"),
			checkBoxMp4 = new CheckBox("Mp4");
	private ChoiceBox<String> menuBitRate = new ChoiceBox<>();
	
	private MenuBar menuBar = new MenuBar();
	
	private Menu menuFichier = new Menu("Fichier");
	private Menu menuLoad = new Menu("Charger");
	private MenuItem itemSave = new MenuItem("Sauvegarder");
	private MenuItem itemLoadAppend = new MenuItem("Ajouter à la liste");
	private MenuItem itemLoadReset = new MenuItem("Réinitialiser la liste");
	private MenuItem itemQuitter = new MenuItem("Quitter");
	
	private Menu menuAction = new Menu("Actions");
	private MenuItem itemActionAnnuler = new MenuItem("Annuler");
	private MenuItem itemActionReexecuter = new MenuItem("Réexécuter");
	
	private Stage stage;
	
	public Selection(Stage stage) {
		this.stage = stage;
		addTable();
		createPane();
		addEvent();
		updateActionPossibleGestionnaire();
	}
	
	/**
	 * Place les éléments.
	 */
	protected void createPane() {
		this.boutonAjout.setMinWidth(TAILLE_BOUTON);
		this.boutonSuppression.setMinWidth(TAILLE_BOUTON);
		this.boutonConvertir.setMinWidth(TAILLE_BOUTON);
		
		menuFichier.getItems().add(itemSave);
		
		menuLoad.getItems().add(itemLoadAppend);
		menuLoad.getItems().add(itemLoadReset);
		menuFichier.getItems().add(menuLoad);
		menuFichier.getItems().add(itemQuitter);
		
		menuAction.getItems().add(itemActionAnnuler);
		menuAction.getItems().add(itemActionReexecuter);
		
		this.menuBar.getMenus().add(menuFichier);
		this.menuBar.getMenus().add(menuAction);
		
		menuBitRate.setMinWidth(TAILLE_BOUTON);
		menuBitRate.getItems().add("128kb/s");
		menuBitRate.getItems().add("192kb/s");
		menuBitRate.getItems().add("320kb/s");
		menuBitRate.getSelectionModel().select(1);
		
		GridPane gridBouton = new GridPane();
		gridBouton.add(menuBitRate, 0, 0);
		gridBouton.add(boutonAjout, 0, 1);
		gridBouton.add(boutonSuppression, 0, 2);
		gridBouton.add(boutonConvertir, 0, 3);
		gridBouton.add(checkBoxMp3, 0, 4);
		gridBouton.add(checkBoxMp4, 0, 5);
		gridBouton.add(boutonConvertirUne, 0, 6);
		GridPane gridTotal = new GridPane();
		gridTotal.add(table, 0, 0);
		gridTotal.add(gridBouton, 1, 0);
		
		GridPane gridProgression = new GridPane();
		gridProgression.add(indicateur, 0, 0);
		gridProgression.add(labelIndicateur, 1, 0);
		
		checkBoxMp3.setSelected(true);

		this.setTop(menuBar);
		this.setCenter(gridTotal);
		this.setBottom(gridProgression);
		boutonConvertirUne.setDisable(true);
	}
	
	/**
	 * Ajoute les événements aux différents boutons.
	 */
	protected void addEvent() {
		boutonAjout.setOnMouseClicked(new MouseEventAjout(this));
		boutonSuppression.setOnMouseClicked(new MouseEventSuppression(this));
		boutonConvertir.setOnMouseClicked(new MouseEventConversion(this));
		boutonConvertirUne.setOnMouseClicked(new MouseEventConversionInstantannee(this));
		
		itemSave.setOnAction(new ActionEventSave(this));
		itemSave.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));

		itemLoadAppend.setOnAction(new ActionEventLoadAppend(this));
		itemLoadAppend.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));

		itemLoadReset.setOnAction(new ActionEventLoadReset(this));
		itemLoadReset.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
		
		itemQuitter.setOnAction(new ActionEventQuitter(this));
		itemQuitter.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
		
		itemActionAnnuler.setOnAction(new ActionEventAnnuler(this));
		itemActionAnnuler.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
		
		itemActionReexecuter.setOnAction(new ActionEventRedo(this));
		itemActionReexecuter.setAccelerator(KeyCombination.keyCombination("Ctrl+Y"));
		
		this.stage.setOnCloseRequest(new WindowEventQuitter(this));
	}
	
	/**
	 * Ajoute une vidéos à la table. Modifie l'affichage en conséquence.
	 * @param url
	 */
	public void addVideoToTable(String url) {
		ArrayList<String> listeUrls = new ArrayList<>();
		listeUrls.add(url);
		TacheCharger tache = new TacheCharger(listeUrls);
		labelIndicateur.textProperty().unbind();
		labelIndicateur.textProperty().bind(tache.messageProperty());
		indicateur.progressProperty().unbind();
		indicateur.progressProperty().bind(tache.progressProperty());
		tache.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
			 
            @Override
            public void handle(WorkerStateEvent t) {
                List<Video> listeVideos = tache.getValue();
        		labelIndicateur.textProperty().unbind();
        		labelIndicateur.setText(listeVideos.size() + " vidéo(s) chargée(s)");
                if(!listeVideos.isEmpty()) {
	        		gestionnaire.addCommande(new CommandeAjout(table, listeVideos));
	        		gestionnaire.executer();
                }
        		if(!tache.getListeUrlsMauvaisLien().isEmpty() || !tache.getListeUrlsErreur().isEmpty())
        			logger.showErrorAlertVideosNonChargees(tache.getListeUrlsMauvaisLien(), tache.getListeUrlsErreur());
    			updateActionPossibleGestionnaire();
            }
        });
		new Thread(tache).start();
	}
	
	/**
	 * Supprime une ligne de la table.
	 */
	public void removeLine() {
	    int selectedIndex = table.getSelectionModel().getSelectedIndex();
	    if (selectedIndex >= 0) {
	    	ArrayList<Video> listeVideos = new ArrayList<>();
	    	listeVideos.add(table.getItems().get(selectedIndex));
	    	gestionnaire.addCommande(new CommandeSuppression(table, listeVideos));
	    	gestionnaire.executer();
	    } else {
	        logger.showWarningAlertAucuneVideoASupprimer();
	    }
	}
	
	/**
	 * Ajoute à la liste tous les éléments nécessaires pour un affichage correct.
	 */
	protected void addTable() {
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setMinWidth(1000);
		
		TableColumn<Video, String> colonneTitre = new TableColumn<>("Titre");
		colonneTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
		colonneTitre.widthProperty().multiply(0.4);
		
		TableColumn<Video, String> colonneLien = new TableColumn<>("Lien");
		colonneLien.setCellValueFactory(new PropertyValueFactory<>("lien"));
		colonneLien.widthProperty().multiply(0.6);
		colonneLien.setSortable(false);
		table.getColumns().add(colonneTitre);
		table.getColumns().add(colonneLien);
	}
	
	/**
	 * Convertit la liste de vidéos en fichier mp3 ou mp4.
	 */
	public void convertirListe() {
		if(table.getItems().isEmpty()) {
			logger.showWarningAlertConvertisseurVide();
			return;
		}
		if(!checkBoxMp3.isSelected() && !checkBoxMp4.isSelected()) {
			logger.showWarningAlertAucuneOption();
			return;
		}
		DirectoryChooser directoryChooser = DirectoryChooserManager.getInstance();
		directoryChooser.setTitle("dossier de sauvegarde");
		File directory = directoryChooser.getInitialDirectory();
        if (directory == null) {
        	directory = directoryChooser.showDialog(stage);
        }
		if(directory == null)
			return;
		directoryChooser.setInitialDirectory(directory);
		int bitRate = Integer.parseInt(menuBitRate.getSelectionModel().getSelectedItem().substring(0, 3))*1000;
		List<String> listeExtensions = new ArrayList<>();
		if(checkBoxMp3.isSelected())
			listeExtensions.add("mp3");
		if(checkBoxMp4.isSelected())
			listeExtensions.add("mp4");
		TacheConvertirToFile tache = new TacheConvertirToFile(table.getItems(), directory, bitRate, listeExtensions);

		TableViewSelectionModel<Video> defaultSelectionModel = table.getSelectionModel();
		updateActionsBoutonPossibles(true, null);
		final File directoryFinal = directory;
		tache.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
			 
            @Override
            public void handle(WorkerStateEvent t) {
                List<File> downloaded = tache.getValue();
                if(downloaded == null)
                	return;
                labelIndicateur.textProperty().unbind();
                labelIndicateur.setText("téléchargé(s) : " + downloaded.size());
                for(File fichier : downloaded)
                	fichier.delete();
                if(directoryFinal.listFiles().length == 0)
                	directoryFinal.delete();
                table.removeAll();
        		updateActionsBoutonPossibles(false, defaultSelectionModel);
        		gestionnaire.clean();
        		updateActionPossibleGestionnaire();
            }
        });
		new Thread(tache).start();
		indicateur.progressProperty().unbind();
		indicateur.progressProperty().bind(tache.progressProperty());
		labelIndicateur.textProperty().bind(tache.messageProperty());
	}
	
	/**
	 * Convertit une vidéo depuis une Url donnée.
	 * @param url
	 */
	public void convertirFromUrl(String url) {
		TacheConvertirUrlToVideo tacheVideo = new TacheConvertirUrlToVideo(url);
		TableViewSelectionModel<Video> defaultSelectionModel = table.getSelectionModel();
		labelIndicateur.textProperty().unbind();
		labelIndicateur.textProperty().bind(tacheVideo.messageProperty());
		indicateur.progressProperty().unbind();
		indicateur.progressProperty().bind(tacheVideo.progressProperty());
		tacheVideo.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
			 
            @Override
            public void handle(WorkerStateEvent t) {
                Video video = tacheVideo.getValue();
                if(video == null)
                	return;
                labelIndicateur.textProperty().unbind();
                labelIndicateur.setText("téléchargé(s) : " + video.getTitre());
        		updateActionsBoutonPossibles(false, defaultSelectionModel);
        		Downloader downloader = new Downloader();
        		try {
					downloader.download(FileManager.getInstance().getFolder(stage), url, false);
				} catch (YoutubeException | IOException e) {
					e.printStackTrace();
				}
            }
        });
		new Thread(tacheVideo).start();
	}
	
	/**
	 * Sauvegarde la liste de vidéos en demandant à l'utilisateur l'emplacement et le nom du fichier de sauvegarde.
	 */
	public void sauvegarder() {
		if(table.getItems().isEmpty()) {
			logger.showWarningAlertNoFileToSave();
			return;
		}
		try {
			FileManager.getInstance();
			fileManager.save(stage, table.getItems().stream().collect(Collectors.toList()));
		}
		catch(IOException e) {
			logger.showErrorAlert(e);
		}
	}
	
	/**
	 * Demande à l'utilisateur de charger un fichier de sauvegarde puis 
	 * charge la liste de vidéos correspondant à ce fichier en ajoutant 
	 * ces vidéos à la liste déjà présente.
	 */
	public void loadAppend() {
		List<Video> listeVideos = null;
		try {
			listeVideos = fileManager.load(stage);
		} catch (ClassNotFoundException | IOException e) {
			logger.showErrorAlert(e);
		}
		if(listeVideos == null || listeVideos.isEmpty())
			return;
		gestionnaire.addCommande(new CommandeAjout(table, listeVideos));
		gestionnaire.executer();
		updateActionPossibleGestionnaire();
	}
	
	/**
	 * Demande à l'utilisateur de charger un fichier de sauvegarde puis 
	 * supprime la liste déjà présente pour charger la nouvelle liste.
	 */
	public void loadReset() {
		List<Video> listeVideos = null;
		try {
			listeVideos = fileManager.load(stage);
		} catch (ClassNotFoundException | IOException e) {
			logger.showErrorAlert(e);
		}
		if(listeVideos == null || listeVideos.isEmpty())
			return;
		gestionnaire.addCommande(new CommandeReset(table, listeVideos));
		gestionnaire.executer();
		updateActionPossibleGestionnaire();
	}
	
	/**
	 * Annule la dernière action faite par l'utilisateur.
	 */
	public void annuler() {
		try {
			gestionnaire.annuler();
		}
		catch(IndexOutOfBoundsException e) {
			
		}
		updateActionPossibleGestionnaire();
	}
	
	/**
	 * Réexécute la dernière action annulée par l'utilisateur.
	 */
	public void reexecuter() {
		try {
			gestionnaire.reexecuter();
		}
		catch(IndexOutOfBoundsException e) {
			
		}
		updateActionPossibleGestionnaire();
	}
	
	/**
	 * Quitte l'application en demandant une confirmation si 
	 * l'utilisateur n'a pas sauvegardé son travail.
	 */
	public void quitter() {
		if(gestionnaire.canSave()) {
			if(logger.canQuit()) {
				System.exit(0);
			}
		}
		else
			System.exit(0);
	}
	
	/**
	 * Modifie l'interface pour empêcher certaines actions impossibles.
	 * (Ex : Annuler une commande alors qu'il n'y en a aucune qui a été effectuée)
	 */
	public void updateActionPossibleGestionnaire() {
			this.itemActionAnnuler.setDisable(!gestionnaire.canAnnuler());
			this.itemActionReexecuter.setDisable(!gestionnaire.canReexecuter());
			this.itemSave.setDisable(!gestionnaire.canSave());
			this.menuAction.setDisable(itemActionAnnuler.isDisable() && itemActionReexecuter.isDisable());
	}
	
	/**
	 * Met à jour les boutons de l'interface ainsi que la liste pour empêcher ou non l'utilisateur d'effectuer une action.
	 * @param isDisabled
	 * @param model
	 */
	public void updateActionsBoutonPossibles(boolean isDisabled, TableViewSelectionModel<Video> model) {
		boutonAjout.setDisable(isDisabled);
		boutonSuppression.setDisable(isDisabled);
		boutonConvertir.setDisable(isDisabled);
		table.setSelectionModel(model);
	}
	
}
