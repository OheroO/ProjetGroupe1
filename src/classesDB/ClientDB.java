package classesDB;

import java.sql.*;
import java.util.*;

import android.util.Log;

public class ClientDB extends Client implements CRUD {
	/**
	 * connexion à la base de données partagée entre toutes les
	 * instances(statique)
	 */
	protected static Connection dbConnect = null;

	/**
	 * constructeur par défaut
	 */
	public ClientDB() {
	}

	public ClientDB(String pseudo, String password, String email, String nom,String prenom) {
		super(0, pseudo, password, email, nom, prenom);
	}

	public ClientDB(int idclient, String pseudo, String password, String email,String nom, String prenom) {
		super(idclient, pseudo, password, email, nom, prenom);
	}

	public ClientDB(int idclient) {
		super(idclient, "", "", "", "", "");
	}

	/**
	 * méthode statique permettant de partager la connexion entre toutes les
	 * instances de ClientDB
	 * 
	 * @param nouvdbConnect
	 *            connexion à la base de données
	 */
	public static void setConnection(Connection nouvdbConnect) {
		dbConnect = nouvdbConnect;
	}

	/**
	 * enregistrement d'un nouveau client dans la base de données
	 * 
	 * @throws Exception
	 *             erreur de création
	 */
	public void create() throws Exception{
		 CallableStatement   cstmt=null;
	       try{
		   String req = "call createclient(?,?,?,?,?,?)";
		     cstmt = dbConnect.prepareCall(req);
	         cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
	         cstmt.setString(2,pseudo);
		     cstmt.setString(3,password);
		     cstmt.setString(4,email);
		     cstmt.setString(5,nom);
		     cstmt.setString(6,prenom);
		     cstmt.executeUpdate();
	         this.idclient=cstmt.getInt(1);
	     }
      catch(Exception e ){
               throw new Exception(e.getMessage());
            }
      finally{
           try{
             cstmt.close();
           }
           catch (Exception e){}
       }
      }

	/**
	 * récupération des données d'un client sur base de son identifiant
	 * 
	 * @throws Exception
	 *             code inconnu
	 */
	public void read() throws Exception {

		String req = "select * from client where IDCLIENT =?";
		// les callable statements retournant un resultset ne fonctionnent pas
		// bien => select classique
		PreparedStatement pstmt = null;
		try {

			pstmt = dbConnect.prepareStatement(req);
			pstmt.setInt(1, idclient);
			ResultSet rs = (ResultSet) pstmt.executeQuery();

			if (rs.next()) {
				this.pseudo = rs.getString("PSEUDO");
				this.password = rs.getString("PASSWORD");
				this.email = rs.getString("EMAIL");
				this.nom = rs.getString("NOM");
				this.prenom = rs.getString("PRENOM");
			} else {
				throw new Exception("Code inconnu");
			}

		} catch (Exception e) {
			Log.d("connexion", "erreur" + e); // Qu'est ce que c'est que ça Etienne ? 
			throw new Exception("Erreur de lecture " + e.getMessage());
		} finally {// effectué dans tous les cas
			try {
				pstmt.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * méthode statique permettant de récupérer tous les clients portant un
	 * certain nom
	 * 
	 * @param nomrech
	 *            nom recherché
	 * @return liste de clients
	 * @throws Exception
	 *             nom inconnu
	 */
	public static ArrayList<ClientDB> rechNom(String nomrech) throws Exception {
		ArrayList<ClientDB> plusieurs = new ArrayList<ClientDB>();
		String req = "select * from client where nom =?";
		// les callable statements retournant un resultset ne fonctionnent pas
		// bien => select classique

		PreparedStatement pstmt = null;
		try {
			pstmt = dbConnect.prepareStatement(req);
			pstmt.setString(1, nomrech);
			ResultSet rs = (ResultSet) pstmt.executeQuery();
			boolean trouve = false;
			while (rs.next()) {
				trouve = true;
				int idclient = rs.getInt("IDCLIENT");
				String pseudo = rs.getString("PSEUDO");
				String password = rs.getString("PASSWORD");
				String email = rs.getString("EMAIL");
				String nom = rs.getString("NOM");
				String prenom = rs.getString("PRENOM");
				plusieurs.add(new ClientDB(idclient, pseudo, password, email,nom, prenom));
			}

			if (!trouve)
				throw new Exception("nom inconnu");
			else
				return plusieurs;
		} catch (Exception e) {
			throw new Exception("Erreur de lecture " + e.getMessage());
		} finally {// effectué dans tous les cas
			try {
				pstmt.close();
			} catch (Exception e) {
			}
		}
	}

	public static ClientDB rechPseudo(String pseudorech) throws Exception {
		String req = "select * from client where pseudo =?";
		// les callable statements retournant un resultset ne fonctionnent pas
		// bien => select classique

		PreparedStatement pstmt = null;
		try {
			ClientDB cli=new ClientDB();
			pstmt = dbConnect.prepareStatement(req);
			pstmt.setString(1, pseudorech);
			ResultSet rs = (ResultSet) pstmt.executeQuery();
			boolean trouve = false;
			while (rs.next()) {
				trouve = true;
				int idClient=rs.getInt("IDCLIENT");
        		String pseudo = rs.getString("PSEUDO");
        		String password = rs.getString("PASSWORD");
        		String email = rs.getString("EMAIL");
        		String prenom=rs.getString("PRENOM");
        		String nom=rs.getString("NOM");
        		cli = new ClientDB(idClient,pseudo,password,email,nom,prenom);
			}
			
			if (!trouve)
				throw new Exception("Pseudo inconnu"); // LANGUE ?
			else {
				return cli;
			}	
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {// effectué dans tous les cas
			try {
				pstmt.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * mise à jour des données du client sur base de son identifiant
	 * 
	 * @throws Exception
	 *             erreur de mise à jour
	 */

	public void update() throws Exception {
		CallableStatement cstmt = null;

		try {
			String req = "call updateclient(?,?,?,?,?,?)";
			cstmt = dbConnect.prepareCall(req);
			PreparedStatement pstm = dbConnect.prepareStatement(req);
			cstmt.setInt(1, idclient);
			cstmt.setString(2, pseudo);
			cstmt.setString(3, password);
			cstmt.setString(3, email);
			cstmt.setString(5, nom);
			cstmt.setString(6, prenom);
			cstmt.executeUpdate();
		}
		catch (Exception e) {
			throw new Exception("Erreur de mise à jour " + e.getMessage());
		} finally {// effectué dans tous les cas
			try {
				cstmt.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * effacement du client sur base de son identifiant
	 * 
	 * @throws Exception
	 *             erreur d'effacement
	 */
	public void delete() throws Exception {

		CallableStatement cstmt = null;
		try {
			String req = "call delclient(?)";
			cstmt = dbConnect.prepareCall(req);
			cstmt.setInt(1, idclient);
			cstmt.executeUpdate();

		} catch (Exception e) {
			throw new Exception("Erreur d'effacement " + e.getMessage());
		} finally {// effectué dans tous les cas
			try {
				cstmt.close();
			} catch (Exception e) {
			}
		}
	}

}
