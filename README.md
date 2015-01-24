# Java-Netcat
Implementation of Netcat in Java for Networks

Run as csci4311.nc.Netcat x [args]
Where x can be 
c for client
s for server
e for exec
p for proxy
m for multicast

NetcatServer is a basic server that allows for chatting and file
transfer.
Arguments: [port]

NetcatClient connects to a server and has the same functionality as the
server.
Arguments: [address] [port]

NetcatExec is a server which the client sends commands to and receives 
output. This allows a client to run programs remotely.
Arguments: [port]

NetcatProxy is a proxy server. It takes an address and port number of
a host to be accessed.
Arguments: [proxy_port] [address] [port]

NetcatMulticast is a server which broadcasts messages it 
receives to listening clients.
Arguments: [port] <[address1] [port1] ... [addressn] [portn]>
