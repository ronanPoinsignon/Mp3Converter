package prog;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

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
	
}
