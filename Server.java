package chatapp;

import java.net.*;
import java.io.*;
public class Server {
	
	ServerSocket server;
	Socket socket;
	
    BufferedReader br;
    PrintWriter out;
	
	public Server() {
		try {
			server = new ServerSocket(7771);
			System.out.println("Server is ready to accept connection...");
			System.out.println("Waiting....");
			socket = server.accept();
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			
			startReading();
			startWriting();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startReading() {
		//thread read kr ke data dega
		Runnable r1 = () -> {
            System.out.println("Reader Started...");
            try {	
			while(!socket.isClosed()) {
				
					String msg = br.readLine();
					if(msg.equals("exit")) {
						System.out.println("Client has terminated the chat");
						socket.close();
						break;
					}
					System.out.println("Client : "+msg);
					
				
				
			}
            } catch (IOException e) {
				//e.printStackTrace();
            	System.out.println("Connection closed");
			}
		};
		new Thread(r1).start();
	}
	
	public void startWriting() {
		//thread - data user lega and send kar ke client tak dega
		Runnable r2 = () -> {
			System.out.println("Writer Started...");
			try {
			while(true) {
				
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String content = br.readLine();
					out.println(content);
					out.flush();
					
					if(content.equals("exit")) {
						socket.close();
						break;
					}
				
			}
			}catch(Exception e) {
				//e.printStackTrace();
				System.out.println("Connection closed");
			}
			//System.out.println("Connection closed");
		};
		new Thread(r2).start();
	}
	
	
	public static void main(String[] args) {
		System.out.println("this is srver... going to start server");
        new Server();

	}
}
