package com.passmanager.server.repository;

import com.passmanager.server.model.PasswordRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
Интерфейс репозитория для доступа к данным.
Наследуясь от JpaRepository, мы получаем полный набор методов для CRUD-операций,
не написав ни одной строчки реализации.
*/
@Repository
public interface PasswordRepository extends JpaRepository<PasswordRecord, Long> {
    
    /* Параметры JpaRepository <PasswordRecord, Long> означают:
    1. PasswordRecord — с каким типом сущности работаем.
    2. Long — какой тип данных используется для первичного ключа (ID).
    
    Здесь уже "из коробки" доступны:
    - .findAll() — получить все записи
    - .save(entity) — создать или обновить запись
    - .deleteById(id) — удалить запись по ID
    - .findById(id) — найти конкретную запись
    */
}