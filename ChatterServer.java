package ChatRoomP2.ChatRoomP2;

import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.StringTokenizer;


// ChatterServer Class: Instantiates a server thread and handles client input
public class ChatterServer extends Thread{
	
	// Socket to listen to the client
	ServerSocket sock; 
	// Linked List of ServerListeners, one per client
	LinkedList<ServerListener> clientList = new LinkedList<ServerListener>();
	
	// Main: gets the port number and starts the server
	public static void main( String[] args ) 
	{
		try{
			
			String portNumber = args[0];
			ChatterServer server = new ChatterServer(portNumber);
			server.start();
		}
		catch (Exception e){
			System.err.println("ChatterServer: error = "+e);
		}
	}
	
	
	// ChatterServer Constructor: Opens the server with a server socket and passes to 
	// openListening
	public ChatterServer(String s) {
		
		try {
			boolean placeholder = true;
			while (placeholder){
				Integer portNum = Integer.parseInt(s); // Get the port number from command line 
				sock = new ServerSocket(portNum);
				System.out.println("Chatter Server Open on Port #: " + portNum);
				
				// Pass the socket to the openListening function
				openListening(sock);
			}
		}
		catch (Exception e){
			System.err.println("ChatterServer: error = "+e);
		}
	}
	
	
	
	// openListening: Accepts the ServerSocket, and opens a client socket and instantiates
	// a ServerListener for each client that calls in.
	public void openListening( ServerSocket sock) {
		try{
			boolean serverOpen = true;
			
			while (serverOpen){
				
				// Get a connection (blocks until client calls)
				Socket client = sock.accept(); 
				
				// Start a new thread for this client
				Thread thread = new Thread(new ServerListener(client));
				thread.start();
				
				// Pass the connection to the server listener class
				ServerListener client1 = new ServerListener(client);
				clientList.add(client1);
				client1.start();
			}
		}
		catch (Exception e){
			System.err.println("ChatterServer: error = "+e);
		}
	} // END openListening
	
	
	// tellOthers: Function to tell all the ServerListeners what one of them says,
	// this does echo back what each client says to itself
	public synchronized void tellOthers(String msg, ServerListener skipMe){
		
		// For each loop to traverse the linked list
		for (ServerListener client : clientList) {
			
			// Send the message to the ServerListener write function
			client.write(msg);
			
			// Sleep for aesthetic purposes
			try{
				Thread.sleep(500);
			}
			catch (Exception e){
				System.err.println("ChatterServer: error = "+e);
			}
				
		}
	} // END tellOthers
	
	
	
	
	// ServerListener Class: There is one ServerListener for each client that calls
	public class ServerListener extends Thread{
		
		protected String nickname; // Nickname of the client
		protected Socket thisClient; // Socket for this client, used to write back
		protected int localID; // Local ID: unique for each thread
		
		public ServerListener(Socket s){
			thisClient = s;
			localID = 0;
		}
		
		// Write function to send message back to the client
		public void write(String msg){
			try{
				PrintWriter pout = new PrintWriter( thisClient.getOutputStream(), true);
				pout.println( nickname + ": " + msg );
				pout.flush();
			}
			catch (Exception e){
				System.err.println("ChatterServer: error = "+e);
			}
		} // END write function
		
		// Run function: Accepts input from the client, determines if it's a command 
		// or a normal line of chat, and performs the appropriate action
		@Override
		public void run() {
			try{
				
				// Set the value of nickname: default is anon
				nickname = "anon";
				
				// Get a buffered reader to get input from the client
				BufferedReader bin = new BufferedReader(new InputStreamReader(thisClient.getInputStream()));
				
				String msg;
		        while ((msg = bin.readLine()) != null){
		        	
		        	// Split the message up to test if it's a command
		        	StringTokenizer token = new StringTokenizer(msg);
		        	String testIfCommand = token.nextToken();
		        	
		        	// See if this is a nickname command
		        	if (testIfCommand.equals("/nick")){
		        		nickname = token.nextToken();
		        		tellOthers(nickname + " has entered the chatroom", this);
		        		
		        		
		        	}
		        	// See if this is a direct message command
		        	else if (testIfCommand.equals("/dm")){
		        		
		        		String otherPerson = token.nextToken();
		    			String messageToOther = token.nextToken();
		    			
		    			for (ServerListener client : clientList){
		    		
		    				if (client.nickname.equals(otherPerson)){
		    					
		    					System.out.println("Entered If");
		    					client.write(messageToOther);
		    				}
		    			}
		        	}
		        	// Send the basic message to all others
		        	else{
		        		tellOthers(msg, this);
		        	}
		        	
		        }
		        
			}
			
			catch (Exception e){
				System.err.println("ChatterServer: error = "+e);
			}
			
		} // END run function
		
	} // END ServerListener class
} // END ChatterServer class
