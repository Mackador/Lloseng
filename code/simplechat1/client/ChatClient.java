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
      if (message.charAt(0) == '#') {
        String[] messageInputs = message.split(" ");
        switch (messageInputs[0]) {
          case "#quit":
            quit();
            break;
          case "#logoff":
            closeConnection();
            break;
          case "#sethost":
            if (!isConnected()) {
              setHost(messageInputs[1]);
              break;
            } else {
              clientUI.display("You are already connected!");
              break;
            }
          case "#setport":
            if (!isConnected()) {
              setPort(Integer.parseInt(messageInputs[1]));
              break;
            } else {
              clientUI.display("You are already connected!");
              break;
            }
          case "#login":
            if (!isConnected()) {
              openConnection();
              break;
            } else {
              clientUI.display("You are already connected!");
              break;
            }
          case "#gethost":
            clientUI.display(getHost());
            break;
          case "#getport":
            clientUI.display(String.valueOf(getPort()));
            break;
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
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	protected void connectionClosed() {
    System.out.println("The connection to the server is closed!");
	}

	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
    System.out.println("The server has stopped listening. The client is shutting down.");
    quit();
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
