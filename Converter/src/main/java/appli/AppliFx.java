package appli;

import affichage.demande.Selection;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe main de l'application FX.
 * @author ronan
 *
 */
public class AppliFx extends Application {

	/**
	 * Main FX.
	 * @param args
	 */
	public static void mainFx(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {
		stage.setResizable(false);
		Group root = new Group();
		root.getChildren().add(new Selection(stage));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Sélection");
		stage.show();
	}

}
