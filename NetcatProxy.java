// 
// CSCI 4311 PA2: NetCat Proxy
// Matthew Romig
//
package csci4311.nc;

import java.io.*;
import java.net.*;

public class NetcatProxy {
  public static void main(String argv[]) throws Exception {
    Socket connectionSocket = null;
    int port = Integer.parseInt(argv[0]); 
    int port2 = Integer.parseInt(argv[2]);
    Socket connectionSocket2 = new Socket(argv[1], port2);
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
      BufferedReader inFromClient2 = new BufferedReader(new InputStreamReader(connectionSocket2.getInputStream()));
      
      // Create output stream attached to connection socket
      DataOutputStream outToClient2 = new DataOutputStream(connectionSocket2.getOutputStream());
      
      //set socket timeout to prevent blocking for too long
      connectionSocket.setSoTimeout(100);
      connectionSocket2.setSoTimeout(100);
      
      try { //arbitrary condition - will time out no matter what
        while (((inFromClient.read(reader, 0, 1)) != -1))
        {
          byte toBytes[] = new String(reader).getBytes();
          outToClient2.write(toBytes);
        }
      } catch (SocketTimeoutException e) { //do nothing - socket still used
      }
      
      try { //arbitrary condition - will time out no matter what
        while (((inFromClient2.read(reader, 0, 1)) != -1))
        {
          byte toBytes[] = new String(reader).getBytes();
          outToClient.write(toBytes);
        }
      } catch (SocketTimeoutException e) { //do nothing - socket still used
      }
    }
    } catch (SocketException e) {
      System.exit(0);
    }
  }
}  