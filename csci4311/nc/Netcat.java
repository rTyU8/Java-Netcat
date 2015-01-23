// 
// CSCI 4311 PA1: NetCat
// Matthew Romig
//
package csci4311.nc;

import java.io.*;
import java.net.*;

public class Netcat {
  public static void main(String argv[]) throws Exception {
    String[] newArgs = new String[argv.length - 1]; 
    switch( argv[0] )
    {
      case "s": System.arraycopy(argv, 1, newArgs, 0, newArgs.length);
        NetcatServer.main(newArgs);
        break;
      case "c": System.arraycopy(argv, 1, newArgs, 0, newArgs.length);
        NetcatClient.main(newArgs);
        break;
      case "e": System.arraycopy(argv, 1, newArgs, 0, newArgs.length);
        NetcatExec.main(newArgs);
        break;
      case "p": System.arraycopy(argv, 1, newArgs, 0, newArgs.length);
        NetcatProxy.main(newArgs);
        break;
      case "m": System.arraycopy(argv, 1, newArgs, 0, newArgs.length);
        NetcatMulticast.main(newArgs);
        break;
      default:
        break;
    }
  }
}
      
      