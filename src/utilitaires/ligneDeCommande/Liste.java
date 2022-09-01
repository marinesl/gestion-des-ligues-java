package utilitaires.ligneDeCommande;

import java.util.List;

/**
 * Liste de valeurs (de type T) dans laquelle l'utilisateur
 * doit faire une s�lection. Les valeurs de trouvant dans les champs
 * {@link liste} sont affich�es et l'utilisateur est invit� � saisir
 * l'indice de l'�l�ment qu'il souhaite.
 */

public class Liste<T> extends Menu
{
	private ActionListe<T> action;
	
	/**
	 * Cr��e une liste.
	 * @param titre intitul� affich� au-dessus de la liste.
	 * @param action l'objet permettant de g�rer la liste.
	 */
	
	public Liste(String titre, ActionListe<T> action)
	{
		super(titre);
		this.action = action;
		setRetourAuto(true);
	}
	
	private Action getAction(final int indice, final T element)
	{
		return new Action()
		{
			@Override
			public void optionSelectionnee()
			{
				elementSelectionne(indice, element);
			}
		};
	}

	/**
	 * Cr��e une liste.
	 * @param titre intitul� affich� au dessus-de la liste.
	 * @param action l'objet permettant de g�rer la liste.
	 * @param raccourci raccourci utilis� dans le cas o� cette liste est utilis� comme option dans un menu.
	 */
	
	public Liste(String titre, String raccourci, ActionListe<T> action)
	{
		this(titre, action);
		this.raccourci = raccourci;
	}
	
	/**
	 * D�termine la fonction � appeler quand un �l�ment est s�lectionn�.
	 * @param action L'objet dont la fonction elementSelectionne() va �tre appel�.
	 */
	
	public void setAction(ActionListe<T> action)
	{
		this.action = action;
	}
	
	private void elementSelectionne(int indice, T element)
	{
		if (action != null)
			action.elementSelectionne(indice, element);
	}
	
	private void actualise()
	{
		List<T> liste = action.getListe();
		clearOptions();
		for (int i = 0 ; i < liste.size() ; i++)
		{
			T element = liste.get(i);
			ajoute(new Option(element.toString(), "" + (i + 1), getAction(i, element))) ;
		}				
	}
	
	/**
	 * Lance l'ex�cution de la liste.
	 */
	
	@Override
	public void start()
	{
		actualise();
		super.start();
	}
}
