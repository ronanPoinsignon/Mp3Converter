package log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import event.clavier.ClavierEventHandler;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import prog.video.Video;

/**
 * Classe de log permettant l'affichage d' {@link Alert} d'erreur ou de mauvaise manipulations.
 * @author ronan
 *
 */
public class Logger {

	private static Logger logger = null;

	private boolean canShowWarning = true;

	public static Logger getInstance() {
		if(Logger.logger == null) {
			Logger.logger = new Logger();
		}
		return Logger.logger;
	}

	private Logger() {

	}

	/**
	 * Affiche une {@link Alert} pour avertir l'utilisateur que le lien donné
	 * à l'application n'est pas un lien Youtube correct.
	 */
	public void showWarningAlertBadLink() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Mauvais lien");
		alert.setHeaderText("Le lien donné n'est pas valide");
		alert.setContentText("Veuillez donner un lien youtube");

		alert.showAndWait();
	}

	/**
	 * Affiche une {@link Alert} pour avertir l'utilisateur que le lien donné
	 * à l'application ne mène vers aucune playlist.
	 */
	public void showWarningAlertBadPlaylistLink() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Aucune playlist trouvée");
		alert.setHeaderText("Aucune playlist n'a été trouvée");
		alert.setContentText("Votre playlist est peut-être en privée");

		alert.showAndWait();
	}

	/**
	 * Affiche une {@link Alert} pour avertir l'utilisateur que la vidéo
	 * qu'il souhaite ajouter à la liste est déjà présente.
	 * @param listeVideos
	 */
	public void showWarningAlertVideosDejaPresentes(List<Video> listeVideos) {
		if(listeVideos.isEmpty()) {
			return;
		}
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Vidéos déjà présentes");

		alert.setHeaderText("Certaienes vidéos sont déjà présentes dans la liste");
		StringBuilder liste = new StringBuilder("Les vidéos suivantes sont déjà présentes :");
		for(Video video : listeVideos) {
			liste.append("\n\t- " + video.getTitre());
		}

		alert.setContentText(liste.toString());

		alert.showAndWait();
	}

	/**
	 * Affiche une {@link Alert} pour avertir l'utilisateur qu'il doit
	 * séléctionner une vidéo à supprimer.
	 */
	public void showWarningAlertAucuneVideoASupprimer() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Pas de vidéo séléctionnée");
		alert.setHeaderText("Aucune vidéo n'a été choisie pour la suppression");
		alert.setContentText("Veuillez choisir une vidéo à supprimer");

		alert.showAndWait();
	}

	/**
	 * Affiche une {@link Alert} pour avertir l'utilisateur que la
	 * conversion ne peut être effectuée car la liste est vide.
	 */
	public void showWarningAlertConvertisseurVide() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Rien à convertir");
		alert.setHeaderText("Aucune vidéo ne peut être convertie");
		alert.setContentText("Veuillez ajouter au moins une vidéo à la liste pour effectuer cette action");

		alert.showAndWait();
	}

	public void showWarningAlertAucuneOption() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Pas de type de conversion donné");
		alert.setHeaderText("Aucun type de conversion n'a été donné");
		alert.setContentText("Veuillez choisir la conversion voulue");

		alert.showAndWait();
	}

	/**
	 * Affiche une {@link Alert} d'erreur pour informer l'utilisateur
	 * que certaines vidéos ont eu un problème lors de leur chargement.
	 * @param listeUrlsMauvaisLien
	 * @param listeUrlsErreur
	 */
	public void showErrorAlertVideosNonChargees(List<String> listeUrlsMauvaisLien, List<String> listeUrlsErreur) {
		if(listeUrlsErreur.isEmpty() && listeUrlsMauvaisLien.isEmpty()) {
			return;
		}
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Un problème est survenu");
			alert.setHeaderText("Certaines vidéos n'ont pu être chargées");
			StringBuilder probleme = new StringBuilder();
			if(!listeUrlsMauvaisLien.isEmpty()) {
				probleme.append("Les url ci-dessous ne correspondent pas à un lien valide venant de youtube :");
				for(String url1 : listeUrlsMauvaisLien) {
					probleme.append("\n\t- " + url1);
				}
			}
			if(!listeUrlsErreur.isEmpty()) {
				probleme.append("\nLes urls ci-dessous ont apporté un problème lors de leur chargement :");
				for(String url2 : listeUrlsErreur) {
					probleme.append("\n\t- " + url2);
				}
			}
			alert.setContentText(probleme.toString());

			alert.showAndWait();
		});
	}

	/**
	 * Affiche une {@link Alert} d'erreur lorsque qu'une erreur est apparue.
	 * @param e
	 */
	public void showErrorAlert(Exception e) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Exception Dialog");
			alert.setHeaderText("Un problème est apparu");
			alert.setContentText("Veuillez redémarrer l'application");

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String exceptionText = sw.toString();

			Label label = new Label("Erreur : ");

			TextArea textArea = new TextArea(exceptionText);
			textArea.setEditable(false);
			textArea.setWrapText(true);

			textArea.setMaxWidth(Double.MAX_VALUE);
			textArea.setMaxHeight(Double.MAX_VALUE);
			GridPane.setVgrow(textArea, Priority.ALWAYS);
			GridPane.setHgrow(textArea, Priority.ALWAYS);

			GridPane expContent = new GridPane();
			expContent.setMaxWidth(Double.MAX_VALUE);
			expContent.add(label, 0, 0);
			expContent.add(textArea, 0, 1);

			alert.getDialogPane().setExpandableContent(expContent);

			alert.showAndWait();
		});
	}

	/**
	 * Retourne TRUE si il est possible d'afficher les {@link Alert} de type Warning.
	 * @return
	 */
	public boolean canShowWarning() {
		return canShowWarning;
	}

	/**
	 * Modifie la valeur pour afficher ou non les {@link Alert} de type Warning
	 * @param canShowWarning
	 */
	public void setShowWarning(boolean canShowWarning) {
		this.canShowWarning = canShowWarning;
	}

	/**
	 * Affiche une {@link Alert} pour avertir l'utilisateur qu'il n'est pas
	 * possible de sauvegrder une liste vide.
	 */
	public void showWarningAlertNoFileToSave() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("La liste est vide");
		alert.setHeaderText("Il n'y a aucune vidéo à sauvegarder");
		alert.setContentText("Veuillez ajouter au moins une vidéo à la liste pour effectuer cette action");

		alert.showAndWait();
	}


	/**
	 * Affiche une {@link Alert} permettant une confirmation lors de la fermeture de l'application
	 * si les actions efféctuées sur cette dernière n'ont pas été sauvegardées.
	 * @return
	 */
	public boolean canQuit() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Vous n'avez pas sauvegardé");
		alert.setHeaderText("Votre travail n'a pas été sauvegardé");
		alert.setContentText("Êtes-vous sûr de vouloir quitter l'application ?");

		Optional<ButtonType> result = alert.showAndWait();
		return result.isPresent() && result.get() == ButtonType.OK;
	}

	/**
	 * Affiche une {@link Alert} mettant en garde l'utilisateur que le lien qu'il a donné à
	 * l'application est un dossier et non un fichier.
	 */
	public void showWarningAlertIsDIrectory() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.getDialogPane().setMinWidth(700);
		alert.setTitle("Ceci n'est pas un fichier vidéo.");
		alert.setHeaderText("Le lien que vous avez donné correspond à un lien de dossier.");
		alert.setContentText("Veuillez donner le lien d'un fichier vidéo.");

		alert.showAndWait();
	}

	/**
	 * Affiche une {@link Alert} mettant en garde l'utilisateur que certains fichiers
	 * qu'il a donné ne correspondent pas à des fichiers vidéos.
	 * @param listeVideos
	 */
	public void showWarningAlertIsNotVideoFile(List<Video> listeVideos) {
		if(listeVideos == null || listeVideos.isEmpty()) {
			return;
		}
		Platform.runLater(() -> {

			Alert alert = null;
			try {
				alert = new Alert(AlertType.ERROR);
			}
			catch(Exception e) {
				e.printStackTrace();
				return;
			}
			alert.setTitle("Les fichiers suivant ne sont pas des vidéos.");
			alert.setHeaderText("Les fichiers que vous avez donné correspondent à un format de fichier n'étant pas un format vidéo.");
			StringBuilder liens = new StringBuilder("Les fichiers en questions sont listés ci-dessous :");
			for(Video video : listeVideos) {
				liens.append("\n\t- " + video.getTitre());
			}
			alert.setContentText(liens.toString());

			alert.showAndWait();
		});
	}

	public void showErrorAlertNoVideoFound(List<Video> listeVideos) {
		if(listeVideos == null || listeVideos.isEmpty()) {
			return;
		}
		Platform.runLater(() -> {

			Alert alert = null;
			try {
				alert = new Alert(AlertType.ERROR);
			}
			catch(Exception e) {
				e.printStackTrace();
				return;
			}
			alert.setTitle("Les fichiers suivant ne donne sur aucune vidéo");
			alert.setHeaderText("Les fichiers que vous avez donné ne renvoie sur aucune vidéo téléchageable.");
			StringBuilder liens = new StringBuilder("Youtube a peut-être des soucis.");
			for(Video video : listeVideos) {
				liens.append("\n\t- " + video.getTitre());
			}
			alert.setContentText(liens.toString());

			alert.showAndWait();
		});
	}

	public void showErrorAlertVideoError(List<Video> listeVideos) {
		if(listeVideos == null || listeVideos.isEmpty()) {
			return;
		}
		Platform.runLater(() -> {

			Alert alert = null;
			try {
				alert = new Alert(AlertType.ERROR);
			}
			catch(Exception e) {
				e.printStackTrace();
				return;
			}
			alert.setTitle("Un problème est apparut.");
			alert.setHeaderText("Certaines vidéos n'ont pu être chargées.");
			StringBuilder liens = new StringBuilder("Les vidéos en questions sont listées ci-dessous :");
			for(Video video : listeVideos) {
				liens.append("\n\t- " + video.getTitre());
			}
			alert.setContentText(liens.toString());

			alert.showAndWait();
		});
	}

	public void showErrorAlertNoVideoFound() {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.getDialogPane().setMinWidth(700);
			alert.setTitle("Aucune vidéo trouvée.");
			alert.setHeaderText("Le lien ne donne sur aucune vidéo téléchargeable");
			alert.setContentText("Le lien que vous avez donné correspond bien à une vidéo youtube mais aucune vidéo n'a pu être trouvée. L'erreur provient peut-être de youtube.");
			alert.showAndWait();
		});
	}

	public void showWarningAlertIsNotPlaylistId(String id) {
		if(id == null) {
			return;
		}
		Platform.runLater(() -> {
			Alert alert = null;
			try {
				alert = new Alert(AlertType.ERROR);
			}
			catch(Exception e) {
				e.printStackTrace();
				return;
			}
			alert.setTitle("Aucune playlist trouvé");
			alert.setHeaderText("Le lien ou l'id donné n'aboutit sur aucune playlist youtube");
			alert.setContentText("Le lien n'est peut-être pas valide");

			alert.showAndWait();

		});
	}

	/**
	 * Affiche une {@link Alert} permettant l'affichage de la liste de raccourcis disponibles dans l'application.
	 */
	public void showRaccourcisInformation() {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.getDialogPane().setMinWidth(700);
			alert.setTitle("Information sur les raccourcis");
			alert.setHeaderText("Liste des raccourcis disponibles");
			StringBuilder raccourcis = new StringBuilder();
			raccourcis.append("\t" + ClavierEventHandler.MODIFIER_COPIER.getKey() + " + " + ClavierEventHandler.KEY_CODE_COPIER.getName()
			+ " -> Copie le lien de la vidéo séléctionnée.");
			raccourcis.append("\n\t" + ClavierEventHandler.MODIFIER_COLLER.getKey() + " + " + ClavierEventHandler.KEY_CODE_COLLER.getName()
			+ " -> Ajoute, si possible, l'URL contenu dans le presse papier à la table.");
			raccourcis.append("\n\t" + ClavierEventHandler.MODIFIER_SUPPRIMER.getKey() + " + " + ClavierEventHandler.KEY_CODE_SUPPRIMER.getName()
			+ " -> Supprime la vidéo séléctionnée de la liste.");
			raccourcis.append("\n\t" + ClavierEventHandler.MODIFIER_INVERSER_HAUT.getKey() + " + " + ClavierEventHandler.KEY_CODE_INVERSER_HAUT.getName()
			+ " -> Monte d'un rang dans la liste la vidéo séléectionnée.");
			raccourcis.append("\n\t" + ClavierEventHandler.MODIFIER_INVERSER_BAS.getKey() + " + " + ClavierEventHandler.KEY_CODE_INVERSER_BAS.getName()
			+ " -> Baisse d'un rang dans la liste la vidéo séléectionnée.");

			alert.setContentText(raccourcis.toString());

			alert.showAndWait();
		});
	}

}
