package com.passmanager.client.network;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.passmanager.client.model.PasswordRecord;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ApiService {
    // URL сервера Виктора (стандартный для Spring Boot)
    private final String BASE_URL = "http://localhost:8080/api/passwords";
    
    // HttpClient встроен в Java 11+, дополнительные библиотеки не нужны
    private final HttpClient httpClient = HttpClient.newHttpClient();
    
    // ObjectMapper превращает JSON (текст) в Java объекты
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Получает список всех паролей от бэкенда.
     */
    public List<PasswordRecord> getAllPasswords() throws Exception {
        // 1. Создаем запрос
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        // 2. Отправляем запрос и ждем текстовый ответ (JSON)
        HttpResponse<String> response = httpClient.send(
                request, 
                HttpResponse.BodyHandlers.ofString()
        );

        // 3. Проверяем статус ответа (200 - это успех)
        if (response.statusCode() == 200) {
            String jsonBody = response.body();
            
            // Превращаем JSON в список объектов PasswordRecord
            return objectMapper.readValue(
                    jsonBody, 
                    new TypeReference<List<PasswordRecord>>() {}
            );
        } else {
            // Если сервер ответил ошибкой, возвращаем пустой список
            return new ArrayList<>();
        }
    }


    /**
     * Отправляет новый пароль на сервер.
     */
    public boolean savePassword(PasswordRecord record) throws Exception {
        // 1. Превращаем объект в JSON-строку
        String json = objectMapper.writeValueAsString(record);

        // 2. Создаем POST-запрос
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json") // Говорим серверу, что шлем JSON
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // 3. Отправляем и проверяем статус
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Если статус 200 (OK) или 201 (Created), значит всё успешно
        return response.statusCode() == 200 || response.statusCode() == 201;
    }

    public void deletePassword(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    
}