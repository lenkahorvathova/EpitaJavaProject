/**
 * 
 */
package fr.epita.iam.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.DAOClosureException;
import fr.epita.iam.exceptions.DAODeleteException;
import fr.epita.iam.exceptions.DAOInitializationException;
import fr.epita.iam.exceptions.DAOSaveException;
import fr.epita.iam.exceptions.DAOSearchException;
import fr.epita.iam.exceptions.DAOUpdateException;

/**
 * This class manages identities using a database.
 * 
 * @author Lenka Horvathova
 */
public class JDBCIdentityDAO implements IdentityDAO {

	private static final String DETAILS = "Details: ";
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet results;	
	
	/**
	 * This is a Constructor for a JDBCIdentityDAO class.
	 * A connection with a database is set with every initialization. 
	 * @throws DAOInitializationException		the customized exception that can thrown during initializing
	 */
	public JDBCIdentityDAO() throws DAOInitializationException {
		try {
			this.connection = DriverManager.getConnection("jdbc:derby://localhost:1527/iam;create=true","IAM","123");
		} catch(SQLException e) {
			System.out.println("Error during an initialization of data!");
			System.out.println(DETAILS + e.getMessage());
			throw new DAOInitializationException();
		}
	}

	/* (non-Javadoc)
	 * @see fr.epita.iam.services.IdentityDAO#save()
	 */
	@Override
	public void save(Identity identity) throws DAOSaveException {
		
		String statement = "INSERT into IDENTITIES "
						 + "(IDENTITY_DISPLAYNAME, IDENTITY_EMAIL) "
						 + "values(?, ?)";
		try {
			preparedStatement = this.connection.prepareStatement(statement);		
			preparedStatement.setString(1, identity.getDisplayName());
			preparedStatement.setString(2, identity.getEmail());		
			preparedStatement.execute();
		} catch (SQLException e) {
			System.out.println("Error during a save of data!");
			System.out.println(DETAILS + e.getMessage());
			throw new DAOSaveException();			
		}
	}

	/* (non-Javadoc)
	 * @see fr.epita.iam.services.IdentityDAO#search()
	 */
	@Override
	public List<Identity> search(Identity criteria) throws DAOSearchException {
		
		List<Identity> returnedList = new ArrayList<>();
		try {
			preparedStatement = this.connection.prepareStatement("SELECT * from IDENTITIES");
			results = preparedStatement.executeQuery();
	
			while (results.next()){
				String uid = results.getString("IDENTITY_UID");
				String displayName = results.getString("IDENTITY_DISPLAYNAME");
				String email = results.getString("IDENTITY_EMAIL");			
				returnedList.add(new Identity(uid, displayName, email));
			}
		} catch (SQLException e) {
			System.out.println("Error during a search of data!");
			System.out.println(DETAILS + e.getMessage());
			throw new DAOSearchException();
		}
		
		return returnedList;
	}

	/* (non-Javadoc)
	 * @see fr.epita.iam.services.IdentityDAO#update()
	 */
	@Override
	public void update(Identity identity) throws DAOUpdateException {
		
		String statement  = "UPDATE IDENTITIES "
						  + "SET IDENTITY_DISPLAYNAME = ?,"
						  + "IDENTITY_EMAIL = ?"
						  + "where IDENTITY_UID = ?";		
		try {
			preparedStatement = connection.prepareStatement(statement);		
			preparedStatement.setString(1, identity.getDisplayName());
			preparedStatement.setString(2, identity.getEmail());
			preparedStatement.setString(3, identity.getUid());		
			preparedStatement.execute();
		} catch (SQLException e) {
			System.out.println("Error during an update of a file!");
			System.out.println(DETAILS + e.getMessage());
			throw new DAOUpdateException();
		}
	}

	/* (non-Javadoc)
	 * @see fr.epita.iam.services.IdentityDAO#delete()
	 */
	@Override
	public void delete(Identity identity) throws DAODeleteException {
		
		String statement = "DELETE from IDENTITIES where IDENTITY_UID = ?";
		try {
			preparedStatement = connection.prepareStatement(statement);	
			preparedStatement.setString(1, identity.getUid());		
			preparedStatement.execute();
		} catch (SQLException e) {
			System.out.println("Error during an deletion of a file!");
			System.out.println(DETAILS + e.getMessage());
			throw new DAODeleteException();
		}		
	}

	/* (non-Javadoc)
	 * @see fr.epita.iam.services.IdentityDAO#closeResources()
	 */
	@Override
	public void closeResources() throws DAOClosureException {
		try {
			this.preparedStatement.close();
			this.connection.close();
		} catch (SQLException e) {
			System.out.println("Error during a closure of resources!");
			System.out.println(DETAILS + e.getMessage());
			throw new DAOClosureException();
		}
	}

	/* (non-Javadoc)
	 * @see fr.epita.iam.services.IdentityDAO#resetToOriginal()
	 */
	@Override
	public void resetToOriginal() throws DAOInitializationException {
		
		try {
			String statement  = "DELETE from IDENTITIES";	
			preparedStatement = connection.prepareStatement(statement);
			preparedStatement.execute();
				
			statement  = "ALTER TABLE IDENTITIES ALTER COLUMN IDENTITY_UID RESTART WITH 1";
			preparedStatement = connection.prepareStatement(statement);
			preparedStatement.execute();
				
			statement = "INSERT into IDENTITIES "
					  + "(IDENTITY_DISPLAYNAME, IDENTITY_EMAIL) "
					  + "values(?, ?)";
			preparedStatement = connection.prepareStatement(statement);
				
			preparedStatement.setString(1, "Lenka Horvathova");
			preparedStatement.setString(2, "l.h@gmail.com");
			preparedStatement.execute();
				
			preparedStatement.setString(1, "Janko Hrasko");
			preparedStatement.setString(2, "jh@gmail.com");
			preparedStatement.execute();
				
			preparedStatement.setString(1, "John Smith");
			preparedStatement.setString(2, "js@yahoo.com");
			preparedStatement.execute();
				
			preparedStatement.setString(1, "Anne Black");
			preparedStatement.setString(2, "anne.b@yahoo.com");	
			preparedStatement.execute();
		} catch (SQLException e) {
			System.out.println("Error during a reset of table in the database!");
			System.out.println(DETAILS + e.getMessage());
			throw new DAOInitializationException();
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.epita.iam.services.IdentityDAO#printContent()
	 */
	@Override
	public void printContent() throws DAOSearchException {
		
		String connectionString = "jdbc:derby://localhost:1527/iam;create=true";
		String user = "IAM";
		String password = "123";
		
		try {
			Connection connection2 = DriverManager.getConnection(connectionString, user, password);		
			PreparedStatement preparedStatement2 = connection2.prepareStatement("SELECT * from IAM.IDENTITIES");		
			ResultSet results2 = preparedStatement2.executeQuery();
			
			while (results2.next()) {			
				String uid = results2.getString("IDENTITY_UID");
				String displayName = results2.getString("IDENTITY_DISPLAYNAME");
				String email = results2.getString("IDENTITY_EMAIL");			
				System.out.println(uid + " - " + displayName + " - " + email);
			}		
			preparedStatement2.close();
			results2.close();
			connection2.close();
		} catch (SQLException e) {
			System.out.println("Error during a print of a content!");
			System.out.println(DETAILS + e.getMessage());
			throw new DAOSearchException();
		}
	}
}
