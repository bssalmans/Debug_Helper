package sample;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import static java.util.Collections.reverseOrder;

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
    static String soql = "SOQL";
    static String cu = "CODE_UNIT";
    static String meth = "METHOD";
    static String con = "CONSTRUCTOR";
    static String exe = "EXECUTION";
    static String dml = "DML";
    static String err = "ERROR";

    static void indentFile(File infile) throws IOException
    {
        int tabs = 0;
        String spaces;
        String tabbedLine;
        //String inPath = infile.getAbsolutePath();
        //String abPath;
        File ofile = new File("parsedLog.txt");

        // TODO: Figure out saving in working directory
        // region working directory code
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
        //endregion

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
        writer.close();
        open(ofile);
    }

    static void createXMLFile(File infile) throws IOException
    {
        String[] section = new String[5];
        //String inPath = infile.getAbsolutePath();
        //String abPath;
        File ofile = new File("parsedLog.xml");

        // TODO: Figure out saving in working directory
        //region Working directory code
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
        //endregion

        PrintWriter writer = new PrintWriter(ofile);
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<?xml-stylesheet type=\"text/xsl\" href=\"logStyle.xsl\"?>");
        writer.println("<log>");

        BufferedReader br = new BufferedReader(new FileReader(infile));
        for(String line = br.readLine(); line != null; line = br.readLine())
        {
            // line type
            if(line.contains(cu)) section[0] = "<type>Code</type>";
            else if(line.contains(meth)) section[0] = "<type>Method</type>";
            else if(line.contains(con)) section[0] = "<type>Constructor</type>";
            else if(line.contains(soql)) section[0] = "<type>SOQL</type>";
            else if(line.contains(exe)) section[0] = "<type>Execution</type>";
            else if(line.contains(dml)) section[0] = "<type>DML</type>";
            else if(line.contains(err)) section[0] = "<type>Error</type>";
            else section[0] = "<type>Unknown</type>";
            // section start/end (start=true/end=false/other=null)
            if(line.contains(strt) || line.contains(ent) || (line.contains(bgin))) section[1] = "<start>true</start>";
            else if(line.contains(fin) || line.contains(ext) || line.contains(end)) section[1] = "<start>false</start>";
            else section[1] = "<start>null</start>";
            // payload (type dependent)
            if(section[0].contains("Code") && section[1].contains("true"))
            {
                String payload = line.substring(line.lastIndexOf("|")+1, line.length());
                section[2] = "<codesection>"+payload+"</codesection>";
            }
            else if(section[0].contains("Method") && section[1].contains("true"))
            {
                String payload = line.substring(line.lastIndexOf("|")+1, line.length());
                section[2] = "<method>"+payload+"</method>";
            }
            else if(section[0].contains("SOQL") && section[1].contains("true"))
            {
                String payload = line.substring(line.lastIndexOf("|")+1, line.length());
                section[2] = "<query>"+payload+"</query>";
            }
            else if(section[0].contains("SOQL") && section[1].contains("false"))
            {
                String payload = line.substring(line.lastIndexOf(":")+1, line.length());
                section[2] = "<queryRows>"+payload+"</queryRows>";
            }
            else if(section[0].contains("DML") && section[1].contains("true"))
            {
                String[] payload = line.split("[|]");
                section[2] = "<op>"+payload[3].substring(payload[3].lastIndexOf(":")+1,payload[3].length())+"</op>";
                section[3] = "<object>"+payload[4].substring(payload[4].lastIndexOf(":")+1,payload[4].length())+"</object>";
                section[4] = "<rows>"+payload[5].substring(payload[5].lastIndexOf(":")+1,payload[5].length())+"</rows>";
            }

            writer.println("<line>");
            writer.println("    "+section[0]);
            writer.println("    "+section[1]);
            if(section[2]!=null) writer.println("    "+section[2]);
            if(section[3]!=null) writer.println("    "+section[3]);
            if(section[4]!=null) writer.println("    "+section[4]);
            writer.println("</line>");

            for(int i=0;i<5;i++) section[i]=null;
        }
        writer.println("</log>");
        writer.close();
        open(ofile);
    }

    // TODO: Figure out stage/scene change
    public static void queryCounter(File infile) throws IOException
    {
        HashMap<String,Integer> qCount = new HashMap<>();

        //String inPath = infile.getAbsolutePath();
        String query;
        int index;

        File ofile = new File("queryCount.txt");

        PrintWriter writer = new PrintWriter(ofile);

        BufferedReader br = new BufferedReader(new FileReader(infile));
        for(String line = br.readLine(); line != null; line = br.readLine())
        {
            if(line.contains(soql))
            {
                index = line.lastIndexOf("|", line.length());
                if(index >= 1 && !line.contains("Row"))
                {
                    query = line.substring(index,line.length()-1);
                    if(qCount.keySet().contains(query)) qCount.put(query,qCount.get(query) + 1);
                    else qCount.put(query, new Integer(1));
                }
            }
        }

        HashMap<String,Integer> newQCount = (HashMap)sortByValue(qCount);
        Iterator it = newQCount.entrySet().iterator();
        while(it.hasNext())
        {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            writer.println(pair.getValue() + " time(s): " + pair.getKey());
        }
        writer.close();
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

    public static Map<String,Integer> sortByValue(Map<String,Integer> map)
    {
        Map<String,Integer> result = new LinkedHashMap<>();
        Stream<Map.Entry<String,Integer>> st = map.entrySet().stream();

        st.sorted(reverseOrder(Map.Entry.comparingByValue())).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }
}
