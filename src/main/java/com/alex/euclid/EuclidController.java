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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.val;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Optional;

import static contstantString.StringStatus.*;

/**
 * Класс управления приложением (Controller), наследует класс View.
 * Реагирует на все события управления от мыши и клавиатуры.
 * Вызывает методы из класса модели для обработки событий
 *
 * @author A. Nosov
 * @version 1.6
 */

public class EuclidController extends View {
    public Font x3;
    public Line newLine;
    public Circle circleFill;


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
    private StackPane cartesian;
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
    @FXML
    private Button btnTangent;
    @FXML
    private Button btnCircleInTreangle;
    @FXML
    private Button btnCircleOutTreangle;

    //Web браузер для вывода данных
    @FXML
    private WebView webViewLeft;//для размещения информации слева от доски
    @FXML
    private Pane paneGrid;//контейнер для сетки
    @FXML
    private Label leftStatus;//Левый статус
    @FXML
    private Label rightStatus;//Правый статус
    @FXML
    private Label coordinateX;
    @FXML
    private Label coordinateY;

    @FXML
    private CheckMenuItem menuShowPoindName;
    @FXML
    private CheckMenuItem menuShowLineName;
    @FXML
    private CheckMenuItem menuShowGrid;
    @FXML
    private CheckMenuItem menuAngleName;
    @FXML
    public CheckMenuItem menuShowLine;
    // Таблица цветов
    private static final String COLOR_TABLE1 = "0xff0000ff";
    private static final String COLOR_TABLE2 = "0x00000000";
    private static final String COLOR_TABLE3 = "0xffb6c1ff";
    private static final String COLOR_TABLE4 = "0x00008bff";
    private static final String COLOR_TABLE5 = "0x87cefaff";
    private static final String COLOR_TABLE6 = "0x006400ff";
    private static final String COLOR_TABLE7 = "0xadff2fff";
    private static final String COLOR_TABLE8 = "0xffd700ff";
    private static final String COLOR_TABLE9 = "0x000000ff";
    private static final String COLOR_TABLE10 = "0xffffe0ff";
    private static final String COLOR_TABLE11 = "0xd3d3d3ff";
    // Всплывающие сообщения при наведении мыши на кнопки
    private static final String TOOL_TIP = "ToolTip";
    // Вывод сообщения в строке статуса.
    private static final String LEFT_STATUS_GO = "LeftStatusGo";


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
        model.setNewLine(newLine);//Передать ссылку для построения отрезка, луча, прямой
        model.setCircleFill(circleFill);//Передать ссылку для точки при наведении на точку
        model.webHTML(webViewLeft, "geometry.html");//Вывод в web файла html (что такое геометрия)
        //Формируем цветовую таблицу
        model.tableColor[0][0] = COLOR_TABLE1;
        model.tableColor[1][0] = COLOR_TABLE1;
        model.tableColor[2][0] = COLOR_TABLE2;
        model.tableColor[0][1] = COLOR_TABLE2;
        model.tableColor[1][1] = COLOR_TABLE3;
        model.tableColor[2][1] = COLOR_TABLE1;
        model.tableColor[0][2] = COLOR_TABLE1;
        model.tableColor[1][2] = COLOR_TABLE4;
        model.tableColor[2][2] = COLOR_TABLE2;
        model.tableColor[0][3] = COLOR_TABLE2;
        model.tableColor[1][3] = COLOR_TABLE5;
        model.tableColor[2][3] = COLOR_TABLE4;
        model.tableColor[0][4] = COLOR_TABLE6;
        model.tableColor[1][4] = COLOR_TABLE6;
        model.tableColor[2][4] = COLOR_TABLE2;
        model.tableColor[0][5] = COLOR_TABLE2;
        model.tableColor[1][5] = COLOR_TABLE7;
        model.tableColor[2][5] = COLOR_TABLE6;
        model.tableColor[0][6] = COLOR_TABLE8;
        model.tableColor[1][6] = COLOR_TABLE8;
        model.tableColor[2][6] = COLOR_TABLE2;
        model.tableColor[0][7] = COLOR_TABLE2;
        model.tableColor[1][7] = COLOR_TABLE10;
        model.tableColor[2][7] = COLOR_TABLE8;
        model.tableColor[0][8] = COLOR_TABLE9;
        model.tableColor[1][8] = COLOR_TABLE9;
        model.tableColor[2][8] = COLOR_TABLE2;
        model.tableColor[0][9] = COLOR_TABLE2;
        model.tableColor[1][9] = COLOR_TABLE11;
        model.tableColor[2][9] = COLOR_TABLE9;
        //формирование линий координат и сетки, перерасчет при изменении размеров доски
        gridViews.setPaneGrid(paneGrid);
        gridViews.setCartesian(cartesian);
        //Изменение ширины окна
        cartesian.widthProperty().addListener((obs, oldVal, newVal) -> {
            gridViews.setVr(cartesian.getWidth());
            gridViews.setWl(-cartesian.getWidth() / 2);
            gridViews.setWr(cartesian.getWidth() / 2);
            gridViews.rate();//Перерасчет коэффициентов
            paneGrid.getChildren().clear();//Очистить экран и память
            gridViews.gridCartesian();//Вывод сетки
            updateShape();//обновить координаты геометрических фигур
        });
        //Изменение высоты окна
        cartesian.heightProperty().addListener((obs, oldVal, newVal) -> {
            gridViews.setVb(cartesian.getHeight());
            gridViews.setWt(cartesian.getHeight() / 2);
            gridViews.setWb(-cartesian.getHeight() / 2);
            gridViews.rate();//Перерасчет коэффициентов
            paneGrid.getChildren().clear();//Очистить экран и память
            gridViews.gridCartesian();//Вывод сетки
            updateShape();//обновить координаты геометрических фигур
        });
        gridViews.gridCartesian();//Вывод сетки
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
        btnTangent.setDisable(b);
        if (model.isTreangleVisibly()) {
            btnBisector.setDisable(b);
            btnMediana.setDisable(b);
            btnHeight.setDisable(b);
            btnCircleInTreangle.setDisable(b);
            btnCircleOutTreangle.setDisable(b);
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
        //Координаты мышки экрана
        model.setScreenXY(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
        //Координаты мышки пересчитанные в декартовые
        model.setDecartX(gridViews.revAccessX(mouseEvent.getX()));
        model.setDecartY(gridViews.revAccessY(mouseEvent.getY()));
        //Вывод координат в окно уведомлений
        DecimalFormat dF = new DecimalFormat("#.##");
        rightStatus.setText("Координаты:  ");
        coordinateX.setText(" x: " + dF.format(gridViews.revAccessX(mouseEvent.getX())));
        coordinateY.setText(" y: " + dF.format(gridViews.revAccessY(mouseEvent.getY())));

        //Построение отрезка и треугольника
        if ((model.getCreateGeometric() == 2 || model.getCreateGeometric() == 8) && model.isPoindOne()) {
            newLine.setVisible(true);//вспомогательная линия, сделать видимой
            model.setLine(newLine);//вывод на доску
            model.notifyObservers("SideGo");
            model.setPoindTwo(true);//построение второй точки
            model.lineAddPoind(newLine);//приклеить к ближайшей точке
        }
        //построение прямой
        if (model.getCreateGeometric() == 3 && model.isPoindOne()) {
            //рассчитать концы прямой по уравнению прямой
            model.createMoveLine(newLine, 3);
            model.setPoindTwo(true);//разрешение для постройки 2 точки
        }
        //построение луча
        if (model.getCreateGeometric() == 4 && model.isPoindOne()) {
            newLine.setVisible(true);
            //рассчитать конец луча по уравнению прямой
            model.createMoveLine(newLine, 4);
            model.setPoindTwo(true);//разрешение для постройки 2 точки
        }
        //Построение окружности
        if (model.getCreateGeometric() == 14 && model.isPoindOne()) {
            //Насчитать радиус
            double r = model.distance(model.getSegmentStartX(), model.getScreenXY().getY(), model.getScreenXY().getX(), model.getScreenXY().getY());
            double rw = model.accessRadiusW(new Point2D(gridViews.revAccessX(model.getSegmentStartX()), gridViews.revAccessY(model.getSegmentStartY())), r);
            model.setRadiusCircle(r);
            model.setRadiusCircleW(rw);
            model.setPoindTwo(true);
            model.notifyObservers("CircleGo");
        }
    }

    /**
     * Метод onMouseDragged()
     * Отслеживает события перемещения мышки с нажатой левой кнопкой
     * Используется для перемещения сетки и координатных осей по доске.
     *
     * @param event - свойство мыши.
     */
    public void onMouseDragged(MouseEvent event) {
        //координаты, нужны для перемещения объектов на доске
        model.setScreenXY(new Point2D(event.getX(), event.getY()));
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
            gridViews.gridCartesian();//Вывод сетки
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
        //Вызвать режим построения геометрических фигур
        model.createGeometrics();
        //Сбросить режим построения
        if (model.getCreateGeometric() == 0) {
            disableButton(false);//сделать доступными кнопки
            model.setCreateShape(false);//сбросить переменную построения
            //Добавить в правую часть доски
            model.setTxtShape("");
            model.txtAreaOutput();
        }
        event.consume();
    }//End onMousePressed()


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
     * Метод перемещения точек при перемещении координатной сетки или изменения масштаба.
     * Для перемещения используются мировые координаты из коллекции.
     */
    private void updateShape() {
        if (!model.isCreateShape()) {
            //обновление точек
            model.getPoindCircles().forEach(p -> {
                if (p != null) {
                    model.setScreenXY(new Point2D(gridViews.accessX(p.getXy().getX()), gridViews.accessY(p.getXy().getY())));
                    model.setVertex(p.getCircle());
                    model.notifyObservers("VertexGo");
                }
            });
//            Обновление окружностей
            model.getCircleLines().forEach(p -> {
                if (p != null) {
                    model.setSegmentStartX(gridViews.accessX(p.getX()));
                    model.setSegmentStartY(gridViews.accessY(p.getY()));
                    model.setRadiusCircle(model.accessRadius(new Point2D(p.getX(), p.getY()), p.getRadius()));
                    model.setCircle(p.getCircle());
                    model.notifyObservers("CircleGo");
                }
            });
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
     * Метод menuAcxiom1Click().
     * Предназначен для вывода аксиом принадлежности из пункта
     * меню Аксиомы и следствие->Аксиомы принадлежности
     */
    public void menuAcxiom1Click() {
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
            if (pn != null && pn.getType().equals(name)) {
                pn.getText().setVisible(bName);
                pn.setVisibleLine(bName);
            }
        }
    }


    /**
     * Метод menuGrid().
     * Пункт меню "Настройки -> Показывать сетку".
     */
    public void menuShowGrid() {
        gridViews.setGridShow(menuShowGrid.isSelected());
        paneGrid.getChildren().clear();//Очистить экран и память
        gridViews.gridCartesian();//Вывод сетки
    }

    /**
     * Метод showGridLine().
     * Пункт меню "Настройки -> Показывать координатные оси".
     */
    public void showGridLine() {
        gridViews.setGridLineShow(menuShowLine.isSelected());
        paneGrid.getChildren().clear();//Очистить экран и память
        gridViews.gridCartesian();//Вывод сетки
    }

    /**
     * Метод menuIsosceles_1().
     * Нажат пункт меню "Теоремы и свойства-> Свойства равнобедренного треугольника->Теорема 1".
     */
    public void menuIsosceles1() {
        model.webHTML(webViewLeft, "isosceles_1.html");
    }

    /**
     * Метод menuIsosceles_2().
     * Нажат пункт меню "Теоремы и свойства-> Свойства равнобедренного треугольника->Теорема 2".
     */
    public void menuIsosceles2() {
        model.webHTML(webViewLeft, "isosceles_2.html");
    }

    /**
     * Метод menuEqual()
     * Нажат пункт меню "Теоремы и свойства-> Первый признак равенства треугольников"
     */
    public void menuEqual() {
        model.setWindShow(0);
        twofxmlLoader();
    }

    /**
     * Метод menuSecond()
     * Нажат пункт меню "Теоремы и свойства-> Второй признак равенства треугольников"
     */
    public void menuSecond() {
        model.setWindShow(1);
        twofxmlLoader();
    }

    /**
     * Метод menuTread()
     * Нажат пункт меню "Теоремы и свойства-> Третий признак равенства треугольников"
     */
    public void menuTread() {
        model.setWindShow(2);
        twofxmlLoader();
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
    public void menuPriznak1() {
        model.webHTML(webViewLeft, "priznak_1.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuPriznak_2().
     * Нажат пункт меню "Теоремы и свойства -> Теоремы об углах, образованных двумя параллельными прямыми и секущей->Теорема № 2"
     */
    public void menuPriznak2() {
        model.webHTML(webViewLeft, "priznak_2.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuPriznak_3().
     * Нажат пункт меню "Теоремы и свойства -> Теоремы об углах, образованных двумя параллельными прямыми и секущей->Теорема № 3"
     */
    public void menuPriznak3() {
        model.webHTML(webViewLeft, "priznak_3.html");//Вывод в браузер файла html
    }


    /**
     * Метод menuSecantAngle_1().
     * Нажат пункт меню "Теоремы и свойства -> Теорема об углах с соответственно параллельными прямыми->Теорема1".
     */
    public void menuSecantAngle1() {
        model.webHTML(webViewLeft, "perAngle_1.html");//Вывод в браузер файла html
    }

    /**
     * Метод menuSecantAngle_2()
     * Нажат пункт меню "Теоремы и свойства -> Теорема об углах с соответственно параллельными прямыми->Теорема2".
     */
    public void menuSecantAngle2() {
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
    public void treangleTheorem2() {
        model.webHTML(webViewLeft, "treangleTheorem_2.html");
    }

    /**
     * Метод treangleTheorem_3().
     * Нажат пункт меню "Теоремы и свойства -> Неравенство треугольника
     */
    public void treangleTheorem3() {
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
     * Метод menuHelp_1()
     * Для вывода видео о работе с программой.
     */
    public void menuHelp1() {
        model.webHTML(webViewLeft, "help_1.html");
    }

    /**
     * Метод createShape().
     * Задает режим создания геометрических фигур.
     * @param addShape определяет форму геометрических фигур.
     */
    public void createShape(int addShape) {
        disableButton(true);//блокировать кнопки
        model.setCreateGeometric(addShape);
        model.setCreateShape(true);//Установить режим создания фигуры
    }
   /**
     * Метод btnPoindClick().
     * Cобытие нажатия кнопки "Добавить точку".
     * Устанавливает режим добавления точки.
     */
    public void btnPoindClick() {
        //Установить статус
        model.setStringLeftStatus(STA_1);
        model.notifyObservers(LEFT_STATUS_GO);
        //Установить режим добавления точки
        createShape(1);
    }

    /**
     * Метод onMouseEnteredPoind().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить точку".
     */
    public void onMouseEnteredPoind() {
        model.setTextToolTip("Добавить точку");
        //Передать в View для вывода
        model.setBtnToolTip(btnPoind);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод btnSegmentClick().
     * Метод для события нажатия кнопки "Добавить отрезок".
     * Устанавливает режим добавления отрезка.
     */
    public void btnSegmentClick() {
        //Установить статус
        model.setStringLeftStatus(STA_2);
        model.notifyObservers(LEFT_STATUS_GO);
        createShape(2);
    }

    /**
     * Метод onMoseEnteredSegment().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить отрезок".
     */
    public void onMoseEnteredSegment() {
        model.setTextToolTip("Добавить отрезок");
        //Передать в View для вывода
        model.setBtnToolTip(btnMiddleSegment);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод btnMiddleSegment().
     * Метод для события нажатия кнопки "Середина отрезка"
     * Предназначен для построения середины отрезка
     */
    public void btnMiddleSegment() {
        model.setStringLeftStatus(STA_31);
        model.notifyObservers(LEFT_STATUS_GO);
        createShape(12);
    }

    /**
     * Метод onMouseEnteredMiddleSegment().
     * Всплывающая подсказка при наведении мышки на кнопку "Найти середину отрезка"
     */
    public void onMouseEnteredMiddleSegment() {
        model.setTextToolTip("Найти середину отрезка");
        //Передать в View для вывода
        model.setBtnToolTip(btnSegment);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод btnRay().
     * Метод для события нажатия кнопки "Добавить луч".
     * Устанавливает режим добавления луча.
     */
    public void btnRay() {
        model.setStringLeftStatus(STA_3);
        model.notifyObservers(LEFT_STATUS_GO);
        createShape(4);
    }

    /**
     * Метод onMouseEnteredRay().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить луч".
     */
    public void onMouseEnteredRay() {
        model.setTextToolTip("Добавить луч");
        //Передать в View для вывода
        model.setBtnToolTip(btnRay);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод btLine().
     * Метод для события нажатия кнопки "Добавить прямую".
     * Устанавливает режим добавления прямой.
     */
    public void btnLine() {
        model.setStringLeftStatus(STA_4);
        model.notifyObservers(LEFT_STATUS_GO);
            createShape(3);
    }

    /**
     * Метод onMouseEnteredLine().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить прямую".
     */
    public void onMouseEnteredLine() {
        model.setTextToolTip("Добавить прямую");
        //Передать в View для вывода
        model.setBtnToolTip(btnLine);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод btnAngle()
     * Метод для события нажатия кнопки "Добавить угол".
     * Устанавливает режим добавления угла.
     */
    public void btnAngle() {
        //Установить статус
        model.setStringLeftStatus(STA_14);
        model.notifyObservers(LEFT_STATUS_GO);
        model.setColVertex(3);//Количество вершин для угла
        createShape(5);
    }

    /**
     * Метод onMouseEnteredAngle()
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить угол"
     */
    public void onMouseEnteredAngle() {
        model.setTextToolTip("Добавить угол");
        //Передать в View для вывода
        model.setBtnToolTip(btnAngle);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод btnVertical()
     * Метод для события нажатия кнопки "Провести перпендикуляр к прямой"
     * Устанавливает режим построения перпендикуляра к прямой.
     */
    public void btnVertical() {
        model.setStringLeftStatus(STA_26);
        model.notifyObservers(LEFT_STATUS_GO);
        createShape(6);
    }

    /**
     * Метод btnCircle().
     * Метод для события нажатия кнопки "Построить окружность".
     * Устанавливает режим построения окружности.
     */
    public void btnCircleClick() {
        model.setStringLeftStatus(STA_28);
        model.notifyObservers(LEFT_STATUS_GO);
        createShape(14);
    }

    /**
     * Метод btnTangentClick().
     * Метод для события нажатия кнопки "Построить касательную к окружности".
     * Устанавливает режим построения касательной к окружности.
     */
    public void btnTangentClick() {
        model.setStringLeftStatus(STA_33);
        model.notifyObservers(LEFT_STATUS_GO);
        createShape(15);
    }

    /**
     * Метод btnParallelLimes().
     * Метод для события нажатия кнопки "Построить параллельные прямые"
     * Устанавливает режим построения параллельных прямых
     */
    public void btnParallelLines() {
        model.setStringLeftStatus(STA_15);
        model.notifyObservers(LEFT_STATUS_GO);
        createShape(7);
    }

    /**
     * Метод onMouseEnteredVertical()
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить перпендикуляр к прямой"
     */
    public void onMouseEnteredVertical() {
        model.setTextToolTip("Построить перпендикуляр к прямой");
        //Передать в View для вывода
        model.setBtnToolTip(btnVertical);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод onMouseEnteredCircle().
     * Всплывающая подсказка при наведении мышки на кнопку "Построить окружность"
     */
    public void onMouseEnteredCircle() {
        model.setTextToolTip("Добавить окружность");
        //Передать в View для вывода
        model.setBtnToolTip(btnCircle);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод onMouseEnteredParallelLines().
     * Всплывающая подсказка при наведении мышки на кнопку "Построить параллельные прямые"
     */
    public void onMouseEnteredParallelLines() {
        model.setTextToolTip("Построить параллельную прямую");
        //Передать в View для вывода
        model.setBtnToolTip(btnParallelLines);
        model.notifyObservers(TOOL_TIP);
    }


    /**
     * Метод btnTreangle().
     * Метод на события нажатия кнопки "Добавить треугольник".
     * Устанавливает режим добавления треугольника.
     */
    public void btnTreangle() {
        model.setStringLeftStatus(STA_5);
        model.notifyObservers(LEFT_STATUS_GO);
        model.setColVertex(3);//задать количество вершин для треугольника
        createShape(8);
    }

    /**
     * Метод onMouseEnteredTreangle().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить треугольник".
     */
    public void onMouseEnteredTreangle() {
        model.setTextToolTip("Добавить треугольник");
        //Передать в View для вывода
        model.setBtnToolTip(btnTreangle);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод btnMedian().
     * Предназначен для установления режима добавить медиану. Нажата кнопка "Добавить медиану"
     */
    public void btnMedian() {
        model.setStringLeftStatus(STA_18);
        model.notifyObservers(LEFT_STATUS_GO);
        createShape(9);
    }

    /**
     * Метод onMouseEnteredMediana().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить медиану".
     */
    public void onMouseEnteredMediana() {
        model.setTextToolTip("Добавить медиану");
        //Передать в View для вывода
        model.setBtnToolTip(btnMediana);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод btnBisector()
     * Предназначен для установления режима добавить биссектрису. Нажата кнопка "Добавить биссектрису".
     */
    public void btnBisector() {
        model.setStringLeftStatus(STA_22);
        model.notifyObservers(LEFT_STATUS_GO);
        createShape(11);
    }

    /**
     * Метод onMouseEnteredBisector().
     * Всплывающая подсказка при наведении мышки на кнопку "Добавить биссектрису".
     */
    public void onMouseEnteredBisector() {
        model.setTextToolTip("Добавить биссектрису");
        //Передать в View для вывода
        model.setBtnToolTip(btnBisector);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод btnHeight()
     * Нажата кнопка добавить высоту.
     */
    public void btnHeight() {
        model.setStringLeftStatus(STA_24);
        model.notifyObservers(LEFT_STATUS_GO);
        createShape(10);
    }

    /**
     * Метод onMouseEnteredHeight().
     * Всплывающая подсказка при наведении на кнопку "Добавить высоту"
     */
    public void onMouseEnteredHeight() {
        model.setTextToolTip("Добавить высоту");
        //Передать в View для вывода
        model.setBtnToolTip(btnHeight);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод btnCircleInTreangle(). Нажата кнопка "Построить окружность вписанную в треугольник".
     */
    public void btnCircleInTreangle() {
        model.setStringLeftStatus(STA_38);
        model.notifyObservers(LEFT_STATUS_GO);
        createShape(16);
    }

    /**
     * Метод onMouseEnteredCircleInTreangle(). Всплывающая подсказка при наведении на кнопку
     * "Построить окружность вписанную в треугольник".
     */
    public void onMouseEnteredCircleInTreangle() {
        model.setTextToolTip("Построить окружность вписанную в треугольник");
        //Передать в View для вывода
        model.setBtnToolTip(btnCircleInTreangle);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод btnCircleOutTreangle(). Нажата кнопка "Построить описанную окружность".
     */
    public void btnCircleOutTreangle() {
        model.setStringLeftStatus(STA_39);
        model.notifyObservers(LEFT_STATUS_GO);
        createShape(17);
    }

    /**
     * Метод onMouseEnteredCircleOutTreangle(). Всплывающая подсказка при наведении на кнопку
     * "Построить описанную окружность".
     */
    public void onMouseEnteredCircleOutTreangle() {
        model.setTextToolTip("Построить описанную окружность");
        //Передать в View для вывода
        model.setBtnToolTip(btnCircleOutTreangle);
        model.notifyObservers(TOOL_TIP);
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
            model.setTreangleVisibly(false);//режим блокировки кнопок медиана, биссектриса, высота
            disableButton(false);//сбросить режим блокировки
            btnHeight.setDisable(true);//заблокировать
            btnMediana.setDisable(true);//заблокировать
            btnBisector.setDisable(true);//заблокировать
            btnCircleOutTreangle.setDisable(true);
            btnCircleInTreangle.setDisable(true);
            //Необходима для построения отрезков, лучей и прямых.
            //Определена fxml.
            paneShape.getChildren().addAll(newLine, circleFill);//необходима для построения отрезков, лучей и прямых
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
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод onMousseEnteredTangent().
     * Всплывающая подсказка при наведении на кнопку "Добавить касательную к окружности"
     */
    public void onMousseEnteredTangent() {
        model.setTextToolTip("Построить касательную к окружности");
        model.setBtnToolTip(btnTangent);
        model.notifyObservers(TOOL_TIP);
    }

    /**
     * Метод onKeyPressed(KeyEvent key)
     * Предназначен для сброса режима построения и разблокировок кнопок.
     *
     * @param key - нажата кнопка ESC
     */
    public void onKeyPressed(KeyEvent key) {
        if (KeyCode.ESCAPE == key.getCode()) {
            //разблокировать кнопки
            disableButton(false);
        }
        //Вывод информации по коллекции (Alt+T)
        if ((key.isAltDown()) && (KeyCode.T == key.getCode())) {
            model.colTest();
        }
    }

    /**
     * Метод TwofxmlLoader().
     * Предназначен для загрузки шаблона для признаков равенства треугольников
     */
    private void twofxmlLoader() {
        try {
            Parent root1 = FXMLLoader.load(Objects.requireNonNull(EuclidApp.class.getResource("Equality-view.fxml")));
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Признаки равенства треугольников");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Image imAbout = new Image(Objects.requireNonNull(getClass().getResourceAsStream("Images/About.png")));
        root.setBackground(new Background(new BackgroundImage(imAbout, BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
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
        Label label3 = new Label("""
                Разработка ученика 8Б класса\s
                 Носова Алексея\s
                 Программа по лицензии GPU v2.1 \t

                Версия 1.6    2022 г.\s
                \s""");
        label3.setFont(Font.font("Courier", FontWeight.BOLD, 24.0));
        label3.setTextFill(Color.YELLOW);
        label3.setTextAlignment(TextAlignment.CENTER);

        root.getChildren().addAll(label2, label, label1, label3);
        Scene scene = new Scene(root, 864, 520);
        window.setScene(scene);
        window.setTitle("О программе");
        window.show();
    }
}