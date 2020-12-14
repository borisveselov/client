package edu.courseproject.client.controller.adminPageController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import edu.courseproject.client.action.Action;
import edu.courseproject.client.connection.ConnectionServer;
import edu.courseproject.client.entity.Customer;
import edu.courseproject.client.generator.GeneratorLogin;
import edu.courseproject.client.generator.GeneratorPassword;
import edu.courseproject.client.validator.DataValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminCustomerPageController implements Initializable {

    private Gson gson = new Gson();
    private Stage stage;
    private ConnectionServer datasource = ConnectionServer.getInstance();
    private static final String SPLIT_ADDRESS = ",";
    private long idCustomerChangeable;
    private List<Customer> customers = new ArrayList<>();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Button stafButn;


    @FXML
    private JFXButton orederBtn;

    @FXML
    private ImageView img;

    @FXML
    private Button customerButn;

    @FXML
    private Button statisticsButn;


    @FXML
    private TabPane tabPane;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TextField nameField;

    @FXML
    private TextField representativeField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField townField;

    @FXML
    private TextField addressField;

    @FXML
    private ComboBox<String> statusBox;

    private Customer createValidCustomer() {
        if (representativeField.getText().trim().isEmpty() || DataValidator.getInstance().isNameValid(representativeField.getText())||
                countryField.getText().trim().isEmpty()||
                townField.getText().trim().isEmpty() ||
                addressField.getText().isEmpty()||
                emailField.getText().trim().isEmpty()|| DataValidator.getInstance().isEmailValid(emailField.getText()))
         {
            return null;
        } else {
            Customer customer = new Customer(idCustomerChangeable, nameField.getText(), representativeField.getText(), emailField.getText(),
                    countryField.getText() + ", " + townField.getText() + ", " + addressField.getText());
            return customer;
        }
    }

    @FXML
    void customerAction(ActionEvent event) {

    }

    @FXML
    void addToBD(ActionEvent event) {
        Customer customer = createValidCustomer();
        if (customer != null) {
            String newLogin = GeneratorLogin.generateLogin() + customer.hashCode();
            customer.setLogin(newLogin);
            String newPass = GeneratorPassword.generatePassword();
            customer.setPassword(newPass);
            customer.setRole("customer");
            customer.setStatus("active");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", Action.ADD_CUSTOMER);
            jsonObject.put("customer", gson.toJson(customer));
            datasource.getWriter().println(jsonObject);
            customerTable.getItems().add(customer);
            customers.add(customer);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Внимание!");
            alert.setContentText("Ошибка при добавлении в базу данных! Проверьте корректность введеных данных!");
            alert.showAndWait();
        }

    }

    //TODO if selected item exists
    @FXML
    void changeInBD(ActionEvent event) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", Action.UPDATE_CUSTOMER);
        Customer customer = createValidCustomer();
        if (customer != null) {
            long idUser = customerTable.getSelectionModel().getSelectedItem().getIdUser();
            customer.setIdUser(idUser);
            customer.setStatus(statusBox.getValue());
            jsonObject.put("customer", gson.toJson(customer));
            datasource.getWriter().println(jsonObject.toString());
            customerTable.getItems().removeAll(customers);
            customers.clear();
            loadCustomers();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Внимание!");
            alert.setContentText("Ошибка при добавлении в базу данных! Проверьте корректность введеных данных!");
            alert.showAndWait();
        }
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
    void clearFields(MouseEvent event) {
        nameField.clear();
        representativeField.clear();
        emailField.clear();
        statusBox.setValue("active");
        statusBox.setEditable(false);
        countryField.clear();
        townField.clear();
        addressField.clear();
    }

    @FXML
    void fillFields(MouseEvent event) {
        idCustomerChangeable = customerTable.getSelectionModel().getSelectedItem().getIdCustomer();
        nameField.setText(customerTable.getSelectionModel().getSelectedItem().getName());
        representativeField.setText(customerTable.getSelectionModel().getSelectedItem().getRepresentative());
        statusBox.setValue(customerTable.getSelectionModel().getSelectedItem().getStatus());
        emailField.setText(customerTable.getSelectionModel().getSelectedItem().getEmail());
        String addressWorker = customerTable.getSelectionModel().getSelectedItem().getRegionCustomer();
        List<String> elementsRegion = Arrays.asList(addressWorker.split(SPLIT_ADDRESS));
        countryField.setText(elementsRegion.get(0));
        townField.setText(elementsRegion.get(1));
        addressField.setText(elementsRegion.get(2));
    }

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


    //FIXme
    @FXML
    void refreshRequest(ActionEvent event) {
        System.out.println("fsfsfsf");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        img.setVisible(false);
        customerButn.setDisable(true);
        fillTabPane(customerTable);
        tabPane.getSelectionModel().select(0);
        ObservableList<String> combobox = FXCollections.observableArrayList();
        combobox.add("active");
        combobox.add("blocked");
        combobox.add("reserved");
        statusBox.setItems(combobox);
        customerTable.getColumns().get(5).setVisible(false);
        customerTable.getColumns().get(6).setVisible(false);
        loadCustomers();
    }

    private void loadCustomers() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", Action.FIND_ALL_CUSTOMERS);
        datasource.getWriter().println(jsonObject);
        JSONObject inputObject;
        try {
            inputObject = new JSONObject(datasource.getReader().readLine());
            Type customersList = new TypeToken<ArrayList<Customer>>() {
            }.getType();
            customers = gson.fromJson(inputObject.getString("customers"), customersList);
            customerTable.getItems().addAll(customers.stream().filter(item -> !item.getStatus().equals("blocked")).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillTabPane(TableView<Customer> tab) {
        tab.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        tab.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("representative"));
        tab.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("status"));
        tab.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("email"));
        tab.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("regionCustomer"));
        tab.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("idCustomer"));
        tab.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("idUser"));
    }
}
