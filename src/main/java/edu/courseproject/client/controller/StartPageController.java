package edu.courseproject.client.controller;

import com.google.gson.Gson;
import edu.courseproject.client.action.Action;
import edu.courseproject.client.connection.ConnectionServer;
import edu.courseproject.client.entity.Session;
import edu.courseproject.client.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartPageController implements Initializable {
    private ConnectionServer connection;
    private ExecutorService executor;
    private Gson gson;
    private final String REGEX_PASSWORD = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\\w\\d]{6,}";

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button logInButton;

    @FXML
    private PasswordField password;

    @FXML
    private TextField login;

    @FXML
    private Label singUp;

    @FXML
    void logIn() {
        String log = login.getText();
        String pass = password.getText();
        if (log.trim().isEmpty()) {
            allertWarning("Введено некорректное значение логина", "Такого пользователя не существует! Проверьте это поле.");
        }
        if (pass.trim().isEmpty()) {
            allertWarning("Введено некорректное значение пароля", "Проверьте это поле!");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", Action.IS_USER_EXISTS);
        User user = new User(log, pass);
        jsonObject.put("user", gson.toJson(user));
        connection.getWriter().println(jsonObject.toString());

        try {
            String jsonString = connection.getReader().readLine();
            JSONObject inputJson = new JSONObject(jsonString);
            boolean isExists = inputJson.getBoolean("isExists");
            if (isExists) {
                User userFromServer = gson.fromJson(inputJson.getString("user"), User.class);
                System.out.println(userFromServer);
                if (userFromServer.getStatus().equals("active")) {
                    Session.setClient(userFromServer);
                    switch (userFromServer.getRole()) {
                        case "admin":
                            showManagerMenu();
                            break;
                        case "worker":
                            goToWorkerMenu();
                            break;
                        case "customer":
                            showCustomerMenu();
                            break;
                    }
                } else {
                    allertWarning("Ошибка при входе в систему!", "В данный момент доступ к системе Вам запрещен! Обратитесь к менеджеру.");
                }
            } else {
                allertWarning("Пользователя не существует!", "Проверьте логин!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        password.clear();
        login.clear();
    }

    private void goToWorkerMenu() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/worker_page.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    private void showCustomerMenu() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/customer_page.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    private void showManagerMenu() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/admin_page.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    public void singUp(MouseEvent event) {

    }

    void allertWarning(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Внимание!");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
        return;
    }

    public void shutDown() {
        executor.shutdown();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        executor = Executors.newSingleThreadExecutor();
        connection = ConnectionServer.getInstance();
        gson = new Gson();
        Runnable connect = () -> {
            if (connection.isConnected()) {
                System.out.println("Подключение прошло успешно");
            } else {
                System.out.println("Подключение не прошло");
            }
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        executor.execute(connect);
        password.setTooltip(new Tooltip("Пароль должен содержать хотя бы одно число, одну латинскую букву в нижнем и нижнем регистрах"));
    }
}
