package chatapp;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

import javax.swing.*;
public class Client extends JFrame{
	
	Socket socket;
	
	BufferedReader br;
    PrintWriter out;
	//Declare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN,20);
    
    
	public Client() {
		try {
			System.out.println("Sending Request to server");
			socket = new Socket("127.0.0.1", 7771);
			System.out.println("Connection Done..");
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			
			createGUI();
			handleEvents();
			startReading();
			//startWriting();
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void handleEvents() {
		messageInput.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
			
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				//System.out.println("Key Released : "+ e.getKeyCode());
				if(e.getKeyCode()==10) {
					//System.out.println("You have pressed enter button");
					String ContentToSend = messageInput.getText();
					messageArea.append("Me : "+ContentToSend+"\n");
					out.println(ContentToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
			}
		});	
		
	}
	
	private void createGUI() {
		this.setTitle("Client Message[END]");
		this.setSize(400,600);
		this.setLocationRelativeTo(JFrame.EXIT_ON_CLOSE);
		//coding for component
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		messageArea.setEditable(false);
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //set layout of frame
		this.setLayout(new BorderLayout());
		//adding the components to frame
		
		this.add(heading, BorderLayout.NORTH);
		JScrollPane jScrollPane = new JScrollPane(messageArea); 
		this.add(messageArea, BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	private void setLocationRelativeTo(int exitOnClose) {
		// TODO Auto-generated method stub
		
	}
	public void startReading() {
		//thread read kr ke data dega
		Runnable r1 = () -> {
            System.out.println("Reader Started...");
            try {	
			while(true) {
				
					String msg = br.readLine();
					if(msg.equals("exit")) {
						System.out.println("Server has terminated the chat");
						JOptionPane.showMessageDialog(this, "Server has terminated the chat");
						messageInput.setEnabled(false);
						socket.close();
						break;
					}
					//System.out.println("Server : "+msg);
					messageArea.append("Server : "+msg+"\n");
							
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
			while(!socket.isClosed()) {
				
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String content = br.readLine();
					out.println(content);
					out.flush();
					
					if(content.equals("exit")) {
						socket.close();
						break;
					}
				
			}
			System.out.println("Connection closed");
			}catch(Exception e) {
				e.printStackTrace();
			}
		};
		new Thread(r2).start();
	}
	
      public static void main(String[] args) {
    	  System.out.println("this is client");
    	  new Client();
      }
}
