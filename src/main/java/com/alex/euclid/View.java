package com.alex.euclid;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Класс View расширяет интерфейс Observer.
 * Базовый класс, предназначен отображения информации на экране.
 *
 * @author A. Nosov
 * @version 1.0
 */
class View implements Observer {
    //Объявляем класс Model
    Model model = new Model();
    //Объявляем класс сетки с методами перемещения координатной сетки и масштабирования
    //Как расширение класса пересчета мировых координат в окне просмотра
    GridView gridViews = new GridView();
    //Для всплывающих подсказок
    private final Tooltip tooltip = new Tooltip();

    /**
     * Констриктор класса отображения View.
     */
    View() {
        //Регистрация слушателя в классе Model
        model.registerObserver(this);
        //Свойства сплывающих подсказок
        tooltip.setShowDelay(Duration.millis(10.0));
        tooltip.setFont(Font.font(12));
        tooltip.setStyle("-fx-background-color: LIGHTBLUE;" +
                "-fx-text-fill: black");
    }

    /**
     * Метод notification(String message).
     * Получает сообщения из класса Model о готовности информации к выводу на экран.
     * Метод переопределяется из интерфейса Observer.
     *
     * @param message - какую информацию надо вывести
     */
    @Override
    public void notification(String message) {
        switch (message) {
            case "VertexGo" -> this.vertexGo(model.getVertex());//перемещение вершин
            case "SideGo" -> this.sideGo(model.getLine());//отрисовка сторон отрезков
            case "RayGo" -> this.rayGo(model.getLine());//для луча и прямой
            case "LeftStatusGo" -> this.statusGo(model.getStatus());//вывод статуса
            case "WebGo" -> this.webFileHTMLGo(model.getWebView());//вывод файла HTML
            case "TextShapeGo" -> this.textShapeGo(model.getTextArea());//для вывода в правое окно
            case "FillShape" -> this.FillColor(model.getShapeColor());//цвет заливки
            case "StrokeShape" -> this.SrokeColor(model.getShapeColor());//цвет обводки
            case "ArcGo" -> this.arcGo(model.getArcGo());//дуги для углов и дуг для треугольников
            case "TextGo" -> this.TextGo(model.getTextGo());//буквы
            case "ToolTip" -> this.ToolTipGo(model.getBtnToolTip());//добавить всплывающие подсказки
            case "CircleGo" -> this.CircleGo(model.getCircle());//вывод окружности
            case "StrokeWidth" -> this.LineStrokeWidth(model.getLine());
        }
    }

    /**
     * Метод LineStrokeWidth(Line line).
     * Предназначен для задания толщины линий
     *
     * @param line - объект линия
     */
    private void LineStrokeWidth(Line line) {
        line.setStrokeWidth(model.getLineStokeWidth());
    }

    /**
     * Метод SrokeColor(Line l)
     * Предназначен для изменения цвета обводки.
     *
     * @param sh - объект circle, line, arc
     */
    private void SrokeColor(Shape sh) {
        sh.setStroke(model.getColorStroke());
    }

    /**
     * Метод FillColor(Shape sh)
     * Предназначен для изменения цвета заливки.
     *
     * @param sh - объект circle, line, arc, text
     */
    private void FillColor(Shape sh) {
        sh.setFill(model.getColorFill());
    }

    /**
     * Метод ToolTipGo(Button btnToolTip)
     * Предназначен для вывода всплывающих подсказок
     *
     * @param btnToolTip - объект кнопка на которую наведена мышка
     */
    private void ToolTipGo(Button btnToolTip) {
        tooltip.setText(model.getTextToolTip());//получить текст подсказки
        btnToolTip.setTooltip(tooltip);//вывести подсказку
    }

    /**
     * Метод textShapeGo(TextArea txtArea)
     * Предназначен для вывода в правую часть доски информации по объектам.
     *
     * @param txtArea - объект Текст
     */
    private void textShapeGo(TextArea txtArea) {
        txtArea.setText(model.getTxtShape());
    }

    /**
     * Метод vertexGo(Circle ver)
     * Предназначен для вывода точки на доске при перемещении.
     *
     * @param ver - объект Окружность
     */
    private void vertexGo(Circle ver) {
        ver.  setCenterX(model.getScreenXY().getX());
        ver.setCenterY(model.getScreenXY().getY());
    }

    /**
     * Метод sideGo(Line side)
     * Предназначен для вывода линии на доске.
     *
     * @param side - объект Линия
     */
    private void sideGo(Line side) {
        side.setStartX(model.getSegmentStartX());
        side.setStartY(model.getSegmentStartY());
        side.setEndX(model.getScreenXY().getX());
        side.setEndY(model.getScreenXY().getY());
    }

    /**
     * Метод rayGo(Line ray)
     * Предназначен для вывода лучей и прямых на доске.
     *
     * @param ray - объект Линия
     */
    private void rayGo(Line ray) {
        ray.setStartX(model.getRayStartX());
        ray.setStartY(model.getRayStartY());
        ray.setEndX(model.getRayEndX());
        ray.setEndY(model.getRayEndY());
    }


    /**
     * Метод webFileHTMLGo(WebView web)
     * Предназначен для вывода в левую часть доски файлов справочной информации в формате html.
     *
     * @param web - объект WebView
     */
    private void webFileHTMLGo(WebView web) {
        web.setContextMenuEnabled(false);
        WebEngine w = web.getEngine();
        w.load(Objects.requireNonNull(getClass().getResource(model.getLeftHTML())).toString());
    }

    /**
     * Метод statusGo(Label label)
     * Предназначен для вывода подсказок в статусной строке.
     *
     * @param label - объект Label
     */
    private void statusGo(Label label) {
        label.setText(model.getStringLeftStatus());
    }


    /**
     * Метод arcGo(Arc arc)
     * Предназначен для вывода углов на доске.
     *
     * @param arc - объект Arc
     */
    private void arcGo(Arc arc) {
        arc.setCenterX(model.getScreenXY().getX());
        arc.setCenterY(model.getScreenXY().getY());
        arc.setRadiusX(model.getArcRadius());
        arc.setRadiusY(model.getArcRadius());
        arc.setStartAngle(model.getAngleStart());
        arc.setLength(model.getAngleLength());
    }

    /**
     * Метод TextGo(Text t).
     * Предназначен для выводов имен точек, прямых, отрезков
     *
     * @param t - объект текст
     */
    private void TextGo(Text t) {
        t.setX(model.getTextX());
        t.setY(model.getTextY());
    }

    /**
     * Метод CircleGo(Circle c).
     * Предназначен для вывода на экран окружности.
     *
     * @param c объект окружность
     */
    private void CircleGo(Circle c) {
        c.setCenterX(model.getSegmentStartX());
        c.setCenterY(model.getSegmentStartY());
        c.setRadius(model.getRadiusCircle());
    }
}
