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
  
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI, String loginID) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
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
    	if(message.startsWith("#")) {
    		handleCommand(message);
    	}
    	else {
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
  
  private void handleCommand(String cmd) {
	  if (cmd.equals("#quit")) {
		  quit();
	  }
	  else if (cmd.equals("#logoff")) {
		  try {
			  if(this.isConnected()) {
				  this.closeConnection();
			  }
			  else {
				  clientUI.display("Already logged off");
			  }
		} catch (IOException e) {
			clientUI.display("Exception: \"" + e + "\" while attempting to logoff");
		}
	  }
	  else if (cmd.contains("#sethost")) {
		  if (this.isConnected()) {
			  clientUI.display("You have to be disconnected to set host");
		  }
		  else {
			  cmd = cmd.substring(9);
			  if (cmd.isEmpty()) {
				  clientUI.display("Please input a value to set as host");
			  }
			  else {
				  this.setHost(cmd);
				  clientUI.display("You have set host as: " + cmd);
			  }
		  }
	  }
	  else if (cmd.contains("#setport")) {
		  if (this.isConnected()) {
			  clientUI.display("You have to be disconnected to set host");
		  }
		  else {
			  cmd = cmd.substring(9);
			  if (cmd.isEmpty()) {
				  clientUI.display("Please input a value to set as port");
			  }
			  try {
				  this.setPort(Integer.parseInt(cmd));
				  clientUI.display("You have set host as: " + cmd);
			  } catch (NumberFormatException nf) {
				  clientUI.display("Please input numbers ONLY to set as port");
			  }
		  }
	  }
	  else if (cmd.equals("#login")) {
		  try {
			  if(this.isConnected()) {
				  clientUI.display("Already logged in");
			  }
			  else {
				  this.openConnection();
				  clientUI.display("You have logged in");
			  }
		  } catch (IOException e) {
			  clientUI.display("Exception: \"" + e + "\" while attempting to login");
		  }
	  }
	  else if (cmd.equals("#gethost")) {
		  clientUI.display("Host: " + this.getHost());
	  }
	  else if (cmd.equals("#getport")) {
		  clientUI.display("Port: " + Integer.toString(this.getPort()));
	  }
	  else {
		  clientUI.display("Invalid command");
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
  
	/**
	 * Implementation of the Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  @Override
	protected void connectionClosed() {
	  clientUI.display("The connection has been closed.");
	}

	/**
	 * Implementation of the Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  @Override
	protected void connectionException(Exception exception) {
	  clientUI.display("The server has shut down.");
	  System.exit(0);
	  
	}
  
	/**
	 * Implementation of the Hook method called after a connection has been established. The default
	 * implementation does nothing. It may be overridden by subclasses to do
	 * anything they wish.
	 */
  @Override
	protected void connectionEstablished() {
	  clientUI.display(loginID + " has logged on.");
		try {
			sendToServer("#1login " + loginID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
//End of ChatClient class
