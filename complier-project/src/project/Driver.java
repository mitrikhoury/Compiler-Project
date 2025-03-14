package project;


import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Driver extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Set up the FileChooser
        FileChooser fileChooser = createFileChooser();

        // Show the file chooser dialog immediately when the application starts
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        // Handle the selected file (or no file selected)
        if (selectedFile != null) {
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            // Parse the selected file
            Parser.parse(selectedFile.getAbsolutePath());
        } else {
            System.out.println("No file selected");
            // Close the application
            System.exit(0);
        }
        primaryStage.setTitle("File Chooser");
        primaryStage.setWidth(400);
        primaryStage.setHeight(400);
        primaryStage.show();
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        return fileChooser;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

