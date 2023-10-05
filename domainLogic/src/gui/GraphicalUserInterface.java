package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class GraphicalUserInterface extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/gui/view/view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Warehouse Management System");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception exception)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception");
            alert.setContentText(exception.getMessage());
            alert.show();
        }
    }

    public static void invoke(String[] args)
    {
        launch(args);
    }

}
