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
 * typed on one client will NOT echo back to the same client. /quit will quit the client
 * side, but NOT the server side, which must be killed manually.
 * 
 * 
 */




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
			
			System.out.println("Welcome to the ChatRoom! Server: " + sName 
		    		+ " Port: " + pNum);
			
			
			Socket socket = new Socket(sName, pNum);
			// Setup to read and write to the console/terminal
			InputStream in = socket.getInputStream();
		    BufferedReader bin = new BufferedReader( new InputStreamReader(in) );
		    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		    PrintWriter pout = new PrintWriter( socket.getOutputStream(), true);
		    OutputStream out = socket.getOutputStream();
		    BufferedWriter bout = new BufferedWriter( new OutputStreamWriter( out ) );
		    //new ClientListens(socket);
		    //System.out.println("Test");
		    
		    
		    // while loop while this user hasn't quit
		    while (!hasQuit){
		    	
		    	
		    	
		    	synchronized(this){
		    		
		    		Runnable runMe = new ClientListens(socket);
		    		Thread thisThread = new Thread(runMe);
		    		thisThread.start();
		    		
		    		String input = stdIn.readLine();
		    	
		    		pout.println(input);
		    		pout.flush();
		    	
		        //System.out.println("echo: " + bin.readLine());
		        
		    		//Runnable runMe = new ClientListens(socket);
		    		//Thread thisThread = new Thread(runMe);
		    		//thisThread.start();
		    	
		    		StringTokenizer token = new StringTokenizer(input);
		    		String argument = token.nextToken();
		        
		    		if (argument.equals("/nick")){

		    			String name = token.nextToken();
		    			setNickname(name);
		    			bout.write("/" +name);

		    		}
		    		// If the user types /dm, send a message to a specific client
		    		// Input in form of /dm UserNickName Hello!
		    		else if (argument.equals("/dm")){
		    			String name = token.nextToken();
		    			String msg = token.nextToken();
		    			
		    			
		    		}
		    		// If the user types /quit, stop this client
		    		else if (argument.equals("/quit")){
		    			hasQuit = true;
		    		}
		    		// Otherwise, this is just a normal line of chat being entered
		    		else {
		    			try{
		    			// Output the line of chat through the socket to the server
				    		//System.out.println("TESTING");
				    		//Runnable runMe = new ClientListens(socket);
				    		//Thread thisThread = new Thread(runMe);
				    		//thisThread.start();
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
		
	}
	
	
	// Class to echo what the server tells you to the screen
	public class ClientListens extends Thread{
		
		protected Socket sock;
		
		// Takes in a socket, creates a reader and outputs info to the screen
		public ClientListens(Socket s){
			sock = s;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
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