package fr.epita.iam.tests.datamodel;

import fr.epita.iam.datamodel.Identity;

/**
 * This is a class for testing the correct implementation of Identity class.
 * 
 * @author Lenka Horvathova
 */
public class TestIdentity {

	/**
	 * This main method creates a new identity and then prints it out for a check.
	 * 
	 * @param args			arguments from command line, unused
	 */
	public static void main(String[] args) {
		
		Identity identity = new Identity("001", "Lenka Horvathova", "lenka.m.horvathova@gmail.com");
		
		System.out.println("Created " + identity);
	}

}
