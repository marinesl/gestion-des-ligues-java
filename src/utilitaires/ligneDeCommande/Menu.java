package utilitaires.ligneDeCommande;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utilitaires.EntreesSorties;

/**
 * Menu affich� en ligne de commande. En haut du menu est affich� le {@link titre}, 
 * suivi par une liste d'options. L'utilisateur est invit� � choisir une option
 * qui est ensuite ex�cut�e. Il est possible de placer un sous-menu en option, 
 * ou il est possible d'utiliser la classe Option pour affecter une action � la s�lection 
 * d'une action.
 */

public class Menu extends Option
{
	private Map<String, Option> optionsMap = new TreeMap<>();
	private List<Option> optionsList = new ArrayList<>();
	private boolean retourAuto = false;
	
	/**
	 * Cr��e un menu.
	 * @param titre titre affich� au dessus du menu.
	 */
	
	public Menu(String titre)
	{
		super(titre, "");
	}
	
	/**
	 * Cr��e un menu.
	 * @param titre titre affich� au dessus du menu.
	 * @param raccourci Si ce menu est aussi une option, 
	 * raccourci permettant de l'activer.
	 */
	
	public Menu(String titre, String raccourci)
	{
		super(titre, raccourci);
	}

	/**
	 * Ajoute une option dans le menu.
	 * @param option option � ajouter.
	 */
	
	public void ajoute(Option option)
	{
		Option autre = optionsMap.get(option.getRaccourci());
		if (autre != null)
			throw new RuntimeException("Collision entre " + autre.getTitre()
					+ " et " + option.getTitre() + " pour le raccourci" +
					option.getRaccourci() + " dans le menu " + 
					getTitre() + ".");
		optionsMap.put(option.getRaccourci(), option);
		optionsList.add(option);
	}
	
	protected void clearOptions()
	{
		optionsList.clear();
		optionsMap.clear();
	}
	
	/**
	 * Ajoute une option permettant de quitter le programme.
	 * @param raccourci le raccourci permettant de quitter le programme.
	 */
	
	public void ajouteQuitter(String raccourci)
	{
		ajoute(new Option("Quitter", raccourci, Action.QUITTER));
	}
	
	/**
	 * Ajoute une option permettant de revenir au menu pr�c�dent.
	 * @param raccourci le raccourci permettant de revenir au menu pr�c�dent.
	 */
	
	public void ajouteRevenir(String raccourci)
	{
		ajoute(new Option("Revenir", raccourci, Action.REVENIR));
	}
	
	/**
	 * D�termine si le choix d'une option entra�ne automatiquement le retour au menu pr�c�dent.
	 * D�sactiv� par d�faut.
	 * @param retourAuto vrai si le choix d'une option entra�ne le retour au 
	 * menu pr�c�dent.
	 */
	
	public void setRetourAuto(boolean retourAuto)
	{
		this.retourAuto = retourAuto;
	}
	
	protected String saisitOption()
	{
		return EntreesSorties.getString(this.toString());
	}
	
	/**
	 * Ex�cute le menu.	
	 */
	
	public void start()
	{
		Option option = null;
		do
		{
			String saisie = saisitOption();
			option = optionsMap.get(saisie);
			if (option != null)
				option.optionSelectionnee();
			else
				System.out.println("Cette option n'est pas disponible.");
		}
		while(option == null || !retourAuto && option.getAction() != Action.REVENIR);
	}

	@Override
	void optionSelectionnee()
	{
		this.start();
	}
	
	@Override
	public String toString()
	{
		String res = getTitre() + '\n';
		for (Option option : optionsList)
			res += option.stringOfOption() + "\n";
		return res;
	}
}
