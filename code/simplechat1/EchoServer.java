// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{

  static class Message extends Thread {
    /* Executes when server suddenly shuts down (alt-f4 or ^C) */
    public void run() {
       log.deleteFile();
    }
 }

  //Class variables *************************************************
  private boolean is_closed=false;
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  private int max_users = 10;
  Object[][] clients = new Object[max_users][2];
  int user_index = 0;
  private static Log log;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    String[] array_msg = Objects.toString(msg).split("@");
    String id = array_msg[0];
    String message = array_msg[1];
    if (message.startsWith("#login")) {
      if (userIsLogged(client)) {
        System.out.println("Already logged in");
      }else{
        setInfo(client, id);
      }
    }
    else{
      if (userIsLogged(client)) {
        try {
          if (message.contains("#save")) { //if user wants to save logs
            sendPrivateMessage(id, Log.saveLog());
          }
          else if (message.startsWith("#list")) { //if user wants a list of online users
            sendOnlineUsers(id);
          }
          else if (message.startsWith("$")) { //if user sends private message
            sendPrivateMessageFrom(id, message.substring(1));
          }
          else{
            String tmp_msg = id+" > "+message;
            System.out.println(tmp_msg);
            this.sendToAllClients(tmp_msg);
            Log.writeLog(tmp_msg);
          }
        } catch (IOException e) {
          System.out.println("something wrong");
        }
      }
    }
    }
    
  public void setInfo(ConnectionToClient client, String id){
      this.clients[user_index][0]=client;
      this.clients[user_index][1]=id;
      this.user_index++;
      System.out.println("#login "+id);
    }

  public boolean userIsLogged(ConnectionToClient client){
    boolean flag = false;
    for (int i = 0; i < clients.length; i++) {
      if (clients[i][0]==(client)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
    is_closed=false;
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
    is_closed=true;
    log.deleteFile();
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) throws IOException,InterruptedException
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
    //in case the server is suddenly terminated, deletes logs
    Runtime.getRuntime().addShutdownHook(new Message());
    EchoServer sv = new EchoServer(port);
    log = new Log();
    
    try 
    {
      sv.listen();
      
      ServerConsole chat= new ServerConsole(sv,port);
      chat.accept();
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }

  public void clientConnected(ConnectionToClient client) {
    System.out.println("Client Connected");
  }


  public synchronized void clientException(
    ConnectionToClient client, Throwable exception) {
    System.out.println("Client disconnected");
  }

  public void serverClosed(){
    is_closed=true;
    log.deleteFile();
    sendToAllClients("WARNING - Server has stopped listening for connections.");
  }


  public void setOpen(){
    this.is_closed=false;
  }

  public boolean isClosed(){
    return this.is_closed;
  }

  public Log getLog(){
    return log;
  }

/* Handles private messages in the format of @@@id:message */
  public void sendPrivateMessage(String message){
    sendToAllClients(message);
  }

/* Takes the id and message as args and sends them in the @@@id:msg format */
  public void sendPrivateMessage(String id, String message){
    String msg = "@@@"+id+":"+message;
    sendToAllClients(msg);
  }

  public void sendPrivateMessageFrom(String from, String message){
    String msg = "$"+from+"@"+message;
    sendToAllClients(msg);
  }

  public String getOnlineUsers(){
    String users="";
    for (int i = 0; i < user_index; i++) {
      users += clients[i][1]+System.lineSeparator();
    }
    return users;
  }

/* Sends online users to @id */
  public void sendOnlineUsers(String id){
    String msg = "List of online users: "+System.lineSeparator()+getOnlineUsers();
    sendPrivateMessage(id, msg);
  }
}
//End of EchoServer class
