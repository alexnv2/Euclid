package com.alex.euclid;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс PoindCircle
 * Для коллекции PoindCircle. Предназначен для хранения информации о точках.
 */
@Data
@AllArgsConstructor
public class PoindCircle {

    private Circle circle; //точка
    private String id; //номер
    private Point2D XY;
  //  private double x; //координата x мировая
  //  private double y; //координата y мировая
    private boolean bMove;// true- разрешено перемещение, false - точка расчетная, перемещение запрещено
    private boolean bTreangle;//true - точка принадлежит треугольнику, false - по умолчанию
    private Line line;//линия к которой принадлежит точка
    private double t;//коэффициент для параметрического уравнения прямой (0.0 по умолчанию)
    private boolean bLine;//true - точка принадлежит прямой (по умолчанию false)
    private boolean bCircle;//true - точка принадлежит окружности (по умолчанию false)
    private Circle circleName;//окружность которой принадлежит точка
    private double angle;//угол для точки на окружности
}
