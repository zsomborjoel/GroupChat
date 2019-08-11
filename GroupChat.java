import java.net.*;
import java.io.*;
import java.util.*;

class GroupChat{

    

    public static void main(String[] args) {
        if(args.length < 2){
            System.out.println("Please provide the following arguments: {ip address} {port number}");
        } else {
            try {
                InetAddress ip = InetAddress.getByName(args[0]);
                int port = Integer.parseInt(args[1]);
                Socket socket = New Socker(ip, port);

            } catch (UnknownHostException e){
                System.out.println(e);
            }
        }

    }
}