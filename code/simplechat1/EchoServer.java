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
  //Class variables *************************************************
  private boolean is_closed=false;
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  private int max_users = 10;
  Object[][] clients = new Object[max_users][2];
  int user_index = 0;
  
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
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client){
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
        System.out.println(id+" > "+message);
        this.sendToAllClients(id+" > "+message);
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
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
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
	
    EchoServer sv = new EchoServer(port);
    
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
    sendToAllClients("WARNING - Server has stopped listening for connections.");
  }


  public void setOpen(){
    this.is_closed=false;
  }

  public boolean isClosed(){
    return this.is_closed;
  }

  
}
//End of EchoServer class
