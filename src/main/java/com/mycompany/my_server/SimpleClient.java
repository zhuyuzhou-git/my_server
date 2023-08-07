package com.mycompany.my_server;



import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SimpleClient {
	public static void main(String[] args) throws IOException {

		String ip = "127.0.0.1";
		int port = 1261;
		String msg = "";
                
                
                
                Scanner scanner = new Scanner(System.in);
		Socket cSocket = new Socket(ip, port); // or "localhost" or your IP
		System.out.println("Socket is open now at " + ip + ":" + port + "\n");
               
               Thread clientThread = new Thread(() -> {
                try {
                    DataOutputStream out = new DataOutputStream(cSocket.getOutputStream());
                    while (true) {
                        System.out.println("you can send msg to others:");
                        String inputmsg = scanner.nextLine();
                        out.writeUTF(inputmsg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

                    // 启动副线程
                clientThread.start();

		DataInputStream inputFromServer = new DataInputStream(cSocket.getInputStream());
		System.out.println("Waiting for data from the server:");
		while (!msg.equalsIgnoreCase("finish")) {
			msg = inputFromServer.readUTF();

			System.out.println(msg);

		}

		inputFromServer.close();
		cSocket.close();
		System.out.println("Socket is closed at " + ip + ":" + port + "\n");

	}
}
