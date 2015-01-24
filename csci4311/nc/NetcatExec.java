// 
// CSCI 4311 PA2: NetCat Exec
// Matthew Romig
//
package csci4311.nc;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class NetcatExec {
  public static void main(String argv[]) throws Exception {
    Socket connectionSocket = null;
    int port = Integer.parseInt(argv[0]);
    
    // Create welcoming socket using port from argument
    ServerSocket welcomeSocket = new ServerSocket(port, 0);
    
    // Wait for some client to connect and create new socket for connection
    if( connectionSocket == null) {
      connectionSocket = welcomeSocket.accept();
    }
    try {
      // Create (buffered) input stream attached to connection socket
      BufferedReader inFromClient = new BufferedReader(new InputStreamReader( 
                                                           connectionSocket.getInputStream()));
      
      // Create output stream attached to connection socket
      DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
      connectionSocket.setSoTimeout(100);
      
      //first argument to run remotely
      String[] executableWithArgs = argv[1].split("\\s+");
      
      //run program with arguments
      Process cmd = Runtime.getRuntime().exec(executableWithArgs);
      
      final InputStream inStream = cmd.getInputStream(); //comes from client
      OutputStream outStream = cmd.getOutputStream(); //goes to client
      final InputStream errorStream = cmd.getErrorStream();
      
      //thread to run process
      new Thread(new Runnable() {
        public void run() {
          InputStreamReader reader = new InputStreamReader(inStream);
          Scanner scan = new Scanner(reader);
          while (scan.hasNextLine()) {
            /**
             * Convert text to byte array (taking into account newlines),
             * which can then be sent to client.
             */
            byte toBytes[] = scan.nextLine().getBytes(); //output without newline
            byte lineSeparator[] = System.lineSeparator().getBytes(); //newline char(s)
            byte[] bytesWithNewline = new byte[toBytes.length + 2];
            bytesWithNewline[bytesWithNewline.length - 1] = '\0'; //account for Windows newlines
            
            System.arraycopy(toBytes, 0, bytesWithNewline, 0, toBytes.length);
            System.arraycopy(lineSeparator, 0, bytesWithNewline, toBytes.length, lineSeparator.length);
            try { 
              outToClient.write( bytesWithNewline);
            } catch(IOException e) {} 
          }
        }
      }).start();
      
      //error stream thread
      new Thread(new Runnable() {
        public void run() {
          InputStreamReader errorReader = new InputStreamReader(errorStream);
          Scanner eScan = new Scanner(errorReader);
          while (eScan.hasNextLine()) {
            byte toBytes[] = eScan.nextLine().getBytes(); //output without newline
            byte lineSeparator[] = System.lineSeparator().getBytes(); //newline char(s)
            byte[] bytesWithNewline = new byte[toBytes.length + 2];
            bytesWithNewline[bytesWithNewline.length - 1] = '\0'; //account for Windows newlines
            System.arraycopy(toBytes, 0, bytesWithNewline, 0, toBytes.length);
            System.arraycopy(lineSeparator, 0, bytesWithNewline, toBytes.length, lineSeparator.length);
            try { 
              outToClient.write( bytesWithNewline);
            } catch(IOException e) {} 
          }       
        }
      }).start();
      
      char[] reader = new char[1];  //char to be read in
      
      // Take client's requests and send them to process' input stream
      while(true) {
        
        DataOutputStream pWriter = new DataOutputStream(outStream);
        
        try { //arbitrary condition - will time out no matter what
          while (((inFromClient.read(reader, 0, 1)) != -1))
          {
            byte toBytes[] = new String(reader).getBytes();
            pWriter.write(toBytes);
            pWriter.flush();
          }
        } catch (SocketTimeoutException e) { //do nothing - socket still used
        }
      } 
    } catch (SocketException e) {
      connectionSocket.close();
      welcomeSocket.close();
      System.exit(0);
    }
  }
} 