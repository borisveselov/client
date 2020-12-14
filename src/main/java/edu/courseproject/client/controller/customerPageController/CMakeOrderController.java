package edu.courseproject.client.controller.customerPageController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.courseproject.client.action.Action;
import edu.courseproject.client.connection.ConnectionServer;
import edu.courseproject.client.entity.Order;
import edu.courseproject.client.entity.Product;
import edu.courseproject.client.entity.Session;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CMakeOrderController implements Initializable {
    private long idSklad = 0;
    private static final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private static final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
    private Button makeOrderButn;

    @FXML
    private DatePicker deliveryDataField;

    @FXML
    private Label deliveryDataLabel;

    @FXML
    private Label postLabel;

    @FXML
    private Button sendOrderButn;


    @FXML
    private Button statisticsButn;

    @FXML
    private Label skladLabel;

    @FXML
    private Label basketLabel;

    @FXML
    private TabPane tabPane;

    @FXML
    private Label conditionLabel;

    @FXML
    private ImageView belarusImg;

    @FXML
    private AnchorPane grodnoPane;

    @FXML
    private AnchorPane minskPane;

    @FXML
    private AnchorPane mogilevPane;

    @FXML
    private Label amountLabel;
    @FXML
    private Spinner<Integer> countSpin;

    @FXML
    private Label choiceSkladLabel;

    @FXML
    private TableView<Product> sprinkleTable;

    @FXML
    private TableView<Product> mineralTable;

    @FXML
    private TableView<Product> juiceTable;

    @FXML
    private Button addButn;

    @FXML
    void addToBasket(ActionEvent event) {
        if (!sprinkleTable.getSelectionModel().getSelectedItems().isEmpty()) {
            fullCostOrder += sprinkleTable.getSelectionModel().getSelectedItem().getPrice() * countSpin.getValue();
            prodAmountmap.put(sprinkleTable.getSelectionModel().getSelectedItem().getIdProduct(), countSpin.getValue());
            Product product = sprinkleTable.getSelectionModel().getSelectedItem();
            sprinkleTable.getItems().remove(product);
        }
        if (!mineralTable.getSelectionModel().getSelectedItems().isEmpty()) {
            fullCostOrder += mineralTable.getSelectionModel().getSelectedItem().getPrice() * countSpin.getValue();
            prodAmountmap.put(mineralTable.getSelectionModel().getSelectedItem().getIdProduct(), countSpin.getValue());
            Product product = mineralTable.getSelectionModel().getSelectedItem();
            mineralTable.getItems().remove(product);
        }
        if (!juiceTable.getSelectionModel().getSelectedItems().isEmpty()) {
            fullCostOrder += juiceTable.getSelectionModel().getSelectedItem().getPrice() * countSpin.getValue();
            prodAmountmap.put(juiceTable.getSelectionModel().getSelectedItem().getIdProduct(), countSpin.getValue());
            Product product = juiceTable.getSelectionModel().getSelectedItem();
            juiceTable.getItems().remove(product);
        }
        conditionLabel.setDisable(false);
    }

    @FXML
    void makeOrder(ActionEvent event) {

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

    @FXML
    void sendOrder(ActionEvent event) {
        makeOrderButn.setDisable(false);
        statisticsButn.setDisable(false);
        Date date = new Date();
        String orderDate = formatter.format(date);
        String[] currentData = orderDate.split("/");
        String deliveryDate = sdf.format(deliveryDataField.getValue());
        String[] delivery = deliveryDate.split("/");

        if (deliveryDataField.getValue() == null || ((Integer.parseInt(currentData[0])<Integer.parseInt(delivery[0]))&&(Integer.parseInt(currentData[1])>Integer.parseInt(delivery[1])))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Внимание!");
            alert.setContentText("Дата поставки некорректна!");
            alert.showAndWait();
        } else {
            DecimalFormatSymbols s = new DecimalFormatSymbols();
            s.setDecimalSeparator('.');

            DecimalFormat f = new DecimalFormat("#,##0.00", s);


            Order order = new Order(idSklad, Session.getClient().getIdUser(), orderDate, deliveryDate, "posted");
            order.setCost(Double.parseDouble(f.format(fullCostOrder)));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", Action.ADD_ORDER);
            jsonObject.put("order", gson.toJson(order));
            jsonObject.put("map", gson.toJson(prodAmountmap));
            datasource.getWriter().println(jsonObject);
            postLabel.setVisible(true);
        }
    }

    @FXML
    void basketClick(MouseEvent event) {
        choiceSkladLabel.setVisible(false);
        belarusImg.setVisible(false);
        tabPane.setVisible(true);
        amountLabel.setVisible(true);
        countSpin.setVisible(true);
        addButn.setVisible(true);
    }

    @FXML
    void conditionClick(MouseEvent event) {
        sendOrderButn.setVisible(true);
        tabPane.setVisible(false);
        amountLabel.setVisible(false);
        countSpin.setVisible(false);
        addButn.setVisible(false);
        deliveryDataLabel.setVisible(true);
        deliveryDataField.setVisible(true);
    }

    @FXML
    void grodnoSklad(MouseEvent event) {
        idSklad = 2;
        choiceSkladLabel.setText("Беларусь, г.Гродно, ул. Герасимовича 1А");
        basketLabel.setDisable(false);
    }

    @FXML
    void minskSklad(MouseEvent event) {
        idSklad = 3;
        choiceSkladLabel.setText("Беларусь, г. Минск, ул. Скорины 50");
        basketLabel.setDisable(false);
    }

    @FXML
    void mogilevSkald(MouseEvent event) {
        idSklad = 1;
        choiceSkladLabel.setText("Беларусь, г.Могилев, ул.Колужская 33");
        basketLabel.setDisable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeOrderButn.setDisable(true);
        statisticsButn.setDisable(true);
        conditionLabel.setDisable(true);
        basketLabel.setDisable(true);
        belarusImg.setVisible(true);
        choiceSkladLabel.setVisible(true);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1, 1);
        countSpin.setValueFactory(valueFactory);
        fillTabPane(sprinkleTable);
        fillTabPane(mineralTable);
        fillTabPane(juiceTable);
        deliveryDataField.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return sdf.format(object);
                }
                return null;
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.trim().isEmpty()) {
                    return LocalDate.parse(string, sdf);
                }
                return null;
            }
        });
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
