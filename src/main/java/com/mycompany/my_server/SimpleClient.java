package com.mycompany.simpleserver;



import java.io.*;
import java.net.*;

public class SimpleClient {
	public static void main(String[] args) throws IOException {

		String ip = "127.0.0.1";
		int port = 1261;
		String msg = "";
		Socket cSocket = new Socket(ip, port); // or "localhost" or your IP
		System.out.println("Socket is open now at " + ip + ":" + port + "\n");

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
