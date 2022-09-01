package utilitaires.ligneDeCommande;

import java.util.List;

/**
 * Permet de g�rer les listes. C'est-�-dire : affecter une action au choix d'un �l�ment 
 * dans une liste, et actualiser les �l�ments de la liste devant �tre affich�e.
 */

public interface ActionListe<T>
{
	/**
	 * Fonction permettant de rafra�chir la liste juste avant de l'afficher.
	 * @returns la liste des �l�ments parmi lesquels la s�lection devra 
	 * se faire.
	 */
	
	public List<T> getListe();

	/**
	 * Fonction ex�cut�e lorsqu'un �l�ment est choisi dans une liste.
	 * @param indice indice de l'�l�ment s�lectionn�.
	 * @param element �l�ment s�lectionn�.
	 */
	
	public void elementSelectionne(int indice, T element);
}
