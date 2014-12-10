package classesDB;

public class Velo {
	protected int idVelo;
	protected int fkSite; 
	protected String etat;
	
	public Velo() {
		
	}

	public Velo(int idVelo, int fkSite, String etat) {
		super();
		this.idVelo = idVelo;
		this.fkSite = fkSite;
		this.etat = etat;
	}

	public int getIdVelo() {
		return idVelo;
	}

	public void setIdVelo(int idVelo) {
		this.idVelo = idVelo;
	}

	public int getFkSite() {
		return fkSite;
	}

	public void setFkSite(int fkSite) {
		this.fkSite = fkSite;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	@Override
	public String toString() {
		return "Velo [idVelo=" + idVelo + ", fkSite=" + fkSite + ", etat="
				+ etat + "]";
	}
	
	
}

