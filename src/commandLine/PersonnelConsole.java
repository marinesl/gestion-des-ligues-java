package commandLine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import personnel.*;
import utilitaires.ligneDeCommande.*;
import static utilitaires.EntreesSorties.*;


public class PersonnelConsole
{
	private GestionPersonnel gestionPersonnel;
	
	public PersonnelConsole(GestionPersonnel gestionPersonnel)
	{
		this.gestionPersonnel = gestionPersonnel;
	}
	
	public void start()
	{
		menuPrincipal().start();
	}
	
	private Menu menuPrincipal()
	{
		Menu menu = new Menu("Gestion du personnel des ligues");
		menu.ajoute(editerEmploye(gestionPersonnel.getRoot()));
		menu.ajoute(menuLigues());
		menu.ajoute(menuQuitter());
		return menu;
	}

	private Menu menuQuitter()
	{
		Menu menu = new Menu("Quitter", "q");
		menu.ajoute(quitterEtEnregistrer());
		menu.ajoute(quitterSansEnregistrer());
		menu.ajouteRevenir("r");
		return menu;
	}
	
	private Menu menuLigues()
	{
		Menu menu = new Menu("Gérer les ligues", "l");
		menu.ajoute(afficherLigues());
		menu.ajoute(ajouterLigue());
		menu.ajoute(selectionnerLigue());
		menu.ajoute(supprimerLigue());
		menu.ajouteRevenir("q");
		return menu;
	}

	private Option afficherLigues()
	{
		Option option = new Option("Afficher les ligues", "l");
		option.setAction(new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				System.out.println(gestionPersonnel.getLigues());
			}
		});
		return option;
	}
	
	private Option afficherEmployes(final Ligue ligue)
	{
		Option option = new Option("Afficher les employes", "l");
		option.setAction(new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				System.out.println(ligue.getEmployes());
			}
		});
		return option;
	}
	
	private Option afficher(final Ligue ligue)
	{
		Option option = new Option("Afficher la ligue", "l");
		option.setAction(new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				System.out.println(ligue);
				System.out.println("administrée par " + ligue.getAdministrateur());
			}
		});
		return option;
	}

	private Option afficher(final Employe employe)
	{
		Option option = new Option("Afficher l'employé", "l");
		option.setAction(new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				System.out.println(employe);
			}
		});
		return option;
	}

	private Option ajouterLigue()
	{
		Option option = new Option("Ajouter une ligue", "a");
		option.setAction(new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				new Ligue (getString("nom : "));						
			}
		});
		return option;
	}
	
	private Option ajouterEmploye(final Ligue ligue)
	{
		Option option = new Option("Ajouter un employé", "a");
		option.setAction(new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				ligue.addEmploye(getString("nom : "), 
						getString("prenom : "), getString("mail : "), 
						getString("password : "));
			}
		});
		return option;
	}
	
	private Menu editerLigue(Ligue ligue)
	{
		Menu menu = new Menu("Editer " + ligue.getNom());
		menu.ajoute(afficher(ligue));
		menu.ajoute(gererEmployes(ligue));
		menu.ajoute(changerAdministrateur(ligue));
		menu.ajoute(changerNom(ligue));
		menu.ajouteRevenir("q");
		return menu;
	}

	private Menu editerEmploye(Employe employe)
	{
		Menu menu = new Menu("Gérer le compte " + employe.getNom(), "c");
		menu.ajoute(afficher(employe));
		menu.ajoute(changerNom(employe));
		menu.ajoute(changerPrenom(employe));
		menu.ajoute(changerMail(employe));
		menu.ajoute(changerPassword(employe));
		menu.ajouteRevenir("q");
		return menu;
	}

	private Menu gererEmployes(Ligue ligue)
	{
		Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
		menu.ajoute(afficherEmployes(ligue));
		menu.ajoute(ajouterEmploye(ligue));
		menu.ajoute(modifierEmploye(ligue));
		menu.ajoute(supprimerEmploye(ligue));
		menu.ajouteRevenir("q");
		return menu;
	}

	private Liste<Employe> modifierEmploye(final Ligue ligue)
	{
		return new Liste<>("Modifier un employé", "e", 
				new ActionListe<Employe>()
		{
			@Override
			public List<Employe> getListe()
			{
				return new ArrayList<>(ligue.getEmployes());
			}
			@Override
			public void elementSelectionne(int indice, Employe element)
			{
				editerEmploye(element);
			}
		});
	}
	
	private Liste<Employe> supprimerEmploye(final Ligue ligue)
	{
		return new Liste<>("Supprimer un employé", "s", 
				new ActionListe<Employe>()
		{
			@Override
			public List<Employe> getListe()
			{
				return new ArrayList<>(ligue.getEmployes());
			}
			@Override
			public void elementSelectionne(int indice, Employe element)
			{
				element.remove();
			}
		});
	}
	
	private Liste<Employe> changerAdministrateur(final Ligue ligue)
	{
		return new Liste<Employe>("Changer d'administrateur", "c", 
				new ActionListe<Employe>()
				{
					@Override
					public List<Employe> getListe()
					{
						return new ArrayList<>(ligue.getEmployes());
					}
					@Override
					public void elementSelectionne(int indice, Employe element)
					{
						ligue.setAdministrateur(element);
					}
				});
	}		
	
	private Option changerNom(final Ligue ligue)
	{
		Option option = new Option("Renommer", "r");
		option.setAction(new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				ligue.setNom(getString("Nouveau nom : "));
			}
		});
		return option;
	}

	private Liste<Ligue> selectionnerLigue()
	{
		Liste<Ligue> liste = new Liste<Ligue>("Sélectionner une ligue", "e", 
				new ActionListe<Ligue>()
				{
					@Override
					public List<Ligue> getListe()
					{
						return new ArrayList<>(gestionPersonnel.getLigues());
					}
					@Override
					public void elementSelectionne(int indice, Ligue element)
					{
						editerLigue(element).start();
					}
				});
		return liste;
	}
	
	private Liste<Ligue> supprimerLigue()
	{
		Liste<Ligue> liste = new Liste<Ligue>("Supprimer une ligue", "d", 
				new ActionListe<Ligue>()
				{
					@Override
					public List<Ligue> getListe()
					{
						return new ArrayList<>(gestionPersonnel.getLigues());
					}
					@Override
					public void elementSelectionne(int indice, Ligue element)
					{
						element.remove();
					}
				});
		return liste;
	}
	
	private Option changerNom(final Employe employe)
	{
		Option option = new Option("Changer le nom", "n");
		option.setAction(new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				employe.setNom(getString("Nouveau nom : "));				
			}
		});
		return option;
	}
	
	private Option changerPrenom(final Employe employe)
	{
		Option option = new Option("Changer le prénom", "p");
		option.setAction(new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				employe.setPrenom(getString("Nouveau prénom : "));				
			}
		});
		return option;
	}
	
	private Option changerMail(final Employe employe)
	{
		Option option = new Option("Changer le mail", "e");
		option.setAction(new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				employe.setMail(getString("Nouveau mail : "));				
			}
		});
		return option;
	}
	
	private Option changerPassword(final Employe employe)
	{
		Option option = new Option("Changer le password", "x");
		option.setAction(new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				employe.setPassword(getString("Nouveau password : "));				
			}
		});
		return option;
	}
	
	private Option quitterEtEnregistrer()
	{
		return new Option("Quitter et enregistrer", "q", new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				try
				{
					gestionPersonnel.sauvegarder();
					Action.QUITTER.optionSelectionnee();
				} 
				catch (SauvegardeImpossible e)
				{
					System.out.println("Impossible d'effectuer la sauvegarde");
				}
			}
		});
	}
	
	private Option quitterSansEnregistrer()
	{
		return new Option("Quitter sans enregistrer", "a", Action.QUITTER);
	}
	
	private boolean verifiePassword()
	{
		boolean ok = gestionPersonnel.getRoot().checkPassword(getString("password : "));
		if (!ok)
			System.out.println("Password incorrect.");
		return ok;
	}
	
	public static void main(String[] args)
	{
		
		// La connexion à la base s'effectue en 2 étapes: le driver et la cnnexion
		// J'ai différencié les 2 étapes dans 2 Try-Catch différents afin de localiser plus facilement les erreurs s'il y en a    
			
			// Chargement du Driver
				       try {
				           Class.forName("com.mysql.jdbc.Driver").newInstance();
				           System.out.println("Le chargement du driver fonctionne");
				       } catch (Exception ex) {
				    	   System.out.println("Erreur du chargement de Driver");
				       }
				       
			//Connexion à la Base de Données	       
				       try {
				    	   
				    	   String bdd = "m2l_personnel"; //nom base de données
				    	   String url = "jdbc:mysql://localhost/"+bdd; //URL de connexion à la base de données
				    	   String user = "root"; // identifiant MySQL
				    	   String passwd = ""; //mot de passe MySQL

				    	   Connection conn = DriverManager.getConnection(url, user, passwd);
				    	   System.out.println("Connexion effective !");  
				    	      			    	   			    	         
				    	    } catch (Exception e) {
				    	    	System.out.println("Erreur de connexion à la BDD");
				    	    }
		
		PersonnelConsole personnelConsole = 
				new PersonnelConsole(GestionPersonnel.getGestionPersonnel());
		if (personnelConsole.verifiePassword())
			personnelConsole.start();
		
	}
}
