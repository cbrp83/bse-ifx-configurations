import socket
import json
import sys

HOST = "127.0.0.1"
PORT = 5959

print ("#STARTED#")

try:
  sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  print ("socket created. sock: " + str(sock))
except socket.error as error:
    print("Error to create")
    print (error)
    #sys.stderr.write("[ERROR] %s\n" % msg[1])
    sys.exit(1)

try:
  sock.connect((HOST, PORT))

  print ("socket connected to HOST: "+HOST+" PORT: "+str(PORT))
  print ("socket connected. sock: " + str(sock))
except socket.error as error:
    print ("Error to connect")
    print (error)
    #sys.stderr.write("[ERROR] %s\n" % msg[1])
    sys.exit(2)

f = open("json-apic-event-records.json", "r")
msg = f.read()
#msg = {'@message': 'python test message', '@tags': ['python', 'test']}
print ("sending message: " + str(msg))

#sock.sendall(json.dumps(msg).encode())
sock.sendall(msg.encode())
#sock.send('\n'.encode())

print ("end")

sock.close()
sys.exit(0)