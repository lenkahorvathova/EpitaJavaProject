/**
 * 
 */
package fr.epita.iam.services;

/**
 * This is a main authenticator service.
 * 
 * @author Lenka Horvathova
 */
public class Authenticator {

	/**
	 * This method is checking authentication.
	 * 
	 * @param username		a username to check
	 * @param password		a password for a given username to check
	 * @return				true, if the username is registered and the correct password is input;
	 * 						false, otherwise
	 */
	public static boolean authenticate(String username, String password) {
		
		// for now only an administrator with username 'adm' and password 'pwd' can access
		return "adm".equals(username) && "pwd".equals(password);
	}
}
