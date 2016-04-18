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
    static String exCP = "existingCP|{";

    static void indentFile(File infile) throws IOException
    {
        int tabs = 0;
        String spaces = "";
        String tabbedLine = "";
        File outfile = new File("parsedLog.txt");
        FileOutputStream fos = new FileOutputStream(outfile);

        BufferedReader br = new BufferedReader(new FileReader(infile));
        String line = null;
        while ((line = br.readline()) != null)
        {
            if(line.contains(fin) || line.contains(ext) || line.contains(end)) { if(tabs > 0) tabs--; }
            for(int i = tabs; i > 0; i--) spaces += "  -  ";
            tabbedLine = spaces + line;
            fos.write(tabbedLine);
        }
    }
}
