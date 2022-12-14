package personnel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Gestion du personnel. Un seul objet de cette classe existe.
 * Il n'est pas possible d'instancier directement cette classe, 
 * la m?thode {@link #getGestionPersonnel getGestionPersonnel} 
 * le fait automatiquement et retourne toujours le m?me objet.
 * Dans le cas o? {@link #sauvegarder()} a ?t? appel? lors 
 * d'une ex?cution pr?c?dente, c'est l'objet sauvegard? qui est
 * retourn?.
 */

public class GestionPersonnel implements Serializable
{
	private static final String FILE_NAME = "GestionPersonnel.srz";
	private static final long serialVersionUID = -105283113987886425L;
	private static GestionPersonnel gestionPersonnel;
	private SortedSet<Ligue> ligues;
	private Employe root = new Employe(null, "root", "", "", "toor");
	
	/**
	 * Retourne l'unique instance de cette classe.
	 * Cr?e cet objet s'il n'existe d?j?.
	 * @return l'unique objet de type {@link GestionPersonnel}.
	 */
	
	public static GestionPersonnel getGestionPersonnel()
	{
		
		if (gestionPersonnel == null)
		{
			gestionPersonnel = readObject();
			if (gestionPersonnel == null)
				gestionPersonnel = new GestionPersonnel();
		}
		return gestionPersonnel;
	}
	
	private static GestionPersonnel readObject()
	{
		ObjectInputStream ois = null;
		try
		{
			FileInputStream fis = new FileInputStream(FILE_NAME);
			ois = new ObjectInputStream(fis);
			return (GestionPersonnel)(ois.readObject());
		}
		catch (IOException | ClassNotFoundException e)
		{
			return null;
		}
		finally
		{
				try
				{
					if (ois != null)
						ois.close();
				} 
				catch (IOException e){}
		}	
	}
	
	/**
	 * Sauvegarde le gestionnaire pour qu'il soit ouvert automatiquement 
	 * lors d'une ex?cution ult?rieure du programme.
	 * @throws SauvegardeImpossible Si le support de sauvegarde est inaccessible.
	 */
	
	public void sauvegarder() throws SauvegardeImpossible
	{
		ObjectOutputStream oos = null;
		try
		{
			FileOutputStream fis = new FileOutputStream(FILE_NAME);
			oos = new ObjectOutputStream(fis);
			oos.writeObject(this);
		}
		catch (IOException e)
		{
			throw new SauvegardeImpossible();
		}
		finally
		{
			try
			{
				if (oos != null)
					oos.close();
			} 
			catch (IOException e){}
		}
	}
	
	private GestionPersonnel()
	{
		ligues = new TreeSet<>();
	}
	
	/**
	 * Retourne la ligue dont administrateur est l'administrateur,
	 * null s'il n'est pas un administrateur.
	 * @param administrateur l'administrateur de la ligue recherch?e.
	 * @return la ligue dont administrateur est l'administrateur.
	 */
	
	public Ligue getLigue(Employe administrateur)
	{
		if (administrateur.estAdmin(administrateur.getLigue()))
			return administrateur.getLigue();
		else
			return null;
	}

	/**
	 * Retourne toutes les ligues enregistr?es.
	 * @return toutes les ligues enregistr?es.
	 */
	
	public SortedSet<Ligue> getLigues()
	{
		return Collections.unmodifiableSortedSet(ligues);
	}

	void add(Ligue ligue)
	{
		ligues.add(ligue);
	}
	

	void remove(Ligue ligue)
	{
		ligues.remove(ligue);
	}
	

	/**
	 * Retourne le root (super-utilisateur).
	 * @return le root.
	 */
	
	public Employe getRoot()
	{
		return root;
	}
}
