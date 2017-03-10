package ChatRoomP2.ChatRoomP2;

import java.net.*;
import java.io.*;

public class ChatterClient {
	// Client has to listen to stuff coming from the keyboard, getUserInput()
	
	protected String nickname; // Name of the client
	
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
	}
	// Fun stuff here
	public ChatterClient(String sName, int pNum) {
		
		try {
			nickname = "anon"; // Default nickname
			boolean hasQuit = false;
			//System.out.println("Entered Chatter Client with server " +sName + " and "
			//		+ "port number " + pNum);
			
			
			Socket socket = new Socket(sName, pNum);
			// Setup to read and write to the console/terminal
			InputStream in = socket.getInputStream();
		    BufferedReader bin = new BufferedReader( new InputStreamReader(in) );
		    OutputStream out = socket.getOutputStream();
		    BufferedWriter bout = new BufferedWriter( new OutputStreamWriter( out ) );
		    
		    System.out.println("Welcome to the ChatRoom! Server: " + sName 
		    		+ " Port: " + pNum);
		    
		    // while loop while this user hasn't quit
		    while (!hasQuit){
		    
		    	// If the user types in /nick, activate the setNickname
		    	String input = bin.readLine();
		    	if (input.substring(0, 4).equals("/nick")){
		    		setNickname(input.substring(6));
		    		System.out.println(nickname);
		    	}
		    	// If the user types /dm, send a message to a specific client
		    	// Not sure how to do this...
		    	else if (input.substring(0, 2).equals("/dm")){
		    	
		    	}
		    	// If the user types /quit, stop this client
		    	else if (input.substring(0,4).equals("/quit")){
		    		hasQuit = true;
		    	}
		    	// Otherwise, this is just a normal line of chat
		    	else {
		    		new ClientListens(socket);
		    	}
		    }
			
			
			socket.close();
		}
		catch (Exception e){
			System.err.println("ChatterClient: error = "+e);
		}
		
	}
	
	
	// Class to echo what the server tells you to the screen
	public class ClientListens{
		
		// Takes in a socket, creates a reader and outputs info to the screen
		public ClientListens(Socket s){
			try{
				// Reader to output info to the screen
				InputStream in = s.getInputStream();
				BufferedReader bin = new BufferedReader( new InputStreamReader(in) );
				// read the line from the socket
			    String line;
			    line = bin.readLine();
			    System.out.println(line);
				
			}
			catch (Exception e){
				System.err.println("ChatterClient: error = "+e);
			}
		}
		
	}
	
	// Function which activates when the users types "/nick"
	// This accepts a string from the command line and sets the nickname to the string
	public void setNickname(String n){
		nickname = n;
	}
}
