// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @author Anthony Daher 300233710
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
	
	
	final private String key = "loginKey";
	//Instance variables **********************************************
	  
	  /**
	   * The interface type variable.  It allows the implementation of 
	   * the display method in the server.
	   */
	  ChatIF serverUI; 
	  

  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) 
		    throws IOException  
  {
    super(port);
    this.serverUI = serverUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    
    
    String msgStr = (String)msg;
    if (msgStr.startsWith("#1login ")) {
    	String loginID = msgStr.substring(8);
    	client.setInfo(key, loginID);
    	serverUI.display("Message received: #login " + loginID + " from null");
    	serverUI.display(loginID + " has logged on.");
    }
    else if (msgStr.startsWith("#login ")) {
    	serverUI.display("ERROR");
    	try {
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    else {
    	serverUI.display("Message received: " + msg + " from " + client.getInfo(key));
    	this.sendToAllClients(client.getInfo(key) + "> " + msg);
    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    serverUI.display
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
	serverUI.display
      ("Server has stopped listening for connections.");
  }
  
  /**
   * Implementation of the Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  serverUI.display("A new client has connected to the server.");
  }

  /**
   * Implementation of the Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  serverUI.display("Client " + client.getInfo(key) + " has disconnected.");
  }


  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromServerUI(String message)
  {
	    if(message.startsWith("#")) {
			handleCommand(message);
		}
		else {
			serverUI.display(message);
			this.sendToAllClients("SERVER MSG> " + message);
		}
  }
  
  
  private void handleCommand(String cmd) {
	  if (cmd.equals("#quit")) {
		  this.quit();;
	  }
	  else if (cmd.equals("#stop")) {
		  this.stopListening();
	  }
	  else if (cmd.equals("#close")) {
		  try {
			this.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
	  else if (cmd.contains("#setport")) {
		  if (this.isListening()) {
			  serverUI.display("You have to close server to set port");
		  }
		  else {
			  cmd = cmd.substring(9);
			  if (cmd.isEmpty()) {
				  serverUI.display("Please input a value to set as port");
			  }
			  try {
				  this.setPort(Integer.parseInt(cmd));
				  serverUI.display("You have set host as: " + cmd);
			  } catch (NumberFormatException nf) {
				  serverUI.display("Please input numbers ONLY to set as port");
			  }
		  }
	  }
	  else if (cmd.equals("#start")) {
		  try {
			this.listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  else if (cmd.equals("#getport")) {
		  serverUI.display("Port: " + Integer.toString(this.getPort()));
	  }
	  else {
		  serverUI.display("Invalid command");
	  }
  }
  
  /**
   * This method terminates the server.
   */
  public void quit()
  {
    try
    {
      close();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  


}
//End of EchoServer class
