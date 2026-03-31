/**
 * Модель данных для сервера.
 * Она должна быть идентична клиентской по полям, 
 * чтобы JSON правильно передавался.
 */
package com.passmanager.server.model;

import jakarta.persistence.*; // Важно: импорты для базы

/* Аннотация @Entity помечает класс как сущность, которая будет 
автоматически превращена в таблицу базы данных.
@Table задает имя этой таблицы в PostgreSQL.
*/
@Entity 
@Table(name = "passwords")
public class PasswordRecord {

    /* Первичный ключ (Primary Key). 
    IDENTITY указывает на использование встроенного в БД механизма автоинкремента.
    */
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    // Название ресурса (например, "Google" или "GitHub")
    private String website;
    
    // Имя пользователя или email
    private String username;
    
    /* Поле для хранения пароля. 
    Важно: здесь всегда хранится зашифрованная строка (Base64), 
    присланная клиентом. Сервер не знает реального значения.
    */
    private String password;

    /* Пустой конструктор без аргументов.
    Критически важен для библиотек Jackson (десериализация JSON) 
    и Hibernate (создание объектов из строк БД).
    */
    public PasswordRecord() {
    }

    /* Конструктор для удобного создания новых объектов в коде бэкенда 
    (например, при тестировании или миграциях).
    */
    public PasswordRecord(String website, String username, String password) {
        this.website = website;
        this.username = username;
        this.password = password;
    }

    /* Геттеры и сеттеры — стандартный механизм доступа к приватным полям.
    Необходимы для работы сериализаторов и маппинга данных.
    */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}