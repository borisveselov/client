package edu.courseproject.client.controller;

import com.jfoenix.controls.JFXButton;
import edu.courseproject.client.action.Action;
import edu.courseproject.client.connection.ConnectionServer;
import edu.courseproject.client.controller.adminPageController.AdminCustomerPageController;
import edu.courseproject.client.controller.adminPageController.AdminOrderPageController;
import edu.courseproject.client.controller.adminPageController.AdminStaffPageController;
import edu.courseproject.client.controller.adminPageController.AdminStatisticsPageController;
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
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMainController implements Initializable {
    private ConnectionServer datasource = ConnectionServer.getInstance();

    @FXML
    private Button stafButn;

    @FXML
    private Button statisticsButn;

    @FXML
    private JFXButton orderButn;

    @FXML
    private Button customerButn;

    @FXML
    private ImageView img;

    @FXML
    void orderAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminview/admin_adopt_order_page.fxml"));
        Parent staffViewParent = loader.load();
        Scene staffViewScene = new Scene(staffViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminOrderPageController adminOrderPageController = loader.getController();
        adminOrderPageController.setStage(window);
        window.setScene(staffViewScene);
        window.show();
    }

    @FXML
    void refreshRequest(ActionEvent event) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", Action.HAS_POSTED_ORDERS);
        datasource.getWriter().println(jsonObject.toString());

        try {
            String jsonString = datasource.getReader().readLine();
            JSONObject inputJson = new JSONObject(jsonString);
            boolean hasNewOrders = inputJson.getBoolean("hasNewOrders");
            if (hasNewOrders) {
                img.setVisible(true);
                orderButn.setDisable(false);
            } else {
                img.setVisible(false);
                orderButn.setDisable(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void customerAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminview/admin_customer_page.fxml"));
        Parent staffViewParent = loader.load();
        Scene staffViewScene = new Scene(staffViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminCustomerPageController adminCustomerPageController = loader.getController();
        adminCustomerPageController.setStage(window);
        window.setScene(staffViewScene);
        window.show();
    }

    @FXML
    void staffAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminview/admin_staff_page.fxml"));
        Parent staffViewParent = loader.load();
        Scene staffViewScene = new Scene(staffViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminStaffPageController adminStaffPageController = loader.getController();
        adminStaffPageController.setStage(window);
        window.setScene(staffViewScene);
        window.show();
    }

    @FXML
    void statisticsAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminview/admin_statistic_page.fxml"));
        Parent staffViewParent = loader.load();
        Scene staffViewScene = new Scene(staffViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminStatisticsPageController adminStatisticsPageController = loader.getController();
        adminStatisticsPageController.setStage(window);
        window.setScene(staffViewScene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        img.setVisible(false);
    }
}
