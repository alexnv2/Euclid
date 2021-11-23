package com.alex.euclid;

/**
 * Интерфейс Observable.
 * На его основе построен класс Model. Выполняет роль наблюдателя. Все классы отображения (View) должны
 * зарегистрироваться в классе модели (Model).
 *
 * @author A. Nosov
 * @version 1.0
 */

interface Observable {
    void registerObserver(Observer o);//регистрация классов отображения (View)

    void notifyObservers(String message);//Передача сообщений для класса отображения
}