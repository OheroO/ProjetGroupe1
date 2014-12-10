package classesDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ReservationDB extends Reservation implements CRUD {

protected static Connection dbConnect=null;
	
	public ReservationDB() {
		
	}

// Constructeur paramétré à utiliser avant lors de la création, l'idReservation ne doit pas être précisé, il sera affecté par la base de données lors de la création
	public ReservationDB(int fkVelo, int fkClient, Date dateReservation, String etat) {
		super(0,fkVelo,fkClient,dateReservation,etat);
	}
	
	// Constructeur paramétré à utiliser avant lorsque la réservation est déjà présent dans la base de données
	public ReservationDB(int idReservation, int fkVelo, int fkClient, Date dateReservation, String etat) {
		super(idReservation,fkVelo,fkClient,dateReservation,etat);
	}
	
	// A utiliser lorsque l'on veut lire les infos d'une reservation mais que l'on ne connait que son numéro
	public ReservationDB(int idReservation) {
		super(idReservation,0,0,null,"");
	}

	// méthode statique permettant de partager la connexion entre toutes les instances de ReservationDB
	public static void setConnection(Connection nouvdbConnect) {
	      dbConnect=nouvdbConnect;
	}
	
	@Override
	public void create() throws Exception {
		CallableStatement cstmt = null;
        try {
            String req = "call createreservation(?,?,?,?,?)";
            cstmt = dbConnect.prepareCall(req);
            cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
            cstmt.setInt(2,fkVelo);
            cstmt.setInt(3,fkClient);
            cstmt.setDate(4,dateReservation);
            cstmt.setString(5,etat);
            cstmt.executeUpdate();
            this.idReservation = cstmt.getInt(1);
        } catch (Exception e) {
            throw new Exception("Erreur de création " + e.getMessage());
        } finally {
            try {
                cstmt.close();
            } catch (Exception e) {}
        }
    }

	@Override
    public void read() throws Exception {

		String req = "select * from reservation where idreservation =?"; 
        PreparedStatement  pstmt=null;
        try{
        	pstmt=dbConnect.prepareStatement(req);
            pstmt.setInt(1,idReservation);
     	    ResultSet rs=(ResultSet)pstmt.executeQuery();	
     	    
     	    if(rs.next()){
     	    	this.fkVelo=rs.getInt("IDVELO");
     	    	this.fkClient = rs.getInt("IDCLIENT");
     	    	this.dateReservation=rs.getDate("DATERESA");
     	    	this.etat=rs.getString("ETAT");
     	    }
     	    else { 
     	    	throw new Exception("Code inconnu");
     	    }
        } catch(Exception e) {
        	throw new Exception("Erreur de lecture "+e.getMessage());
        }
        finally {
            try {
              pstmt.close();
            } catch (Exception e){}
        }
     }
	
	public static ArrayList<ReservationDB> rechReservation(String pseudoRech) throws Exception{ 
	    ArrayList<ReservationDB> plusieurs = new ArrayList<ReservationDB>();
	    String req = "select * from reservation where idclient = (select idclient from client where pseudo = ?)";
            
	    PreparedStatement pstmt=null;
	    try{
	    	pstmt = dbConnect.prepareStatement(req);
	    	pstmt.setString(1,pseudoRech);
	    	ResultSet rs=(ResultSet)pstmt.executeQuery();
	    	boolean trouve=false;
            	while(rs.next()){
            		trouve=true;
            		int idReservation=rs.getInt("IDRESERVATION");
            		int fkVelo=rs.getInt("IDVELO");
            		int fkClient=rs.getInt("IDCLIENT");
            		Date dateReservation = rs.getDate("DATERESA");
            		String etat = rs.getString("ETAT");
            		plusieurs.add(new ReservationDB(idReservation,fkVelo,fkClient,dateReservation,etat));
            	}
            	if(!trouve)throw new Exception("Aucune réservation pour ce client");
            	else return plusieurs;
	    } catch(Exception e){
	    	throw new Exception("Erreur de lecture "+e.getMessage());
	    } finally{
	    	try{
              pstmt.close();
            } catch (Exception e){}
        }
     }

	@Override
    public void update() throws Exception {
		CallableStatement cstmt=null;

	    try{
		     String req = "call updatereservation(?,?,?,?,?)";
		     cstmt=dbConnect.prepareCall(req);
		     cstmt.setInt(1,idReservation);
		     cstmt.setInt(2,fkVelo);
		     cstmt.setInt(3,fkClient);
		     cstmt.setDate(4,dateReservation);
		     cstmt.setString(5,etat);
		     cstmt.executeUpdate();  
	    }  catch (Exception e) {
            throw new Exception("Erreur de mise à jour " + e.getMessage());
        } finally {
            try {
                cstmt.close();
            } catch (Exception e) {}
        }
    }


	@Override
    public void delete() throws Exception {
		CallableStatement cstmt =null;
	 	   try{
	 	     String req = "call delreservation(?)";
	 	     cstmt = dbConnect.prepareCall(req);
	 	     cstmt.setInt(1,idReservation);
	 	     cstmt.executeUpdate();
	 	     	     
	 	     } catch (Exception e) {
            throw new Exception("Erreur d'effacement " + e.getMessage());
        } finally {
            try {
            	cstmt.close();
            } catch (Exception e) {}
        }
    }
}
