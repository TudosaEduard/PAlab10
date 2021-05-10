package server;

import client.Client;
import client.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ClientThread implements Runnable {
    private final Socket socket;
    public boolean stopServer;
    public List<Client> clients;
    public List<String> arguments = new ArrayList<>();

    public ClientThread (Socket socket, List<Client> clients , boolean stopServer)
    {
        this.socket = socket;
        this.clients = clients;
        this.stopServer = stopServer;
    }

    @Override
    public void run () {

        String request;
        String response;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            Command commands = new Command(clients);

            boolean login = false;

            // Get the request from the input stream: client → server
            request = in.readLine();
            Client client = null;

            while (!request.equalsIgnoreCase("exit") && !login ) {

                if(request.equalsIgnoreCase("register") && !stopServer){

                    response = "Give your name: ";
                    out.println(response);
                    out.flush();

                    String registerName = in.readLine();
                    commands.register(registerName);
                    response = "Client " + registerName + " has been registered!";

                }
                else {

                    if (request.equalsIgnoreCase("login") && !stopServer){

                        response = "Give your name: ";
                        out.println(response);
                        out.flush();

                        String loginName = in.readLine();
                        login = commands.login(loginName);

                        if (login){
                            response = "Login succesful!";
                            client = commands.getClients(loginName);
                        }
                        else {
                            response = "Incorect name! Please try again!";
                        }
                    }
                    else {
                        if(stopServer)
                            response = "Server has stopped!";
                        else
                            response = "Incorrect command! Please try again!";
                    }
                }

                // Send the response to the oputput stream: server → client
                out.println(response);
                out.flush();

                // Get the request from the input stream: client → server
                request = in.readLine();
            }

            while(!request.equalsIgnoreCase("exit")) {

                if(request.equalsIgnoreCase("friend")) {

                    response = "Please give me the names of your friends: ";
                    out.println(response);
                    out.flush();

                    String friendNames = in.readLine();
                    arguments = Arrays.asList(friendNames.split(" "));

                    boolean friend = commands.friend(arguments, client);

                    if (friend){
                        response = "You have new friends!";
                    }
                    else {
                        response = "All the names are incorect!";
                    }
                }
                else {

                    if (request.equalsIgnoreCase("send")){

                        response = "Give a message for your friends: ";
                        out.println(response);
                        out.flush();

                        String message = in.readLine();
                        for (Client friend: client.friends){
                            friend.mail.add(message);
                        }

                        response = "The message was sent!";

                    }
                    else {

                        if (request.equalsIgnoreCase("read")){

                            StringBuilder message = new StringBuilder();
                            for (String msg: client.mail) {
                                message.insert(0, msg + "; ");
                            }
                            response = message.toString();

                            if(message.isEmpty())
                                response = "No message for reading!";
                        }
                        else{
                            response = "Incorrect command! Please try again!";
                        }
                    }
                }

                // Send the response to the oputput stream: server → client
                out.println(response);
                out.flush();
                request = in.readLine();

            }

            // Send the response to the oputput stream: server → client
            response = "Connection stopped!";
            out.println(response);
            out.flush();

        }
        catch(IOException e){
            System.err.println("Communication error... " + e);
        }

        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("error: " + e);
        }

    }
}
