package fr.epita.iam.datamodel;

/**
 * This class represents an identity.
 * 
 * @author Lenka Horvathova
 */
public class Identity {

	private String uid;
	private String displayName;
	private String email;
	
	/**
	 * This is a Constructor for an Identity class.
	 * It initializes a new identity with passed information.
	 * 
	 * @param uid				a user's unique identification
	 * @param displayName		a user's full name
	 * @param email				a user's e-mail
	 */
	public Identity(String uid, String displayName, String email) {
		
		this.uid = uid;
		this.displayName = displayName;
		this.email = email;
	}
		
	/**
	 * UID Getter : This method returns a user's unique identification number.
	 * 
	 * @return 					the uid
	 */
	public String getUid() {
		return uid;
	}
	
	/**
	 * UID Setter : This method sets a user's unique identification number.
	 * 
	 * @param uid 				the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * DisplayName Getter : This method returns a user's name.
	 * 
	 * @return					the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * DisplayName Setter : This method sets a user's name.
	 * 
	 * @param displayName 		the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Email Getter : This method returns a user's e-mail address.
	 * 
	 * @return 					the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Email Setter : This method sets a user's e-mail address.
	 * 
	 * @param email 			the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * This is an overridden method to print an Identity information in this format:
	 * 
	 *  UID - displayName - email	
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getUid() + " - " + getDisplayName() + " - " + getEmail();
	}
}
