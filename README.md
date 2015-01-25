# Java-Netcat
Implementation of Netcat in Java for Networks

Run as csci4311.nc.Netcat x args <br />
Where x can be <br />
c for client <br />
s for server <br />
e for exec <br />
p for proxy <br />
m for multicast <br />

NetcatServer is a basic server that allows for chatting and file
transfer. <br />
Arguments: port

NetcatClient connects to a server and has the same functionality as the
server. <br />
Arguments: [address] port

NetcatExec is a server which the client sends commands to and receives 
output. This allows a client to run programs remotely. <br />
Arguments: port [command] [args]

NetcatProxy is a proxy server. It takes an address and port number of
a host to be accessed. <br />
Arguments: proxy_port address port

NetcatMulticast is a server which broadcasts messages it 
receives to listening clients. <br />
Arguments: port address1 port1 [[address_n] [port_n]...]
