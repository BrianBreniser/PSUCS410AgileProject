/* 
 *   authentication.java 
 *   
 *   Manages routines involving user authentication, 
 *   including username and password management (input,
 *   authenitcation, hashing, and storage   
 *
 */

import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import java.security.*;


public class authentication {
	
	private Scanner input = new Scanner(System.in);
	private JPasswordField passwordField = new JPasswordField(20);
	private String user;
	private String pass;

	/* main() for testing purposes */
	public static void main(String [] args) {
		System.out.println("Calling getCredentials()");
		authentication cred = new authentication();
		int ret = cred.getCredentials();
	}
	
	public int getCredentials() {
		
		System.out.print("Username: ");
		user = input.next();
		System.out.print("Password: ");
        pass = input.next();
        
        // Hash pw
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(pass.getBytes());
            String encryptedString = new String(messageDigest.digest());
        }
        catch (NoSuchAlgorithmException e) {
        	System.err.println("Not a valid message digest algorithm");
        	return -1;
        }

        // Check database for user match (and if so password match)
        // If checks out, return 0
        // Else return -1
        
        // If user not in db, add to db
        
		return 0;
	}
	
};