// 
// CSCI 4311 PA1: NetCat Client
// Matthew Romig
//
package csci4311.nc;

import java.io.*;
import java.net.*;

public class NetcatClient {
  public static void main(String argv[]) throws Exception {
    Socket clientSocket = null;
    int port = 0;
    String address = "localhost";
    char[] reader = new char[1]; //char to be read in
    
    if (argv.length == 1)
      port = Integer.parseInt(argv[0]);
    else if (argv.length == 2)
      port = Integer.parseInt(argv[1]);
    
    // Create client socket with connection to server at port; 
    // Uses DNS implicitly
    if( clientSocket == null) {
      if( argv.length == 2)  // Is server name given on the command line? 
      {
        address = argv[0]; //server name first argument
        clientSocket = new Socket( argv[0], port);
      }
      else 
      {
        address = "localhost";
        clientSocket = new Socket( "localhost", port);
      }
    }
    
    try {
      while (true) {  
        
        // Create (buffered) input stream using standard input    
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader( System.in));
        
        // Create output stream for standard out
        DataOutputStream output = new DataOutputStream(System.out);
        
        // Create output stream attached to socket
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        // Create (buffered) input stream attached to socket
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
        
        //Put whatever's available from input into char array,
        //convert it to byte array, and write it to server
        while (System.in.available() > 0) {
          char[] text = new char[System.in.available()];
          inFromUser.read(text, 0, System.in.available());
          byte toBytes[] = new String(text).getBytes();
          outToServer.write(toBytes);
        }
        
        //set socket timeout to prevent blocking for too long
        clientSocket.setSoTimeout(100);  
        
        try {  //using other read methods won't work
          while (((inFromServer.read(reader, 0, 1)) != -1))
          {
            byte toBytes[] = new String(reader).getBytes();
            output.write(toBytes);
          }
        } catch(SocketTimeoutException e) { //do nothing - socket still used
        }
      }
    } catch (SocketException s) {
      clientSocket.close();
    }
  }
}  