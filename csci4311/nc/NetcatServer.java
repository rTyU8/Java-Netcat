// 
// CSCI 4311 PA1: NetCat Server
// Matthew Romig
//
package csci4311.nc;

import java.io.*;
import java.net.*;

public class NetcatServer {
  public static void main(String argv[]) throws Exception {
    Socket connectionSocket = null;
    int port = Integer.parseInt(argv[0]); 
    char[] reader = new char[1];  //char to be read in
    
    // Create welcoming socket using port from argument
    ServerSocket welcomeSocket = new ServerSocket(port, 0);
    try {
    // While loop to handle arbitrary sequence of clients making requests
    while(true) {
      // Wait for some client to connect and create new socket for connection
      if( connectionSocket == null) {
        connectionSocket = welcomeSocket.accept();
      }
      
      // Create (buffered) input stream attached to connection socket
      BufferedReader inFromClient = new BufferedReader( 
                                                       new InputStreamReader( connectionSocket.getInputStream()));
      
      // Create output stream attached to connection socket
      DataOutputStream outToClient = new DataOutputStream( 
                                                          connectionSocket.getOutputStream());
      
      // Create (buffered) input stream attached to connection socket
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      
      // Create output stream attached to connection socket
      DataOutputStream output = new DataOutputStream(System.out);
      
      //Put whatever's available from input into char array,
      //convert it to byte array, and write it to client
      while (System.in.available() > 0) {
        char[] text = new char[System.in.available()];
        input.read(text, 0, System.in.available());
        byte toBytes[] = new String(text).getBytes();
        outToClient.write(toBytes);
      }
      
      //set socket timeout to prevent blocking for too long
      connectionSocket.setSoTimeout(100);
      
      try { //arbitrary condition - will time out no matter what
        while (((inFromClient.read(reader, 0, 1)) != -1))
        {
          byte toBytes[] = new String(reader).getBytes();
          output.write(toBytes);
        }
      } catch (SocketTimeoutException e) { //do nothing - socket still used
      }
    }
    } catch (SocketException e) {System.exit(0);} 
  }
} 