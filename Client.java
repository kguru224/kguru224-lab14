import java.io.*;
import java.net.*;
 
public class Client {
    private Socket socket; 
    private PrintWriter out;
    private BufferedReader in;

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public Socket getSocket() {
        return socket;
    }

    public void handshake() {
        out.println("12345");
        out.flush();
    }

    public String request(String number) {
        try {
            out.println(number);
            out.flush();
            return in.readLine();
        } catch (IOException e) {
            return "There was an exception on the server";
        }
    } 

    public void disconnect() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    } 
}

