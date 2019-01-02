package client;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    private static String host;
    private static int port;
    private static String logfile;
    private static BufferedReader input;
    private static PrintWriter output;

    private static int minOccurrences;

    public static void main(String[] argv) throws IOException {
        getUserInput();

        Socket socket = null;
        try {
            socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));

            output.println(minOccurrences);
            sendFile(logfile);
            output.println();
            output.flush();

            receiveMinedData();

        } catch(UnknownHostException e) {
            System.out.println("Unknown host");
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null) {
                socket.close();
            }
        }
    }

    private static void getUserInput() {
        Scanner userInput = new Scanner(System.in);

        System.out.print("Set host: ");
        host = userInput.nextLine();
        System.out.print("Set port: ");
        port = Integer.parseInt(userInput.nextLine());
        System.out.print("Set log file name: ");
        logfile = userInput.nextLine();
        System.out.print("Set min occurrences: ");
        minOccurrences = Integer.parseInt(userInput.nextLine());
    }

    private static void sendFile(String filename) throws IOException {
        BufferedReader fileStream = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));
        String line;

        while((line = fileStream.readLine()) != null) {
            if(!line.trim().isEmpty()) { output.println(line); }
        }
    }

    private static void receiveMinedData() throws IOException {
        String line = null;
        while(!(line = input.readLine()).isEmpty()) {
            System.out.println(line);
        }
    }
}

