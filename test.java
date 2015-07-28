/*
 * How to use this module:
 * Run this program after running make:
 *   java -cp .:./lib:./lib/commons-net-3.3.jar -ea test
 * need the -ea options or else the assert statements WILL NOT RUN
 * I recommend doing this from the command line
 * in intellij IDEA it works in the included terminal, alt+f12
 */
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import java.net.InetAddress;
import java.lang.Process;
import java.lang.Runtime;

public class test {

    private static Runtime rt = Runtime.getRuntime();
    private static String thesystem = "linux";

    /**
     * @param args
     * Assumes the running system is linux unless an argument of 'windows' or 'Windows' is given
     * will run the test suite against a localhost ftp server with static username and password
     */
    public static void main(String[] args) {

        // Run the ftp server that we will test with
        Process pr = null;
        if (args.length > 0) {
            thesystem = args[0];
        }
        try {
            if (thesystem.equals("windows") || thesystem.equals("Windows")) {
                pr = rt.exec("sfk-windows.exe ftpserv -port=2121 -user=testuser -pw=password");
            } else {
                pr = rt.exec("./sfk ftpserv -port=2121 -user=testuser -pw=password");
            }
        } catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
        }

        // build our ftp client

        ftp_client ftp = new ftp_client();
        ftp.directSetupArgs("localhost", "testuser", "password", 2121);
        ftp.setupFtp();

        /*
         * Run your tests in here, please label what the test should do ------------------
         */

        // Some formatting to make finding errors easier
        System.out.println();
        System.out.println();
        System.out.println(" --------- Errors: -----------");
        System.out.println();

        assert (ftp.getRemoteAddress().equals("localhost"));

        /* Test getFile() and putFile(). Methods throw IO exception, so tests must be able to catch end.
         * Just uses simple "test.txt".  Since test ftp server is running locally (and in same directory
         * as execution) the file will already be there.  I simply checked the files modification date
         * to assure the tests ran and updated file.  If failed, they would throw an exception anywho, or
         * return false, depending on the error.
         */
        try {
            assert (ftp.getFile("get test.txt") == true);
        } catch (IOException ex) {
            System.out.println("Error testing getFile()");
            ex.printStackTrace();
        }
        try {
            assert (ftp.putFile("get test.txt") == true);
        } catch (IOException ex) {
            System.out.println("Error testing putFile()");
            ex.printStackTrace();
        }

    // tests related to making a directory on the ftp server
    // 1. create a directory in the current working directory
    // 2. create a directory that exists
    // 3. create a directory with a relative path
    // 4. create a directory along a path that partially exists

    try {
        assert (ftp.createDirectory("test") == true);
    } catch (IOException ex) {
        System.out.println("Unable to create directory");
        ex.printStackTrace();
    }

    try {
        assert (ftp.createDirectory("test") == true);
    } catch (IOException ex) {
        System.out.println("Creating a directory that exists failed");
        ex.printStackTrace();
    }
    try {
        assert (ftp.createDirectory("./test1/test2/test3") == true);
    } catch (IOException ex) {
        System.out.println("Unable to create directory");
        ex.printStackTrace();
    }

    try {
        assert (ftp.createDirectory("./test1/test2/test3/test4/test5") == true);
    } catch (IOException ex) {
        System.out.println("Unable to create directory");
        ex.printStackTrace();
    }

        /*
         * Done running tests here -------------------------------------------------------
         */

        // end formatting errors
        System.out.println();
        System.out.println(" No Errors :) ");
        System.out.println();
        System.out.println();

        // kill the ftp server if it was successfully made
        if(pr != null) {
            pr.destroy();
        }
    }

}

