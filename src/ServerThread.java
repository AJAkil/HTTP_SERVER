import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Date;

public class ServerThread extends Thread {
    Socket socket;
    BufferedInputStream in;
    BufferedOutputStream pr = null;
    ServerThread(Socket socket) throws IOException {
        this.socket = socket;
    }

    public  void sendPacketdata(BufferedOutputStream bos, String path) throws IOException {
        byte[] bytearray = new byte[1024];
        FileInputStream in = null;
        try {
            File f = new File(path);
            in = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(in);

            int readLength = -1;
            while ((readLength = bis.read(bytearray))>0){
                bos.write(bytearray,0,readLength);
            }

            //System.out.println("Total sent: "+sum);
            //bos.flush();
            //bis.close();
            //bos.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public  String ProcessResponse(BufferedInputStream is) throws IOException {
        byte[] byteArray;
        while (is.available() <= 0) ;
        byteArray = new byte[is.available()];
        is.read(byteArray);
        String input = new String(byteArray);
        return input;
    }

    @Override
    public void run() {

        DirectoryDemo d = new DirectoryDemo();
        String mimeType = null;
        String status = null;
        String content = null;

            String input = null;
            try {
                this.in = new BufferedInputStream(this.socket.getInputStream());
                pr = new BufferedOutputStream(this.socket.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                input = ProcessResponse(in);
                System.out.println("The response is: "+input);

                 if(input == null) ;
                else  if( input.length()>0) {
                    if(input.startsWith("GET"))
                    {
                        System.out.println("BEFORE PROCESSING: "+input);
                        String path = d.processPath(input);

                        if(d.doesExist(path)){
                            status = "200 OK";
                            if(d.isDirectory(path)){

                                content = d.processHtml(d.ShowDirectory(path));
                                mimeType = "text/html";

                                //common for now
                                pr.write(("HTTP/1.1 "+status+"\r\n").getBytes());
                                pr.write("Server: Java HTTP Server: 1.0\r\n".getBytes());
                                pr.write(("Date: " + new Date() + "\r\n").getBytes());
                                pr.write(("Content-Type: "+mimeType+"\r\n").getBytes());
                                pr.write(("Content-Length: " + content.length() + "\r\n").getBytes());
                                pr.write("\r\n".getBytes());
                                pr.write(content.getBytes());
                                pr.flush();

                            }else{

                                String extension = d.getExtension(path);
                                mimeType = d.procssMINEType(extension);
                                //System.out.println(extension);
                                //content = d.processHtml(d.ShowDirectory(path.substring(0,path.lastIndexOf('/'))));
                                //System.out.println("Not a folder");

                                File f = new File(path);
                                String fname = d.getFileName(path);
                                pr.write(("HTTP/1.1 "+status+"\r\n").getBytes());
                                pr.write("Server: Java HTTP Server: 1.0\n".getBytes());
                                pr.write(("Date: " + new Date() + "\r\n").getBytes());
                                pr.write(("Content-Type: "+mimeType+"\r\n").getBytes());
                                pr.write(("Content-Length: " + Long.toString(f.length())+ "\r\n").getBytes());
                                pr.write("Content-Disposition: attachment\r\n".getBytes());
                                pr.write(("filename=\""+fname+"\"\r\n").getBytes());
                                pr.write("\r\n".getBytes());
                                //bos.write(("Content-Length: " + Long.toString(f.length())+ "\r\n").getBytes());
                                sendPacketdata(pr,path);
                                pr.flush();

                            }

                        }else{
                            status = "404 PAGE NOT FOUND";
                            System.out.println("404 PAGE NOT FOUND");
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

                            pr.write(("HTTP/1.1 "+status+"\r\n").getBytes());
                            pr.write("Server: Java HTTP Server: 1.0\r\n".getBytes());
                            pr.write(("Date: " + new Date() + "\r\n").getBytes());
                            pr.write(("Content-Type: "+mimeType+"\r\n").getBytes());
                            pr.write(("Content-Length: " + content.length() + "\r\n").getBytes());
                            pr.write("\r\n".getBytes());
                            pr.write(content.getBytes());
                            pr.flush();
                        }
                    }
                    else if(input.startsWith("UPLOAD "))
                    {
                        System.out.println("Ekhane ashtese");

                        String splittedString[] = input.split(" ");

                        byte[] bytearray = new byte[1024];
                        FileOutputStream out = null;
                        try {
                            File f = new File("root/"+splittedString[1]);
                            out = new FileOutputStream(f);
                            BufferedOutputStream bos = new BufferedOutputStream(out);

                            int readLength = -1;
                            int sum = 0;

                            String fileSize = splittedString[2];

                            while ((readLength = in.read(bytearray)) > 0) {
                                bos.write(bytearray, 0, readLength);
                                sum+=readLength;
                                if(sum == Integer.parseInt(fileSize))break;
                            }


                            System.out.println("merging sesh!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else if(input.startsWith("Invalid")){
                        System.out.println("File name is invalid");
                    }
                }
                try {
                    this.in.close();
                    this.socket.close();
                    System.out.println("Connection closed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    
}
