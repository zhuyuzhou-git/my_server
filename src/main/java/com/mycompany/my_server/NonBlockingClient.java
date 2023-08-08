/**
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.my_server;

/**
 *
 * @author Ziyue Zhou
 */

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
public class NonBlockingClient {
   
   public static void main(String[] args) throws Exception {
      InetSocketAddress addr = new InetSocketAddress(
         InetAddress.getByName("localhost"), 1254);
      Selector selector = Selector.open();
      SocketChannel sc = SocketChannel.open();
      sc.configureBlocking(false);
      sc.connect(addr);
      sc.register(selector, SelectionKey.OP_CONNECT |
         SelectionKey.OP_READ);
      System.out.println("Connected with server! Hearing from server:");
      
      
      while (true) {
         if (selector.select() > 0) {
            Boolean doneStatus = processReadySet
               (selector.selectedKeys());
            if (doneStatus) {
               break;
            }
         }
      }
      sc.close();
   }
   public static Boolean processReadySet(Set readySet)
         throws Exception {
      SelectionKey key = null;
      Iterator iterator = null;
      iterator = readySet.iterator();
      while (iterator.hasNext()) {
         key = (SelectionKey) iterator.next();
         iterator.remove();
      }
      if (key.isConnectable()) {
         Boolean connected = processConnect(key);
         if (!connected) {
            return true;
         }
      }
       if (key.isReadable()) {
           SocketChannel sc = (SocketChannel) key.channel();
           ByteBuffer bb = ByteBuffer.allocate(1024);
           sc.read(bb);
           String result = new String(bb.array()).trim();
           System.out.println(result);
           if (result.equalsIgnoreCase("finish")) {
               return true;
           }
       }
     
      return false;
   }
   public static Boolean processConnect(SelectionKey key) {
      SocketChannel sc = (SocketChannel) key.channel();
      try {
         while (sc.isConnectionPending()) {
            sc.finishConnect();
         }
      } catch (IOException e) {
         key.cancel();
         e.printStackTrace();
         return false;
      }
      return true;
   }
}
