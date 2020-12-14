package edu.courseproject.client.controller.adminPageController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import edu.courseproject.client.action.Action;
import edu.courseproject.client.connection.ConnectionServer;
import edu.courseproject.client.entity.Customer;
import edu.courseproject.client.entity.Order;
import edu.courseproject.client.entity.Sklad;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.*;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminOrderPageController implements Initializable {
    private Stage stage;
    private ObservableList<String> orderItemLists = FXCollections.observableArrayList();
    private ConnectionServer datasource = ConnectionServer.getInstance();
    private long idOrderSelected;
    private Gson gson = new Gson();
    private String selectedOrder;
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
    private Label newOrderLabel;

    @FXML
    private Button checkOrderButn;

    @FXML
    private Label failOrderLabel;

    @FXML
    private AnchorPane reportPane;

    @FXML
    private TextField nameReportField;

    @FXML
    private Button reportButn;

    @FXML
    private ListView<String> orderList;

    @FXML
    void selectOrder(MouseEvent event) {
        reportPane.setVisible(false);
        failOrderLabel.setVisible(false);
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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", Action.CHECK_ORDER);
        idOrderSelected = Long.parseLong(idOrderStr);
        jsonObject.put("idorder", idOrderSelected);
        datasource.getWriter().println(jsonObject);

        try {
            String jsonString = datasource.getReader().readLine();
            JSONObject inputJson = new JSONObject(jsonString);
            boolean checkOrder = inputJson.getBoolean("checkOrder");
            if (checkOrder) {
                reportPane.setVisible(true);
                updateOrder(true);
            } else {
                failOrderLabel.setVisible(true);
                updateOrder(false);
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
    void generateReport(ActionEvent event) {

        JSONObject jsObject = new JSONObject();
        jsObject.put("action", Action.REPORT);
        jsObject.put("idorder", idOrderSelected);
        datasource.getWriter().println(jsObject);
        JSONObject iObject;
        try {
            iObject = new JSONObject(datasource.getReader().readLine());
            Sklad skladInOrder = gson.fromJson(iObject.getString("sklad"), Sklad.class);
            Customer customerInOrder = gson.fromJson(iObject.getString("customer"), Customer.class);

            List<String> products;
            Type productsList = new TypeToken<ArrayList<String>>() {
            }.getType();
            products = gson.fromJson(iObject.getString("products"), productsList);
            createReport(skladInOrder, customerInOrder, products);

            orderItemLists.remove(selectedOrder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateOrder(boolean action) {
        JSONObject jsOb = new JSONObject();
        jsOb.put("action", Action.UPDATE_ORDER);
        Order order = new Order();
        order.setIdOrder(idOrderSelected);
        if (action) {
            order.setProcessing("processed");
        } else {
            order.setProcessing("rejected");
        }
        jsOb.put("order", gson.toJson(order));
        datasource.getWriter().println(jsOb);
    }

    @FXML
    void orderAction(ActionEvent event) {

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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderButn.setDisable(true);
        loadPostedOrders();
        if (orderItemLists.isEmpty()) {
            newOrderLabel.setVisible(true);
            checkOrderButn.setVisible(false);
            reportPane.setVisible(false);
        } else {
            orderList.setItems(orderItemLists);
            checkOrderButn.setVisible(true);
        }


    }

    private void loadPostedOrders() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", Action.HAS_POSTED_ORDERS);
        datasource.getWriter().println(jsonObject);
        JSONObject inputObject;
        try {
            inputObject = new JSONObject(datasource.getReader().readLine());
            Type ordList = new TypeToken<List<String>>() {
            }.getType();
            orderItemLists.addAll((Collection<String>) gson.fromJson(inputObject.getString("postedOrders"), ordList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createReport(Sklad skladInOrder, Customer customerInOrder, List<String> products) {
        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());

        DecimalFormatSymbols s = new DecimalFormatSymbols();
        s.setDecimalSeparator('.');
        DecimalFormat f = new DecimalFormat("#,##0.00", s);

        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText("Товарно-транспортная накладная");
        run.setFontSize(14);
        run.setFontFamily("Times New Roman");
        run.setBold(true);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.setSpacingAfter(500);
        XWPFParagraph paragraph1 = document.createParagraph();
        XWPFRun run1 = paragraph1.createRun();
        run1.setText("Грузоотправитель: " + skladInOrder.getAddress());
        run1.addBreak();
        run1.setText("Грузополучатель " + customerInOrder.getName() + " " + customerInOrder.getRepresentative());
        run1.addBreak();
        run1.setText("Адрес доставки: " + customerInOrder.getRegionCustomer());

        run1.setFontSize(14);
        run1.setFontFamily("Times New Roman");
        paragraph1.setSpacingAfter(250);

        XWPFTable table = document.createTable();
        table.setCellMargins(50, 0, 10, 0);
        XWPFTableRow row0 = table.getRow(0);

        XWPFTableCell cell0 = row0.getCell(0);
        row0.setHeight(200);
        cell0.setText("Название");
        cell0.setColor("efbf2d");

        XWPFTableCell cell1 = row0.createCell();
        cell1.setText("Количество");
        cell1.setColor("efbf2d");

        XWPFTableCell cell2 = row0.createCell();
        cell2.setText("Цена");
        cell2.setColor("efbf2d");

        XWPFTableCell cell3 = row0.createCell();
        cell3.setText("Стоимость");
        cell3.setColor("efbf2d");

        XWPFTableRow[] row = new XWPFTableRow[products.size()];
        for (int i = 0; i < products.size(); i++) {
            List<String> elements = Arrays.asList(products.get(i).split("="));
            row[i] = table.createRow();
            row[i].getCell(0).setText(elements.get(0));
            row[i].getCell(1).setText(elements.get(1));
            row[i].getCell(2).setText(elements.get(2));
            row[i].getCell(3).setText(f.format(Double.parseDouble(elements.get(1)) * Integer.parseInt(elements.get(2))));
        }

        XWPFParagraph paragraph2 = document.createParagraph();
        XWPFRun run2 = paragraph2.createRun();
        run2.addBreak();
        run2.addBreak();
        run2.setText("Дата проведения анализа:");
        run2.addBreak();
        run2.setText(date);
        run2.addBreak();
        run2.setFontSize(14);
        run2.setFontFamily("Times New Roman");


        try {
            FileOutputStream output = new FileOutputStream(nameReportField.getText() + ".docx");
            document.write(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
