package fr.epita.iam.tests.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class tests a connection and a cooperation with database.
 * 
 * @author Lenka Horvathova
 */
public class TestDatabase {

	/**
	 * This is a main method for testing an insertion into the database.
	 * 
	 * @param args					arguments from command line, unused
	 * @throws SQLException			an exception that provides information on a database access error
	 */
	public static void main(String[] args) throws SQLException {
		String connectionString = "jdbc:derby://localhost:1527/iam;create=true";
		String user = "IAM";
		String password = "123";
		Connection connection = DriverManager.getConnection(connectionString, user, password);
		
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT into TESTDATABASE.PEOPLE "
				+ "(IDENTITY_DISPLAYNAME, IDENTITY_EMAIL, IDENTITY_BIRTHDATE) "
				+ "values(?, ?, ?)");
		
		preparedStatement.setString(1, "Quentin");
		preparedStatement.setString(2, "qdec@qdec.com");
		preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
		
		preparedStatement.execute();
		preparedStatement.close();
		
		preparedStatement = connection.prepareStatement("select * from TESTDATABASE.PEOPLE");
		
		ResultSet results = preparedStatement.executeQuery();
		
		while (results.next()) {
			
			String displayName = results.getString("IDENTITY_DISPLAYNAME");
			String email = results.getString("IDENTITY_EMAIL");
			Date birthDate = results.getDate("IDENTITY_BIRTHDATE");
			
			System.out.println(displayName + "-" + email + "-" + birthDate);
		}		
		
		preparedStatement.close();
		results.close();
		connection.close();
	}

}
