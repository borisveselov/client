package edu.courseproject.client.controller.workerPageController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import edu.courseproject.client.action.Action;
import edu.courseproject.client.connection.ConnectionServer;
import edu.courseproject.client.entity.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkerOrderPageController implements Initializable {
    private Stage stage;
    private ObservableList<String> orderItemLists = FXCollections.observableArrayList();
    private ConnectionServer datasource = ConnectionServer.getInstance();
    private long idOrderSelected;
    private Gson gson = new Gson();
    private String selectedOrder;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private JFXButton orderButn;

    @FXML
    private Button addProductBurn;

    @FXML
    private Button newProductButn;

    @FXML
    private ImageView img;

    @FXML
    private Label newOrderLabel;

    @FXML
    private Button checkOrderButn;

    @FXML
    private ListView<String> orderList;

    @FXML
    private Label successLabel;

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
    void checkOrder(ActionEvent event) {
        selectedOrder = orderList.getSelectionModel().getSelectedItem();
        Pattern pattern = Pattern.compile("^\\d+");
        Matcher matcher = pattern.matcher(selectedOrder);
        String idOrderStr = "";
        while (matcher.find()) {
            idOrderStr = matcher.group();
        }
        idOrderSelected = Long.parseLong(idOrderStr);
        JSONObject jsOb = new JSONObject();
        jsOb.put("action", Action.UPDATE_ORDER);
        Order order = new Order();
        order.setIdOrder(idOrderSelected);
        order.setProcessing("adopted");
        jsOb.put("order", gson.toJson(order));
        datasource.getWriter().println(jsOb);
        orderItemLists.remove(selectedOrder);
        successLabel.setVisible(true);

        jsOb.put("action", Action.MOVE_FROM_SKLAD);
        jsOb.put("idorder", idOrderSelected);
        datasource.getWriter().println(jsOb);
    }

    @FXML
    void newProductAdction(ActionEvent event) {

    }

    @FXML
    void orderAction(ActionEvent event) {

    }

    @FXML
    void refreshRequest(ActionEvent event) {

    }

    @FXML
    void selectOrder(MouseEvent event) {
        successLabel.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderButn.setDisable(true);
        loadProcessedOrders();
        if (orderItemLists.isEmpty()) {
            newOrderLabel.setVisible(true);
        } else {
            orderList.setItems(orderItemLists);
        }
    }

    private void loadProcessedOrders() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", Action.HAS_PROCESSED_ORDERS);
        datasource.getWriter().println(jsonObject);
        JSONObject inputObject;
        try {
            inputObject = new JSONObject(datasource.getReader().readLine());
            Type ordList = new TypeToken<List<String>>() {
            }.getType();
            orderItemLists.addAll((Collection<String>) gson.fromJson(inputObject.getString("processedOrders"), ordList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
