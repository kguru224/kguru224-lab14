import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<LocalDateTime> connectedTimes = new ArrayList<>();

    public Server(int port){
        try {
            serverSock = new ServerSocket(port);
        }
        catch(IOException e){
            System.err.println("Cannot establish server socket");
            System.exit(1);
        }
    }

    public void serve(int numClients){
        int served = 0;
        while(served < numClients){
            try{
                Socket clientSock = serverSock.accept();
                connectedTimes.add(LocalDateTime.now());
                System.out.println("New connection: " + clientSock.getRemoteSocketAddress());
                (new ClientHandler(clientSock)).start();
                served++;
            }
            catch(IOException e){
                System.err.println("Error during accepting a connection");
            }

        }
        
    }

    public ArrayList<LocalDateTime> getConnectedTimes(){
        ArrayList<Integer> factors = new ArrayList<>();
        for(int i = 2; i <= n / i; i++){
            while(n % i == 0){
                factors.add(i);
                n /= i;
            }
        }

        if(n > 1){
            return factors;
        }
    }

    private class ClientHandler extends Thread{
        private Socket sock;

        public ClientHandler(Socket sock){
            this.sock = sock;

        }
    }

    public void run(){
        try{
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            String key = in.readLine();
            if(!"12345".equals(key)){
                out.println("INVALID");
                sock.close();
                return;
            }

            else{
                out.println("CONNECTED");
            }

            String message = in.readLine();
            if(message != null){
                try{
                    int num = Integer.pasrseInt(message);
                    ArrayList<Integer> factors = factorize(num);
                    out.println(factors.toString());
                }
                catch(NumberFormatException e){
                    out.println("Error: Invalid number");
                }

            }

            out.close();
            in.close();
            sock.close();
        }
        catch(IOException e){
            System.err.println("Error in client handler");
        }
    }

    public static void main(String[] args){
        Server server = new Server(2021);
        server.serve(3);
    }
}