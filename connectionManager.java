/*  Name:   connectionManager.java
 *  About:  Stores an array of Connections and all the functions to manage them
 *  Proj:   Eiusmod FTP Client
 *  Author: Geoff Maggi
 */

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class connectionManager {
  /* ----- Class Data ----- */
  ArrayList<connection> connections = new ArrayList<connection>();

  /* ----- Main function used to test ----- */
  /*public static void main(String [] args) {
    connectionManager cm = new connectionManager();
    cm.run();
  }*/
  
  /* ----- Main function for the class ----- */
  public void run() {
    Scanner input = new Scanner(System.in);
    System.out.println("----- Connection Manager -----");
    boolean done = false;
    while(!done) {
      System.out.println();
      listOptions();
      System.out.print("Select an action: ");
      int choice = input.nextInt();
      System.out.println();
      
      switch(choice) {
        case 1: //list connections
          list();
        break;
        case 2: //load connections
          load("default.con");
        break;
        case 3: //save connections
          save("default.con");
        break;
        case 4: //select connection
          select();
        break;
        case 5: //add connection
          add();
        break;
        case 6: //edit connection
          edit();
        break;
        case 7: //delete connection
          delete();
        break;
        case 8: //exit
          done = true;
        break;
        default:
          System.out.println("Not a valid option");
        break;
      }
    }
  }

  /* ----- Initializers ----- */
  connectionManager() { }
  
  connectionManager(String path) {
    load(path);
  }
  
  /* ----- Public Functions ----- */
  public void load(String path) {
    String line = null;
    try{
      BufferedReader br = new BufferedReader(new FileReader(path));
      while ((line = br.readLine()) != null) {
        connection con = new connection();
        con.setFromJson(line);
        add(con);
      }       
    }
    catch(Exception e){ }
  }
  
  public void save(String path) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(path));
      for(connection con: connections) {
        writer.write(con.toJson() + "\r\n");
      }
      writer.close( );
      System.out.println("Saved Successfully");
    }
    catch ( IOException e) { }
  }
  
  public void add() {
    connection con = new connection();
    edit(con);
    add(con);
  }
  
  public void add(connection con) {
    if(con.alias != null && !con.alias.isEmpty())
      if(!aliasExists(con.alias)) {
        connections.add(con);
        //System.out.println("Added: " + con.alias);
      }
      else
        System.out.println("Alias already exists");
    else
      System.out.println("Alias cannot be empty");
  }
  
  public void edit() {
    connection con = select();
    if(con != null)
      edit(con);
  }
  
  public void edit(int loc) {
    edit(connections.get(loc));
  }
  
  public void edit(connection con) {
    if(con == null) {
      System.out.println("Not a valid connection");
      return;
    }
    Scanner input = new Scanner(System.in);
    authentication auth = new authentication();
    while(true) {
      System.out.println();
      listConnectionData(con);
      System.out.print("Select an option:");
      int choice = input.nextInt();
      input.nextLine(); //clears the newline
      System.out.println();
      switch(choice) {
        case 1: //Alias
          System.out.print("Enter an Alias: ");
          con.alias = input.nextLine();
        break;
        case 2: //Server
          System.out.print("Enter a Server: ");
          con.server = input.nextLine();
        break;
        case 3: //Port
          System.out.print("Enter an Port: ");
          con.port = input.nextInt();
        break;
        case 4: //User
          con.user = auth.getUsername();
        break;
        case 5: //Password
          con.setPassword(auth.getPassword());
        break;
        case 6: //Exit
          return;
        //break;
        default:
          System.out.println("Not a valid option");
        break;
      }
    }
  }
  
  public void delete() {
    connection con = select();
    if(con != null)
      delete(con);
  }
  
  public void delete(int loc) {
    if(loc >= 0 && loc < connections.size())
      connections.remove(loc);
    else
      System.out.println("Unable to delete");
  }
  
  //hack-ish
  public void delete(connection con) {
    int loc = findByAlias(con.alias);
    delete(loc);
  }
  
  public void list() {
    int i = 0;
    System.out.println("---(Alias) User@Server---");
    for(connection con: connections) {
      System.out.println(i + ". (" + con.alias + ") " + con.user + "@" + con.server);
      i++;
    }
  }
  
  //returns null on error
  public connection select() {
    if(connections.size() < 1) {
      System.out.println("No connections exist");
      return null;
    }
    
    Scanner input = new Scanner(System.in);
    while(true) {
      list();
      System.out.println(connections.size() + ". Exit");
      System.out.print("Select a connection:");
      int choice = input.nextInt();
      System.out.println();
      if(choice == connections.size()) {
        return null;
      }
      else if(choice >= 0 && choice < connections.size())
        return select(choice);
      else
        System.out.println("Not a valid choice");
    }
  }

  public connection select(int loc) {
    return connections.get(loc);
  }
  
  public connection select(String alias) {
    int loc = findByAlias(alias);
    return connections.get(loc);
  }
  
  /* ----- Private Functions ----- */
  
  //Checks to see if the alias already exists
  private boolean aliasExists(String newAlias) {
    for(connection con: connections) {
      if(con.alias.equals(newAlias))
        return true;
    }
    return false;
  }
  
  private int findByAlias(String alias) {
    for(int i = 0; i < connections.size(); i++) {
      if(connections.get(i).alias.equals(alias))
        return i;
    }
    return -1;
  }
  
  private void listOptions() {
    System.out.println("1. List Connections");
    System.out.println("2. Load Connections");
    System.out.println("3. Save Connections");
    System.out.println("4. Select Connection");
    System.out.println("5. Add Connection");
    System.out.println("6. Edit Connection");
    System.out.println("7. Delete Connections");
    System.out.println("8. Exit Connection Manager");
  }
  
  //used in edit(connection)
  private void listConnectionData(connection con) {
    System.out.println("1. Alias     - " + con.alias);
    System.out.println("2. Server    - " + con.server);
    System.out.println("3. Port      - " + con.port);
    System.out.println("4. User      - " + con.user);
    System.out.println("5. Password? - " + (con.hasPassword()?"Y":"N"));
    System.out.println("6. Exit");
  }
  
  /* ----- Print function ----- */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for(connection con: connections) {
      sb.append(con+"\r\n");
    }
    return sb.toString();
  }
};