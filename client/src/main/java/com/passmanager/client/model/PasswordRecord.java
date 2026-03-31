package com.passmanager.client.model;

public class PasswordRecord {
    private Long id;
    private String website;
    private String username;
    private String password;

    // Пустой конструктор (обязателен для библиотек JSON)
    public PasswordRecord() {
    }

    // Конструктор для создания новых записей
    public PasswordRecord(String website, String username, String password) {
        this.website = website;
        this.username = username;
        this.password = password;
    }

    // Геттеры и Сеттеры в полном виде
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