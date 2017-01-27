package interactivelearner.gui;

import interactivelearner.classifier.NaiveBayesianClassifier;
import interactivelearner.data.Category;
import interactivelearner.data.Corpus;
import interactivelearner.data.Document;
import interactivelearner.featureselection.CommonWordTransformer;
import interactivelearner.featureselection.RareWordTransformer;
import interactivelearner.featureselection.StopWordTransformer;
import interactivelearner.util.FileProcessor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TrainController implements Initializable {

    private Stage mainStage;

    private File selectedFile;
    private NaiveBayesianClassifier classifier;
    private Corpus corpus;
    private File articleFile;

    @FXML
    TextField smoothing;
    @FXML
    TextField chiValue;
    @FXML
    Label selectedFolder;
    @FXML
    Button trainButton;
    @FXML
    Label warning;
    @FXML
    Label selectedArticle;
    @FXML
    CheckBox articleCheckBox;
    @FXML
    TextField commonWordsField;
    @FXML
    TextField unCommonWordsField;
    @FXML
    Label trainingProgress;


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
        commonWordsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                commonWordsField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        unCommonWordsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                unCommonWordsField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void handleFolderSelection() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        selectedFile = directoryChooser.showDialog(mainStage);
        if (selectedFile != null) {
            selectedFolder.setText(selectedFile.getPath());
        }
    }

    public void handleStopWordsSelection() {
        FileChooser fileChooser = new FileChooser();
        articleFile = fileChooser.showOpenDialog(mainStage);
        if (articleFile != null) {
            selectedArticle.setText(articleFile.getAbsolutePath());
        }
    }

    public void trainClassifier() {
        trainingProgress.setText("Training...");
        if (selectedFile != null) {
            classifier = new NaiveBayesianClassifier(Integer.valueOf(smoothing.getCharacters().toString()));
            corpus = new Corpus(Integer.valueOf(chiValue.getCharacters().toString()));
            File[] directories = selectedFile.listFiles(File::isDirectory);
            if (directories != null) {
                for (File directory : directories) {
                    Category category = new Category(directory.getName());
                    try {
                        category.addDocuments(processFiles(FileProcessor.tokenizeFolder(directory.getAbsolutePath())));
                        corpus.addCategory(category);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            classifier.train(corpus);
            trainingProgress.setText("Done!");
        } else {
            trainingProgress.setText("");
            warning.setText("please select training data");
        }
    }

    public void startClassifyScene() {
        try {
            if (corpus != null && classifier != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ClassifyGUI.fxml"));
                Parent classify = loader.load();
                ClassifyController controller = loader.getController();
                Scene classifyScene = new Scene(classify, 400, 300);
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

    private List<Document> processFiles(List<Document> files) {
        List<Document> temp;
        List<Document> result;
        CommonWordTransformer commonWordTransformer = new CommonWordTransformer(Integer.valueOf(commonWordsField.getCharacters().toString()));
        RareWordTransformer rareWordTransformer = new RareWordTransformer(Integer.valueOf(unCommonWordsField.getCharacters().toString()));
        result = commonWordTransformer.run(files);
        temp = rareWordTransformer.run(result);
        if (articleCheckBox.isSelected()) {
            StopWordTransformer stopWordTransformer = new StopWordTransformer(articleFile.getAbsolutePath());
            result = stopWordTransformer.run(temp);
        } else {
            result = temp;
        }
        return result;
    }

    void getStage(Stage stage) {
        mainStage = stage;
    }
}
