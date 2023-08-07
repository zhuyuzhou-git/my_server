/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.my_server;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MultiProcessServer {
    public static void main(String[] args) {
        // 创建主进程用于接受键盘输入并发送给客户端
        Scanner scanner = new Scanner(System.in);
        
        ArrayList <DataOutputStream> outputIoClients = new ArrayList<>();
        Thread mainThread = new Thread(() -> {
            System.out.println("in main thread");
            while(true){
            try {
                System.out.println("checking active client...");
                int clientNumber = outputIoClients.size();
                if (clientNumber > 0) {
                    System.out.println("You have " + clientNumber + " client, Please say something, enter 'finish' to terminate them");
                    String msg = scanner.nextLine();
                    for (DataOutputStream out : outputIoClients) {
                        out.writeUTF(msg);

                    }
                    System.out.println("You have sent: "+msg+" to clients");
                    if (msg.equalsIgnoreCase("finish")) {
                        for (DataOutputStream out : outputIoClients) {
                            out.close();
                            

                        }
                        outputIoClients.clear();
                        System.out.println("disconnected all clients!");
                        
                    }

                }
                
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }});

        // 启动主进程
        mainThread.start();

        // 创建副进程用于监听端口和处理客户端连接
        Thread serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(1261); // 监听端口 12345

                while (true) {
                    System.out.println("Waiting for new client...");
                    Socket clientSocket = serverSocket.accept(); // 等待客户端连接
                    // 每次有客户端连接，创建一个副线程处理连接
                    Thread clientThread = new Thread(() -> {
                        try {
                           DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                            outputIoClients.add(out);
                            System.out.println("new client in");
                        } 
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    // 启动副线程
                    clientThread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // 启动副进程
        serverThread.start();
    }
}
