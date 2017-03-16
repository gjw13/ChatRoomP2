package ChatRoomP2.ChatRoomP2;

import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.StringTokenizer;

//import ChatRoomP2.ChatRoomP2.ChatterClient.ClientListens;

public class ChatterServer extends Thread{
	// When someone calls in, need to make a separate thread to listen to that person
	// Main thread should open phone line "answerThePhone()"
	// Need a ServerListens runnable, wouldn't hurt to make it a class
	// Need a tellOthers() function, that puts information from one to the rest
		// tellOthers() is called by the ServerListeners, this is synchronized
	// Need a private list of all ServerListener instances
	// Has to get nicknames somehow
	
	ServerSocket sock; // Socket to listen to the client
	LinkedList<ServerListener> clientList = new LinkedList<ServerListener>();
	
	public static void main( String[] args ) // Port number is inputed on command line
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
		}
		catch (Exception e){
			System.err.println("ChatterServer: error = "+e);
		}
	}
	
	
	
	// Function which activates the server, and starts listening for the clients
	public void openListening( ServerSocket sock) {
		try{
			boolean serverOpen = true;
			

			while (serverOpen){
				//System.out.println("test");
				Socket client = sock.accept(); // Get a connection (blocks until client calls)
				
				Thread thread = new Thread(new ServerListener(client));
				thread.start();
				
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
	} // END openListening
	
	
	// Function that sends input from one client to all others
	public synchronized void tellOthers(String msg, ServerListener skipMe){
		
		for (ServerListener client : clientList) {
			
			client.write(msg);
			System.out.println(client);
			
			try{
				Thread.sleep(500);
			}
			catch (Exception e){
				System.err.println("ChatterServer: error = "+e);
			}
				
		}
	} // END tellOthers
	
	
	
	
	// Nickname should be instantiated here
	public class ServerListener extends Thread{
		
		protected String nickname;
		protected Socket thisClient; // Socket for this client, used to write back
		
		public ServerListener(Socket s){
			thisClient = s;
			nickname = "anon";
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
		}
		
		
		@Override
		public void run() {
			try{
				
				
				BufferedReader bin = new BufferedReader(new InputStreamReader(thisClient.getInputStream()));
				
				String msg;
		        while ((msg = bin.readLine()) != null){

		        	StringTokenizer token = new StringTokenizer(msg);
		        	String testIfCommand = token.nextToken();
		        	
		        	
		        	if (testIfCommand.equals("/nick")){
		        		nickname = token.nextToken();
		        		tellOthers(nickname + " has entered the chatroom", this);
		        		
		        		
		        	}
		        	
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
		        	
		        	else{
		        		//tellOthers(msg, this);
		        		
		        		synchronized(this){
			        		for (ServerListener client : clientList) {
			        			
			        			client.write(msg);
			        			//System.out.println(nickname);
			        			
			        			try{
			        				Thread.sleep(500);
			        			}
			        			catch (Exception e){
			        				System.err.println("ChatterServer: error = "+e);
			        			}
			        				
			        		}
			        	}
		        	}
		        	
		        }
		        
			}
			
			catch (Exception e){
				System.err.println("ChatterServer: error = "+e);
			}
			
		}
		
	} // END ServerListener class
} // END ChatterServer class
