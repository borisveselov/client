package edu.courseproject.client.controller.workerPageController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import edu.courseproject.client.action.Action;
import edu.courseproject.client.connection.ConnectionServer;
import edu.courseproject.client.entity.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class WorkerAddToSkladPageController implements Initializable {
    private long idSklad = 0;
    private Gson gson = new Gson();
    private ConnectionServer datasource = ConnectionServer.getInstance();
    private List<Product> products = new ArrayList<>();
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private Map<Long, Integer> prodAmountmap = new HashMap<>();
    private double fullCostOrder = 0;


    @FXML
    private JFXButton orderButn;

    @FXML
    private Button addProductBurn;

    @FXML
    private ImageView img;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView<Product> sprinkleTable;

    @FXML
    private TableView<Product> mineralTable;

    @FXML
    private TableView<Product> juiceTable;

    @FXML
    private Label amountLabel;

    @FXML
    private Button addButn;

    @FXML
    private Spinner<Integer> countSpin;

    @FXML
    void addToBasket(ActionEvent event) {
        if (!sprinkleTable.getSelectionModel().getSelectedItems().isEmpty()) {
            prodAmountmap.put(sprinkleTable.getSelectionModel().getSelectedItem().getIdProduct(), countSpin.getValue());
            Product product = sprinkleTable.getSelectionModel().getSelectedItem();
            sprinkleTable.getItems().remove(product);
        }
        if (!mineralTable.getSelectionModel().getSelectedItems().isEmpty()) {
            prodAmountmap.put(mineralTable.getSelectionModel().getSelectedItem().getIdProduct(), countSpin.getValue());
            Product product = mineralTable.getSelectionModel().getSelectedItem();
            mineralTable.getItems().remove(product);
        }
        if (!juiceTable.getSelectionModel().getSelectedItems().isEmpty()) {
            prodAmountmap.put(juiceTable.getSelectionModel().getSelectedItem().getIdProduct(), countSpin.getValue());
            Product product = juiceTable.getSelectionModel().getSelectedItem();
            juiceTable.getItems().remove(product);
        }
    }

    @FXML
    void addToSkaldAction(ActionEvent event) {
    }

    @FXML
    void moveToSklad(ActionEvent event) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", Action.FILL_SKLAD);
        jsonObject.put("map", gson.toJson(prodAmountmap));
        datasource.getWriter().println(jsonObject);
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

    @FXML
    void selectGasDrink(MouseEvent event) {

    }

    @FXML
    void selectJuiceDrink(MouseEvent event) {

    }

    @FXML
    void selectMinaralDrink(MouseEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1, 1);
        countSpin.setValueFactory(valueFactory);
        fillTabPane(sprinkleTable);
        fillTabPane(mineralTable);
        fillTabPane(juiceTable);
        loadProducts();
    }

    private void loadProducts() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", Action.FIND_ALL_PRODUCTS);
        datasource.getWriter().println(jsonObject);
        JSONObject inputObject;
        try {
            inputObject = new JSONObject(datasource.getReader().readLine());
            Type productsList = new TypeToken<ArrayList<Product>>() {
            }.getType();
            products = gson.fromJson(inputObject.getString("products"), productsList);
            sprinkleTable.getItems().addAll(products.stream().filter(item -> item.getCategory().equals("газированная")).collect(Collectors.toList()));
            mineralTable.getItems().addAll(products.stream().filter(item -> item.getCategory().equals("негазированная")).collect(Collectors.toList()));
            juiceTable.getItems().addAll(products.stream().filter(item -> item.getCategory().equals("сок")).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillTabPane(TableView<Product> tab) {
        tab.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        tab.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
        tab.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("idProduct"));
    }

}
