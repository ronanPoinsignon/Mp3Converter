package prog.video;

import java.io.Serializable;

public abstract class Video implements Serializable, Convertissable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String titre, lien;
	
	public Video() {
		
	}
	
	public Video(String titre, String lien) {
		this.titre = titre;
		this.lien = lien;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}
	
	
	public String getLien() {
		return lien;
	}

	public void setLien(String lien) {
		this.lien = lien;
	}
	
}
