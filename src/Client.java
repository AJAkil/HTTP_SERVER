import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",6789);
        Thread t = new ClientThread(socket);
        t.start();
    }
}