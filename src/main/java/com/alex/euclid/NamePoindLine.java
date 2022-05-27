package com.alex.euclid;


import javafx.scene.text.Text;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс NamePoindLine.
 * Предназначен для коллекции NamePoindLine. Предназначен для хранения имен точек, прямых, углов.
 */
@Data
@AllArgsConstructor
public class NamePoindLine {
    private Text text;
    private String id;//имя точка привязки (центр окружности, для отрезка, лучей и прямых центр между этими точками)
    private double dX;//смещение по оси Х от точки привязки
    private double dY;// в мировых координатах
    private double X;//координаты размещения имени мировые
    private double Y;
    private boolean visiblePoind;//показывать имя или нет (по умолчанию true)
    private boolean visibleLine;//показывать имя линий или нет(по умолчанию false)
    private boolean visibleArc;//показывать имя угла или нет (по умолчанию false)
    private String type;//poind - имя точки line -имя линии arc - имя угла
    private String nameShape;//имя объекта, при создании совпадает с text.id, после переименования другое.
}
