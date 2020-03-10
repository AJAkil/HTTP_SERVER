import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Date;

public class ServerThread extends Thread {
    Socket socket;
    BufferedReader in;
    PrintWriter pr = null;
    ServerThread(Socket socket) throws IOException {
        this.socket = socket;
    }

    public  void sendPacketdata(ObjectOutputStream out, String path){
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

    @Override
    public void run() {

        DirectoryDemo d = new DirectoryDemo();
        String mimeType = null;
        String status = null;
        String content = null;

            String input = null;
            try {
                this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                input = this.in.readLine();
                System.out.println("The response is: "+input);

                if(input == null) ;
                if(input.length() > 0) {
                    if(input.startsWith("GET"))
                    {
                        String path = d.processPath(input);

                        if(d.doesExist(path)){
                            status = "200 OK";
                            if(d.isDirectory(path)){

                                try {
                                    pr = new PrintWriter(this.socket.getOutputStream());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

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

                                ObjectOutputStream out = null;
                                try {
                                    out = new ObjectOutputStream(this.socket.getOutputStream());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                DataOutputStream ds = new DataOutputStream(out);
                                File f = new File(path);

                                //out.writeObject("HTTP/1.1 "+status+"\r\n");
                                try {
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
                                    File file = new File(path);
                                    byte [] array = Files.readAllBytes(file.toPath());
                                    System.out.println("ekhne");
                                    ds.write(array);
                                    System.out.println("upto here");
                                    out.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                        }else{
                            status = "404 PAGE NOT FOUND";
                            System.out.println("404 PAGE NOT FOUND");
                            try {
                                pr = new PrintWriter(this.socket.getOutputStream());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

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
                    else if(input.startsWith("UPLOAD "))
                    {

                    }
                }
                try {
                    this.in.close();
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    
}
