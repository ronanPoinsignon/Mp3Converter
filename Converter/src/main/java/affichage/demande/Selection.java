package affichage.demande;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import commande.CommandeAjout;
import commande.CommandeSuppression;
import commande.Gestionnaire;
import exception.PasDeResultatException;
import fichier.FileManager;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import log.Logger;
import prog.Video;
import tache.TacheCharger;
import tache.TacheConvertir;

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
			BoutonConvertir = new Button("Convertir");
	private ChoiceBox<String> menuBitRate = new ChoiceBox<>();
	
	private MenuBar menuBar = new MenuBar();
	
	private Menu menuFichier = new Menu("Fichier");
	private Menu menuLoad = new Menu("Load");
	private MenuItem itemSave = new MenuItem("Save");
	private MenuItem itemLoadAppend = new MenuItem("Ajouter à la liste");
	private MenuItem itemLoadReset = new MenuItem("Réinitialiser la liste");
	private MenuItem itemQuitter = new MenuItem("Quitter");
	
	private Menu menuAction = new Menu("Actions");
	private MenuItem itemActionAnnuler = new MenuItem("Annuler");
	private MenuItem itemActionReexecuter = new MenuItem("Réexécuter");
	
	private Stage stage;
	
	public Selection(Stage stage) {
		this.stage = stage;
		this.boutonAjout.setMinWidth(TAILLE_BOUTON);
		this.boutonSuppression.setMinWidth(TAILLE_BOUTON);
		this.BoutonConvertir.setMinWidth(TAILLE_BOUTON);
		
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
		addTable();
		createPane();
		addEvent();
		updateActionPossibleGestionnaire();
	}
	
	/**
	 * Place les éléments.
	 */
	protected void createPane() {		
		GridPane gridBouton = new GridPane();
		gridBouton.add(menuBitRate, 0, 0);
		gridBouton.add(boutonAjout, 0, 1);
		gridBouton.add(boutonSuppression, 0, 2);
		gridBouton.add(BoutonConvertir, 0, 3);
		
		GridPane gridTotal = new GridPane();
		gridTotal.add(table, 0, 0);
		gridTotal.add(gridBouton, 1, 0);
		
		GridPane gridProgression = new GridPane();
		gridProgression.add(indicateur, 0, 0);
		gridProgression.add(labelIndicateur, 1, 0);
		this.setTop(menuBar);
		this.setCenter(gridTotal);
		this.setBottom(gridProgression);
	}
	
	/**
	 * Ajoute les événements aux différents boutons.
	 */
	protected void addEvent() {
		boutonAjout.setOnMouseClicked(evt -> {
			String url;
			try {
				url = showInputDIalogAjout();
				addLineToTable(url);
			} catch (PasDeResultatException e) {
				
			} 
		});
		boutonSuppression.setOnMouseClicked(evt -> {
			removeLine();
		});
		BoutonConvertir.setOnMouseClicked(evt -> {
			convertir();
		});
		itemSave.setOnAction(evt -> {
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
		});
		itemLoadAppend.setOnAction(evt -> {
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
		});
		itemLoadReset.setOnAction(evt -> {
			List<Video> listeVideos = null;
			try {
				listeVideos = fileManager.load(stage);
			} catch (ClassNotFoundException | IOException e) {
				logger.showErrorAlert(e);
			}
			if(listeVideos == null || listeVideos.isEmpty())
				return;
			if(!table.getItems().isEmpty()) {
				gestionnaire.addCommande(new CommandeSuppression(table, table.getItems()));
				gestionnaire.executer();
			}
			gestionnaire.addCommande(new CommandeAjout(table, listeVideos));
			gestionnaire.executer();
			updateActionPossibleGestionnaire();
		});
		itemQuitter.setOnAction(evt -> {
			System.exit(0);
		});
		itemActionAnnuler.setOnAction(evt -> {
			try {
				gestionnaire.annuler();
			}
			catch(IndexOutOfBoundsException e) {
				
			}
			updateActionPossibleGestionnaire();
		});
		itemActionReexecuter.setOnAction(evt -> {
			try {
				gestionnaire.reexecuter();
			}
			catch(IndexOutOfBoundsException e) {
				
			}
			updateActionPossibleGestionnaire();
		});
	}
	
	/**
	 * Ajoute une vidéos à la table. Modifie l'affichage en conséquence.
	 * @param url
	 */
	private void addLineToTable(String url) {
		ArrayList<String> listeUrls = new ArrayList<>();
		listeUrls.add(url);
		TacheCharger tache = new TacheCharger(listeUrls);
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
		labelIndicateur.textProperty().unbind();
		labelIndicateur.textProperty().bind(tache.messageProperty());
		indicateur.progressProperty().unbind();
		indicateur.progressProperty().bind(tache.progressProperty());
		new Thread(tache).start();
	}
	
	/**
	 * Supprime une ligne de la table.
	 */
	private void removeLine() {
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
		
		TableColumn<Video, String> colonneId = new TableColumn<>("Id");
		colonneId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colonneId.widthProperty().multiply(0.6);
		colonneId.setSortable(false);
		table.getColumns().add(colonneTitre);
		table.getColumns().add(colonneId);
	}
	
	/**
	 * Affiche une boîte de dialogue pour permettre l'ajout d'une nouvelle vidéos Youtube en donnant le lien de celle-ci.
	 * @return
	 * @throws PasDeResultatException
	 */
	public String showInputDIalogAjout() throws PasDeResultatException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Sélection");
		dialog.setHeaderText("Donnez un lien de vidéo");
		dialog.setContentText("lien : ");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent())
			return result.get();
		throw new PasDeResultatException();
	}
	
	/**
	 * Convertit la liste de vidéos en fichier mp3.
	 */
	private void convertir() {
		if(table.getItems().isEmpty()) {
			logger.showWarningAlertConvertisseurVide();
			return;
		}
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("dossier de sauvegarde");
		File directory = directoryChooser.getInitialDirectory();
        if (directory == null) {
        	directory = directoryChooser.showDialog(stage);
        }
		if(directory == null)
			return;
		directoryChooser.setInitialDirectory(directory);
		int bitRate = Integer.parseInt(menuBitRate.getSelectionModel().getSelectedItem().substring(0, 3))*1000;
		TacheConvertir tache = new TacheConvertir(table, directory, bitRate);

		TableViewSelectionModel<Video> defaultSelectionModel = table.getSelectionModel();
		boutonAjout.setDisable(true);
		boutonSuppression.setDisable(true);
		BoutonConvertir.setDisable(true);
		table.setSelectionModel(null);
		tache.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
			 
            @Override
            public void handle(WorkerStateEvent t) {
                List<File> downloaded = tache.getValue();
                if(downloaded == null)
                	return;
                labelIndicateur.textProperty().unbind();
                labelIndicateur.setText("téléchargé(s) : " + downloaded.size());
                table.removeAll();
        		table.setSelectionModel(defaultSelectionModel);
        		boutonAjout.setDisable(false);
        		boutonSuppression.setDisable(false);
        		BoutonConvertir.setDisable(false);
            }
        });
		new Thread(tache).start();
		indicateur.progressProperty().unbind();
		indicateur.progressProperty().bind(tache.progressProperty());
		labelIndicateur.textProperty().bind(tache.messageProperty());
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
	
}
