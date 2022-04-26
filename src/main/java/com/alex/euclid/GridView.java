package com.alex.euclid;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/**
 * Класс GridView наследует класс WView.
 * Предназначен для вывода сетки и декартовых координат.
 * Наследует класс пересчет мировых координат в окно просмотра.
 */
@EqualsAndHashCode(callSuper = true)
@Data

public class GridView extends WView {
    StackPane Cartesian; //начало стека контейнера
    Pane paneGrid;//контейнер для координатной сетки
    boolean gridShow = true; //вывод сетки
    boolean gridLineShow = true; // вывод координатных осей
    //Конструктор
    GridView() {
    }

    /**
     * Метод gridCartesian().
     * Предназначен для вывода сетки и координатных осей.
     */
    public void gridCartesian() {
        Group group = new Group();
        Group group2 = new Group();
        double mk = getM();//Масштаб
        double rk = getK0() * getK3();
        int z1, z2, z3, z4;
        z1 = (int) (abs(getWr() / mk * 2));
        z2 = (int) (abs(getWl() / mk * 2));
        z3 = (int) (abs(getWt() / mk * 2));
        z4 = (int) (abs(getWb() / mk * 2));
        //ДЛя хранения линий
        Shape[] shapes0 = new Shape[z1];
        Shape[] shapes1 = new Shape[z2];
        Shape[] shapes2 = new Shape[z3];
        Shape[] shapes3 = new Shape[z4];
        //Для текста
        Shape[] shapes4 = new Shape[z3 + z4];
        Shape[] shapes5 = new Shape[z1 + z2];
        //Вертикальная сетка
        for (int i = 0; i < z1; i++) {
            if (gridShow) {
                shapes0[i] = new Line(gridShowX(i), 0, gridShowX(i), getVb());
                shapes0[i].setStrokeWidth(1);
                shapes0[i].setStroke(Color.LIGHTBLUE);
            }
            //Нулевая координата
            if (i == 0 && isGridLineShow()) {
                if (gridShowY(0) <= 0) {
                    shapes5[i] = new Text(gridShowX(0) - 10, getVt() + 12, String.valueOf(0));
                } else if (gridShowY(0) >= getVb() - 12) {
                    shapes5[i] = new Text(gridShowX(0) - 10, getVb(), String.valueOf(0));
                } else {
                    shapes5[i] = new Text(gridShowX(0) - 10, gridShowY(0) + 12, String.valueOf(0));
                }
                group2.getChildren().add(shapes5[i]);
            }
            if (i % 5 == 0) {
                if (gridShow) {
                    shapes0[i].setStrokeWidth(2);
                }
                if (i != 0 && isGridLineShow()) {
                    //Если начало координат выше зоны просмотра
                    if (gridShowY(0) <= 0) {
                        shapes5[i] = new Text(gridShowX(i) - 10, getVt() + 12, doubleString(i * rk));

                        //Если начало координат ниже зоны просмотра
                    } else if (gridShowY(0) >= getVb() - 12) {
                        shapes5[i] = new Text(gridShowX(i) - 10, getVb(), doubleString(i * rk));
                    } else {
                        //В зоне окна просмотра
                        shapes5[i] = new Text(gridShowX(i) - 10, gridShowY(0) + 12, doubleString(i * rk));
                    }
                    group2.getChildren().add(shapes5[i]);
                }
            }
            if (gridShow) {
                group.getChildren().add(shapes0[i]);
            }
        }
        for (int i = 0; i < z2; i++) {
            if (gridShow) {
                shapes1[i] = new Line(gridShowX(-i), 0, gridShowX(-i), getVb());
                shapes1[i].setStrokeWidth(1);
                shapes1[i].setStroke(Color.LIGHTBLUE);
            }
            if (i % 5 == 0) {
                if (gridShow) {
                    shapes1[i].setStrokeWidth(2);
                }
                if (i != 0 && isGridLineShow()) {
                    //Если начало координат левее зоны просмотра
                    if (gridShowY(0) <= 0) {
                        shapes5[i] = new Text(gridShowX(-i) - 8, getVt() + 12, "-" + doubleString(i * rk));
                        //Если начало координат правее зоны просмотра
                    } else if (gridShowY(0) >= getVb() - 12) {
                        shapes5[i] = new Text(gridShowX(-i) - 8, getVb(), "-" + doubleString(i * rk));
                        //В зоне просмотра
                    } else {
                        shapes5[i] = new Text(gridShowX(-i) - 8, gridShowY(0) + 12, "-" + doubleString(i * rk));
                    }
                    group2.getChildren().add(shapes5[i]);
                }
            }
            if (gridShow) {
                group.getChildren().add(shapes1[i]);
            }
        }
        //Горизонтальная сетка положительная
        for (int i = 0; i < z3; i++) {
            if (gridShow) {
                shapes2[i] = new Line(getVl(), gridShowY(i), getVr(), gridShowY(i));
                shapes2[i].setStroke(Color.LIGHTBLUE);
                shapes2[i].setStrokeWidth(1);
            }
            if (i % 5 == 0) {
                if (gridShow) {
                    shapes2[i].setStrokeWidth(2);
                }
                if (i != 0 && isGridLineShow()) {
                    if (gridShowX(0) <= 0) {
                        shapes4[i] = new Text(getVl(), gridShowY(i) + 5, doubleString(i * rk));
                    } else if (gridShowX(0) >= getVr() - 25) {
                        shapes4[i] = new Text(getVr() - 25, gridShowY(i) + 5, doubleString(i * rk));
                    } else {
                        shapes4[i] = new Text(gridShowX(0) + 5, gridShowY(i) + 5, doubleString(i * rk));
                    }
                    group2.getChildren().add(shapes4[i]);
                }
            }
            if (gridShow) {
                group.getChildren().add(shapes2[i]);
            }
        }
        //Горизонтальная сетка отрицательная
        for (int i = 0; i < z4; i++) {
            if (gridShow) {
                shapes3[i] = new Line(getVl(), gridShowY(-i), getVr(), gridShowY(-i));
                shapes3[i].setStroke(Color.LIGHTBLUE);
                shapes3[i].setStrokeWidth(1);
            }
            if (i % 5 == 0) {
                if (gridShow) {
                    shapes3[i].setStrokeWidth(2);
                }
                if (i != 0 && isGridLineShow()) {
                    if (gridShowX(0) <= 0) {
                        shapes4[i] = new Text(getVl(), gridShowY(-i) + 5, "-" + doubleString(i * rk));
                    } else if (gridShowX(0) >= getVr() - 25) {
                        shapes4[i] = new Text(getVr() - 25, gridShowY(-i) + 5, "-" + doubleString(i * rk));
                    } else {
                        shapes4[i] = new Text(gridShowX(0) + 5, gridShowY(-i) + 5, "-" + doubleString(i * rk));
                    }
                    group2.getChildren().add(shapes4[i]);
                }
            }
            if (gridShow) {
                group.getChildren().add(shapes3[i]);
            }
        }
        if (isGridLineShow()) {
            //Абсцисса и ордината
            Line l1 = new Line(getVl(), gridShowY(0), getVr(), gridShowY(0));
            l1.setStroke(Color.BLACK);
            Line l2 = new Line(gridShowX(0), getVt(), gridShowX(0), getVb());
            l1.setStroke(Color.BLACK);
            //Треугольные концы
            Polygon p1 = new Polygon(getVr(), gridShowY(0),
                    getVr() - 10, gridShowY(0) - 4,
                    getVr() - 10, gridShowY(0) + 4);
            Polygon p2 = new Polygon(gridShowX(0), getVt(),
                    gridShowX(0) - 4, getVt() + 10,
                    gridShowX(0) + 4, getVt() + 10);
            group.getChildren().addAll(l1, l2, p1, p2);

        }
        paneGrid.getChildren().addAll(group, group2);
    }

    /**
     * Метод onScrollView(double sc).
     * Предназначен для изменения масштаба координатной сетки.
     *
     * @param sc - количество поворотов колеса мышки
     */
    public void onScrollView(double sc) {
        //double sc=scrollEvent.getDeltaY();//
        setWl(getWl() + (getVr() / sc));//для пропорциональности
        setWr(getWr() - (getVr() / sc));
        setWt(getWt() - (getVb() / sc));
        setWb(getWb() + (getVb() / sc));
        rate();//Перерасчет коэффициентов
        //Увеличить масштаб
        if (getA() < 0.7) {
            switch (getK1()) {
                case 1 -> {
                    setK3(2 * pow(10, getK2()));
                    setK1(2);
                }
                case 2 -> {
                    setK3(5 * pow(10, getK2()));
                    setK1(5);
                }
                case 5 -> {
                    setK2(getK2() + 1);
                    setK3(pow(10, getK2()));
                    setK1(1);
                }
            }
            setWl(-Cartesian.getWidth() / 2);
            setWr(Cartesian.getWidth() / 2);
            setWt(Cartesian.getHeight() / 2);
            setWb(-Cartesian.getHeight() / 2);
        }
        //Уменьшить масштаб
        if (getA() > 1.4) {
            switch (getK1()) {
                case 1 -> {
                    setK2(getK2() - 1);
                    setK3(5 * pow(10, getK2()));
                    setK1(2);
                }
                case 2 -> {
                    setK3(2 * pow(10, getK2()));
                    setK1(5);
                }
                case 5 -> {
                    setK3(pow(10, getK2()));
                    setK1(1);
                }
            }
            //Исходные размеры после изменения масштаба
            setWl(-Cartesian.getWidth() / 2);
            setWr(Cartesian.getWidth() / 2);
            setWt(Cartesian.getHeight() / 2);
            setWb(-Cartesian.getHeight() / 2);
        }
        paneGrid.getChildren().clear();//Очистить экран и память
        gridCartesian();
    }
}

