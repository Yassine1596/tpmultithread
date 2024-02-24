import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Serveur {
    private static final int MAX_CLIENTS = 10;
    private static ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);
    public static void main(String argv[]) {
        int port = 0;
        Scanner keyb = new Scanner(System.in);
        ServerSocket serverSocket = null;
        // Demande à l'utilisateur le numéro de port
        System.out.print("Port d'écoute : ");
        try {
            // Lecture du numéro de port saisi par l'utilisateur
            port = keyb.nextInt();
        } catch (NumberFormatException e) {
            // Gestion des exceptions
            System.err.println("Le paramètre n'est pas un entier.");
            System.err.println("Usage : java ServeurUDP port-serveur");
            System.exit(-1);
        }

        try {
            // Création d'une socket serveur liée au port spécifié
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);

                // Traiter la demande de chaque client
                executor.submit(new ClientHandler(clientSocket));
            }
        } catch (Exception e) {
            // Gestion des exceptions
            System.err.println("Erreur : " + e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            executor.shutdown();
        }
    }
}
