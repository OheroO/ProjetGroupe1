package classesDB;

import java.sql.Date;

public class Reservation {
	protected int idReservation;
	protected int fkVelo;
	protected int fkClient;
	protected Date dateReservation;
	protected String etat;
	
	public Reservation() {
		
	}

	public Reservation(int idReservation, int fkVelo, int fkClient, Date dateReservation, String etat) {
		super();
		this.idReservation = idReservation;
		this.fkVelo = fkVelo;
		this.fkClient=fkClient;
		this.dateReservation = dateReservation;
		this.etat = etat;
	}

	public int getFkClient() {
		return fkClient;
	}

	public void setFkClient(int fkClient) {
		this.fkClient = fkClient;
	}

	public int getIdReservation() {
		return idReservation;
	}

	public void setIdReservation(int idReservation) {
		this.idReservation = idReservation;
	}

	public int getFkVelo() {
		return fkVelo;
	}

	public void setFkVelo(int fkVelo) {
		this.fkVelo = fkVelo;
	}

	public Date getDateReservation() {
		return dateReservation;
	}

	public void setDateReservation(Date dateReservation) {
		this.dateReservation = dateReservation;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	@Override
	public String toString() {
		return "Reservation [idReservation=" + idReservation + ", fkVelo="
				+ fkVelo + ", fkClient=" + fkClient + ", dateReservation="
				+ dateReservation + ", etat=" + etat + "]";
	}
	
	

}
