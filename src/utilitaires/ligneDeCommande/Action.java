package utilitaires.ligneDeCommande;

/**
 * Permet d'affecter des actions au choix d'un �l�ment dans un menu.
 */

public interface Action
{
	/**
	 * Action pr�d�finie permettant de quitter le programme.
	 */
	
	public static final Action 
	QUITTER = new Action()
	{
		@Override 
		public void optionSelectionnee()
		{
			System.exit(0);
		}
	},
	
	/**
	 * Action pr�d�finie permettant de revenir au menu pr�c�dent.
	 */
	
	REVENIR = new Action()
	{
		@Override 
		public void optionSelectionnee(){}
	};


	/**
	 * Fonction automatiquement ex�cut�e quand une option est s�lectionn�e.
	 */
	
	public void optionSelectionnee();
}
