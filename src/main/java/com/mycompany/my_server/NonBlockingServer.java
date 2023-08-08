/**
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.my_server;

/**
 *
 * @author Ziyue Zhou
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
public class NonBlockingServer {
    private static BufferedReader input = null;
   public static void main(String[] args)
         throws Exception {
      InetAddress host = InetAddress.getByName("localhost");
      Selector selector = Selector.open();
      ServerSocketChannel serverSocketChannel =
         ServerSocketChannel.open();
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.bind(new InetSocketAddress(host, 1254));
      serverSocketChannel.register(selector, SelectionKey.
         OP_ACCEPT);
      SelectionKey key = null;
      System.out.println("Server started");
      input = new BufferedReader(new
         InputStreamReader(System.in));
      
      while (true) {
         if (selector.select() <= 0)
            continue;
         Set<SelectionKey> selectedKeys = selector.selectedKeys();
         Iterator<SelectionKey> iterator = selectedKeys.iterator();
         while (iterator.hasNext()) {
            key = (SelectionKey) iterator.next();
            iterator.remove();
            if (key.isAcceptable()) {
               SocketChannel sc = serverSocketChannel.accept();
               sc.configureBlocking(false);
               sc.register(selector,SelectionKey.OP_WRITE);
               System.out.println("Connection Accepted: "
                  + sc.getLocalAddress() + "n");
            }

             if (key.isWritable()) {
                 System.out.print("Type a message to client (type finish to disconnect): ");
                 String msg = input.readLine();

                 SocketChannel sc = (SocketChannel) key.channel();
                 ByteBuffer bb = ByteBuffer.wrap(msg.getBytes());
                 sc.write(bb);
                 if (msg.equalsIgnoreCase("finish")) {
                     sc.close();
                     System.out.println("Connection closed...");
                     System.out.print("Do you want to stop server? (Y/N): ");
                     msg = input.readLine();
                     if (msg.equalsIgnoreCase("Y")) {
                         serverSocketChannel.close();
                         System.out.println("Server stopped, see you nex time");
                         return;
                     }
                     System.out.println(
                             "Server will keep running. "
                             + "Try running another client to "
                             + "re-establish connection");
                 }
             }
         }
      }
   }
}