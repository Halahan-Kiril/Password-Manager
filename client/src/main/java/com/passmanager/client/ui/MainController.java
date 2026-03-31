package com.passmanager.client.ui;

import com.passmanager.client.model.PasswordRecord;
import com.passmanager.client.network.ApiService;
import com.passmanager.client.util.EncryptionUtil;
import com.passmanager.client.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableCell;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

/*
Контроллер главного окна. Управляет списком паролей, поиском 
и взаимодействием с API.
*/
public class MainController {

    @FXML private TableView<PasswordRecord> passwordTable;
    @FXML private TableColumn<PasswordRecord, String> colWebsite;
    @FXML private TableColumn<PasswordRecord, String> colUsername;
    @FXML private TextField searchField;
    
    @FXML private TableColumn<PasswordRecord, String> colPassword;

    // Основной список данных, полученный от сервера
    private final ObservableList<PasswordRecord> masterData = FXCollections.observableArrayList();

    private final ApiService apiService = new ApiService();

    @FXML
    public void initialize() {
        // Привязка полей модели PasswordRecord к колонкам таблицы
        colWebsite.setCellValueFactory(new PropertyValueFactory<>("website"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        /*
        Кастомная фабрика ячеек для маскировки пароля. 
        Позволяет хранить реальные данные в модели, но отображать только символы подстановки.
        */
        colPassword.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText("********");
                }
            }
        });

        // Создание обертки для фильтрации списка
        FilteredList<PasswordRecord> filteredData = new FilteredList<>(masterData, p -> true);

        // Логика динамического поиска по сайту или имени пользователя
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(record -> {
                if (newValue == null || newValue.isEmpty()) return true;
                
                String lowerCaseFilter = newValue.toLowerCase();
                if (record.getWebsite().toLowerCase().contains(lowerCaseFilter)) return true;
                if (record.getUsername().toLowerCase().contains(lowerCaseFilter)) return true;
                
                return false;
            });
        });

        passwordTable.setItems(filteredData);
        handleRefresh();
    }

    @FXML
    private void handleRefresh() {
        try {
            // Получение зашифрованного списка и мастер-ключа сессии
            List<PasswordRecord> dataFromServer = apiService.getAllPasswords();
            String masterKey = SessionManager.getMasterPassword();

            // Дешифровка каждой записи перед отображением
            for (PasswordRecord record : dataFromServer) {
                try {
                    String decrypted = EncryptionUtil.decrypt(record.getPassword(), masterKey);
                    record.setPassword(decrypted);
                } catch (Exception e) {
                    record.setPassword("******** (Decryption Error)");
                }
            }

            // Обновление ObservableList автоматически обновляет таблицу через FilteredList
            masterData.setAll(dataFromServer);
            
            System.out.println("Table refreshed: " + dataFromServer.size() + " records.");

        } catch (Exception e) {
            System.err.println("Failed to refresh list: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddNew() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/passmanager/client/add_password.fxml"));
            Parent root = loader.load();

            // Настройка модального окна добавления записи
            Stage stage = new Stage();
            stage.setTitle("Add New Password");
            stage.initModality(Modality.APPLICATION_MODAL); 
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/passmanager/client/style.css").toExternalForm());
            stage.setScene(scene);

            stage.showAndWait();

            // Обновление таблицы после возврата из окна добавления
            handleRefresh();
        } catch (IOException e) {
            System.err.println("Error opening Add window: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        PasswordRecord selected = passwordTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            apiService.deletePassword(selected.getId());
            handleRefresh();
        } catch (Exception e) {
            System.err.println("Error deleting record: " + e.getMessage());
        }
    }

    @FXML
    private void handleCopy() {
        PasswordRecord selected = passwordTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            // Работа с системным буфером обмена для копирования расшифрованного пароля
            final javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
            final javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
            
            content.putString(selected.getPassword());
            clipboard.setContent(content);

            System.out.println("Password for " + selected.getWebsite() + " copied!");
        }
    }
}