import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<LocalDateTime> connectedTimes;
    private static final String HANDSHAKE_KEY = "12345";

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        connectedTimes = new ArrayList<>();
    }

    public void serve(int numClients) {
        for (int i = 0; i < numClients; i++) {
            try {
                Socket clientSocket = serverSocket.accept();
                LocalDateTime connectionTime = LocalDateTime.now();
                
                // Handle handshake in a separate thread
                Thread clientThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                            
                            // Perform handshake
                            String handshake = in.readLine();
                            if (handshake == null || !handshake.equals(HANDSHAKE_KEY)) {
                                out.println("couldn't handshake");
                                out.flush();
                                clientSocket.close();
                                return;
                            }
                            
                            // Record connection time after successful handshake
                            synchronized (connectedTimes) {
                                connectedTimes.add(connectionTime);
                            }
                            
                            // Process client request
                            String numberStr = in.readLine();
                            if (numberStr != null) {
                                String response = factorize(numberStr);
                                out.println(response);
                                out.flush();
                            }
                            
                            in.close();
                            out.close();
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                
                clientThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String factorize(String numberStr) {
        try {
            long number = Long.parseLong(numberStr);
            
            // Check if number is too large (greater than Integer.MAX_VALUE)
            if (number > Integer.MAX_VALUE) {
                return "There was an exception on the server";
            }
            
            int count = countFactors((int) number);
            return "The number " + number + " has " + count + " factors";
        } catch (NumberFormatException | ArithmeticException e) {
            return "There was an exception on the server";
        }
    }

    private int countFactors(int n) {
        if (n <= 0) {
            return 0;
        }
        int count = 0;
        for (int i = 1; i <= n; i++) {
            if (n % i == 0) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<LocalDateTime> getConnectedTimes() {
        synchronized (connectedTimes) {
            ArrayList<LocalDateTime> sorted = new ArrayList<>(connectedTimes);
            Collections.sort(sorted);
            return sorted;
        }
    }

    public void disconnect() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

