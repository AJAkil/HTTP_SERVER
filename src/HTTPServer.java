import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class HTTPServer {
    static final int PORT = 6789;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6789);
        System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");


        while (true){
            Socket s = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pr = new PrintWriter(s.getOutputStream());
            String input = in.readLine();
            System.out.println("The response is: "+input);

            //to process the response and get the appropriate things to find

            //split the response message by spaces, keep it in an array, out target is the last element of the line starting with GET, if we get the last element we call
            // the directory traversal function with this as our path, then we would get the list of all the files and folders under those directory, if exists, and then
            //we call a function to dynamically generate a HTMl file. We pass in the list a parameter to the function

            if(input == null) continue;
            if(input.length() > 0) {
                if(input.startsWith("GET"))
                {

                }

                else
                {

                }
            }




        }
    }
}
