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
            if (message.equals("#save")) {
              Log.saveLog();
          }
            if (message.equals("#stop")) {
                System.out.println("Stopping..");
                server.stopListening();
                server.serverClosed();
            }
            if (message.equals("#quit")) {
                quit();
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
            if (message.startsWith("#list")) {
              System.out.println("List of online users: "+System.lineSeparator()+server.getOnlineUsers());
            } 
            
          }
        else{
          if (message.startsWith("@@@")) {
            /* String[] tmp = message.split(":");
            String id = tmp[0].substring(3);
            String msg = tmp[1]; */
            server.sendPrivateMessage(message);
          } else {
             String tmp_msg = "SERVER MSG > " +message;
             server.sendToAllClients(tmp_msg);    
             Log.writeLog(tmp_msg);
          }
          
        }
          
        }
        catch(IOException e)
        {
          System.out.println("Could not send message to server.  Terminating client.");
          quit();
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

    public void quit(){
      System.out.println("Quitting..");
      server.getLog().deleteFile();
      System.exit(0);
    }

    public void display(String message)
    {
        String tmp_msg=">x " + message;
        System.out.println(tmp_msg);
        //Log.writeLog(tmp_msg);
    }

    
}