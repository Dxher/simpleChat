import java.io.IOException;
import java.util.Scanner;

import common.ChatIF;

public class ServerConsole implements ChatIF 
{
  
	  //Instance variables **********************************************
	  
	  /**
	   * The instance of the client that created this ConsoleChat.
	   */
	  EchoServer server;
	  
	  
	  
	  /**
	   * Scanner to read from the console
	   */
	  Scanner fromConsole; 

	  
	  //Constructors ****************************************************

	  /**
	   * Constructs an instance of the ClientConsole UI.
	   *
	   * @param host The host to connect to.
	   * @param port The port to connect on.
	   */
	  public ServerConsole(int port) 
	  {
		  try {
			server= new EchoServer(port, this);
		    try 
		    {
		      server.listen(); //Start listening for connections
		    } 
		    catch (Exception ex) 
		    {
		      System.out.println("ERROR - Could not listen for clients!");
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}

	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	  }

	  
	  //Instance methods ************************************************
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the client's message handler.
	   */
	  public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        server.handleMessageFromServerUI(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }

	  /**
	   * This method overrides the method in the ChatIF interface.  It
	   * displays a message onto the screen.
	   *
	   * @param message The string to be displayed.
	   */
	  public void display(String message) 
	  {
	    System.out.println(message);
	  }

	  
	  //Class methods ***************************************************
	  
	  /**
	   * This method is responsible for the creation of the Client UI.
	   *
	   * @param args[0] The host to connect to.
	   */
	  public static void main(String[] args) 
	  {
	    int port = 0;
	    
	    try {
		      port = Integer.parseInt(args[0]);
		}
		catch(NumberFormatException ne) {
		      port = 5555;
		}
		catch(ArrayIndexOutOfBoundsException e){
		      port = 5555;
		}
	    
	    ServerConsole chat= new ServerConsole(port);
	    chat.accept();  //Wait for console data
	  }
	}
	//End of ConsoleChat class