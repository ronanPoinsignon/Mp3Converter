package prog;

import java.util.HashMap;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import exception.PasDeResultatException;
import javafx.scene.control.TextInputDialog;

/**
 * Classe Fourre-tout.
 * @author ronan
 *
 */
public class Utils {

	public static final int TIMEOUT = 3000;
	
	/**
	 * Convertit une Url en une map correspondant aux paramètres de cette Url.
	 * @param query
	 * @return
	 */
	public static HashMap<String, String> getQueryMap(String query) {  
		HashMap<String, String> urlMap=new HashMap<String, String>();
        String queryString=StringUtils.substringAfter(query,"?");
        for(String param : queryString.split("&")){
            urlMap.put(StringUtils.substringBefore(param, "="),StringUtils.substringAfter(param, "="));
        }
        return urlMap;
	}
	
	/**
	 * Récupère l'id de la vidéo Youtube.
	 * @param url
	 * @return
	 */
	public static String getvideoId(String url) {
		String id = Utils.getQueryMap(url).get("v");
		if(id == null) {
			id = url.substring(url.lastIndexOf("/") + 1);
		};
		if(id == null) {
			id = url;
		}
		return id;
	}
	
	/**
	 * Affiche une boîte de dialogue pour permettre l'ajout d'une nouvelle vidéos Youtube en donnant le lien de celle-ci.
	 * @return
	 * @throws PasDeResultatException
	 */
	public static String showInputDIalogAjout() throws PasDeResultatException {
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
	
}
