package interactivelearner.gui;

import interactivelearner.classifier.NaiveBayesianClassifier;
import interactivelearner.data.Category;
import interactivelearner.data.Corpus;
import interactivelearner.util.FileProcessor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TrainController implements Initializable {

    private File selectedFile;
    private NaiveBayesianClassifier classifier;
    private Corpus corpus;

    @FXML
    TextField smoothing;
    @FXML
    TextField chiValue;
    @FXML
    Label selectedFolder;
    @FXML
    Button folderButton;
    @FXML
    Button trainButton;
    @FXML
    VBox vBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        smoothing.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                smoothing.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        chiValue.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                chiValue.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void handleFolderSelection() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) vBox.getScene().getWindow();
        selectedFile = directoryChooser.showDialog(stage);
        selectedFolder.setText(selectedFile.getPath());
    }

    public void trainClassifier() {
        classifier = new NaiveBayesianClassifier(Integer.valueOf(smoothing.getCharacters().toString()));
        corpus = new Corpus(Integer.valueOf(chiValue.getCharacters().toString()));
        File[] directories = selectedFile.listFiles(File::isDirectory);
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
