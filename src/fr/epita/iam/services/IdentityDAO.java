package fr.epita.iam.services;

import java.util.List;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.DAOClosureException;
import fr.epita.iam.exceptions.DAODeleteException;
import fr.epita.iam.exceptions.DAOInitializationException;
import fr.epita.iam.exceptions.DAOSaveException;
import fr.epita.iam.exceptions.DAOSearchException;
import fr.epita.iam.exceptions.DAOUpdateException;

/**
 * This is an Interface for managing identities.
 * DAO: Data Access Object, here to manage object.
 * 
 * @author Lenka Horvathova
 */
public interface IdentityDAO {
	
	/**
	 * This is a save method for adding a new identity.
	 * 
	 * @param identity						the identity to record
	 * @throws DAOSaveException				the customized exception that can thrown during saving
	 */
	public void save(Identity identity) throws DAOSaveException;
	
	/**
	 * This is a search method that returns a list with all the identities that met criteria.
	 * 
	 * @param criteria				 		the criteria of the identity to be met
	 * @throws DAOSearchException			the customized exception that can thrown during searching		
	 * @return								the list of identities corresponding to the criteria
	 */
	public List<Identity> search(Identity criteria) throws DAOSearchException;
	
	/**
	 * This is an update method for updating an identity, if it already exists.
	 * 
	 * @param identity						the identity with unique UID and rest of info updated
	 * @throws DAOUpdateException			the customized exception that can thrown during updating
	 */
	public void update(Identity identity) throws DAOUpdateException;
	
	/**
	 * This method deletes the given identity.
	 * 
	 * @param identity						the identity to be deleted
	 * @throws DAODeleteException			the customized exception that can thrown during deleting
	 */
	public void delete(Identity identity) throws DAODeleteException;
	
	/**
	 * This method releases all the resources.
	 * 
	 * @throws DAOClosureException			the customized exception that can thrown during closing
	 */
	public void closeResources() throws DAOClosureException;
	
	/**
	 * This method resets a DAO source (a file or a table in database) to the original example version.
	 * 
	 * @throws DAOInitializationException	the customized exception that can thrown during initializing
	 */
	public void resetToOriginal() throws DAOInitializationException;
	
	/**
	 * This method prints a current content of a DAO source (a file or a table in database).
	 * 
	 * @throws DAOSearchException			the customized exception that can thrown during searching
	 */
	public void printContent() throws DAOSearchException;
}
