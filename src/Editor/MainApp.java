package Editor;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MainApp extends Application {
    public String dictFile = "data/dictionary.txt";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        AutoCompleteTrie ac = new AutoCompleteTrie();
        Dictionary dict = new DictionaryBST();
        SpellingSuggest ss;

        DictionaryLoader.loadDictionary(ac, dictFile);
        DictionaryLoader.loadDictionary(dict, dictFile);
        ss = new NearbyWords(dict);

        SmartTextArea editor = new SmartTextArea(ac, ss, dict);
        BorderPane layout = new BorderPane();

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem menuNewFile = new MenuItem("New");
        menuNewFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                editor.clear();
            }
        });
        MenuItem menuOpenFile = new MenuItem("Open");
        menuOpenFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TextEditor", "*.txt"));
                File file = fileChooser.showOpenDialog(primaryStage);
                try (Scanner fileReader = new Scanner(file)) {
                    editor.clear();
                    String line;
                    while ((line = fileReader.nextLine()) != null) {
                        editor.appendText(line);
                        editor.appendText("\n");
                    }
                }catch (FileNotFoundException e) {
                    error();
                }catch (Exception e) {}
            }
        });
        MenuItem menuSaveFile = new MenuItem("Save");
        menuSaveFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("text", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(primaryStage);

                if(file != null){
                    try {
                        FileWriter fileWriter;

                        fileWriter = new FileWriter(file);
                        fileWriter.write(editor.getText());
                        fileWriter.close();
                    }catch (IOException e) {
                        error();
                    }
                }
            }
        });
        Menu menuTool = new Menu("Tools");
        MenuItem wordCount = new MenuItem("Word Count");
        wordCount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Document doc = new Document(editor.getText());
                String text = "Words: " + doc.getNumWords() + "\n\n" +
                              "Characters: " + doc.getText().length() + "\n\n" +
                              "Characters excluding spaces: " +
                              doc.getText().replaceAll(" +", "").length() + "\n\n" +
                              "Sentences: " + doc.getNumSentences() + "\n\n" +
                              "Flesch Score: " + doc.getFleschScore();
                Label label = new Label(text);
                Stage popup = new Stage();

                BorderPane popupLayout = new BorderPane();
                popupLayout.setCenter(label);
                popup.setScene(new Scene(popupLayout, 300, 200));
                popup.show();
            }
        });
        menuFile.getItems().addAll(menuNewFile, menuOpenFile, menuSaveFile);
        menuTool.getItems().addAll(wordCount);
        menuBar.getMenus().addAll(menuFile, menuTool);


        layout.setTop(menuBar);
        layout.setCenter(editor);

        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setTitle("Text Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void error() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.show();
    }
}
