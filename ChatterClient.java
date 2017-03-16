package ChatRoomP2.ChatRoomP2;

import java.net.*;
import java.io.*;
import java.util.StringTokenizer;

/*
 * IMPORTANT NOTES/RULES ON THIS CHATROOM:
 * 
 * This chatroom was tested almost exclusively on localhost. That being said, it should also
 * work on a server, just be careful to follow the format for the commands outlined in 
 * the project specification. Always start the server before each of the clients. Messages
 * typed on one client will echo back to the same client. /quit will quit the client
 * side, but NOT the server side, which must be killed manually.
 */



// ChatterClient Class: Accepts keyboard input and sends it to the server
public class ChatterClient {
	
	protected String nickname; // Name of the client
	
	// Main Function: Gets the server name and port number from the command line
	// and passes it to the constructor
	public static void main(String[] args){
		try{
			String serverName = args[0];
			String portString = args[1];
			int portNum = Integer.parseInt(portString);
			new ChatterClient(serverName, portNum);
		}
		catch (Exception e){
			System.err.println("ChatterClient: error = "+e);
		}
		System.exit(0);
	}// END main
	
	
	// ChatterClient Constructor: Does the heavy lifting of accepting user input,
	// and sending it to the server
	public ChatterClient(String sName, int pNum) {
		
		try {
			nickname = "anon"; // Default nickname
			boolean hasQuit = false;
			
			System.out.println("Welcome to the ChatRoom! Server: " + sName 
		    		+ " Port: " + pNum);
			
			// Get the socket info
			Socket socket = new Socket(sName, pNum);
			
			// Setup to read and write to the console/terminal
			InputStream in = socket.getInputStream();
		    BufferedReader bin = new BufferedReader( new InputStreamReader(in) );
		    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		    PrintWriter pout = new PrintWriter( socket.getOutputStream(), true);
		    OutputStream out = socket.getOutputStream();
		    BufferedWriter bout = new BufferedWriter( new OutputStreamWriter( out ) );
		    
		    
		    // while loop while this user hasn't quit
		    while (!hasQuit){
		    	
		    	// Synchronization of Threads to prevent mistimed messages
		    	synchronized(this){
		    		
		    		// Start a ClientListens thread
		    		Runnable runMe = new ClientListens(socket);
		    		Thread thisThread = new Thread(runMe);
		    		thisThread.start();
		    		
		    		// Get input from the line
		    		String input = stdIn.readLine();
		    		
		    		// Send input to the server
		    		pout.println(input);
		    		pout.flush();
		    	
		    		// Split up the input into tokens to handle commands
		    		StringTokenizer token = new StringTokenizer(input);
		    		String argument = token.nextToken();
		        
		    		// If the user types /nick, they are setting their nickname
		    		// Send this information to the server
		    		if (argument.equals("/nick")){

		    			String name = token.nextToken();
		    			setNickname(name);
		    			bout.write("/" +name);

		    		}
		    		// If the user types /dm, send a message to a specific client
		    		// Input in form of /dm UserNickName Hello!
		    		else if (argument.equals("/dm")){
		    			
		    			
		    		}
		    		// If the user types /quit, stop this client
		    		else if (argument.equals("/quit")){
		    			hasQuit = true;
		    		}
		    		// Otherwise, this is just a normal line of chat being entered, 
		    		// which is handled up above
		    		else {
		    			try{
		    				// Do nothing
						}
						catch (Exception e){
							System.err.println("ChatterClient: error = "+e);
						}
		    		}
		    		
		    	}
		    	
		    }
		    pout.close();
			socket.close();
		}
		catch (Exception e){
			System.err.println("ChatterClient: error = "+e);
		}
		
	} // END ChatterClient Constructor
	
	
	// ClientListens Class: Gets output from the server from other clients and 
	// sends it out to the window
	public class ClientListens extends Thread{
		
		protected Socket sock;
		
		// Takes in a socket, creates a reader and outputs info to the screen
		public ClientListens(Socket s){
			sock = s;
		}
		
		// Run function: Handles the output to the screen
		@Override
		public void run() {
			boolean test = true;
			
			while(test){
				
				try{
					// Reader to output info to the screen
					InputStream in = sock.getInputStream();
					BufferedReader bin = new BufferedReader( new InputStreamReader(in) );
					// read the line from the socket and print it to the screen
				    String line;
				    line = bin.readLine();
				    System.out.println(line);
					
				}
				catch (Exception e){
					System.err.println("ChatterClient: error = "+e);
				}
			}
		}
		
	}
	
	// Function which activates when the users types "/nick"
	// This accepts a string from the command line and sets the nickname to the string
	public void setNickname(String n){
		nickname = n;
	}
	
}