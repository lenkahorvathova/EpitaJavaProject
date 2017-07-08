package fr.epita.iam.tests.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.DAOClosureException;
import fr.epita.iam.exceptions.DAODeleteException;
import fr.epita.iam.exceptions.DAOInitializationException;
import fr.epita.iam.exceptions.DAOSaveException;
import fr.epita.iam.exceptions.DAOUpdateException;
import fr.epita.iam.services.FileIdentityDAO;

/**
 * This class tests the correct implementation of FileIdentityDAO class.
 * 
 * @author Lenka Horvathova
 */
public class TestFileIdentityDAO {

	private static final String DETAILS = "Details: ";
	private static final String DIVIDER = "~ ~ ~ ~ ~ ~ ~ ~ ~ ~";

	/**
	 * This is a main method for testing.
	 * 
	 * @param args			arguments from command line, unused
	 */
	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		FileIdentityDAO dao = new FileIdentityDAO();
		String answer = "";
		Set<String> correctAnswers = new HashSet<>(Arrays.asList( new String[] {"Y", "y", "N", "n"}));
		
		System.out.println("Do you want to reset the testing file? (Y/N)");
		
		while (!correctAnswers.contains(answer)) {
			
			System.out.print("Answer : ");
			answer = scan.nextLine();
			
			switch (answer) {
			case "Y": case "y":			
				try {
					dao.resetToOriginal();
				} catch (DAOInitializationException e) {
					e.printStackTrace();
				}			
				break;
								
			case "N": case "n":			
				break;
								
			default: 
				System.out.println("Invalid choice! Please, choose 'Y' or 'N'!");			
				break;
			}	
		}		
			
		try {
			scan.close();	
			dao.closeResources();
		} catch (DAOClosureException e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println(DIVIDER);
			System.out.println("TEST FOR THE SAVE METHOD");
			testSave();
			System.out.println(DIVIDER + "\n");
		} catch (DAOSaveException e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println(DIVIDER);
			System.out.println("TEST FOR THE UPDATE METHOD");
			testUpdate();
			System.out.println(DIVIDER + "\n");
		} catch (DAOUpdateException e) {
			e.printStackTrace();
		}		
		
		try {
			System.out.println(DIVIDER);
			System.out.println("TEST FOR THE DELETE METHOD");
			testDelete();
			System.out.println(DIVIDER + "\n");
		} catch (DAODeleteException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * This method tests saving a identity into the file.
	 * It prints an original and a updated file for comparison.
	 * 
	 * @throws DAOSaveException 		the customized exception that can thrown during saving
	 */
	public static void testSave() throws DAOSaveException {
		
		try {
			FileIdentityDAO dao = new FileIdentityDAO();	
			Identity identity = new Identity("5", "Pierre Baptiste", "baptistp@gmail.com");		
			List<Identity> identityList = dao.search(null);		
			int initialSize = identityList.size();
			
			System.out.println("\nThis Identity should be added to the file: " + identity);
			
			System.out.println("\nContent of an original file : ");
			dao.printContent();
			
			dao.save(identity);
			
			identityList = dao.search(null);		
			int finalSize = identityList.size();
					
			if (finalSize - initialSize != 1) {
				
				System.out.println("Something went wrong while saving a new identity!");
				
			} else {
				
				System.out.println("\nContent of an updated file : ");
				dao.printContent();
			}
		
			dao.closeResources();	
		} catch (Exception e) {
			System.out.println("Error during a test of a save of data!");
			System.out.println(DETAILS + e.getMessage());
			throw new DAOSaveException();
		}
	}
	
	/**
	 * This method tests updating an identity in the file.
	 * It prints an original and a updated file for comparison.
	 * 
	 * @throws DAOUpdateException 		the customized exception that can thrown during updating
	 */
	public static void testUpdate() throws DAOUpdateException {
		
		try {
			FileIdentityDAO dao = new FileIdentityDAO();
			Identity identity = new Identity("1", "Lenka Horvathova", "lenka.m.horvathova@gmail.com");	
			
			System.out.println("\nMail of this Identity should be updated in the file : " + 
								identity.getUid() + " - " + identity.getDisplayName());
			
			System.out.println("\nContent of an original file : ");
			dao.printContent();
			
			dao.update(identity);
		
			System.out.println("\nContent of an updated file : ");
			dao.printContent();
			
			dao.closeResources();		
		} catch (Exception e) {
			System.out.println("Error during a test of an update of data!");
			System.out.println(DETAILS + e.getMessage());
			throw new DAOUpdateException();			
		}
	}
	
	/**
	 * This method test deleting an identity from the file.
	 * It prints an original and a updated file for comparison.
	 * 
	 * @throws DAODeleteException 		the customized exception that can thrown during deleting
	 */
	public static void testDelete() throws DAODeleteException {
		
		try {
			FileIdentityDAO dao = new FileIdentityDAO();
			Identity identity = new Identity("2", "Janko Hrasko", "jh@gmail.com");
			
			System.out.println("\nThis Identity should be deleted from the file : " + identity);
			
			System.out.println("\nContent of an original file : ");
			dao.printContent();
			
			dao.delete(identity);
			
			System.out.println("\nContent of an updated file : ");
			dao.printContent();
			
			dao.closeResources();	
		} catch (Exception e) {
			System.out.println("Error during a test of a deletion of data!");
			System.out.println(DETAILS + e.getMessage());
			throw new DAODeleteException();			
		}
	}
}
