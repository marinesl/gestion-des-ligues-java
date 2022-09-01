package commandLine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Connexion {
	
	private static Connection conn;
	
	static
	{	
		// Chargement du Driver
	    try {
	    	Class.forName("com.mysql.jdbc.Driver").newInstance();
	    }
	    catch (Exception ex) {
	    	System.out.println("Erreur du chargement de Driver");
	    }
	       
	    //Connexion à la Base de Données	       
	    try {
	  		String bdd = "m2l_personnel"; //nom base de données
	    	String url = "jdbc:mysql://localhost/"+bdd; //URL de connexion à la base de données
	    	String user = "root"; // identifiant MySQL
	    	String passwd = ""; //mot de passe MySQL

	    	conn = DriverManager.getConnection(url, user, passwd);   
	    } 
	    catch (Exception e) {
   	    	System.out.println("Erreur de connexion à la BDD");
   	    }
	}
	
	
	// Requête récupérant le mot de passe du SuperAdmin
	public static String password() throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet tableUtilisateur = statement.executeQuery("SELECT password FROM utilisateur WHERE superAdmin = 1;");
		 
		if (tableUtilisateur.next()) {
  	       	String password = tableUtilisateur.getString("password");
  	      	return tableUtilisateur.getString("password");
  	   	}
		return null;
	}
	
	// Requête retournant la liste des ligues
	public static ArrayList<String> ligue() throws SQLException {	
		ArrayList<String> listeLigue = new ArrayList<>();
		Statement statement = conn.createStatement();
		ResultSet tableLigue = statement.executeQuery("SELECT ligue,prenom,nom,idLigue FROM ligue,utilisateur WHERE utilisateur.idUtilisateur = ligue.idAdmin;");
			
		//Récupération des valeurs 
		while (tableLigue.next()) {
			String ligue = tableLigue.getString("ligue");
			listeLigue.add(ligue);
			String prenom = tableLigue.getString("prenom");
			listeLigue.add(prenom);
			String nom = tableLigue.getString("nom");
			listeLigue.add(nom);
			String id = tableLigue.getString("idLigue");
			listeLigue.add(id);
		}
		return listeLigue;
	}
	
	// Requête retournant la liste des Admin des ligues
	public static ArrayList<String> admin() throws SQLException {
		ArrayList<String> listeAdmin = new ArrayList<>();			
		Statement statement = conn.createStatement();
		ResultSet tableAdmin = statement.executeQuery("SELECT * FROM utilisateur WHERE admin = 1");
			
		//Récupération des valeurs 
		while (tableAdmin.next()) {
			String prenom = tableAdmin.getString("prenom");
			listeAdmin.add(prenom);
			String nom = tableAdmin.getString("nom");
			listeAdmin.add(nom);
			String id = tableAdmin.getString("idUtilisateur");
			listeAdmin.add(id);
		}
		return listeAdmin;
	}

	// Requête ajoutant une ligue
	public static void ajoutLigue(String ligue, String admin) throws SQLException {
		Statement statement = conn.createStatement();
		int statut = statement.executeUpdate("INSERT INTO ligue (ligue) VALUES ('" + ligue + "');");
		
		String[] listeAdmin = admin.split(" ");
		String nomAdmin = listeAdmin[0];
		String prenomAdmin = listeAdmin[1];
	
		ResultSet tableAdmin = statement.executeQuery("SELECT idUtilisateur FROM utilisateur WHERE nom='" + nomAdmin + "' AND prenom='" + prenomAdmin + "';");
		
		ArrayList<String> listeUser = new ArrayList<>();
		
		//Récupération des valeurs 
		while (tableAdmin.next()) {
			String id = tableAdmin.getString("idUtilisateur");
			listeUser.add(id);	
		}
		
		for(int i = 0 ; i < listeUser.size() ; i++) {
			int statut2 = statement.executeUpdate("UPDATE ligue SET idAdmin=" + listeUser.get(i) + " WHERE ligue='" + ligue + "';");
		}
	}
			
	//Requête ajoute un admin
	public static void ajoutAdmin(String nom, String prenom, String mail, String password) throws SQLException {
		Statement statement = conn.createStatement();
		int statut = statement.executeUpdate("INSERT INTO utilisateur (nom, prenom, mail, password, admin) VALUES ('" + nom + "','" + prenom + "','" + mail + "','" + password + "',1);");
	}
	
	// Requête modifiant l'admin
	public static void modificationAdmin(String nom, String prenom, String id) throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet tableAdmin = statement.executeQuery("SELECT nom,prenom FROM utilisateur WHERE idUtilisateur=" + id + ";");
		ArrayList<String> listeUser = new ArrayList<>();
		
		//Récupération des valeurs 
		while (tableAdmin.next()) {
			String nomAdmin = tableAdmin.getString("nom");
			listeUser.add(nomAdmin);
			String prenomAdmin = tableAdmin.getString("prenom");
			listeUser.add(prenomAdmin);
		}
		
		// Modification
		int statut = 0;
		if(nom != listeUser.get(0))
			statut = statement.executeUpdate("UPDATE utilisateur SET nom='" + nom + "' WHERE idUtilisateur=" + id + ";");
		if(prenom != listeUser.get(1))
			statut = statement.executeUpdate("UPDATE utilisateur SET prenom='" + prenom + "' WHERE idUtilisateur=" + id + ";");
	}
		
	// Requête modifiant l'admin
	public static void modificationLigue(String ligue, String adminOld, String adminNew, String id) throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet tableLigue = statement.executeQuery("SELECT ligue FROM ligue WHERE idLigue=" + id + ";");
		ArrayList<String> listeLigue = new ArrayList<>();
		
		//Récupération des valeurs 
		while (tableLigue.next()) {
			String nomLigue = tableLigue.getString("ligue");
			listeLigue.add(nomLigue);
		}
		
		// Modification
		int statut = 0;
		if(ligue != listeLigue.get(0))
			statut = statement.executeUpdate("UPDATE ligue SET ligue='" + ligue + "' WHERE idLigue=" + id + ";");
		if(adminNew != " ") {
			if(adminOld != adminNew) {
				String[] admin = adminNew.split(" ");
				ResultSet tableAdmin = statement.executeQuery("SELECT idUtilisateur FROM utilisateur WHERE nom='" + admin[0] + "' AND prenom='" + admin[1] + "';");
				ArrayList<String> listeAdmin = new ArrayList<>();
				//Récupération des valeurs 
				while (tableAdmin.next()) {
					String idAdmin = tableAdmin.getString("idUtilisateur");
					listeAdmin.add(idAdmin);
				}
				statut = statement.executeUpdate("UPDATE ligue SET idAdmin='" + listeAdmin.get(0) + "' WHERE ligue='" + ligue + "';");
			}
		}
	}
	
	// Requête de suppression de la ligue
	public static void supprimerLigue(String id) throws SQLException {
		Statement statement = conn.createStatement();
		int statut = statement.executeUpdate("DELETE FROM ligue WHERE idLigue = " + id + ";");
	}
	
	// Requête de suppression de l'administrateur
		public static void supprimerAdmin(String id) throws SQLException {
			Statement statement = conn.createStatement();
			int statut = statement.executeUpdate("DELETE FROM utilisateur WHERE idUtilisateur = " + id + ";");
		}
	
}
