package ChatRoomP2.ChatRoomP2;

import java.net.*;
import java.io.*;
import java.util.LinkedList;

public class ChatterServer extends Thread{
	// When someone calls in, need to make a separate thread to listen to that person
	// Main thread should open phone line "answerThePhone()"
	// Need a ServerListens runnable, wouldn't hurt to make it a class
	// Need a tellOthers() function, that puts information from one to the rest
		// tellOthers() is called by the ServerListeners, this is syncronized
	// Need a private list of all ServerListener instances
	// Has to get nicknames somehow
	
	ServerSocket sock; // Socket to listen to the client
	LinkedList<ServerListener> clientList = new LinkedList<ServerListener>();
	
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
				System.out.println("Chatter Server Open on Port #: " + portNum);
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
	public synchronized void openListening( ServerSocket sock) {
		try{
			boolean serverOpen = true;
			
			while (serverOpen){
				//System.out.println("test");
				Socket client = sock.accept(); // Get a connection (blocks until client calls)
				
				// Pass the connection to the server listener class
				ServerListener client1 = new ServerListener(client);
				clientList.add(client1);
				client1.start();
				//System.out.println("test");
				//BufferedReader bin = new BufferedReader(new InputStreamReader(client.getInputStream()));
		        //PrintWriter pout = new PrintWriter( client.getOutputStream(), true);
		        //System.out.println("test");
		        //BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		        
		        
			}
			//client.close();
		}
		catch (Exception e){
			System.err.println("ChatterServer: error = "+e);
		}
	}
	
	// Function that sends input from one client to all others
	
	public void tellOthers(String msg, ServerListener skipMe){
		//System.out.println("testOthers");
		for (ServerListener client : clientList) {
			if (client != skipMe){
				client.write(msg);
			}
		}
	}
	
	
	
	
	// Nickname should be instantiated here
	public class ServerListener extends Thread{
		
		protected String nickname; // Nickname, got from client class
		protected Socket thisClient; // Socket for this client, used to write back
		
		public ServerListener(Socket s){
			thisClient = s;
		}
		
		// Write function to send message back to the client
		public void write(String msg){
			try{
				PrintWriter pout = new PrintWriter( thisClient.getOutputStream(), true);
				pout.println( msg );
			}
			catch (Exception e){
				System.err.println("ChatterServer: error = "+e);
			}
		}
		
		
		@Override
		public void run() {
			try{
				//System.out.println("testing");
				BufferedReader bin = new BufferedReader(new InputStreamReader(thisClient.getInputStream()));
				PrintWriter pout = new PrintWriter( thisClient.getOutputStream(), true);
				String msg;
		        while ((msg = bin.readLine()) != null){
		        	//System.out.println(msg);
		        	//System.out.println("testing");
		        	//pout.println(msg);
		        	tellOthers(msg, this);
		        }
			}
			catch (Exception e){
				System.err.println("ChatterServer: error = "+e);
			}
		}
		
	} // END ServerListener class
} // END ChatterServer class
