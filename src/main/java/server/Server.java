package server;

import client.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Server {

    private static final int PORT = 8100;
    private static final boolean running = true;
    private static final Scanner serverInput = new Scanner(System.in);
    public List<Client> clients = new LinkedList<>();
    public boolean stop = false;

    public Server() throws IOException{

        //Some clients for testing friend
        Client client1 = new Client("Eduard");
        clients.add(client1);
        Client client2 = new Client("Bogdan");
        clients.add(client2);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setReuseAddress(true);
            while (running) {

                System.out.println("Doresti sa opresti serverul?(da sau alt mesaj = nu)");
                String stopServer = serverInput.next();

                stop = stopServer.equalsIgnoreCase("da");

                System.out.println("Waiting for a client ...");
                Socket socket = serverSocket.accept();
                System.out.println("Conection accepted!");

                ClientThread clientThread = new ClientThread(socket, clients , stop);
                new Thread(clientThread).start();
            }
        } catch (IOException e) {
            System.err.println("error..." + e);
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
    }
}
