package edu.courseproject.client.controller.adminPageController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import edu.courseproject.client.action.Action;
import edu.courseproject.client.connection.ConnectionServer;
import edu.courseproject.client.entity.Order;
import edu.courseproject.client.entity.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminStatisticsPageController implements Initializable {
    private Stage stage;
    private ConnectionServer datasource = ConnectionServer.getInstance();
    private Gson gson = new Gson();
    private static final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private String begin;
    private String end;
    private List<Order> personList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private JFXButton orderButn;

    @FXML
    private Button stafButn;

    @FXML
    private Button customerButn;

    @FXML
    private Button statisticsButn;

    @FXML
    private ImageView img;

    @FXML
    private BarChart<String, Double> barChar;

    @FXML
    private DatePicker beginDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private RadioButton sumCostRB;

    @FXML
    private RadioButton topProdRB;

    @FXML
    private PieChart pieChart;

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
    void refreshRequest(ActionEvent event) {

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
    void statisticsAction(ActionEvent event) {

    }

    @FXML
    void popularAction(ActionEvent event) {
        personList.clear();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", Action.TOP_CUSTOMER);
        jsonObject.put("beginDate", gson.toJson(begin));
        jsonObject.put("endDate", gson.toJson(end));
        datasource.getWriter().println(jsonObject);
        JSONObject inputObject;

        try {
            inputObject = new JSONObject(datasource.getReader().readLine());
            Type dataList = new TypeToken<List<Order>>() {
            }.getType();
            personList.addAll(gson.fromJson(inputObject.getString("sum"), dataList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPieChart();
        barChar.setVisible(false);
        pieChart.setVisible(true);

    }

    @FXML
    void sumCostAction(ActionEvent event) {
        productList.clear();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", Action.TOP_PRODUCT);
        datasource.getWriter().println(jsonObject);
        JSONObject inputObject;

        try {
            inputObject = new JSONObject(datasource.getReader().readLine());
            Type dataList = new TypeToken<List<Product>>() {
            }.getType();
            productList.addAll(gson.fromJson(inputObject.getString("popular"), dataList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setBarChart();

        pieChart.setVisible(false);
        barChar.setVisible(true);

    }

    private void setPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (int i = 0; i < personList.size(); i++) {
            String str;
            if (personList.get(i).getOrderDate() == null) {
                str = personList.get(i).getDeliveryDate();
            } else {
                str = personList.get(i).getOrderDate();
            }
            pieChartData.add(new PieChart.Data(str, personList.get(i).getCost()));
        }
        pieChartData.add(new PieChart.Data("Табакеров Алексей Андреевич", 1109 + (int) (Math.random() * 1200)));
        pieChartData.addAll(new PieChart.Data("ООО 'Ларчин Гасподар'", 700 + (int) (Math.random() * 1600)));
        pieChart.setData(pieChartData);
    }

    private void setBarChart() {
        barChar.getData().clear();
        XYChart.Series set = new XYChart.Series<>();
        int i = 0;
        for (Product item : productList) {
            set.getData().add(new XYChart.Data(item.getName(), item.getPrice()));
            i++;
            if (i == 5) {
                break;
            }
        }
        set.getData().add(new XYChart.Data("Кока-Кола Лайт 1л", 10 + (int) (Math.random() * 90)));
        barChar.getData().addAll(set);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statisticsButn.setDisable(true);
    }
}
