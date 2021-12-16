package com.alex.euclid;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Класс PoindCircle
 * Для коллекции PoindCircle. Предназначен для хранения информации о точках.
 */

public class PoindCircle {
    private Circle circle; //точка
    private String id; //номер
    private double x; //координата x мировая
    private double y; //координата y мировая
    private boolean bMove;// true- разрешено перемещение, false - точка расчетная, перемещение запрещено
    private boolean bSelect;//true - выделена на экране для группового удаления, false - по умолчанию
    private int index; //Счетчик точек которые входят в геометрические фигуры. Точку нельзя удалить, пока индекс не станет равным 0.
    private Line line;//линия к которой принадлежит точка
    private double t;//коэффициент для параметрического уравнения прямой (0.0 по умолчанию)
    private boolean bLine;//true - точка принадлежит прямой (по умолчанию false)

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Circle getCircle() {
        return circle;
    }

    public double getT() {
        return t;
    }

    public boolean isBLine() {
        return bLine;
    }

    public void setLine(Line timeLine) {
        line=timeLine;
    }

    public Line getLine() {
        return line;
    }

    public void setT(double param) {
        t=param;
    }

    public void setBLine(boolean b) {
        bLine=b;
    }

    public void setX(double rX) {
        x=rX;
    }

    public void setY(double rY) {
        y=rY;
    }

    public boolean isBMove() {
        return bMove;
    }


    public static class Builder {
    //Необходимые параметры
    private Circle circle;
    private String id;
    //    Необязательные параметры
    private double x = 0;
    private double y = 0;
    private boolean bMove = true;
    private boolean bSelect = false;
    private int index = 0;
    private Line line = null;
    private double t = 0;
    private boolean bLine = false;

    public Builder(Circle circle, String id) {
        this.circle = circle;
        this.id = id;
    }

    public Builder x(double val) {
        x = val;
        return this;
    }

    public Builder y(double val) {
        y = val;
        return this;
    }

    public Builder bMove(boolean val) {
        bMove = val;
        return this;
    }

    public Builder bSelect(boolean val) {
        bSelect = val;
        return this;
    }

    public Builder index(int val) {
        index = val;
        return this;
    }

    public Builder line(Line val) {
        line = val;
        return this;
    }

    public Builder t(double val) {
        t = val;
        return this;
    }

    public Builder bLine(boolean val) {
        bLine = val;
        return this;
    }

    public PoindCircle build() {
        return new PoindCircle(this);
    }
}
    private PoindCircle(Builder builder){
        circle=builder.circle;
        id=builder.id;
        x=builder.x;
        y=builder.y;
        bMove=builder.bMove;
        bSelect=builder.bSelect;
        index= builder.index;
        line=builder.line;
        t=builder.t;
        bLine= builder.bLine;
    }

}


