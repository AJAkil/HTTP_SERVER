import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Date;

public class HTTPServer2 {
    static final int PORT = 6789;

    public static void sendPacketdata(ObjectOutputStream out, String path){
        byte[] bytearray = new byte[1024];
        FileInputStream in = null;
        try {
            File f = new File(path);
            in = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(in);

            int readLength = -1;
            while ((readLength = bis.read(bytearray))>0){
                out.write(bytearray,0,readLength);
            }
            bis.close();
            out.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6789);
        System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

        while (true){
            Socket s = serverSocket.accept();
            Thread t = new ServerThread(s);
            t.start();
        }
    }
}
