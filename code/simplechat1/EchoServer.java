// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
    // Class variables *************************************************

    /**
     * The default port to listen on.
     */
    final public static int DEFAULT_PORT = 5555;

    // Constructors ****************************************************

    /**
     * Constructs an instance of the echo server.
     *
     * @param port The port number to connect on.
     */
    public EchoServer(int port) {
        super(port);
    }

    // Instance methods ************************************************

    /**
     * This method handles any messages received from the client.
     *
     * @param msg    The message received from the client.
     * @param client The connection from which the message originated.
     */
    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        String[] message = msg.toString().split(" ");
        if (message[0].equals("#login")) {
            if (client.getInfo("username") == null) {
                System.out.println(message[1] + " has logged in.");
                client.setInfo("login", message[1]);
            } else {
                try {
                    client.sendToClient("You have already set your login ID!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (client.getInfo("username") == null) {
                try {
                    client.sendToClient("You must set a login ID first!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Message received: " + msg + " from " + client.getInfo("login"));
                this.sendToAllClients(client.getInfo("login") + ": " + msg);
            }
        }
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server starts listening for connections.
     */
    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server stops listening for connections.
     */
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
    }

    /**
     * Hook method called each time a new client connection is
     * accepted. The default implementation does nothing.
     * @param client the connection connected to the client.
     */
    protected void clientConnected(ConnectionToClient client) {
        System.out.println("Welcome to the server, " + client.getName() + "!");
    }

    /**
     * Hook method called each time a client disconnects.
     * The default implementation does nothing. The method
     * may be overridden by subclasses but should remains synchronized.
     *
     * @param client the connection with the client.
     */
    synchronized protected void clientDisconnected(ConnectionToClient client) {
        System.out.println(client.getName() + " has disconnected from the server!");
    }

    public void handleMessageFromServerUI(String message) {
        try {
            if (message.charAt(0) == '#') {
                String[] messageInputs = message.split(" ");
                switch (messageInputs[0]) {
                    case "#quit":
                        close();
                        System.out.println("The server has been closed.");
                        break;
                    case "#stop":
                        stopListening();
                        break;
                    case "#close":
                        stopListening();
                        close();
                        System.out.println("The server has been closed.");
                        break;
                    case "#setport":
                        if (!isListening()) {
                            setPort(Integer.parseInt(messageInputs[1]));
                            System.out.println("You have changed the port to " + messageInputs[1]);
                            break;
                        } else {
                            System.out.println("You are already connected!");
                            break;
                        }
                    case "#start":
                        if (!isListening()) {
                            listen();
                            break;
                        } else {
                            System.out.println("The server has already started!");
                            break;
                        }
                    case "#getport":
                        System.out.println("Port: " + String.valueOf(getPort()));
                        break;
                }
            } else {
                System.out.println("SERVER MSG> " + message);
                sendToAllClients("SERVER MSG> " + message);
            }
        } catch (IOException e) {
            System.out.println("Could not send message to server.  Terminating client.");
        }
    }

    //Class methods ***************************************************

    /**
     * This method is responsible for the creation of 
     * the server instance (there is no UI in this phase).
     *
     * @param args[0] The port number to listen on.  Defaults to 5555 
     *          if no argument is entered.
     */
    public static void main(String[] args) {
        int port = 0; //Port to listen on

        try {
            port = Integer.parseInt(args[0]); //Get port from command line
        } catch (Throwable t) {
            port = DEFAULT_PORT; //Set port to 5555
        }

        EchoServer sv = new EchoServer(port);

        try {
            sv.listen(); //Start listening for connections
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }

}
//End of EchoServer class