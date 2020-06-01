import java.io.*;
import client.*;
import common.*;

public class ServerConsole implements ChatIF {

    EchoServer server;

    public ServerConsole(int port) {

            server = new EchoServer(port);

    }

    public void accept() {
        try{
            BufferedReader fromConsole = 
                new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true) {
                message = fromConsole.readLine();
                try {
                    server.sendToAllClients(message);
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