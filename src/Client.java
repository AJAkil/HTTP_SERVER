import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {

        while (true){
            Scanner scan = new Scanner(System.in);
            String uploadRequest = scan.nextLine();
            Socket socket = new Socket("localhost",6789);
            Thread t = new ClientThread(socket,uploadRequest);
            t.start();
        }

    }
}
