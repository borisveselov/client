package edu.courseproject.client.controller.adminPageController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.courseproject.client.action.Action;
import edu.courseproject.client.connection.ConnectionServer;
import edu.courseproject.client.entity.Worker;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminStaffPageController implements Initializable {
    private Gson gson = new Gson();
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private ConnectionServer datasource = ConnectionServer.getInstance();
    private static final String SPLIT_ADDRESS = ",";
    private long idWorkerChangeable;
    private List<Worker> workers = new ArrayList<>();


    @FXML
    private Button stafButn;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView<Worker> workerTable;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField nameField;

    @FXML
    private Spinner<Double> senioriteSpinner;

    @FXML
    private ComboBox<String> statusBox;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField townField;

    @FXML
    private TextField addressField;

    private Worker createValidWorker() {
        if ((surnameField.getText().trim().isEmpty() || !DataValidator.getInstance().isNameValid(surnameField.getText().trim())) ||
                (nameField.getText().trim().isEmpty() || !DataValidator.getInstance().isNameValid(nameField.getText().trim())) ||
                !DataValidator.getInstance().isPhoneValid(phoneField.getText()) ||
                countryField.getText().trim().isEmpty() ||
                (townField.getText().trim().isEmpty()  || (addressField.getText().isEmpty()))) {
            return null;
        } else {
            Worker worker = new Worker(idWorkerChangeable, surnameField.getText(), nameField.getText(),
                    senioriteSpinner.getValue(), phoneField.getText(),
                    countryField.getText() + ", " + townField.getText() + ", " + addressField.getText());
            return worker;
        }
    }

    @FXML
    void addToBD(ActionEvent event) {
        Worker worker = createValidWorker();
        if (worker != null) {
            String newLogin = GeneratorLogin.generateLogin() + worker.hashCode();
            worker.setLogin(newLogin);
            String newPass = GeneratorPassword.generatePassword();
            worker.setPassword(newPass);
            worker.setRole("worker");
            worker.setStatus("active");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", Action.ADD_WORKER);
            jsonObject.put("worker", gson.toJson(worker));
            datasource.getWriter().println(jsonObject);
            workerTable.getItems().add(worker);
            workers.add(worker);
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
        jsonObject.put("action", Action.UPDATE_WORKER);
        Worker worker = createValidWorker();
        if (worker != null) {
            long idUser = workerTable.getSelectionModel().getSelectedItem().getIdUser();
            worker.setIdUser(idUser);
            worker.setStatus(statusBox.getValue());
            jsonObject.put("worker", gson.toJson(worker));
            datasource.getWriter().println(jsonObject.toString());
            workerTable.getItems().removeAll(workers);
            workers.clear();
            loadWorkers();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Внимание!");
            alert.setContentText("Ошибка при добавлении в базу данных! Проверьте корректность введеных данных!");
            alert.showAndWait();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stafButn.setDisable(true);
        fillTabPane(workerTable);
        tabPane.getSelectionModel().select(0);

        ObservableList<String> combobox = FXCollections.observableArrayList();
        combobox.add("active");
        combobox.add("blocked");
        combobox.add("fired");
        statusBox.setItems(combobox);

        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.5, 56, 0.5, 0.5);
        senioriteSpinner.setValueFactory(valueFactory);
        senioriteSpinner.setEditable(true);
        workerTable.getColumns().get(6).setVisible(false);
        workerTable.getColumns().get(7).setVisible(false);
        loadWorkers();
    }

    //FIXME
    @FXML
    void refreshRequest(ActionEvent event) {

    }

    @FXML
    void fillFields(MouseEvent event) {
        idWorkerChangeable = workerTable.getSelectionModel().getSelectedItem().getIdWorker();
        surnameField.setText(workerTable.getSelectionModel().getSelectedItem().getSurname());
        nameField.setText(workerTable.getSelectionModel().getSelectedItem().getName());
        senioriteSpinner.getValueFactory().setValue(workerTable.getSelectionModel().getSelectedItem().getSeniority());
        statusBox.setValue(workerTable.getSelectionModel().getSelectedItem().getStatus());
        phoneField.setText(workerTable.getSelectionModel().getSelectedItem().getPhone());
        String addressWorker = workerTable.getSelectionModel().getSelectedItem().getRegionWorker();
        List<String> elementsRegion = Arrays.asList(addressWorker.split(SPLIT_ADDRESS));
        countryField.setText(elementsRegion.get(0));
        townField.setText(elementsRegion.get(1));
        addressField.setText(elementsRegion.get(2));
    }

    @FXML
    void clearFields(MouseEvent event) {
        surnameField.clear();
        nameField.clear();
        senioriteSpinner.getValueFactory().setValue(0.);
        statusBox.setValue("active");
        statusBox.setEditable(false);
        phoneField.clear();
        countryField.clear();
        townField.clear();
        addressField.clear();
    }

    private void loadWorkers() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", Action.FIND_ALL_WORKERS);
        datasource.getWriter().println(jsonObject);
        JSONObject inputObject;
        try {
            inputObject = new JSONObject(datasource.getReader().readLine());
            Type workersList = new TypeToken<ArrayList<Worker>>() {
            }.getType();
            workers = gson.fromJson(inputObject.getString("workers"), workersList);
            workerTable.getItems().addAll(workers.stream().filter(item -> !item.getStatus().equals("fired")).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillTabPane(TableView<Worker> tab) {
        tab.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("surname"));
        tab.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tab.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("status"));
        tab.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("seniority"));
        tab.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("phone"));
        tab.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("regionWorker"));
        tab.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("idWorker"));
        tab.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("idUser"));
    }
}
