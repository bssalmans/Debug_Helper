package sample;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Main extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        final File[] file = new File[1];

        primaryStage.setTitle("Debug Log Parser");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        Text file_txt = new Text("File Select");
        file_txt.setId("option-text");
        grid.add(file_txt,0,0,2,1);

        Text filelabel_txt = new Text("Selected file: ");
        grid.add(filelabel_txt,0,2);

        final Text filename = new Text();
        filename.setId("filename");
        grid.add(filename,1,2);

        Text options_txt = new Text("Options");
        options_txt.setId("option-text");
        grid.add(options_txt,0,3);

        Button indent_btn = new Button("Indent");
        indent_btn.setOnAction(arg0 ->
        {
            try { debugParser.indentFile(file[0]); } catch (IOException ioe) {} finally {}
        });
        indent_btn.setDisable(true);
        grid.add(indent_btn,0,4);

        Button queryCount_btn = new Button("Query Counter");
        queryCount_btn.setOnAction(arg0 ->
        {
            try { debugParser.queryCounter((file[0])); } catch (IOException ioe) {} finally {}
        });
        queryCount_btn.setDisable(true);
        grid.add(queryCount_btn,1,4);

        Button selectFile_btn = new Button("Select File");
        selectFile_btn.setOnAction(arg0 ->
        {
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT Files(*.txt)", "*.txt");
            fc.getExtensionFilters().add(extFilter);
            file[0] = fc.showOpenDialog(primaryStage);
            filename.setText(file[0].getName());
            indent_btn.setDisable(false);
            queryCount_btn.setDisable(true);
        });
        grid.add(selectFile_btn,0,1);

        Scene menu = new Scene(grid,450,350);
        primaryStage.setScene(menu);
        menu.getStylesheets().add(Main.class.getResource("debug.css").toExternalForm());
        primaryStage.show();
    }

}
