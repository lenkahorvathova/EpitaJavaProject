package fr.epita.iam.exceptions;

/**
 * This class represents an error which occurs during a process of an initialization.
 * 
 * @author Lenka Horvathova
 */
public class DAOInitializationException extends Exception {
	
	private Object faultObject;
	
	/**
	 * FaultObject Setter : This method sets a fault object.
	 * @param obj			the fault object to be set
	 */
	public void setFaultObject(Object obj) {
		this.faultObject = obj;
	}

	/**
	 * This is an overridden method that returns a message for the exception.
	 */
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return (super.getMessage() + String.valueOf(this.faultObject));
	}
}
