package commandLine;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class PersonnelGraphique {
	
	private JFrame frame;
	private Connexion cnx;
	
	public PersonnelGraphique() {
		// Connexion à la base de données
		cnx = new Connexion();
		
		frame = new JFrame();
		frame.setTitle("Gestion du personnel des ligues");
		frame.setSize(500, 525);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	
		formConnexion();
	}
	
	
	/**
	 * Panel de la connexion du super administrateur
	 */
	public void formConnexion() {
		// Efface le contenu du panel précédent
		frame.getContentPane().removeAll();
		frame.setLayout(null);
		
		// Création des éléments du panel
		JLabel titre = new JLabel("Connexion administrateur");
		titre.setBounds(170,100,200,15);
		JLabel message = new JLabel("");
		message.setBounds(130,250,200,15);
		JLabel password = new JLabel("Mot de passe :");
		password.setBounds(70,150,200,15);
		JPasswordField textPassword  = new JPasswordField(10);
		textPassword.setBounds(200,150,200,20);
		JButton buttonConnexion = new JButton("Connexion");
		buttonConnexion.setBounds(170,200,150,20);
		
		// Evénement : connexion à l'application -> Affichage du menu
		buttonConnexion.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					char[] c = textPassword.getPassword();
					String s = new String(c);
					if(formConnect(s))
						menuAdmin();
					else {
						message.setText("Mot de passe incorrect !");
					}
				} catch (Exception ex) {
					message.setText("Mot de passe incorrect !");
				}	
			}
			
		});
		
		frame.getContentPane().add(titre);
		frame.getContentPane().add(password);
		frame.getContentPane().add(textPassword);
		frame.getContentPane().add(buttonConnexion);
		frame.getContentPane().add(message);		
		
		
		frame.validate();
		frame.repaint();
	}
	
	
	/**
	 * Panel du menu du super administrateur
	 */
	public void menuAdmin() {
		// Efface le contenu du panel précédent
		frame.getContentPane().removeAll();
		frame.setLayout(null);
		
		// Création des objets du panel
		JLabel titre = new JLabel("Menu administrateur");
		titre.setBounds(180,100,200,15);
		JButton buttonListeLigue = new JButton("Les ligues");
		buttonListeLigue.setBounds(140,150,200,20);
		JButton buttonAjouterLigue = new JButton("Ajouter une ligue");
		buttonAjouterLigue.setBounds(140,200,200,20);
		JButton buttonListeUser = new JButton("Les administrateur");
		buttonListeUser.setBounds(140,250,200,20);
		JButton buttonAjouterUser = new JButton("Ajouter un administrateur");
		buttonAjouterUser.setBounds(140,300,200,20);
		JButton buttonDeconnect = new JButton("Déconnexion");
		buttonDeconnect.setBounds(140,350,200,20);
		
		// Evénement : affichage de la liste des ligues
		buttonListeLigue.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					listeLigue();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
		});
		
		// Evénement : affichage du formulaire d'ajout d'une ligue
		buttonAjouterLigue.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					ajoutLigue();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
		});
		
		// Evénement : affichage de la liste des administrateur
		buttonListeUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					listeAdministrateurs();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
		});
		
		// Evénement : affichage du formulaire d'ajout d'un administrateur
		buttonAjouterUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ajoutAdmin();				
			}
			
		});
		
		// Evénement : déconnexion -> affichage du formulaire de connexion
		buttonDeconnect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				formConnexion();				
			}
			
		});

		// Ajout des objets au panel
		frame.getContentPane().add(titre);
		frame.getContentPane().add(buttonListeLigue);
		frame.getContentPane().add(buttonAjouterLigue);
		frame.getContentPane().add(buttonListeUser);
		frame.getContentPane().add(buttonAjouterUser);
		frame.getContentPane().add(buttonDeconnect);
		
		
		frame.validate();
		frame.repaint();
	}
	
	
	/**
	 * Panel de la liste des ligues
	 * @throws SQLException
	 */
	public void listeLigue() throws SQLException {
		// Efface le contenu du panel précédent
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new FlowLayout());
		
		// Création des objets du panel
		JLabel titre = new JLabel("Les ligues");	
		URL urlSupp = getClass().getResource("logo_supp.png"); 
		Icon iconeSupp = new ImageIcon(urlSupp);
		JButton buttonSuppLigue = new JButton(iconeSupp);
		URL urlModif = getClass().getResource("logo_modif.png"); 
		Icon iconeModif = new ImageIcon(urlModif);
		JButton buttonModifLigue = new JButton(iconeModif);
		JButton buttonAjoutLigue = new JButton("Ajouter");
		JButton buttonMenu = new JButton("Retour au menu");
		
		// Création du tableau
		int taille = (cnx.ligue().size()) / 4;
		Object[][] donneesLigue = new Object[taille][3];
		ArrayList listeLigue = new ArrayList(cnx.ligue());
		int n = 0;
		for(int i = 0 ; i < taille ; i++) {
			donneesLigue[i][0] = listeLigue.get(n + 3); 
			donneesLigue[i][1] = listeLigue.get(n); 
			donneesLigue[i][2] = listeLigue.get(n + 1) + " " + listeLigue.get(n + 2);
			n += 4;
		}		
        String entetesLigue[] = {"Numéro", "Nom", "Administrateur"};
        JTable tableauLigue = new JTable(donneesLigue, entetesLigue);
		
		// Evénement : modification de la ligue
		buttonModifLigue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int ligne = tableauLigue.getSelectedRow();
					String nom = (String) donneesLigue[ligne][1];
					String admin = (String) donneesLigue[ligne][2];
					String id = (String) donneesLigue[ligne][0];
					modifLigue(nom,admin,id);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		// Evénement : suppression de la ligue
		buttonSuppLigue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {	
				int ligne = tableauLigue.getSelectedRow();
				String id = (String) donneesLigue[ligne][0];
				try {
					cnx.supprimerLigue(id);
					listeLigue();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		// Evénement : affichage du formulaire d'ajout d'une ligue
		buttonAjoutLigue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					ajoutLigue();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
				
		// Evénement : retour au menu
		buttonMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				menuAdmin();
			}
			
		});
		
		// Ajout des objets au panel
		frame.getContentPane().add(titre);		
        frame.getContentPane().add(new JScrollPane(tableauLigue));
        frame.getContentPane().add(buttonAjoutLigue);
        frame.getContentPane().add(buttonModifLigue);
        frame.getContentPane().add(buttonSuppLigue);
        frame.getContentPane().add(buttonMenu);
		
        
		frame.validate();
		frame.repaint();
	}
	
	
	/**
	 * Panel de la liste des administrateurs
	 * @throws SQLException 
	 */
	public void listeAdministrateurs() throws SQLException {
		// Efface le contenu du panel précédent
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new FlowLayout());
		
		// Création des objets du panel
		JLabel titre = new JLabel("Les administrateurs");
		URL urlModif = getClass().getResource("logo_modif.png"); 
		Icon iconeModif = new ImageIcon(urlModif);
		JButton buttonModifAdmin = new JButton(iconeModif);
		URL urlSupp = getClass().getResource("logo_supp.png"); 
		Icon iconeSupp = new ImageIcon(urlSupp);
		JButton buttonSuppAdmin = new JButton(iconeSupp);
		JButton buttonAjoutAdmin = new JButton("Ajouter");
		JButton buttonMenu = new JButton("Retour au menu");
		
		// Création du tableau
		int taille = (cnx.admin().size()) / 3;
		Object[][] donneesAdmin = new Object[taille][3];
		ArrayList listeAdmin = new ArrayList(cnx.admin());
		int n = 0;
		for(int i = 0 ; i < taille ; i++) {
			donneesAdmin[i][0] = listeAdmin.get(n + 2); 
			donneesAdmin[i][1] = listeAdmin.get(n + 1); 
			donneesAdmin[i][2] = listeAdmin.get(n);
			n += 3;
		}	
        String entetesAdmin[] = {"Numéro", "Nom", "Prénom"};
        JTable tableauUser = new JTable(donneesAdmin, entetesAdmin);
        
        // Evénement : modification de l'administrateur
        buttonModifAdmin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int ligne = tableauUser.getSelectedRow();
				String nom = (String) donneesAdmin[ligne][1];
				String prenom = (String) donneesAdmin[ligne][2];
				String id = (String) donneesAdmin[ligne][0];
				modifAdmin(nom,prenom,id);
			}
        	
        });
        
        // Evénement : suppression de l'administrateur
        buttonSuppAdmin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int ligne = tableauUser.getSelectedRow();				
				String id = (String) donneesAdmin[ligne][0];
				try {
					cnx.supprimerAdmin(id);
					listeAdministrateurs();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        	
        });
        
        // Evénement : ajout d'un administrateur
        buttonAjoutAdmin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ajoutAdmin();
			}
			
        });
        
        // Evénement : retour au menu
        buttonMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				menuAdmin();				
			}
			
        });
		
		// Ajout des objets au panel
		frame.getContentPane().add(titre);
        frame.getContentPane().add(new JScrollPane(tableauUser));
        frame.getContentPane().add(buttonAjoutAdmin);
        frame.getContentPane().add(buttonModifAdmin);
        frame.getContentPane().add(buttonSuppAdmin);
        frame.getContentPane().add(buttonMenu);
		
		frame.validate();
		frame.repaint();
	}
	
	
	/**
	 * Panel d'ajout d'une ligue
	 * @throws SQLException 
	 */
	public void ajoutLigue() throws SQLException {
		// Efface le contenu du panel précédent
		frame.getContentPane().removeAll();
		frame.setLayout(null);
		
		// Création des objets du panel
		JLabel titre = new JLabel("Ajouter une ligue");
		titre.setBounds(170,100,200,15);
		JLabel nom = new JLabel("Nom de la ligue :");
		nom.setBounds(70,150,200,15);
		JTextField textNom  = new JTextField(10);
		textNom.setBounds(200,150,200,20);
		JLabel admin = new JLabel("Nom de l'administrateur :");
		admin.setBounds(25,200,200,15);
		JButton buttonAjout = new JButton("Ajouter");
		buttonAjout.setBounds(300,250,100,20);
		JButton buttonMenu = new JButton("Retour au menu");
		buttonMenu.setBounds(10,430,150,20);
		
		// Création de la liste déroulante
		int taille = (cnx.admin().size()) / 3;
		Object[] elements = new Object[taille];
		ArrayList listeAdmin = new ArrayList(cnx.admin());
		int n = 0;
		for(int i = 0 ; i < taille ; i++) {
			elements[i] = listeAdmin.get(n + 1) + " " + listeAdmin.get(n);
			n += 3;
		}
		JComboBox ListeAdmin = new JComboBox(elements);
		ListeAdmin.setBounds(200,200,200,20);
		
		// Evénement : ajout d'une ligue
		buttonAjout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					String selection = ListeAdmin.getSelectedItem().toString();
					cnx.ajoutLigue(textNom.getText(), selection);
					listeLigue();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
        // Evénement : retour au menu
        buttonMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				menuAdmin();				
			}
			
        });
		
		// Ajout des objets au panel
		frame.getContentPane().add(titre);
		frame.getContentPane().add(nom);
		frame.getContentPane().add(textNom);
		frame.getContentPane().add(admin);
		frame.getContentPane().add(ListeAdmin);
		frame.getContentPane().add(buttonAjout);
		frame.getContentPane().add(buttonMenu);
	
		
		frame.validate();
		frame.repaint();
	}
	
	
	/**
	 * Panel d'ajout d'un administrateur de ligue
	 */
	public void ajoutAdmin() {
		// Efface le contenu du panel précédent
		frame.getContentPane().removeAll();
		frame.setLayout(null);
		
		// Création des objets du panel
		JLabel titre = new JLabel("Ajouter un administrateur");
		titre.setBounds(170,100,200,15);
		JLabel nom = new JLabel("Nom de l'administrateur :");
		nom.setBounds(40,150,200,15);
		JTextField textNom  = new JTextField(10);
		textNom.setBounds(220,150,200,20);
		JLabel prénom = new JLabel("Prénom de l'administrateur :");
		prénom.setBounds(20,200,200,15);
		JTextField textPrénom  = new JTextField(10);
		textPrénom.setBounds(220,200,200,20);
		JLabel email = new JLabel("Email :");
		email.setBounds(145,250,200,15);
		JTextField textEmail  = new JTextField(10);
		textEmail.setBounds(220,250,200,20);
		JLabel password = new JLabel("Mot de passe :");
		password.setBounds(100,300,200,15);
		JTextField textPassword  = new JTextField(10);
		textPassword.setBounds(220,300,200,20);
		JButton buttonAjout = new JButton("Ajouter");
		buttonAjout.setBounds(320,350,100,20);
		JButton buttonMenu = new JButton("Retour au menu");
		buttonMenu.setBounds(10,430,150,20);
		
		// Evénement : ajout d'un administrateur
		buttonAjout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					cnx.ajoutAdmin(textNom.getText(), textPrénom.getText(), textEmail.getText(), textPassword.getText());
					listeAdministrateurs();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
        // Evénement : retour au menu
        buttonMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				menuAdmin();				
			}
			
        });
		
		// Ajout des objets au panel
		frame.getContentPane().add(titre);
		frame.getContentPane().add(nom);
		frame.getContentPane().add(textNom);
		frame.getContentPane().add(prénom);
		frame.getContentPane().add(textPrénom);
		frame.getContentPane().add(email);
		frame.getContentPane().add(textEmail);
		frame.getContentPane().add(password);
		frame.getContentPane().add(textPassword);
		frame.getContentPane().add(buttonAjout);
		frame.getContentPane().add(buttonMenu);

		
		frame.validate();
		frame.repaint();
	}
	
	/**
	 * Panel de modification d'un administrateur de ligue
	 */
	public void modifAdmin(String nomAdmin, String prenomAdmin, String id) {
		// Efface le contenu du panel précédent
		frame.getContentPane().removeAll();
		frame.setLayout(null);
		
		// Création des objets du panel
		JLabel titre = new JLabel("Modifier un administrateur");
		titre.setBounds(170,100,200,15);
		JLabel nom = new JLabel("Nom de l'administrateur :");
		nom.setBounds(40,150,200,15);
		JTextField textNom  = new JTextField(nomAdmin);
		textNom.setBounds(220,150,200,20);
		JLabel prénom = new JLabel("Prénom de l'administrateur :");
		prénom.setBounds(20,200,200,15);
		JTextField textPrénom  = new JTextField(prenomAdmin);
		textPrénom.setBounds(220,200,200,20);
		JButton buttonModif = new JButton("Modifier");
		buttonModif.setBounds(320,250,100,20);
		JButton buttonListe = new JButton("Retour à liste");
		buttonListe.setBounds(10,430,150,20);
		
		// Evénement : modification d'un administrateur
		buttonModif.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					cnx.modificationAdmin(textNom.getText(), textPrénom.getText(), id);
					listeAdministrateurs();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
        // Evénement : retour à la liste
		buttonListe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					listeAdministrateurs();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
			
        });
		
		// Ajout des objets au panel
		frame.getContentPane().add(titre);
		frame.getContentPane().add(nom);
		frame.getContentPane().add(textNom);
		frame.getContentPane().add(prénom);
		frame.getContentPane().add(textPrénom);
		frame.getContentPane().add(buttonModif);
		frame.getContentPane().add(buttonListe);
		
		frame.validate();
		frame.repaint();
	}
	
	/**
	 * Panel de modification d'une ligue
	 * @throws SQLException 
	 */
	public void modifLigue(String nomLigue, String admin, String id) throws SQLException  {
		// Efface le contenu du panel précédent
		frame.getContentPane().removeAll();
		frame.setLayout(null);
		
		// Création des objets du panel
		JLabel titre = new JLabel("Modifier une ligue");
		titre.setBounds(170,100,200,15);
		JLabel nom = new JLabel("Nom de la ligue :");
		nom.setBounds(70,150,200,15);
		JTextField textNom  = new JTextField(nomLigue);
		textNom.setBounds(200,150,200,20);
		JLabel adminLabel = new JLabel("Nom de l'administrateur :");
		adminLabel.setBounds(25,200,200,15);
		JLabel adminOld = new JLabel(admin);
		adminOld.setBounds(25,220,200,15);
		JButton buttonModif = new JButton("Modifier");
		buttonModif.setBounds(300,250,100,20);
		JButton buttonListe = new JButton("Retour à la liste");
		buttonListe.setBounds(10,430,150,20);
		
		// Création de la liste déroulante
		int taille = (cnx.admin().size()) / 3;
		Object[] elements = new Object[taille];
		ArrayList listeAdmin = new ArrayList(cnx.admin());
		int n = 0;
		elements[0] = " ";
		for(int i = 1 ; i < taille ; i++) {
			elements[i] = listeAdmin.get(n + 1) + " " + listeAdmin.get(n);
			n += 3;
		}
		JComboBox ListeAdmin = new JComboBox(elements);
		ListeAdmin.setBounds(200,200,200,20);
		
		// Evénement : modification d'une ligue
		buttonModif.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					String selection = ListeAdmin.getSelectedItem().toString();
					cnx.modificationLigue(textNom.getText(), admin, selection, id);
					listeLigue();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
        // Evénement : retour à la liste
		buttonListe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					listeLigue();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
			
        });
		
		// Ajout des objets au panel
		frame.getContentPane().add(titre);
		frame.getContentPane().add(nom);
		frame.getContentPane().add(textNom);
		frame.getContentPane().add(adminLabel);
		frame.getContentPane().add(adminOld);
		frame.getContentPane().add(ListeAdmin);
		frame.getContentPane().add(buttonModif);
		frame.getContentPane().add(buttonListe);
		
		frame.validate();
		frame.repaint();
	}

	
	
	/**
	 * Fonction de connexion administrateur
	 * @param password
	 * @return true si le mot de passe est correct
	 */
	private boolean formConnect(String password) throws SQLException {
		String pass = cnx.password();
		String hash = hash(password);
		return (hash.equals(pass));
	}
	

	/**
	 * Hachage de mot de passe
	 * @param password
	 * @return password haché
	 */
	private String hash(String password) {
		byte[] uniqueKey = password.getBytes();
		byte[] hash = null;
		try {
			hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		StringBuilder hashString = new StringBuilder();
		for (int i = 0; i < hash.length; i++)
		{
			String hex = Integer.toHexString(hash[i]);
		    if (hex.length() == 1)
		    {
		     	hashString.append('0');
		        hashString.append(hex.charAt(hex.length() - 1));
		    }
		    else
		        hashString.append(hex.substring(hex.length() - 2));
		}
		return hashString.toString();
	}
	
	
	public static void main(String[] args) {
		new PersonnelGraphique();
	}

}