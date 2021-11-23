package com.alex.euclid;


import javafx.scene.shape.Arc;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс VertexArc для коллекции углов VertexArc. Предназначен для хранения данных об углах.
 */
//класс для хранения углов
@Data
@AllArgsConstructor
public class VertexArc {
    private Arc arc;
    private String id;//имя угла, типа АВС
    private String nameAngle;//имя вершины греческие символы
    private double centerX;//центр дуги
    private double centerY;
    private double radiusX;//радиус дуги
    private double radiusY;
    private double startAngle;//Начальный угол
    private double lengthAngle;//длина дуги
    private boolean bSelect;//true- для группового выделения
}
