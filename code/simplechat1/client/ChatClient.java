// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

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
    clientUI.display(msg.toString());
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
      if (message.charAt(0)=='#') {
        if (message.equals("#quit")) {
          System.out.println("Quitting..");  
          quit();
        }
        if (message.equals("#logoff")) {
          closeConnection();
          System.out.println("Logged off");
        }
        if (message.startsWith("#sethost")) {
          if (isConnected()) {
            System.out.println("Error, cant change host while connected");
          } else {
            String[] new_host = message.split(" "); 
            setHost(new_host[1]);
            System.out.println("Host change to "+ getHost());
          }
        }
        if (message.startsWith("#setport")) {
          if (isConnected()) {
            System.out.println("Error, cant change port while connected");
          } else {
            String[] new_port = message.split(" "); 
            setPort(Integer.parseInt(new_port[1]));
            System.out.println("Port change to "+ getPort());
          }
        }
        if (message.startsWith("#login")){
          if (isConnected()) {
            System.out.println("Error, client is already connected");
          } else {
            openConnection();
          }
        } 
        if (message.startsWith("#gethost")){
          System.out.println("Host is "+getHost());
        }
        if (message.startsWith("#getport")) {
          System.out.println("Port is "+getPort());
        }
      } else {
        sendToServer(message);
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
}
//End of ChatClient class
