package classesDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SiteDB extends Site implements CRUD {
	
	protected static Connection dbConnect=null;
	
	public SiteDB() {
		
	}

// Constructeur paramétré à utiliser avant lors de la création, l'idSite ne doit pas être précisé, il sera affecté par la base de données lors de la création
	public SiteDB(int fkEntite, String nom, String typeAdresse, String nomAdresse, int numero, String ville, int empMax, int veloMax) {
		super(0,fkEntite,nom,typeAdresse,nomAdresse,numero,ville,empMax,veloMax);
	}
	
	// Constructeur paramétré à utiliser avant lorsque le site est déjà présent dans la base de données
	public SiteDB(int idSite, int fkEntite, String nom, String typeAdresse, String nomAdresse, int numero, String ville, int empMax, int veloMax) {
		super(idSite, fkEntite,  nom,  typeAdresse,  nomAdresse,  numero,  ville,  empMax,  veloMax);
	}
	
	// A utiliser lorsque l'on veut lire les infos d'un site mais que l'on ne connait que son numéro
	public SiteDB(int idSite) {
		super(idSite,0,"","","",0,"",0,0);
	}

	// méthode statique permettant de partager la connexion entre toutes les instances de SiteDB
	public static void setConnection(Connection nouvdbConnect) {
	      dbConnect=nouvdbConnect;
	}

	public void create() throws Exception{
        CallableStatement   cstmt = null;
       try{
	     String req = "call createsite(?,?,?,?,?,?,?,?,?)";
	     cstmt = dbConnect.prepareCall(req);
         cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
         cstmt.setInt(2, fkEntite);
         cstmt.setString(3,nom);
         cstmt.setString(4,typeAdresse);
         cstmt.setString(5,nomAdresse);
         cstmt.setInt(6,numeroAdresse);
         cstmt.setString(7,ville);
         cstmt.setInt(8,empMax);
         cstmt.setInt(9,veloMax);
         cstmt.executeUpdate();
         this.idSite=cstmt.getInt(1);
       }
       catch(Exception e ){
    	   throw new Exception("Erreur de création "+e.getMessage());
       }
       finally{
            try{
              cstmt.close();
            } catch (Exception e){}
        }
       }

	@Override
    public void read() throws Exception {

		String req = "select * from site where idsite =?"; 
        PreparedStatement  pstmt=null;
        try{
        	pstmt=dbConnect.prepareStatement(req);
            pstmt.setInt(1,idSite);
     	    ResultSet rs=(ResultSet)pstmt.executeQuery();	
     	    
     	    if(rs.next()){
     	    	this.fkEntite=rs.getInt("IDENTITE");
     	    	this.nom = rs.getString("NOM");
     	    	this.typeAdresse=rs.getString("TYPEADRESSE");
     	    	this.nomAdresse=rs.getString("NOMADRESSE");
     	    	this.numeroAdresse=rs.getInt("NUMERO");
     	    	this.ville=rs.getString("Ville");
     	    	this.empMax=rs.getInt("EMPLACEMENTMAX");
     	    	this.veloMax=rs.getInt("VELOMAX");
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
	
	 public int veloReserve(int idSiteRech) throws Exception {
		 	int resultat = 0;
		 	boolean trouve = false;
			String req = "select count(r.idreservation) as 'VeloReserve' from reservation r, velo v, site s where r.idvelo = v.idvelo and r.dateresa= TO_DATE(sysdate, 'DD/MM/YY') and v.idsite = s.idsite and s.idsite=?"; 
	        PreparedStatement  pstmt=null;
	        try{
	        	pstmt=dbConnect.prepareStatement(req);
	            pstmt.setInt(1,idSiteRech);
	     	    ResultSet rs=(ResultSet)pstmt.executeQuery();	
	     	    
	     	    if(rs.next()){
	     	    	resultat = rs.getInt("VeloReserve");
	     	    	System.out.println(resultat);
	     	    }
	     	    if(!trouve)throw new Exception("Aucun site pour cette entité");
	     	    else return resultat;
	        } catch(Exception e) {
	        	throw new Exception("Erreur de lecture "+e.getMessage());
	        }
	        finally {
	            try {
	              pstmt.close();
	            } catch (Exception e){}
	        }
	     }
	
	public static ArrayList<SiteDB> rechSites(String entiteRech) throws Exception{ 
	    ArrayList<SiteDB> plusieurs = new ArrayList<SiteDB>();
	    String req = "select * from site where identite = (select identite from entite where nom = upper(?))";
            
	    PreparedStatement pstmt=null;
	    try{
	    	pstmt = dbConnect.prepareStatement(req);
	    	pstmt.setString(1,entiteRech);
	    	ResultSet rs=(ResultSet)pstmt.executeQuery();
	    	boolean trouve=false;
            	while(rs.next()){
            		trouve=true;
            		int idSite=rs.getInt("IDSITE");
            		int fkEntite=rs.getInt("IDENTITE");
            		String nom=rs.getString("NOM");
            		String typeAdresse = rs.getString("TYPEADRESSE");
            		String nomAdresse = rs.getString("NOMADRESSE");
            		int numero = rs.getInt("NUMERO");
            		String ville=rs.getString("VILLE");
               		int empMax=rs.getInt("EMPLACEMENTMAX");
            		int veloMax=rs.getInt("VELOMAX");
            		plusieurs.add(new SiteDB(idSite,fkEntite,nom,typeAdresse,nomAdresse,numero,ville,empMax,veloMax));
            	}
            	if(!trouve)throw new Exception("Aucun site pour cette entité");
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
		     String req = "call updatesite(?,?,?,?,?,?,?,?,?)";
		     cstmt=dbConnect.prepareCall(req);
		     cstmt.setInt(1,idSite);
		     cstmt.setInt(2, fkEntite);
		     cstmt.setString(3,nom);
		     cstmt.setString(4,typeAdresse);
		     cstmt.setString(5,nomAdresse);
		     cstmt.setInt(6, numeroAdresse);
		     cstmt.setString(7,ville);
		     cstmt.setInt(8,empMax);
	         cstmt.setInt(9,veloMax);
		     cstmt.executeUpdate();
	            
	    } catch (Exception e) {
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
	 	     String req = "call delSite(?)";
	 	     cstmt = dbConnect.prepareCall(req);
	 	     cstmt.setInt(1,idSite);
	 	     cstmt.executeUpdate();
	 	     	     
	 	     }	 catch (Exception e) {
            throw new Exception("Erreur d'effacement " + e.getMessage());
        } finally {
            try {
            	cstmt.close();
            } catch (Exception e) {}
        }
    }

}
