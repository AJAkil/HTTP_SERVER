import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;

public class ClientThread extends Thread {
    Socket socket;
    BufferedOutputStream out;
    final int sizeOfPacket = 1024;

    ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new BufferedOutputStream(this.socket.getOutputStream());
    }

    public void sendPacketdata(BufferedOutputStream bos, String path) throws IOException {
        byte[] bytearray = new byte[sizeOfPacket];
        FileInputStream in = null;
        try {
            File f = new File(path);
            in = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(in);

            int readLength = -1;
            while ((readLength = bis.read(bytearray)) > 0) {
                bos.write(bytearray, 0, readLength);
            }

            //System.out.println("Total sent: "+sum);
            //bos.flush();
            //bis.close();
            //bos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Pathao");
                this.out.write("UPLOAD".getBytes());
                System.out.println("pathai dilam");
                Scanner scan  = new Scanner(System.in);
                //scan.nextLine();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
