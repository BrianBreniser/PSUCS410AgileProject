// Used code found in the example provided at http://www.codejava.net/java-se/networking/ftp/connect-and-login-to-a-ftp-server
// to connect to an ftp server.

import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

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
	
		int port = 21;
		String server;
		String user;
		String pass;

		if(args.length != 3) {
			System.out.println("Usage: ftp_client <server> <username> <password>");
			System.exit(-1);
		}

		server = args[0];
		user = args[1];
		pass = args[2];

		FTPClient ftpClient = new FTPClient();
	
		try {
	        	ftpClient.connect(server, port);
	        	showServerReply(ftpClient);
	        	int replyCode = ftpClient.getReplyCode();
	
	        	if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Operation failed. Server reply code: " + replyCode);
	                	return;
	            	}
	
			boolean success = ftpClient.login(user, pass);
			showServerReply(ftpClient);
	
			if (!success) {
	        	        System.out.println("Could not login to the server");
	        	        return;
	        	    } else {
	                System.out.println("LOGGED IN SERVER");
	            }
	
		} catch (IOException ex) {
	        	System.out.println("Oops! Something wrong happened");
	        	ex.printStackTrace();
	 	}
	}

}


