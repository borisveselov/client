package edu.courseproject.client;


import com.jfoenix.controls.JFXDecorator;
import edu.courseproject.client.connection.ConnectionServer;
import edu.courseproject.client.controller.StartPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    private final static String PORT_REGEX = "\\d{4}";
    private static int port = 2525;

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].matches(PORT_REGEX)) {
                port = Integer.valueOf(args[0]);
            }
        }
        ConnectionServer.getInstance().setPort(port);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/start_page.fxml"));
        Parent root = loader.load();
        StartPageController controller = loader.getController();
        JFXDecorator decorator = new JFXDecorator(primaryStage, root);
        primaryStage.setOnCloseRequest(event ->
                controller.shutDown()
        );
        primaryStage.setTitle("Логистическая компания");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(decorator));
        primaryStage.show();
    }
}

