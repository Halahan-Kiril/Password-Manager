package com.passmanager.client.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

/*
Утилитарный класс для шифрования и дешифрования данных по алгоритму AES.
Использует мастер-пароль пользователя для генерации секретного ключа.
*/
public class EncryptionUtil {

    /*
    Превращаем любую строку (Мастер-пароль) в правильный 16-байтный ключ для AES.
    Алгоритм AES требует ключ фиксированной длины (16, 24 или 32 байта).
    */
    private static SecretKeySpec deriveKey(String masterPassword) throws Exception {
        // Переводим строку пароля в массив байтов в кодировке UTF-8
        byte[] key = masterPassword.getBytes(StandardCharsets.UTF_8);
        
        // Используем хеш-функцию SHA-256, чтобы получить уникальный отпечаток пароля.
        // Это гарантирует, что даже короткий пароль превратится в сложный набор байтов.
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        
        // SHA-256 выдает 32 байта, но для стандартного AES-128 нам нужно ровно 16.
        // Копируем первые 16 байт из полученного хеша.
        key = Arrays.copyOf(key, 16);
        
        // Создаем спецификацию ключа, указывая, что он предназначен для алгоритма AES
        return new SecretKeySpec(key, "AES");
    }

    /*
    Зашифровывает строку данных с использованием мастер-ключа.
    Возвращает строку в формате Base64, которую удобно хранить в БД или передавать по сети.
    */
    public static String encrypt(String data, String masterKey) throws Exception {
        // Создаем экземпляр шифра с настройками AES
        Cipher cipher = Cipher.getInstance("AES");
        
        // Инициализируем шифр в режиме шифрования (ENCRYPT_MODE) с нашим сгенерированным ключом
        cipher.init(Cipher.ENCRYPT_MODE, deriveKey(masterKey));
        
        // Выполняем само шифрование. doFinal возвращает зашифрованный массив байтов.
        byte[] encrypted = cipher.doFinal(data.getBytes());
        
        // Преобразуем "сырые" байты в читабельную строку через Base64, 
        // иначе при попытке сохранить это как текст данные могут повредиться.
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /*
    Расшифровывает данные, закодированные в Base64, обратно в исходный текст.
    Требует тот же мастер-ключ, который использовался при шифровании.
    */
    public static String decrypt(String encryptedData, String masterKey) throws Exception {
        // Создаем экземпляр шифра AES
        Cipher cipher = Cipher.getInstance("AES");
        
        // Инициализируем шифр в режиме расшифровки (DECRYPT_MODE)
        cipher.init(Cipher.DECRYPT_MODE, deriveKey(masterKey));
        
        // Сначала переводим строку Base64 обратно в массив зашифрованных байтов
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        
        // Проводим дешифровку и конвертируем итоговый массив байтов в обычную строку
        return new String(cipher.doFinal(decoded));
    }
}