package ChatRoomP2.ChatRoomP2;

import java.net.*;
import java.io.*;
import java.util.Vector;

public class ChatterServer {
	// When someone calls in, need to make a separate thread to listen to that person
	// Main thread should open phone line "answerThePhone()"
	// Need a ServerListens runnable, wouldn't hurt to make it a class
	// Need a tellOthers() function, that puts information from one to the rest
		// tellOthers() is called by the ServerListeners, this is syncronized
	// Need a private list of all ServerListener instances
	// Has to get nicknames somehow
	
	ServerSocket sock; // Socket to listen to the client
	Vector<ServerListener> clientList; // Vector of ServerListeners, one per client
	
	public static void main( String[] args ) // Port number is inputed on command line
	{
		try{
			String portNumber = args[0];
			new ChatterServer(portNumber);
		}
		catch (Exception e){
			System.err.println("ChatterServer: error = "+e);
		}
	}
	// Where all the magic happens
	public ChatterServer(String s) {
		
		try {
			boolean placeholder = true;
			while (placeholder){
				Integer portNum = Integer.parseInt(s); // Get the port number from command line 
				sock = new ServerSocket(portNum);
				// Pass the socket to the openListening function
				openListening(sock);
			}
			
			
			
			sock.close();
		}
		catch (Exception e){
			System.err.println("ChatterServer: error = "+e);
		}
	}
	
	// Function which activates the server, and starts listening for the clients
	public void openListening( ServerSocket sock) {
		try{
			Socket client = sock.accept(); // Get a connection
			// Pass the connection to the server listener class???
		}
		catch (Exception e){
			System.err.println("ChatterServer: error = "+e);
		}
	}
	
	// Function that sends input from one client to all others
	public void tellOthers(String msg){
		
	}
	
	
	
	// Nickname should be instantiated here
	public class ServerListener implements Runnable{
		
		protected String nickname; // Nickname, got from client class
		
		public ServerListener(Socket s){
			
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	} // END ServerListener class
} // END ChatterServer class
