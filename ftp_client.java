// Used code found in the example provided at http://www.codejava.net/java-se/networking/ftp/connect-and-login-to-a-ftp-server
// to connect to an ftp server.
/*
Exit codes
----------
1   IOException on user input for server
2   IOException on user input for username
3   IOException on user input for password
4   User provided invalid arguments when running the program
5   Directory creation failure
6   Failed to connect
*/
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import java.net.InetAddress;
import java.util.Scanner;

//import it.sauronsoftware.ftp4j.FTPClient;
//import it.sauronsoftware.ftp4j.FTPReply;

public class ftp_client {

    private static String server;
    private static String username;
    private static String password;
    private static int port = 21;
    private static FTPClient ftpClient = new FTPClient();

    public static void directSetupArgs(String ser, String use, String pass, int por) {
        server = ser;
        username = use;
        password = pass;
        port = por;
    }

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    private static void setupServerUnamePass(String[] args) {
        Scanner input = new Scanner(System.in);

        authentication auth = new authentication();

        if (args.length == 0) {
            // if no arguments were passed in, prompt user for server, username, and password
            // get server
            System.out.print("Server: ");
            server = input.nextLine();
            if (server == null) {
                System.out.println("Critical Failure! Exiting program with exit code 1");
                System.exit(1);
            }
            // get username
            username = auth.getUsername();
            if (username == null) {
                System.out.println("Critical Failure! Exiting program with exit code 2");
                System.exit(2);
            }
            // get password
            password = auth.getPassword();
            if (password == null) {
                System.out.println("Critical Failure! Exiting program with exit code 3");
                System.exit(3);
            }
        } else if (args.length == 1) {
            // User provided server
            server = args[0];
            // get username
            username = auth.getUsername();
            if (username == null) {
                System.out.println("Critical Failure! Exiting program with exit code 2");
                System.exit(2);
            }

            // get password
            password = auth.getPassword();
            if (password == null) {
                System.out.println("Critical Failure! Exiting program with exit code 3");
                System.exit(3);
            }
        } else {
            // Invalid arguments
            System.out.println("Invalid arguments. Only arguments should be server name");
            System.exit(4);
        }
    }

    public static void setupFtp() {
        try {
            if(username.equals("testuser")) {
                port = 2121;
            }
            ftpClient.connect(server, port);
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                return;
            }

            boolean success = ftpClient.login(username, password);
            showServerReply(ftpClient);

            if (!success) {
                System.out.println("Could not login to the server");
            } else {
                System.out.println("LOGGED IN SERVER");
            }

        } catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
            System.exit(6);
        }

    }

    //Creates a new directory and makes it the current working directory
    public static void createDirectory(FTPClient f, String dir) throws IOException {
	
	//Check whether or not the directory already exists by attempting to navigate to it
	boolean exists = f.changeWorkingDirectory(dir);

	    if(!exists) {
	        if(!f.makeDirectory(dir)) {
		        throw new IOException("Failed to create directory" + dir + " error=" + f.getReplyString());
		    }

	        if(!f.changeWorkingDirectory(dir)) {
		        throw new IOException("Failed to change to directory" + dir + " error=" + f.getReplyString());
	        }
	    }
    }

    


    public static void main(String[] args) {
        // Set up server, username, and password to prepare FTP client
        setupServerUnamePass(args);

        // Set up ftp client with parameters
        setupFtp();

        // grab commands and do stuff for the user
        command_loop(ftpClient);

    }

    private static void command_loop(FTPClient f) {
        Scanner input = new Scanner(System.in);

        String commandInput;
	    String dirName;

        while(true) {
	    try {
                System.out.print("Command: ");
                commandInput = input.nextLine();

            switch (commandInput) {
                case "exit":
		            exit();
		            break;
                case "get address":
		            System.out.println(getRemoteAddress());
		            break;
		        case "create dir":
		            System.out.print("Directory name: ");
		            dirName = input.nextLine();
		            createDirectory(f, dirName);
		            break;
	      
                // case "user input": correspondingMethodName();
                }
	    }
            catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
            }
        }
    }

    /**
     * Will exit the application with a message
     */
    public static void exit() {
        System.out.println("Thanks for using this FTP client!");
        System.exit(0);
    }

    /**
     * @param f FTPClient
     * @return String
     * Pass in an FTPClient object as an argument
     * return, via a string, the remote address, may return qualified domain name,
     * or ip address, depending on circumstances of the object and machine.
     */
    public static String getRemoteAddress() {
        InetAddress addr = ftpClient.getRemoteAddress();

        return addr.getCanonicalHostName();
    }

}


