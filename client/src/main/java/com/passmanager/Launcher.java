package com.passmanager.client;

/*
Вспомогательный класс-запускатор. 
Он необходим, так как JavaFX проверяет, расширяет ли главный класс 
аргумент Application. Если запустить Main напрямую из JAR, 
может возникнуть ошибка "JavaFX runtime components are missing".
*/
public class Launcher {
    
    /*
    Точка входа, которая не наследуется от Application.
    Это позволяет обмануть проверку среды выполнения и корректно 
    загрузить все графические библиотеки перед стартом основного окна.
    */
    public static void main(String[] args) {
        // Просто перенаправляем выполнение в основной класс Main
        Main.main(args);
    }
}