package com.passmanager.client.ui;

import com.passmanager.client.model.PasswordRecord;
import com.passmanager.client.network.ApiService;
import com.passmanager.client.util.EncryptionUtil;
import com.passmanager.client.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.security.SecureRandom;

public class AddPasswordController {

    @FXML private TextField websiteField;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField; // Поле для ввода пароля (теперь НЕ скрытое)

    private final ApiService apiService = new ApiService();

    /**
     * Обработка нажатия кнопки Save.
     */
    @FXML
    private void handleSave() {
        String website = websiteField.getText();
        String username = usernameField.getText();
        String plainPassword = passwordField.getText();

        // Простая проверка на пустые поля
        if (website.isEmpty() || username.isEmpty() || plainPassword.isEmpty()) {
            System.err.println("All fields are required!");
            return;
        }

        try {
            // 1. Получаем Мастер-пароль, который мы ввели в окне Login
            String masterKey = SessionManager.getMasterPassword();

            // 2. Шифруем пароль ПЕРЕД отправкой на сервер
            // Теперь в объект PasswordRecord попадет зашифрованный текст (Base64)
            String encryptedPassword = EncryptionUtil.encrypt(plainPassword, masterKey);

            // 3. Создаем объект записи
            PasswordRecord newRecord = new PasswordRecord(website, username, encryptedPassword);

            // 4. Отправляем зашифрованную запись Виктору на сервер
            boolean isSaved = apiService.savePassword(newRecord);

            if (isSaved) {
                System.out.println("Encrypted password sent to server successfully.");
                closeWindow();
            } else {
                System.err.println("Server rejected the request.");
            }

        } catch (Exception e) {
            System.err.println("Error during encryption or saving: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) websiteField.getScene().getWindow();
        stage.close();
    }

    // Метод генерации теперь будет сразу показывать текст в поле
    @FXML
    private void generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        java.security.SecureRandom random = new java.security.SecureRandom();
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < 16; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        // Теперь этот текст будет ВИДЕН пользователю в окне добавления
        passwordField.setText(sb.toString());
    }
}