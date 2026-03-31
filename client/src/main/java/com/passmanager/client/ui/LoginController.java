package com.passmanager.client.ui;

import com.passmanager.client.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.io.IOException;

// Контроллер экрана входа. Отвечает за проверку мастер-пароля и переход к основному интерфейсу приложения.
public class LoginController {

    @FXML private PasswordField masterPasswordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String password = masterPasswordField.getText();

        // Базовая валидация: не пускаем дальше без ввода данных
        if (password == null || password.isEmpty()) {
            errorLabel.setText("Password cannot be empty!");
            return;
        }

        // Передаем пароль в SessionManager, где он будет использоваться 
        // как ключ для расшифровки данных в течение сессии
        SessionManager.setMasterPassword(password);

        switchToMainWindow();
    }


    // Загружает главную сцену и заменяет ею текущее окно логина

    private void switchToMainWindow() {
        try {
            // Загрузка разметки главного окна из ресурсов
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/passmanager/client/main.fxml"));
            Parent root = loader.load();

            // Получаем текущее Stage (окно) через ссылку на один из элементов текущей сцены
            Stage stage = (Stage) masterPasswordField.getScene().getWindow();
            Scene scene = new Scene(root, 600, 450);

            // Принудительное подключение стилей, так как новая сцена создается программно
            scene.getStylesheets().add(getClass().getResource("/com/passmanager/client/style.css").toExternalForm());

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            // Печать стека ошибок в консоль для отладки при проблемах с загрузкой FXML/CSS
            e.printStackTrace();
        }
    }
}