package fr.epita.iam.launcher;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.DAODeleteException;
import fr.epita.iam.exceptions.DAOInitializationException;
import fr.epita.iam.exceptions.DAOSaveException;
import fr.epita.iam.exceptions.DAOSearchException;
import fr.epita.iam.exceptions.DAOUpdateException;
import fr.epita.iam.services.Authenticator;
import fr.epita.iam.services.FileIdentityDAO;
import fr.epita.iam.services.IdentityDAO;
import fr.epita.iam.services.JDBCIdentityDAO;
import fr.epita.logging.LogConfiguration;
import fr.epita.logging.Logger;

/**
 * This is a Launcher used for communicating with a user.
 * 
 * @author Lenka Horvathova
 */
public class Launcher {

	private static final String DETAILS = "Details: ";

	/**
	 * At start, it asks user to choose, if he/she wants to work with a file or a database version of the program.
	 * Then, he/she can reset the file or table to the original example state. 
	 * At the beginning it is already in that state.
	 * Of course, for a file version, file has to be resent in the "/temp/test" directory.
	 * For a database version, a table has to be set.
	 * Then the 'real' program starts.
	 * First, it authenticates the user.
	 * If it is the admin ('adm', 'pwd'), it lets him in, otherwise it exits the launcher.
	 * Once the user is authenticated, he chooses from following options : 
	 * 		-Creating a new identity;
	 * 		-Modifying an existing identity;
	 * 		-Deleting an existing identity;
	 * 		-Exiting the program.
	 * Each of these options has several substeps,
	 * 	like providing parameters for the new identity, specifying the UID, etc.
	 * While the program is running, it stores information about performed actions in '/temp/application.log'.
	 * 
	 * @param args							arguments from command line, unused
	 * @throws FileNotFoundException 		signals that an attempt to open the file 
	 * 											denoted by a specified pathname has failed
	 * @throws FileNotFoundException		the exception that can be thrown because of Logger
	 * @throws DAOInitializationException   the customized exception that can thrown during initializing
	 */
	public static void main(String[] args) throws FileNotFoundException, DAOInitializationException {
		
		Scanner scan = new Scanner(System.in);
		LogConfiguration conf = new LogConfiguration("/temp/application.log");
		Logger logger = new Logger(conf); //may throws an 'FileNotFoundException' exception 
		
		System.out.println("Please, choose options you prefer : ");
		IdentityDAO dao = daoChoice(logger, scan); //a separate method for choosing a DAO
		resetChoice(dao, logger, scan);	//a separate method for an option of a reset to the original example version 
										//may throws an 'NullPointerExcetion' exception
		
		String uid = "";
		String displayName = "";
		String email = "";
		
		System.out.println("~ ~ WELCOME ~ ~");
		System.out.println("Please, authenticate yourself!");		
		System.out.print("Username : ");
		String username = scan.nextLine();		
		System.out.print("Password : ");
		String password = scan.nextLine();		
		logger.log("User '" + username + "' tries to authenticate.");
		
		if (Authenticator.authenticate(username, password)) {			
			System.out.println("Successfully authenticated!");
			logger.log("Successfully authenticated!");			
			System.out.println("Please, choose an option you want to perform : ");			
			String choice = "";
			
			while (!"4".equals(choice)) {					
				System.out.println(" \t 1. Create identity");
				System.out.println(" \t 2. Update identity");
				System.out.println(" \t 3. Delete identity");
				System.out.println(" \t 4. Quit");				
				System.out.print("Your choice : ");
				choice = scan.nextLine();				
				logger.log("User chose the " + choice + ". choice.");
				
				switch (choice) {				
				case "1":
					System.out.println("You chose an Identity Creation!");
					logger.log("Identity Creation selected.");
					
					if (dao instanceof FileIdentityDAO) {
						System.out.print("Insert UID : ");
						uid = scan.nextLine();
					}							
					System.out.print("Insert name : ");
					displayName = scan.nextLine();					
					System.out.print("Insert email : ");
					email = scan.nextLine();
					
					try {
						dao.save(new Identity(uid, displayName, email)); //may throws an 'NullPointerExcetion' exception
						System.out.print("Identity was saved successfully! \n");
						logger.log("A new identity was saved successfully!");	
					} catch (DAOSaveException e) {
						System.out.println("Error during a save of data!");
						System.out.println(DETAILS + e.getMessage());
					}									
					break;
					
				case "2":
					System.out.println("You chose an Identity Update!");
					logger.log("Identity Update selected.");	
					
					System.out.print("Insert UID of an identity, you want to update : ");
					uid = scan.nextLine();					
					System.out.print("Update name : ");
					displayName = scan.nextLine();					
					System.out.print("Update email : ");
					email = scan.nextLine();
					
					try {
						dao.update(new Identity(uid, displayName, email)); //may throw NullPointerException
						System.out.print("Identity was updated successfully! \n");
						logger.log("An identity was updated successfully!");	
					} catch (DAOUpdateException e) {
						System.out.println("Error during a update of data!");
						System.out.println(DETAILS + e.getMessage());
					}			
					break;
					
				case "3":
					System.out.println("You chose an Identity Deletion!");
					logger.log("Identity Deletion selected.");
					
					System.out.print("Insert UID of a identity, you want to delete : ");
					uid = scan.nextLine();
					
					try {
						dao.delete(new Identity(uid, displayName, email)); //may throws an 'NullPointerExcetion' exception
						System.out.print("Identity was deleted successfully! \n");
						logger.log("An identity was deleted successfully!");
					} catch (DAODeleteException e) {
						System.out.println("Error during a deletion of data!");
						System.out.println(DETAILS + e.getMessage());
					}					
					break;	
					
				case "4":
					System.out.println("You decided to quit!");
					logger.log("Quiting selected.");					
					scan.close();
					break;
					
				default:
					System.out.println("Invalid choice! Please, choose number 1-4 for an action:");
					logger.log("Invalid choice selected.");					
					break;
				}
			}
		} else {			
			System.out.println("Wrong username or password!");
			logger.log("Wrong username or password!");			
			System.out.println("You did not pass authentication. Program will automatically close.");
		}		
		System.out.println("Thank you for your visit!");
		logger.log("The session was closed.");	
		System.out.println("~ ~ GOODBYE ~ ~");
	}
	
	/**
	 * This method gives and option to the user to choose a DAO he/she wants to use.
	 * First option is working with a file,
	 * second option is working with a database.
	 * 
	 * @param logger							a logger for keeping a track of action a user took
	 * @param scan								a scanner used for a communication with a user
	 * @return									returns a IdentityDAO a user chose
	 * @throws DAOInitializationException 		the customized exception that can thrown during initializing
	 */
	private static IdentityDAO daoChoice(Logger logger, Scanner scan) throws DAOInitializationException {
		
		System.out.println("- Which DAO would you like to work with?");
		System.out.println("\t A: a file;\n \t B: a database.");		
		
		IdentityDAO dao = null;		
		String answer = "";
		Set<String> correctAnswers = new HashSet<>(Arrays.asList( new String[] {"A", "a", "B", "b"}));
		
		while (!correctAnswers.contains(answer)) {			
			System.out.print("Your choice : ");
			answer = scan.nextLine();
			
			switch (answer) {
			case "A": case "a":
				dao = new FileIdentityDAO();
				try {
					logger.log("File version was selected.");				
					System.out.println("The file you will work with : ");
					dao.printContent();
					System.out.println("");		
				} catch (DAOSearchException e) {
					e.printStackTrace();
				}		
				break;
								
			case "B": case "b":
				dao = new JDBCIdentityDAO();
				try {
					logger.log("Database version was selected.");				
					System.out.println("The table you will work with : ");
					dao.printContent();
					System.out.println("");	
				} catch (DAOSearchException e) {
					e.printStackTrace();
				}			
				break;
								
			default: 
				System.out.println("Invalid choice! Please, choose 'A' or 'B' : ");
				logger.log("Invalid choice selected.");				
				break;
			}
		}		
		return dao;
	}
	
	/**
	 * This method give a user an option to reset a DAO source (a file or a table in database) to the original example version.
	 * 
	 * @param dao					a chosen DAO
	 * @param logger				a logger for keeping a track of action a user took
	 * @param scan					a scanner used for a communication with a user
	 */
	private static void resetChoice(IdentityDAO dao, Logger logger, Scanner scan) {
		
		System.out.println("- Do you want to reset it to the original?");
		System.out.println("\t A: yes;\n \t B: no.");
		
		String answer = "";
		Set<String> correctAnswers = new HashSet<>(Arrays.asList(new String[] {"A", "a", "B", "b"}));
		
		while (!correctAnswers.contains(answer)) {			
			System.out.print("Your choice : ");
			answer = scan.nextLine();
			
			switch (answer) {
			case "A": case "a":
				
				if (dao instanceof JDBCIdentityDAO) {
					System.out.println("Please, before continuing, make sure, there the table 'IAM' is set using 'IAMSQL.sql' query.");
				} else {
					System.out.println("Please, before continuing, make sure, there is the original file 'identities.txt' in the '/temp' folder.");
				}							
				System.out.print("Press ENTER to continue!");
				String button = scan.nextLine();
				
				if (button != null) {					
					try {
						dao.resetToOriginal();
						logger.log("The content was reset to the original version.");	
						System.out.println("The file you will work with : ");
						dao.printContent();
						System.out.println("");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}						
				break;
								
			case "B": case "b":
				try {
					logger.log("The content was not reset to the original version.");				
					System.out.println("The table you will work with : ");
					dao.printContent();
					System.out.println("");	
				} catch (DAOSearchException e) {
					e.printStackTrace();
				}			
				break;
								
			default: 
				System.out.println("Invalid choice! Please, choose 'A' or 'B' : ");
				logger.log("Invalid choice selected.");				
				break;
			}
		}
	}
}
