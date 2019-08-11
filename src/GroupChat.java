import java.net.*;
import java.io.*;
import java.util.*;

public class GroupChat{ 
    static String name; 
    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("Please provide the following arguments: {ip address} {port number}");
        } else {
            try {
                //Get params
                InetAddress group = InetAddress.getByName(args[0]);
                int port = Integer.parseInt(args[1]);
                MulticastSocket socket = new MulticastSocket(port);
                
                //Open Scanner for username and messages
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter your name:");
                name = sc.nextLine();

                //Join to the multicast group
                socket.setTimeToLive(0);
                socket.joinGroup(group);

                //Open a thread
                Thread thread = new Thread(new ReadThread(socket, group, port));
                thread.start();

                //Buffer the input message and send via Socket
                System.out.println("Now you can start typing messages.\n");
                while(true){
                    String message = sc.nextLine();
                    message = name + ": " + message;
                    byte[] buffer = message.getBytes();
                    DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port); 
                    socket.send(datagram);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }

    }
}

class ReadThread implements Runnable{
    private MulticastSocket socket;
    private InetAddress group;
    private int port; 
    private static final int MAX_LEN = 1000;
    
    //Constructor
    ReadThread(MulticastSocket socket, InetAddress group, int port){
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run(){
        while(true){
            try{
                //Recive messages
                byte[] buffer = new byte[ReadThread.MAX_LEN];
                DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
                socket.receive(datagram);
                String message = new String(buffer, 0, buffer.length, "UTF-8");
                if(!message.startsWith(GroupChat.name)){
                    System.out.println(message);
                }
            } catch (IOException e){
                System.out.println(e);
            }
        }
        
    }
}