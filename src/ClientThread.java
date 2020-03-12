import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;

public class ClientThread extends Thread {
    Socket socket;
    OutputStream out;
    BufferedWriter bw;
    PrintWriter pw;
    String uploadRequest;
    final int sizeOfPacket = 1024;

    ClientThread(Socket socket, String uploadRequest) throws IOException {
        this.socket = socket;
        this.out = socket.getOutputStream();
        this.uploadRequest = uploadRequest;
        pw = new PrintWriter(this.out);
    }

    public void sendPacketdata(OutputStream os, String path) throws IOException {
        byte[] bytearray = new byte[sizeOfPacket];
        FileInputStream in = null;
        try {
            File f = new File(path);
            in = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(in);

            os.flush();
            System.out.println(f.length());

            int readLength = -1;
            while ((readLength = bis.read(bytearray)) > 0) {
                os.write(bytearray, 0, readLength);
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
            try {
                File f = new File(uploadRequest.split(" ")[1]);
                if(f.exists()){

                    String s = uploadRequest+" "+(f.length());
                    out.write(s.getBytes());
                    sendPacketdata(out,uploadRequest.split(" ")[1]);

                }else{
                    out.write("Invalid".getBytes());
                    System.out.println("File name is invalid");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
