package classesDB;

public class Client {
	// identifiant unique du client
	protected int idclient;
	// pseudo du client
	protected String pseudo;
	// password du client
	protected String password;
	// email du client
	protected String email;
	// nom du client
	protected String nom;
	// prenom du client
	protected String prenom;

	/**
	 * constructeur par défaut
	 */
	public Client() {

	}

	

	public Client(int idclient, String pseudo, String password, String email,
			String nom, String prenom) {
		super();
		this.idclient = idclient;
		this.pseudo = pseudo;
		this.password = password;
		this.email = email;
		this.nom = nom;
		this.prenom = prenom;
	}


	public int getIdclient() {
		return idclient;
	}

	
	public void setIdclient(int idclient) {
		this.idclient = idclient;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	@Override
	public String toString() {
		return "Client [idclient=" + idclient + ", pseudo=" + pseudo
				+ ", password=" + password + ", email=" + email + ", nom="
				+ nom + ", prenom=" + prenom + "]";
	}

}
