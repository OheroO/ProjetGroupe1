package classesDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class VeloDB extends Velo implements CRUD {

	protected static Connection dbConnect=null;
	
	public VeloDB() {
		
	}
	
	// Constructeur param�tr� � utiliser avant lors de la cr�ation, l'idVelo ne doit pas �tre pr�cis�, il sera affect� par la base de donn�es lors de la cr�ation
	public VeloDB(int fkSite, String etat) {
		super(0,fkSite,etat);
	}
	
	// Constructeur param�tr� � utiliser avant lorsque le v�lo est d�j� pr�sent dans la base de donn�es
	public VeloDB(int idVelo, int fkSite, String etat) {
		super(idVelo, fkSite, etat);
	}
	
	// A utiliser lorsque l'on veut lire les infos d'un v�lo mais que l'on ne connait que son num�ro
	public VeloDB(int idVelo) {
		super(idVelo,0,"");
	}

	// m�thode statique permettant de partager la connexion entre toutes les instances de VeloDB
	public static void setConnection(Connection nouvdbConnect) {
	      dbConnect=nouvdbConnect;
	}

	@Override
	public void create() throws Exception {
		CallableStatement   cstmt=null;
	       try{
		   String req = "call createvelo(?,?,?)";
		     cstmt = dbConnect.prepareCall(req);
	         cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	         cstmt.setInt(2,fkSite);
		     cstmt.setString(3,etat);
		     cstmt.executeUpdate();
	         this.idVelo=cstmt.getInt(1);
	     } catch (Exception e) {
            throw new Exception("Erreur de cr�ation " + e.getMessage());
        } finally {
            try {
                cstmt.close();
            } catch (Exception e) {}
        }
    }
	
	@Override
    public void read() throws Exception {

		String req = "select * from velo where idvelo =?"; 
        PreparedStatement  pstmt=null;
        try{
        	pstmt=dbConnect.prepareStatement(req);
            pstmt.setInt(1,idVelo);
     	    ResultSet rs=(ResultSet)pstmt.executeQuery();	
     	    
     	    if(rs.next()){
     	    	this.fkSite=rs.getInt("IDSITE");
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
	
	public static ArrayList<VeloDB> rechVelosDispo(int siteRech) throws Exception{ 
	    ArrayList<VeloDB> plusieurs = new ArrayList<VeloDB>();
	    String req = "select * from velo where idsite =? and etat='D'";
            
	    PreparedStatement pstmt=null;
	    try{
	    	pstmt = dbConnect.prepareStatement(req);
	    	pstmt.setInt(1,siteRech);
	    	ResultSet rs=(ResultSet)pstmt.executeQuery();
	    	boolean trouve=false;
            	while(rs.next()){
            		trouve=true;
            		int idVelo=rs.getInt("IDVELO");
            		int fkSite=rs.getInt("IDSITE");
            		String etat=rs.getString("ETAT");
            		plusieurs.add(new VeloDB(idVelo,fkSite,etat));
            	}
            	if(!trouve)throw new Exception("Aucun v�lo pour ce site");
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
		     String req = "call updateclient(?,?,?)";
		     cstmt=dbConnect.prepareCall(req);
		     cstmt.setInt(1,idVelo);
		     cstmt.setInt(2,fkSite);
		     cstmt.setString(3,etat);
		     cstmt.executeUpdate();
	            
	    }  catch (Exception e) {
            throw new Exception("Erreur de mise � jour " + e.getMessage());
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
	 	     String req = "call delVelo(?)";
	 	     cstmt = dbConnect.prepareCall(req);
	 	     cstmt.setInt(1,idVelo);
	 	     cstmt.executeUpdate();
	 	     	     
	 	     }	catch (Exception e) {
            throw new Exception("Erreur d'effacement " + e.getMessage());
        } finally {
            try {
            	cstmt.close();
            } catch (Exception e) {}
        }
    }

}
