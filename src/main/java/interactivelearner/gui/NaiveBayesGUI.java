package interactivelearner.gui;

import interactivelearner.classifier.NaiveBayesianClassifier;
import interactivelearner.data.Corpus;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;

public class NaiveBayesGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/TrainGUI.fxml"));
        Parent train = loader.load();
        TrainController controller = loader.getController();
        controller.getStage(primaryStage);
        primaryStage.setTitle("Naive bayesian classifier");
        Scene trainScene = new Scene(train, 600, 400);
        primaryStage.setScene(trainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
