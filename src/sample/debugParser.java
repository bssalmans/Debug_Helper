package sample;
import javafx.scene.control.Alert;

import java.io.*;
/**
 * Created by bssalmans on 4/18/2016.
 */
public class debugParser
{
    static String fin = "FINISHED";
    static String ent = "ENTRY";
    static String bgin = "BEGIN";
    static String strt = "STARTED";
    static String ext = "EXIT";
    static String end = "END";
    static String var = "VARIABLE";

    static void indentFile(File infile) throws IOException
    {
        int tabs = 0;
        String spaces = "";
        String tabbedLine;
        String inPath = infile.getAbsolutePath();
        String abPath;
        File ofile = new File("parsedLog.txt");

        /* saving file in working directory is not working
        if(OSDetector.isWindows())
        {
            int index = inPath.lastIndexOf("\\", inPath.length());
            abPath = inPath.substring(0,index);
            ofile = new File(abPath + "parsedLog.txt");
        }
        else if(OSDetector.isMac() || OSDetector.isLinux())
        {
            int index = inPath.lastIndexOf("/", inPath.length());
            abPath = inPath.substring(0,index);
            ofile = new File(abPath + "parsedLog.txt");
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sorry, guy...");
            alert.setContentText("I don't know what operating system you're using and therefore can't open the file.");
            return;
        }
        */

        PrintWriter writer = new PrintWriter(ofile);

        BufferedReader br = new BufferedReader(new FileReader(infile));
        for(String line = br.readLine(); line != null; line = br.readLine())
        {
            spaces = "";
            if(line.contains(fin) || line.contains(ext) || line.contains(end)) { if(tabs > 0) tabs--; }
            for(int i = tabs; i > 0; i--) spaces += "  -  ";
            tabbedLine = spaces + line;
            writer.println(tabbedLine);
            if(line.contains(strt) || line.contains(ent) || (line.contains(bgin) && !line.contains(var))) { tabs++; }
        }
        open(ofile);
    }

    public static boolean open(File file)
    {
        try
        {
            if (OSDetector.isWindows())
            {
                Runtime.getRuntime().exec(new String[]
                        {"rundll32", "url.dll,FileProtocolHandler",
                                file.getAbsolutePath()});
                return true;
            } else if (OSDetector.isLinux() || OSDetector.isMac())
            {
                Runtime.getRuntime().exec(new String[]{"/usr/bin/open",
                        file.getAbsolutePath()});
                return true;
            } else
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sorry, guy...");
                alert.setContentText("I don't know what operating system you're using and therefore can't open the file.");
                return false;
            }
        } catch (Exception e)
        {
            e.printStackTrace(System.err);
            return false;
        }
    }
}
