package classesDB;

public class Site {

	protected int idSite;
	protected int fkEntite;
	protected String nom;
	protected String typeAdresse;
	protected String nomAdresse;
	protected int numeroAdresse;
	protected String ville;
	protected int empMax;
	protected int veloMax;
	
	public Site() {
		
	}
	
	public Site(int idSite, int fkEntite, String nom, String typeAdresse,
			String nomAdresse, int numeroAdresse, String ville, int empMax,
			int veloMax) {
		super();
		this.idSite = idSite;
		this.fkEntite = fkEntite;
		this.nom = nom;
		this.typeAdresse = typeAdresse;
		this.nomAdresse = nomAdresse;
		this.numeroAdresse = numeroAdresse;
		this.ville = ville;
		this.empMax = empMax;
		this.veloMax = veloMax;
	}

	public int getIdSite() {
		return idSite;
	}

	public void setIdSite(int idSite) {
		this.idSite = idSite;
	}

	public int getFkEntite() {
		return fkEntite;
	}

	public void setFkEntite(int fkEntite) {
		this.fkEntite = fkEntite;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getTypeAdresse() {
		return typeAdresse;
	}

	public void setTypeAdresse(String typeAdresse) {
		this.typeAdresse = typeAdresse;
	}

	public String getNomAdresse() {
		return nomAdresse;
	}

	public void setNomAdresse(String nomAdresse) {
		this.nomAdresse = nomAdresse;
	}

	public int getNumeroAdresse() {
		return numeroAdresse;
	}

	public void setNumeroAdresse(int numeroAdresse) {
		this.numeroAdresse = numeroAdresse;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public int getEmpMax() {
		return empMax;
	}

	public void setEmpMax(int empMax) {
		this.empMax = empMax;
	}

	public int getVeloMax() {
		return veloMax;
	}

	public void setVeloMax(int veloMax) {
		this.veloMax = veloMax;
	}

	@Override
	public String toString() {
		return "Site [idSite=" + idSite + ", fkEntite=" + fkEntite + ", nom="
				+ nom + ", typeAdresse=" + typeAdresse + ", nomAdresse="
				+ nomAdresse + ", numeroAdresse=" + numeroAdresse + ", ville="
				+ ville + ", empMax=" + empMax + ", veloMax=" + veloMax + "]";
	}
	
	
}
