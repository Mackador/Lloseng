// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF {
    // Class variables *************************************************

    /**
     * The default port to connect on.
     */
    final public static int DEFAULT_PORT = 5555;

    // Instance variables **********************************************

    /**
     * The instance of the client that created this ConsoleChat.
     */
    ChatClient client;

    // Constructors ****************************************************

    /**
     * Constructs an instance of the ClientConsole UI.
     *
     * @param host The host to connect to.
     * @param port The port to connect on.
     */
    public ClientConsole(String login_id, String host, int port) {
        try {
            client = new ChatClient(login_id, host, port, this);
        } catch (IOException exception) {
            System.out.println("Cannot open connection.  Awaiting command.");
        }
    }


    //Instance methods ************************************************

    /**
     * This method waits for input from the console.  Once it is 
     * received, it sends it to the client's message handler.
     */
    public void accept() {
        try {
            BufferedReader fromConsole =
                new BufferedReader(new InputStreamReader(System.in));
            String message;
            while (true) {
                message = fromConsole.readLine();
                client.handleMessageFromClientUI(message);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }
/*
    public void handleConstructorError(String login_id) {
        try {
            String host;
            int port;
            BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
            String message;
            while(true) {
                message = fromConsole.readLine();
                handleConstructorErrorFromClient(message);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }

    public void handleConstructorErrorFromClient(String message) {
        if (message.charAt(0) == '#') {
            String[] messageInputs = message.split(" ");
            switch (messageInputs[0]) {
                case "#sethost":
                    host = messageInputs[1];
                    break;
                case "#setport":
                    port = (Integer.parseInt(messageInputs[1]));
                    break;
            }
        } else {
            System.out.println("You are not connected to any servers.");
        }
    }
*/
    /**
     * This method overrides the method in the ChatIF interface.  It
     * displays a message onto the screen.
     *
     * @param message The string to be displayed.
     */
    public void display(String message) {
        System.out.println("> " + message);
    }


    //Class methods ***************************************************

    /**
     * This method is responsible for the creation of the Client UI.
     *
     * @param args[0] The host to connect to.
     */
    public static void main(String[] args) {
        String login_id = "";
        String host = "";
        int port = 0; //The port number

        try {
            login_id = "Mack";
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR - No login ID specified.  Connection aborted.");
            System.exit(1);
        }

        try {
            host = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            host = "localhost";
        }

        try {
            port = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            port = DEFAULT_PORT;
        }

        ClientConsole chat = new ClientConsole(login_id, host, port);
        chat.accept(); //Wait for console data
    }
}
//End of ConsoleChat class