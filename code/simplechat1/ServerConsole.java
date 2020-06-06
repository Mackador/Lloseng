import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import common.ChatIF;

public class ServerConsole implements ChatIF {

    final public static int DEFAULT_PORT = 5555;

    EchoServer server;

    //Constructors ****************************************************

    /**
     * Constructs an instance of the ClientConsole UI.
     *
     * @param host The host to connect to.
     * @param port The port to connect on.
     */
    public ServerConsole(int port) {
        server = new EchoServer(port);
        try {
            server.listen();
        } catch (IOException exception) {
            System.out.println("Error: Can't setup connection!" +
                " Terminating server.");
            System.exit(1);
        }
    }

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
                server.handleMessageFromServerUI(message);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }

    /**
     * This method overrides the method in the ChatIF interface.  It
     * displays a message onto the screen.
     *
     * @param message The string to be displayed.
     */
    public void display(String message) {
        server.sendToAllClients("SERVER MSG> " + message);
    }

    //Class methods ***************************************************

    /**
     * This method is responsible for the creation of the Server UI.
     *
     * @param args[0] The port to use.
     */
    public static void main(String[] args) {
        int port = 0; //The port number

        try {
            port = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            port = DEFAULT_PORT;
        }

        ServerConsole chat = new ServerConsole(port);
        chat.accept(); //Wait for console data
    }


}