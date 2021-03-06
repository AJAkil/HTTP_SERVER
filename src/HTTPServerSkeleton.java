import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class HTTPServerSkeleton {
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
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while(( line = br.readLine()) != null ) {
            sb.append( line );
            sb.append( '\n' );
        }

        String content = sb.toString();
        DirectoryDemo d = new DirectoryDemo();
        String mimeType = null;
        String status = null;
        while(true)
        {
            Socket s = serverConnect.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pr = new PrintWriter(s.getOutputStream());
            String input = in.readLine();
            System.out.println("The response is: "+input);

            if(input != null){
                //System.out.println(d.processPath(input));
                String path = d.processPath(input);

                if(d.doesExist(path)){
                    status = "200 OK";
                    if(d.isDirectory(path)){
                        content = d.processHtml(d.ShowDirectory(path));
                        mimeType = "text/html";
                    }else{

                        String extension = d.getExtension(path);
                        mimeType = d.procssMINEType(extension);
                        System.out.println(extension);
                        content = d.processHtml(d.ShowDirectory(path.substring(0,path.lastIndexOf('/'))));
                        System.out.println("Not a folder");
                    }

                }else{
                    status = "404 NOT FOUND";
                }


            }



            //content = "<html><a href=\"dire,root\">link text</a></html>";
            if(input == null) continue;
            if(input.length() > 0) {
                if(input.startsWith("GET"))
                {
                    pr.write("HTTP/1.1 "+status+"\r\n");
                    pr.write("Server: Java HTTP Server: 1.0\r\n");
                    pr.write("Date: " + new Date() + "\r\n");
                    pr.write("Content-Type: "+mimeType+"\r\n");
                    pr.write("Content-Length: " + content.length() + "\r\n");
                    pr.write("\r\n");
                    pr.write(content);
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
