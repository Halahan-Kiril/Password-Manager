package com.passmanager.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
Главный класс для запуска сервера.
Аннотация @SpringBootApplication объединяет в себе три функции: 
включает автоконфигурацию, сканирование компонентов (пакетов) 
и позволяет определять дополнительные конфигурации.
*/
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        /* Запуск всей экосистемы Spring Boot. 
        На этом этапе поднимается встроенный сервер (Tomcat), 
        создаются подключения к базе данных и настраиваются REST-контроллеры.
        */
        SpringApplication.run(ServerApplication.class, args);
    }
}