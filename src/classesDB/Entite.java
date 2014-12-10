package classesDB;

public class Entite {
	protected int idEntite;
	protected String nom;
	
	public Entite() {
		
	}
	
	public Entite(int idEntite, String nom) {
		super();
		this.idEntite = idEntite;
		this.nom = nom;
	}

	public int getIdEntite() {
		return idEntite;
	}

	public void setIdEntite(int idEntite) {
		this.idEntite = idEntite;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public String toString() {
		return "Entite [idEntite=" + idEntite + ", nom=" + nom + "]";
	}

}
