package interactivelearner.gui;

import interactivelearner.classifier.NaiveBayesianClassifier;
import interactivelearner.data.Category;
import interactivelearner.data.Corpus;
import interactivelearner.data.Document;
import interactivelearner.util.FileProcessor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClassifyController implements Initializable {

    private Corpus corpus;
    private NaiveBayesianClassifier classifier;
    private File file;
    private String currentlyClassified;
    private Document document;
    private Stage mainStage;

    @FXML
    Label documentCount;
    @FXML
    Label specifiedFile;
    @FXML
    Label warning;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    void initData(Corpus corpus, NaiveBayesianClassifier classifier, Stage stage) {
        this.corpus = corpus;
        this.classifier = classifier;
        documentCount.setText(String.valueOf(corpus.getDocumentCount()));
        mainStage = stage;
    }

    public void handleFolderSelection() {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            specifiedFile.setText(file.getPath());
        } else {
            specifiedFile.setText("null");
        }
    }

    public void classifyFile() {
        if (file != null) {
            try {
                document = FileProcessor.tokenizeFile(file.getAbsolutePath());
                Category category = classifier.classify(corpus, document);
                currentlyClassified = category.getName();
                handleClassification();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            warning.setText("Please specify a file before classifying");
        }
    }

    private void handleClassification() {
        List<Category> categories = corpus.getCategories();
        List<String> choices = new ArrayList<>();
        for (Category category : categories) {
            choices.add(category.getName());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(currentlyClassified, choices);
        dialog.setTitle("Classification");
        dialog.setHeaderText("Classified the document as " + currentlyClassified + ". Please select the right classification");
        dialog.setContentText("Category: ");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(letter -> currentlyClassified = letter);

        Category category = corpus.getCategory(currentlyClassified);
        category.addDocument(document);
        classifier.train(corpus);
        documentCount.setText(String.valueOf(corpus.getDocumentCount()));
    }

    @FXML
    private void toTrainScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/TrainGUI.fxml"));
            Parent train = loader.load();
            TrainController controller = loader.getController();
            controller.getStage(mainStage);
            Scene trainScene = new Scene(train, 600, 400);
            mainStage.setScene(trainScene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
