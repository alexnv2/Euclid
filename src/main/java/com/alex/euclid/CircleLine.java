package com.alex.euclid;
import javafx.scene.shape.Circle;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс CircleLine предназначен для хранения коллекции CircleLine. Предназначен для хранения информации об
 * окружностях.
 */
@Data
@AllArgsConstructor
public class CircleLine {
    private Circle circle;
    private double x;//мировые координаты центра окружности
    private double y;
    private String id; //имя линии одна буква
    private double radius;//радиус окружности в мировых координатах
    private String poindID;//имя точки центра окружности
}
