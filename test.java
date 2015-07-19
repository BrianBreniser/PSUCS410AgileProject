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
















        /*
         * Run your tests in here, please label what the test should do
         */

        assert(1 == 1);

        if(pr != null) {
            pr.destroy();
        }
    }

}

