import java.io.*;
import java.util.Arrays;

import client.*;
import common.*;

public class ServerConsole implements ChatIF {

    EchoServer server;

    public ServerConsole(EchoServer sv ,int port) {
        server = sv;
    }


    public void handleMessageFromServerUI(String message){
        try
        {
          if (message.charAt(0)=='#') {
            /* if (message.equals("#quit")) {
              System.out.println("Quitting..");  
              quit(); */
            //}
            if (message.equals("#stop")) {
                System.out.println("Stopping..");
                server.stopListening();
                server.serverClosed();
            }
            if (message.equals("#quit")) {
                System.out.println("Quitting..");
                System.exit(0);
            }
            if (message.equals("#close")) {
                server.close();
                System.out.println("serv closed");
              }
            if (message.startsWith("#setport")) {
              if (server.isClosed()==true) {
                String[] new_port = message.split(" "); 
                server.setPort(Integer.parseInt(new_port[1]));
                System.out.println("Port changed to "+ server.getPort());
              } else {
                System.out.println("Error, cant change host while connected");
              }
            }
            if (message.startsWith("#start")) {
                if (server.isClosed()==true) {
                  server.listen();
                  server.setOpen();
                } else {
                  System.out.println("Error, server already running");
                }
            }
            if (message.startsWith("#getport")) {
              System.out.println("Port is "+server.getPort());
            } 
          }
        else server.sendToAllClients("SERVER MSG > " +message);
          
        }
        catch(IOException e)
        {
          System.out.println("Could not send message to server.  Terminating client.");
        }
      }

    public void accept() {
        try{
            BufferedReader fromConsole = 
                new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true) {
                message = fromConsole.readLine();
                try {
                    
                    handleMessageFromServerUI(message);
                } catch (Exception ex) {
                    System.out.println("Unexpected error while reading from console! 1");
                }
            }
            } 
            catch (Exception ex) 
            {
            System.out.println("Unexpected error while reading from console! 2");
        }

  }

    public void display(String message) 
    {
        System.out.println("> " + message);
    }


    
}