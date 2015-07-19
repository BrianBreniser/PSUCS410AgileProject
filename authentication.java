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
import java.security.*;


public class authentication {

    private Scanner input = new Scanner(System.in);
    private String user;
    private String pass;
    Console console = System.console();


    /* main() for testing purposes */
    /*
    public static void main(String [] args) {
        System.out.println("Calling getCredentials()");
        authentication cred = new authentication();
        int ret = cred.getCredentials();
    }
    */

    public String getUsername() {

        System.out.print("Username: ");
        user = input.next();
        return user;
    }

    public String getPassword() {

        try {
            char [] passwordChar = console.readPassword("Password: ");
            pass = new String(passwordChar);
        }
        catch (NullPointerException e) {
            System.out.print("Password: ");
            pass = input.next();
        }

        return pass;
    }

    private int hashPassword(String pw) {

        // Hash pw
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(pw.getBytes());
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