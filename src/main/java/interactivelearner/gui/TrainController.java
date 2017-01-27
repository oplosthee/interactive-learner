package interactivelearner.gui;

import interactivelearner.classifier.NaiveBayesianClassifier;
import interactivelearner.data.Category;
import interactivelearner.data.Corpus;
import interactivelearner.util.FileProcessor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TrainController implements Initializable {

    private Stage mainStage;

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
    Label warning;


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
        selectedFile = directoryChooser.showDialog(mainStage);
        if (selectedFile != null) {
            selectedFolder.setText(selectedFile.getPath());
        } else {
            selectedFolder.setText("null");
        }
    }

    public void trainClassifier() {
        System.out.println("started training");
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
        System.out.println("finished training");
    }

    public void startClassifyScene() {
        try {
            if (corpus != null && classifier != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ClassifyGUI.fxml"));
                Parent classify = loader.load();
                ClassifyController controller = loader.getController();
                Scene classifyScene = new Scene(classify, 600, 400);
                controller.initData(corpus, classifier, mainStage);
                mainStage.setScene(classifyScene);
                mainStage.show();
            } else {
                warning.setText("please select training data and train the classifier");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void getStage(Stage stage) {
        mainStage = stage;
    }
}
