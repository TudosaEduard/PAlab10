import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String serverAddress = "127.0.0.1";
    private static final int PORT = 8100;
    private static final Scanner userInput = new Scanner(System.in);

    public static void main (String[] args) {
        String request = " ";
        try(Socket socket = new Socket(serverAddress, PORT)) {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Alege una din urmatoarele comenzi:");
            System.out.println("1.Register(inregistrarea clientului in baza de date)");
            System.out.println("2.Login(logarea clientului in baza de date)");
            System.out.println();

            boolean login = false;

            while (!request.equalsIgnoreCase("exit")) {

                if(login)
                {
                    System.out.println("Alege una din urmatoarele comenzi:");
                    System.out.println("1.Friend(leaga noi prietenii)");
                    System.out.println("2.Send(trimite nesaje prietenilor tai)");
                    System.out.println("3.Read(citeste mesajele primite de la prieteni)");
                    System.out.println("4.Exit(iesire din aplicatie)");
                    login = false;
                }

                // Send the request to the output stream: client → server
                request = userInput.next();
                out.println(request);

                // Get the response from the input stream: server → client
                String response = in.readLine();
                System.out.println(response);

                if(response.equalsIgnoreCase("Login succesful!"))
                    login = true;
            }

            userInput.close();
        } catch (Exception e) {
            System.err.println("No server listening... " + e);
        }
    }
}