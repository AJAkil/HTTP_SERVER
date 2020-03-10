import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Date;

public class HTTPServer {
    static final int PORT = 6789;

    public static void sendPacketdata(ObjectOutputStream out, String path){
        byte[] bytearray = new byte[1024];
        FileInputStream in = null;
        try {
            File f = new File(path);
            in = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(in);
            System.out.println(f.length());
            int sum = 0;

            int readLength = -1;
            while ((readLength = bis.read(bytearray))>0){
                sum+=readLength;
                out.write(bytearray,0,readLength);
            }
            System.out.println("Total sent: "+sum);
            bis.close();
            out.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6789);
        System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
        DirectoryDemo d = new DirectoryDemo();
        String mimeType = null;
        String status = null;
        String content = null;


        while (true){
            Socket s = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pr = null;
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
                    String path = d.processPath(input);

                    if(d.doesExist(path)){
                        status = "200 OK";
                        if(d.isDirectory(path)){

                            pr = new PrintWriter(s.getOutputStream());
                            content = d.processHtml(d.ShowDirectory(path));
                            mimeType = "text/html";

                            //common for now
                            pr.write("HTTP/1.1 "+status+"\r\n");
                            pr.write("Server: Java HTTP Server: 1.0\r\n");
                            pr.write("Date: " + new Date() + "\r\n");
                            pr.write("Content-Type: "+mimeType+"\r\n");
                            pr.write("Content-Length: " + content.length() + "\r\n");
                            pr.write("\r\n");
                            pr.write(content);
                            pr.flush();

                        }else{

                            String extension = d.getExtension(path);
                            mimeType = d.procssMINEType(extension);
                            System.out.println(extension);
                            //content = d.processHtml(d.ShowDirectory(path.substring(0,path.lastIndexOf('/'))));
                            System.out.println("Not a folder");

                            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                            DataOutputStream ds = new DataOutputStream(out);
                            File f = new File(path);

                            //out.writeObject("HTTP/1.1 "+status+"\r\n");
                            ds.writeBytes("HTTP/1.1 "+status+"\r\n");
                            //out.writeObject("Server: Java HTTP Server: 1.0\n");
                            ds.writeBytes("Server: Java HTTP Server: 1.0\n");
                            //out.writeObject("Date: " + new Date() + "\r\n");
                            ds.writeBytes("Date: " + new Date() + "\r\n");
                            ds.writeBytes("Content-Type: "+mimeType+"\r\n");
                            //ds.writeBytes("Content-Type: application/x-forcedownload\r\n");
                            //ds.writeBytes("Content-Disposition: attachment\r\n");
                            //ds.writeBytes("filename=\"send.txt\"\r\n");
                            ds.writeBytes("Content-Length: " + Long.toString(f.length())+ "\r\n");
                            ds.writeBytes("\r\n");
                            /*File file = new File(path);
                            byte [] array = Files.readAllBytes(file.toPath());
                            System.out.println("ekhne");
                            ds.write(array);
                            System.out.println("upto here");*/
                            sendPacketdata(out,path);
                            out.close();

                        }

                    }else{
                        status = "404 PAGE NOT FOUND";
                        System.out.println("404 PAGE NOT FOUND");
                        pr = new PrintWriter(s.getOutputStream());
                        content = d.processHtml(d.ShowDirectory(path));
                        mimeType = "text/html";
                        content = "<html>\n" +
                                "\t<head>\n" +
                                "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                                "\t</head>\n" +
                                "\t<body>\n" +
                                "\t\t<h1> 404 PAGE NOT FOUND</h1>\n" +
                                "\t</body>\n" +
                                "</html>";

                        pr.write("HTTP/1.1 "+status+"\r\n");
                        pr.write("Server: Java HTTP Server: 1.0\r\n");
                        pr.write("Date: " + new Date() + "\r\n");
                        pr.write("Content-Type: "+mimeType+"\r\n");
                        pr.write("Content-Length: " + content.length() + "\r\n");
                        pr.write("\r\n");
                        pr.write(content);
                        pr.flush();

                    }





                }

                else
                {

                }
            }



            s.close();
        }
    }
}
