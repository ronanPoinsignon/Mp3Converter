package prog.video;

import java.io.Serializable;

/**
 * Classe abstraite symbolisant une vidéo.
 * @author ronan
 *
 */
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lien == null) ? 0 : lien.hashCode());
		result = prime * result + ((titre == null) ? 0 : titre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Video other = (Video) obj;
		if (lien == null) {
			if (other.lien != null)
				return false;
		} else if (!lien.equals(other.lien))
			return false;
		if (titre == null) {
			if (other.titre != null)
				return false;
		} else if (!titre.equals(other.titre))
			return false;
		return true;
	}
	
}
