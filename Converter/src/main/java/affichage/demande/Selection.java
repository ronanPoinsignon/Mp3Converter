package affichage.demande;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import commande.CommandeAjout;
import commande.CommandeInversion;
import commande.CommandeReset;
import commande.CommandeSuppression;
import commande.Gestionnaire;
import event.action.ActionEventAnnuler;
import event.action.ActionEventLoadAppend;
import event.action.ActionEventLoadReset;
import event.action.ActionEventQuitter;
import event.action.ActionEventRaccourcis;
import event.action.ActionEventRedo;
import event.action.ActionEventSave;
import event.action.ActionEventSaveSous;
import event.clavier.ClavierEventHandler;
import event.mouse.MouseEventAjout;
import event.mouse.MouseEventAjoutPlaylist;
import event.mouse.MouseEventConversion;
import event.mouse.MouseEventConversionInstantannee;
import event.mouse.MouseEventSuppression;
import event.window.WindowEventQuitter;
import exception.NoVideoFoundException;
import fichier.DirectoryChooserManager;
import fichier.FileManager;
import javafx.concurrent.WorkerStateEvent;
import javafx.geometry.Insets;
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
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import log.Logger;
import prog.Downloader;
import prog.Utils;
import prog.video.Video;
import prog.video.VideoFichier;
import tache.TacheCharger;
import tache.TacheChargerPlaylist;
import tache.TacheConvertirInstant;
import tache.TacheConvertirToFile;

/**
 * Classe gérant les éléments affichés.
 * @author ronan
 *
 */
public class Selection extends BorderPane {

	private static final double TAILLE_BOUTON = 100;
	private static final String[] extensions = new String[] {"webm", "mp4", "avi"};
	private final ProgressIndicator indicateur = new ProgressIndicator(0);
	private final Label labelIndicateur = new Label("");

	private Logger logger = Logger.getInstance();
	private FileManager fileManager = FileManager.getInstance();
	private Gestionnaire gestionnaire = Gestionnaire.getInstance();

	private TableVideo table = new TableVideo();
	private Button boutonAjout = new Button("Ajouter"),
			boutonSuppression = new Button("Supprimer"),
			boutonConvertir = new Button("Convertir"),
			boutonConvertirUne = new Button("Convertir Une"),
			boutonAjouterPlaylist = new Button("Ajouter Playlist");
	private CheckBox checkBoxMp3 = new CheckBox("Mp3"),
			checkBoxMp4 = new CheckBox("Mp4");
	private ChoiceBox<String> menuBitRate = new ChoiceBox<>();

	private MenuBar menuBar = new MenuBar();

	private Menu menuFichier = new Menu("Fichier");
	private Menu menuLoad = new Menu("Charger");
	private MenuItem itemSave = new MenuItem("Sauvegarder");
	private MenuItem itemSaveSous = new MenuItem("Sauvegarder sous");
	private MenuItem itemLoadAppend = new MenuItem("Ajouter à la liste");
	private MenuItem itemLoadReset = new MenuItem("Réinitialiser la liste");
	private MenuItem itemQuitter = new MenuItem("Quitter");

	private Menu menuAction = new Menu("Actions");
	private MenuItem itemActionAnnuler = new MenuItem("Annuler");
	private MenuItem itemActionReexecuter = new MenuItem("Réexécuter");

	private Menu menuAide = new Menu("Aide");
	private MenuItem itemRaccourcis = new MenuItem("Liste des raccourcis");

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
		boutonAjout.setMinWidth(Selection.TAILLE_BOUTON);
		boutonSuppression.setMinWidth(Selection.TAILLE_BOUTON);
		boutonConvertir.setMinWidth(Selection.TAILLE_BOUTON);

		menuFichier.getItems().add(itemSave);
		menuFichier.getItems().add(itemSaveSous);
		menuLoad.getItems().add(itemLoadAppend);
		menuLoad.getItems().add(itemLoadReset);
		menuFichier.getItems().add(menuLoad);
		menuFichier.getItems().add(itemQuitter);

		menuAction.getItems().add(itemActionAnnuler);
		menuAction.getItems().add(itemActionReexecuter);

		menuAide.getItems().add(itemRaccourcis);

		menuBar.getMenus().add(menuFichier);
		menuBar.getMenus().add(menuAction);
		menuBar.getMenus().add(menuAide);

		menuBitRate.setMinWidth(Selection.TAILLE_BOUTON);
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
		gridBouton.add(boutonAjouterPlaylist, 0, 7);
		GridPane gridTotal = new GridPane();
		gridTotal.add(table, 0, 0);
		gridTotal.add(gridBouton, 1, 0);

		GridPane gridProgression = new GridPane();
		gridProgression.add(indicateur, 0, 0);
		gridProgression.add(labelIndicateur, 1, 0);

		checkBoxMp3.setSelected(true);

		setTop(menuBar);
		setCenter(gridTotal);
		setBottom(gridProgression);
		labelIndicateur.setPadding(new Insets(0,0,0,30));
		gridProgression.setPadding(new Insets(10,0,0,10));
	}

	/**
	 * Ajoute les événements aux différents boutons.
	 */
	protected void addEvent() {
		this.addEventHandler(KeyEvent.KEY_PRESSED, new ClavierEventHandler(this));
		boutonAjout.setOnMouseClicked(new MouseEventAjout(this));
		boutonSuppression.setOnMouseClicked(new MouseEventSuppression(this));
		boutonConvertir.setOnMouseClicked(new MouseEventConversion(this));
		boutonConvertirUne.setOnMouseClicked(new MouseEventConversionInstantannee(this));
		boutonAjouterPlaylist.setOnMouseClicked(new MouseEventAjoutPlaylist(this));

		itemSave.setOnAction(new ActionEventSave(this));
		itemSave.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));

		itemSaveSous.setOnAction(new ActionEventSaveSous(this));

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

		itemRaccourcis.setOnAction(new ActionEventRaccourcis(this));
		itemRaccourcis.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));

		stage.setOnCloseRequest(new WindowEventQuitter(this));

		table.setOnDragOver(event -> {
			if (event.getDragboard().hasFiles()) {
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			}
			event.consume();
		});

		table.setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if (db.hasFiles()) {
				System.out.println("oui");
				success = true;
				List<File> listeFichiers = db.getFiles();
				List<Video> listeVideos = new ArrayList<>();
				for (File fichier : listeFichiers) {
					if(checkExtension(fichier.getPath())) {
						listeVideos.add(new VideoFichier(fichier.getPath()));
					}
				}
				gestionnaire.addCommande(new CommandeAjout(table, listeVideos)).executer();
				updateActionPossibleGestionnaire();
			}
			/* let the source know whether the string was successfully
			 * transferred and used */
			event.setDropCompleted(success);

			event.consume();
		});
	}

	private boolean checkExtension(String filePath) {
		System.out.println(Arrays.asList(Selection.extensions));
		System.out.println(filePath.substring(filePath.lastIndexOf(".")) + 1);
		return Arrays.asList(Selection.extensions).contains(filePath.substring(filePath.lastIndexOf(".") + 1));
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
				t -> {
					List<Video> listeVideos = tache.getValue();
					labelIndicateur.textProperty().unbind();
					labelIndicateur.setText(listeVideos.size() + " vidéo(s) chargée(s)");
					if(!listeVideos.isEmpty()) {
						gestionnaire.addCommande(new CommandeAjout(table, listeVideos));
						gestionnaire.executer();
					}
					if(!tache.getListeUrlsMauvaisLien().isEmpty() || !tache.getListeUrlsErreur().isEmpty()) {
						logger.showErrorAlertVideosNonChargees(tache.getListeUrlsMauvaisLien(), tache.getListeUrlsErreur());
					}
					updateActionPossibleGestionnaire();
				});
		Thread th = new Thread(tache);
		th.setDaemon(true);
		th.start();
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
		updateActionPossibleGestionnaire();
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

	public void ajouterPlaylist(String id) {
		id = Utils.getPlaylistId(id);
		//"PLFNlXTBp7USwpAZAiiAT_DP828Y6fmU_M"
		if(id == null || id.isEmpty()) {
			return;
		}
		TacheChargerPlaylist tache = new TacheChargerPlaylist(id);
		labelIndicateur.textProperty().unbind();
		labelIndicateur.textProperty().bind(tache.messageProperty());
		indicateur.progressProperty().unbind();
		indicateur.progressProperty().bind(tache.progressProperty());
		final String idFinal = id;
		tache.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				t -> {
					List<Video> listeVideos = tache.getValue();
					labelIndicateur.textProperty().unbind();
					labelIndicateur.setText(listeVideos.size() + " vidéo(s) chargée(s)");
					if(tache.hasPb()) {
						Logger.getInstance().showWarningAlertIsNotPlaylistId(idFinal);
					}
					if(!tache.getLinkError().isEmpty()) {
						Logger.getInstance().showErrorAlertVideoError(listeVideos);
					}
					if(!listeVideos.isEmpty()) {
						gestionnaire.addCommande(new CommandeAjout(table, listeVideos));
						gestionnaire.executer();
					}
					updateActionPossibleGestionnaire();
				});
		Thread th = new Thread(tache);
		th.setDaemon(true);
		th.start();
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
		DirectoryChooser directoryChooser = DirectoryChooserManager.getInstance("convertir");
		directoryChooser.setTitle("dossier de conversion");
		File directory = directoryChooser.getInitialDirectory();
		if (directory == null) {
			directory = directoryChooser.showDialog(stage);
		}
		if(directory == null) {
			return;
		}
		directoryChooser.setInitialDirectory(directory);
		int bitRate = Integer.parseInt(menuBitRate.getSelectionModel().getSelectedItem().substring(0, 3))*1000;
		List<String> listeExtensions = new ArrayList<>();
		if(checkBoxMp3.isSelected()) {
			listeExtensions.add("mp3");
		}
		if(checkBoxMp4.isSelected()) {
			listeExtensions.add("mp4");
		}
		TacheConvertirToFile tache = new TacheConvertirToFile(table.getItems(), directory, bitRate, listeExtensions);

		TableViewSelectionModel<Video> defaultSelectionModel = table.getSelectionModel();
		updateActionsBoutonPossibles(true, null);
		tache.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				t -> {
					List<File> downloaded = tache.getValue();
					if(downloaded == null) {
						return;
					}
					labelIndicateur.textProperty().unbind();
					labelIndicateur.setText("téléchargé(s) : " + downloaded.size());
					table.removeAll();
					updateActionsBoutonPossibles(false, defaultSelectionModel);
					gestionnaire.clean();
					updateActionPossibleGestionnaire();
				});
		Thread th = new Thread(tache);
		th.setDaemon(true);
		th.start();
		indicateur.progressProperty().unbind();
		indicateur.progressProperty().bind(tache.progressProperty());
		labelIndicateur.textProperty().bind(tache.messageProperty());
	}

	/**
	 * Convertit une vidéo depuis une Url donnée.
	 * @param url
	 */
	public void convertirFromUrl(String url) {
		int bitRate = Integer.parseInt(menuBitRate.getSelectionModel().getSelectedItem().substring(0, 3))*1000;
		List<String> listeExtensions = new ArrayList<>();
		if(checkBoxMp3.isSelected()) {
			listeExtensions.add("mp3");
		}
		if(checkBoxMp4.isSelected()) {
			listeExtensions.add("mp4");
		}
		File directory = fileManager.getFolder(stage);
		if(directory == null) {
			return;
		}
		TacheConvertirInstant tacheVideo = new TacheConvertirInstant(url, directory, bitRate, listeExtensions);
		TableViewSelectionModel<Video> defaultSelectionModel = table.getSelectionModel();
		labelIndicateur.textProperty().unbind();
		labelIndicateur.textProperty().bind(tacheVideo.messageProperty());
		indicateur.progressProperty().unbind();
		indicateur.progressProperty().bind(tacheVideo.progressProperty());
		tacheVideo.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
			Video video = tacheVideo.getValue();
			if(video == null) {
				return;
			}
			labelIndicateur.textProperty().unbind();
			labelIndicateur.setText("téléchargé(s) : " + video.getTitre());
			updateActionsBoutonPossibles(false, defaultSelectionModel);
			Downloader downloader = new Downloader();
			try {
				downloader.download(FileManager.getInstance().getFolder(stage), url, false);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoVideoFoundException e) {
				Logger.getInstance().showErrorAlertNoVideoFound();
			}
		});
		Thread th = new Thread(tacheVideo);
		th.setDaemon(true);
		th.start();
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
			fileManager.save(stage, table.getItems().stream().collect(Collectors.toList()));
			updateActionPossibleGestionnaire();
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
		if(listeVideos == null || listeVideos.isEmpty()) {
			return;
		}
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
		if(listeVideos == null || listeVideos.isEmpty()) {
			return;
		}
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
			// ne rien faire
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
			// ne rien faire
		}
		updateActionPossibleGestionnaire();
	}

	/**
	 * Quitte l'application en demandant une confirmation si
	 * l'utilisateur n'a pas sauvegardé son travail.
	 */
	public boolean quitter() {
		return !gestionnaire.canSave() || logger.canQuit();
	}

	/**
	 * Modifie l'interface pour empêcher certaines actions impossibles.
	 * (Ex : Annuler une commande alors qu'il n'y en a aucune qui a été effectuée)
	 */
	public void updateActionPossibleGestionnaire() {
		itemActionAnnuler.setDisable(!gestionnaire.canAnnuler());
		itemActionReexecuter.setDisable(!gestionnaire.canReexecuter());
		itemSave.setDisable(!gestionnaire.canSave());
		itemSaveSous.setDisable(table.getItems().isEmpty());
		menuAction.setDisable(itemActionAnnuler.isDisable() && itemActionReexecuter.isDisable());
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

	/**
	 * Récupère le lien de la ligne séléctionnée dans la table.
	 * @return
	 */
	public String getSelectedLink() {
		int index = table.getSelectionModel().getSelectedIndex();
		return table.getItems().get(index).getLien();
	}

	/**
	 * Inverse la ligne séléctionnée avec celle du haut.
	 */
	public void swapUp() {
		int index = table.getSelectionModel().getSelectedIndex();
		Gestionnaire.getInstance().addCommande(new CommandeInversion(table, index, index - 1)).executer();
	}

	/**
	 * Inverse la ligne séléctionnée avec celle du bas.
	 */
	public void swapDown() {
		int index = table.getSelectionModel().getSelectedIndex();
		Gestionnaire.getInstance().addCommande(new CommandeInversion(table, index, index + 1)).executer();
	}

}
