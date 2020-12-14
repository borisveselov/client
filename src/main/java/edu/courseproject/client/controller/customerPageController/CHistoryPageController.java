package edu.courseproject.client.controller.customerPageController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.courseproject.client.action.Action;
import edu.courseproject.client.connection.ConnectionServer;
import edu.courseproject.client.entity.Order;
import edu.courseproject.client.entity.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CHistoryPageController implements Initializable {
    private Gson gson = new Gson();
    private ConnectionServer datasource = ConnectionServer.getInstance();
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Button makeOrderButn;

    @FXML
    private Button statisticsButn;

    @FXML
    private TextArea processingOrdersArea;

    @FXML
    private TextArea adoptedOrdersArea;

    @FXML
    private TextArea rejectedOrdersArea;

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
    void statisticsAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statisticsButn.setDisable(true);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", Action.HISTORY_CUSTOMER);
        jsonObject.put("idcustomer", Session.getClient().getIdUser());
        datasource.getWriter().println(jsonObject);

        List<Order> orders = new ArrayList<>();
        JSONObject inputObject;
        try {
            inputObject = new JSONObject(datasource.getReader().readLine());
            Type orderList = new TypeToken<ArrayList<Order>>() {
            }.getType();
            orders = gson.fromJson(inputObject.getString("orders"), orderList);
            StringBuilder rejectStr = new StringBuilder();
            StringBuilder procesStr = new StringBuilder();
            StringBuilder adopStr = new StringBuilder();
            adoptedOrdersArea.setPrefRowCount(orders.size());
            for (Order item : orders) {
                if (item.getProcessing().equals("rejected")) {
                    rejectStr.append("Дата заказа: " + item.getOrderDate() + " общая стоимость: " + item.getCost() + "\n");
                } else {
                    if (item.getProcessing().equals("adopted")) {
                        adopStr.append("Дата заказа: " + item.getOrderDate() + " общая стоимость: " + item.getCost() + "\n");
                    } else {
                        procesStr.append("Дата заказа: " + item.getOrderDate() + " общая стоимость: " + item.getCost() + "\n");
                    }
                }
            }
            adoptedOrdersArea.setText(adopStr.toString());
            rejectedOrdersArea.setText(rejectStr.toString());
            processingOrdersArea.setText(procesStr.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
