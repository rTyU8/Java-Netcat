// 
// CSCI 4311 PA2: NetCat Multicast
// Matthew Romig
//
package csci4311.nc;

import java.io.*;
import java.net.*;

public class NetcatMulticast {
  public static void main(String argv[]) throws Exception {
    Socket connectionSocket = null;
    int port = Integer.parseInt(argv[0]); 
    Socket[] sockets = new Socket[(argv.length - 1)/2];
    DataOutputStream[] writers = new DataOutputStream[(argv.length - 1)/2];
    
    int j = 0;
    for (int i = 1; i < argv.length; i+=2)
    {
      sockets[j] = new Socket(argv[i], Integer.parseInt(argv[i + 1]));
      writers[j] = new DataOutputStream(sockets[j].getOutputStream());
      j++;
    }
    char[] reader = new char[1];  //char to be read in
    
    // Create welcoming socket using port from argument
    ServerSocket welcomeSocket = new ServerSocket(port, 0);
    try{
    // While loop to handle arbitrary sequence of clients making requests
    while(true) {
      // Wait for some client to connect and create new socket for connection
      if( connectionSocket == null) {
        connectionSocket = welcomeSocket.accept();
      }
      
      // Create (buffered) input stream attached to connection socket
      BufferedReader inFromClient = new BufferedReader(new InputStreamReader( 
                                            connectionSocket.getInputStream()));
      
      // Create output stream attached to connection socket
      DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
      
      //set socket timeout to prevent blocking for too long
      connectionSocket.setSoTimeout(100);
      
      try { //arbitrary condition - will time out no matter what
        while (((inFromClient.read(reader, 0, 1)) != -1))
        {
          byte toBytes[] = new String(reader).getBytes();
          for (int i = 0; i < writers.length; i++)
            writers[i].write(toBytes);
        }
      } catch (SocketTimeoutException e) { //do nothing - socket still used
      }
    }
    } catch (SocketException e) {
      System.exit(0);
    }
  }
}  