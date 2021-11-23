package com.alex.euclid;

/**
        * Интерфейс Observer.
        * На его основе построен класс представления View.
        * Получает сообщения из класса Model.
        *
        * @author A. Nosov
        * @version 1.0
        */

interface Observer {
    void notification(String message);//получение сообщения

}