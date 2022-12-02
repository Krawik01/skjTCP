package pl.pijok;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

public class Main {

    private static InetAddress PJATKServerAddress = null;
    private static int PJATKServerPort = -1;

    public static void main(String[] args) {


        int port = 2137;
        int flag = 11141;
        byte[] buffer = new byte[1024];

        try {
            DatagramSocket socket = new DatagramSocket(port);

          //  sendMessage(PJATKServerAddress, PJATKServerPort, socket, String.valueOf(flag));

            //Step 1
            sendTCPInitSignal("localhost", 5325, "localhost", 2137);

            receiveMessage(socket);
            //Step 2
            int sum = 0;
            for(int i = 0; i < 5; i++){
                sum += Integer.parseInt(receiveMessage(socket));
            }

            sendMessage(PJATKServerAddress, PJATKServerPort, socket, String.valueOf(sum));

            //Step 3
            int naturalNumber = Integer.parseInt(receiveMessage(socket));
            int k = 0;
            while(Math.pow(k, 4) < naturalNumber){
                k++;
            }

            sendMessage(PJATKServerAddress, PJATKServerPort, socket, String.valueOf(k));

            //Step 4
            String text = receiveMessage(socket);
            sendMessage(PJATKServerAddress, PJATKServerPort, socket, text.replace("8", ""));

            //Final flag
            System.out.println("Final flag: " + receiveMessage(socket));

        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    private static void sendTCPInitSignal(String PJATKServerAddress, int PJATKServerPort, String yourAddress, int yourPort){
        try {
            Socket socket = new Socket(PJATKServerAddress, PJATKServerPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(yourAddress + ":" + yourPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String receiveMessage(DatagramSocket socket){
        String message = "";
        byte[] buffer = new byte[1024];

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
            message = new String(packet.getData(), 0, packet.getLength());

            if(PJATKServerAddress == null){
                PJATKServerAddress = packet.getAddress();
                PJATKServerPort = packet.getPort();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
    }

    private static void sendMessage(InetAddress address, int port, DatagramSocket socket, String message){
        byte[] tempBuff = message.getBytes();
        DatagramPacket packet = new DatagramPacket(tempBuff, tempBuff.length, address,port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
