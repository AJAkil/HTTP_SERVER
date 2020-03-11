import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Test1 {
    static final int PORT = 6789;

    public static String readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return String.valueOf(fileData);
    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverConnect = new ServerSocket(PORT);
        System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

        File file = new File("index.html");
        BufferedInputStream bi = new BufferedInputStream(new FileInputStream(file));

//        FileInputStream fis = new FileInputStream(file);
//        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while(( line = br.readLine()) != null ) {
//            sb.append( line );
//            sb.append( '\n' );
//        }
//
//        String content = sb.toString();

        while(true)
        {
            Socket s = serverConnect.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            //BufferedOutputStream pr = new BufferedOutputStream(s.getOutputStream());
            OutputStream pr = s.getOutputStream();
            String input = in.readLine();
            System.out.println(input);

            // String content = "<html>Hello</html>";
            if(input == null) continue;
            if(input.length() > 0) {
                if(input.startsWith("GET"))
                {

                    pr.write("HTTP/1.1 200 OOK\r\n".getBytes());
                    pr.write("Server: Java HTTP Server: 1.0\r\n".getBytes());
                    pr.write(("Date: " + new Date() + "\r\n").getBytes());
                    //pr.write("Accept-Ranges: bytes\r\n".getBytes());
                    //pr.write("Content-Disposition: attachment\r\n".getBytes());
                    //pr.write(("Content-Type: octate/byte\r\n").getBytes());
                    //pr.write(("Content-Length: " + bi.available() + "\r\n").getBytes());
                    pr.write("\r\n".getBytes());
                    byte[] chunk = new byte[1000];
                    while(bi.available()>0)
                    {
                        int size=bi.read(chunk);
                        if(size==1000)
                        {
                            pr.write(chunk);
                        }
                        else
                        {
                            byte [] chunk2 = new byte[size];
                            for(int i=0;i<size;i++)
                            {
                                chunk2[i] = chunk[i];
                            }
                            pr.write(chunk2);
                            pr.flush();
                        }

                    }

                    pr.flush();
                }

                else
                {

                }
            }

            s.close();
        }

    }

}
