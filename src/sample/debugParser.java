package sample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
    static String soql = "SOQL_EXECUTE_BEGIN";

    static void indentFile(File infile) throws IOException
    {
        int tabs = 0;
        String spaces;
        String tabbedLine;
//        String inPath = infile.getAbsolutePath();
//        String abPath;
        File ofile = new File("parsedLog.txt");

        // TODO: Figure out saving in working directory
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

    public static void queryCounter(File infile) throws IOException
    {
        HashMap<String,Integer> qCount = new HashMap<>();

        String inPath = infile.getAbsolutePath();
        String query;
        int index;

        BufferedReader br = new BufferedReader(new FileReader(infile));
        for(String line = br.readLine(); line != null; line = br.readLine())
        {
            if(line.contains(soql))
            {
                index = line.lastIndexOf("|", line.length());
                query = line.substring(index,line.length()-1);
                if(qCount.keySet().contains(query)) qCount.put(query,qCount.get(query) + 1);
                else qCount.put(query, new Integer(1));
            }
        }

        Stage qStage = new Stage();
        qStage.setTitle("Query Metrics");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        Text file_txt = new Text("Results");
        file_txt.setId("option-text");
        grid.add(file_txt,0,0,2,1);

        int rowCount = 1;
        ArrayList<Text> qs = new ArrayList<>();
        Iterator it = qCount.entrySet().iterator();
        while(it.hasNext())
        {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            qs.add(new Text(pair.getKey() + ": " + pair.getValue()));
            grid.add(qs.get(rowCount-1),0,rowCount);
        }

        Scene qScene = new Scene(grid,300,275);
        qStage.setScene(qScene);
        qScene.getStylesheets().add(Main.class.getResource("debug.css").toExternalForm());
        qStage.show();
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
