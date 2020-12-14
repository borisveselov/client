package edu.courseproject.client.controller;

import com.jfoenix.controls.JFXButton;
import edu.courseproject.client.connection.ConnectionServer;
import edu.courseproject.client.controller.workerPageController.WorkerAddToSkladPageController;
import edu.courseproject.client.controller.workerPageController.WorkerOrderPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WorkerPageController implements Initializable {

    private ConnectionServer datasource = ConnectionServer.getInstance();
    @FXML
    private JFXButton orderButn;

    @FXML
    private Button addProductBurn;

    @FXML
    private Button newProductButn;

    @FXML
    private ImageView img;

    @FXML
    void addToSkaldAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/workerview/worker_add_to_sklad.fxml"));
        Parent staffViewParent = loader.load();
        Scene staffViewScene = new Scene(staffViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        WorkerAddToSkladPageController workerAddToSkladPageController = loader.getController();
        workerAddToSkladPageController.setStage(window);
        window.setScene(staffViewScene);
        window.show();
    }


    @FXML
    void orderAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/workerview/worker_order_page.fxml"));
        Parent staffViewParent = loader.load();
        Scene staffViewScene = new Scene(staffViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        WorkerOrderPageController workerOrderPageController = loader.getController();
        workerOrderPageController.setStage(window);
        window.setScene(staffViewScene);
        window.show();
    }

    @FXML
    void refreshRequest(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
