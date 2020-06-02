// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;
import java.util.Set;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  private String user_id=null;
  private boolean initialized=false;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.initialized=true;
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display( msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      
        if (message.equals("#quit")) {
          System.out.println("Quitting..");  
          quit();
        }
        else if (message.equals("#logoff")) {
          closeConnection();
          System.out.println("Logged off");
        }
        else if (message.startsWith("#sethost")) {
          if (isConnected()) {
            System.out.println("Error, cant change host while connected");
          } else {
            String[] new_host = message.split(" "); 
            setHost(new_host[1]);
            System.out.println("Host change to "+ getHost());
          }
        }
        else if (message.startsWith("#setport")) {
          if (isConnected()) {
            System.out.println("Error, cant change port while connected");
          } else {
            String[] new_port = message.split(" "); 
            setPort(Integer.parseInt(new_port[1]));
            System.out.println("Port change to "+ getPort());
          }
        }
        else if (message.startsWith("#login")){
          if (isConnected()) {
            System.out.println("Error, client is already connected");
          } else {
            openConnection();
          }
        } 
        else if (message.startsWith("#gethost")){
          System.out.println("Host is "+getHost());
        }
        else if (message.startsWith("#getport")) {
          System.out.println("Port is "+getPort());
        }
      
        else{
          sendToServer(this.user_id+"@"+message);
        }
      
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }


public void connectionException(Exception exception) {
  System.out.println("Error, server has shut down");
  System.out.println("Quitting..");
  quit();
}


public String getId(){
  return this.user_id;
}

public void setId(String id){
  this.user_id=id;
  return;
}
}


//End of ChatClient class
