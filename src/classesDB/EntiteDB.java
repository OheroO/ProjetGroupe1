package classesDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class EntiteDB extends Entite implements CRUD  {
	
	protected static Connection dbConnect=null;
	
		public EntiteDB() {
			
		}
	
	// Constructeur paramétré à utiliser avant lors de la création, l'idEntite ne doit pas être précisé, il sera affecté par la base de données lors de la création
		public EntiteDB(String nom) {
			super(0,nom);
		}
		
		// Constructeur paramétré à utiliser avant lorsque l'entité est déjà présent dans la base de données
		public EntiteDB(int idEntite, String nom) {
			super(idEntite,nom);
		}
		
		// A utiliser lorsque l'on veut lire les infos d'une entité mais que l'on ne connait que son numéro
		public EntiteDB(int idEntite) {
			super(idEntite,"");
		}

		// méthode statique permettant de partager la connexion entre toutes les instances de EntiteDB
		public static void setConnection(Connection nouvdbConnect) {
		      dbConnect=nouvdbConnect;
		}

		public void create() throws Exception{
			CallableStatement   cstmt=null;
		       try{
			   String req = "call createEntite(?,?)";
			     cstmt = dbConnect.prepareCall(req);
		         cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
		         cstmt.setString(2,nom);
			     cstmt.executeUpdate();
		         this.idEntite=cstmt.getInt(1);
		     }
	       catch(Exception e ){
	          
	                throw new Exception("Erreur de création "+e.getMessage());
	             }
	       finally{ 
	            try{
	              cstmt.close();
	            }
	            catch (Exception e){}
	        }
	       }
		
		@Override
	    public void read() throws Exception {

			String req = "select * from entite where identite =?"; 
	        PreparedStatement  pstmt=null;
	        try{
	        	pstmt=dbConnect.prepareStatement(req);
	            pstmt.setInt(1,idEntite);
	     	    ResultSet rs=(ResultSet)pstmt.executeQuery();	
	     	    
	     	    if(rs.next()){
	     	    	this.nom=rs.getString("NOM");
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
		
		public static EntiteDB rechEntite(String nomRech) throws Exception{ 
			EntiteDB resultat = null;
		    String req = "select * from entite where nom = upper(?)";
	            
		    PreparedStatement pstmt=null;
		    try{
		    	pstmt = dbConnect.prepareStatement(req);
		    	pstmt.setString(1,nomRech);
		    	ResultSet rs=(ResultSet)pstmt.executeQuery();
		    	boolean trouve=false;
	            	while(rs.next()){
	            		trouve=true;
	            		int idEntite=rs.getInt("IDENTITE");
	            		String nom=rs.getString("NOM");
	            		resultat = new EntiteDB(idEntite,nom);
	            	}
	            	if(!trouve)throw new Exception("ORA-99999");
	            	else return resultat;
		    } catch(Exception e){
		    	throw new Exception(e.getMessage());
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
			     String req = "call updateEntite(?,?)";
			     cstmt=dbConnect.prepareCall(req);
			     cstmt.setInt(1,idEntite);
			     cstmt.setString(2,nom);
			     cstmt.executeUpdate();
		    }catch (Exception e) {
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
		 	     String req = "call delentite(?)";
		 	     cstmt = dbConnect.prepareCall(req);
		 	     cstmt.setInt(1,idEntite);
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
