package com.alex.euclid;

//Класс перерасчета координат

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.DecimalFormat;

/**
 * Класс WView.
 * Предназначен для перерасчета мировых координат в координаты окна просмотра.
 */
@Data
@AllArgsConstructor
public class WView {
    private double A, B, C, D; //Коэффициенты перерасчета
    private double Wt, Wb, Wl, Wr;//Мировые координаты
    private double Vt, Vb, Vl, Vr;//Координаты окна просмотра
    private double M = 20;//Сетка по умолчанию 20 рх
    private double k0 = 0.2; //Нумерация по умолчанию начинается 1
    private int k1 = 1; //Первая точка входа для масштабирования
    private int k2 = 0;//Степень для масштаба
    private double k3 = 5;//Коэффициент масштабирования
    private double VPx, VPy;//Координаты мыши при нажатии кнопки

    //Конструктор с параметрами
    WView(double Wt, double Wb, double Wl, double Wr, double Vt, double Vb, double Vl, double Vr) {
        this.Wt = Wt;
        this.Wb = Wb;
        this.Wl = Wl;
        this.Wr = Wr;
        this.Vt = Vt;
        this.Vb = Vb;
        this.Vl = Vl;
        this.Vr = Vr;
        //Коэффициенты
        rate();
    }

    //Конструктор по умолчанию
    WView() {
        Wt = 300;
        Wb = -300;
        Wl = -400;
        Wr = 400;
        Vt = 0;
        Vb = 600;
        Vl = 0;
        Vr = 800;
        rate();
    }

    //Сетка х
    double gridShowX(double tx) {
        return ((A * tx * M) + C);
    } //Без масштабирования

    //Сетка Y
    double gridShowY(double ty) {
        return ((B * ty * M) + D);
    }

    /**
     * Метод accessX(double tx).
     * Предназначен для пересчета мировых координат по оси X в координаты экрана в масштабе.
     *
     * @param tx - мировые координаты по оси X
     * @return - возвращает координаты экрана
     */
    double accessX(double tx) {
        return ((A * tx * M) / k3 + C);
    }

    /**
     * Метод accessY(double ty).
     * Предназначен для пересчета мировых координат по оси Y в координаты экрана в масштабе.
     *
     * @param ty - мировые координаты по оси Y
     * @return - возвращает координаты экрана
     */
    double accessY(double ty) {
        return ((B * ty * M) / k3 + D);
    }

    /**
     * Метод revAccessX(double wx).
     * Предназначен для пересчета координат экрана по оси х в мировые с округлением.
     *
     * @param wx - координаты экрана по оси х.
     * @return - возвращает мировые координаты экрана по оси х.
     */

    double revAccessX(double wx) {
        return ((wx - C) / A) / M *  k3;
    }

    /**
     * Метод revAccessY(double wy).
     * Предназначен для пересчета координат экрана по оси y в мировые с округлением.
     *
     * @param wy - координаты экрана по оси y
     * @return - возвращает мировые координаты по оси y
     */
    double revAccessY(double wy) {
        return ((wy - D) / B) / M * k3;
    }

    /**
     * Метод rate().
     * Предназначен для вычисления коэффициентов пересчета координат с масштабом.
     */

    void rate() {
        A = (Vr - Vl) / (Wr - Wl);
        C = Vl - A * Wl;
        B = (Vb - Vt) / (Wb - Wt);
        D = Vb - B * Wb;
    }

    /**
     * Метод doubleString(double d).
     * Предназначен для удаления лишних нулей.
     *
     * @param d - число
     * @return - возвращает число без лишних нулей после запятой.
     */
    public String doubleString(double d) {
        DecimalFormat dF = new DecimalFormat("#.###########");//задаем формат вывода
        return String.valueOf(dF.format(d));//форматируем число
    }
}
