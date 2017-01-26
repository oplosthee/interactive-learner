package interactivelearner.gui;

import interactivelearner.classifier.NaiveBayesianClassifier;
import interactivelearner.data.Category;
import interactivelearner.data.Corpus;
import interactivelearner.util.FileProcessor;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class TrainGUI extends Application {

    private File file;
    private NaiveBayesianClassifier classifier;
    private Corpus corpus;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Naive bayesian classifier");

        HBox topMenu = new HBox(12);
        Label chiValue = new Label("Words with highest chi value");
        TextField chiValueField = new TextField();
        chiValueField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                chiValueField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        chiValueField.setText("300");
        chiValueField.setMaxWidth(50);
        Label smoothingLabel = new Label("Smoothing (>= 1)");
        TextField smoothingField = new TextField();
        smoothingField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                smoothingField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        smoothingField.setText("1");
        smoothingField.setMaxWidth(50);
        topMenu.getChildren().addAll(smoothingLabel, smoothingField, chiValueField, chiValue);

        Label training = new Label("Select folder for training the classifier");
        Label currentlySelected = new Label();
        Button fileSelect = new Button("Select a folder");
        fileSelect.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            file = directoryChooser.showDialog(primaryStage);
            currentlySelected.setText(file.getName());
        });

        FlowPane centerPane = new FlowPane();
        centerPane.getChildren().addAll(training, fileSelect, currentlySelected);

        Button train = new Button("train");
        Label state = new Label();
        train.setOnAction(event -> {
            if (file == null || Integer.valueOf(chiValueField.getCharacters().toString()) < 0 || Integer.valueOf(smoothingField.getCharacters().toString()) < 1) {
                state.setText("cannot perform training, check configuration");
            } else {
                classifier = new NaiveBayesianClassifier(Integer.valueOf(smoothingField.getCharacters().toString()));
                trainClassifier(Integer.valueOf(chiValueField.getCharacters().toString()));
            }
        });

        HBox bottomPane = new HBox();
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.getChildren().addAll(train, state);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);
        borderPane.setCenter(centerPane);
        borderPane.setBottom(bottomPane);
        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void trainClassifier(int vocabularyLength) {
        corpus = new Corpus(vocabularyLength);
        File[] directories = file.listFiles(File::isDirectory);
        if (directories != null) {
            for (File directory : directories) {
                Category category = new Category(directory.getName());
                try {
                    category.addDocuments(FileProcessor.tokenizeFolder(directory.getAbsolutePath()));
                    corpus.addCategory(category);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        classifier.train(corpus);
    }

}
