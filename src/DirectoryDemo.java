import java.io.File;
import java.util.ArrayList;

public class DirectoryDemo {

    public String processPath(String path){
        String name = null;


            String[]separatedValues = path.split(" ");
            String pathFromURL = separatedValues[1];
            System.out.println("The path from URL is : " + pathFromURL);

            name = pathFromURL.substring(1);



        return name;
    }

    public String processHtml(ArrayList<String>links ){
        String htmlFile = "<html>\n" +
                "\t<head>\n" +
                "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "\t</head>\n" +
                "\t<body>\n" ;

        for (String fileLinks:
             links) {
            htmlFile = htmlFile + "\t\t"+fileLinks+"\n";
        }

        htmlFile += "\t</body>\n" +
                "</html>";

        return htmlFile;
    }

    public ArrayList<String> ShowDirectory(String pathName){
        System.out.println("The path name is: "+pathName);
       ArrayList <String> generatedlinks = new ArrayList<>();

        String dirname = pathName;
        File f1 = new File(dirname);

        if (f1.isDirectory()) {
            String s[] = f1.list();
            //generating html links
            for (int i = 0; i < s.length; i++) {
                File f = new File(dirname + "/" + s[i]);
                if (f.isDirectory()) {
                    System.out.println("Inserting this to the list: "+pathName+"/"+s[i]);
                    generatedlinks.add("<p><b><a href=\""+"/"+f1.getPath()+"/"+s[i]+"\">"+s[i]+"</a></b></p>");
                    //System.out.println(s[i] + " is a directory");
                } else {
                    generatedlinks.add("<p><a href=\""+""+s[i]+"\">"+s[i]+"</a></p>");
                    //System.out.println(s[i] + " is a file");
                }
            }


        } else {
            System.out.println(dirname + " is not a directory");
        }


        return generatedlinks;
    }




    public static void main(String args[]) {
    /*String dirname = "root";
    File f1 = new File(dirname);

    if (f1.isDirectory()) {
        System.out.println("Directory of " + dirname);
        String s[] = f1.list();

        for (int i = 0; i < s.length; i++) {
            File f = new File(dirname + "/" + s[i]);
            if (f.isDirectory()) {
                System.out.println(s[i] + " is a directory");
            } else {
                System.out.println(s[i] + " is a file");
            }
        }
    } else {
        System.out.println(dirname + " is not a directory");
    }*/

    DirectoryDemo d = new DirectoryDemo();
    ArrayList<String>lists  = d.ShowDirectory("root\\dir1");
        for (String l:
             lists) {
            System.out.println(l);
        }
        //System.out.println(d.processPath("E:\\ACADEMICS\\3-2\\16-batch\\NW LAB\\OFFLINE 1\\Offline 1\\root"));
        System.out.println(d.processHtml(lists));

        //System.out.println(d.processPath("GET /E:/ACADEMICS/3-2/16-batch/NW%20LAB/OFFLINE%201/Offline%201/root HTTP/1.1"));


}
}
