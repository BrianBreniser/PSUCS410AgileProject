/*  Name:   connection.java
 *  About:  Stores data for a connection to the server
 *  Proj:   Eiusmod FTP Client
 *  Author: Geoff Maggi
 */

import com.google.gson.Gson;
import org.apache.commons.net.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class connection {
  /* ----- Class Data ----- */
  public String alias = ""; //This must be unique when saved(Checked in savedConnections class)
  public String server = "";
  public int port = 21; //Default value
  public String user = "";
  private String password = ""; //This is the encrypted password

  /* ----- main() for testing ----- */
  /*public static void main(String [] args) {
    System.out.println("Running connection tests");
    connection con = new connection("test", "ftptest.com", 2021, "user1", "password1!");
    System.out.println(con);
    System.out.println();
    
    String json = con.toJson();
    System.out.println("JSON:");
    System.out.println(json);
    System.out.println();
    
    System.out.println("New object from JSON:");
    connection newCon = new connection();
    newCon.setFromJson(json);
    System.out.println(newCon);
    System.out.println(newCon.getPassword()); //test the password
  }*/

  /* ----- Initializers ----- */
  connection() { } //just use defaults
  
  connection(String alias) {
    this.alias = alias;
  }
  
  connection(String alias, String server, String user, String password) {
    this.alias = alias;
    this.server = server;
    //port set by default
    this.user = user;
    setPassword(password);
  }
  
  connection(String alias, String server, String port, String user, String password) throws Exception {
    this.alias = alias;
    this.server = server;
    
    try { setPort(port); }
      catch(Exception e) { throw e; }
      
    this.user = user;
    setPassword(password);
  }
  
  connection(String alias, String server, int port, String user, String password) {
    this.alias = alias;
    this.server = server;
    this.port = port;
    this.user = user;
    setPassword(password);
  }
  
  /* ----- Set functions ----- */
  public void setPassword(String password) {
    this.password = encrypt(password);
  }
  
  public void setPort(String port) throws Exception {
    try {
      int test = Integer.parseInt(port);
      this.port = test;
    }
    catch (Exception e) {
      throw new Exception("Not a valid Port");
    }
  }
  
  public void setFromJson(String json) {
    Gson gson = new Gson();
    connection con = new connection();
    con = gson.fromJson(json, connection.class);
    this.copy(con);
  }
  
  public void copy(connection con) { //messy :(
    if(con==null) {
      return; //Nothing to do....
    }
    this.alias = con.alias;
    this.server = con.server;
    this.port = con.port;
    this.user = con.user;
    this.password = con.password; //no need to encrypt
  }
  
  /* ----- Get functions ----- */
  public String getPassword() {
    return decrypt(password);
  }
  
  public boolean hasPassword() {
    if(password != null && !password.isEmpty())
      return true;
    else
      return false;
  }
  
  /* ----- Print function ----- */
  public String toString() {
    String ret;
    ret =  "Alias: " + alias;
    ret += " | Server: " + server + " (" + port + ")";
    ret += " | User: " + user;
    ret += " | Password?: ";
    ret += hasPassword()?"Y":"N";
    return ret;
  }

  public String toJson() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
  
  /* ----- Private Functions ----- */
  private String encrypt(String s) {
    try{
      SecretKeySpec key = getKey();
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, key);
      final String encryptedString = Base64.encodeBase64String(cipher.doFinal(s.getBytes()));
      return encryptedString;
    }
    catch(Exception e) {return null;}
  }
  
  private String decrypt(String s) {
    try{
      SecretKeySpec key = getKey();
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, key);
      final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(s)));
      return decryptedString;
    }
    catch(Exception e) {return null;}
  }
  
  private SecretKeySpec getKey() {
    byte[] kv = "EiusmodSecretKey".getBytes(); //must be 16 bytes
    SecretKeySpec key = new SecretKeySpec(kv, "AES");
    return key;
  }
};