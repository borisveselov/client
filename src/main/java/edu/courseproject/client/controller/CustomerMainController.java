package edu.courseproject.client.controller;

import com.google.gson.Gson;
import edu.courseproject.client.connection.ConnectionServer;
import edu.courseproject.client.controller.customerPageController.CHistoryPageController;
import edu.courseproject.client.controller.customerPageController.CMakeOrderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerMainController {

    private Gson gson = new Gson();
    private ConnectionServer datasource = ConnectionServer.getInstance();

    @FXML
    private Button makeOrderButn;

    @FXML
    private Button statisticsButn;

    @FXML
    void makeOrder(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customerview/make_order_page.fxml"));
        Parent staffViewParent = loader.load();
        Scene staffViewScene = new Scene(staffViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        CMakeOrderController cMakeOrderController = loader.getController();
        cMakeOrderController.setStage(window);
        window.setScene(staffViewScene);
        window.show();
    }

    @FXML
    void statisticsAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customerview/customer_history_page.fxml"));
        Parent staffViewParent = loader.load();
        Scene staffViewScene = new Scene(staffViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        CHistoryPageController cHistoryPageController = loader.getController();
        cHistoryPageController.setStage(window);
        window.setScene(staffViewScene);
        window.show();
    }
}
