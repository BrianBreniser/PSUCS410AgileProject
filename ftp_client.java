// Used code found in the example provided at http://www.codejava.net/java-se/networking/ftp/connect-and-login-to-a-ftp-server
// to connect to an ftp server.
/*
Exit codes
----------
1   IOException on user input for server
2   IOException on user input for username
3   IOException on user input for password
4   User provided invalid arguments when running the program
*/
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import java.util.Scanner;
//import it.sauronsoftware.ftp4j.FTPClient;
//import it.sauronsoftware.ftp4j.FTPReply;

public class ftp_client {

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int port = 21;
        String server = "";
        String username = "";
        String password = "";
        
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
                System.out.print("Username: ");
                username = input.nextLine();
                if (username == null) {
                    System.out.println("Critical Failure! Exiting program with exit code 2");
                    System.exit(2);
                }

                // get password
                System.out.print("Password: ");
                password = input.nextLine();
                if (password == null) {
                    System.out.println("Critical Failure! Exiting program with exit code 3");
                    System.exit(3);
                }
            } else {
                // Invalid arguments
                System.out.println("Invalid arguments. Only arguments should be server name");
                System.exit(4);
            }

            FTPClient ftpClient = new FTPClient();

            try {
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

        command_loop();

        }
    }

    private static void command_loop() {
        Scanner input = new Scanner(System.in);

        System.out.print("Command: ");

        String commandInput;

        while(true) {
            commandInput = input.nextLine();

            switch (commandInput) {
                case "exit": exit();
                
                // case "user input": correspondingMethodName();
            }
        }
    }

    private static void exit() {
        System.out.println("Thanks for using this FTP client!");
        System.exit(0);
    }

}


