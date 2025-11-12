import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String host, int port) {
        try {
            sock = new Socket(host, port);
            out = new PrintWriter(sock.getOutputStream(), true);
            in = new BufferedReader( new InputStreamReader(sock.getInputStream()));

        }
        catch(IOException e){
            System.err.println("Cannot connect to server");
            System.exit(1);
        }
    }

    public boolean handshake() {

        try{
            out.println("12345");
            String reply = in.readLine();
            if("CONNECTED".equals(reply)){
                System.out.println("Handshake sucessful");
                return true;
            }

            else{
                System.out.println("Handshake failed");
                return false;
            }
        }
        catch(IOException e){
            System.err.println("Handshake Failed due to IO Exception");
            return false;
        }

    }

    public String requestFactorization(int num) {

        try{
            out.println(num);
            String response = in.readLine();
            return response;
        }
        catch(IOException e){
            return "Error during communition with server";
        }

    }

    public void close() {
        try{
            in.close();
            out.close();
            sock.close();
        }
        catch(IOException e){
            System.err.println("Error closing connection");
        }

    }

    public static void main(String[] args) {

        Client client = new Client("localhost", 2021);
        if(client.handshake()){
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter a num to factor: ");
            int num = sc.nextInt();

            String result = client.requestFactorization(num);
            System.out.println("Factors: " + result);
            sc.close();
        }

        else{
            System.out.println("Handshake failed. Exiting");
        }

    }
}