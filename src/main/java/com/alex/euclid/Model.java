package com.alex.euclid;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import lombok.Data;

import java.text.MessageFormat;
import java.util.*;

import static ContstantString.StringStatus.*;
import static java.lang.Math.abs;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;


/**
 * Класс Model расширение интерфейса Observable
 * Класс Модели, получает запросы на отработку событий от контроллера и
 * выставляет переменные и отправляет сообщения классу отображения (View),
 * который выводит эти данные на экран.
 *
 * @author A. Nosov
 * @version 1.0
 */
@Data
//Класс модель для расчета и выдачи информации для представления
class Model implements Observable {

    //Переменные класса
    private Circle circle;//окружность
    private Circle vertex; //точка
    private Circle circleFill;//увеличенная точка при наведении
    private Line line;//линия, луч, прямая
    private Shape shapeColor;//графический объект для задания цвета, точка, линия, арка
    private Line newLine;//луча, отрезка, прямой для построения и при наведении на линию
    private Label Status;//левый статус, вывод действий
    private Label rightStatus;//правый статус, вывод координат
    private Text textGo;//Для наименования точек, отрезков, прямых и т.д
    private WebView webView; //браузер в левой части доски
    private TextArea textArea;//получаем ссылку на правую часть доски для вывода информации о фигурах
    private Pane paneBoards;//получаем ссылку на доску, где размещены объекты
    private GridView gridViews;//сетка
    private Arc arcGo;//дуга угла
    private Button btnToolTip;//ссылка на кнопку
    private String textToolTip;//текст при наведении на кнопку

    private String stringWebView;//text left
    private String stringLeftStatus;//для хранения и передачи в View статусных сообщений
    private String leftHTML;//хранит адрес файла HTML из папки Web для передачи в View
    private String txtShape = "";//хранит строку о геометрической фигуре на доске
    //Вершины треугольника при построении
    private Circle vertexTr1;
    private Circle vertexTr2;
    private Circle vertexTr3;
    private int colVertex;//Количество вершин для многоугольника, 3-для треугольника, 4- для четырехугольника

    //координаты
    private Point2D screenXY;
    private double decartX;//координата X мировая на доске, зависят от мышки
    private double decartY;//координата Y мировая на доске, зависят от мышки
    private double segmentStartX;//координата StartX для отрезков
    private double segmentStartY;//Координата StartY для отрезков
    private double rayStartX;//координаты экрана для луча и прямой StartX
    private double rayStartY;//координаты экрана для луча и прямой StartY
    private double rayEndX;//координаты экрана для луча и прямой EndX
    private double rayEndY;//координаты экрана для луча и прямой EndY
    private double verLineStartX;//координата X мировая для Line StartX
    private double verLineStartY;//координата Y мировая для Line StartY
    private double verLineEndX;//координата X мировая для Line EndX
    private double verLineEndY;//координата Y мировая для Line EndY
    private double textX;//координата Х для имен точек
    private double textY;//координата Y для имен точек
    private double dXStart; //смещение по х от нажатой мышки до начала линии для её перемещения
    private double dYStart;
    private double dXEnd;//смещение по х от нажатой мышки до конца линии для её перемещения
    private double dYEnd;

    //Для временного хранения точек и линий при выборе объектов.
    private Circle timeVer;//для временного хранения выбранных вершин
    private Line timeLine;// для временного хранения выбранной линии
    private Circle timeCircle;//для временного хранения окружности
    private Polygon timeTreangle;//для временного хранения треугольников

    //Индексы
    private String indexPoind = "A";//Индекс для точек
    private int indexPoindInt = 0;
    private String indexLine = "a";//Индекс для прямых, отрезков, лучей
    private int indexLineInt = 0;
    private char indexAngle = '\u03b1';//Индекс для углов, начинается с альфа
    private int indexAngleInt = 0;

    private boolean poindOldAdd = false;//true - Берем существующие точки для построения фигур
    private boolean poindOld = false;//true - Если точка на дистанции 25рх от конца линии
    private boolean lineOldAdd = false;//true - Берем существующую линию для построения фигур
    private boolean poindLineAdd1 = false;//true - добавление точки на линию
    private boolean poindLineAdd2 = false;//true - добавление точки на линию
    private boolean treangleVisibly = false;//true - кнопки построить биссектрису, медиану, высоту видимы, т.к. есть уже треугольник

    private boolean poindOne = false;//true - первая точка отрезка построена
    private boolean poindTwo = false;//true - построение второй точки
    private boolean poindThree = false;//true - построение третьей точки
    boolean poindAddVertical = false;//для построения перпендикуляра, выбор точки
    boolean lineAddVertical = false;//выбор линии
    private boolean poindAddVParallel;//для построения параллельных прямых, выбор точки
    private boolean lineAddParallel;//выбор линии
    private boolean circleAddPoind = false;//окружность для добавления точки

    //режимы создания
    //Режим создания: 0- выход, 1- точка, 2-отрезок, 3- прямая, 4-луч, 5-угол, 6-перпендикуляр
    //7-параллельная прямая, 8-треугольник, 9-медиана, 10-высота, 11-биссектриса, 12-середина отрезка
    //14-окружность
    private int createGeometric = 0;
    StringBuilder newSegment = new StringBuilder();//для временного хранения вершин при построении фигур
    private int segmentLength = 0;//Для очистки переменной.

    //Свойства углов
    private double arcRadius = 30;//радиус дуги
    private double angleStart;//начало дуги гр.
    private double angleLength;//длина дуги гр.
    private Color colorFill = Color.hsb(195, 0.53, 0.78, 1);//цвет заливки
    private Color colorStroke = Color.hsb(216, 1, 0.62, 1); //цвет обводки
    public String[][] tableColor = new String[3][10];
    private double lineStokeWidth = 2;//толщина линий
    private double radiusPoind = 5;//радиус точки
    //Логические переменные из меню настроек
    private boolean showPoindName = true;//по умолчанию, всегда показывать имена точек
    private boolean showLineName = false;//по умолчанию, не показывать имена линий
    private boolean showDate = false;//по умолчанию, не показывать данные объектов на доске
    private boolean showGrid = true;//по умолчанию, показывать сетку
    private boolean showCartesian = true;//по умолчанию, показывать координатную ось
    private boolean showAngleName = false;//по умолчанию, не показывать имя углов

    /**
     * Логическая переменная createShape.
     * Задает глобальный режим построения всех фигур, блокирует
     * возможность случайного перемещения фигуры при построении.
     * Задается контролером при построении, после построения устанавливает false.
     * Используется при подключении к фигуре свойств мышки.
     * Блокирует режим перемещения фигуры.
     */
    private boolean createShape = false;//true-режим создания фигуры
    private double t;//для параметрической прямой, когда точка принадлежит прямой

    private double radiusCircle;//радиус окружности, для View
    private double radiusCircleW;//радиус окружности в мировых координатах


    //Коллекции
    private LinkedList<PoindCircle> poindCircles = new LinkedList<>();//коллекция для точек по классу
    private LinkedList<PoindLine> poindLines = new LinkedList<>();//коллекция для линий по классу
    private LinkedList<VertexArc> vertexArcs = new LinkedList<>();//коллекция для арок углов
    private ArrayList<Double> arrDash = new ArrayList<>();//массив для создания вида строк
    private LinkedList<NamePoindLine> namePoindLines = new LinkedList<>();//коллекция для имен
    private LinkedList<TreangleName> treangleNames = new LinkedList<>();//коллекция треугольников
    private LinkedList<CircleLine> circleLines = new LinkedList<>();//коллекция окружностей
    private Stack<Circle> circleStack = new Stack<>();//стек для точек при построении треугольников и углов

    //Определяем связанный список для регистрации классов слушателей
    private LinkedList<Observer> observers = new LinkedList<>();

    //Переменные для передачи в другой контроллер
    public void setWindShow(int w) {
        WIND_SHOW = w;
    }

    public int getWindShow() {
        return WIND_SHOW;
    }

    /**
     * Конструктор класса без переменных
     */
    Model() {
    }

    /**
     * Метод registerObserver(Observer o)
     * Метод регистрации слушателя, переопределяем функцию интерфейса
     *
     * @param o - объект слушатель, добавляем в коллекцию слушателей
     */
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    /**
     * Метод notifyObservers(String message)
     * Уведомление слушателя, переопределяем функцию интерфейса.
     *
     * @param message - сообщение для слушателя
     */
    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.notification(message);
        }
    }

    /**
     * Метод initIndex()
     * Инициализация переменных после очистки доски.
     */
    public void initIndex() {
        setIndexPoind("A");
        setIndexPoindInt(0);
        setIndexLine("a");
        setIndexLineInt(0);
        setIndexAngle('\u03b1');
        setIndexAngleInt(0);
    }

    /**
     * Метод indexPoindAdd().
     * Предназначен для увелечения индекса в названии точки.
     */
    private String indexPoindAdd() {
        String s;
        if (indexPoindInt > 0) {
            s = indexPoind + indexPoindInt;
        } else {
            s = indexPoind;
        }
        char[] chars = indexPoind.toCharArray();
        if (String.valueOf(chars[0]).equals("Z")) {
            indexPoind = "A";
            indexPoindInt += 1;
        } else {
            chars[0] += 1;
            indexPoind = String.valueOf(chars[0]);
        }
        return s;
    }

    /**
     * Метод indexLinedAdd().
     * Предназначен для увелечения индекса в названии прямых, отрезков, лучей.
     */
    private String indexLineAdd() {
        String s;
        if (indexLineInt > 0) {
            s = indexLine + indexLineInt;
        } else {
            s = indexLine;
        }
        char[] chars = indexLine.toCharArray();
        if (String.valueOf(chars[0]).equals("z")) {
            indexLine = "a";
            indexLineInt += 1;
        } else {
            chars[0] += 1;
            indexLine = String.valueOf(chars[0]);
        }
        return s;
    }

    /**
     * Метод indexAngledAdd()
     * Предназначен для задания имени угла греческим алфавитом
     *
     * @return - имя
     */
    private String indexAngledAdd() {
        String s;
        if (indexAngleInt > 0) {
            s = indexAngle + String.valueOf(indexAngleInt);
        } else {
            s = String.valueOf(indexAngle);
        }
        indexAngle++;
        if (indexAngle == '\u03ca') {
            indexAngle = '\u03b1';
            indexAngleInt++;
        }
        return s;
    }

    /**
     * Метод WebHTML()
     * Предназначен для вывода справочной информации в левую часть доски.
     *
     * @param o    - ссылка на объект WebView
     * @param file - имя файла html.
     */
    public void webHTML(WebView o, String file) {
        leftHTML = "/com/alex/euclid/Web/" + file;
        //Передать в View для вывода
        webView = o;
        notifyObservers("WebGo");
    }


    /**
     * Метод nameSplitRemove(String s).
     * Предназначен для удаления символа разделителя в именах названий точек, прямых и т.д.
     *
     * @param s - строка вида А_В (отрезок АВ).
     * @return - возвращает строку АВ.
     */
    StringBuilder nameSplitRemove(String s) {
        String[] name = s.split("_");
        StringBuilder sb = new StringBuilder();
        for (String n : name) {
            sb.append(n);
        }
        return sb;
    }

    /**
     * Метод txtAreaOutput()
     * Предназначен для выборки из коллекций объектов информацию о геометрических фигурах
     * и выводе в правой части доски.
     */
    public void txtAreaOutput() {
        //Информация о точках
        for (PoindCircle p : poindCircles) {
            if (p.getCircle() != null) {
                String s1 = p.getId();
                double s2 = p.getXY().getX();
                double s3 = p.getXY().getY();
                txtShape = MessageFormat.format("{0}Точка: {1} ({2,number, #.#}, {3,number, #.#})\n", txtShape, s1, s2, s3);
            }
        }
        //Информация об отрезках, лучах и прямых
        for (PoindLine p : poindLines) {
            if (p.getLine() != null) {
                int l = p.getSegment();
                switch (l) {
                    case 0 -> {
                        double lengthSegment = distance(p.getStX(), p.getStY(), p.getEnX(), p.getEnY());
                        txtShape = MessageFormat.format("{0}{1}{2} Длина:{3,number, #.#}\n", txtShape, STA_10, nameSplitRemove(p.getId()), lengthSegment);
                    }
                    case 1 ->
                            txtShape = txtShape + STA_11 + p.getLine().getId() + " или " + nameSplitRemove(p.getId()) + "\n";
                    case 2 ->
                            txtShape = txtShape + STA_12 + p.getLine().getId() + " или " + nameSplitRemove(p.getId()) + "\n";
                    case 3 -> {
                        double lengthSegment = Math.round(distance(p.getStX(), p.getStY(), p.getEnX(), p.getEnY()) * 100);
                        txtShape = txtShape + STA_17 + nameSplitRemove(p.getId()) + " Длина:" + lengthSegment / 100 + "\n";
                    }
                    case 4 -> txtShape = txtShape + STA_20 + nameSplitRemove(p.getId()) + "\n";
                    case 5 -> txtShape = txtShape + STA_23 + nameSplitRemove(p.getId()) + "\n";
                    case 6 -> txtShape = txtShape + STA_25 + nameSplitRemove(p.getId()) + "\n";
                    case 7 -> txtShape = txtShape + STA_27 + nameSplitRemove(p.getId()) + "\n";
                    case 8 -> txtShape = txtShape + STA_35 + nameSplitRemove(p.getId()) + "\n";
                    case 9 -> txtShape = txtShape + STA_36 + nameSplitRemove(p.getId()) + "\n";

                }
            }
        }

        //Информация об углах
        for (VertexArc v : vertexArcs) {
            if (v != null) {
                txtShape = MessageFormat.format("{0}Угол {1}= {2} гр. \n", txtShape, nameSplitRemove(v.getId()), v.getLengthAngle());
            }
        }
        //Информация об треугольниках
        for (TreangleName t : treangleNames) {
            if (t != null) {
                txtShape = MessageFormat.format("{0}{1}{2} \n", txtShape, STA_21, nameSplitRemove(t.getID()));
            }
        }
        //Информация об окружностях
        for (CircleLine p : circleLines)
            if (p != null) {
                txtShape = MessageFormat.format("{0}{1}{2}{3}{4,number, #.#} \n", txtShape, "Окружность: ", p.getId(), ", R=", p.getRadius());
            }
        //Передать в View для вывода
        notifyObservers("TextShapeGo");
    }

    /**
     * Метод createGeometrics().
     * Предназначен для построения геометрических фигур.
     * Переменная createGeometric - определяет режим построения
     * 0-сбросить режим, 1- точка, 2- отрезок, 3-прямая, 4-луч, 5-угол
     * 6- перпендикуляр, 7 - параллельные прямые, 8-треугольник
     * 9- медиана 10-высота, 11-биссектриса, 12-середина отрезка, 14-окружность
     * 15-касательная к окружности
     */
    public void createGeometrics() {
        switch (createGeometric) {
            case 1 -> {
                createPoindAdd(true);
                createGeometric = 0;
            }
            case 2 -> {
                newPoindShape();
                if (isPoindTwo()) {
                    newPoindTwoShape(0);
                    newPoindShapeEnd();
                }
            }
            case 3 -> {
                newPoindShape();
                if (isPoindTwo()) {
                    newPoindTwoShape(2);
                    newPoindShapeEnd();
                }
            }
            case 4 -> {
                newPoindShape();
                if (isPoindTwo()) {
                    newPoindTwoShape(1);
                    newPoindShapeEnd();
                }
            }
            case 5 -> {
                vertexTr1 = newCirclePoind();
                newSegment.append(vertexTr1.getId());
                newSegment.append("_");
                circleStack.push(vertexTr1);
                setColVertex(getColVertex() - 1);
                if (getColVertex() == 0) {
                    vertexTr3 = circleStack.pop();
                    vertexTr2 = circleStack.pop();
                    vertexTr1 = circleStack.pop();
                    newSegment.delete(newSegment.length() - 1, newSegment.length());//удалить последнее _
                    Arc arcAngle = createVertexAdd(vertexTr1, vertexTr2, vertexTr3, newSegment.toString());
                    paneBoards.getChildren().add(arcAngle);//рисуем арку дуги
                    arcAngle.toBack();//перемещать узел вниз только после добавления на стол
                    //Связываем арку с углом и именем
                    arcBindPoind(vertexTr1, vertexTr2, vertexTr3, arcAngle);
                    //закончить построение
                    setCreateGeometric(0);
                    setSegmentLength(0);
                    newSegment.delete(0, newSegment.length());//очистить строку
                }
            }
            case 6 -> {
                if (isPoindOldAdd()) {
                    poindAddVertical = true;
                    newSegment.append(getTimeVer().getId());
                    newSegment.append("_");
                }
                if (isLineOldAdd()) {
                    line = getTimeLine();//получаем прямую к которой надо опустить перпендикуляр
                    lineAddVertical = true;
                }
                //Опускаем перпендикуляр, не важно что выбрано первым
                if (poindAddVertical && lineAddVertical) {
                    String[] nameLine = findID(line).split("_");//получить имя отрезка по имени прямой
                    Point2D A1 = new Point2D(getTimeVer().getCenterX(), getTimeVer().getCenterY());
                    Point2D B1 = new Point2D(line.getStartX(), line.getStartY());
                    Point2D C1 = new Point2D(line.getEndX(), line.getEndY());
                    Point2D D1 = heightPoind(A1, B1, C1);//координаты точки пересечения
                    Line lP = createLineAdd(7);//Создание перпендикуляра
                    //Задаем координаты
                    setSegmentStartX(getTimeVer().getCenterX());
                    setSegmentStartY(getTimeVer().getCenterY());
                    setScreenXY(new Point2D(D1.getX(), D1.getY()));
                    closeLine(lP);//запрет на перемещение
                    //Передать в View для вывода
                    setLine(lP);
                    notifyObservers("SideGo");
                    //Привязать события мыши
                    mouseLine(lP);
                    //Переводим координаты линии в мировые
                    findLinesUpdateXY(lP.getId());
                    //Переводим в мировые координаты точки
                    setDecartX(gridViews.revAccessX(D1.getX()));
                    setDecartY(gridViews.revAccessY(D1.getY()));
                    //Создаем расчетную точку не перемещаемую
                    Circle newPoind = createPoindAdd(false);//создать точку
                    newSegment.append(newPoind.getId());
                    //Обновить мировые координаты коллекции
                    findCirclesUpdateXY(newPoind.getId(), gridViews.revAccessX(newPoind.getCenterX()), gridViews.revAccessY(newPoind.getCenterY()));
                    //Заменить имя прямой на имя отрезка
                    findNameId(newSegment.toString(), lP.getId());
                    setTxtShape("");
                    txtAreaOutput();
                    //связать точки и перпендикуляр для перемещения
                    verticalBindCircles(getTimeVer(), findCircle(nameLine[0]), findCircle(nameLine[1]), newPoind, lP);
                    poindAddVertical = false;
                    lineAddVertical = false;
                    //закрыть режим создания перпендикуляра
                    newSegment.delete(0, newSegment.length());//очистить строку
                    createGeometric = 0;
                }
            }
            //Параллельная прямая
            case 7 -> {
                if (isPoindOldAdd()) {
                    // получаем точку через которую надо провести параллельную прямую
                    newSegment.append(getTimeVer().getId());
                    newSegment.append("_");
                    poindAddVParallel = true;
                }
                if (isLineOldAdd()) {
                    lineAddParallel = true;
                }
                if (poindAddVParallel && lineAddParallel) {
                    //получить имя отрезка по имени прямой
                    String[] nameLine = findID(getTimeLine()).split("_");
                    //Рассчитать координаты новой точки
                    double a = findCircle(nameLine[1]).getCenterY() - findCircle(nameLine[0]).getCenterY();
                    double b = findCircle(nameLine[0]).getCenterX() - findCircle(nameLine[1]).getCenterX();
                    double x = 4000;
                    double y;
                    if (b != 0) {
                        y = (a * (timeVer.getCenterX() - 4000) + b * timeVer.getCenterY()) / b;
                    } else {
                        y = a * (timeVer.getCenterX() - 4000);
                    }
                    //Создать новую точку
                    setScreenXY(new Point2D(x, y));
                    Circle newPoind = createPoindAdd(false);
                    newSegment.append(newPoind.getId());
                    //Обновить мировые координаты коллекции
                    findCirclesUpdateXY(newPoind.getId(), gridViews.revAccessX(newPoind.getCenterX()), gridViews.revAccessY(newPoind.getCenterY()));
                    //Вывести на доску
                    setVertex(newPoind);
                    notifyObservers("VertexGo");
                    //Создать параллельную прямую
                    Line parallelLine = createLineAdd(9);
                    setSegmentStartX(timeVer.getCenterX());
                    setSegmentStartY(timeVer.getCenterY());
                    setScreenXY(new Point2D(x, y));
                    createMoveLine(parallelLine, 3);
                    newLine.setVisible(false);//Нужна, иначе появится вторая прямая на линии
                    parallelLine.toBack();
                    //Обновить мировые координаты
                    findLinesUpdateXY(parallelLine.getId());
                    //Привязка свойств мышки
                    mouseLine(parallelLine);
                    //Заменить имя
                    findNameId(newSegment.toString(), parallelLine.getId());
                    //Добавить имя
                    nameLineAdd(parallelLine);
                    //Связать точки с прямой
                    circlesBindLine(getTimeVer(), newPoind, parallelLine);
                    //Связать параллельные прямые
                    parallelBindLine(findCircle(nameLine[0]), findCircle(nameLine[1]), getTimeVer(), newPoind);
                    //Вывод информации об объектах в правую часть доски
                    setTxtShape("");
                    txtAreaOutput();
                    //закрыть режим создания параллельных прямых
                    newSegment.delete(0, newSegment.length());//очистить строку
                    poindAddVParallel = false;
                    lineAddParallel = false;
                    createGeometric = 0;
                }
            }
            //Добавить треугольник
            case 8 -> {
                vertexTr1 = newCirclePoind();
                setSegmentStartX(vertexTr1.getCenterX());
                setSegmentStartY(vertexTr1.getCenterY());
                setPoindOne(true);
                newSegment.append(vertexTr1.getId());
                newSegment.append("_");
                circleStack.push(vertexTr1);
                if (poindTwo) {
                    Line l1 = createLineAdd(3);
                    setSegmentStartX(newLine.getStartX());
                    setSegmentStartY(newLine.getStartY());
                    setScreenXY(new Point2D(newLine.getEndX(), newLine.getEndY()));
                    setLine(l1);
                    l1.toBack();
                    //заменить имя отрезка
                    findNameId(newSegment.substring(segmentLength, segmentLength + 3), l1.getId());
                    segmentLength += 2;
                    notifyObservers("SideGo");
                    setPoindTwo(false);
                    setColVertex(getColVertex() - 1);
                    setSegmentStartX(newLine.getEndX());
                    setSegmentStartY(newLine.getEndY());
                }
                if (getColVertex() == 1) {
                    vertexTr3 = circleStack.pop();
                    vertexTr2 = circleStack.pop();
                    vertexTr1 = circleStack.pop();
                    Line l1 = createLineAdd(3);
                    setSegmentStartX(vertexTr1.getCenterX());
                    setSegmentStartY(vertexTr1.getCenterY());
                    setScreenXY(new Point2D(newLine.getEndX(), newLine.getEndY()));
                    newLine.setVisible(false);
                    setLine(l1);
                    l1.toBack();
                    //заменить имя отрезка
                    findNameId(vertexTr1.getId() + "_" + vertexTr3.getId(), l1.getId());
                    notifyObservers("SideGo");
                    newSegment.delete(newSegment.length() - 1, newSegment.length());//удалить последнее _
                    //связать имена вершин с линиями
                    lineBindCircles(vertexTr1, vertexTr2, findLineVertex(vertexTr1.getId() + "_" + vertexTr2.getId()));
                    lineBindCircles(vertexTr2, vertexTr3, findLineVertex(vertexTr2.getId() + "_" + vertexTr3.getId()));
                    lineBindCircles(vertexTr1, vertexTr3, findLineVertex(vertexTr1.getId() + "_" + vertexTr3.getId()));
                    //Добавить многоугольник в форме треугольника, добавить треугольник в коллекцию
                    Polygon t = treangleAdd(vertexTr1, vertexTr2, vertexTr3, newSegment.toString());
                    paneBoards.getChildren().add(t);
                    //   t.toBack();
                    //Внести в коллекцию точек, что точки являются вершинами треугольника
                    vertexTreangle(vertexTr1.getId(), vertexTr2.getId(), vertexTr3.getId());
                    //закончить построение
                    newSegment.delete(0, newSegment.length());//очистить строку
                    setCreateGeometric(0);
                    setSegmentLength(0);
                    setPoindThree(false);
                    setPoindOne(false);
                    setPoindTwo(false);
                    setTreangleVisibly(true);//разблокировать кнопки высота, медиана, биссектриса
                }
            }
            //Медиана
            case 9 -> {
                Circle cMediana = getTimeVer();
                if (findVertexTreangle(cMediana.getId())) {
                    line = mbhLineAdd(cMediana, 4);
                    line.toFront();
                    mouseLine(line);//привязка событий мыши
                    closeLine(line);//запрет на перемещение
                    createGeometric = 0;
                } else {
                    setStringLeftStatus(STA_37);
                    notifyObservers("LeftStatusGo");
                }
            }
            //Высота
            case 10 -> {
                Circle cHeight = getTimeVer();
                if (findVertexTreangle(cHeight.getId())) {
                    line = mbhLineAdd(cHeight, 6);
                    line.toFront();
                    mouseLine(line);//привязка событий мыши
                    closeLine(line);//запрет на перемещение
                    createGeometric = 0;
                } else {
                    setStringLeftStatus(STA_37);
                    notifyObservers("LeftStatusGo");
                }
            }
            //Биссектриса
            case 11 -> {
                Circle cBisector = getTimeVer();
                if (findVertexTreangle(cBisector.getId())) {
                    line = mbhLineAdd(cBisector, 5);
                    line.toFront();
                    mouseLine(line);//привязка событий мыши
                    closeLine(line);//запрет на перемещение
                    createGeometric = 0;
                } else {
                    setStringLeftStatus(STA_37);
                    notifyObservers("LeftStatusGo");
                }
            }
            case 12 -> {
                // Построение середины отрезка
                Line l = getTimeLine();//получаем отрезок, для которого надо построить середину
                //проверить, является ли данная линия отрезком, исключаем прямые и лучи
                if (findTypeLine(l) == 0) {
                    String[] namePoind = findID(l).split("_");//получить имена точек отрезка
                    Circle c1 = findCircle(namePoind[0]);
                    Circle c2 = findCircle(namePoind[1]);
                    Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
                    Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
                    Point2D poindMiddle = p1.midpoint(p2);//получили координаты середины отрезка
                    Circle middleCircle = createPoindAdd(false);
                    setScreenXY(new Point2D(poindMiddle.getX(), poindMiddle.getY()));
                    notifyObservers("VertexGo");//вывести на экран
                    //Связать полученную точку с линией
                    middleBindSegment(middleCircle, l);

                } else {
                    setStringLeftStatus(STA_32);
                    notifyObservers("LeftStatusGo");
                }
                createGeometric = 0;
            }
            //Построение окружности
            case 14 -> {
                //Взять существующую или создать новую точку для прямой
                if (!isPoindTwo()) {
                    vertex = newCirclePoind();
                    //Сохранить координаты центра окружности
                    setSegmentStartX(vertex.getCenterX());
                    setSegmentStartY(vertex.getCenterY());
                    //создать новую окружность
                    circle = createCircleAdd(vertex);
                    setPoindOne(true);
                }
                if (isPoindTwo()) {
                    updateCircle(circle, vertex);
                    setTxtShape("");
                    txtAreaOutput();
                    createGeometric = 0;
                    setPoindOne(false);
                    setPoindTwo(false);
                }
            }
            //Построение касательной
            case 15 -> {
                vertex = getTimeVer();
                //Проверить, принадлежит ли точка окружности
                if (findBCircle(vertex.getId())) {
                    circleStack.push(vertex);
                    newSegment.append(vertex.getId());
                    newSegment.append("_");
                    //Рассчитать координаты второй точки касательной
                    Circle circle1 = findCirclePoind(vertex.getId());
                    assert circle1 != null;
                    Point2D a = new Point2D(gridViews.revAccessX(circle1.getCenterX()), gridViews.revAccessY(circle1.getCenterY()));
                    Point2D b = new Point2D(gridViews.revAccessX(vertex.getCenterX()), gridViews.revAccessY(vertex.getCenterY()));
                    double r = distance(a.getX(), a.getY(), b.getX(), b.getY());
                    Point2D pd = tangentCircle(a, b, r);
                    //Построить точку
                    Circle newPoind = createPoindAdd(false);
                    setScreenXY(new Point2D(gridViews.accessX(pd.getX()), gridViews.accessY(pd.getY())));
                    newSegment.append(newPoind.getId());
                    //Обновить мировые координаты коллекции
                    findCirclesUpdateXY(newPoind.getId(), pd.getX(), pd.getY());
                    //Вывести на доску
                    setVertex(newPoind);
                    notifyObservers("VertexGo");
                    //построить касательную
                    Line tangentLine = createLineAdd(8);
                    Circle c = circleStack.pop();
                    //задать координаты прямой
                    setRayStartX(c.getCenterX() + (newPoind.getCenterX() - c.getCenterX()) * 3);
                    setRayStartY(c.getCenterY() + (newPoind.getCenterY() - c.getCenterY()) * 3);
                    setRayEndX(c.getCenterX() + (newPoind.getCenterX() - c.getCenterX()) * -3);
                    setRayEndY(c.getCenterY() + (newPoind.getCenterY() - c.getCenterY()) * -3);
                    //Передать в View для вывода
                    setLine(tangentLine);
                    notifyObservers("RayGo");
                    tangentLine.toBack();
                    //Обновить мировые координаты
                    findLinesUpdateXY(tangentLine.getId());
                    //Заменить имя
                    findNameId(newSegment.toString(), tangentLine.getId());
                    //Привязка свойств мышки
                    mouseLine(tangentLine);
                    //Добавить имя
                    nameLineAdd(tangentLine);
                    //Связать касательную с окружностью, последовательность связывания такая,
                    //иначе неправильно работает.
                    tangentBindCircles(circle1, c, newPoind, tangentLine);
                    //Связать прямую с точками
                    circlesBindLine(newPoind, c, tangentLine);
                    //Закончить построение
                    //Вывод информации об объектах в правую часть доски
                    setTxtShape("");
                    txtAreaOutput();
                    newSegment.delete(0, newSegment.length());//очистить строку
                    createGeometric = 0;
                } else {
                    setStringLeftStatus(STA_34);
                    notifyObservers("LeftStatusGo");
                }
            }

            //Вписанная окружность
            case 16 -> {
                //Выбрать вершины треугольника
                String[] ver = timeTreangle.getId().split("_");
                Circle v1 = findCircle(ver[0]);
                Circle v2 = findCircle(ver[1]);
                Circle v3 = findCircle(ver[2]);
                Circle c1 = createPoindAdd(false);//центр вписанной окружности
                Circle c2 = createCircleAdd(c1);//вписанная окружность
                //Создать вписанную окружность
                accessInCircle(v1, v2, v3, c2, c1);
                updateCircle(c1, c2);
                //Привязать окружность к треугольнику
                poindBindInTriangleCircle(v1, v2, v3, c2, c1);
                setTxtShape("");
                txtAreaOutput();
                createGeometric = 0;
            }
            // Описанная окружность
            case 17 -> {
                //Выбрать вершины треугольника
                String[] ver = timeTreangle.getId().split("_");
                Circle v1 = findCircle(ver[0]);
                Circle v2 = findCircle(ver[1]);
                Circle v3 = findCircle(ver[2]);
                //Выбрать координаты точек
                Point2D v1XY = new Point2D(v1.getCenterX(), v1.getCenterY());
                Point2D v2XY = new Point2D(v2.getCenterX(), v2.getCenterY());
                Point2D v3XY = new Point2D(v3.getCenterX(), v3.getCenterY());
                //Рассчитать центр окружности
                Point2D circleXY = middlePerpendicular(v1XY, v2XY, v3XY);
                double radius = radiusOutCircle(v1XY, v2XY, v3XY);
                //создать центр окружности
                Circle c1 = createPoindAdd(true);//центр окружности
                Circle c2 = createCircleAdd(c1);// сама окружность
                vertex = c1;
                setScreenXY(new Point2D(circleXY.getX(), circleXY.getY()));
                notifyObservers("VertexGo");
                circle = c2;
                setSegmentStartX(c1.getCenterX());
                setSegmentStartY(c1.getCenterY());
                setRadiusCircle(radius);
                setRadiusCircleW(accessRadiusW(new Point2D(getSegmentStartX(), getSegmentStartY()), radius));
                notifyObservers("CircleGo");
                //Присвоить вершины треугольника окружности
                vertexInCircle(v1.getId(), v2.getId(), v3.getId(), circle);
                updateCircle(circle, vertex);
                setTxtShape("");
                txtAreaOutput();
                createGeometric = 0;
            }
        }
    }

    /**
     * Метод poindBindInTriangleCircle(Circle v1, Circle v2, Circle v3, Circle circle).
     * Предназначен для связывания вершин треугольника с вписанной окружностью.
     *
     * @param v1  - первая вершина
     * @param v2  - вторая вершина
     * @param v3  - третья вершина
     * @param c1  -вписанная окружность
     * @param c2- центр окружности
     */
    private void poindBindInTriangleCircle(Circle v1, Circle v2, Circle v3, Circle c1, Circle c2) {
        //Рассчитать радиус и координаты
        v1.centerXProperty().addListener((obj, oldValue, newValue) -> accessInCircle(v1, v2, v3, c1, c2));
        v1.centerYProperty().addListener((obj, oldValue, newValue) -> accessInCircle(v1, v2, v3, c1, c2));
        v2.centerXProperty().addListener((obj, oldValue, newValue) -> accessInCircle(v1, v2, v3, c1, c2));
        v2.centerYProperty().addListener((obj, oldValue, newValue) -> accessInCircle(v1, v2, v3, c1, c2));
        v3.centerXProperty().addListener((obj, oldValue, newValue) -> accessInCircle(v1, v2, v3, c1, c2));
        v3.centerYProperty().addListener((obj, oldValue, newValue) -> accessInCircle(v1, v2, v3, c1, c2));
        updateCircle(c1, c2);
        setTxtShape("");
        txtAreaOutput();
    }

    /**
     * Метод accessInCircle(Circle v1,Circle v2, Circle v3, Circle c1, Circle c2).
     * Предназначен для расчета центра и радиуса вписанной окружности.
     *
     * @param v1  - первая вершина
     * @param v2  - вторая вершина
     * @param v3  - третья вершина
     * @param c1  -вписанная окружность
     * @param c2- центр окружности
     */
    private void accessInCircle(Circle v1, Circle v2, Circle v3, Circle c1, Circle c2) {
        //Выбрать координаты точек
        Point2D v1XY = new Point2D(v1.getCenterX(), v1.getCenterY());
        Point2D v2XY = new Point2D(v2.getCenterX(), v2.getCenterY());
        Point2D v3XY = new Point2D(v3.getCenterX(), v3.getCenterY());
        //Рассчитать центр окружности
        Point2D circleXY = inCircleXY(v1XY, v2XY, v3XY);
        double radius = radiusInCircle(v1XY, v2XY, v3XY);
        setVertex(c2);
        double stX = getScreenXY().getX();
        double stY = getScreenXY().getY();
        setScreenXY(new Point2D(circleXY.getX(), circleXY.getY()));
        notifyObservers("VertexGo");
        setCircle(c1);
        setSegmentStartX(circleXY.getX());
        setSegmentStartY(circleXY.getY());
        setRadiusCircle(radius);
        setRadiusCircleW(accessRadiusW(new Point2D(getSegmentStartX(), getSegmentStartY()), radius));
        notifyObservers("CircleGo");
        setScreenXY(new Point2D(stX, stY));
        updateCircle(c1, c2);
    }

    /**
     * Метод vertexInCircle(String id, String id1, String id2).
     * Предназначена для присвоения вершин треугольника окружности.
     *
     * @param id  - первая вершина треугольника
     * @param id1 - вторая вершина треугольника
     * @param id2 - третья вершина треугольника
     * @param c   - окружность для связывания с точкой
     */
    private void vertexInCircle(String id, String id1, String id2, Circle c) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    p.setBCircle(true);
                    p.setCircleName(c);
                    double angle = newAngle(new Point2D(c.getCenterX(), c.getCenterY()), new Point2D(p.getCircle().getCenterX(), p.getCircle().getCenterY()));
                    if (findCircle(id).getCenterY() < c.getCenterY()) {
                        updateAngle(360 - angle, id);
                    } else {
                        updateAngle(angle, id);
                    }
                    poindBindOnCircles(p.getCircle(), c);//связать точку с окружностью

                }
                if (p.getId().equals(id1)) {
                    p.setBCircle(true);
                    p.setCircleName(c);
                    double angle = newAngle(new Point2D(c.getCenterX(), c.getCenterY()), new Point2D(p.getCircle().getCenterX(), p.getCircle().getCenterY()));
                    if (findCircle(id1).getCenterY() < c.getCenterY()) {
                        updateAngle(360 - angle, id1);
                    } else {
                        updateAngle(angle, id1);
                    }
                    poindBindOnCircles(p.getCircle(), c);//связать точку с окружностью
                }
                if (p.getId().equals(id2)) {
                    p.setBCircle(true);
                    p.setCircleName(c);
                    double angle = newAngle(new Point2D(c.getCenterX(), c.getCenterY()), new Point2D(p.getCircle().getCenterX(), p.getCircle().getCenterY()));
                    if (findCircle(id2).getCenterY() < c.getCenterY()) {
                        updateAngle(360 - angle, id2);
                    } else {
                        updateAngle(angle, id2);
                    }
                    poindBindOnCircles(p.getCircle(), c);//связать точку с окружностью
                }
            }
        }
    }

    /**
     * Метод vertexTreangle(String id, String id1, String id2).
     * Предназначен для внесения в коллекцию точек признака принадлежности
     * точек к вершинам треугольника
     *
     * @param id  - первая вершина треугольника
     * @param id1 - вторая вершина треугольника
     * @param id2 - третья вершина треугольника
     */
    private void vertexTreangle(String id, String id1, String id2) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    p.setBTreangle(true);
                }
                if (p.getId().equals(id1)) {
                    p.setBTreangle(true);
                }
                if (p.getId().equals(id2)) {
                    p.setBTreangle(true);
                }
            }
        }
    }

    /**
     * Метод findVertexTreangle(String id).
     * Предназначен для поиска вершин треугольника
     *
     * @param id - точка
     * @return - true- если точка принадлежит вершине треугольника
     */
    private boolean findVertexTreangle(String id) {
        boolean vertexTr = false;
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    vertexTr = p.isBTreangle();
                }
            }
        }
        return vertexTr;
    }


    /**
     * Метод newPoindShape().
     * Предназначен для выбора новой или существующей точки.
     */
    private void newPoindShape() {
        //Взять существующую или создать новую точку для прямой
        Circle c1 = newCirclePoind();
        setSegmentStartX(c1.getCenterX());
        setSegmentStartY(c1.getCenterY());
        setPoindOne(true);
        newSegment.append(c1.getId());
        newSegment.append("_");
    }

    /**
     * Метод newPoindTwoShape(int segment).
     * Предназначен для начала построения отрезка, луча и прямой
     *
     * @param segment - вид фигуры
     */
    private void newPoindTwoShape(int segment) {
        newLine.setVisible(false);
        newSegment.delete(3, 4);//удалить последнее _
        String[] nameP = newSegment.toString().split("_");
        Line l = createLineAdd(segment);
        l.toBack();
        //Вывести на доску
        setSegmentStartX(newLine.getStartX());
        setSegmentStartY(newLine.getStartY());
        setScreenXY(new Point2D(newLine.getEndX(), newLine.getEndY()));
        setLine(l);
        //обновить имя
        findNameId(newSegment.toString(), l.getId());
        if (segment == 0) {
            notifyObservers("SideGo");
            findLinesUpdateXY(l.getId());
        } else {
            notifyObservers("RayGo");
            //Обновить мировые координаты
            findLinesUpdateXY(l.getId());
        }
        //связать линию с точками
        switch (segment) {
            case 0 -> lineBindCircles(findCircle(nameP[0]), findCircle(nameP[1]), l);
            case 1 -> rayBindCircles(findCircle(nameP[0]), findCircle(nameP[1]), l);
            case 2 -> circlesBindLine(findCircle(nameP[0]), findCircle(nameP[1]), l);
        }
        if (segment != 0) {
            nameLineAdd(l);//добавить имя
        }
    }

    /**
     * Метод newPoindShapeEnd().
     * Предназначен для окончания построения отрезка, луча, прямой.
     */
    private void newPoindShapeEnd() {
        //закончить построение
        newSegment.delete(0, newSegment.length());//очистить строку
        createGeometric = 0;
        setPoindOne(false);
        setPoindTwo(false);
    }

    /**
     * Метод newCirclePoind()
     * Предназначен для создания новой или выбора существующей точки.
     * Используется при создании всех геометрических фигур.
     *
     * @return - возвращает ссылку на точку
     */
    private Circle newCirclePoind() {
        //Создать новую или взять существующую точку
        //PoindOldAdd -false - новая точка, true - взять существующую
        Circle newPoind;
        if (isPoindOldAdd() || isPoindOld()) {
            newPoind = getTimeVer();
            setPoindOld(false);
        } else {
            newPoind = createPoindAdd(true);
        }
        return newPoind;
    }

    /**
     * Метод createNameShapes().
     * Предназначен для создания объекта хранения имени геометрической фигуру.
     * Привязка свойств мышки к объекту.
     *
     * @return - возвращает созданный объект
     */
    Text createNameShapes(String name) {
        Text nameText = new Text();//создать новый объект
        nameText.setId(name);//присвоить имя
        nameText.setFont(Font.font("Alexander", FontWeight.BOLD, FontPosture.REGULAR, 14));
        nameText.setFill(getColorStroke());//цвет букв
        //Привязка к событию мышки
        nameText.setOnMouseDragged(e -> {//перемещение
            //Найти точку с которой связано имя
            Circle circle = findCircle(nameText.getId());
            if (circle != null) {//проверить, выбрани имя точки или линии
                if (nameText.xProperty().isBound()) {//проверить на связь
                    textUnBindCircle(nameText);//снять связь для перемещения
                }
                //Максимальное расстояние при перемещении от точки
                double maxRadius = distance(e.getX(), e.getY(), circle.getCenterX(), circle.getCenterY());
                if (maxRadius < 80) {
                    //Перемещаем имя точки
                    nameText.setX(e.getX());
                    nameText.setY(e.getY());
                }
                nameUpdateXY(nameText.getId());//обновляем данные коллекции
                //устанавливаем связь с точкой
                textBindCircle(circle, nameText, (int) (nameText.getX() - circle.getCenterX()), (int) (nameText.getY() - circle.getCenterY()));
            }
            //Если выбрано имя линии
            Line line = findLineForPoind(nameText.getId());
            if (line != null) {
                //Перемещаем имя точки
                nameText.setX(e.getX());
                nameText.setY(e.getY());
                nameUpdateXY(nameText.getId());//обновляем данные коллекции
            }
        });
        //Наведение мышки на объект
        nameText.setOnMouseEntered(e -> nameText.setCursor(Cursor.HAND));
        //Уход мышки с объекта
        nameText.setOnMouseExited(e -> nameText.setCursor(Cursor.DEFAULT));
        return nameText;
    }

    /**
     * Метод nameUpdateXY(String id).
     * Предназначен для обновления мировых координат и расстояния до точки при
     * перемещении объекта Text. Вызывается из метода createNameShapes(String name).
     *
     * @param id - строка имя объекта Text.
     */
    private void nameUpdateXY(String id) {
        //Найти точку в коллекции
        Circle circle = findCircle(id);
        if (circle != null) {
            for (NamePoindLine np : namePoindLines) {
                if (np != null) {
                    if (np.getId().equals(id)) {
                        np.setDX(gridViews.revAccessX(np.getText().getX()) - gridViews.revAccessX(circle.getCenterX()));
                        np.setDY(gridViews.revAccessY(np.getText().getY()) - gridViews.revAccessY(circle.getCenterY()));
                        np.setX(gridViews.revAccessX(circle.getCenterX()));
                        np.setY(gridViews.revAccessY(circle.getCenterY()));
                    }
                }
            }
        }
        Line line = findLineForPoind(id);//Объект имя линии
        if (line != null) {
            for (NamePoindLine np : namePoindLines) {
                if (np != null) {
                    if (np.getText().getId().equals(id)) {
                        np.setX(decartX);
                        np.setY(decartY);
                    }
                }
            }
        }
    }

    /**
     * Метод nameCircleAdd().
     * Предназначен для добавления объекта Text связанного с именем точки.
     * Вызывается из метода createPoindAdd() при добавлении точек.
     *
     * @param circle - объект точка.
     */
    private void nameCircleAdd(Circle circle) {
        Text textCircle = createNameShapes(circle.getId());//создать объект текст (имя точки)
        //Добавить в коллекцию NamePoindLine
        namePoindLines.add(new NamePoindLine(textCircle, circle.getId(), -1, 1, gridViews.revAccessX(circle.getCenterX()), gridViews.revAccessY(circle.getCenterY()), showPoindName, showLineName, showAngleName, "poind"));
        textCircle.setText(circle.getId());//Имя для вывода на доску
        textX = circle.getCenterX() - 20;//место вывода Х при создании
        textY = circle.getCenterY() + 20;//место вывода Y при создании
        textCircle.setVisible(showPoindName);//показывать не показывать, зависит от меню "Настройка"
        //Передать для вывода в View
        textGo = textCircle;
        notifyObservers("TextGo");
        //Добавить в коллекцию объектов на доске
        paneBoards.getChildren().add(textCircle);
        //Односторонняя связь точки с именем объекта для перемещения
        textBindCircle(circle, textCircle, -20, 20);
    }

    /**
     * Метод nameArcAdd()
     * Предназначен для добавления имени угла на доске.
     *
     * @param circle - объект вершина угла
     * @param s      - имя угла на греческом
     * @param arc    - объект угол
     */
    private void nameArcAdd(Circle circle, String s, Arc arc) {
        Text textAngle = createNameShapes(s);//создать объект текст (имя угла)
        //Добавить в коллекцию NamePoindLine
        textAngle.setText(s);//Имя для вывода на доску
        Point2D arcXY = nameArcShow(circle, arc, textAngle);//рассчитать место буквы
        namePoindLines.add(new NamePoindLine(textAngle, circle.getId(), arcXY.getX(), arcXY.getY(), gridViews.revAccessX(circle.getCenterX()), gridViews.revAccessY(circle.getCenterY()), showPoindName, showLineName, showAngleName, "arc"));
        //Добавить в коллекцию объектов на доске
        paneBoards.getChildren().add(textAngle);
    }

    /**
     * Метод nameArcShow()
     * Предназначен для расчета местоположения имени угла
     *
     * @param circle    - объект вершина угла
     * @param arc       - объект угол
     * @param textAngle - объект текст (имя угла)
     * @return - смещение координат для имени от вершины угла
     */
    private Point2D nameArcShow(Circle circle, Arc arc, Text textAngle) {
        double x = 15 * Math.cos(Math.toRadians(arc.getStartAngle() + arc.getLength() / 2));
        double y = 15 * Math.sin(Math.toRadians(arc.getStartAngle() + arc.getLength() / 2));
        Point2D arcXY = new Point2D(x, y);
        textX = circle.getCenterX() + x;//место вывода Х при создании
        textY = circle.getCenterY() - y;//место вывода Y при создании
        textAngle.setVisible(showAngleName);//показывать не показывать, зависит от меню "Настройка"
        //Передать для вывода в View
        textGo = textAngle;
        notifyObservers("TextGo");
        return arcXY;
    }

    /**
     * Метод findNameText().
     * Предназначен для поиска в коллекции NamePoindLine объекта Text связанного с именем точки.
     *
     * @param circle - объект точка
     * @return - объект Text
     */
    private Text findNameText(Shape circle) {
        for (NamePoindLine np : namePoindLines) {
            if (np != null) {
                if (np.getId().equals(circle.getId())) {
                    return np.getText();
                }
            }
        }
        return null;
    }

    /**
     * Метод findID(Line line)
     * Предназначен для поиска имени линии по объекту линия.
     *
     * @param line - объект линия
     * @return имя линии
     */
    public String findID(Line line) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (p.getLine().getId().equals(line.getId())) {
                    return p.getId();
                }
            }
        }
        return null;
    }

    /**
     * Метод nameLineAdd().
     * Предназначен для добавления имен к прямой и лучам.
     * Вызывается из метода newPoindTwoShape(int segment) при добавлении луча и прямой.
     *
     * @param line - объект линия
     */
    public void nameLineAdd(Line line) {
        //Создать текстовый объект
        Text nameLine = createNameShapes(line.getId());
        //Вызвать метод расчета координат перпендикуляра к середине линии
        nameLineRatchet(line, nameLine);
        namePoindLines.add(new NamePoindLine(nameLine, line.getId(), 0, 0, gridViews.revAccessX(textX), gridViews.revAccessY(textY), showPoindName, showLineName, showAngleName, "line"));
        //Связать линию с именем
        nameBindLines(line, nameLine);
        //Добавить в коллекцию объектов на доске
        paneBoards.getChildren().add(nameLine);
    }

    /**
     * Метод nameLineRatchet().
     * Предназначен для расчета координат места названия линии.
     *
     * @param line - объект линия
     */
    private void nameLineRatchet(Line line, Text nameLine) {
        //Найти точки на линии
        String[] sVer = Objects.requireNonNull(findID(line)).split("_");
        //Найти середину линии
        double aX = findCircle(sVer[0]).getCenterX();
        double aY = findCircle(sVer[0]).getCenterY();
        double bX = findCircle(sVer[1]).getCenterX();
        double bY = findCircle(sVer[1]).getCenterY();
        Point2D mP = new Point2D(aX, aY).midpoint(new Point2D(bX, bY));
        double cX = mP.getX();
        double cY = mP.getY();
        //Рассчитать координаты перпендикуляр от середины линии на расстоянии 15рх
        double dlina = sqrt((pow((aX - bX), 2)) + (pow((aY - bY), 2)));
        textX = cX - 15 * ((aY - bY) / dlina);//место вывода Х при создании
        textY = cY + 15 * ((aX - bX) / dlina);//место вывода Y при создании
        nameLine.setText(line.getId());//Имя для вывода на доску
        nameLine.setVisible(showLineName);//показывать не показывать, зависит от меню "Настройка"
        //Передать для вывода в View
        textGo = nameLine;
        notifyObservers("TextGo");
    }

    /**
     * Метод nameBindLines().
     * Предназначен для связывания имени линии с началом и концом линии.
     * Для задания перемещения имени луча и прямой.
     *
     * @param line     - объект линия.
     * @param nameLine - объект текст.
     */
    private void nameBindLines(Line line, Text nameLine) {
        line.startXProperty().addListener((obj, oldValue, newValue) -> nameLineRatchet(line, nameLine));
        line.startYProperty().addListener((obj, oldValue, newValue) -> nameLineRatchet(line, nameLine));
        line.endYProperty().addListener((obj, oldValue, newValue) -> nameLineRatchet(line, nameLine));
        line.endXProperty().addListener((obj, oldValue, newValue) -> nameLineRatchet(line, nameLine));
    }

    /**
     * Метод circlesBindOnLine(Circle c, Line l).
     * Предназначен для связывания точки принадлежащей линии.
     *
     * @param poindC - ссылка на точку принадлежащей линии
     * @param lineA  - ссылка на линию которой принадлежит точка
     */
    private void circlesBindOnLine(Circle poindC, Line lineA) {
        String[] namePoind = findID(lineA).split("_");
        Circle vertexA = findCircle(namePoind[0]);
        Circle vertexB = findCircle(namePoind[1]);
        vertexA.centerXProperty().addListener((obj, ojdValue, newValue) -> {
            double cordX = vertexA.getCenterX() + (vertexB.getCenterX() - vertexA.getCenterX()) * findT(poindC);
            poindC.setCenterX(cordX);
            poindUpdateXY(poindC);
        });
        vertexA.centerYProperty().addListener((obj, ojdValue, newValue) -> {
            double cordY = vertexA.getCenterY() + (vertexB.getCenterY() - vertexA.getCenterY()) * findT(poindC);
            poindC.setCenterY(cordY);
            poindUpdateXY(poindC);
        });
        vertexB.centerXProperty().addListener((obj, ojdValue, newValue) -> {
            double cordX = vertexA.getCenterX() + (vertexB.getCenterX() - vertexA.getCenterX()) * findT(poindC);
            poindC.setCenterX(cordX);
            poindUpdateXY(poindC);
        });
        vertexB.centerYProperty().addListener((obj, ojdValue, newValue) -> {
            double cordY = vertexA.getCenterY() + (vertexB.getCenterY() - vertexA.getCenterY()) * findT(poindC);
            poindC.setCenterY(cordY);
            poindUpdateXY(poindC);
        });
    }

    /**
     * Метод poindBindOnCircles(Circle poindA, Circle poindB).
     * Предназначен для связывания точки с окружностью.
     *
     * @param poindA - объект точка на окружности
     * @param poindB - объект окружность
     */
    private void poindBindOnCircles(Circle poindA, Circle poindB) {
        poindA.centerXProperty().addListener((obj, oldValue, newValue) -> {
            double cordX = poindB.getRadius() * Math.cos(Math.toRadians(findAngle(poindA))) + poindB.getCenterX();
            poindA.setCenterX(cordX);
        });
        poindA.centerYProperty().addListener((obj, oldValue, newValue) -> {
            double cordY = poindB.getRadius() * Math.sin(Math.toRadians(findAngle(poindA))) + poindB.getCenterY();
            poindA.setCenterY(cordY);
        });

        poindB.centerXProperty().addListener((obj, oldValue, newValue) -> {
            double cordX = poindB.getRadius() * Math.cos(Math.toRadians(findAngle(poindA))) + poindB.getCenterX();
            poindA.setCenterX(cordX);
        });
        poindB.centerYProperty().addListener((obj, oldValue, newValue) -> {
            double cordY = poindB.getRadius() * Math.sin(Math.toRadians(findAngle(poindA))) + poindB.getCenterY();
            poindA.setCenterY(cordY);
        });
        poindB.radiusProperty().addListener((obj, oldValue, newValue) -> {
            double cordX = poindB.getRadius() * Math.cos(Math.toRadians(findAngle(poindA))) + poindB.getCenterX();
            poindA.setCenterX(cordX);
        });
        poindB.radiusProperty().addListener((obj, oldValue, newValue) -> {
            double cordY = poindB.getRadius() * Math.sin(Math.toRadians(findAngle(poindA))) + poindB.getCenterY();
            poindA.setCenterY(cordY);
        });
    }

    /**
     * Метод findT(Circle c).
     * Для поиска и возврата параметрического параметра прямой.
     *
     * @param c - ссылка на точку
     * @return - возвращает параметрический параметр
     */
    private double findT(Circle c) {
        double t = 0;
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(c.getId())) {
                    t = p.getT();
                }
            }
        }
        return t;
    }

    /**
     * Метод findAngle(Circle c).
     * Для поиска и возврата угла для точек окружности.
     *
     * @param c - ссылка на точку
     * @return - возвращает угол
     */
    private double findAngle(Circle c) {
        double angle = 0;
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(c.getId())) {
                    angle = p.getAngle();
                }
            }
        }
        return angle;
    }

    /**
     * Метод accessRadiusW(Circle c1, Circle c2).
     * Предназначен для расчета радиуса в мировых координатах.
     *
     * @param poind - координаты центра окружности
     * @param R     - радиус окружности
     * @return радиус в мировых координатах
     */
    public double accessRadiusW(Point2D poind, double R) {
        return distance(gridViews.revAccessX(poind.getX()), gridViews.revAccessY(poind.getY()), gridViews.revAccessX(poind.getX() + R), gridViews.revAccessY(poind.getY()));
    }

    /**
     * Метод accessRadius(Point2D xy, int R).
     * Предназначен для пересчета радиуса из мировых координат в координаты экрана.
     *
     * @param xy - мировые координаты центра окружности.
     * @param R  - радиус в мировых значениях.
     * @return возвращает радиус в экранных значениях.
     */
    public double accessRadius(Point2D xy, double R) {
        return distance(gridViews.accessX(xy.getX()), gridViews.accessY(xy.getY()), gridViews.accessX(xy.getX() + R), gridViews.accessY(xy.getY()));
    }


    /**
     * Метод createPoindAdd()
     * Предназначен для создания точек и вывод на доску.
     * Для создания точки вызывается метод createPoind().
     *
     * @return новая точка
     */
    Circle createPoindAdd(boolean bMove) {
        vertex = createPoind();//Создать точку
        //добавить на доску
        paneBoards.getChildren().add(vertex);
        notifyObservers("VertexGo");//передать в View для вывода на экран
        //Добавить имя на доску
        nameCircleAdd(vertex);
        //добавить в коллекцию точек
        poindCircles.add(new PoindCircle(vertex, vertex.getId(), new Point2D(decartX, decartY), 1, 2, bMove, false, null, 0.0, false, false, null, 0));
        //Связать изменение координат с перерасчетом мировых координат
        poindBindUpdateXY(vertex);
        //Добавить в правую часть доски
        setTxtShape("");
        txtAreaOutput();
        //Если точка на линии
        if (lineOldAdd && createGeometric != 12 && createGeometric != 6 && createGeometric != 7) {
            String[] n = findID(timeLine).split("_");
            if ((findCircle(n[1]).getCenterX() - findCircle(n[0]).getCenterX()) != 0) {
                t = (vertex.getCenterX() - findCircle(n[0]).getCenterX()) / (findCircle(n[1]).getCenterX() - findCircle(n[0]).getCenterX());
            } else {
                t = (vertex.getCenterY() - findCircle(n[0]).getCenterY()) / (findCircle(n[1]).getCenterY() - findCircle(n[0]).getCenterY());
            }
            for (PoindCircle p : poindCircles) {
                if (p != null) {
                    if (p.getCircle().getId().equals(vertex.getId())) {
                        p.setLine(timeLine);//добавить линию
                        p.setT(t);//параметр для параметрического уравнения
                        p.setBLine(true);//точка принадлежит линии
                    }
                }
            }
            //Связать точку с линией
            circlesBindOnLine(vertex, timeLine);
        }

        //Если точка на окружности
        if (circleAddPoind) {
            Point2D a = new Point2D(timeCircle.getCenterX(), timeCircle.getCenterY());
            Point2D b = new Point2D(vertex.getCenterX(), vertex.getCenterY());
            double angle = newAngle(a, b);
            for (PoindCircle p : poindCircles) {
                if (p != null) {
                    if (p.getCircle().getId().equals(vertex.getId())) {
                        p.setCircleName(timeCircle);//добавить линию
                        p.setAngle(angle);//угол для преобразования из полярных координат в декартовые
                        p.setBCircle(true);//точка принадлежит окружности
                    }
                }
            }
            poindBindOnCircles(vertex, timeCircle);//связать точку с окружностью
        }
        return vertex;//возвращает точку
    }

    /**
     * Метод newAngle(Point2D a, Point2D b).
     * Предназначен для расчета угла точки в полярных координатах.
     *
     * @param a - центр окружности
     * @param b - точка на окружности
     * @return угол.
     */
    private double newAngle(Point2D a, Point2D b) {
        Point2D c = new Point2D(a.getX() + 20, a.getY());
        return a.angle(b, c);
    }

    /**
     * Метод createPoind()
     * Предназначен для создания точек в виде кругов, а также привязке событий к данным точкам.
     * Определяет основные свойства объекта.
     *
     * @return точку.
     */
    Circle createPoind() {
        Circle newPoind = new Circle();
        newPoind.setRadius(radiusPoind);//радиус
        //Цвет точки
        setColorStroke(Color.valueOf(tableColor[1][2]));
        setColorFill(Color.valueOf(tableColor[1][3]));
        setShapeColor(newPoind);
        notifyObservers("StrokeShape"); //Передаем в View для вывода
        notifyObservers("FillShape");
        newPoind.setId(indexPoindAdd());//Индификатор узла
        //Контекстное меню
        newContextMenu(newPoind, 1);
        //Обработка событий
        //Перемещение с нажатой клавишей
        newPoind.setOnMouseDragged(e -> {
            if (!createShape) {
                if (findPoindCircleMove(newPoind.getId())) {
                    //Найти по точке имя в коллекции
                    Text txt = findNameText(newPoind);
                    if (!Objects.requireNonNull(txt).xProperty().isBound()) {//проверить на связь, если нет связать
                        textBindCircle(newPoind, txt, (int) (txt.getX() - newPoind.getCenterX()), (int) (txt.getY() - newPoind.getCenterY()));//если нет, связать
                    }
                    //Проверить, принадлежит ли точка линии
                    if (findBLine(newPoind.getId())) {
                        Line l = findLineForPoind(newPoind.getId());
                        if (l != null) {
                            Point2D A1 = new Point2D(e.getX(), e.getY());
                            Point2D B1 = new Point2D(l.getStartX(), l.getStartY());
                            Point2D C1 = new Point2D(l.getEndX(), l.getEndY());
                            Point2D D1 = heightPoind(A1, B1, C1);//координаты точки пересечения
                            String[] n = findID(l).split("_");
                            if ((findCircle(n[1]).getCenterX() - findCircle(n[0]).getCenterX()) != 0) {
                                t = (D1.getX() - findCircle(n[0]).getCenterX()) / (findCircle(n[1]).getCenterX() - findCircle(n[0]).getCenterX());
                            } else {
                                t = (D1.getY() - findCircle(n[0]).getCenterY()) / (findCircle(n[1]).getCenterY() - findCircle(n[0]).getCenterY());
                            }
                            setScreenXY(new Point2D(D1.getX(), D1.getY()));
                            //Определить тип линии
                            double typeL = findTypeLine(l);
                            if (typeL == 0 || typeL == 1 || typeL == 3 || typeL == 4 || typeL == 5 || typeL == 6 || typeL == 7) {
                                //Проверить дошла ли точка до начала линии
                                if (t <= 0) {
                                    setScreenXY(new Point2D(l.getStartX(), l.getStartY()));
                                    t = 0;
                                }
                            }
                            if (typeL == 0 || typeL == 3 || typeL == 4 || typeL == 5 || typeL == 6 || typeL == 7) {
                                //Проверить достигла ли точка конца линии
                                if (t >= 1) {
                                    setScreenXY(new Point2D(l.getEndX(), l.getEndY()));
                                    t = 1;
                                }
                            }
                            //Сохранить параметрический параметр t для прямой в коллекции
                            updateT(newPoind, t);
                        }
                    }
                    //Проверить принадлежит ли точка окружности
                    if (findBCircle(newPoind.getId())) {
                        Circle c = findCirclePoind(newPoind.getId());
                        Point2D b = new Point2D(e.getX(), e.getY());
                        assert c != null;
                        Point2D a = new Point2D(c.getCenterX(), c.getCenterY());
                        double angle = newAngle(a, b);
                        if (e.getY() < c.getCenterY()) {
                            updateAngle(360 - angle, newPoind.getId());
                        } else {
                            updateAngle(angle, newPoind.getId());
                        }
                    }

                    //изменить местоположение точки
                    vertex = newPoind;
                    notifyObservers("VertexGo");
                    vertex = circleFill;
                    notifyObservers("VertexGo");
                    setTxtShape("");
                    txtAreaOutput();
                } else {
                    setStringLeftStatus(STA_8);
                    notifyObservers("LeftStatusGo");
                }
            }
        });
        //Нажатие клавиши
        newPoind.setOnMousePressed(e -> {
            //Проверить разрешено ли взять эту точку. Если расчетная, то запрещено
            //if (findPoindAddMove(newPoind)) {
            poindOldAdd = true;//взять эту точку для отрезка
            timeVer = newPoind;//сохранить выбранную точку для построения
            //}
        });
        //Наведение на точку
        newPoind.setOnMouseEntered(e ->

        {
            newPoind.setCursor(Cursor.HAND);
            circleFill.setVisible(true);//добавить увеличенную точку
            circleFill.setCenterX(newPoind.getCenterX());
            circleFill.setCenterY(newPoind.getCenterY());
            circleFill.setFill(Color.valueOf(tableColor[1][findColorPoind(newPoind.getId()) + 1]));
            circleFill.setOpacity(0.4);
            //Установить статус "Точка + выбранная точка"
            setStringLeftStatus(STA_9 + newPoind.getId());
            notifyObservers("LeftStatusGo");
        });
        //Уход с точки
        newPoind.setOnMouseExited(e ->

        {
            newPoind.setCursor(Cursor.DEFAULT);
            circleFill.setVisible(false);
            poindOldAdd = false;//запрет брать точку для отрезков, прямых, лучей
            //Установить статус пустая строка
            setStringLeftStatus("");
            notifyObservers("LeftStatusGo");
        });
        return newPoind;//завершено создание новой точки
    }

    /**
     * Метод newContextMenu(Shape c).
     * Предназначен для добавления контекстного меню
     * отрезку, прямой, луча.
     *
     * @param c     - графический объект
     * @param shape - тип графической фигуры 1-точка, 2-линия, 3 - угол
     */
    private void newContextMenu(Shape c, int shape) {
        //Контекстное меню
        MenuItem menuItem = new MenuItem("Переименовать");
        menuItem.setOnAction(event -> {
            TextField text = new TextField();
            text.setText(c.getId());
            text.setAlignment(Pos.CENTER);
            text.setLayoutX(getScreenXY().getX());
            text.setLayoutY(getScreenXY().getY());
            paneBoards.getChildren().add(text);

            text.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    event.consume();
                    paneBoards.getChildren().remove(text);

                }
            });

        });
        Menu menuItem2 = new Menu("Изменить цвет ");
        MenuItem mCol1 = new MenuItem();
        MenuItem mCol2 = new MenuItem();
        MenuItem mCol3 = new MenuItem();
        MenuItem mCol4 = new MenuItem();
        MenuItem mCol5 = new MenuItem();

        mCol1.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/alex/euclid/Images/RedColor.png")))));
        mCol2.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/alex/euclid/Images/BlueColor.png")))));
        mCol3.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/alex/euclid/Images/GreenColor.png")))));
        mCol4.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/alex/euclid/Images/YellowColor.png")))));
        mCol5.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/alex/euclid/Images/BlackColor.png")))));
        menuItem2.getItems().addAll(mCol1, mCol2, mCol3, mCol4, mCol5 );
        mCol1.setOnAction(event -> tableColor(c, shape, 0));
        mCol2.setOnAction(event -> tableColor(c, shape, 2));
        mCol3.setOnAction(event -> tableColor(c, shape, 4));
        mCol4.setOnAction(event -> tableColor(c, shape, 6));
        mCol5.setOnAction(event -> tableColor(c, shape, 8));

        Menu menuItem3 = new Menu("Форма линий");
        MenuItem mIt1 = new MenuItem();
        MenuItem mIt2 = new MenuItem();
        MenuItem mIt3 = new MenuItem();
        MenuItem mIt4 = new MenuItem();
        MenuItem mIt5 = new MenuItem();
        mIt1.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/alex/euclid/Images/Line1.png")))));
        mIt2.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/alex/euclid/Images/Line2.png")))));
        mIt3.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/alex/euclid/Images/Line3.png")))));
        mIt4.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/alex/euclid/Images/Line4.png")))));
        mIt5.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/alex/euclid/Images/line5.png")))));
        menuItem3.getItems().addAll(mIt1, mIt2, mIt3, mIt4, mIt5);

        mIt1.setOnAction(event -> {
            c.getStrokeDashArray().clear();
            c.getStrokeDashArray().addAll(getLineStokeWidth());
        });
        mIt2.setOnAction(event -> {
            c.getStrokeDashArray().clear();
            c.getStrokeDashArray().addAll(getLineStokeWidth(), 2 * getLineStokeWidth());
        });
        mIt3.setOnAction(event -> {
            c.getStrokeDashArray().clear();
            c.getStrokeDashArray().addAll(7 * getLineStokeWidth(), 2 * getLineStokeWidth());
        });
        mIt4.setOnAction(event -> {
            c.getStrokeDashArray().clear();
            c.getStrokeDashArray().addAll(5.0 * getLineStokeWidth(), getLineStokeWidth(), 5.0 * getLineStokeWidth());
        });
        mIt5.setOnAction(event -> {
            c.getStrokeDashArray().clear();
            c.getStrokeDashArray().addAll(5.0 * getLineStokeWidth(), getLineStokeWidth(), 2.5 * getLineStokeWidth(), getLineStokeWidth(), 5.0 * getLineStokeWidth());
        });

        Menu menuItem4 = new Menu("Толщина линий");
        MenuItem m1 = new MenuItem();
        MenuItem m2 = new MenuItem();
        MenuItem m3 = new MenuItem();
        MenuItem m4 = new MenuItem();
        MenuItem m5 = new MenuItem();
        m1.setText("2 px");
        m2.setText("3 px");
        m3.setText("4 px");
        m4.setText("5 px");
        m5.setText("6 px");
        menuItem4.getItems().addAll(m1, m2, m3, m4, m5);

        m1.setOnAction(event -> {
            setLineStokeWidth(2);
            setShapeColor(c);
            notifyObservers("StrokeWidth");
        });
        m2.setOnAction(event -> {
            setLineStokeWidth(3);
            setShapeColor(c);
            notifyObservers("StrokeWidth");
        });
        m3.setOnAction(event -> {
            setLineStokeWidth(4);
            setShapeColor(c);
            notifyObservers("StrokeWidth");
        });
        m4.setOnAction(event -> {
            setLineStokeWidth(5);
            setShapeColor(c);
            notifyObservers("StrokeWidth");
        });
        m5.setOnAction(event -> {
            setLineStokeWidth(6);
            setShapeColor(c);
            notifyObservers("StrokeWidth");
        });

        Menu menuItem5 = new Menu("Форма точек");
        MenuItem mCt1 = new MenuItem();
        MenuItem mCt2 = new MenuItem();
        MenuItem mCt3 = new MenuItem();
        mCt1.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/alex/euclid/Images/circle_1.png")))));
        mCt2.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/alex/euclid/Images/circle_2.png")))));
        mCt3.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/alex/euclid/Images/circle_3.png")))));
        menuItem5.getItems().addAll(mCt1, mCt2, mCt3);
        mCt1.setOnAction(e -> poindForm(c, 0));
        mCt2.setOnAction(e -> poindForm(c, 1));
        mCt3.setOnAction(e -> poindForm(c, 2));

        ContextMenu menu = new ContextMenu();
        if (shape == 2) {
            menu.getItems().addAll(menuItem, menuItem2, menuItem3, menuItem4);
        } else if (shape == 1) {
            menu.getItems().addAll(menuItem, menuItem2, menuItem5);
        } else {
            menu.getItems().addAll(menuItem, menuItem2);
        }
        //Привязка меню к графическому объекту
        c.setOnMouseClicked(t -> {
            if (t.getButton().toString().equals("SECONDARY"))
                menu.show(c, t.getScreenX(), t.getSceneY());
        });
    }

    /**
     * Метод poindForm(Shape c, int form).
     * Предназначен для изменения формы точки.
     *
     * @param c    - объект точка
     * @param form - номер формы
     */
    private void poindForm(Shape c, int form) {
        setShapeColor(c);
        int colorPoind = findColorPoind(c.getId());
        setColorStroke(Color.valueOf(tableColor[form][colorPoind]));
        notifyObservers("StrokeShape");
        setColorFill(Color.valueOf(tableColor[form][colorPoind + 1]));
        notifyObservers("FillShape");
        updateFormPoind(c.getId(), form);
    }

    /**
     * Метод tableColor(Shape c, int shape, int color).
     * Предназначен для изменения цвета точку, учитывая форму точки.
     * Данные берутся из таблицы цветов. Таблица цветов инициализируется в контролере при запуске.
     *
     * @param c     - объект точка
     * @param shape - форма точки
     * @param color - цвет точки
     */
    private void tableColor(Shape c, int shape, int color) {
        setShapeColor(c);
        if (shape == 1) {
            int f = findFormPoind(c.getId());
            setColorStroke(Color.valueOf(tableColor[f][color]));
            notifyObservers("StrokeShape");
            setColorFill(Color.valueOf(tableColor[f][color + 1]));
            notifyObservers("FillShape");
            updateColorPoind(c.getId(), color);
        } else {
            setColorStroke(Color.valueOf(tableColor[1][color]));
            notifyObservers("StrokeShape");
            setColorFill(Color.valueOf(tableColor[1][color + 1]));
            notifyObservers("FillShape");
        }
        setColorFill(Color.valueOf(tableColor[1][color]));
        if (shape == 3) {
            setShapeColor(findArcNameAngle(c.getId()));
            notifyObservers("FillShape");
        } else {
            if (findNameText(c) != null) {
                setShapeColor(findNameText(c));//буквы
                notifyObservers("FillShape");
            }
        }
    }

    /**
     * Метод findFormPoind(String id).
     * Предназначен для поиска в коллекции типа формы точки
     *
     * @param id - имя точки
     * @return тип формы точки
     */
    private int findFormPoind(String id) {
        int form = 1;
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    form = p.getColorX();
                }
            }
        }
        return form;
    }

    /**
     * Метод updateFormPoind(String id, int form).
     * Предназначен для обновления типа формы в коллекции.
     *
     * @param id   - тмя точки
     * @param form - тип формы
     */
    private void updateFormPoind(String id, int form) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    p.setColorX(form);
                }
            }
        }
    }

    /**
     * Метод findColorPoind(String id).
     * Предназначен для поиска в коллекции цвета точки
     *
     * @param id - имя точки
     * @return - возвращает цвет точки
     */
    private int findColorPoind(String id) {
        int colorPoind = 2;
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    colorPoind = p.getColorY();
                }
            }
        }
        return colorPoind;
    }

    /**
     * Метод updateColorPoind(String id, int colorPoind).
     * Предназначен для обновления цвета точки в коллекции.
     *
     * @param id         - имя точки
     * @param colorPoind - цвет точки
     */
    private void updateColorPoind(String id, int colorPoind) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    p.setColorY(colorPoind);
                }
            }
        }
    }

    /**
     * Метод updateAngle(double angle, String id).
     * Предназначен для обновления угла в коллекции.
     *
     * @param id    - имя точки
     * @param angle - угол
     */
    private void updateAngle(double angle, String id) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    p.setAngle(angle);
                }
            }
        }
    }

    /**
     * Метод tangentCircle(Point2D c, Point2D d, double r).
     * Предназначен для расчета точки которая принадлежит касательной к окружности.
     *
     * @param c - координаты центра окружности
     * @param d - координаты точки лежащей на окружности, через которую проведена касательная
     * @param r - радиус окружности
     * @return e - координаты второй точки касательной
     */
    private Point2D tangentCircle(Point2D c, Point2D d, double r) {
        double a4 = -2 * c.getX();
        double a5 = -2 * c.getY();
        double a6 = pow(c.getX(), 2) + pow(c.getY(), 2) - pow(r, 2);
        double a1 = d.getX() + a4 / 2;
        double b1 = d.getY() + a5 / 2;
        double c1 = (a4 * d.getX() + a5 * d.getY()) / 2 + a6;
        double x = d.getX() + 10;
        double y = (-a1 / b1) * (d.getX() + 10) - c1 / b1;
        return new Point2D(x, y);
    }

    /**
     * Метод findCirclePoind(String id).
     * Предназначен для поиска объекта окружность, которой принадлежит точка.
     *
     * @param id - имя точки
     * @return - объект окружность
     */
    private Circle findCirclePoind(String id) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    return p.getCircleName();
                }
            }
        }
        return null;
    }

    /**
     * Метод findBCircle(String id).
     * Предназначен для поиска точки принадлежащей окружности.
     *
     * @param id - имя точки
     */
    private boolean findBCircle(String id) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    return p.isBCircle();
                }
            }
        }
        return false;
    }

    /**
     * Метод poindBindUpdateXY(Circle vertex).
     * Предназначен для связывания изменения координат точки с перерасчетом в декартовы координаты.
     *
     * @param vertex - ссылка на точку
     */
    private void poindBindUpdateXY(Circle vertex) {
        vertex.centerXProperty().addListener((obj, oldValue, newValue) -> poindUpdateXY(vertex));
        vertex.centerYProperty().addListener((obj, oldValue, newValue) -> poindUpdateXY(vertex));
    }

    /**
     * Метод poindUpdateXY(Circle poind).
     * Предназначен для перерасчета координат точки в декартовые.
     *
     * @param pCircle -ссылка на точку
     */
    private void poindUpdateXY(Circle pCircle) {
        poindCircles.forEach(p -> {
            if (p != null) {
                if (p.getId().equals(pCircle.getId())) {
                    p.setXY(new Point2D(gridViews.revAccessX(pCircle.getCenterX()), gridViews.revAccessY(pCircle.getCenterY())));
                }
            }
        });
    }

    /**
     * Метод updateT((Line l, Circle c).
     * Предназначен для пересчета параметрического параметра прямой.
     *
     * @param t - параметрический параметр
     * @param c - ссылка на точку
     */
    private void updateT(Circle c, double t) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(c.getId())) {
                    p.setT(t);
                    p.setXY(new Point2D(gridViews.revAccessX(c.getCenterX()), gridViews.revAccessY(c.getCenterY())));
                }
            }
        }
    }

    /**
     * Метод findBLine(String poind).
     * Предназначен для поиска принадлежности точки линии
     *
     * @param poind - имя точка
     * @return - если точка принадлежит линии true, иначе false
     */
    private boolean findBLine(String poind) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(poind)) {
                    return p.isBLine();
                }
            }
        }
        return false;
    }

    /**
     * Метод findLineForPoind(String poind).
     * Для поиска по имени точки линии которой она принадлежит
     *
     * @param poind - имя точки
     * @return имя линии которой принадлежит точка
     */
    private Line findLineForPoind(String poind) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(poind)) {
                    return p.getLine();
                }
            }
        }
        return null;
    }


    /**
     * Метод createCircle().
     * Предназначен для создания новой окружности и подключения событий мышки.
     *
     * @return circle - возвращает созданную окружность
     */
    Circle createCircle() {
        Circle newCircle = new Circle(getScreenXY().getX(), getScreenXY().getY(), 0, Color.TRANSPARENT);
        newCircle.setStroke(Color.CHOCOLATE);
        newCircle.setStrokeWidth(2.0);
        newCircle.setId(indexLineAdd());//добавить имя окружности
        //привязать события мышки
        //При наведении
        newCircle.setOnMouseEntered(e -> {
            newCircle.setCursor(Cursor.HAND);
            newCircle.setStrokeWidth(3.0);
            //Установить статус "Окружность"
            setStringLeftStatus(MessageFormat.format("{0}{1}{2}{3,number, #.#} \n", "Окружность ", newCircle.getId(), ", R=", findCircleRadiusW(newCircle)));
            notifyObservers("LeftStatusGo");
        });
        //При уходе
        newCircle.setOnMouseExited(e -> {
            newCircle.setCursor(Cursor.DEFAULT);
            newCircle.setStrokeWidth(2.0);
            setStringLeftStatus("");
            notifyObservers("LeftStatusGo");
            circleAddPoind = false;
        });
        //При перемещении с нажатой кнопкой
        newCircle.setOnMouseDragged(e -> {
            if (!createShape) {
                setRadiusCircle(distance(newCircle.getCenterX(), newCircle.getCenterY(), getScreenXY().getX(), getScreenXY().getY()));
                setRadiusCircleW(distance(gridViews.revAccessX(newCircle.getCenterX()), gridViews.revAccessY(newCircle.getCenterY()), getDecartX(), getDecartY()));
                updateCircle(newCircle, findCircle(findNameCenterCircle(newCircle)));
                setCircle(newCircle);
                setSegmentStartX(newCircle.getCenterX());
                setSegmentStartY(newCircle.getCenterY());
                notifyObservers("CircleGo");
                setTxtShape("");
                txtAreaOutput();
            }
        });
        //Добавление точки на окружность
        newCircle.setOnMousePressed(e -> {
            circleAddPoind = true;
            timeCircle = newCircle;//сохранить ссылку во временной переменной
        });
        return newCircle;
    }

    /**
     * Метод updateCircle(Circle c, Circle c0).
     * Предназначен для обновления мировых координат и радиуса окружности в коллекции.
     * Вызывается при изменении радиуса и смещении окружности.
     *
     * @param c1 - ссылка на окружность
     * @param c2 - центр окружности
     */
    public void updateCircle(Circle c1, Circle c2) {
        for (CircleLine p : circleLines) {
            if (p != null) {
                if (p.getId().equals(c1.getId())) {
                    p.setRadius(accessRadiusW(new Point2D(c2.getCenterX(), c2.getCenterY()), c1.getRadius()));
                    p.setCircle(c1);
                    p.setX(gridViews.revAccessX(c2.getCenterX()));
                    p.setY(gridViews.revAccessY(c2.getCenterY()));
                }
            }
        }
    }


    /**
     * Метод findCircleRadiusW(Circle c).
     * Возвращает радиус окружности в мировых координатах
     *
     * @param c - ссылка на окружность
     * @return - радиус окружности в мировых координатах
     */
    double findCircleRadiusW(Circle c) {
        for (CircleLine p : circleLines) {
            if (p != null) {
                if (p.getCircle().getId().equals(c.getId())) {
                    return p.getRadius();
                }
            }
        }
        return 0;
    }


    /**
     * Метод findNameCenterCircle(Circle c).
     * Возвращает имя центра окружности.
     *
     * @param c - ссылка на окружность
     * @return - возвращает имя центра окружности
     */
    String findNameCenterCircle(Circle c) {
        return circleLines.stream().filter(p -> p.getId().equals(c.getId())).findFirst().map(CircleLine::getPoindID).orElse(null);
    }

    /**
     * Метод bindPoindCircle(Circle poind, Circle circle).
     * Предназначен для связывания центра окружности с окружностью для перемещения
     *
     * @param poind - ссылка на центр окружности
     * @param c     - ссылка на окружность
     */
    public void bindPoindCircle(Circle poind, Circle c) {
        poind.centerXProperty().bindBidirectional(c.centerXProperty());
        poind.centerYProperty().bindBidirectional(c.centerYProperty());
        c.centerYProperty().addListener((old, oldValue, newValue) -> updateCircle(c, poind));
        c.centerXProperty().addListener((old, oldValue, newValue) -> updateCircle(c, poind));
    }

    /**
     * Метод createCircleAdd(Circle name).
     * Предназначен для добавления окружности на доску.
     *
     * @param name - объект центр окружности
     * @return - объект окружность
     */
    Circle createCircleAdd(Circle name) {
        Circle circle = createCircle();
        circleLines.add(new CircleLine(circle, gridViews.revAccessX(circle.getCenterX()), gridViews.revAccessY(circle.getCenterY()), circle.getId(), circle.getRadius(), name.getId()));
        paneBoards.getChildren().add(circle);//добавить окружность на доску
        circle.toBack();
        bindPoindCircle(name, circle);
        setTxtShape("");
        txtAreaOutput();
        return circle;
    }

    /**
     * Метод createLine(int seg).
     * Предназначен для создания линий отрезков, лучей, прямых, сторон треугольника, медиан, биссектрис, высот.
     *
     * @return - возвращает новый объект Line.
     */
    Line createLine() {
        line = new Line();
        line.setStrokeWidth(lineStokeWidth);//Толщина линии
        line.getStrokeDashArray().addAll(2.0); //Задаем вид линии
        //Цвет линии задается переменной
        setColorStroke(Color.DARKSLATEBLUE);
        setColorFill(Color.DEEPSKYBLUE);
        setShapeColor(line);
        notifyObservers("StrokeShape"); //Передаем в View для вывода
        notifyObservers("FillShape");
        line.setId(indexLineAdd());//Идентификатор узла
        //Привязка событий мышки
        mouseLine(line);
        //Контекстное меню
        newContextMenu(line, 2);
        return line;
    }

    /**
     * Метод mouseLine().
     * Предназначен для привязки событий мышки к объекту Line.
     *
     * @param mLine - ссылка на линию к которой привязаны события мышки
     */
    public void mouseLine(Line mLine) {
        //Перемещение линий
        mLine.setOnMouseDragged(e -> {
            //Блокировать, если режим построения
            if (!createShape) {
                //Определить, разрешено ли перемещение линии
                if (findLineMove(mLine)) {
                    newLine.setVisible(false);
                    String[] nameId = findID(mLine).split("_");
                    Circle A = findCircle(nameId[0]);
                    Circle B = findCircle(nameId[1]);
                    //Перемещение линии
                    if (findPoindMove(nameId[0]) && findPoindMove(nameId[1])) {
                        //Если не расчетная, пересчитать координаты.
                        A.setCenterX(e.getX() + getDXStart());
                        A.setCenterY(e.getY() + getDYStart());
                        B.setCenterX(e.getX() + getDXEnd());
                        B.setCenterY(e.getY() + getDYEnd());
                    }
                    setTxtShape("");
                    txtAreaOutput();

                } else {
                    stringLeftStatus = STA_30;
                    notifyObservers("LeftStatusGo");
                }
            }
        });
        //Наведение на графический объект
        mLine.setOnMouseEntered(e -> {
            mLine.setCursor(Cursor.HAND);
            //Установить статус
            for (PoindLine p : poindLines) {
                if (p.getLine().getId().equals(mLine.getId())) {
                    switch (p.getSegment()) {
                        case 0 -> {
                            setStringLeftStatus(STA_10 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 1 -> {
                            setStringLeftStatus(STA_11 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 2 -> {
                            setStringLeftStatus(STA_12 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 3 -> {
                            setStringLeftStatus(STA_17 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 4 -> {
                            setStringLeftStatus(STA_20 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 5 -> {
                            setStringLeftStatus(STA_23 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 6 -> {
                            setStringLeftStatus(STA_25 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                        case 7 -> {
                            setStringLeftStatus(STA_27 + nameSplitRemove(p.getId()));
                            notifyObservers("LeftStatusGo");
                        }
                    }
                }
            }
            if (!createShape) {
                newLine.setStartX(mLine.getStartX());
                newLine.setStartY(mLine.getStartY());
                newLine.setEndX(mLine.getEndX());
                newLine.setEndY(mLine.getEndY());
                newLine.setStrokeWidth(mLine.getStrokeWidth() * 4);
                newLine.setStroke(mLine.getFill());
                newLine.setOpacity(0.3);
                newLine.setVisible(true);
                newLine.toBack();
            }
        });
        //уход с линии
        mLine.setOnMouseExited(e -> {
            //Установить статус
            setStringLeftStatus("");
            notifyObservers("LeftStatusGo");
            newLine.setStroke(Color.DARKSLATEBLUE);
            newLine.setStrokeWidth(2);
            newLine.setOpacity(1);
            newLine.setVisible(false);
            lineOldAdd = false;
        });
        //нажата левая кнопка
        mLine.setOnMousePressed(e -> {
            timeLine = mLine;//выбрана данная линия, для построения
            lineOldAdd = true;//линия выбрана
            //Определить, разрешено ли перемещение линии
            if (findLineMove(mLine)) {
                //Вычислить смещение для перемещения всех линий
                // if (!createLine) {
                String[] nameId = findID(mLine).split("_");
                setDXStart(findCircle(nameId[0]).getCenterX() - e.getX());
                setDYStart(findCircle(nameId[0]).getCenterY() - e.getY());
                setDXEnd(findCircle(nameId[1]).getCenterX() - e.getX());
                setDYEnd(findCircle(nameId[1]).getCenterY() - e.getY());
                newLine.setVisible(false);
                //}
            }
        });
    }

    /**
     * Метод findPoindMove(String vertex).
     * Предназначен для поиска принадлежности точки линии
     *
     * @param vertex - ссылка на линию
     * @return - true-точка принадлежит линии
     */
    private boolean findPoindMove(String vertex) {
        return poindCircles.stream().filter(p -> p.getId().equals(vertex)).findFirst().filter(PoindCircle::isBLine).isEmpty();
    }

    /**
     * Метод indLineMove(Line line).
     * Предназначен для поиска разрешения на перемещение линии.
     *
     * @param line - ссылка на линию
     * @return - true - перемещение разрешено, false - перемещение запрещено
     */
    private boolean findLineMove(Line line) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (p.getLine().getId().equals(line.getId())) {
                    return p.isBMove();
                }
            }
        }
        return false;
    }


    /**
     * Метод createLineAdd(int segment).
     * Предназначен для создания линий. Вызывает метод createLine(segment). Добавляет линию на доску и в коллекцию.
     *
     * @param segment - определяет тип линии в коллекции
     * @return -возвращает объект Line.
     */
    Line createLineAdd(int segment) {
        Line l = createLine();//добавить линию
        paneBoards.getChildren().add(l);
        if (segment == 9 || segment == 8) {
            poindLines.add(new PoindLine(l, l.getId(), decartX, decartY, decartX, decartY, false, segment));
        } else {
            poindLines.add(new PoindLine(l, l.getId(), decartX, decartY, decartX, decartY, true, segment));

        }
        return l;
    }

    /**
     * Метод lineAddPoind(Line nl, boolean poindAdd2).
     * Предназначен для приклеивания конца линии к лежащим точкам.
     *
     * @param nl - объект линия.
     */
    public void lineAddPoind(Line nl) {
        Circle pCl;
        for (PoindCircle c : poindCircles) {
            if (c != null && nl != null) {
                pCl = c.getCircle();
                double d = distance(pCl.getCenterX(), pCl.getCenterY(), getScreenXY().getX(), getScreenXY().getY());
                if (d < 25) {
                    setScreenXY(new Point2D(pCl.getCenterX(), pCl.getCenterY()));
                    //Передать в View для вывода
                    line = nl;
                    notifyObservers("SideGo");
                    setPoindOld(true);
                    setTimeVer(pCl);
                    break;
                } else if (d > 40) {
                    setPoindOld(false);
                }

            }
        }
    }

    /**
     * Метод createMoveLine(Line line).
     * Предназначен для расчета и построения прямых и лучей.
     *
     * @param line    - ссылка на линию
     * @param rayLine - 3-прямая, 4 -луч
     */
    public void createMoveLine(Line line, int rayLine) {
        if (rayLine == 3) {
            newLine.setVisible(abs(getScreenXY().getX() - getSegmentStartX()) > 5 || abs(getScreenXY().getY() - getSegmentStartY()) > 5);
            double t1, t2;
            if (getScreenXY().getX() - getSegmentStartX() != 0) {
                t1 = (gridViews.getVr() - getSegmentStartX()) / (getScreenXY().getX() - getSegmentStartX());
                t2 = (-getSegmentStartX()) / (getScreenXY().getX() - getSegmentStartX());
            } else {
                t1 = (gridViews.getVb() - getSegmentStartY()) / (getScreenXY().getY() - getSegmentStartY());
                t2 = (-getSegmentStartY()) / (getScreenXY().getY() - getSegmentStartY());
            }

            setRayStartX(getSegmentStartX() + (getScreenXY().getX() - getSegmentStartX()) * t1);
            setRayStartY(getSegmentStartY() + (getScreenXY().getY() - getSegmentStartY()) * t1);
            setRayEndX(getSegmentStartX() + (getScreenXY().getX() - getSegmentStartX()) * t2);
            setRayEndY(getSegmentStartY() + (getScreenXY().getY() - getSegmentStartY()) * t2);
        }
        if (rayLine == 4) {
            newLine.setVisible(abs(getScreenXY().getX() - getSegmentStartX()) > 5 || abs(getScreenXY().getY() - getSegmentStartY()) > 5);
            double t1;
            if (getScreenXY().getX() - getSegmentStartX() != 0) {
                t1 = (gridViews.getVr() - getSegmentStartX()) / (getScreenXY().getX() - getSegmentStartX());
                if (t1 < 0) {
                    t1 = -getSegmentStartX() / (getScreenXY().getX() - getSegmentStartX());
                }
            } else {
                t1 = (gridViews.getVb() - getSegmentStartY()) / (getScreenXY().getY() - getSegmentStartY());
                if (t1 < 0) {
                    t1 = -getSegmentStartY() / (getScreenXY().getY() - getSegmentStartY());
                }
            }
            setRayStartX(getSegmentStartX());
            setRayStartY(getSegmentStartY());
            setRayEndX(getSegmentStartX() + (getScreenXY().getX() - getSegmentStartX()) * t1);
            setRayEndY(getSegmentStartY() + (getScreenXY().getY() - getSegmentStartY()) * t1);
        }
        //Передать в View для вывода
        setLine(line);
        notifyObservers("RayGo");
    }

    /**
     * Метод distance(double x1, double y1, double x2, double y2).
     * Предназначен для расчета расстояния между двумя вершинами, заданные координатами.
     *
     * @param x1 - координаты вершины x1
     * @param y1 - координаты вершины y1
     * @param x2 - координаты вершины x2
     * @param y2 - координаты вершины y2
     * @return возвращает длину.
     */
    public double distance(double x1, double y1, double x2, double y2) {
        return sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2));
    }

    /**
     * Метод treangleAdd(Point2D v1, Point2D v2, Point2D v3)
     * Предназначен для создания треугольников.
     *
     * @param v1 - вершина А
     * @param v2 - вершина В
     * @param v3 - вершина С
     */
    public Polygon treangleAdd(Circle v1, Circle v2, Circle v3, String nameTr) {
        Polygon treangle = new Polygon();
        treangle.getPoints().addAll(v1.getCenterX(), v1.getCenterY(), v2.getCenterX(), v2.getCenterY(),
                v3.getCenterX(), v3.getCenterY());
        treangle.setFill(Color.CHOCOLATE);
        treangle.setOpacity(0.2);
        treangle.setId(nameTr);
        polygonBindCircles(v1, v2, v3, treangle);
        treangleNames.add(new TreangleName(treangle, nameTr));
        //привязать событие мыши
        treangle.setOnMouseEntered(e -> {
            stringLeftStatus = STA_21 + nameSplitRemove(nameTr);
            notifyObservers("LeftStatusGo");
        });
        treangle.setOnMouseExited(e -> {
            stringLeftStatus = " ";
            notifyObservers("LeftStatusGo");
        });
        treangle.setOnMousePressed(e -> timeTreangle = treangle);
        return treangle;
    }


    /**
     * Метод findNameId(String name, String linaA)
     * Предназначен для поиска по коллекции PoindLine для замены имени отрезка в коллекции
     *
     * @param name  - имя отрезка (а) для замены (на А_В)
     * @param linaA - линия а
     */
    public void findNameId(String name, String linaA) {
        poindLines.forEach(p -> {
            if (p != null) {
                if (p.getId().equals(linaA)) {
                    p.setId(name);
                }
            }
        });
    }

    /**
     * Метод findCircle(String c)
     * Предназначен для поиска в коллекции точек объектов Circle по имени.
     * Вызывается из метода createNameShapes() при создании объекта text, для перемещения.
     *
     * @param c - имя точки
     * @return объект Circle или null если не найден.
     */
    Circle findCircle(String c) {
        for (PoindCircle c0 : poindCircles) {
            if (c0 != null) {
                if (c0.getId().equals(String.valueOf(c))) {
                    return c0.getCircle();
                }
            }
        }
        return null;//ничего не найдено
    }

    /**
     * Метод findLineVertex(String s).
     * Предназначен для поиска в коллекции линии по названию вершин (А_В)
     *
     * @param s - имя вершин типа А_В
     * @return - возвращает ссылку на линию
     */
    Line findLineVertex(String s) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (p.getId().equals(s)) {
                    return p.getLine();
                }
            }
        }
        return null;
    }

    /**
     * Метод findCirclesUpdateXY(String id).
     * Предназначен для поиска объектов в коллекции PoindCircle по имени и
     * замены мировых координат
     *
     * @param id - имя объекта
     * @param x  - мировые координаты точки
     * @param y  -мировые координаты точки
     */
    public void findCirclesUpdateXY(String id, double x, double y) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    p.setXY(new Point2D(x, y));
                }
            }
        }
    }

    /**
     * Метод findLinesUpdateXY(String id).
     * Предназначен для замены мировых координат при построении отрезков, лучей и прямых
     *
     * @param id - имя линии до замены
     *           Особенность метода, должен всегда вызываться до метода findNameId(Circle1, Circle2, Line), который
     *           меняет имя в коллекции PoindLines.
     */
    public void findLinesUpdateXY(String id) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (p.getLine().getId().equals(id)) {
                    p.setStX(gridViews.revAccessX(p.getLine().getStartX()));
                    p.setStY(gridViews.revAccessY(p.getLine().getStartY()));
                    p.setEnX(gridViews.revAccessX(p.getLine().getEndX()));
                    p.setEnY(gridViews.revAccessY(p.getLine().getEndY()));
                }
            }
        }

    }

    /**
     * Метод findPoindCircleMove(String id).
     * Предназначен для поиска по коллекции PoindCircle по имени точки разрешения на
     * перемещение точки по доске.
     *
     * @param id - имя точки
     * @return true- перемещение точки разрешено, false - точка расчетная, перемещение запрещено
     */
    private boolean findPoindCircleMove(String id) {
        boolean bfMove = false;//всегда запрещено
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getId().equals(id)) {
                    bfMove = p.isBMove();//определяется в коллекции
                }
            }
        }
        return bfMove;
    }

    /**
     * Метод createVertexAdd(String arc).
     * Предназначен для создания нового объекта дуги, которая задается в виде
     * трех вершин состоящих из точек. Для расчета параметров дуги и вывода на доску
     * вызывается метод arcVertex(o1,o2,o3,arcNew), о1, о2, о3 - точки вершин из класса Circle.
     * arcNew - новая дуга.
     *
     * @param arc - срока для добавления дуги, состоит из имен вершин, угол АВС
     * @return - возвращает объект дугу
     */
    Arc createVertexAdd(Circle o1, Circle o2, Circle o3, String arc) {
        Arc arcNew = new Arc();
        arcNew.setId(arc);
        arcNew.setType(ArcType.ROUND);//тип арки
        arcNew.setOpacity(0.5);//прозрачность
        setColorStroke(Color.DARKSLATEBLUE);
        setColorFill(Color.DEEPSKYBLUE);
        setShapeColor(arcNew);//установить цвет
        notifyObservers("StrokeShape"); //Передаем в View для вывода
        notifyObservers("FillShape");
        //Контекстное меню
        newContextMenu(arcNew, 3);
        arcVertexGo(o1, o2, o3, arcNew);//вывести на доску
        String nameAngle = indexAngledAdd();//увеличить индекс
        nameArcAdd(o2, nameAngle, arcNew);//вывести имя угла
        //добавить в коллекцию дуг
        vertexArcs.add(new VertexArc(arcNew, arc, nameAngle, gridViews.revAccessX(o2.getCenterX()), gridViews.revAccessY(o2.getCenterY()), arcRadius,
                arcRadius, angleStart, angleLength, false));
        //При наведении мышки на дугу, вывод статусной строки
        arcNew.setOnMouseEntered(e -> {
            //Установить статус "Угол + выбранный угол + длина дуги в градусах"
            setStringLeftStatus(STA_16 + arcNew.getId() + " = " + arcNew.getLength() + " гр.");
            notifyObservers("LeftStatusGo");
        });
        //При выходе мышки из дуги, сбросить статусную стоку.
        arcNew.setOnMouseExited(e -> {
            setStringLeftStatus("");
            notifyObservers("LeftStatusGo");
        });
        return arcNew;
    }

    /**
     * Метод arcVertex(Circle o1, Circle o2, Circle o3, double r).
     * Предназначен для расчета угла по координатам трех точек. Построения дуги, перемещения дуги.
     *
     * @param o1 -первая точка А
     * @param o2 - вторая точка В (центр угла)
     * @param o3 - третья точка С
     *           <p>
     *           Устанавливает, для класса View, следующие переменные:
     *           angleLength - длину дуги в градусах
     *           arcRadius - радиус дуги
     *           angleStart - начальный угол в градусах
     *           screenX screenY - координаты центра дуги
     */
    public void arcVertex(Circle o1, Circle o2, Circle o3) {
        //Длина дуги в градусах
        Point2D pA = new Point2D(o1.getCenterX(), o1.getCenterY());
        Point2D pB = new Point2D(o2.getCenterX(), o2.getCenterY());
        Point2D pC = new Point2D(o3.getCenterX(), o3.getCenterY());
        double angleABC = angleTriangle(pB, pA, pC);//размер угла в градусах
        angleLength = angleABC;
        //Начальный угол в
        double arcStart = angleVector(o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        double str = areaTriangle(o2.getCenterX() + 200, o2.getCenterY(), o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        double str1 = areaTriangle(o1.getCenterX(), o1.getCenterY(), o2.getCenterX(), o2.getCenterY(), o3.getCenterX(), o3.getCenterY());
        if (str < 0) {
            arcStart = 360 - arcStart;
        }
        if (str1 > 0) {
            arcStart = arcStart - angleABC;
        }
        angleStart = arcStart;
    }

    /**
     * Метод arcVertexGo(Circle o1, Circle o2, Circle o3, Arc arc).
     * Предназначен для вывода на экран арки угла.
     *
     * @param o1  - первая вершина А
     * @param o2  - вторая вершина В
     * @param o3  - третья вершина С
     * @param arc - угол АВС
     */
    public void arcVertexGo(Circle o1, Circle o2, Circle o3, Arc arc) {
        arcVertex(o1, o2, o3);//рассчитать угол
        //Запомнить текущие координаты мышки
        double stX = getScreenXY().getX();
        double stY = getScreenXY().getY();
        //Заменить для построения арки угла
        setScreenXY(new Point2D(o2.getCenterX(), o2.getCenterY()));
        //Передать в View для вывода
        arcGo = arc;
        notifyObservers("ArcGo");
        //Восстановить текущие координаты мышки
        setScreenXY(new Point2D(stX, stY));
    }

    /**
     * Метод findArcUpdate(String s)
     * Предназначен для поиска дуги по вершине угла и изменения в коллекции дуг
     * мировых координат после перемещения дуги.
     *
     * @param arc- ссылка на арку
     */
    private void findArcUpdate(Arc arc) {
        for (VertexArc v : vertexArcs) {
            if (v != null) {
                if (v.getId().equals(arc.getId())) {
                    v.setCenterX(gridViews.revAccessX(arc.getCenterX()));
                    v.setCenterY(gridViews.revAccessY(arc.getCenterY()));
                    v.setStartAngle(arc.getStartAngle());
                    v.setLengthAngle(arc.getLength());
                }
            }
        }
    }

    /**
     * Метод angleTriangle(Point2D p1, Point2D p2, Point2D p3).
     * Предназначен для расчета угла по координатам вершин.
     *
     * @param p1 - координаты центральной вершины
     * @param p2 - координаты первой боковой вершины
     * @param p3 - координаты второй боковой вершины
     * @return угол в градусах с точностью до десятых градуса.
     */
    private double angleTriangle(Point2D p1, Point2D p2, Point2D p3) {
        return Math.round(p1.angle(p2, p3));
    }

    /**
     * Метод angleVector(double X, double Y, double X1, double Y1).
     * Предназначен для определения угла наклона вектора.
     *
     * @param X  -координата начала вектора
     * @param Y  - координата начала вектора
     * @param X1 -координата конца вектора
     * @param Y1 координата конца вектора
     * @return - возвращает угол наклона вектора
     */
    private double angleVector(double X, double Y, double X1, double Y1) {
        Point2D p1 = new Point2D(100, 0);
        Point2D p2 = new Point2D(X1 - X, Y1 - Y);
        return p1.angle(p2);
    }

    /**
     * Метод areaTriangle(double x1, double y1, double x2, double y2, double x3, double y3).
     * Предназначен для расчета площади треугольника по координатам трех вершин
     *
     * @param x1 - координаты вершины x1
     * @param y1 - координаты вершины y1
     * @param x2 - координаты вершины x2
     * @param y2 - координаты вершины y2
     * @param x3 - координаты вершины x3
     * @param y3 - координаты вершины y3
     * @return возвращает площадь треугольника.
     */
    private double areaTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        return ((x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1)) / 2;
    }

    /**
     * Метод lineBindCircles(Circle c1, Circle c2, Line l)
     * Метод двунаправленного связывания точек начала и конца линии с самой линией.
     * Используется при создании отрезков и треугольников.
     *
     * @param c1 - точка начала отрезка
     * @param c2 - точка конца отрезка
     * @param l  - линия между этими точками
     */
    public void lineBindCircles(Circle c1, Circle c2, Line l) {
        l.startXProperty().bindBidirectional(c1.centerXProperty());
        l.startYProperty().bindBidirectional(c1.centerYProperty());
        l.endXProperty().bindBidirectional(c2.centerXProperty());
        l.endYProperty().bindBidirectional(c2.centerYProperty());
        c1.centerXProperty().addListener((obj, oldValue, newValue) -> findLinesUpdateXY(l.getId()));
        c1.centerYProperty().addListener((obj, oldValue, newValue) -> findLinesUpdateXY(l.getId()));
        c2.centerXProperty().addListener((obj, oldValue, newValue) -> findLinesUpdateXY(l.getId()));
        c2.centerYProperty().addListener((obj, oldValue, newValue) -> findLinesUpdateXY(l.getId()));
    }

    /**
     * Метод verticalBindCircles(Circle c, Line l)
     * Предназначен для связывания перпендикуляра с прямой, перемещается только точка
     *
     * @param c1 - объект точка из которой опущен перпендикуляр
     * @param c2 - объект точка на прямой
     * @param c3 - объект точка на прямой
     * @param c4 - объект точка пересечения перпендикуляра с прямой
     * @param l  - объект линия перпендикуляр
     */
    public void verticalBindCircles(Circle c1, Circle c2, Circle c3, Circle c4, Line l) {
        l.startXProperty().bindBidirectional(c1.centerXProperty());
        l.startYProperty().bindBidirectional(c1.centerYProperty());
        l.startYProperty().addListener((obj, oldValue, newValue) -> verticalUpdateCircle(c1, c2, c3, c4, l));
        l.startXProperty().addListener((obj, oldValue, newValue) -> verticalUpdateCircle(c1, c2, c3, c4, l));
        c2.centerXProperty().addListener((obj, oldValue, newValue) -> verticalUpdateCircle(c1, c2, c3, c4, l));
        c2.centerYProperty().addListener((obj, oldValue, newValue) -> verticalUpdateCircle(c1, c2, c3, c4, l));
        c3.centerXProperty().addListener((obj, oldValue, newValue) -> verticalUpdateCircle(c1, c2, c3, c4, l));
        c3.centerYProperty().addListener((obj, oldValue, newValue) -> verticalUpdateCircle(c1, c2, c3, c4, l));

    }

    /**
     * Метод verticalUpdateCircle(Circle c1, Circle c2,Circle c3, Circle c4, Line l)
     * Вспомогательный метод для пересчета точки пересечения перпендикуляра с прямой.
     * Вызывается из метода verticalBindCircles().
     *
     * @param c1 - объект точка из которой опущен перпендикуляр
     * @param c2 - объект точка на прямой
     * @param c3 - объект точка на прямой
     * @param c4 - объект точка пересечения перпендикуляра с прямой
     * @param l  - объект линия перпендикуляр
     */
    private void verticalUpdateCircle(Circle c1, Circle c2, Circle c3, Circle c4, Line l) {
        Point2D A1 = new Point2D(c1.getCenterX(), c1.getCenterY());
        Point2D B1 = new Point2D(c2.getCenterX(), c2.getCenterY());
        Point2D C1 = new Point2D(c3.getCenterX(), c3.getCenterY());
        Point2D D1 = heightPoind(A1, B1, C1);
        l.setEndX(D1.getX());
        l.setEndY(D1.getY());
        c4.setCenterX(D1.getX());
        c4.setCenterY(D1.getY());
        //Обновляем мировые координаты в коллекциях
        findMedianaUpdateXY(c4, l);
    }

    /**
     * Метод textBindCircle(Circle c, Text txt, int dx, int dy).
     * Предназначен для связывания место расположения надписи с точкой.
     *
     * @param c   - объект круг.
     * @param txt - объект имя точки.
     * @param dx  - смещения имени от центра точки.
     * @param dy  - смещение имени от центра точки.
     */
    private void textBindCircle(Circle c, Text txt, int dx, int dy) {
        txt.xProperty().bind(c.centerXProperty().add(dx));
        txt.yProperty().bind(c.centerYProperty().add(dy));
    }

    /**
     * Метод parallelBindLine(Circle b, Circle c, Circle a, Circle d).
     * Предназначен для связывания прямой с параллельной прямой.
     *
     * @param cA - точка на прямой начало
     * @param cB - точка на прямой, конец с-прямая относительно которой построена параллельная прямая
     * @param cC - точка через которую проходит параллельная прямая
     * @param cD - точка на параллельной прямой расчетная
     */
    public void parallelBindLine(Circle cA, Circle cB, Circle cC, Circle cD) {
        cA.centerXProperty().addListener((obj, OldValue, newValue) -> accessParalel(cA, cB, cC, cD));
        cA.centerYProperty().addListener((obj, OldValue, newValue) -> accessParalel(cA, cB, cC, cD));
        cB.centerXProperty().addListener((obj, OldValue, newValue) -> accessParalel(cA, cB, cC, cD));
        cB.centerYProperty().addListener((obj, OldValue, newValue) -> accessParalel(cA, cB, cC, cD));
        cC.centerXProperty().addListener((obj, OldValue, newValue) -> accessParalel(cA, cB, cC, cD));
        cC.centerYProperty().addListener((obj, OldValue, newValue) -> accessParalel(cA, cB, cC, cD));

    }

    /**
     * Метод accessParalel(Circle cA, Circle cB, Circle cC, Circle cD).
     * Предназначен для расчета точки для параллельной прямой.
     *
     * @param cA - первая точка прямой
     * @param cB - вторая точка прямой
     * @param cC - первая точка параллельной прямой
     * @param cD - расчетная точка параллельной прямой
     */
    private void accessParalel(Circle cA, Circle cB, Circle cC, Circle cD) {
        //Рассчитать координаты новой точки
        double a = cB.getCenterY() - cA.getCenterY();
        double b = cA.getCenterX() - cB.getCenterX();
        double x = 4000;
        double y;
        if (b != 0) {
            y = (a * (cC.getCenterX() - 4000) + b * cC.getCenterY()) / b;
        } else {
            y = a * (cC.getCenterX() - 4000);
        }
        double dx = getScreenXY().getX();
        double dy = getScreenXY().getY();
        setScreenXY(new Point2D(x, y));
        setVertex(cD);
        notifyObservers("VertexGo");
        setScreenXY(new Point2D(dx, dy));
        findCirclesUpdateXY(cD.getId(), gridViews.revAccessX(x), gridViews.revAccessY(y));
    }

    /**
     * Метод textUnBindCircle(Text txt).
     * Предназначен для отключения связи имени точки с текстом.
     *
     * @param txt - объект имя точки.
     */
    private void textUnBindCircle(Text txt) {
        txt.xProperty().unbind();
        txt.yProperty().unbind();
    }

    /**
     * Метод rayBindCircles(Circle cStart, Circle cEnd, Line ray)
     * Метод создания двунаправленного связывания точки начала луча и начала линии,
     * а также однонаправленного связывания второй точки на луче с окончанием линии.
     *
     * @param cStart - точка начала луча
     * @param cEnd   - вторая точка на луче
     * @param ray    -прямая от начала луча через вторую точку
     *               Вызывает метод rayLineX(cStart, cEnd) и rayLineY(cStart, cEnd) для расчета окончания прямой при построении.
     */
    public void rayBindCircles(Circle cStart, Circle cEnd, Line ray) {
        ray.startXProperty().bindBidirectional(cStart.centerXProperty());
        ray.startYProperty().bindBidirectional(cStart.centerYProperty());
        //Расчет конца луча
        ray.startYProperty().addListener((obj, oldValue, newValue) -> {
            updateStartEndLine(cStart, cEnd, ray, 2);
            findLinesUpdateXY(ray.getId());
        });
        ray.startXProperty().addListener((obj, oldValue, newValue) -> {
            updateStartEndLine(cStart, cEnd, ray, 2);
            findLinesUpdateXY(ray.getId());
        });
        //Точка на луче
        cEnd.centerXProperty().addListener((obj, oldValue, newValue) -> {
            updateStartEndLine(cStart, cEnd, ray, 2);
            findLinesUpdateXY(ray.getId());
        });
        cEnd.centerYProperty().addListener((obj, oldValue, newValue) -> {
            updateStartEndLine(cStart, cEnd, ray, 2);
            findLinesUpdateXY(ray.getId());
        });
    }

    /**
     * Метод polygonBindCircles(Circle c1, Circle c2, Circle c3, Polygon treangle).
     * Предназначен для однонаправленного связывания точек треугольника с вершинами
     * многоугольника.
     *
     * @param c1       - вершина треугольника
     * @param c2       - вершина треугольника
     * @param c3       - вершина треугольника
     * @param treangle - многоугольник в форме треугольника
     */
    private void polygonBindCircles(Circle c1, Circle c2, Circle c3, Polygon treangle) {
        c1.centerXProperty().addListener((obj, oldValue, newValue) -> treangle.getPoints().set(0, c1.getCenterX()));
        c1.centerYProperty().addListener((obj, oldValue, newValue) -> treangle.getPoints().set(1, c1.getCenterY()));
        c2.centerXProperty().addListener((obj, oldValue, newValue) -> treangle.getPoints().set(2, c2.getCenterX()));
        c2.centerYProperty().addListener((obj, oldValue, newValue) -> treangle.getPoints().set(3, c2.getCenterY()));
        c3.centerXProperty().addListener((obj, oldValue, newValue) -> treangle.getPoints().set(4, c3.getCenterX()));
        c3.centerYProperty().addListener((obj, oldValue, newValue) -> treangle.getPoints().set(5, c3.getCenterY()));
    }

    /**
     * Метод arcBindPoind(Circle c1, Circle c2, Circle c3, Arc a)
     * Метод двунаправленного связывания центра угла с точкой, а также однонаправленного связывания
     * двух точек угла с расчетными размерами угла. А также связывания имени угла.
     *
     * @param c1  - ссылка на вершину А
     * @param c2  - ссылка на вершину В
     * @param c3  - ссылка на вершину С
     * @param arc - объект арка дуги угла.
     */
    public void arcBindPoind(Circle c1, Circle c2, Circle c3, Arc arc) {
        c2.centerXProperty().bindBidirectional(arc.centerXProperty());
        c2.centerYProperty().bindBidirectional(arc.centerYProperty());
        arc.centerXProperty().addListener((obj, oldValue, newValue) -> {
            arcVertexGo(c1, c2, c3, arc);//новый угол
            nameArcShow(c2, arc, Objects.requireNonNull(findArcNameAngle(arc.getId())));//новое место имени угла
            //Добавить новые данные коллекцию VertexArc
            findArcUpdate(arc);

        });
        arc.centerYProperty().addListener((obj, oldValue, newValue) -> {
            arcVertexGo(c1, c2, c3, arc);//новый угол
            nameArcShow(c2, arc, Objects.requireNonNull(findArcNameAngle(arc.getId())));//новое место имени угла
            findArcUpdate(arc);
        });
        c1.centerXProperty().addListener((obj, oldValue, newValue) -> {
            arcVertexGo(c1, c2, c3, arc);
            nameArcShow(c2, arc, Objects.requireNonNull(findArcNameAngle(arc.getId())));//новое место имени угла
            findArcUpdate(arc);
        });
        c3.centerXProperty().addListener((obj, oldValue, newValue) -> {
            arcVertexGo(c1, c2, c3, arc);
            nameArcShow(c2, arc, Objects.requireNonNull(findArcNameAngle(arc.getId())));//новое место имени угла
            findArcUpdate(arc);
        });
    }

    /**
     * Метод findArcNameAngle()
     * Предназначен для поиска имени угла по вершине. Вызывается из метода связывания угла.
     *
     * @param id - имя вершины
     * @return - объект текст (имя угла)
     */
    private Text findArcNameAngle(String id) {
        String nAngle;
        for (VertexArc p : vertexArcs) {
            if (p != null) {
                if (p.getArc().getId().equals(id)) {
                    nAngle = p.getNameAngle();
                    for (NamePoindLine n : namePoindLines) {
                        if (n != null) {
                            if (n.getText().getId().equals(nAngle)) {
                                return n.getText();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Метод findTypeLine(Line line).
     * Предназначен для поиска в коллекции и возвращения типа линии
     * Необходим для проверки при построении середины отрезка
     *
     * @param line - ссылка на линию
     * @return - тип линии, нужно 0 - отрезок
     */
    public int findTypeLine(Line line) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (p.getLine().getId().equals(line.getId())) {
                    return p.getSegment();
                }
            }
        }
        return -1;
    }

    /**
     * Метод tangentBindCircles(Circle A, Circle B, Circle C, Line tangentLine).
     * Метод однонаправленного связывания точки окружности и касательной к окружности.
     *
     * @param A           - объект окружность
     * @param B           - объект точка на окружности
     * @param C           - объект расчетная точка касательной
     * @param tangentLine - объект касательная
     */
    private void tangentBindCircles(Circle A, Circle B, Circle C, Line tangentLine) {
        B.centerXProperty().addListener((obj, oldValue, newValue) -> {
            //рассчитать новую точку
            Point2D a = new Point2D(gridViews.revAccessX(A.getCenterX()), gridViews.revAccessY(A.getCenterY()));
            Point2D b = new Point2D(gridViews.revAccessX(B.getCenterX()), gridViews.revAccessY(B.getCenterY()));
            double r = distance(a.getX(), a.getY(), b.getX(), b.getY());
            Point2D pd = tangentCircle(a, b, r);
            C.setCenterX(gridViews.accessX(pd.getX()));
            C.setCenterY(gridViews.accessY(pd.getY()));
            updateStartEndLine(B, C, tangentLine, 1);
            findLinesUpdateXY(tangentLine.getId());
        });
        B.centerYProperty().addListener((obj, oldValue, newValue) -> {
            //рассчитать новую точку
            Point2D a = new Point2D(gridViews.revAccessX(A.getCenterX()), gridViews.revAccessY(A.getCenterY()));
            Point2D b = new Point2D(gridViews.revAccessX(B.getCenterX()), gridViews.revAccessY(B.getCenterY()));
            double r = distance(a.getX(), a.getY(), b.getX(), b.getY());
            Point2D pd = tangentCircle(a, b, r);
            C.setCenterX(gridViews.accessX(pd.getX()));
            C.setCenterY(gridViews.accessY(pd.getY()));
            updateStartEndLine(B, C, tangentLine, 1);
            findLinesUpdateXY(tangentLine.getId());
        });
    }

    /**
     * Метод circlesBindLine(Circle cStart, Circle cEnd, Line l)
     * Метод однонаправленного связывания двух точек на прямой с прямой и расчетом начала и конца прямой.
     * Для расчета начала и конца прямой вызываются методы:
     * rayLineX() и rayLineY()
     *
     * @param cStart - первая точка на прямой
     * @param cEnd   - вторая точка на прямой
     * @param l      - прямая
     */
    public void circlesBindLine(Circle cStart, Circle cEnd, Line l) {
        //Точка на прямой
        cEnd.centerXProperty().addListener((obj, oldValue, newValue) -> {
            updateStartEndLine(cStart, cEnd, l, 1);
            findLinesUpdateXY(l.getId());
        });
        cEnd.centerYProperty().addListener((obj, oldValue, newValue) -> {
            updateStartEndLine(cStart, cEnd, l, 1);
            findLinesUpdateXY(l.getId());
        });
        cStart.centerXProperty().addListener((obj, oldValue, newValue) -> {
            updateStartEndLine(cStart, cEnd, l, 1);
            findLinesUpdateXY(l.getId());
        });
        cStart.centerYProperty().addListener((obj, oldValue, newValue) -> {
            updateStartEndLine(cStart, cEnd, l, 1);
            findLinesUpdateXY(l.getId());
        });
    }

    /**
     * Метод updateStartEndLine(Circle cStart, Circle cEnd, Line l).
     * Для обновления начала и конца прямой, а также обновления конца луча.
     *
     * @param cStart - первая точка на прямой
     * @param cEnd   - вторая точка на прямой
     * @param l      - прямая
     * @param n      - 1 - прямая, 2 - луч
     */
    private void updateStartEndLine(Circle cStart, Circle cEnd, Line l, int n) {
        //Для прямой
        if (n == 1) {
            double t1, t2;
            if (cEnd.getCenterX() - cStart.getCenterX() != 0) {
                t1 = (gridViews.getVr() - cStart.getCenterX()) / (cEnd.getCenterX() - cStart.getCenterX());
                t2 = (-cStart.getCenterX()) / (cEnd.getCenterX() - cStart.getCenterX());
            } else {
                t1 = (gridViews.getVb() - cStart.getCenterY()) / (cEnd.getCenterY() - cStart.getCenterY());
                t2 = (-cStart.getCenterY()) / (cEnd.getCenterY() - cStart.getCenterY());
            }
            setRayStartX(cStart.getCenterX() + (cEnd.getCenterX() - cStart.getCenterX()) * t1);
            setRayStartY(cStart.getCenterY() + (cEnd.getCenterY() - cStart.getCenterY()) * t1);
            setRayEndX(cStart.getCenterX() + (cEnd.getCenterX() - cStart.getCenterX()) * t2);
            setRayEndY(cStart.getCenterY() + (cEnd.getCenterY() - cStart.getCenterY()) * t2);
        }
        //Для луча
        if (n == 2) {
            double t1;
            if (cEnd.getCenterX() - cStart.getCenterX() != 0) {
                t1 = (gridViews.getVr() - cStart.getCenterX()) / (cEnd.getCenterX() - cStart.getCenterX());
                if (t1 < 0) {
                    t1 = (-cStart.getCenterX()) / (cEnd.getCenterX() - cStart.getCenterX());
                }
            } else {
                t1 = (gridViews.getVb() - cStart.getCenterY()) / (cEnd.getCenterY() - cStart.getCenterY());
                if (t1 < 0) {
                    t1 = (-cStart.getCenterY()) / (cEnd.getCenterY() - cStart.getCenterY());
                }
            }
            setRayStartX(cStart.getCenterX());
            setRayStartY(cStart.getCenterY());
            setRayEndX(cStart.getCenterX() + (cEnd.getCenterX() - cStart.getCenterX()) * t1);
            setRayEndY(cStart.getCenterY() + (cEnd.getCenterY() - cStart.getCenterY()) * t1);
        }
        //Передать в View для вывода
        setLine(l);
        notifyObservers("RayGo");
    }

    /**
     * Метод createMedianaBisectorHeight(Circle c, Circle c1, Circle c2,Point2D mc, int i).
     * Предназначен для построения отрезка медианы, биссектрисы, высоты и точки на противолежащей стороне от вершины,
     * из которой проводится медиана
     *
     * @param c  - объект вершина из которой проводится медиана, биссектриса и высота.
     * @param c1 - объект боковая вершина угла.
     * @param c2 - объект боковая вершина угла.
     * @param mc - объект точка расчетная для медианы, биссектрисы и высоты.
     * @param i  - номер объекта в коллекции PoindCircle (4- медиана, 5 - биссектриса, 6 - высота)
     */
    private Line createMedianaBisectorHeight(Circle c, Circle c1, Circle c2, Point2D mc, int i) {
        Line lineTreangle = createLineAdd(i);//создать новую линию
        Circle newPoindTreangle = createPoindAdd(false);//создать новую расчетную точку
        setScreenXY(new Point2D(mc.getX(), mc.getY()));
        //Передать в View для вывода
        vertex = newPoindTreangle;
        notifyObservers("VertexGo");
        findCirclesUpdateXY(newPoindTreangle.getId(), gridViews.revAccessX(getScreenXY().getX()), gridViews.revAccessY(getScreenXY().getY()));
        segmentStartX = c.getCenterX();
        segmentStartY = c.getCenterY();
        line = lineTreangle;
        notifyObservers("SideGo");
        findLinesUpdateXY(lineTreangle.getId());
        //paneBoards.getChildren().addAll(newLineTreangle, newPoindTreangle);//добавить на доску
        lineTreangle.toBack();
        findNameId(c.getId() + "_" + newPoindTreangle.getId(), lineTreangle.getId());
        //Связывание созданных отрезков и точки с вершинами треугольника
        switch (i) {
            case 4 -> mbhBindCircles(c, c1, c2, newPoindTreangle, lineTreangle, 4);
            case 5 -> mbhBindCircles(c, c1, c2, newPoindTreangle, lineTreangle, 5);
            case 6 -> mbhBindCircles(c, c1, c2, newPoindTreangle, lineTreangle, 6);
        }
        return lineTreangle;
    }

    /**
     * Метод mbh(Circle c, Circle c1, Circle c2, Circle md, Line lm, int nl).
     * Предназначен для связывания медианы, биссектрисы и высоты с вершинами треугольника.
     *
     * @param c  - объект вершина треугольника
     * @param c1 - объект вершина треугольника
     * @param c2 - объект вершина треугольника
     * @param md - объект точка пересечения биссектрисы со стороной треугольника
     * @param lm - отрезок биссектриса
     * @param nl - код точки (4- медиана, 5 - биссектриса, 6 - высота)
     */
    private void mbhBindCircles(Circle c, Circle c1, Circle c2, Circle md, Line lm, int nl) {
        c.centerXProperty().bindBidirectional(lm.startXProperty());
        c.centerYProperty().bindBidirectional(lm.startYProperty());

        c.centerXProperty().addListener((obj, oldValue, newValue) -> {
            Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
            Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
            Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
            Point2D mc;
            switch (nl) {
                case 4 -> mc = p1.midpoint(p2);
                case 5 -> mc = bisectorPoind(p1, p3, p2);
                case 6 -> mc = heightPoind(p3, p2, p1);
                default -> throw new IllegalStateException("Неопределенно значение: " + nl);
            }
            md.setCenterX(mc.getX());
            lm.setEndX(mc.getX());
            md.setCenterY(mc.getY());
            lm.setEndY(mc.getY());
            findMedianaUpdateXY(md, lm);
        });
        c.centerXProperty().addListener((obj, oldValue, newValue) -> {
            Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
            Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
            Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
            Point2D mc;
            switch (nl) {
                case 4 -> mc = p1.midpoint(p2);
                case 5 -> mc = bisectorPoind(p1, p3, p2);
                case 6 -> mc = heightPoind(p3, p2, p1);
                default -> throw new IllegalStateException("Неопределенно значение: " + nl);
            }
            md.setCenterX(mc.getX());
            lm.setEndX(mc.getX());
            md.setCenterY(mc.getY());
            lm.setEndY(mc.getY());
            findMedianaUpdateXY(md, lm);
        });

        c1.centerXProperty().addListener((obj, oldValue, newValue) -> {
            Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
            Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
            Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
            Point2D mc;
            switch (nl) {
                case 4 -> mc = p1.midpoint(p2);
                case 5 -> mc = bisectorPoind(p1, p3, p2);
                case 6 -> mc = heightPoind(p3, p2, p1);
                default -> throw new IllegalStateException("Неопределенно значение: " + nl);
            }
            md.setCenterX(mc.getX());
            lm.setEndX(mc.getX());
            md.setCenterY(mc.getY());
            lm.setEndY(mc.getY());
            findMedianaUpdateXY(md, lm);
        });
        c1.centerYProperty().addListener((obj, oldValue, newValue) -> {
            Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
            Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
            Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
            Point2D mc;
            switch (nl) {
                case 4 -> mc = p1.midpoint(p2);
                case 5 -> mc = bisectorPoind(p1, p3, p2);
                case 6 -> mc = heightPoind(p3, p2, p1);
                default -> throw new IllegalStateException("Неопределенно значение: " + nl);
            }
            md.setCenterX(mc.getX());
            lm.setEndX(mc.getX());
            md.setCenterY(mc.getY());
            lm.setEndY(mc.getY());
            findMedianaUpdateXY(md, lm);
        });
        c2.centerXProperty().addListener((obj, oldValue, newValue) -> {
            Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
            Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
            Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
            Point2D mc;
            switch (nl) {
                case 4 -> mc = p1.midpoint(p2);
                case 5 -> mc = bisectorPoind(p1, p3, p2);
                case 6 -> mc = heightPoind(p3, p2, p1);
                default -> throw new IllegalStateException("Неопределенно значение: " + nl);
            }
            md.setCenterX(mc.getX());
            lm.setEndX(mc.getX());
            md.setCenterY(mc.getY());
            lm.setEndY(mc.getY());
            findMedianaUpdateXY(md, lm);
        });
        c2.centerYProperty().addListener((obj, oldValue, newValue) -> {
            Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
            Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
            Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
            Point2D mc;
            switch (nl) {
                case 4 -> mc = p1.midpoint(p2);
                case 5 -> mc = bisectorPoind(p1, p3, p2);
                case 6 -> mc = heightPoind(p3, p2, p1);
                default -> throw new IllegalStateException("Неопределенно значение: " + nl);
            }
            md.setCenterX(mc.getX());
            lm.setEndX(mc.getX());
            md.setCenterY(mc.getY());
            lm.setEndY(mc.getY());
            findMedianaUpdateXY(md, lm);
        });
    }

    /**
     * Метод findMedianaUpdateXY(Circle md, Line lm).
     * Предназначен для обновления мировых координат точки и линии окончания медианы в коллекциях.
     *
     * @param md - объект точка медианы.
     * @param lm - объект линия медианы.
     */
    private void findMedianaUpdateXY(Circle md, Line lm) {
        for (PoindCircle p : poindCircles) {
            if (p != null) {
                if (p.getCircle().getId().equals(md.getId())) {
                    p.setXY(new Point2D(gridViews.revAccessX(md.getCenterX()), gridViews.revAccessY(md.getCenterY())));
                }
            }
        }
        for (PoindLine pl : poindLines) {
            if (pl != null) {
                if (pl.getLine().getId().equals(lm.getId())) {
                    pl.setEnX(gridViews.revAccessX(lm.getEndX()));
                    pl.setEnY(gridViews.revAccessY(lm.getEndY()));
                }
            }
        }
    }

    /**
     * Метод mbhLineAdd(Circle c)
     * Предназначен для проведения медианы, биссектрисы и высоты треугольника.
     *
     * @param c  - вершина треугольника, из которой надо провести высоту
     * @param nl - код линии: 4-медиана 5-биссектриса 6- высота
     * @return - объект медиана, биссектриса или высота.
     */
    public Line mbhLineAdd(Circle c, int nl) {
        Line newHeight = null;
        Point2D mc;
        for (TreangleName tn : treangleNames) {
            if (tn != null) {
                String[] vertex = tn.getID().split("_");
                if (c.getId().equals(vertex[0])) {
                    Circle c1 = findCircle(vertex[1]);
                    Circle c2 = findCircle(vertex[2]);
                    Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
                    Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
                    Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
                    switch (nl) {
                        case 4 -> mc = p1.midpoint(p2);
                        case 5 -> mc = bisectorPoind(p1, p3, p2);
                        case 6 -> mc = heightPoind(p3, p2, p1);
                        default -> throw new IllegalStateException("Неопределенно значение: " + nl);
                    }
                    newHeight = createMedianaBisectorHeight(c, c1, c2, mc, nl);
                } else if (c.getId().equals(vertex[1])) {
                    Circle c1 = findCircle(vertex[0]);
                    Circle c2 = findCircle(vertex[2]);
                    Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
                    Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
                    Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
                    switch (nl) {
                        case 4 -> mc = p1.midpoint(p2);
                        case 5 -> mc = bisectorPoind(p1, p3, p2);
                        case 6 -> mc = heightPoind(p3, p2, p1);
                        default -> throw new IllegalStateException("Неопределенно значение: " + nl);
                    }
                    newHeight = createMedianaBisectorHeight(c, c1, c2, mc, nl);
                } else if (c.getId().equals(vertex[2])) {
                    Circle c1 = findCircle(vertex[0]);
                    Circle c2 = findCircle(vertex[1]);
                    Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
                    Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
                    Point2D p3 = new Point2D(c.getCenterX(), c.getCenterY());
                    switch (nl) {
                        case 4 -> mc = p1.midpoint(p2);
                        case 5 -> mc = bisectorPoind(p1, p3, p2);
                        case 6 -> mc = heightPoind(p3, p2, p1);
                        default -> throw new IllegalStateException("Неопределенно значение: " + nl);
                    }
                    newHeight = createMedianaBisectorHeight(c, c1, c2, mc, nl);
                }
            }
        }
        return newHeight;
    }

    /**
     * Метод bisectorPoind(Point2D pA, Point2D pB, Point2D pC).
     * Предназначен для определения координат пересечения биссектрисы со стороной треугольника.
     *
     * @param pA - вершина треугольника.
     * @param pB - вершина треугольника из которой проведена биссектриса.
     * @param pC - вершина треугольника.
     * @return - возвращает координаты точки пересечения.
     */
    private Point2D bisectorPoind(Point2D pA, Point2D pB, Point2D pC) {
        double ra = pA.distance(pB) / pC.distance(pB);
        double dX = (pA.getX() + ra * pC.getX()) / (1 + ra);
        double dY = (pA.getY() + ra * pC.getY()) / (1 + ra);
        return new Point2D(dX, dY);
    }

    /**
     * Метод heightPoind(Point2D p1, Point2D p2, Point2D p3)
     * Предназначен для определения координаты точки пересечения высоты со стороной треугольника.
     * А также нахождения точки пересечения перпендикуляра с прямой.
     * Вызывается из метода heightAdd(Circle c) - добавить высоту.
     *
     * @param p1 - координаты вершины А
     * @param p2 - координаты вершины В
     * @param p3 - координаты вершины С
     * @return - возвращает координаты точки пересечения высоты треугольника из вершины А к стороне ВС.
     */
    public Point2D heightPoind(Point2D p1, Point2D p2, Point2D p3) {
        double a1 = p3.getY() - p2.getY();
        double b1 = p2.getX() - p3.getX();
        double c1 = p2.getX() * p3.getY() - p3.getX() * p2.getY();
        double c2 = -p1.getX() * (p3.getX() - p2.getX()) + p1.getY() * (p2.getY() - p3.getY());
        //Вычисление главного определителя
        double o = -pow(a1, 2) - pow(b1, 2);
        return new Point2D((-c1 * a1 - c2 * b1) / o, (a1 * c2 - b1 * c1) / o);
    }

    /**
     * Метод closeLine(Line l).
     * Предназначен для запрета линий на перемещение от мышки.
     * К этим линиям относятся высота, медиана, биссектриса треугольника.
     *
     * @param l - ссылка на линию
     */
    public void closeLine(Line l) {
        for (PoindLine p : poindLines) {
            if (p != null) {
                if (p.getLine().getId().equals(l.getId())) {
                    p.setBMove(false);
                }
            }
        }

    }

    //Тестовый метод для вывода информации по коллекциям
    public void ColTest() {
        //Взято из книги Кэн Коузен "Современный Java. Рецепты программирования".
        //Глава 2. Пакет java.util.function
        // стр.40 Пример 2.3
        System.out.println("Коллекция PoindCircle");
        //ссылка на метод
        for (PoindCircle poindCircle : poindCircles) {
            System.out.println(poindCircle);
        }

        System.out.println("Коллекция PoindLine");
        poindLines.forEach(System.out::println);//лямбда выражение

        System.out.println("Коллекция дуг");
        vertexArcs.forEach(System.out::println);

        System.out.println("Коллекция имен");
        namePoindLines.forEach(System.out::println);

        System.out.println("Коллекция треугольников");
        treangleNames.forEach(System.out::println);

        System.out.println("Коллекция окружностей");
        circleLines.forEach(System.out::println);

        System.out.println("Коллекция объектов");
        paneBoards.getChildren().forEach(System.out::println);

    }

    /**
     * Метод middleBindSegment(Circle newCircle, Line l).
     * Предназначен для связывания середины отрезка с линией
     *
     * @param c - ссылка на точку
     * @param l - ссылка на линию
     */
    public void middleBindSegment(Circle c, Line l) {
        l.startXProperty().addListener((old, oldValue, newValue) -> {
            Point2D p1 = new Point2D(l.startXProperty().get(), l.startYProperty().get());
            Point2D p2 = new Point2D(l.endXProperty().get(), l.endYProperty().get());
            c.setCenterX(p1.midpoint(p2).getX());
            c.setCenterY(p1.midpoint(p2).getY());
            findCirclesUpdateXY(c.getId(), gridViews.revAccessX(c.getCenterX()), gridViews.revAccessY(c.getCenterY()));
        });
        l.startYProperty().addListener((old, oldValue, newValue) -> {
            Point2D p1 = new Point2D(l.startXProperty().get(), l.startYProperty().get());
            Point2D p2 = new Point2D(l.endXProperty().get(), l.endYProperty().get());
            c.setCenterX(p1.midpoint(p2).getX());
            c.setCenterY(p1.midpoint(p2).getY());
            findCirclesUpdateXY(c.getId(), gridViews.revAccessX(c.getCenterX()), gridViews.revAccessY(c.getCenterY()));
        });
        l.endXProperty().addListener((old, oldValue, newValue) -> {
            Point2D p1 = new Point2D(l.startXProperty().get(), l.startYProperty().get());
            Point2D p2 = new Point2D(l.endXProperty().get(), l.endYProperty().get());
            c.setCenterX(p1.midpoint(p2).getX());
            c.setCenterY(p1.midpoint(p2).getY());
            findCirclesUpdateXY(c.getId(), gridViews.revAccessX(c.getCenterX()), gridViews.revAccessY(c.getCenterY()));
        });
        l.endYProperty().addListener((old, oldValue, newValue) -> {
            Point2D p1 = new Point2D(l.startXProperty().get(), l.startYProperty().get());
            Point2D p2 = new Point2D(l.endXProperty().get(), l.endYProperty().get());
            c.setCenterX(p1.midpoint(p2).getX());
            c.setCenterY(p1.midpoint(p2).getY());
            findCirclesUpdateXY(c.getId(), gridViews.revAccessX(c.getCenterX()), gridViews.revAccessY(c.getCenterY()));
        });
    }

    /**
     * Метод radiusInCircle(Point2D v1, Point2D v2, Point2D v3).
     * Предназначен для расчета радиуса вписанной окружности.
     *
     * @param v1 - координаты первой вершины треугольника.
     * @param v2 - координаты второй вершины треугольника.
     * @param v3 - координаты третьей вершины треугольника.
     * @return - возвращает радиус описанной окружности.
     */
    private double radiusInCircle(Point2D v1, Point2D v2, Point2D v3) {
        double ab = v1.distance(v2);
        double ac = v1.distance(v3);
        double bc = v2.distance(v3);
        double s = areaTriangle(v1.getX(), v1.getY(), v2.getX(), v2.getY(), v3.getX(), v3.getY());
        double p = abs((ab + ac + bc) / 2);
        return abs(s / p);
    }

    /**
     * Метод inCircleXY(Point2D v1, Point2D v2, Point2D v3).
     * Предназначен для расчета координат центра вписанной окружности.
     *
     * @param v1 - координаты первой вершины треугольника.
     * @param v2 - координаты второй вершины треугольника.
     * @param v3 - координаты третьей вершины треугольника.
     * @return - возвращает радиус описанной окружности.
     */
    private Point2D inCircleXY(Point2D v1, Point2D v2, Point2D v3) {
        double ab = v1.distance(v2);
        double ac = v1.distance(v3);
        double bc = v2.distance(v3);
        return new Point2D((bc * v1.getX() + ac * v2.getX() + ab * v3.getX()) / (ab + bc + ac), ((bc * v1.getY() + ac * v2.getY() + ab * v3.getY()) / (ab + bc + ac)));
    }


    /**
     * Метод radiusOutCircle(Point2D v1, Point2D v2, Point2D v3).
     * Предназначен для расчета радиуса описанной окружности.
     *
     * @param v1 - координаты первой вершины треугольника.
     * @param v2 - координаты второй вершины треугольника.
     * @param v3 - координаты третьей вершины треугольника.
     * @return - возвращает радиус описанной окружности.
     */
    private double radiusOutCircle(Point2D v1, Point2D v2, Point2D v3) {
        double ab = v1.distance(v2);
        double ac = v1.distance(v3);
        double bc = v2.distance(v3);
        double s = areaTriangle(v1.getX(), v1.getY(), v2.getX(), v2.getY(), v3.getX(), v3.getY());
        return abs((ab * bc * ac) / (s * 4));
    }

    /**
     * Метод middlePerpendicular(Point2D v1, Point2D v2, Point2D v3).
     * Предназначен для расчета центра описанной окружности.
     *
     * @param v1 - координаты первой вершины треугольника.
     * @param v2 - координаты второй вершины треугольника.
     * @param v3 - координаты третьей вершины треугольника.
     * @return возвращает координаты центра описанной окружности.
     */
    private Point2D middlePerpendicular(Point2D v1, Point2D v2, Point2D v3) {
        double smAx = (v2.getX() + v3.getX()) / 2;//середина
        double smAy = (v2.getY() + v3.getY()) / 2;
        double smCx = (v1.getX() + v2.getX()) / 2;
        double smCy = (v1.getY() + v2.getY()) / 2;
        double sxAB = v1.getX() - v2.getX();
        double syAB = -(v2.getY() - v1.getY());
        double sAB = smCx * sxAB + smCy * syAB;
        double sxBC = v2.getX() - v3.getX();
        double syBC = v2.getY() - v3.getY();
        double sBC = smAx * sxBC + smAy * syBC;
        double d = sxAB * syBC - sxBC * syAB;
        double dx = sAB * syBC - sBC * syAB;
        double dy = sxAB * sBC - sxBC * sAB;
        return new Point2D(dx / d, dy / d);
    }
}