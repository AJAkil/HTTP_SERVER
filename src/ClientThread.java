import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread{
    Socket socket;
    ObjectOutputStream out;
    final int sizeOfPacket = 1024;
    ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
    }


    public void sendPacketData(ObjectOutputStream out, String path){

    }


    @Override
    public void run() {
        super.run();
    }
}
