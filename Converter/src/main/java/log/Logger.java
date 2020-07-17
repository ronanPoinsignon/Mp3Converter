package log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import prog.Video;

/**
 * Classe de log permettant l'affichage d' {@link Alert} d'erreur ou de mauvaise manipulations.
 * @author ronan
 *
 */
public class Logger {

	private static Logger logger = null;
	
	private boolean canShowWarning = true;
	
	public static Logger getInstance() {
		if(logger == null)
			logger = new Logger();
		return logger;
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
	 * Affiche une {@link Alert} pour avertir l'utilisateur que la vidéo 
	 * qu'il souhaite ajouter à la liste est déjà présente.
	 * @param listeVideos
	 */
	public void showWarningAlertVideosDejaPresentes(List<Video> listeVideos) {
		if(listeVideos.isEmpty())
			return;
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Vidéos déjà présentes");
		
		alert.setHeaderText("Certaienes vidéos sont déjà présentes dans la liste");
		StringBuilder liste = new StringBuilder("Les vidéos suivantes sont déjà présentes :");
		for(Video video : listeVideos)
			liste.append("\n\t- " + video.getTitre());
		
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
	
	/**
	 * Affiche une {@link Alert} d'erreur pour informer l'utilisateur 
	 * que certaines vidéos ont eu un problème lors de leur chargement.
	 * @param listeUrlsMauvaisLien
	 * @param listeUrlsErreur
	 */
	public void showErrorAlertVideosNonChargees(List<String> listeUrlsMauvaisLien, List<String> listeUrlsErreur) {
		if(listeUrlsErreur.isEmpty() && listeUrlsMauvaisLien.isEmpty())
			return;
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Des problèmes sont apparus durant le chargement de vos vidéos");
		alert.setHeaderText("Certaines vidéos n'ont pu être chargées");
		StringBuilder probleme = new StringBuilder();
		if(!listeUrlsMauvaisLien.isEmpty()) {
			probleme.append("Les url ci-dessous ne correspondent pas à un lien valide venant de youtube :");
			for(String url : listeUrlsMauvaisLien)
				probleme.append("\n\t- " + url);
		}
		if(!listeUrlsErreur.isEmpty()) {
			probleme.append("\nLes urls ci-dessous ont apporté un problème lors de leur chargement :");
			for(String url : listeUrlsErreur)
				probleme.append("\n\t- " + url);
		}
		alert.setContentText(probleme.toString());

		alert.showAndWait();
	}
	
	/**
	 * Affiche une {@link Alert} d'erreur lorsque qu'une erreur est apparue.
	 * @param e
	 */
	public void showErrorAlert(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
		alert.setHeaderText("Un problème est apparu");
		alert.setContentText("Veuillez redémarrer l'application");

		// Create expandable Exception.
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
	
}