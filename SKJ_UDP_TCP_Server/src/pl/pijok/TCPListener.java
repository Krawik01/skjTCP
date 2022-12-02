package pl.pijok;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPListener extends Thread {

    private ServerSocket socket;
    private Socket clientSocket;

    public TCPListener(int port){
        try {
            this.socket = new ServerSocket(port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Server tcp starting");
        try {
            this.clientSocket = socket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while((inputLine = in.readLine()) != null){
                String[] parts = inputLine.split(":");
                new UDPServerTasks(parts[0], Integer.parseInt(parts[1])).start();
            }
        } catch (IOException e) {
            System.out.println("Client disconnected!");
        }
    }
}
