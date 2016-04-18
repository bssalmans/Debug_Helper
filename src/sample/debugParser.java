package sample;
import java.io.*;

/**
 * Created by brado on 4/18/2016.
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
        PrintWriter writer = new PrintWriter("./parsedLog.txt", "UTF-8");

        BufferedReader br = new BufferedReader(new FileReader(infile));
        for(String line = br.readLine(); line != null; line = br.readLine())
        {
            spaces = "";
            if(line.contains(fin) || line.contains(ext) || line.contains(end)) { if(tabs > 0) tabs--; }
            for(int i = tabs; i > 0; i--) spaces += "  -  ";
            tabbedLine = spaces + line;
            System.out.println("Spaces: " + spaces);
            //System.out.println(tabbedLine);
            writer.println(tabbedLine);
            if(line.contains(strt) || line.contains(ent) || (line.contains(bgin) && !line.contains(var))) { tabs++; }
        }
    }
}
