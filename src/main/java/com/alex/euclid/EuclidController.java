package com.alex.euclid;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.val;

import java.util.Objects;
import java.util.Optional;

import static ContstantString.StringStatus.*;

/**
 * Класс управления приложением (Controller), наследует класс View.
 * Реагирует на все события управления от мыши и клавиатуры.
 * Вызывает методы из класса модели для обработки событий
 *
 * @author A. Nosov
 * @version 1.0

 * */

public class EuclidController extends View {
    public Menu menuTreangleP;
    public MenuItem menuTreangle;
    public MenuItem menuMedina;
    public MenuItem menuEqualTr;
    public MenuItem menuSecondTr;
    public Font x3;
    //Связать переменные с шаблоном FXML
    /**
     * paneShape - контейнер для геометрических фигур
     */
    @FXML
    private Pane paneShape;
    /**
     * Cartesian - контейнер для декартовых координат
     */
    @FXML
    private StackPane Cartesian;
    /**
     * txtShape - контейнер для правой части доски
     */
    @FXML
    private TextArea txtShape;
    @FXML
    private Button btnTreangle;
    @FXML
    private Button btnPoind;
    @FXML
    private Button btnSegment;
    @FXML
    private Button btnRay;
    @FXML
    private Button btnLine;
    @FXML
    private Button btnAngle;
    @FXML
    private Button btnVertical;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnMediana;
    @FXML
    private Button btnBisector;
    @FXML
    private Button btnHeight;
    @FXML
    private Button btnCircle;
    @FXML
    private Button btnParallelLines;
    @FXML
    private Button btnMiddleSegment;

    //Web браузер для вывода данных
    @FXML
    private WebView webViewLeft;//для размещения информации слева от доски
    @FXML
    private Pane paneGrid;//контейнер для сетки
    @FXML
    private Label leftStatus;//Левый статус
    @FXML
    private Label rightStatus;//Правый статус

    private Line newLine;//отрезок
    private Circle poindLine1;//первая точка луча, прямой, отрезка
    private Circle poindLine2;//вторая точка луча, прямой, отрезка
    private Circle circle;//окружность
    @FXML
    private CheckMenuItem menuShowPoindName;
    @FXML
    private CheckMenuItem menuShowLineName;
    @FXML
    private CheckMenuItem menuGrid;
    @FXML
    private CheckMenuItem menuAngleName;

    //Режимы создания
    private boolean segmentAdd;//true - создать отрезок
    private boolean rayAdd;//true - создание луча
    private boolean lineAdd;//true - создание прямой


    private boolean medianaAdd;//true - проведение медианы из вершины треугольника
    private boolean bisectorAdd;//true - проведение биссектрисы из вершины треугольника
    private boolean heightAdd;//true - проведение высоты из вершины треугольника
    private boolean circleAdd;//true - построение окружности
    private boolean middleSegmentAdd;//true- построение середины отрезка
    private Circle poindTreangle1;//первая точка треугольника
    private Line lineTriangle1;//первая сторона треугольника для построения
    private int angleCol = 0;//индекс счета углов

    private boolean poindAdd1 = false;//true - создание первой точки для отрезка
    private boolean poindAdd2 = false;//true - создание второй точки для отрезка
    private boolean poindCircle = false;//true - когда создана окружность и идет выбор размера
    private boolean poindAddVertical = false;//true - выбрана точка для перпендикуляра
    private boolean lineAddVertical = false;//true - выбрана прямая для перпендикуляра
    private boolean poindAddVParallel = false;//true - выбрана точка для параллельной прямой
    private boolean lineAddParallel = false;// true - выбрана линия для параллельной прямой
    private boolean treangleVisibly = false;//true - кнопки построить биссектрису, медиану, высоту видимы, т.к. есть уже треугольник
    private String infoStatus;//Вершины угла и вершины треугольника при создании.


    /**
     * Метод инициализации для класса Controller
     */
    @FXML
    private void initialize() {
        //Передача ссылок в класс Model
        model.setStatus(leftStatus);//Передать ссылку на статус для модели
        model.setTextArea(txtShape);//Передать ссылку фигуры
        model.setGridViews(gridViews);//Передать ссылку для пересчета координат класса модели.
        model.setPaneBoards(paneShape);//Передать ссылку на доску для класса модели.
        model.webHTML(webViewLeft, "geometry.html");//Вывод в web файла html (что такое геометрия)
        //формирование линий координат и сетки, перерасчет при изменении размеров доски
        gridViews.setPaneGrid(paneGrid);
        gridViews.setCartesian(Cartesian);
        //Изменение ширины окна
        Cartesian.widthProperty().addListener((obs, oldVal, newVal) -> {
            gridViews.setVr(Cartesian.getWidth());
            gridViews.setWl(-Cartesian.getWidth() / 2);
            gridViews.setWr(Cartesian.getWidth() / 2);
            gridViews.rate();//Перерасчет коэффициентов
            paneGrid.getChildren().clear();//Очистить экран и память
            if (model.isShowGrid()) {
                gridViews.gridCartesian();//Вывод сетки
            }
            updateShape();//обновить координаты геометрических фигур
        });
        //Изменение высоты окна
        Cartesian.heightProperty().addListener((obs, oldVal, newVal) -> {
            gridViews.setVb(Cartesian.getHeight());
            gridViews.setWt(Cartesian.getHeight() / 2);
            gridViews.setWb(-Cartesian.getHeight() / 2);
            gridViews.rate();//Перерасчет коэффициентов
            paneGrid.getChildren().clear();//Очистить экран и память
            if (model.isShowGrid()) {
                gridViews.gridCartesian();//Вывод сетки
            }
            updateShape();//обновить координаты геометрических фигур
        });

        if (model.isShowGrid()) {
            gridViews.gridCartesian();//Вывод сетки
        }

        //Добавить изображение к кнопкам

        btnPoind.setStyle("-fx-background-image: url(/Images/point.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnSegment.setStyle("-fx-background-image: url(/Images/point&line.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnAngle.setStyle("-fx-background-image: url(/Images/angle.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnVertical.setStyle("-fx-background-image: url(/Images/vertical.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnDelete.setStyle("-fx-background-image: url(/Images/delete.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnLine.setStyle("-fx-background-image: url(/Images/line.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnRay.setStyle("-fx-background-image: url(/Images/ray.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnTreangle.setStyle("-fx-background-image: url(/Images/triangle.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnHeight.setStyle("-fx-background-image: url(/Images/triangle_height.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnMediana.setStyle("-fx-background-image: url(/Images/mediana.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnBisector.setStyle("-fx-background-image: url(/Images/bisector.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnParallelLines.setStyle("-fx-background-image: url(/Images/parallel.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnCircle.setStyle("-fx-background-image: url(circle.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");
        btnMiddleSegment.setStyle("-fx-background-image: url(../../../../resources/com/alex/euclid/Images/middle.png);" + "-fx-background-repeat: no-repeat;" + "-fx-background-position:center center");

    }

    /**
     * Метод visibleCreate()
     * Предназначен для сброса всех режимов построения.
     * Вызывается перед тем, как установить какой-то режим построения фигуры
     * на доске.
     */
    private void visibleCreate() {
        model.setPoindAdd(false);//true - создать точку
        segmentAdd = false;//true - создать отрезок
        middleSegmentAdd = false;//true - построить середину отрезка
        rayAdd = false;//true - создание луча
        lineAdd = false;//true - создание прямой
        circleAdd = false;//true - создание окружности
        model.setAngleAdd(false);//true - построение угла
        model.setVerticalAdd(false);//true - построение перпендикуляра к прямой
        model.setParallelAdd(false);//true - построение параллельной прямой
        model.setTreangleAdd(false);//true - создание треугольника
        model.setRemoveObject(false);//true - режим удаления фигур
        medianaAdd = false;//true - проведение медианы из вершины треугольника
        bisectorAdd = false;//true - проведение биссектрисы из вершины треугольника
        heightAdd = false;//true - проведение высоты из вершины треугольника
    }

    /**
     * Метод disableButton(Boolean b)
     * Предназначен для блокировки кнопок, когда включен режим рисования фигур на доске.
     * После завершения режима построения, кнопки разблокируются.
     *
     * @param b - true кнопки блокированы, false - разблокированы
     */
    private void disableButton(Boolean b) {
        btnPoind.setDisable(b);
        btnSegment.setDisable(b);
        btnMiddleSegment.setDisable(b);
        btnRay.setDisable(b);
        btnLine.setDisable(b);
        btnCircle.setDisable(b);
        btnAngle.setDisable(b);
        btnVertical.setDisable(b);
        btnParallelLines.setDisable(b);
        btnTreangle.setDisable(b);
        btnDelete.setDisable(b);
        if (treangleVisibly) {
            btnBisector.setDisable(b);
            btnMediana.setDisable(b);
            btnHeight.setDisable(b);
        }
    }

    /**
     * Метод onMouseMoved()
     * Отслеживает события перемещения мышки по доске без нажатой кнопки
     * Выводит координаты мыши в статусной строке
     * Используется при добавлении геометрических фигур на доску.
     *
     * @param mouseEvent - координаты мыши
     */
    public void onMouseMoved(MouseEvent mouseEvent) {
        model.setScreenX(mouseEvent.getX());
        model.setScreenY(mouseEvent.getY());
        model.setDecartX(gridViews.revAccessX(mouseEvent.getX()));
        model.setDecartY(gridViews.revAccessY(mouseEvent.getY()));
        rightStatus.setText("Координаты доски x: " + gridViews.revAccessX(mouseEvent.getX()) + " y: " + gridViews.revAccessY(mouseEvent.getY()));

        //Добавление треугольника
        if (model.isTreangleAdd() && newLine != null && poindAdd2) {
            //Передать в View для вывода
            model.setLine(newLine);
            model.notifyObservers("SideGo");
            model.findLinesUpdateXY(newLine.getId());
            if (angleCol == 2) {//2-для угла, треугольника
                poindAdd1 = true;
            }
        }

        //Создание отрезка
        if (segmentAdd && newLine != null && poindAdd2) {
            //Передать в View для вывода
            model.setLine(newLine);
            model.notifyObservers("SideGo");
            model.findLinesUpdateXY(newLine.getId());//обновляем мировые координаты
            poindAdd1 = true;//первая точка создана


        }
        //Вторая точка, если расстояние до неё меньше 15px, конец отрезка
        //переходит на точку. Щелчок мышкой, вторая точка выбрана заданная
        model.lineAddPoind(newLine, poindAdd2);

        //Добавить окружность
        if (circleAdd && circle != null && poindAdd1) {
            poindCircle = true;//для завершения создания окружности
            //Расчитать радиус
            double r = model.distance(poindLine1.getCenterX(), poindLine1.getCenterY(), model.getScreenX(), model.getScreenY());
            double rw = model.distance(gridViews.revAccessX(poindLine1.getCenterX()), gridViews.revAccessY(poindLine1.getCenterY()), model.getDecartX(), model.getDecartY());
            model.setRadiusCircle(Math.round(r));
            model.setRadiusCircleW(Math.round(rw));
            model.circleView(circle);//вывести на доску
        }


        //Создание луча
        if (rayAdd && newLine != null && poindAdd2) {
            //Расчитать координаты окончания луча
            model.rayAddLine(newLine, 1);
            poindAdd1 = true;//первая точка создана
        }

        //Создание прямой
        if (lineAdd && newLine != null && poindAdd2) {
            //расчитать концов прямой по уравнению прямой

            double x = poindLine1.getCenterX() + (model.getScreenX() - poindLine1.getCenterX()) * 3;
            double y = poindLine1.getCenterY() + (model.getScreenY() - poindLine1.getCenterY()) * 3;
            double x1 = poindLine1.getCenterX() + (model.getScreenX() - poindLine1.getCenterX()) * -3;
            double y1 = poindLine1.getCenterY() + (model.getScreenY() - poindLine1.getCenterY()) * -3;

            //Добавить координаты пересчета в коллекцию
            model.setVerLineStartX(gridViews.revAccessX(x1));
            model.setVerLineStartY(gridViews.revAccessY(y1));
            model.setVerLineEndX(gridViews.revAccessX(x));
            model.setVerLineEndY(gridViews.revAccessY(y));
            model.findLinesUpdateXY(newLine.getId());
            //задать координаты прямой
            model.setRayStartX(x1);
            model.setRayStartY(y1);
            model.setRayEndX(x);
            model.setRayEndY(y);
            //Передать в View для вывода
            model.setLine(newLine);
            model.notifyObservers("RayGo");
            poindAdd1 = true;//разрешение для постройки 2 точки

        }

    }

    /**
     * Метод onMouseDraggen()
     * Отслеживает события перемещения мышки с нажатой левой кнопкой
     * Используется для перемещения сетки и координатных осей по доске.
     *
     * @param event - свойство мыши.
     */
    public void onMouseDraggen(MouseEvent event) {
        //координаты, нужны для перемещения объектов на доске
        model.setScreenX(event.getX());
        model.setScreenY(event.getY());
        model.setDecartX(gridViews.revAccessX(event.getX()));
        model.setDecartY(gridViews.revAccessY(event.getY()));

        //Перемещение сетки
        if (event.getTarget() == paneShape) {
            val dx = gridViews.getVPx() - event.getX();//Вычисляем смещение мышки по Х
            val dy = gridViews.getVPy() - event.getY();// по Y
            gridViews.setVPx(event.getX()); //Сохраняем текущие координаты мышки
            gridViews.setVPy(event.getY());
            //Вычисляем смещение окна
            gridViews.setWl(gridViews.getWl() + dx);
            gridViews.setWr(gridViews.getWr() + dx);
            gridViews.setWt(gridViews.getWt() - dy);
            gridViews.setWb(gridViews.getWb() - dy);
            gridViews.rate();//Перерасчет коэффициентов
            paneGrid.getChildren().clear();//Очистить экран и память
            if (model.isShowGrid()) {
                gridViews.gridCartesian();//Вывод сетки
            }
            updateShape();
        }
        event.consume();
    }

    /**
     * Метод oMousePressed()
     * Отслеживает нажатие кнопки на доске
     * Используется для перемещения сетки и координатных осей, а также
     * для создания геометрических фигур.
     *
     * @param event - событие мыши
     */
    public void onMousePressed(MouseEvent event) {
        // Фиксируем точку нажатия кнопки мыши для перемещения сетки и координатных осей
        if (event.getTarget() == paneShape) {
            gridViews.setVPx(event.getX());
            gridViews.setVPy(event.getY());
        }

        //Добавление точки на доске
        if (model.isPoindAdd()) {
            Circle newPoind = model.createPoindAdd(true);//создать точку
            paneShape.getChildren().add(newPoind);//добавить на доску
            model.setPoindAdd(false);//Режим добавления точки окончен
            disableButton(false);//разблокировать кнопки
            model.setCreateShape(false);//Сбросить режим создания фигуры

        }

        //Добавление отрезка
        if (segmentAdd && !poindAdd1) {
            model.setCreateLine(true);//заблокировать режим перемещения линий
            addLineRayStart(0);//Создание первой точки и линии
        }
        //Вторая точка для отрезка
        if (segmentAdd && poindAdd1) {
            addLineRayEnd();
            disableButton(false);//разблокировать кнопки
            segmentAdd = false;//окончание режима добавления
            //Обновить мировые координаты
            model.findLinesUpdateXY(newLine.getId());
            //Заменить имя
            model.findNameId(poindLine1.getId(), poindLine2.getId(), newLine.getId());
            //Связать точки с прямой
            model.lineBindCircles(poindLine1, poindLine2, newLine);
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
            model.setCreateShape(false);//Сбросить режим создания фигуры
        }
        // Построение середины отрезка
        if (middleSegmentAdd) {
            newLine = model.getTimeLine();//получаем отрезок, для которого надо построить середину
            //проверить, является ли данная линия отрезком, исключаем прямые и лучи
            if (model.findTypeLine(newLine) == 0) {
                String[] namePoind = model.findID(newLine).split("_");//получить имена точек отрезка
                Circle c1 = model.findCircle(namePoind[0]);
                Circle c2 = model.findCircle(namePoind[1]);
                Point2D p1 = new Point2D(c1.getCenterX(), c1.getCenterY());
                Point2D p2 = new Point2D(c2.getCenterX(), c2.getCenterY());
                Point2D poindMiddle = model.midPoindAB(p1, p2);//получили координаты середины отрезка
                Circle newCircle = model.createPoindAdd(false);
                model.setScreenX(poindMiddle.getX());
                model.setScreenY(poindMiddle.getY());
                model.notifyObservers("VertexGo");//вывести на экран
                paneShape.getChildren().add(newCircle);
                //Связать полученную точку с линией
                model.middleBindSegment(newCircle, newLine);

            } else {
                model.setStringLeftStatus(STA_32);
                model.notifyObservers("LeftStatusGo");
            }
            //завершение построения
            disableButton(false);//разблокировать кнопки
            middleSegmentAdd = false;
            model.setCreateShape(false);//Сбросить режим создания фигуры
        }

        //Добавление угла первая точка
        if (model.isAngleAdd() && !poindAdd1) {
            //увеличить индекс
            if (!model.isPoindOldAdd()) {//false - новая вершина true - взять имеющую
                poindLine1 = model.createPoindAdd(true);//создать новую вершину
                infoStatus = infoStatus + poindLine1.getId() + "_";//добавить вершину в список
                paneShape.getChildren().add(poindLine1);
            } else {
                infoStatus = infoStatus + model.getTimeVer().getId() + "_";//Вершина из временной переменной
                poindLine1 = model.getTimeVer();
            }
            model.indexAdd(poindLine1);//увеличить индекс
            angleCol += 1;//увеличиваем счетчик вершин
            if (angleCol == 3) {
                poindAdd2 = false;//закрыть 2 точку
                model.setPoindOldAdd(false);//закрыть добавление из имеющихся точек
                //угол
                Arc arcAngle = model.createVertexAdd(infoStatus);
                paneShape.getChildren().add(arcAngle);//рисуем арку дуги
                arcAngle.toBack();//перемещать узел вниз только после добавления на стол
                //Связываем арку с углом и именем
                model.arcBindPoind(infoStatus, arcAngle);
                model.setAngleAdd(false);//окончание режима добавления
                poindAdd1 = false;
                disableButton(false);//разблокировать кнопки
                angleCol = 0;
                //Вывод информации об объектах в правую часть доски
                model.setTxtShape("");
                model.txtAreaOutput();
                model.setCreateShape(false);//Сбросить режим создания фигуры
            }
        }
        //Построение треугольника
        if (model.isTreangleAdd() && !poindAdd1) {
            model.setCreateLine(true);//заблокировать режим перемещения линий
            addLineRayStart(3);//Создание первой точки и линии
            angleCol += 1;//увеличиваем счетчик вершин
        }
        //Окончание построения треугольника
        if (model.isTreangleAdd() && poindAdd1) {
            //Двунаправленная связь
            model.lineBindCircles(poindTreangle1, poindLine1, lineTriangle1);
            //Свойство мышки
            model.mouseLine(lineTriangle1);
            Point2D v1 = new Point2D(poindTreangle1.getCenterX(), poindTreangle1.getCenterY());
            infoStatus = poindTreangle1.getId() + "_";
            //Заменить имя
            model.findNameId(poindTreangle1.getId(), poindLine1.getId(), lineTriangle1.getId());
            Point2D v2 = new Point2D(poindLine1.getCenterX(), poindLine1.getCenterY());
            infoStatus = infoStatus + poindLine1.getId() + "_";
            addLineRayEnd();
            //Двунаправленная связь
            model.lineBindCircles(poindLine1, poindLine2, newLine);
            model.mouseLine(newLine);
            //Заменить имя
            model.findNameId(poindLine1.getId(), poindLine2.getId(), newLine.getId());
            newLine = model.createLineAdd(3);
            model.setScreenX(poindTreangle1.getCenterX());
            model.setScreenY(poindTreangle1.getCenterY());
            //Передать в View для вывода
            model.setLine(newLine);
            model.notifyObservers("SideGo");
            //Двунаправленная связь
            model.lineBindCircles(poindTreangle1, poindLine2, newLine);
            model.mouseLine(newLine);
            Point2D v3 = new Point2D(poindLine2.getCenterX(), poindLine2.getCenterY());
            infoStatus = infoStatus + poindLine2.getId();
            //Добавить многоугольник в форме треугольника
            Polygon t = model.treangleAdd(v1, v2, v3, infoStatus);
            paneShape.getChildren().addAll(newLine, t);
            t.toBack();
            newLine.toBack();
            model.findLinesUpdateXY(newLine.getId());//обновляем мировые координаты
            //Заменить имя
            model.findNameId(poindTreangle1.getId(), poindLine2.getId(), newLine.getId());
            model.setTreangleAdd(false);//окончание режима добавления
            poindAdd1 = false;
            angleCol = 0;
            model.setCreateLine(false);//заблокировать режим перемещения линий
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
            treangleVisibly = true;//разблокировать кнопки высота, медиана, биссектриса
            disableButton(false);//разблокировать кнопки
            model.setCreateShape(false);//Сбросить режим создания фигуры
        }

        //Добавления луча
        if (rayAdd && !poindAdd1) {
            model.setCreateLine(true);//заблокировать режим перемещения линий
            addLineRayStart(1);
        }
        //Окончание построения луча
        if (rayAdd && poindAdd1) {
            addLineRayEnd();
            rayAdd = false;//закончить построение луча
            disableButton(false);//разблокировать кнопки
            //Связать точки с лучом
            model.rayBindCircles(poindLine1, poindLine2, newLine);
            model.findLinesUpdateXY(newLine.getId());//обновить мировые координаты
            //Заменить имя
            model.findNameId(poindLine1.getId(), poindLine2.getId(), newLine.getId());
            //Добавить имя на доску
            model.nameLineAdd(newLine);
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
            model.setCreateShape(false);//Сбросить режим создания фигуры
        }
        //Добавление прямой
        if (lineAdd && !poindAdd1) {
            model.setCreateLine(true);//заблокировать режим перемещения линий
            addLineRayStart(2);//Создание первой точки и линии
        }
        if (lineAdd && poindAdd1) {
            addLineRayEnd();
            //Привязка свойств мышки
            model.mouseLine(newLine);
            //Связать точки с прямой
            model.circlesBindLine(poindLine1, poindLine2, newLine);
            model.findLinesUpdateXY(newLine.getId());//обновить мировые координаты
            //Заменить имя
            model.findNameId(poindLine1.getId(), poindLine2.getId(), newLine.getId());
            //Добавить имя
            model.nameLineAdd(newLine);
            lineAdd = false;
            disableButton(false);//разблокировать кнопки
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
            model.setCreateShape(false);//Сбросить режим создания фигуры
        }

        //Добавление медианы
        if (medianaAdd) {
            poindLine1 = model.getTimeVer();
            newLine = model.mbhLineAdd(poindLine1, 4);
            model.mouseLine(newLine);//привязка событий мыши
            model.closeLine(newLine);//запрет на перемещение
            medianaAdd = false;
            disableButton(false);//разблокировать кнопки
            model.setTxtShape("");
            model.txtAreaOutput();
            model.setCreateShape(false);//Сбросить режим создания фигуры
        }
        //Добавление биссектрисы
        if (bisectorAdd) {
            poindLine1 = model.getTimeVer();
            newLine = model.mbhLineAdd(poindLine1, 5);
            model.mouseLine(newLine);//привязка событий мыши
            model.closeLine(newLine);//запрет на перемещение
            bisectorAdd = false;
            disableButton(false);//разблокировать кнопки
            model.setTxtShape("");
            model.txtAreaOutput();
            model.setCreateShape(false);//Сбросить режим создания фигуры
        }
        //Добавление высоты
        if (heightAdd) {
            poindLine1 = model.getTimeVer();
            newLine = model.mbhLineAdd(poindLine1, 6);
            model.mouseLine(newLine);//привязка событий мыши
            model.closeLine(newLine);//запрет на перемещение
            heightAdd = false;
            disableButton(false);//разблокировать кнопки
            model.setTxtShape("");
            model.txtAreaOutput();
            model.setCreateShape(false);//Сбросить режим создания фигуры
        }
        //Построение перпендикуляра к прямой, отрезку, лучу.
        if (model.isVerticalAdd() && model.isPoindOldAdd()) {
            poindLine1 = model.getTimeVer();//получаем точку из которой надо опустить перпендикуляр
            poindAddVertical = true;
        }
        if (model.isVerticalAdd() && model.isLineOldAdd()) {
            newLine = model.getTimeLine();//получаем прямую к которой надо опустить перпендикуляр
            lineAddVertical = true;
        }
        //Опускаем перпендикуляр, не важно что выбрано первым
        if (poindAddVertical && lineAddVertical) {
            model.setPoindOldAdd(true);//взять выбранную точку
            String[] nameLine = model.findID(newLine).split("_");//получить имя отрезка по имени прямой
            Point2D A1 = new Point2D(poindLine1.getCenterX(), poindLine1.getCenterY());
            Point2D B1 = new Point2D(newLine.getStartX(), newLine.getStartY());
            Point2D C1 = new Point2D(newLine.getEndX(), newLine.getEndY());
            Point2D D1 = model.heightPoind(A1, B1, C1);//координаты точки пересечения
            addLineRayStart(7);//Создание перпендикуляра
            poindAdd2 = false;
            //Задаем координаты
            model.setSegmentStartX(poindLine1.getCenterX());
            model.setSegmentStartY(poindLine1.getCenterY());
            model.setScreenX(D1.getX());
            model.setScreenY(D1.getY());
            model.closeLine(newLine);//запрет на перемещение
            //Передать в View для вывода
            model.setLine(newLine);
            model.notifyObservers("SideGo");
            //Привязать события мыши
            model.mouseLine(newLine);
            //Переводим координаты линии в мировые
            model.findLinesUpdateXY(newLine.getId());
            //Переводим в мировые координаты точки
            model.setDecartX(gridViews.revAccessX(D1.getX()));
            model.setDecartY(gridViews.revAccessY(D1.getY()));
            //Создаем расчетную точку не перемещаемую
            Circle newPoind = model.createPoindAdd(false);//создать точку
            //Обновить мировые координаты коллекции
            model.findCirclesUpdateXY(newPoind.getId(), gridViews.revAccessX(newPoind.getCenterX()), gridViews.revAccessY(newPoind.getCenterY()));
            //Заменить имя прямой на имя отрезка
            model.findNameId(poindLine1.getId(), newPoind.getId(), newLine.getId());
            model.setTxtShape("");
            model.txtAreaOutput();
            paneShape.getChildren().add(newPoind);//добавить на доску
            //связать точки и перпендикуляр для перемещения
            model.verticalBindCircles(poindLine1, model.findCircle(nameLine[0]), model.findCircle(nameLine[1]), newPoind, newLine);
            model.setVerticalAdd(false);//выход из режима перпендикуляра
            poindAddVertical = false;
            lineAddVertical = false;
            poindAdd2 = false;
            disableButton(false);//разблокировать кнопки
            model.setPoindOldAdd(false);
            model.setCreateShape(false);//Сбросить режим создания фигуры
            //закрыть режим создания перпендикуляра
        }
        //Построить параллельную прямую
        if (model.isParallelAdd() && model.isPoindOldAdd()) {
            poindLine1 = model.getTimeVer();//получаем точку через которую надо провести параллельную прямую
            //  model.findCircleMove(poindLine1.getId());//изменить статус на не перемещаемую и принадлежит прямой
            poindAddVParallel = true;
        }
        if (model.isParallelAdd() && model.isLineOldAdd()) {
            newLine = model.getTimeLine();//получаем прямую к которой надо построить параллельную прямую
            lineAddParallel = true;
        }
        //Строим параллельную прямую
        if (poindAddVParallel && lineAddParallel) {
            model.setPoindOldAdd(false);//взять выбранную точку
            String[] nameLine = model.findID(newLine).split("_");//получить имя отрезка по имени прямой
            model.setScreenX(model.findCircle(nameLine[1]).getCenterX() + (poindLine1.getCenterX() - model.findCircle(nameLine[0]).getCenterX()));
            model.setScreenY(model.findCircle(nameLine[1]).getCenterY() + (poindLine1.getCenterY() - model.findCircle(nameLine[0]).getCenterY()));
            Circle newPoind = model.createPoindAdd(true);
            //Обновить мировые координаты коллекции
            model.findCirclesUpdateXY(newPoind.getId(), gridViews.revAccessX(newPoind.getCenterX()), gridViews.revAccessY(newPoind.getCenterY()));
            //model.findCircleMove(newPoind.getId());//изменить статус на не перемещаемую и принадлежит прямой
            paneShape.getChildren().add(newPoind);//добавить на доску
            model.setVertex(newPoind);
            model.notifyObservers("VertexGo");
            Line parallelLine = model.createLineAdd(2);
            double x1 = poindLine1.getCenterX() + (newPoind.getCenterX() - poindLine1.getCenterX()) * 3;
            double y1 = poindLine1.getCenterY() + (newPoind.getCenterY() - poindLine1.getCenterY()) * 3;
            double x = poindLine1.getCenterX() + (newPoind.getCenterX() - poindLine1.getCenterX()) * -3;
            double y = poindLine1.getCenterY() + (newPoind.getCenterY() - poindLine1.getCenterY()) * -3;

            //задать координаты прямой
            model.setRayStartX(x1);
            model.setRayStartY(y1);
            model.setRayEndX(x);
            model.setRayEndY(y);
            //Передать в View для вывода
            model.setLine(parallelLine);
            model.notifyObservers("RayGo");
            paneShape.getChildren().add(parallelLine);//добавить на доску
            parallelLine.toBack();
            //Добавить координаты пересчета в коллекцию
            model.setVerLineStartX(gridViews.revAccessX(x1));
            model.setVerLineStartY(gridViews.revAccessY(y1));
            model.setVerLineEndX(gridViews.revAccessX(x));
            model.setVerLineEndY(gridViews.revAccessY(y));
            model.findLinesUpdateXY(parallelLine.getId());
            //Привязка свойств мышки
            model.mouseLine(parallelLine);
            //Заменить имя
            model.findNameId(poindLine1.getId(), newPoind.getId(), parallelLine.getId());
            //Добавить имя
            model.nameLineAdd(parallelLine);
            //Связать параллельные прямые
            model.parallelBindLine(model.findCircle(nameLine[0]), model.findCircle(nameLine[1]), poindLine1, newPoind);
            //Связать точки с прямой
            model.circlesBindLine(poindLine1, newPoind, parallelLine);
            //завершить построение
            poindAddVParallel = false;
            lineAddParallel = false;
            model.setParallelAdd(false);
            disableButton(false);//разблокировать кнопки
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
            model.setCreateShape(false);//Сбросить режим создания фигуры
        }

        //Построить окружность
        if (circleAdd && !poindAdd1) {
            //добавить центр окружности на доску
            if (!model.isPoindOldAdd() && !poindAdd1) {//false - новая точка true - взять имеющую
                poindLine1 = model.createPoindAdd(true);//создать новую вершину
                paneShape.getChildren().add(poindLine1);
            } else {
                poindLine1 = model.getTimeVer();
            }
            poindAdd1 = true;
            //создать новую окружность
            circle = model.createCircleAdd(poindLine1);
        }
        if (circleAdd && poindCircle) {
            model.updateCircle(circle);
            circleAdd = false;
            poindAdd1 = false;
            poindCircle = false;
            disableButton(false);//разблокировать кнопки
            //Вывод информации об объектах в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
            model.setCreateShape(false);//Сбросить режим создания фигуры
            //закончить построение окружности
        }

        event.consume();
    }//End onMousePressed()

    /**
     * Метод addLineRayStart(int Segment)
     * Метод начала построения отрезков, лучей и прямых.
     * Вход: номер геометрической фигуры, 0-отрезок, 1-луч, 2 прямая,3 треугольник
     *
     * @param Segment - номер фигуры
     */

    public void addLineRayStart(int Segment) {
        newLine = model.createLineAdd(Segment);//создать линию
        newLine.toBack();
        //увеличить индекс
        if (!model.isPoindOldAdd()) {//false - новая вершина true - взять имеющую
            poindLine1 = model.createPoindAdd(true);//создать новую вершину
            model.indexAdd(poindLine1);//увеличить индекс
            paneShape.getChildren(). add(poindLine1);
        } else {
            poindLine1 = model.getTimeVer();
        }

        //newLine = model.createLineAdd(Segment);//создать линию
        //Сохранить первую точку треугольника и первую сторону
        if (model.isTreangleAdd() && !poindAdd2) {
            poindTreangle1 = poindLine1;
            lineTriangle1 = newLine;//
        }
        paneShape.getChildren().add(newLine);//добавить на доску
        newLine.toBack();//переместить линию вниз под точку
        poindAdd2 = true;//режим добавления второй точки и последующих
    }

    /**
     * Метод addLineRayEnd()
     * Метод окончания построения геометрической фигуры.
     * Закрывает режим добавления фигур.
     */

    public void addLineRayEnd() {
        if (!model.isPoindOldAdd()) {
            poindLine2 = model.createPoindAdd(true);//создать новую вершину
            model.indexAdd(poindLine1);//увеличить индекс
            paneShape.getChildren().add(poindLine2);
        } else {
            poindLine2 = model.getTimeVer();
        }
        //Привязка свойств мышки
        model.mouseLine(newLine);
        //закрыть режим добавления
        poindAdd1 = false;//закрыть 1 точку
        poindAdd2 = false;//закрыть 2 точку
        model.setPoindOldAdd(false);//закрыть добавление из имеющихся точек
        model.setCreateLine(false);
    }

    /**
     * Метод onScroll(ScrollEvent event)
     * Метод изменения масштаба координатной сетки при вращении колесика мышки
     *
     * @param event - изменения колесика мышки
     */
    public void onScroll(ScrollEvent event) {
        //Проверить настройки отображения сетки
        if (model.isShowGrid()) {
            double sc = event.getDeltaY();//пересчитать обороты колеса
            gridViews.onScrollView(sc);//изменить масштаб
            updateShape();//обновить фигуры
        }
    }

    /**
     * Метод updateShape()
     * Метод перемещения всех геометрических объектов на доске при
     * перемещении координатной сетки или изменения масштаба.
     * Для перемещения используются мировые координаты фигур из коллекций.
     */
    private void updateShape() {
        if (!model.isCreateShape()) {
            //обновление точек
            for (PoindCircle p : model.getPoindCircles())
                if (p != null) {
                    Circle c = p.getCircle();
                    c.setCenterX(gridViews.accessX(p.getX()));
                    c.setCenterY(gridViews.accessY(p.getY()));
                }
            //Обновление всех линий
            for (PoindLine pl : model.getPoindLines()) {
                if (pl != null) {
                    //Обновляем отрезки
                    Line l = pl.getLine();
                    l.setStartX(gridViews.accessX(pl.getStX()));
                    l.setStartY(gridViews.accessY(pl.getStY()));
                    l.setEndX(gridViews.accessX(pl.getEnX()));
                    l.setEndY(gridViews.accessY(pl.getEnY()));
                }
            }

            //Обновление дуг углов
            for (VertexArc va : model.getVertexArcs()) {
                if (va != null) {
                    Arc a = va.getArc();
                    a.setCenterX(gridViews.accessX(va.getCenterX()));
                    a.setCenterY(gridViews.accessY(va.getCenterY()));
                    a.setRadiusX(va.getRadiusX());
                    a.setRadiusY(va.getRadiusY());
                    a.setStartAngle(va.getStartAngle());
                    a.setLength(va.getLengthAngle());
                }
            }
            //Обновление окружности
            for (CircleLine cp : model.getCircleLines()) {
                if (cp != null) {
                    Circle c = cp.getCircle();
                    c.setCenterX(gridViews.accessX(cp.getX()));
                    c.setCenterY(gridViews.accessY(cp.getY()));
                    double d = model.distance(gridViews.accessX(cp.getX()), gridViews.accessY(cp.getY()), gridViews.accessX(cp.getX() + cp.getRadius()), gridViews.accessY(cp.getY()));
                    c.setRadius(d);
                }
            }
        }

    }


    /**
     * Метод menuPoindClick().
     * Предназначен для вывода определения точки, прямой, отрезка.
     * Вызывается из пункта меню Фигуры->Точка, прямая, отрезок.
     */
    public void menuPoindClick() {
        model.webHTML(webViewLeft, "line.html");//Вывод в web файла
    }

    /**
     * Метод menuRayClick().
     * Предназначен для вывода определений луча и угла
     * Вызывается из пункта меню Фигуры->Луч и угол.
     */
    public void menuRayClick() {
        model.webHTML(webViewLeft, "rayandangle.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuBisectorAngle().
     * Предназначен для вывода теоремы о свойствах биссектрисы угла.
     * Вызывается из пункта меню Фигуры->Луч и угол->Свойство биссектрисы угла
     */
    public void menuBisectorAngle() {
        model.webHTML(webViewLeft, "bisectorAngle.html");
    }

    /**
     * Метод menuAngleClick().
     * Предназначен для вывода определений смежных и вертикальных углов
     * Вызывается из пункта меню Фигуры->Смежные и вертикальные углы
     */
    public void menuAngleClick() {
        model.webHTML(webViewLeft, "angle.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuTriangle()
     * Предназначен для вывода определения треугольника.
     * Вызывается из пункта меню Фигуры->Треугольник ->Определения треугольника.
     */
    public void menuTriangle() {
        model.webHTML(webViewLeft, "triangle.html");
    }

    /**
     * Метод menuTr()
     * Предназначен для вывода видов треугольников.
     * Вызывается из пункта меню Фигуры->Треугольник ->Виды треугольников.
     */
    public void menuTr() {
        // model.webViewLeftString(webViewLeft, 9);
        model.webHTML(webViewLeft, "treangleView.html");
    }

    /**
     * Метод menuBisector()
     * Предназначен для вывода определения биссектрисы.
     * Вызывается из пункта меню Фигуры->Треугольник ->Биссектриса треугольника.
     */
    public void menuBisector() {
        model.webHTML(webViewLeft, "bisectorTreangle.html");
    }

    /**
     * Метод menuMediana()
     * Предназначен для вывода определения биссектрисы.
     * Вызывается из пункта меню Фигуры->Треугольник ->Медиана треугольника.
     */
    public void menuMediana() {
        model.webHTML(webViewLeft, "medianaTreangle.html");
    }

    /**
     * Метод menuHeight()
     * Предназначен для вывода определения биссектрисы.
     * Вызывается из пункта меню Фигуры->Треугольник ->Высота треугольника.
     */
    public void menuHeight() {
        model.webHTML(webViewLeft, "heightTreangle.html");
    }


    /**
     * Метод menuCircle().
     * Предназначен для выводя определений окружности.
     * Вызывается из пункта меню Фигуры->Окружность.
     */
    public void menuCircle() {
        model.webHTML(webViewLeft, "circle.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuAcsiomy1Click().
     * Предназначен для вывода аксиом принадлежности из пункта
     * меню Аксиомы и следствие->Аксиомы принадлежности
     */
    public void menuAcsiomy1Click() {
        model.webHTML(webViewLeft, "acsiomy_1.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuAcsiomy2Click().
     * Предназначен для вывода аксиом принадлежности из пункта
     * меню Аксиомы и следствие->Аксиомы равенства и наложения
     */
    public void menuAcsiomy2Click() {
        model.webHTML(webViewLeft, "acsiomy_2.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuAcsiomy3Click().
     * Предназначен для вывода аксиом принадлежности из пункта
     * меню Аксиомы и следствие->Аксиомы измерения
     */
    public void menuAcsiomy3Click() {
        model.webHTML(webViewLeft, "acsiomy_3.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuAcsiomy4Click().
     * Предназначен для вывода аксиом принадлежности из пункта
     * меню Аксиомы и следствие->Аксиомы параллельности.
     */
    public void menuAcsiomy4Click() {
        model.webHTML(webViewLeft, "acsiomy_4.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuPerpend().
     * Вывод теоремы о перпендикуляре
     */
    public void menuPerpend() {
        model.webHTML(webViewLeft, "perpend.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuShowPoindName()
     * Предназначен для замены логической переменной, показывать большие буквы в именах точек.
     * Пункт из меню "Настройки - Показывать большие буквы."
     */
    public void menuShowPoindName() {
        visibleName(menuShowPoindName.isSelected(), "poind");
        model.setShowPoindName(menuShowPoindName.isSelected());//поменять логическую переменную
    }

    /**
     * Метод menuShowLineName().
     * Предназначен скрытия от показа имен прямых, лучей, отрезков.
     * Пункт из меню "Настройки - Показывать маленькие буквы".
     */
    public void menuShowLineName() {
        visibleName(menuShowLineName.isSelected(), "line");
        model.setShowLineName(menuShowLineName.isSelected()); //поменять логическую переменную
    }

    /**
     * Метод menuAngleName()
     * Предназначен для показа и скрытия имен углов
     * Пункт из меню "Настройки - Показывать имена углов".
     */
    public void menuAngleName() {
        visibleName(menuAngleName.isSelected(), "arc");
        model.setShowAngleName(menuAngleName.isSelected());//поменять логическую переменную
    }

    /**
     * Метод visibleNameLine(boolean bName, String name).
     * Предназначен для показа и скрытия имен.
     *
     * @param bName - логическая переменная (true - показывать, false - не показывать)
     * @param name  - какие имена (line, poind, arc)
     */
    private void visibleName(boolean bName, String name) {
        for (NamePoindLine pn : model.getNamePoindLines()) {
            if (pn != null) {
                if (pn.getType().equals(name)) {
                    pn.getText().setVisible(bName);
                    pn.setVisibleLine(bName);
                }
            }
        }
    }


    /**
     * Метод menuGrid().
     * Пункт меню "Настройки -> Показывать сетку".
     */
    public void menuGrid() {
        model.setShowGrid(menuGrid.isSelected());
        if (model.isShowGrid()) {
            gridViews.gridCartesian();//Вывод сетки
        } else {
            paneGrid.getChildren().clear();//Очистить экран и память
        }
    }

    /**
     * Метод menuIsosceles_1().
     * Нажат пункт меню "Теоремы и свойства-> Свойства равнобедренного треугольника->Теорема 1".
     */
    public void menuIsosceles_1() {
        // model.webViewLeftString(webViewLeft, 1);
        model.webHTML(webViewLeft, "isosceles_1.html");
    }

    /**
     * Метод menuIsosceles_2().
     * Нажат пункт меню "Теоремы и свойства-> Свойства равнобедренного треугольника->Теорема 2".
     */
    public void menuIsosceles_2() {
        model.webHTML(webViewLeft, "isosceles_2.html");
    }

    /**
     * Метод menuEquil()
     * Нажат пункт меню "Теоремы и свойства-> Первый признак равенства треугольников"
     */
    public void menuEquil() {
        model.setWindShow(0);
        TwofxmlLoader();
    }

    /**
     * Метод menuSecond()
     * Нажат пункт меню "Теоремы и свойства-> Второй признак равенства треугольников"
     */
    public void menuSecond() {
        model.setWindShow(1);
        TwofxmlLoader();
    }

    /**
     * Метод menuTread()
     * Нажат пункт меню "Теоремы и свойства-> Третий признак равенства треугольников"
     */
    public void menuTread() {
        model.setWindShow(2);
        TwofxmlLoader();
    }

    /**
     * Метод menuPriznak().
     * Нажат пункт меню "Теоремы и свойств-> Три признака параллельности прямых"
     */
    public void menuPriznak() {
        model.webHTML(webViewLeft, "priznak.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuPriznak_1().
     * Нажат пункт меню "Теоремы и свойства -> Теоремы об углах, образованных двумя параллельными прямыми и секущей->Теорема № 1"
     */
    public void menuPriznak_1() {
        model.webHTML(webViewLeft, "priznak_1.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuPriznak_2().
     * Нажат пункт меню "Теоремы и свойства -> Теоремы об углах, образованных двумя параллельными прямыми и секущей->Теорема № 2"
     */
    public void menuPriznak_2() {
        model.webHTML(webViewLeft, "priznak_2.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuPriznak_3().
     * Нажат пункт меню "Теоремы и свойства -> Теоремы об углах, образованных двумя параллельными прямыми и секущей->Теорема № 3"
     */
    public void menuPriznak_3() {
        model.webHTML(webViewLeft, "priznak_3.html");//Вывод в браузер файла html
    }


    /**
     * Метод menuSecantAngle_1().
     * Нажат пункт меню "Теоремы и свойства -> Теорема об углах с соответственно параллельными прямыми->Теорема1".
     */
    public void menuSecantAngle_1() {
        model.webHTML(webViewLeft, "perAngle_1.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuSecantAngle_2()
     * Нажат пункт меню "Теоремы и свойства -> Теорема об углах с соответственно параллельными прямыми->Теорема2".
     */
    public void menuSecantAngle_2() {
        model.webHTML(webViewLeft, "perAngle_2.html");//Вывод в браузер файла html
    }


    /**
     * Метод treangleAngle().
     * Нажат пункт меню "Теоремы и свойства -> Теорема о сумме углов треугольника"
     */
    public void treangleAngle() {
        model.webHTML(webViewLeft, "treangleAngle.html");//Вывод в браузер файла html
    }

    /**
     * Метод treangleTheorem_2().
     * Нажат пункт меню "Теоремы и свойства -> Теорема о соотношениях между сторонами углами треугольника"
     */
    public void treangleTheorem_2() {
        model.webHTML(webViewLeft, "treangleTheorem_2.html");
    }

    /**
     * Метод treangleTheorem_3().
     * Нажат пункт меню "Теоремы и свойства -> Неравенство треугольника
     */
    public void treangleTheorem_3() {
        model.webHTML(webViewLeft, "treangleTheorem_3.html");
    }

    /**
     * Метод treangleProperty().
     * Нажат пункт меню "Теоремы и свойства -> Свойства прямоугольных треугольников".
     */
    public void treangleProperty() {
        model.webHTML(webViewLeft, "treangleProperty.html");
    }


    /**
     * Метод treangleQuayle().
     * Нажат пункт меню "Теоремы и свойства -> Признаки равенства прямоугольных треугольников".
     */
    public void treangleQuayle() {
        model.webHTML(webViewLeft, "treangleQuayle.html");
    }

    /**
     * Метод distantPar().
     * Нажат пункт меню "Теоремы и свойства -> Расстояние между параллельными прямыми"
     */
    public void distantPar() {
        model.webHTML(webViewLeft, "distantPar.html");
    }

    /**
     * Метод menuAbout().
     * Нажат пункт меню "Помощь-> О программе"
     */

    public void menuAbout() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);//Блокирует все окна приложения
        window.initStyle(StageStyle.UTILITY);//Только кнопка закрыть
        VBox root = new VBox();
        root.setStyle(
                "-fx-background-image: url(/Images/About.png); " +
                        "-fx-background-repeat: no-repeat;"
        );
        root.setAlignment(Pos.TOP_CENTER);

        Label label2 = new Label("МБОУ \"Центр образования Опочецкого района\"\nСтруктурное подразделение \"Средняя школа № 4\"\n\n\n ");
        label2.setFont(Font.font("Verdana", FontWeight.BOLD, 24.0));
        label2.setTextFill(Color.SANDYBROWN);
        label2.setTextAlignment(TextAlignment.CENTER);
        Label label = new Label("Учебно-справочное пособие");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 34.0));
        label.setTextFill(Color.YELLOW);
        label.setTextAlignment(TextAlignment.CENTER);
        Label label1 = new Label("Геометрия\n\n");
        label1.setFont(Font.font("TimesRoman", FontWeight.BOLD, 58.0));
        label1.setTextFill(Color.RED);
        label1.setTextAlignment(TextAlignment.CENTER);
        Label label3 = new Label("Разработка ученика 8Б класса \n Носова Алексея \n2022 г. \nПрограмма с открытым исходным кодом.\n \n ");
        label3.setFont(Font.font("Courier", FontWeight.BOLD, 24.0));
        label3.setTextFill(Color.YELLOW);
        label3.setTextAlignment(TextAlignment.CENTER);

        root.getChildren().addAll(label2, label, label1, label3);
        Scene scene = new Scene(root, 864, 520);
        window.setScene(scene);
        window.setTitle("О программе");
        window.show();

    }

    /**
     * Метод btnPoindClick().
     * Cобытие нажатия кнопки "Добавить точку".
     * Устанавливает режим добавления точки.
     */
    public void btnPoindClick() {
        //Установить статус
        model.setStringLeftStatus(STA_1);
        model.notifyObservers("LeftStatusGo");
        visibleCreate();//сбросить все режимы
        disableButton(true);//блокировать кнопки
        model.setPoindAdd(true);//Установить режим добавления
        model.setCreateShape(true);//Установить режим создания фигуры
    }

    /**
     * Метод onMouseEnteredPoind().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить точку".
     */
    public void onMouseEnteredPoind() {
        model.setTextToolTip("Добавить точку");
        //Передать в View для вывода
        model.setBtnToolTip(btnPoind);
        model.notifyObservers("ToolTip");
    }

    /**
     * Метод btnSegmentClick().
     * Метод для события нажатия кнопки "Добавить отрезок".
     * Устанавливает режим добавления отрезка.
     */
    public void btnSegmentClick() {
        //Установить статус
        model.setStringLeftStatus(STA_2);
        model.notifyObservers("LeftStatusGo");
        visibleCreate();//сбросить все режимы
        disableButton(true);//блокировать кнопки
        segmentAdd = true;//режим построения отрезка
        model.setCreateShape(true);//Установить режим создания фигуры
    }

    /**
     * Метод onMoseEnteredSegment().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить отрезок".
     */
    public void onMoseEnteredSegment() {
        model.setTextToolTip("Добавить отрезок");
        //Передать в View для вывода
        model.setBtnToolTip(btnMiddleSegment);
        model.notifyObservers("ToolTip");
    }

    /**
     * Метод btnMiddleSegment().
     * Метод для события нажатия кнопки "Середина отрезка"
     * Предназначен для построения середины отрезка
     */
    public void btnMiddleSegment() {
        model.setStringLeftStatus(STA_31);
        model.notifyObservers("LeftStatusGo");
        visibleCreate();//сбросить все режимы
        disableButton(true);//блокировать кнопки
        middleSegmentAdd = true;//режим построения отрезка
        model.setCreateShape(true);//Установить режим создания фигуры
    }

    /**
     * Метод onMouseEnteredMiddleSegment().
     * Всплывающая подсказка при наведении мышки на кнопку "Найти середину отрезка"
     */
    public void onMouseEnteredMiddleSegment() {
        model.setTextToolTip("Найти середину отрезка");
        //Передать в View для вывода
        model.setBtnToolTip(btnSegment);
        model.notifyObservers("ToolTip");
    }

    /**
     * Метод btnRay().
     * Метод для события нажатия кнопки "Добавить луч".
     * Устанавливает режим добавления луча.
     */
    public void btnRay() {
        //Установить статус
        model.setStringLeftStatus(STA_3);
        model.notifyObservers("LeftStatusGo");
        visibleCreate();//сбросить все режимы
        disableButton(true);//блокировать кнопки
        rayAdd = true;//режим построения луча
        model.setCreateShape(true);//Установить режим создания фигуры
    }

    /**
     * Метод onMouseEnteredRay().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить луч".
     */
    public void onMouseEnteredRay() {
        model.setTextToolTip("Добавить луч");
        //Передать в View для вывода
        model.setBtnToolTip(btnRay);
        model.notifyObservers("ToolTip");
    }

    /**
     * Метод btLine().
     * Метод для события нажатия кнопки "Добавить прямую".
     * Устанавливает режим добавления прямой.
     */
    public void btnLine() {
        //Установить статус
        model.setStringLeftStatus(STA_4);
        model.notifyObservers("LeftStatusGo");
        visibleCreate();//сбросить все режимы
        disableButton(true);//блокировать кнопки
        lineAdd = true;//режим построения прямой
        model.setCreateShape(true);//Установить режим создания фигуры
    }

    /**
     * Метод onMouseEnteredLine().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить прямую".
     */
    public void onMouseEnteredLine() {
        model.setTextToolTip("Добавить прямую");
        //Передать в View для вывода
        model.setBtnToolTip(btnLine);
        model.notifyObservers("ToolTip");
    }

    /**
     * Метод btnAngle()
     * Метод для события нажатия кнопки "Добавить угол".
     * Устанавливает режим добавления угла.
     */
    public void btnAngle() {
        //Установить статус
        model.setStringLeftStatus(STA_14);
        model.notifyObservers("LeftStatusGo");
        infoStatus = "";//Сбросить переменную для новой записи вершин угла
        visibleCreate();//сбросить все режимы
        disableButton(true);//блокировать кнопки
        model.setAngleAdd(true);//режим построения угла
        model.setCreateShape(true);//Установить режим создания фигуры
    }

    /**
     * Метод onMouseEnteredAngle()
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить угол"
     */
    public void onMouseEnteredAngle() {
        model.setTextToolTip("Добавить угол");
        //Передать в View для вывода
        model.setBtnToolTip(btnAngle);
        model.notifyObservers("ToolTip");
    }

    /**
     * Метод btnVertical()
     * Метод для события нажатия кнопки "Провести перпендикуляр к прямой"
     * Устанавливает режим построения перпендикуляра к прямой.
     */
    public void btnVertical() {
        model.setStringLeftStatus(STA_26);
        model.notifyObservers("LeftStatusGo");
        visibleCreate();//сбросить все режимы
        disableButton(true);//блокировать кнопки
        model.setVerticalAdd(true);//режим построения перпендикуляра
        model.setCreateShape(true);//Установить режим создания фигуры
    }

    /**
     * Метод btnCircle().
     * Метод для события нажатия кнопки "Построить окружность".
     * Устанавливает режим построения окружности.
     */
    public void btnCircleClick() {
        model.setStringLeftStatus(STA_28);
        model.notifyObservers("LeftStatusGo");
        visibleCreate();//сбросить все режимы
        disableButton(true);//блокировать кнопки
        circleAdd = true;//режим построения окружности
        model.setCreateShape(true);//Установить режим создания фигуры
    }

    /**
     * Метод btnParallelLimes().
     * Метод для события нажатия кнопки "Построить параллельные прямые"
     * Устанавливает режим построения параллельных прямых
     */
    public void btnParallelLines() {
        model.setStringLeftStatus(STA_15);
        model.notifyObservers("LeftStatusGo");
        visibleCreate();//сбросить все режимы
        disableButton(true);//блокировать кнопки
        model.setParallelAdd(true);//режим построения параллельных прямых
        model.setCreateShape(true);//Установить режим создания фигуры
    }

    /**
     * Метод onMouseEnteredVertical()
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить перпендикуляр к прямой"
     */
    public void onMouseEnteredVertical() {
        model.setTextToolTip("Построить перпендикуляр к прямой");
        //Передать в View для вывода
        model.setBtnToolTip(btnVertical);
        model.notifyObservers("ToolTip");
    }


    /**
     * Метод onMouseEnteredCircle().
     * Всплывающая подсказка при наведении мышки на кнопку "Построить окружность"
     */
    public void onMouseEnteredCircle() {
        model.setTextToolTip("Добавить окружность");
        //Передать в View для вывода
        model.setBtnToolTip(btnCircle);
        model.notifyObservers("ToolTip");
    }

    /**
     * Метод onMouseEnteredParallelLines().
     * Всплывающая подсказка при наведении мышки на кнопку "Построить параллельные прямые"
     */
    public void onMouseEnteredParallelLines() {
        model.setTextToolTip("Построить параллельную прямую");
        //Передать в View для вывода
        model.setBtnToolTip(btnParallelLines);
        model.notifyObservers("ToolTip");
    }


    /**
     * Метод btnTreangle().
     * Метод на события нажатия кнопки "Добавить треугольник".
     * Устанавливает режим добавления треугольника.
     */
    public void btnTreangle() {
        model.setStringLeftStatus(STA_5);
        model.notifyObservers("LeftStatusGo");
        infoStatus = "";//Сбросить переменную для новой записи вершин треугольника
        visibleCreate();//сбросить все режимы
        disableButton(true);//блокировать кнопки
        model.setTreangleAdd(true);
        model.setCreateShape(true);//Установить режим создания фигуры
    }

    /**
     * Метод onMouseEnteredTreangle().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить треугольник".
     */
    public void onMouseEnteredTreangle() {
        model.setTextToolTip("Добавить треугольник");
        //Передать в View для вывода
        model.setBtnToolTip(btnTreangle);
        model.notifyObservers("ToolTip");
    }

    /**
     * Метод btnMedian().
     * Предназначен для установления режима добавить медиану. Нажата кнопка "Добавить медиану"
     */
    public void btnMedian() {
        model.setStringLeftStatus(STA_18);
        model.notifyObservers("LeftStatusGo");
        visibleCreate();//сбросить все режимы
        disableButton(true);//блокировать кнопки
        medianaAdd = true;
        model.setCreateShape(true);//Установить режим создания фигуры
    }

    /**
     * Метод onMouseEnteredMediana().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить медиану".
     */
    public void onMouseEnteredMediana() {
        model.setTextToolTip("Добавить медиану");
        //Передать в View для вывода
        model.setBtnToolTip(btnMediana);
        model.notifyObservers("ToolTip");
    }

    /**
     * Метод btnBisector()
     * Предназначен для установления режима добавить биссектрису. Нажата кнопка "Добавить биссектрису".
     */
    public void btnBisector() {
        model.setStringLeftStatus(STA_22);
        model.notifyObservers("LeftStatusGo");
        visibleCreate();//сбросить все режимы
        disableButton(true);//блокировать кнопки
        bisectorAdd = true;
        model.setCreateShape(true);//Установить режим создания фигуры
    }

    /**
     * Метод onMouseEnteredBisector().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить биссектрису".
     */
    public void onMouseEnteredBisector() {
        model.setTextToolTip("Добавить биссектрису");
        //Передать в View для вывода
        model.setBtnToolTip(btnBisector);
        model.notifyObservers("ToolTip");
    }

    /**
     * Метод btnHeight()
     * Нажата кнопка добавить высоту.
     */
    public void btnHeight() {
        model.setStringLeftStatus(STA_24);
        model.notifyObservers("LeftStatusGo");
        visibleCreate();//сбросить все режимы
        disableButton(true);//блокировать кнопки
        heightAdd = true;
        model.setCreateShape(true);//Установить режим создания фигуры
    }

    /**
     * Метод onMouseEnteredHeight().
     * Всплывающая подсказка при наведении на кнопку "Добавить высоту"
     */
    public void onMouseEnteredHeight() {
        model.setTextToolTip("Добавить высоту");
        //Передать в View для вывода
        model.setBtnToolTip(btnHeight);
        model.notifyObservers("ToolTip");
    }

    /**
     * Метод btnDelete()
     * Нажата копка "Удалить геометрическую фигуру." Метод удаляет все геометрические объекты.
     */
    public void btnDelete() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Вы уверены, что надо удалить все геометрические фигуры?", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Удаление геометрический фигур");
        alert.setHeaderText("Очистить доску от всех геометрических фигур.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            //удаляем из коллекций
            paneShape.getChildren().clear();
            model.getPoindCircles().clear();
            model.getPoindLines().clear();
            model.getVertexArcs().clear();
            model.getNamePoindLines().clear();
            model.getTreangleNames().clear();
            model.getCircleLines().clear();
            model.initIndex();//инициализация индексов
            model.setTxtShape("");
            model.txtAreaOutput();
            treangleVisibly = false;//режим блокировки кнопок медиана, биссектриса, высота
            disableButton(false);//сбросить режим блокировки
            btnHeight.setDisable(true);//заблокировать
            btnMediana.setDisable(true);//заблокировать
            btnBisector.setDisable(true);//заблокировать
        }
    }

    /**
     * Метод onMouseEnteredDelete()
     * Всплывающая подсказка при наведении на кнопку "Удалить"
     */
    public void onMouseEnteredDelete() {
        model.setTextToolTip("Удалить с доски все геометрические фигуры");
        //Передать в View для вывода
        model.setBtnToolTip(btnDelete);
        model.notifyObservers("ToolTip");
    }

    /**
     * Метод onKeyPressed(KeyEvent key)
     * Предназначен для сброса режима построения и разблокировок кнопок.
     *
     * @param key - нажата кнопка ESC
     */
    public void onKeyPressed(KeyEvent key) {
        if (key.getCode() == KeyCode.ESCAPE) {
            disableButton(false);//разблокировать кнопки
            // visibleCreate();//сбросить все режимы
        }
    }


    /**
     * Метод TwofxmlLoader().
     * Предназначен для загрузки шаблона для признаков равенства треугольников
     */
    private void TwofxmlLoader() {
        try {
            Parent root1 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("equality.fxml")));
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Признаки равенства треугольников");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Тестовая кнопка вывод информации по всем коллекциям для тестирования системы
    public void btnTest() {
        model.ColTest();
    }


}


