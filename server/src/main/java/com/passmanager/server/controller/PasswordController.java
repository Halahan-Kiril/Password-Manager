package com.passmanager.server.controller;

import com.passmanager.server.model.PasswordRecord;
import com.passmanager.server.repository.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
REST-контроллер для связи JavaFX клиента с базой данных PostgreSQL.
Реализует логику Zero-Knowledge: хранит данные, зашифрованные на стороне клиента.
*/
@RestController
@RequestMapping("/api/passwords")
@CrossOrigin 
public class PasswordController {

    @Autowired
    private PasswordRepository passwordRepository;

    @GetMapping
    public List<PasswordRecord> getAllPasswords() {
        // Возвращает список всех записей (пароли в них уже в зашифрованном виде)
        return passwordRepository.findAll();
    }

    @PostMapping
    public PasswordRecord addPassword(@RequestBody PasswordRecord record) {
        // Сохранение новой записи. Сервер не расшифровывает входящий пароль.
        return passwordRepository.save(record);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassword(@PathVariable Long id) {
        // Проверка существования записи перед удалением для корректного HTTP-ответа
        if (passwordRepository.existsById(id)) {
            passwordRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}