/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simpleserver;
import java.io.*;
import java.util.Scanner;
import java.net.*;
import java.util.ArrayList;

/**
 *
 * @author zyz
 */
public class NonBlockingServer {
     static final int port = 1256;
     static ArrayList <DataOutputStream> outputIoClients = new ArrayList<>();
     
     
     
    
    public static void main(String[] args) throws IOException {
       
        Scanner scanner = new Scanner(System.in);
        ServerSocket ss= new ServerSocket(port);
        
       
	String msg;
        
        Thread svrThread = new Thread(new ServerStart(ss));
	svrThread.start();
        
        
        while(true) {
            int clientNumber = outputIoClients.size();
           
            if(clientNumber > 0) {
                System.out.println("You have " + clientNumber + " client, Please say something, enter 'finish' to terminate them");
                msg = scanner.nextLine();
                for(DataOutputStream out : outputIoClients) {
                    out.writeUTF(msg);
                    
                }
                
            }          
            
        }
        

    }
    
    
    static class ServerStart implements Runnable {

        ServerSocket ss;

        ServerStart(ServerSocket s) {
            ss = s;
           
        }

        @Override
        public void run() {
            try {
                startServer();
            } catch (IOException e) {
                // TODO Auto-generated catch block

            }
        }

        private void startServer() throws IOException {
           
            
            while (true) {
                Socket s = ss.accept();
                Thread thread = new Thread(new ServerHandler(s));
		thread.start();
            }

        }

    }
    static class ServerHandler implements Runnable {
	
	Socket s;
	
	ServerHandler(Socket s) {
		this.s = s;
                try {
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
                        outputIoClients.add(out);
                        
                }
                catch (IOException e) {
			// TODO Auto-generated catch block
			
		}
                
	}
	
        @Override
	public void run() {
		System.out.println("Connected!!");
                
        }
    }
    
}



    
