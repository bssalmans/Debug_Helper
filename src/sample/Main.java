package sample;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;


public class Main extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Button buttonLoad = new Button("Load");
        buttonLoad.setOnAction(arg0 -> {
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT Files(*.txt)", "*.txt");
            fc.getExtensionFilters().add(extFilter);
            File file = fc.showOpenDialog(primaryStage);
            try { debugParser.indentFile(file); } catch (IOException ioe) {}
        });
        VBox vBox = VBoxBuilder.create().children(buttonLoad).build();
        root.getChildren().add(vBox);
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();
    }

}
