package com.alex.euclid;

import javafx.scene.shape.Polygon;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс TreangleName для коллекции треугольников TreangleName.
 * Предназначен для хранения информации об треугольниках.
 */
@Data
@AllArgsConstructor
public class TreangleName {
    private Polygon polygon;
    private String ID;
}

