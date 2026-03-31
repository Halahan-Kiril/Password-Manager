package com.passmanager.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
Главный класс запуска приложения. 
Наследуется от Application, что является обязательным для 
любого графического интерфейса на JavaFX.
*/
public class Main extends Application {

    /*
    Основной метод жизненного цикла JavaFX. 
    Вызывается автоматически после метода main. 
    Stage — это подмостки (окно), на которых мы размещаем Scene (сцену).
    */
    @Override
    public void start(Stage stage) throws Exception {
        
        /* Загружаем разметку окна авторизации. 
        На этом этапе FXML-файл парсится и создаются Java-объекты контроллера и элементов UI.
        */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        
        Scene scene = new Scene(loader.load(), 400, 350);
        
        /* Подключаем файл стилей CSS. 
        Метод toExternalForm() превращает путь к ресурсу в валидную для JavaFX строку URL.
        */
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        
        // Установка заголовка окна, который отображается в системной рамке
        stage.setTitle("Authentication Required");
        
        // Привязываем подготовленную сцену к нашему окну (Stage)
        stage.setScene(scene);
        
        /* Запрещаем пользователю изменять размер окна логина вручную. 
        Это гарантирует, что дизайн не «поедет» при попытке растянуть окно.
        */
        stage.setResizable(false);
        
        // Финальная команда: делаем окно видимым на экране
        stage.show();
    }

    /*
    Точка входа в программу на уровне JVM. 
    Вызывает метод launch(), который запускает всю магию JavaFX 
    и в конечном итоге передает управление методу start().
    */
    public static void main(String[] args) {
        launch(args);
    }
}