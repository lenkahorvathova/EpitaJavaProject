package fr.epita.iam.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.DAOClosureException;
import fr.epita.iam.exceptions.DAODeleteException;
import fr.epita.iam.exceptions.DAOInitializationException;
import fr.epita.iam.exceptions.DAOSaveException;
import fr.epita.iam.exceptions.DAOSearchException;
import fr.epita.iam.exceptions.DAOUpdateException;

/**
 * This class manages identities using a file.
 * It implements an IdentityDAO interface.
 * 
 * @author Lenka Horvathova
 */
public class FileIdentityDAO implements IdentityDAO {
	
	private static final String DETAILS = "Details: ";

	private static final String IDENTITY_FILE_PATH = "/temp/tests/identities.txt";
	
	private PrintWriter printer;
	private Scanner scanner;
	private FileWriter writer;
	private FileReader reader;
	
	/**
	 * This is a Constructor for a FileIdentityDAO class.
	 * A file is prepared with every initialization. 
	 */
	public FileIdentityDAO() {
		
		File file = new File(IDENTITY_FILE_PATH);	
		
		initFile(file); //a separate method for an initialization of a file is used		
		System.out.println("All the identities can be found in this file : " + file.getAbsolutePath());			
	}

	/* (non-Javadoc)
	 * @see fr.epita.iam.services.IdentityDAO#save()
	 */
	@Override
	public void save(Identity identity) throws DAOSaveException {
		
		ArrayList<Identity> result = scanAllIdentities();
		
		result.add(identity);		
		updateFile(result); //a separate method for an update of a file is used
	}
	
	/* (non-Javadoc)
	 * @see fr.epita.iam.services.IdentityDAO#search()
	 */
	@Override
	public List<Identity> search(Identity criteria) throws DAOSearchException {
		
		ArrayList<Identity> results = new ArrayList<>();
		
		while (this.scanner.hasNext()) {			
			Identity identity = scanIdentity(scanner); //a separate method for a scanning of an identity is used		
			
			if ((criteria == null) 
			 || (identity.getUid().equals(criteria.getUid()))
			 || (identity.getDisplayName().startsWith(criteria.getDisplayName()))) {				
				results.add(identity);	
			}			
		}		
		scanReset(); //a separate method for a reset of a scanner is used
		
		return results;		
	}
	
	/* (non-Javadoc)
	 * @see fr.epita.iam.services.IdentityDAO#update()
	 */
	@Override
	public void update(Identity updatedIdentity) throws DAOUpdateException {
		
		ArrayList<Identity> results = new ArrayList<>();
		
		while (this.scanner.hasNext()) {
			Identity currentIdentity = scanIdentity(scanner); //a separate method for a scanning of an identity is used
			
			if (updatedIdentity.getUid().equals(currentIdentity.getUid())) {				
				results.add(updatedIdentity);
			} else {				
				results.add(currentIdentity);
			}
		}
		updateFile(results); //a separate method for an update of a file is used
	}
	
	/* (non-Javadoc)
	 * @see fr.epita.iam.services.IdentityDAO#delete()
	 */
	@Override
	public void delete(Identity identity) throws DAODeleteException {
		
		ArrayList<Identity> results = new ArrayList<>();
		
		while (this.scanner.hasNext()) {
			Identity currentIdentity = scanIdentity(scanner); //a separate method for a scanning of an identity is used
			
			if (!identity.getUid().equals(currentIdentity.getUid())) {				
				results.add(currentIdentity);				
			}
		}
		updateFile(results); //a separate method for an update of a file is used
	}
	
	/* (non-Javadoc)
	 * @see fr.epita.iam.services.IdentityDAO#closeResources()
	 */
	@Override
	public void closeResources() throws DAOClosureException {		
		
		try {
			this.scanner.close();
			this.printer.close();
			this.writer.close();
			this.reader.close();			
		} catch (Exception e) {			
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
		File oldFile = new File(IDENTITY_FILE_PATH);
		File orgFile = new File("/temp/identities.txt");		
		initFile(orgFile); //a separate method for an initialization of a file is used			
				
		try {						
			oldFile.delete();
			Files.copy(orgFile.toPath(), oldFile.toPath(), StandardCopyOption.REPLACE_EXISTING);			
			oldFile = new File(IDENTITY_FILE_PATH);
			initFile(oldFile); //a separate method for an initialization of a file is used				
		} catch (IOException e) {			
			System.out.println("Error during a reset of a file!");
			System.out.println(DETAILS + e.getMessage());
			throw new DAOInitializationException();
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.epita.iam.services.IdentityDAO#printContent()
	 */
	@Override
	public void printContent() throws DAOSearchException {
		List<Identity> identities = search(null);
		
		for (Identity identity : identities) {
			System.out.println(identity);
		}
	}
	
	/**
	 * This method creates a new file, if doesn't exist, and sets a printer and a scanner.
	 * 
	 * @param file				the file to be prepared
	 */
	private void initFile(File file) {
		
		if ((this.printer != null) 	|| (this.scanner != null) 
		|| 	(this.writer != null) 	|| (this.reader != null)) {			
			try {
				closeResources();
			} catch (DAOClosureException e) {
				e.printStackTrace();
			}
		}
		
		try {		
			if (!file.exists()) {				
				file.getParentFile().mkdirs();
				file.createNewFile();
			}			
			writer = new FileWriter(file,true);
			reader = new FileReader(file);			
		} catch (IOException e) {			
			System.out.println("Error during a preparation of a file!");
			System.out.println(DETAILS + e.getMessage());
		}
		
		if (writer == null || reader == null) {				
			System.out.println("Error during a preparation of a writer and a reader for a file!");
			return;			
		} else {			
			try {				
				this.printer = new PrintWriter(writer);
				this.scanner = new Scanner(file);				
			} catch (FileNotFoundException e) {				
				System.out.println("Error during a preparation of a printer and a scanner for a file!");
				System.out.println(DETAILS + e.getMessage());
			}
		}
	}
	
	/**
	 * This method updates a file after an addition, update or deletion of an identity.
	 * 
	 * @param results			the updated list of identities to be saved 
	 * @throws DAOInitializationException 
	 */
	private void updateFile(ArrayList<Identity> results) {
		
		File tmpFile = new File("/temp/tests/identities.tmp.txt");		
		initFile(tmpFile); // a separate method for a initialization of a file is used

		for (Identity tobeSaved : results) {			
			printIdentity(tobeSaved); // a separate method for a print of an identity is used
		}
		
		if ((this.reader != null) || (this.writer != null)) {			
			try {
				closeResources();
			} catch (DAOClosureException e) {
				e.printStackTrace();
			}
		}
			
		File oldFile = new File(IDENTITY_FILE_PATH);	
			
		try {					
			oldFile.delete();
			Files.move(tmpFile.toPath(), oldFile.toPath(), StandardCopyOption.REPLACE_EXISTING);		
			oldFile = new File(IDENTITY_FILE_PATH);
			initFile(oldFile);			
		} catch (IOException e) {			
			System.out.println("Error during an update of a file!");
			System.out.println(DETAILS + e.getMessage());
		}
	}
	
	/**
	 * This is a method for printing an identity.
	 * It records the identity in the file using the format:
	 * 
	 * <pre>
	 * --- Identity ---
	 * uid
	 * displayName
	 * email
	 * --- Identity ---
	 * </pre>
	 * 
	 * @param identity			the identity to record
	 */
	private void printIdentity(Identity identity) {
		
		this.printer.println("--- Identity ---");		
		this.printer.println(identity.getUid());
		this.printer.println(identity.getDisplayName());
		this.printer.println(identity.getEmail());		
		this.printer.println("--- Identity ---");
		this.printer.flush();
	}
	
	/**
	 * This method reads an identity from the file with identities.
	 * 
	 * @param scanner			the scanner used for reading the file
	 * @return					the read identity
	 */
	private Identity scanIdentity(Scanner scanner) {
		
		scanner.nextLine();		
		String uid = scanner.nextLine();
		String displayName = scanner.nextLine();
		String mail = scanner.nextLine();		
		scanner.nextLine();
		
		return new Identity(uid, displayName, mail);		
	}
	
	/**
	 * This method reads all identities in the file.
	 * 
	 * @return					the list of all identities
	 */
	private ArrayList<Identity> scanAllIdentities() {
		
		ArrayList<Identity> results = new ArrayList<>();		
		while (scanner.hasNext()) {			
			results.add(scanIdentity(scanner));
		}
		scanReset(); //a separate method for a reset of a scanner is used
		
		return results;
	}
	
	/**
	 * This method resets a scanner to the beginning of the file.
	 */
	private void scanReset() {
		
		try {			
			this.scanner.close();
			this.scanner = new Scanner(new File(IDENTITY_FILE_PATH));			
		} catch (FileNotFoundException e) {			
			System.out.println("Error during a reset of a scanner!");	
			System.out.println(DETAILS + e.getMessage());
		}
	}
}
