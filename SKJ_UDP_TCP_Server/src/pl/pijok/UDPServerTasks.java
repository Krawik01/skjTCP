package pl.pijok;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Random;

public class UDPServerTasks extends Thread {

    private InetAddress address;
    private int port;
    private DatagramSocket socket;

    public UDPServerTasks(String host, int port){
        try {
            address = InetAddress.getByName(host);
            this.port = port;

            socket = new DatagramSocket();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            return;
        }

        doTasks();
    }

    private void doTasks(){
        System.out.println("Starting tasks");
        sendMessage(address, port, socket, String.valueOf(5555));
        //Step 2
        Random random = new Random();
        int sum = 0;
        for(int i = 0; i < 5; i++){
            int a = random.nextInt(500);
            sendMessage(address, port, socket, String.valueOf(a));
            sum += a;
        }

        //Solution to Step 2
        String receivedSum = receiveMessage(socket);
        if(sum != Integer.parseInt(receivedSum)){
            sendMessage(address, port, socket, "Wrong sum! Should be " + sum + " Received (" + receivedSum + ")");
            System.out.println("Wrong sum! Should be" + sum + " Received (" + receivedSum + ")");
            return;
        }

        System.out.println("Passed step 2");
        sendMessage(address, port, socket, String.valueOf("lol"));

        //Step 3
        int naturalNumber = random.nextInt(2500);
        int correctK = 0;
        while(Math.pow(correctK, 4) < naturalNumber){
            correctK++;
        }
        sendMessage(address, port, socket, String.valueOf(naturalNumber));

        //Solution to Step 3
        String receivedK = receiveMessage(socket);
        if (correctK != Integer.parseInt(receivedK)) {
            sendMessage(address, port, socket, "Wrong k! Should be " + correctK + " Received (" + receivedK + ")");
            System.out.println("Wrong k! Should be " + correctK + " Received (" + receivedK + ")");
            return;
        }

        System.out.println("Passed step 3");

        //Step 4
        String generatedText = "";
        for(int i = 0; i < 15; i++){
            generatedText = String.valueOf(+ random.nextInt(9));
        }

        String correctText = generatedText.replace("8", "");

        sendMessage(address, port, socket, generatedText);

        //Solution to step 4
        String receivedChangedText = receiveMessage(socket);
        if(!receivedChangedText.equalsIgnoreCase(correctText)){
            sendMessage(address, port, socket, "Wrong text! Should be " + correctText + " Received (" + receivedChangedText + ")");
            System.out.println("Wrong text! Should be " + correctText + " Received (" + receivedChangedText + ")");
            return;
        }

        System.out.println("Passed step 4");

        //Final flag
        String finalFlag = "Praise the sun \\o/";
        sendMessage(address, port, socket, finalFlag);

    }

    private String receiveMessage(DatagramSocket socket){
        String message = "";
        byte[] buffer = new byte[1024];

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
            message = new String(packet.getData(), 0, packet.getLength());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
    }

    private void sendMessage(InetAddress address, int port, DatagramSocket socket, String message){
        byte[] tempBuff = message.getBytes();
        DatagramPacket packet = new DatagramPacket(tempBuff, tempBuff.length, address,port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
